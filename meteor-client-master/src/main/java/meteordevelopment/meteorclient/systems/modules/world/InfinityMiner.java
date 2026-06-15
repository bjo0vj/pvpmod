package meteordevelopment.meteorclient.systems.modules.world;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import net.minecraft.world.level.block.Block;
import java.util.List;
import java.util.ArrayList;
public class InfinityMiner extends Module {
    public final Setting<List<Block>> targetBlocks = settings.getDefaultGroup().add(new BlockListSetting.Builder().name("target").build());
    public final Setting<List<Block>> repairBlocks = settings.getDefaultGroup().add(new BlockListSetting.Builder().name("repair").build());
    public final Setting<Boolean> logOut = settings.getDefaultGroup().add(new BoolSetting.Builder().name("logout").defaultValue(false).build());
    public final Setting<Boolean> walkHome = settings.getDefaultGroup().add(new BoolSetting.Builder().name("walkhome").defaultValue(false).build());
    public InfinityMiner() { super(Categories.Combat, "infinityminer", "Stub"); }
}