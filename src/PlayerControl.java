import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.Timer;

public class PlayerControl {
    private AssaultModel am;
    private AssaultView av;
    private Timer attackCD, dashCD;
    private Player p;
    private Attack attackControl;
    private int dashStart;

    public PlayerControl(AssaultModel am, AssaultView av, Player p, Attack a) {
        this.am = am;
        this.av = av;
        this.p = p;
        this.attackControl = a;
        initAttackTimer();
        initDashTimer();
    }

    private void initAttackTimer() {
        // pressing X only registers every 0.45 seconds
        this.attackCD = new Timer(450, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attackCD.stop();
            }
        });
    }

    private void initDashTimer() {
        this.dashCD = new Timer(750, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dashCD.stop();
            }
        });
    }

    public void move() {
        preventMoveBelow();
        preventMoveAbove();
        preventMoveOutRight();
        preventMoveOutLeft();
        if (p.isDashing()) dashEnd();
        p.setYOrd(p.getYOrd() + p.getVelY());
        p.setXOrd(p.getXOrd() + p.getVelX());
        if (!attackControl.isCompleted()) attackControl.increaseWidth();
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

    private void preventMoveOutLeft() {
        if (p.getXOrd() > 0) return;
        p.setXOrd(1);
        p.setVelX(0);
        p.setDashing(false);
    }

    private void preventMoveOutRight() {
        if (p.getXOrd() + p.getPlayerLength() < am.GAME_LENGTH) return;
        p.setXOrd(am.GAME_LENGTH - p.getPlayerLength() - 1);
        p.setVelX(0);
        p.setDashing(false);
    }

    private void dashEnd() {
        if (Math.abs(p.getXOrd() - dashStart) < p.getMaxJump()) return;
        p.setFalling(true);
        p.setVelX(0);
        p.setDashing(false);
        p.setVelY(p.getMoveVel());
    }

    public void keyPressed(KeyEvent e) {
        if (p.isDashing()) return;
        if (attackControl.isCompleted()) {
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                p.setVelX(p.getMoveVel());
                p.setDirection("E");
                attackControl.changeAttackAngle("E");
            }

            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                p.setVelX(-p.getMoveVel());
                p.setDirection("W");
                attackControl.changeAttackAngle("W");
            }

            if (e.getKeyCode() == KeyEvent.VK_UP) {
                p.setDirection("N");
                attackControl.changeAttackAngle("N");
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_Z && !p.isFalling()) {
            p.setVelY(-p.getMoveVel() - 2);
            p.setFalling(false);
        }

        if (e.getKeyCode() == KeyEvent.VK_X && !attackCD.isRunning()) {
            attackControl.resetWidth();
            attackCD.restart();
        }
    }

    public void keyReleased(KeyEvent e) {
        if (p.isDashing()) return;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            p.setVelX(0);
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            p.setVelX(0);
        }

        if (e.getKeyCode() == KeyEvent.VK_Z) {
            p.setVelY(p.getMoveVel());
            p.setFalling(true);
        }

        if (e.getKeyCode() == KeyEvent.VK_C && !dashCD.isRunning()) {
            int direction = (p.getDirection().compareTo("E") == 0) ? 1 : -1;
            p.setDashing(true);
            dashStart = p.getXOrd();
            p.setVelY(0);
            p.setVelX(20*direction);
            dashCD.restart();
        }
    }
}
