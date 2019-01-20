import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Arc2D;
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
        Graphics2D g2d = (Graphics2D) g.create();
        Point p = new Point(e.getXOrd() + e.getPlayerLength()/2, e.getYOrd() + e.getPlayerHeight()/2);
        g2d.rotate(Math.toRadians(e.getAngle()), p.getX(), p.getY());
        Rectangle entity = e.getBoundary();
        // entity.contains(x, y)

        if (e.getClass() == Player.class && !a.isCompleted()) drawAttack(g, e, a);

        g2d.setColor(e.getColor());
        g2d.fill(entity);
        drawHealth(g, e);
        g2d.dispose();
    }

    private void drawAttack(Graphics g, Entity e, Attack a) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.WHITE);
        // offset    separates attack from the player
        int offset = (a.getExtent() == -180) ? 25 : -25;
        // x    x-ordinate of top left boundary of the circle (not the visible arc)
        int x = e.getXOrd() + e.getPlayerLength()/2 - a.getWidth()/2 + offset;
        int y = e.getYOrd();

        if (a.getDirection().compareTo("N") == 0) {
            x = e.getXOrd() + e.getPlayerLength()/3;
            y = e.getYOrd() - e.getPlayerHeight()/2  - a.getHeight()/2;
        }

        //extent  90 = half of arc   180 = full arc  360 = ellipse
        Arc2D.Double attack = new Arc2D.Double(x, y,
                a.getWidth(), a.getHeight(), a.getStart(), a.getExtent(), Arc2D.OPEN);
        g2d.draw(attack);


        g2d.dispose();
    }

    private void drawHealth(Graphics g, Entity e) {
        if (e.getAngle() != 0) return;

        Graphics2D g2d = (Graphics2D) g.create();
        double percentRed = (e.getHealth() <= 0) ? 0 : (double)e.getHealth()/100;
        double redBarWidth = e.getPlayerLength() * percentRed;
        int redBarXOrd = e.getXOrd() + (int) redBarWidth;
        int grayBarLength = e.getPlayerLength() - (int) redBarWidth;

        g2d.setColor(Color.RED);
        g2d.fillRect(e.getXOrd(), e.getYOrd() - 7,  (int) redBarWidth, 2);
        g2d.setColor(Color.GRAY);
        g2d.fillRect(redBarXOrd, e.getYOrd() - 7, grayBarLength, 2);

        g2d.dispose();
    }
}
