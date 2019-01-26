import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
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
    private EnemyAttackControl eac;
    private PlayerControl pc;
    private EnemyControl ec;
    private List<Obstacle> obstacles;

    public AssaultControl() {
        this.p = new Player(100, am.PLATFORM_Y - 25, 25, 25, 0,0, 100, Color.BLACK);
        this.e = new Enemy(1300, am.PLATFORM_Y - 50, 50, 50, 0, 0, 100, Color.WHITE);
        this.playerAttack = new Attack();
        this.am = new AssaultModel(p, e);
        this.av = new AssaultView();
        this.pc = new PlayerControl(am, p, playerAttack);
        this.eac = new EnemyAttackControl(e);
        this.ec = new EnemyControl(am, e, eac);
        this.obstacles = new ArrayList<>();
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
        av.drawPlatform(this, am.GAME_LENGTH, am.AQUA, am.LIGHT_GRAY);
    }

    private void actionPerformed() {
        if (am.isPaused()) return;
        requestFocusInWindow();
        if (e.isWaiting()) growShockwave();
        if (eac.attackCollision(p)) {
            int a = eac.getCurrentAttack();
            if ((a == 7  || a == 11) && !p.isKnockedBack()) pc.knockBack(new Point(e.getXOrd(), e.getYOrd()));
            if (!p.isDamaged()) pc.takeDamage();
        }
        if (isCollided() && !p.isDamaged()) pc.takeDamage();
        if (enemyHit() && !e.isDamaged()) ec.takeDamage();
        if (playerAttack.isCompleted()) e.setDamaged(false);
        pc.move();
        ec.move();
    }

    private void growShockwave() {
        moveShockwave();
        if (obstacles.size() == 0) {
            // add part of right and left shockwave
            obstacles.add(new Obstacle(e.getXOrd() + e.getLength() + 20, am.PLATFORM_Y - 20, 20, 20));
            obstacles.add(new Obstacle(e.getXOrd() - 20 - 20, am.PLATFORM_Y - 20, 20, 20));

        } else if (obstacles.size() == 18 && !obstacles.get(0).inFrame() &&
                !obstacles.get(obstacles.size() - 1).inFrame()) {
            e.setWaiting(false);
            obstacles.clear();

        } else if (obstacles.size() < 18) {
            int heightDifference = 15;
            Obstacle oR = obstacles.get(obstacles.size() - 2);
            Obstacle oL = obstacles.get(obstacles.size() - 1);
            int h = (obstacles.size() > 8) ? oR.getHeight() - heightDifference : oR.getHeight() + heightDifference;
            int y = (obstacles.size() > 8) ? oR.getY() + heightDifference : oR.getY() - heightDifference;

            // add part of right and left shockwave
            obstacles.add(new Obstacle(oR.getX() + oR.getLength(), y, oR.getLength(), h));
            obstacles.add(new Obstacle(oL.getX() - oL.getLength(), y, oL.getLength(), h));
        }
    }

    private void moveShockwave() {
        for (int i = 0; i < obstacles.size(); i++) {
            Obstacle o = obstacles.get(i);
            if (shockwaveCollision(o) && !p.isDamaged()) {
                pc.knockUp();
                pc.takeDamage();
            }

            // move shockwave when total length is 18*20
            if (obstacles.size() != 18) break;
            int direction = (i % 2 == 0) ? 1 : -1;  // ensures move all left/right parts of wave left/right
            if (o.inFrame()) o.move(direction);
        }
    }

    private boolean shockwaveCollision(Obstacle o) {
        return p.getBoundary().intersects(o.getBounds());
    }

    private boolean enemyHit() {
        return (playerAttack.isCompleted()) ? false : playerAttack.getBounds(p).intersects(e.getBoundary());
    }

    private boolean isCollided() {
        return p.getBoundary().intersects(e.getBoundary());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        av.drawEntity(g, e, null);
        if (e.isWaiting()) av.drawShockwave(g, obstacles, am.AQUA);
        av.drawEntity(g, p, playerAttack);
        if (eac.isAttacking()) drawEnemyAttack(g);
    }

    private void drawEnemyAttack(Graphics g) {
        av.drawBallAttack(g, eac.getAttackComponents(), (eac.getCurrentAttack() == 8) ? true : false);
        if (eac.getCurrentAttack() == 8) av.drawLaser(g, eac.getAttackComponents());
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
            if (paused) {
                eac.stopAttackTimer();
                gameTimer.stop();
            } else {
                eac.startAttackTimer();
                gameTimer.start();
            }

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
