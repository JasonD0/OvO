import java.awt.Dimension;
import javax.swing.JFrame;

public class Assault extends JFrame {
    private static int LENGTH = 1500;
    private static int HEIGHT = 700;
    private static String TITLE = "Attack Simulation";

    private Assault() {
        init();
    }

    private void init() {
        setTitle(TITLE);
        setPreferredSize(new Dimension(LENGTH, HEIGHT));
        setMaximumSize(new Dimension(LENGTH, HEIGHT));
        setMinimumSize(new Dimension(LENGTH, HEIGHT));
        setFocusable(true);
        add(new AssaultControl());
        pack();
        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new Assault();
    }
}
