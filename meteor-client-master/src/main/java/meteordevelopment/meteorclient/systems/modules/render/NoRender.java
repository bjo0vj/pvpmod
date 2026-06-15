package meteordevelopment.meteorclient.systems.modules.render;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
public class NoRender extends Module {
    public NoRender() { super(Categories.Render, "norender", "Stub"); }
    public boolean noArmor() { return false; }
    public boolean noFireworkExplosions() { return false; }
    public boolean noGuiBackground() { return false; }
    public boolean noTotemAnimation() { return false; }
    public boolean noToastNotifications() { return false; }
    public boolean noMapMarkers() { return false; }
    public boolean noTextBackground() { return false; }
    public boolean noLiquidOverlay() { return false; }
    public boolean noFireOverlay() { return false; }
    public boolean noPumpkinOverlay() { return false; }
    public boolean noSpyglassOverlay() { return false; }
    public boolean noPortalOverlay() { return false; }
    public boolean noPowderedSnow() { return false; }
    public boolean noVignette() { return false; }
    public boolean noBossBar() { return false; }
    public boolean noFog() { return false; }
    public boolean noWeather() { return false; }
    public boolean noEnchGlint() { return false; }
    public boolean noBlockBreakParticles() { return false; }
    public boolean noBlockBreakOverlay() { return false; }
    public boolean noSkylightUpdates() { return false; }
    public boolean noFallingBlocks() { return false; }
    public boolean noEatParticles() { return false; }
    public boolean noCrashReports() { return false; }
    public boolean noCrosshair() { return false; }
    public boolean noHeldItemName() { return false; }
    public boolean noPotionIcons() { return false; }
    public boolean noBarrierInvisible() { return false; }
}