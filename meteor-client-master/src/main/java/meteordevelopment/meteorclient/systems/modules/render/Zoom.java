package meteordevelopment.meteorclient.systems.modules.render;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
public class Zoom extends Module {
    public Zoom() { super(Categories.Render, "zoom", "Stub"); }
    public double getFov(double fov) { return fov; }
}