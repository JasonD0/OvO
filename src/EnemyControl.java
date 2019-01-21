import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.Timer;

public class EnemyControl {
    private AssaultModel am;
    private Enemy e;
    private EnemyAttack ea;
    private int currentAttack; // ensures current progresses until end
    private Timer attackCD;
    private Random rand;

    public EnemyControl(AssaultModel am, Enemy e, EnemyAttack ea) {
        this.am = am;
        this.e = e;
        this.ea = ea;
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
                attackCD.setDelay(rand.nextInt(2000 - 500 + 1) + 500);
            }
        });
        this.attackCD.start();
    }

    public void move() {
        if (e.isDead()) die();
        else {
            if (e.isAttacking()) performAttack();
            else currentAttack = 0;
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
        e.setHealth(e.getHealth() - 10);
    }

    private void stopOnPlatform() {
        if (e.getYOrd() + e.getPlayerHeight() < am.PLATFORM_Y) return;
        e.setYOrd(am.PLATFORM_Y - e.getPlayerHeight());
        e.setVelY(0);
    }

    private void stopAtRightWall() {
        if (e.getXOrd() + e.getPlayerLength() < am.GAME_LENGTH) return;
        currentAttack = 0;
        ea.endAttack();
        e.setXOrd(am.GAME_LENGTH - e.getPlayerLength() - 1);
        e.setVelX(0);
    }

    private void stopAtLeftWall() {
        if (e.getXOrd() > 0) return;
        currentAttack = 0;
        ea.endAttack();
        e.setXOrd(1);
        e.setVelX(0);
    }

    private void performAttack() {
        currentAttack = ea.chooseAttack(am.getPlayerPos(), currentAttack);

    }
}
