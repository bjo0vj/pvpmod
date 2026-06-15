package meteordevelopment.meteorclient.systems.modules.render;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
public class Chams extends Module {
    public final Setting<Boolean> crystals = settings.getDefaultGroup().add(new BoolSetting.Builder().name("crystals").defaultValue(false).build());
    public final Setting<Double> crystalsBounce = settings.getDefaultGroup().add(new DoubleSetting.Builder().name("bounce").defaultValue(1.0).build());
    public final Setting<Double> crystalsRotationSpeed = settings.getDefaultGroup().add(new DoubleSetting.Builder().name("rotation-speed").defaultValue(1.0).build());
    public Chams() { super(Categories.Render, "chams", "Stub"); }
    public boolean shouldRender(Object entity) { return false; }
    public boolean isShader() { return false; }
}