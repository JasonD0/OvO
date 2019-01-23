import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.Timer;

public class EnemyControl {
    private AssaultModel am;
    private Enemy e;
    private EnemyAttackControl eac;
    private EnemyAttack ea;
    private int currentAttack; // ensures current progresses until end
    private Timer attackCD;
    private Random rand;

    public EnemyControl(AssaultModel am, Enemy e, EnemyAttackControl eac) {
        this.am = am;
        this.e = e;
        this.eac = eac;
        this.currentAttack = 0;
        this.rand = new Random();
        initAttackTimer();
    }

    /**
     * Cooldown for attacks
     */
    private void initAttackTimer() {
        this.attackCD = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ee) {
                e.setAttacking(true);       // ensures enemy only attacks after timer delay
                attackCD.setDelay(rand.nextInt(5000 - 3000 + 1) + 3000);
            }
        });
        this.attackCD.start();
    }

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
     * Descends enemy to hell 😈
     */
    private void die() {
        e.setVelX(0);
        e.setVelY(5);
        e.setYOrd(e.getYOrd() + e.getVelY());
    }

    public void takeDamage() {
        e.setDamaged(true);
        e.setHealth(e.getHealth() - 5);
    }

    private void stopOnPlatform() {
        if (e.getYOrd() + e.getHeight() <= am.PLATFORM_Y) return;
        if (currentAttack == 5) e.setWaiting(true);
        currentAttack = 0;
        eac.endAttack();
        e.setYOrd(am.PLATFORM_Y - e.getHeight());
        e.setVelY(0);
    }

    private void stopAtRightWall() {
        if (e.getXOrd() + e.getLength() < am.GAME_LENGTH) return;
        currentAttack = 0;
        eac.endAttack();
        e.setXOrd(am.GAME_LENGTH - e.getLength() - 1);
        e.setVelX(0);
    }

    private void stopAtLeftWall() {
        if (e.getXOrd() > 0) return;
        currentAttack = 0;
        eac.endAttack();
        e.setXOrd(1);
        e.setVelX(0);
    }

    private void performAttack() {
        currentAttack = eac.chooseAttack(am.getPlayerPos(), currentAttack);

    }
}
