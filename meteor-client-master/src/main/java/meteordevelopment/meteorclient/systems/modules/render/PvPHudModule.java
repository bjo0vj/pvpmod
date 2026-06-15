/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package meteordevelopment.meteorclient.systems.modules.render;

import meteordevelopment.meteorclient.events.meteor.ActiveModulesChangedEvent;
import meteordevelopment.meteorclient.events.meteor.MouseButtonEvent;
import meteordevelopment.meteorclient.events.render.Render2DEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.network.MeteorExecutor;
import meteordevelopment.meteorclient.utils.render.color.Color;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class PvPHudModule extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> cps = sgGeneral.add(new BoolSetting.Builder()
        .name("cps")
        .description("Displays your Clicks Per Second.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> ping = sgGeneral.add(new BoolSetting.Builder()
        .name("ping")
        .description("Displays your ping.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> combo = sgGeneral.add(new BoolSetting.Builder()
        .name("combo")
        .description("Displays your current combo.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> toggleMsgs = sgGeneral.add(new BoolSetting.Builder()
        .name("toggle-messages")
        .description("Displays on-screen messages when toggling modules.")
        .defaultValue(true)
        .build()
    );

    // Toggle Messages state
    private static class ToggleMessage {
        String text;
        long expireTime;
        Color color;

        ToggleMessage(String text, long expireTime, Color color) {
            this.text = text;
            this.expireTime = expireTime;
            this.color = color;
        }
    }
    private final List<ToggleMessage> activeMessages = new ArrayList<>();

    // CPS state
    private final List<Long> clicks = new ArrayList<>();

    // Combo state
    private int comboCount = 0;
    private long lastHitTime = 0;

    public PvPHudModule() {
        super(Categories.Render, "pvp-hud", "Displays PvP related info on screen.");
    }

    @Override
    public void onActivate() {
        activeMessages.clear();
        clicks.clear();
        comboCount = 0;
    }

    @EventHandler
    private void onMouseButton(MouseButtonEvent event) {
        if (event.action == meteordevelopment.meteorclient.utils.misc.input.KeyAction.Press) {
            if (event.button == 0 || event.button == 1) { // Left or right click
                clicks.add(System.currentTimeMillis());
            }
        }
    }

    @EventHandler
    private void onAttack(meteordevelopment.meteorclient.events.entity.player.AttackEntityEvent event) {
        long now = System.currentTimeMillis();
        if (now - lastHitTime > 2000) {
            comboCount = 1;
        } else {
            comboCount++;
        }
        lastHitTime = now;
    }

    public void addToggleMessage(String moduleName, boolean active) {
        if (!isActive() || !toggleMsgs.get()) return;
        String text = "[" + moduleName + " " + (active ? "ON" : "OFF") + "]";
        Color color = active ? new Color(0, 255, 0) : new Color(255, 0, 0);
        activeMessages.add(new ToggleMessage(text, System.currentTimeMillis() + 2000, color)); // 2 seconds
    }

    @EventHandler
    private void onRender2D(Render2DEvent event) {
        // Draw Toggle Messages
        int yOffset = 10;
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        long now = System.currentTimeMillis();
        Iterator<ToggleMessage> it = activeMessages.iterator();
        while (it.hasNext()) {
            ToggleMessage msg = it.next();
            if (now > msg.expireTime) {
                it.remove();
                continue;
            }

            int width = mc.font.width(msg.text);
            event.guiGraphics.drawString(mc.font, msg.text, (screenWidth - width) / 2, screenHeight / 2 + yOffset, msg.color.getPacked());
            yOffset += 12;
        }

        // Draw Stats
        int xOffset = 5;
        int statsYOffset = 5;

        if (cps.get()) {
            clicks.removeIf(time -> System.currentTimeMillis() - time > 1000);
            String cpsText = "CPS: " + clicks.size();
            event.guiGraphics.drawString(mc.font, cpsText, xOffset, statsYOffset, Color.WHITE.getPacked());
            statsYOffset += 12;
        }

        if (ping.get()) {
            int p = -1;
            if (mc.getSingleplayerServer() != null) p = 0;
            else if (mc.getConnection() != null && mc.player != null) {
                var entry = mc.getConnection().getPlayerInfo(mc.player.getUUID());
                if (entry != null) p = entry.getLatency();
            }
            String pingText = "Ping: " + (p == -1 ? "0" : p) + "ms";
            event.guiGraphics.drawString(mc.font, pingText, xOffset, statsYOffset, Color.WHITE.getPacked());
            statsYOffset += 12;
        }

        if (combo.get()) {
            if (now - lastHitTime > 2000) comboCount = 0;
            String comboText = "Combo: x" + comboCount;
            event.guiGraphics.drawString(mc.font, comboText, xOffset, statsYOffset, Color.WHITE.getPacked());
            statsYOffset += 12;
        }
    }
}
