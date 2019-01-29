import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Assault extends JFrame {
    private static int LENGTH = 1500;
    private static int HEIGHT = 700;
    private static String TITLE = "Attack Simulation";
    private List<Image> icons;

    private Assault() {
        icons = new ArrayList<>();
        init();
    }

    private void init() {
        setTitle(TITLE);
        addJFrameIcon();
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

    private void addJFrameIcon() {
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/16x16.png")));
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/32x32.png")));
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/40x40.png")));
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/64x64.png")));
        icons.add(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/128x128.png")));
        setIconImages(icons);
    }

    public static void main(String[] args) {
        new Assault();
    }
}
