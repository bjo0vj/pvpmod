package meteordevelopment.meteorclient.systems.modules.render;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
public class Freecam extends Module {
    public Freecam() { super(Categories.Render, "freecam", "Stub"); }
    public float getYaw(float original) { return original; }
    public float getPitch(float original) { return original; }
    public double getX(double original) { return original; }
    public double getY(double original) { return original; }
    public double getZ(double original) { return original; }
}