/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package meteordevelopment.meteorclient.systems.modules.combat;

import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.friends.Friends;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.entity.EntityUtils;
import meteordevelopment.meteorclient.utils.entity.SortPriority;
import meteordevelopment.meteorclient.utils.entity.Target;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import meteordevelopment.meteorclient.utils.player.Rotations;
import meteordevelopment.meteorclient.utils.render.RenderUtils;
import meteordevelopment.meteorclient.utils.render.color.Color;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Target Aim — Hệ thống auto-aim cho PvP tốc độ cao (trident / elytra combat).
 *
 * === LOGIC ===
 *
 * Khi module BẬT:
 * 1. Quét tất cả Player trong range (trừ bản thân, bạn bè, spectator, blacklist)
 * 2. Line-of-Sight check: raycast đường thẳng từ mắt mình → mắt target
 *    - Nếu bị chặn bởi block → bỏ qua player này, chọn target khác
 * 3. Sắp xếp theo Target Mode (máu thấp nhất / khoảng cách gần nhất)
 * 4. Chọn target đầu tiên hợp lệ
 * 5. Tự động xoay camera (yaw + pitch) aim thẳng vào target
 * 6. Nếu đang bay elytra (isFallFlying) → client-side rotation → elytra lao thẳng vào target
 * 7. Nếu đang đi bộ bình thường → vẫn auto aim thẳng
 * 8. Render ESP highlight box + tracer line tới target
 *
 * Tóm lại: Khi module ON → LUÔN auto target, bất kể đang bay hay đi bộ.
 */
public class TargetAim extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgBlacklist = settings.createGroup("Blacklist");
    private final SettingGroup sgRender = settings.createGroup("Render");

    // ===== GENERAL =====

    private final Setting<TargetMode> targetMode = sgGeneral.add(new EnumSetting.Builder<TargetMode>()
        .name("target-mode")
        .description("How to prioritize targets: lowest health or closest distance.")
        .defaultValue(TargetMode.Closest)
        .build()
    );

    private final Setting<Double> range = sgGeneral.add(new DoubleSetting.Builder()
        .name("range")
        .description("Maximum range to detect targets.")
        .defaultValue(64)
        .min(1)
        .sliderRange(10, 128)
        .build()
    );

    private final Setting<Boolean> lineOfSight = sgGeneral.add(new BoolSetting.Builder()
        .name("line-of-sight")
        .description("Only target players with clear line of sight (not blocked by blocks). Raycast from your eyes to their eyes.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> elytraRam = sgGeneral.add(new BoolSetting.Builder()
        .name("elytra-ram")
        .description("When flying with elytra, auto-aim camera to fly directly into the target. Client-side rotation for elytra steering.")
        .defaultValue(true)
        .build()
    );

    // ===== BLACKLIST =====

    private final Setting<List<String>> blacklist = sgBlacklist.add(new StringListSetting.Builder()
        .name("blacklisted-players")
        .description("Player names to ignore (will never be targeted).")
        .defaultValue()
        .build()
    );

    // ===== RENDER =====

    private final Setting<Boolean> espHighlight = sgRender.add(new BoolSetting.Builder()
        .name("esp-highlight")
        .description("Draw a highlight glow box around the current target.")
        .defaultValue(true)
        .build()
    );

    private final Setting<SettingColor> highlightColor = sgRender.add(new ColorSetting.Builder()
        .name("highlight-color")
        .description("Color of the ESP highlight box.")
        .defaultValue(new SettingColor(255, 50, 50, 100))
        .visible(espHighlight::get)
        .build()
    );

    private final Setting<SettingColor> highlightLineColor = sgRender.add(new ColorSetting.Builder()
        .name("highlight-line-color")
        .description("Color of the ESP highlight box outline.")
        .defaultValue(new SettingColor(255, 50, 50, 200))
        .visible(espHighlight::get)
        .build()
    );

    private final Setting<Boolean> tracerLine = sgRender.add(new BoolSetting.Builder()
        .name("tracer-line")
        .description("Draw a tracer line from you to the target.")
        .defaultValue(true)
        .build()
    );

    private final Setting<SettingColor> tracerColor = sgRender.add(new ColorSetting.Builder()
        .name("tracer-color")
        .description("Color of the tracer line to target.")
        .defaultValue(new SettingColor(255, 80, 80, 180))
        .visible(tracerLine::get)
        .build()
    );

    // State
    private Entity currentTarget = null;
    private final List<Player> candidates = new ArrayList<>();

    public TargetAim() {
        super(Categories.Combat, "target-aim", "Auto-aims at players for high-speed PvP (trident/elytra). Always targets when ON.");
        this.keybind.set(true, org.lwjgl.glfw.GLFW.GLFW_KEY_MINUS, 0);
    }

    @Override
    public void onActivate() {
        currentTarget = null;
    }

    @Override
    public void onDeactivate() {
        currentTarget = null;
        candidates.clear();
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (mc.player == null || mc.level == null) return;
        if (!mc.player.isAlive() || PlayerUtils.getGameMode() == GameType.SPECTATOR) {
            currentTarget = null;
            return;
        }

        // === Scan & filter candidates ===
        candidates.clear();
        for (Entity entity : mc.level.entitiesForRendering()) {
            if (!(entity instanceof Player player)) continue;
            if (player == mc.player) continue;
            if (player.isDeadOrDying() || !player.isAlive()) continue;
            if (player.isSpectator()) continue;

            // Distance check
            if (!PlayerUtils.isWithin(player, range.get())) continue;

            // Friends check
            if (!Friends.get().shouldAttack(player)) continue;

            // Blacklist check
            String playerName = player.getGameProfile().getName();
            if (isBlacklisted(playerName)) continue;

            // Line of sight check: raycast straight line from my eyes to their eyes
            if (lineOfSight.get() && !hasLineOfSight(player)) continue;

            candidates.add(player);
        }

        // === Select best target ===
        if (candidates.isEmpty()) {
            currentTarget = null;
            return;
        }

        // Sort by target mode
        switch (targetMode.get()) {
            case LowestHealth -> candidates.sort(Comparator.comparingDouble(p ->
                ((LivingEntity) p).getHealth() + ((LivingEntity) p).getAbsorptionAmount()
            ));
            case Closest -> candidates.sort(Comparator.comparingDouble(p ->
                PlayerUtils.squaredDistanceTo(p)
            ));
        }

        currentTarget = candidates.getFirst();

        // === Auto Aim ===
        // Always aim at target, whether walking or flying
        double yaw = Rotations.getYaw(currentTarget);
        double pitch = Rotations.getPitch(currentTarget, Target.Body);

        if (mc.player.isFallFlying() && elytraRam.get()) {
            // Elytra mode: set client-side rotation directly so elytra steers towards target
            // This makes the player fly straight into the target
            mc.player.setYRot((float) yaw);
            mc.player.setXRot((float) pitch);
        } else {
            // Normal mode: use server-side rotation (standard aim)
            Rotations.rotate(yaw, pitch);
        }
    }

    /**
     * Check if there is a clear line of sight from player's eyes to target's eyes.
     * Uses raycast: if the ray hits a block before reaching the target → blocked.
     */
    private boolean hasLineOfSight(Player target) {
        Vec3 eyePos = new Vec3(mc.player.getX(), mc.player.getEyeY(), mc.player.getZ());

        // Check to target's eyes
        Vec3 targetEyes = new Vec3(target.getX(), target.getEyeY(), target.getZ());
        HitResult eyeResult = mc.level.clip(new ClipContext(
            eyePos, targetEyes,
            ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE,
            mc.player
        ));
        if (eyeResult.getType() == HitResult.Type.MISS) return true;

        // Also check to target's body (center)
        Vec3 targetBody = new Vec3(target.getX(), target.getY() + target.getBbHeight() / 2.0, target.getZ());
        HitResult bodyResult = mc.level.clip(new ClipContext(
            eyePos, targetBody,
            ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE,
            mc.player
        ));
        return bodyResult.getType() == HitResult.Type.MISS;
    }

    /**
     * Check if a player name is in the blacklist.
     */
    private boolean isBlacklisted(String name) {
        for (String blacklisted : blacklist.get()) {
            if (blacklisted.equalsIgnoreCase(name)) return true;
        }
        return false;
    }

    // ===== RENDERING =====

    @EventHandler
    private void onRender3D(Render3DEvent event) {
        if (currentTarget == null || !currentTarget.isAlive()) return;

        Entity target = currentTarget;
        float tickDelta = event.tickDelta;

        // Interpolated position
        double x = Mth.lerp(tickDelta, target.xOld, target.getX());
        double y = Mth.lerp(tickDelta, target.yOld, target.getY());
        double z = Mth.lerp(tickDelta, target.zOld, target.getZ());

        // === ESP Highlight Box ===
        if (espHighlight.get()) {
            AABB box = target.getBoundingBox();
            double offX = x - target.getX();
            double offY = y - target.getY();
            double offZ = z - target.getZ();

            Color sideColor = highlightColor.get();
            Color lineColor = highlightLineColor.get();

            event.renderer.box(
                offX + box.minX, offY + box.minY, offZ + box.minZ,
                offX + box.maxX, offY + box.maxY, offZ + box.maxZ,
                sideColor, lineColor, ShapeMode.Both, 0
            );
        }

        // === Tracer Line ===
        if (tracerLine.get()) {
            double height = target.getBbHeight() / 2.0;
            Color color = tracerColor.get();

            event.renderer.line(
                RenderUtils.center.x, RenderUtils.center.y, RenderUtils.center.z,
                x, y + height, z,
                color
            );
        }
    }

    /** Returns the current target for other modules to use */
    public Entity getTarget() {
        return currentTarget;
    }

    @Override
    public String getInfoString() {
        if (currentTarget != null) return EntityUtils.getName(currentTarget);
        return null;
    }

    public enum TargetMode {
        LowestHealth,
        Closest
    }
}
