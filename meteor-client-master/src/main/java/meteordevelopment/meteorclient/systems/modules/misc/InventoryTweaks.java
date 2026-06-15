package meteordevelopment.meteorclient.systems.modules.misc;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import net.minecraft.world.inventory.AbstractContainerMenu;
public class InventoryTweaks extends Module {
    public InventoryTweaks() { super(Categories.Combat, "inventorytweaks", "Stub"); }
    public boolean showButtons() { return false; }
    public boolean canSteal(AbstractContainerMenu menu) { return false; }
    public void steal(AbstractContainerMenu menu) {}
    public void dump(AbstractContainerMenu menu) {}
    public boolean mouseDragItemMove() { return false; }
}