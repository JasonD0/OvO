public class EnemyControl {
    private AssaultModel am;
    private Enemy e;
    private EnemyAttackControl eac;
    private int currentAttack; // ensures current progresses until end

    public EnemyControl(AssaultModel am, Enemy e, EnemyAttackControl eac) {
        this.am = am;
        this.e = e;
        this.eac = eac;
        this.currentAttack = 0;
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
        if (e.getLength() != e.getStartLength()) return;
        if (e.getXOrd() + e.getLength() < am.GAME_LENGTH) return;
        currentAttack = (currentAttack == 8) ? 8 : 0;
        eac.endAttack();
        e.setXOrd(am.GAME_LENGTH - e.getLength() - 1);
        e.setVelX(0);
        if (e.getYOrd() + e.getHeight() < am.PLATFORM_Y) e.setVelY(e.getVel()*3);
    }

    private void stopAtLeftWall() {
        if (e.getLength() != e.getStartLength()) return;
        if (e.getXOrd() > 0) return;
        currentAttack = (currentAttack == 8) ? 8 : 0;
        eac.endAttack();
        e.setXOrd(1);
        e.setVelX(0);
        if (e.getYOrd() + e.getHeight() < am.PLATFORM_Y) e.setVelY(e.getVel()*3);

    }

    private void performAttack() {
        currentAttack = eac.chooseAttack(am.getPlayerPos(), currentAttack);

    }

}
