/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package meteordevelopment.meteorclient.systems.modules.combat;

import meteordevelopment.meteorclient.events.meteor.MouseClickEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.entity.EntityUtils;
import meteordevelopment.meteorclient.utils.entity.SortPriority;
import meteordevelopment.meteorclient.utils.entity.Target;
import meteordevelopment.meteorclient.utils.entity.TargetUtils;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import meteordevelopment.meteorclient.utils.player.Rotations;
import meteordevelopment.meteorclient.utils.world.TickRate;
import meteordevelopment.meteorclient.utils.misc.input.KeyAction;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Kill Aura Mob - Chỉ tấn công mob (không tấn công player).
 *
 * === LOGIC TARGET / AIM LOCK ===
 *
 * 1. Khi module BẬT (phím +), nó sẽ quét tất cả mob trong tầm range.
 *
 * 2. Chế độ tấn công (Attack Mode):
 *    - Auto:  Tự động đánh mob gần nhất/máu thấp nhất liên tục, không cần click.
 *    - Click: Click chuột trái 1 lần → khóa tâm (lock) vào mob đang nhìn.
 *             Giữ chuột trái → đánh liên tục mob đó cho tới khi chết/out range.
 *             Nhả chuột → dừng đánh, nhưng vẫn giữ lock.
 *             Click chuột trái vào mob khác → đổi lock sang mob mới.
 *             Nếu mob locked chết hoặc ra khỏi range → tự động bỏ lock.
 *
 * 3. Target Lock (khóa tâm):
 *    - Khi đã lock 1 mob, camera sẽ tự xoay (aim) về phía mob đó (nếu rotation != None).
 *    - Mob bị lock sẽ hiện tên trên info string.
 *    - Lock chỉ mất khi: mob chết, mob ra ngoài range, hoặc bạn click vào mob mới.
 */
public class KillAuraMob extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgTargeting = settings.createGroup("Targeting");
    private final SettingGroup sgTiming = settings.createGroup("Timing");

    // General

    private final Setting<AttackMode> attackMode = sgGeneral.add(new EnumSetting.Builder<AttackMode>()
        .name("attack-mode")
        .description("Auto: attacks automatically. Click: click/hold left mouse to attack locked target.")
        .defaultValue(AttackMode.Click)
        .build()
    );

    private final Setting<RotationMode> rotation = sgGeneral.add(new EnumSetting.Builder<RotationMode>()
        .name("rotate")
        .description("Determines when you should rotate towards the target. Always = auto aim lock on target.")
        .defaultValue(RotationMode.Always)
        .build()
    );

    // Targeting

    private final Setting<Set<EntityType<?>>> entities = sgTargeting.add(new EntityTypeListSetting.Builder()
        .name("mob-entities")
        .description("Select which mob types to attack. Only mobs are shown, no players.")
        .filter(type -> {
            if (!EntityUtils.isAttackable(type)) return false;
            if (type == EntityType.PLAYER) return false;
            return true;
        })
        .defaultValue(EntityType.ZOMBIE, EntityType.SKELETON, EntityType.CREEPER, EntityType.SPIDER)
        .build()
    );

    private final Setting<SortPriority> priority = sgTargeting.add(new EnumSetting.Builder<SortPriority>()
        .name("priority")
        .description("How to filter targets within range (closest, lowest health, etc).")
        .defaultValue(SortPriority.LowestHealth)
        .build()
    );

    private final Setting<Integer> maxTargets = sgTargeting.add(new IntSetting.Builder()
        .name("max-targets")
        .description("How many mobs to target at once.")
        .defaultValue(1)
        .min(1)
        .sliderRange(1, 5)
        .build()
    );

    private final Setting<Double> range = sgTargeting.add(new DoubleSetting.Builder()
        .name("range")
        .description("The maximum range the mob can be to attack it.")
        .defaultValue(4.5)
        .min(0)
        .sliderMax(6)
        .build()
    );

    private final Setting<Double> wallsRange = sgTargeting.add(new DoubleSetting.Builder()
        .name("walls-range")
        .description("The maximum range the mob can be attacked through walls.")
        .defaultValue(3.5)
        .min(0)
        .sliderMax(6)
        .build()
    );

    private final Setting<Boolean> ignoreNamed = sgTargeting.add(new BoolSetting.Builder()
        .name("ignore-named")
        .description("Whether or not to attack mobs with a name tag.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> ignoreTamed = sgTargeting.add(new BoolSetting.Builder()
        .name("ignore-tamed")
        .description("Will avoid attacking mobs you tamed.")
        .defaultValue(true)
        .build()
    );

    // Timing

    private final Setting<Boolean> pauseOnLag = sgTiming.add(new BoolSetting.Builder()
        .name("pause-on-lag")
        .description("Pauses if the server is lagging.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> pauseOnUse = sgTiming.add(new BoolSetting.Builder()
        .name("pause-on-use")
        .description("Does not attack while using an item.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> tpsSync = sgTiming.add(new BoolSetting.Builder()
        .name("TPS-sync")
        .description("Tries to sync attack delay with the server's TPS.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> customDelay = sgTiming.add(new BoolSetting.Builder()
        .name("custom-delay")
        .description("Use a custom delay instead of the vanilla cooldown.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Integer> hitDelay = sgTiming.add(new IntSetting.Builder()
        .name("hit-delay")
        .description("How fast you hit the mob in ticks.")
        .defaultValue(11)
        .min(0)
        .sliderMax(60)
        .visible(customDelay::get)
        .build()
    );

    private final Setting<Integer> switchDelay = sgTiming.add(new IntSetting.Builder()
        .name("switch-delay")
        .description("How many ticks to wait before hitting a mob after switching hotbar slots.")
        .defaultValue(0)
        .min(0)
        .sliderMax(10)
        .build()
    );

    private final List<Entity> targets = new ArrayList<>();
    private int switchTimer, hitTimer;

    // Target lock state
    private Entity lockedTarget = null;
    private boolean mouseHeld = false;

    public KillAuraMob() {
        super(Categories.Combat, "kill-aura-mob", "Attacks selected mob types around you. Click/hold left mouse to lock & attack a mob.");
    }

    @Override
    public void onActivate() {
        lockedTarget = null;
        mouseHeld = false;
    }

    @Override
    public void onDeactivate() {
        targets.clear();
        lockedTarget = null;
        mouseHeld = false;
    }

    /**
     * Handle mouse click events for target locking in Click mode.
     * - Left click (Press): Look at crosshair → if it's a valid mob, LOCK onto it. Set mouseHeld = true.
     * - Left click (Release): Set mouseHeld = false (stop attacking but keep lock).
     */
    @EventHandler
    private void onMouseClick(MouseClickEvent event) {
        if (attackMode.get() != AttackMode.Click) return;
        if (mc.player == null || mc.level == null) return;

        // Only handle left mouse button (button 0)
        if (event.button() != 0) return;

        if (event.action == KeyAction.Press) {
            mouseHeld = true;

            // Check if crosshair is pointing at a valid mob → lock onto it
            Entity crosshairEntity = mc.crosshairPickEntity;
            if (crosshairEntity != null && entityCheck(crosshairEntity)) {
                lockedTarget = crosshairEntity;
            }
        } else if (event.action == KeyAction.Release) {
            mouseHeld = false;
        }
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (!mc.player.isAlive() || PlayerUtils.getGameMode() == GameType.SPECTATOR) return;
        if (pauseOnUse.get() && (mc.gameMode.isDestroying() || mc.player.isUsingItem())) return;
        if (TickRate.INSTANCE.getTimeSinceLastTick() >= 1f && pauseOnLag.get()) return;

        if (attackMode.get() == AttackMode.Click) {
            // === CLICK MODE: Target Lock Logic ===
            //
            // 1. Validate locked target: if dead, out of range, or invalid → unlock
            // 2. If locked & mouse held (click or hold) → attack locked target
            // 3. Rotation/aim always tracks the locked target (camera auto-aims)

            // Validate locked target
            if (lockedTarget != null) {
                if (!lockedTarget.isAlive()
                    || (lockedTarget instanceof LivingEntity le && le.isDeadOrDying())
                    || !entityCheck(lockedTarget)) {
                    lockedTarget = null;
                }
            }

            targets.clear();

            if (lockedTarget != null) {
                targets.add(lockedTarget);

                // Rotate/aim towards locked target (auto aim-lock)
                if (rotation.get() == RotationMode.Always) {
                    Rotations.rotate(Rotations.getYaw(lockedTarget), Rotations.getPitch(lockedTarget, Target.Body));
                }

                // Attack only when mouse is held (click or hold)
                if (mouseHeld && delayCheck()) {
                    attack(lockedTarget);
                }
            }
        } else {
            // === AUTO MODE: Original automatic targeting ===
            targets.clear();
            TargetUtils.getList(targets, this::entityCheck, priority.get(), maxTargets.get());

            if (targets.isEmpty()) return;

            Entity primary = targets.getFirst();

            if (rotation.get() == RotationMode.Always) {
                Rotations.rotate(Rotations.getYaw(primary), Rotations.getPitch(primary, Target.Body));
            }

            if (delayCheck()) targets.forEach(this::attack);
        }
    }

    @EventHandler
    private void onSendPacket(PacketEvent.Send event) {
        if (event.packet instanceof ServerboundSetCarriedItemPacket) {
            switchTimer = switchDelay.get();
        }
    }

    private boolean entityCheck(Entity entity) {
        if (entity.equals(mc.player) || entity.equals(mc.getCameraEntity())) return false;
        if ((entity instanceof LivingEntity livingEntity && livingEntity.isDeadOrDying()) || !entity.isAlive())
            return false;

        // Skip players — this module is for mobs only
        if (entity instanceof Player) return false;

        AABB hitbox = entity.getBoundingBox();
        if (!PlayerUtils.isWithin(
            Mth.clamp(mc.player.getX(), hitbox.minX, hitbox.maxX),
            Mth.clamp(mc.player.getY(), hitbox.minY, hitbox.maxY),
            Mth.clamp(mc.player.getZ(), hitbox.minZ, hitbox.maxZ),
            range.get()
        )) return false;

        if (!entities.get().contains(entity.getType())) return false;
        if (ignoreNamed.get() && entity.hasCustomName()) return false;
        if (!PlayerUtils.canSeeEntity(entity) && !PlayerUtils.isWithin(entity, wallsRange.get())) return false;
        if (ignoreTamed.get()) {
            if (entity instanceof OwnableEntity tameable
                && tameable.getOwner() != null
                && tameable.getOwner().equals(mc.player)
            ) return false;
        }

        return true;
    }

    private boolean delayCheck() {
        if (switchTimer > 0) {
            switchTimer--;
            return false;
        }

        float delay = (customDelay.get()) ? hitDelay.get() : 0.5f;
        if (tpsSync.get()) delay /= (TickRate.INSTANCE.getTickRate() / 20);

        if (customDelay.get()) {
            if (hitTimer < delay) {
                hitTimer++;
                return false;
            } else return true;
        } else return mc.player.getAttackStrengthScale(delay) >= 1;
    }

    private void attack(Entity target) {
        if (rotation.get() == RotationMode.OnHit)
            Rotations.rotate(Rotations.getYaw(target), Rotations.getPitch(target, Target.Body));

        mc.gameMode.attack(mc.player, target);
        mc.player.swing(InteractionHand.MAIN_HAND);

        hitTimer = 0;
    }

    /** Returns the currently locked/primary target */
    public Entity getTarget() {
        if (lockedTarget != null) return lockedTarget;
        if (!targets.isEmpty()) return targets.getFirst();
        return null;
    }

    @Override
    public String getInfoString() {
        Entity target = getTarget();
        if (target != null) return EntityUtils.getName(target);
        return null;
    }

    public enum AttackMode {
        Auto,
        Click
    }

    public enum RotationMode {
        Always,
        OnHit,
        None
    }
}
