import java.awt.Color;

public class AssaultModel {
    public static int GAME_LENGTH = 1383;
    public static int GAME_HEIGHT = 560;
    public static int PLATFORM_Y = 490;
    public final static Color AQUA = new Color(127, 255, 212);
    public final static Color LIGHT_GRAY = new Color(51, 51, 51);
    public final static Color DARK_GRAY = new Color(45, 45, 45);
    private int counter;

    public AssaultModel() {
        this.counter = 0;
    }

    public void updateCounter() {
        this.counter++;
    }

    public int getTime() {
        return this.counter;
    }
}
