package meteordevelopment.meteorclient.systems.modules.movement;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import net.minecraft.world.entity.player.Player;
public class AutoWasp extends Module {
    public Player target;
    public AutoWasp() { super(Categories.Movement, "autowasp", "Stub"); }
}