package meteordevelopment.meteorclient.systems.modules.movement;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
public class Velocity extends Module {
    public Velocity() { super(Categories.Movement, "velocity", "Stub"); }
    public boolean blocks() { return false; }
    public double getHorizontal() { return 1.0; }
    public double getVertical() { return 1.0; }
}