package meteordevelopment.meteorclient.commands.commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.commands.CommandSourceStack;
public class FakePlayerCommand extends Command {
    public FakePlayerCommand() { super("fake-player", "FakePlayer command (disabled)."); }
    @Override public void build(LiteralArgumentBuilder<CommandSourceStack> builder) {}
}