package meteordevelopment.meteorclient.systems.modules.player;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
public class NameProtect extends Module {
    public NameProtect() { super(Categories.Combat, "nameprotect", "Stub"); }
    public String getName(String original) { return original; }
}