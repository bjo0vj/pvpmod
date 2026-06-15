package meteordevelopment.meteorclient.systems.modules.render;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
public class Chams extends Module {
    public Chams() { super(Categories.Render, "chams", "Stub"); }
    public boolean shouldRender(Object entity) { return false; }
    public boolean isShader() { return false; }
}