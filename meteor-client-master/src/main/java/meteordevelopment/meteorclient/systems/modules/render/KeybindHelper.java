/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package meteordevelopment.meteorclient.systems.modules.render;

import meteordevelopment.meteorclient.events.render.Render2DEvent;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.render.color.Color;
import meteordevelopment.orbit.EventHandler;

import static meteordevelopment.meteorclient.MeteorClient.mc;

/**
 * Keybind Helper — Hiện GUI overlay hiển thị tất cả phím tắt khi bật.
 * Phím toggle: \ (Backslash)
 * Khi bật: hiển thị bảng phím tắt trên màn hình.
 * Khi tắt: ẩn bảng.
 */
public class KeybindHelper extends Module {

    // Colors
    private static final Color BG_COLOR = new Color(0, 0, 0, 170);
    private static final Color TITLE_COLOR = new Color(255, 85, 85, 255);
    private static final Color KEY_COLOR = new Color(255, 255, 85, 255);
    private static final Color DESC_COLOR = new Color(255, 255, 255, 255);
    private static final Color SEPARATOR_COLOR = new Color(100, 100, 100, 200);

    // Keybind entries
    private static final String[][] KEYBINDS = {
        {"\\",  "Keybind Helper (bảng này)"},
        {"=",   "Kill Aura (đánh player + entity)"},
        {"-",   "Target Aim (auto-aim PvP elytra/trident)"},
        {"'",   "Auto Armor (tự mặc giáp)"},
        {",",   "Flight (bật/tắt bay)"},
        {".",   "ESP + Tracers (nhìn player xuyên tường)"},
    };

    public KeybindHelper() {
        super(Categories.Render, "keybind-helper", "Shows a helper overlay with all keybind shortcuts. Press \\ to toggle.");
        this.keybind.set(true, org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSLASH, 0);
    }

    @EventHandler
    private void onRender2D(Render2DEvent event) {
        if (mc.player == null || mc.level == null) return;

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        // Box dimensions
        int boxWidth = 250;
        int lineHeight = 14;
        int padding = 8;
        int titleHeight = 18;
        int boxHeight = titleHeight + padding + (KEYBINDS.length * lineHeight) + padding + lineHeight + padding;

        // Center the box
        int boxX = (screenWidth - boxWidth) / 2;
        int boxY = (screenHeight - boxHeight) / 2;

        // Draw background
        event.guiGraphics.fill(boxX, boxY, boxX + boxWidth, boxY + boxHeight, BG_COLOR.getPacked());

        // Draw border
        event.guiGraphics.fill(boxX, boxY, boxX + boxWidth, boxY + 2, TITLE_COLOR.getPacked());
        event.guiGraphics.fill(boxX, boxY + boxHeight - 2, boxX + boxWidth, boxY + boxHeight, TITLE_COLOR.getPacked());
        event.guiGraphics.fill(boxX, boxY, boxX + 2, boxY + boxHeight, TITLE_COLOR.getPacked());
        event.guiGraphics.fill(boxX + boxWidth - 2, boxY, boxX + boxWidth, boxY + boxHeight, TITLE_COLOR.getPacked());

        // Draw title
        String title = "⌨ KEYBIND HELPER";
        int titleWidth = mc.font.width(title);
        event.guiGraphics.drawString(mc.font, title, boxX + (boxWidth - titleWidth) / 2, boxY + 6, TITLE_COLOR.getPacked());

        // Draw separator line
        int sepY = boxY + titleHeight + 2;
        event.guiGraphics.fill(boxX + padding, sepY, boxX + boxWidth - padding, sepY + 1, SEPARATOR_COLOR.getPacked());

        // Draw keybind entries
        int entryY = sepY + padding;
        for (String[] entry : KEYBINDS) {
            String keyText = "[" + entry[0] + "]";
            String descText = " " + entry[1];

            event.guiGraphics.drawString(mc.font, keyText, boxX + padding, entryY, KEY_COLOR.getPacked());
            event.guiGraphics.drawString(mc.font, descText, boxX + padding + mc.font.width(keyText), entryY, DESC_COLOR.getPacked());

            entryY += lineHeight;
        }

        // Draw footer
        entryY += 2;
        event.guiGraphics.fill(boxX + padding, entryY, boxX + boxWidth - padding, entryY + 1, SEPARATOR_COLOR.getPacked());
        entryY += 5;
        String footer = "Bấm \\ để ẩn bảng này";
        int footerWidth = mc.font.width(footer);
        event.guiGraphics.drawString(mc.font, footer, boxX + (boxWidth - footerWidth) / 2, entryY, SEPARATOR_COLOR.getPacked());
    }
}
