package meteordevelopment.meteorclient.systems.modules.render;

import meteordevelopment.meteorclient.events.render.Render2DEvent;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.render.color.Color;
import meteordevelopment.orbit.EventHandler;
import static meteordevelopment.meteorclient.MeteorClient.mc;

public class KeybindHelper extends Module {
    private static final Color BG_COLOR = new Color(0, 0, 0, 170);
    private static final Color TITLE_COLOR = new Color(255, 85, 85, 255);
    private static final Color KEY_COLOR = new Color(255, 255, 85, 255);
    private static final Color DESC_COLOR = new Color(255, 255, 255, 255);
    private static final Color SEP_COLOR = new Color(100, 100, 100, 200);

    private static final String[][] KEYBINDS = {
        {"\\\\", "Keybind Helper"}, {"=", "Kill Aura"}, {"-", "Target Aim"},
        {"'", "Auto Armor"}, {",", "Flight"}, {".", "ESP + Tracers (player)"},
    };

    public KeybindHelper() {
        super(Categories.Render, "keybind-helper", "Shows keybind shortcuts. Press \\\\ to toggle.");
        this.keybind.set(true, org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSLASH, 0);
    }

    @EventHandler
    private void onRender2D(Render2DEvent event) {
        if (mc.player == null) return;
        int sw = event.screenWidth, sh = event.screenHeight;
        int bw = 250, lh = 14, pad = 8, th = 18;
        int bh = th + pad + (KEYBINDS.length * lh) + pad + lh + pad;
        int bx = (sw - bw) / 2, by = (sh - bh) / 2;

        event.graphics.fill(bx, by, bx + bw, by + bh, BG_COLOR.getPacked());
        event.graphics.fill(bx, by, bx + bw, by + 2, TITLE_COLOR.getPacked());
        event.graphics.fill(bx, by + bh - 2, bx + bw, by + bh, TITLE_COLOR.getPacked());
        event.graphics.fill(bx, by, bx + 2, by + bh, TITLE_COLOR.getPacked());
        event.graphics.fill(bx + bw - 2, by, bx + bw, by + bh, TITLE_COLOR.getPacked());

        String title = "KEYBIND HELPER";
        event.graphics.text(mc.font, title, bx + (bw - mc.font.width(title)) / 2, by + 6, TITLE_COLOR.getPacked());

        int sepY = by + th + 2;
        event.graphics.fill(bx + pad, sepY, bx + bw - pad, sepY + 1, SEP_COLOR.getPacked());

        int ey = sepY + pad;
        for (String[] e : KEYBINDS) {
            String k = "[" + e[0] + "]", d = " " + e[1];
            event.graphics.text(mc.font, k, bx + pad, ey, KEY_COLOR.getPacked());
            event.graphics.text(mc.font, d, bx + pad + mc.font.width(k), ey, DESC_COLOR.getPacked());
            ey += lh;
        }
        ey += 2;
        event.graphics.fill(bx + pad, ey, bx + bw - pad, ey + 1, SEP_COLOR.getPacked());
        ey += 5;
        String footer = "Press \\\\ to hide";
        event.graphics.text(mc.font, footer, bx + (bw - mc.font.width(footer)) / 2, ey, SEP_COLOR.getPacked());
    }
}