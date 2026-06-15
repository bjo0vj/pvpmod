package meteordevelopment.meteorclient.systems.modules.world;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
public class Timer extends Module {
    public Timer() { super(Categories.Combat, "timer", "Stub"); }
    public double getMultiplier() { return 1.0; }
}