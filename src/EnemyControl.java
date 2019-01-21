public class EnemyControl {
    private AssaultModel am;
    private Enemy e;

    public EnemyControl(AssaultModel am, Enemy e) {
        this.am = am;
        this.e = e;
    }

    public void move() {
        if (e.isDead()) die();
        else {
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
}
