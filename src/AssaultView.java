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
        drawHealth(g, e);
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
