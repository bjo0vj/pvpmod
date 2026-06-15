package meteordevelopment.meteorclient.systems.modules.player;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
public class FakePlayer extends Module {
    public final Setting<String> name = settings.getDefaultGroup().add(new StringSetting.Builder().name("name").defaultValue("FakePlayer").build());
    public final Setting<Integer> health = settings.getDefaultGroup().add(new IntSetting.Builder().name("health").defaultValue(20).build());
    public final Setting<Boolean> copyInv = settings.getDefaultGroup().add(new BoolSetting.Builder().name("copy-inv").defaultValue(true).build());
    public FakePlayer() { super(Categories.Combat, "fakeplayer", "Stub"); }
}