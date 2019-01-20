import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
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
    private Attack playerAttack;
    private PlayerControl pc;

    public AssaultControl() {
        this.av = new AssaultView();
        this.am = new AssaultModel();
        this.p = new Player(100, am.PLATFORM_Y - 25, 25, 25, 0,0, 100, Color.BLACK);
        this.e = new Enemy(1300, am.PLATFORM_Y - 50, 50, 50, 0, 0, 100, Color.WHITE);
        this.playerAttack = new Attack();
        this.pc = new PlayerControl(am, av, p, playerAttack);
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
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        addPlatform();
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

    private void addPlatform() {
        add(Box.createRigidArea(new Dimension(0, am.PLATFORM_Y)));
        av.drawPlatform(this, am.GAME_LENGTH, am.AQUA);
    }

    private void actionPerformed() {
        if (am.isPaused()) return;
        requestFocusInWindow();
        pc.move();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        av.drawEntity(g, e, null);
        av.drawEntity(g, p, playerAttack);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (am.isPaused()) return;
        pc.keyPressed(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_P) {
            boolean paused = (am.isPaused()) ? false : true;
            am.setPaused(paused);
            if (paused) gameTimer.stop();
            else gameTimer.start();

        } else {
            pc.keyReleased(e.getKeyCode());
        }
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
