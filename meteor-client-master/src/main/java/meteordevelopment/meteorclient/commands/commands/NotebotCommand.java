package meteordevelopment.meteorclient.commands.commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.commands.CommandSourceStack;
public class NotebotCommand extends Command {
    public NotebotCommand() { super("notebot", "Notebot command (disabled)."); }
    @Override public void build(LiteralArgumentBuilder<CommandSourceStack> builder) {}
}