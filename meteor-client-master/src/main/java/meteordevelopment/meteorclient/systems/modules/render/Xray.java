package meteordevelopment.meteorclient.systems.modules.render;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
public class Xray extends Module {
    public Xray() { super(Categories.Render, "xray", "Stub"); }
    public boolean isBlocked(Object block, Object pos) { return false; }
}