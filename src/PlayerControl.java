import java.awt.event.KeyEvent;

public class PlayerControl {
    private AssaultModel am;
    private AssaultView av;
    private Player p;

    public PlayerControl(AssaultModel am, AssaultView av, Player p) {
        this.am = am;
        this.av = av;
        this.p = p;
    }

    public void move() {
        preventMoveBelow();
        preventMoveAbove();
        p.setYOrd(p.getYOrd() + p.getVelY());
        p.setXOrd(p.getXOrd() + p.getVelX());
    }

    private void preventMoveAbove() {
        if (am.PLATFORM_Y - p.getYOrd() < p.getMaxJump()) return;
        p.setVelY(p.getMoveVel());
        p.setFalling(true);
    }

    private void preventMoveBelow() {
        if (p.getYOrd() <= am.PLATFORM_Y - p.getPlayerHeight()) return;
        p.setYOrd(am.PLATFORM_Y - p.getPlayerHeight());
        p.setVelY(0);
        p.setFalling(false);
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            p.setVelX(p.getMoveVel());

        } if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            p.setVelX(-p.getMoveVel());
        }

        if (e.getKeyCode() == KeyEvent.VK_Z && !p.isFalling()) {
            p.setVelY(-p.getMoveVel());
            p.setFalling(false);
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            p.setVelX(0);

        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            p.setVelX(0);
        }

        if (e.getKeyCode() == KeyEvent.VK_Z) {
            p.setVelY(p.getMoveVel());
            p.setFalling(true);
        }
    }
}
