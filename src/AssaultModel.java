import java.awt.Color;
import java.awt.Point;

public class AssaultModel {
    public final static int GAME_LENGTH = 1484;
    public final static int GAME_HEIGHT = 660;
    public final static int PLATFORM_Y = 590;
    public final static Color AQUA = new Color(127, 255, 212);
    public final static Color LIGHT_GRAY = new Color(51, 51, 51);
    public final static Color DARK_GRAY = new Color(45, 45, 45);
    private int counter;
    private boolean paused;
    private Player p;

    public AssaultModel(Player p) {
        this.counter = 0;
        this.paused = false;
        this.p = p;
    }

    public void updateCounter() {
        this.counter++;
    }

    public int getTime() {
        return this.counter;
    }

    public void setPaused(boolean b) {
        this.paused = b;
    }

    public boolean isPaused() {
        return this.paused;
    }

    public Point getPlayerPos() {
        return new Point(p.getXOrd(), p.getYOrd());
    }
}
