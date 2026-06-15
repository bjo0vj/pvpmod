package meteordevelopment.meteorclient.commands.commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.commands.CommandSourceStack;
public class SwarmCommand extends Command {
    public SwarmCommand() { super("swarm", "Swarm command (disabled)."); }
    @Override public void build(LiteralArgumentBuilder<CommandSourceStack> builder) {}
}