package meteordevelopment.meteorclient.systems.modules.render.marker;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import java.util.ArrayList;
import java.util.List;
public class Marker extends Module {
    public final List<BaseMarker> markers = new ArrayList<>();
    public Marker() { super(Categories.Render, "marker", "Stub"); }
}