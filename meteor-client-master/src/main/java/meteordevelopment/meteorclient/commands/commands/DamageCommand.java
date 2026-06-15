package meteordevelopment.meteorclient.commands.commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.commands.CommandSourceStack;
public class DamageCommand extends Command {
    public DamageCommand() { super("damage", "Damage command (disabled)."); }
    @Override public void build(LiteralArgumentBuilder<CommandSourceStack> builder) {}
}