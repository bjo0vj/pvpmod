package meteordevelopment.meteorclient.systems.modules.misc;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import java.io.File;
public class Notebot extends Module {
    public Notebot() { super(Categories.Combat, "notebot", "Stub"); }
    public void playRandomSong() {}
    public void loadSong(File f) {}
    public void previewSong(File f) {}
    public void pause() {}
    public void stop() {}
    public String getStatus() { return "disabled"; }
}