import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class AssaultView {
    public void drawPlatform(Graphics g, Color c, int y, int l, int h) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(c);
        Rectangle platform = new Rectangle(0, y, l, h);
        g2d.fill(platform);
        g2d.dispose();
    }

    public void drawEntity(Graphics g, Entity e) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.rotate(Math.toRadians(e.getAngle()), e.getXOrd() + e.getPlayerLength()/2, e.getYOrd() + e.getPlayerHeight()/2);
        Rectangle entity = e.getBoundary();
        g2d.setColor(e.getColor());
        g2d.fill(entity);
        g2d.dispose();
    }
}
