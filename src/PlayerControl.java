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

    public PlayerControl(AssaultModel am, AssaultView av, Player p, Attack a) {
        this.am = am;
        this.av = av;
        this.p = p;
        this.attackControl = a;
        initAttackTimer();
        initDashTimer();
    }

    private void initAttackTimer() {
        this.attackCD = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attackCD.stop();
            }
        });
    }

    private void initDashTimer() {
        this.dashCD = new Timer(1500, new ActionListener() {
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
        p.setYOrd(p.getYOrd() + p.getVelY());
        p.setXOrd(p.getXOrd() + p.getVelX());
        attackControl.increaseWidth(p.isAttacking());
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
    }

    private void preventMoveOutRight() {
        if (p.getXOrd() + p.getPlayerLength() < am.GAME_LENGTH) return;
        p.setXOrd(am.GAME_LENGTH - p.getPlayerLength() - 1);
        p.setVelX(0);
    }

    public void keyPressed(KeyEvent e) {
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

        if (e.getKeyCode() == KeyEvent.VK_Z && !p.isFalling()) {
            p.setVelY(-p.getMoveVel() - 2);
            p.setFalling(false);
        }

        if (e.getKeyCode() ==  KeyEvent.VK_X) {
            p.setAttacking(true);
        }
    }

    public void keyReleased(KeyEvent e) {
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

        if (e.getKeyCode() ==  KeyEvent.VK_X) {
            p.setAttacking(false);
        }
    }
}
