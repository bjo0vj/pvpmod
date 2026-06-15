package meteordevelopment.meteorclient.systems.modules.misc;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.misc.swarm.SwarmConnection;
public class Swarm extends Module {
    public SwarmConnection host;
    public Swarm() { super(Categories.Combat, "swarm", "Stub"); }
    public boolean isHost() { return false; }
    public boolean isWorker() { return false; }
    public void close() {}
    public void start() {}
}