package meteordevelopment.meteorclient.systems.modules.render;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
public class Fullbright extends Module {
    public Fullbright() { super(Categories.Render, "fullbright", "Stub"); }
    public int getLuminance() { return 0; }
    public boolean getGamma() { return false; }
}