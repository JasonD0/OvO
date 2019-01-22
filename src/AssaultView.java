import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;

public class AssaultView {
    public void drawPlatform(AssaultControl parent, int l, Color c) {
        JLabel platform = new JLabel();
        platform.setBorder(BorderFactory.createMatteBorder(20, 0, 0, 0, c));
        platform.setMaximumSize(new Dimension(l, 20));
        platform.setMinimumSize(new Dimension(l, 20));
        platform.setPreferredSize(new Dimension(l,20));
        platform.setBackground(c);
        parent.add(platform);
    }

    public void drawEntity(Graphics g, Entity e, Attack a) {
        if (!e.inFrame()) return;
        Graphics2D g2d = (Graphics2D) g.create();
        Point p = new Point(e.getXOrd() + e.getLength()/2, e.getYOrd() + e.getHeight()/2);
        g2d.rotate(Math.toRadians(e.getAngle()), p.getX(), p.getY());
        Rectangle entity = e.getBoundary();

        if (e.getClass() == Player.class && !a.isCompleted()) drawAttack(g, e, a);
        if (e.getClass() == Player.class) changeOpacity(g2d, e);

        g2d.setColor(e.getColor());
        g2d.fill(entity);

        g2d.setColor(Color.BLUE);

        //extent  90 = half of arc   180 = full arc  360 = ellipse
        /*Arc2D.Double attack = new Arc2D.Double(200, 200, 150, 10,
                90, -90, Arc2D.OPEN);
        g2d.draw(attack); */   // change extent from 0 to -180

        if (!e.isDead()) drawHealth(g, e);
        else drawHalo(g, e);

        g2d.dispose();
    }

    /**
     * Chnage opacity of player when damaged
     * @param g2d
     * @param e
     */
    private void changeOpacity(Graphics2D g2d, Entity e) {
        Player player = (Player) e;
        if (player.isDamaged()) g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
    }

    private void drawAttack(Graphics g, Entity e, Attack a) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.WHITE);

        //extent  90 = half of arc   180 = full arc  360 = ellipse
        Arc2D.Double attack = new Arc2D.Double(a.getX(e), a.getY(e), a.getWidth(), a.getHeight(),
                a.getStart(), a.getExtent(), Arc2D.OPEN);
        g2d.draw(attack);

        g2d.dispose();
    }

    public void drawShockwave(Graphics g, List<Obstacle> shockwave, Color c) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(c);

        for (Obstacle o : shockwave) {
            g2d.fill(o.getBounds());
        }

        g2d.create();
    }

    private void drawHealth(Graphics g, Entity e) {
        if (e.getAngle() != 0) return;

        Graphics2D g2d = (Graphics2D) g.create();
        double percentRed = (e.getHealth() <= 0) ? 0 : (double)e.getHealth()/100;
        double redBarWidth = e.getLength() * percentRed;
        int redBarXOrd = e.getXOrd() + (int) redBarWidth;
        int grayBarLength = e.getLength() - (int) redBarWidth;

        g2d.setColor(Color.RED);
        g2d.fillRect(e.getXOrd(), e.getYOrd() - 7,  (int) redBarWidth, 2);
        g2d.setColor(Color.GRAY);
        g2d.fillRect(redBarXOrd, e.getYOrd() - 7, grayBarLength, 2);

        g2d.dispose();
    }

    private void drawHalo(Graphics g, Entity e) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.WHITE);
        Ellipse2D.Double ee = new Ellipse2D.Double(e.getXOrd(),e.getYOrd() - 10,e.getLength(),5);
        g2d.draw(ee);
        g2d.dispose();
    }
}
