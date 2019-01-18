import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import javax.swing.Timer;

public class AssaultControl extends JPanel implements Runnable, KeyListener {
    private Thread t;
    private boolean running;
    private AssaultView av;
    private AssaultModel am;
    private Timer gameTimer;
    private Player p;
    private Enemy e;
    private PlayerControl pc;

    public AssaultControl() {
        this.av = new AssaultView();
        this.am = new AssaultModel();
        this.p = new Player(100, am.PLATFORM_Y - 25, 25, 25, 0,0, 100, Color.BLACK);
        this.e = new Enemy(1300, am.PLATFORM_Y - 50, 50, 50, 0, 0, 100, Color.WHITE);
        this.pc = new PlayerControl(am, av, p);
        init();
    }

    private synchronized void start() {
        if (running) return;
        running = true;
        t = new Thread(this);
        t.start();
    }

    private synchronized void stop() {
        if (!running) return;
        running = false;
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        setBackground(am.LIGHT_GRAY);
        setFocusTraversalKeysEnabled(false);
        setRequestFocusEnabled(true);
        addKeyListener(this);
        setFocusable(true);
        setVisible(true);
        start();
        initGameTimer();
    }

    private void initGameTimer() {
        gameTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                am.updateCounter();
                //System.out.println(am.getTime());
            }
        });
        gameTimer.start();
    }

    private void actionPerformed() {
        requestFocusInWindow();
        pc.move();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        av.drawPlatform(g, am.AQUA, am.PLATFORM_Y, am.GAME_LENGTH, 20);
        av.drawEntity(g, p);
        av.drawEntity(g, e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        pc.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pc.keyReleased(e);
    }

    /**
     * Game loop
     * Allows for more consistent frames
     */
    @Override
    public void run() {
        long lastTime = System.nanoTime();
        final double fps = 60.0;
        final double updateInterval = 1000000000 / fps;
        double delta = 0;
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime)/updateInterval;
            lastTime = now;

            if (delta >= 1) {
                actionPerformed();
                delta--;
            }
            repaint();

            try {
                if ((lastTime - System.nanoTime() + updateInterval)/1000000 < 0) continue;
                Thread.sleep(8);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        stop();
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
