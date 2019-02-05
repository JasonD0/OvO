import java.awt.Graphics;
import javax.swing.JPanel;

public class EnemyControl extends JPanel {
    private AssaultModel am;
    private Enemy e;
    private EnemyAttackControl eac;
    private AssaultView av;
    private int damage;
    private int currentAttack; // ensures current progresses until end

    public EnemyControl(AssaultModel am, Enemy e, EnemyAttackControl eac, AssaultView av) {
        this.am = am;
        this.e = e;
        this.eac = eac;
        this.currentAttack = 0;
        this.av = av;
        this.damage = 5;
    }

    /**
     * Move player
     */
    public void move() {
        if (e.isDead()) die();
        else {
            if (e.isAttacking()) performAttack();
            else if (!e.isWaiting()) currentAttack = 0;
            stopOnPlatform();
            stopAtRightWall();
            stopAtLeftWall();
            e.setXOrd(e.getXOrd() + e.getVelX());
            e.setYOrd(e.getYOrd() + e.getVelY());
        }
    }

    /**
     * Descends enemy to hell when enemy health reaches 0 😈
     */
    private void die() {
        e.setVelX(0);
        e.setVelY(5);
        e.setYOrd(e.getYOrd() + e.getVelY());
    }

    /**
     * Reduce enemy health
     */
    public void takeDamage() {
        e.setDamaged(true);
        e.setHealth(e.getHealth() - damage);
    }

    /**
     * Prevent enemy from moving below platform
     * Enemy stops on platform
     */
    private void stopOnPlatform() {
        if (e.getYOrd() + e.getHeight() <= am.PLATFORM_Y) return;
        if (currentAttack == 5) e.setWaiting(true);
        currentAttack = 0;
        eac.endAttack();
        e.setYOrd(am.PLATFORM_Y - e.getHeight());
        e.setVelY(0);
    }

    /**
     * Prevents enemy from moving past right wall
     * Enemy stops at right wall
     */
    private void stopAtRightWall() {
        if (e.getLength() != e.getStartLength()) return;
        if (e.getXOrd() + e.getLength() < am.GAME_LENGTH) return;
        currentAttack = 0;
        eac.endAttack();
        e.setXOrd(am.GAME_LENGTH - e.getLength() - 1);
        e.setVelX(0);
        // enemy moves down if not on platform
        if (e.getYOrd() + e.getHeight() < am.PLATFORM_Y) e.setVelY(e.getVel()*3);
    }

    /**
     * Prevents enemy from moving past left wall
     * Enemy stops at left wall
     */
    private void stopAtLeftWall() {
        if (e.getLength() != e.getStartLength()) return;
        if (e.getXOrd() > 0) return;
        currentAttack = 0;
        eac.endAttack();
        e.setXOrd(1);
        e.setVelX(0);
        // enemy moves down if not on platform
        if (e.getYOrd() + e.getHeight() < am.PLATFORM_Y) e.setVelY(e.getVel()*3);

    }

    /**
     * Perform an attack
     */
    private void performAttack() {
        currentAttack = eac.chooseAttack(am.getPlayerPos(), currentAttack);
    }

    /**
     * Draws enemy attack components
     * @param g    graphic used for drawing
     */
    @Override
    public void paintComponent(Graphics g) {
        if (currentAttack == 0) return;
        if (e.isCasting()) {
            av.drawMagicCircle(g, e);
            this.damage = 1;
        } else {
            this.damage = 5;
        }
        if (currentAttack == 12) av.drawRectangularAttack(g, eac.getAttackComponents());
        else av.drawBallAttack(g, eac.getAttackComponents(), (eac.getCurrentAttack() == 8) ? true : false);
        if (currentAttack == 8) av.drawLaser(g, eac.getAttackComponents());
    }
}
