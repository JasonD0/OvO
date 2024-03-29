import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class OvoView {
    public void drawPlatform(OvoControl parent) {
        JLabel platform = new JLabel();
        platform.setBorder(BorderFactory.createMatteBorder(20, 0, 0, 0, OvoModel.AQUA));
        platform.setMaximumSize(new Dimension(OvoModel.GAME_LENGTH, 20));
        platform.setMinimumSize(new Dimension(OvoModel.GAME_LENGTH, 20));
        platform.setPreferredSize(new Dimension(OvoModel.GAME_LENGTH,20));
        platform.setBackground(OvoModel.LIGHT_GRAY);
        parent.add(platform);
    }

    public void drawRestart(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(OvoModel.AQUA);
        g2d.setFont(new Font(null, Font.BOLD, 25));
        g2d.drawString("Press F to Restart", OvoModel.GAME_LENGTH/2 - 110, OvoModel.GAME_HEIGHT/2);
        g2d.dispose();
    }

    public void drawInstructions(OvoControl parent) {
        JLabel instructions = new JLabel("", SwingConstants.CENTER);
        instructions.setFont(new Font(null, Font.BOLD, 20));
        instructions.setMaximumSize(new Dimension(OvoModel.GAME_LENGTH, 50));
        instructions.setMinimumSize(new Dimension(OvoModel.GAME_LENGTH, 50));
        instructions.setPreferredSize(new Dimension(OvoModel.GAME_LENGTH,50));
        instructions.setBackground(OvoModel.LIGHT_GRAY);
        String s = "<html><font color='rgb(127, 255, 212)'>LEFT/RIGHT:</font> MOVE #SPACE# " +
                "         <font color='rgb(127, 255, 212)'>Z:</font> JUMP #SPACE# " +
                "         <font color='rgb(127, 255, 212)'>X:</font> ATTACK #SPACE# " +
                "         <font color='rgb(127, 255, 212)'>C:</font> DASH #SPACE# " +
                "         <font color='rgb(127, 255, 212)'>P:</font> PAUSE</html>";
        s = s.replaceAll("#SPACE#",  "&emsp; &emsp; &emsp; &emsp; &emsp; &emsp;");
        instructions.setText(s);
        instructions.setForeground(Color.WHITE);
        parent.add(instructions);
    }

    public void drawEntity(Graphics g, Entity e, Attack a) {
        if (!e.inFrame()) return;
        Graphics2D g2d = (Graphics2D) g.create();
        Point p = new Point(e.getXOrd() + e.getLength()/2, e.getYOrd() + e.getHeight()/2);
        g2d.rotate(Math.toRadians(e.getAngle()), p.getX(), p.getY());
        Rectangle entity = e.getBoundary();

        if (e.getClass() == Player.class && !a.isCompleted()) drawPlayerAttack(g, e, a);
        if (e.getClass() == Player.class) changeOpacity(g2d, e);

        g2d.setColor(e.getColor());
        g2d.fill(entity);

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

    private void drawPlayerAttack(Graphics g, Entity e, Attack a) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.WHITE);
        //extent  90 = half of arc   180 = full arc  360 = ellipse
        Arc2D.Double attack = new Arc2D.Double(a.getX(e), a.getY(e), a.getWidth(), a.getHeight(),
                a.getStart(), a.getExtent(), Arc2D.OPEN);
        g2d.draw(attack);
        g2d.dispose();
    }

    /**
     *
     * @param g
     * @param components
     * @param fill    false for outline
     */
    public void drawBallAttack(Graphics g, List<EnemyAttack> components, boolean fill) {
        Graphics2D g2d = (Graphics2D) g.create();
        for (EnemyAttack ea : components) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ea.getBallOpacity()));
            int x = ea.getX() - ea.getWidth();
            int y = ea.getY() - ea.getWidth();
            int diameter = ea.getWidth() * 2;

            g2d.setColor(Color.WHITE);
            // draw arc for laser when laser begins fading
            if (fill && Float.compare(ea.getBallOpacity(), 1f) < 0) {
                Arc2D.Double attack = new Arc2D.Double(x, y, diameter, diameter, 90, (ea.getDirX() == -1) ? -180 : 180, Arc2D.OPEN);
                g2d.fill(attack);

            // draw ball
            } else {
                g2d.drawOval(x, y, diameter, diameter);
                if (fill) g2d.fillOval(x, y, diameter, diameter);
            }
        }
        g2d.dispose();
    }

    public void drawShockwave(Graphics g, List<Obstacle> shockwave, Color c) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(c);
        for (Obstacle o : shockwave) g2d.fill(o.getBounds());
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

    public void drawLaser(Graphics g, List<EnemyAttack> components) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.WHITE);
        for (EnemyAttack ea : components) {
            float alpha = (ea.getOpacity() < 0f) ? 0f : (ea.getOpacity() > 1f) ? 1f : ea.getOpacity();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2d.fill(ea.getLaserBounds());
        }
        g2d.dispose();
    }

    public void drawRectangularAttack(Graphics g, List<EnemyAttack> components) {
        for (EnemyAttack ea : components) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(OvoModel.AQUA);
            g2d.rotate(Math.toRadians(ea.getAngle()), ea.getX(), ea.getY());
            g2d.fill(new Rectangle(ea.getX(), ea.getY(), ea.getWidth(), ea.getHeight()));
            g2d.dispose();
        }
    }

    public void drawMagicCircle(Graphics g, Enemy e) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.CYAN);
        int x = e.getXOrd() + e.getLength()/2 + e.getDirX()*(e.getLength()/2 + 15);
        if (e.getDirX() == 0) x -= (e.getLength()/2 + 10);
        int y = e.getYOrd() + ((e.getDirX() == 0) ? -25 : 0);
        int w = (e.getDirX() == 0) ? e.getLength() + 20: 5;
        int h = (e.getDirX() == 0) ? 5 : e.getHeight();
        Ellipse2D.Double circle = new Ellipse2D.Double(x, y, w, h);
        g2d.draw(circle);
        g2d.dispose();
    }
}
