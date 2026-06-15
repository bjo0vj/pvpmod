package meteordevelopment.meteorclient.commands.commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.commands.CommandSourceStack;
public class WaspCommand extends Command {
    public WaspCommand() { super("wasp", "Wasp command (disabled)."); }
    @Override public void build(LiteralArgumentBuilder<CommandSourceStack> builder) {}
}