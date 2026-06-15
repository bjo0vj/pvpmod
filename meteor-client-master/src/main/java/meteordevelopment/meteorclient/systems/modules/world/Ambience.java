package meteordevelopment.meteorclient.systems.modules.world;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
public class Ambience extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    public final Setting<Boolean> changeLightningColor = sgGeneral.add(new BoolSetting.Builder().name("lightning-color").defaultValue(false).build());
    public final Setting<SettingColor> lightningColor = sgGeneral.add(new ColorSetting.Builder().name("lightning").defaultValue(new SettingColor(255, 255, 255)).build());
    public final Setting<Boolean> customSkyColor = sgGeneral.add(new BoolSetting.Builder().name("custom-sky").defaultValue(false).build());
    public final Setting<SettingColor> skyColor = sgGeneral.add(new ColorSetting.Builder().name("sky").defaultValue(new SettingColor(135, 206, 235)).build());
    public final Setting<Boolean> customFoliageColor = sgGeneral.add(new BoolSetting.Builder().name("custom-foliage").defaultValue(false).build());
    public final Setting<SettingColor> foliageColor = sgGeneral.add(new ColorSetting.Builder().name("foliage").defaultValue(new SettingColor(76, 153, 0)).build());
    public final Setting<Boolean> customGrassColor = sgGeneral.add(new BoolSetting.Builder().name("custom-grass").defaultValue(false).build());
    public final Setting<SettingColor> grassColor = sgGeneral.add(new ColorSetting.Builder().name("grass").defaultValue(new SettingColor(76, 153, 0)).build());
    public final Setting<Boolean> customWaterColor = sgGeneral.add(new BoolSetting.Builder().name("custom-water").defaultValue(false).build());
    public final Setting<SettingColor> waterColor = sgGeneral.add(new ColorSetting.Builder().name("water").defaultValue(new SettingColor(63, 118, 228)).build());
    public final Setting<Boolean> customLavaColor = sgGeneral.add(new BoolSetting.Builder().name("custom-lava").defaultValue(false).build());
    public final Setting<SettingColor> lavaColor = sgGeneral.add(new ColorSetting.Builder().name("lava").defaultValue(new SettingColor(207, 90, 12)).build());
    public Ambience() { super(Categories.Combat, "ambience", "Stub"); }
}