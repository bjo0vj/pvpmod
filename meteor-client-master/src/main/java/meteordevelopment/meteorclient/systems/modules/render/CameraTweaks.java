package meteordevelopment.meteorclient.systems.modules.render;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
public class CameraTweaks extends Module {
    public CameraTweaks() { super(Categories.Render, "cameratweaks", "Stub"); }
    public boolean clip() { return false; }
    public double getDistance() { return 4.0; }
}