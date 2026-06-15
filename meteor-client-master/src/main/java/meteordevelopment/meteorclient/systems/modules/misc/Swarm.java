package meteordevelopment.meteorclient.systems.modules.misc;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.misc.swarm.SwarmConnection;
import meteordevelopment.meteorclient.systems.modules.misc.swarm.SwarmWorker;
public class Swarm extends Module {
    public enum Mode { Host, Worker }
    public final Setting<Mode> mode = settings.getDefaultGroup().add(new EnumSetting.Builder<Mode>().name("mode").defaultValue(Mode.Host).build());
    public SwarmConnection host;
    public SwarmWorker worker;
    public Swarm() { super(Categories.Combat, "swarm", "Stub"); }
    public boolean isHost() { return false; }
    public boolean isWorker() { return false; }
    public void close() {}
    public void start() {}
}