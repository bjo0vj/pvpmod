package meteordevelopment.meteorclient.systems.modules.render;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
public class FreeLook extends Module {
    public FreeLook() { super(Categories.Render, "freelook", "Stub"); }
    public float getYaw(float original) { return original; }
    public float getPitch(float original) { return original; }
}