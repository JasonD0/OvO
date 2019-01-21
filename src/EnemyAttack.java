import java.awt.Point;
import java.util.Random;

public class EnemyAttack {
    private final static int NUM_ATTACKS = 2;
    private Enemy e;
    private Random rand;
    private int directionX, directionY;
    private Point playerPos;

    public EnemyAttack(Enemy e) {
        this.e = e;
        this.rand = new Random();
    }

    /**
     *
     * @param pos       co-ordinate in which the attack is directed towards
     * @param attack
     * @return
     */
    public int chooseAttack(Point pos, int attack) {
        e.setAttacking(true);
        if (playerPos == null) setDirection(pos);

        int a = (attack == 0) ? rand.nextInt(NUM_ATTACKS) + 1 : attack;

        switch (a) {
            case 1: rollAttack(); break;
            case 2: rush(); break;
        }

        return a;
    }

    /**
     * Sets direction in which the attack is directed towards
     * @param pos
     */
    private void setDirection(Point pos) {
        playerPos = pos;
        this.directionX = (playerPos.getX() < e.getXOrd()) ? -1 : 1;
        this.directionY = (playerPos.getY() < e.getYOrd()) ? -1 : 1;
    }

    public void endAttack() {
        e.setAttacking(false);
        playerPos = null;
        e.setAngle(0);
        e.setVelX(0);
        e.setVelY(0);
    }

    /**
     * Enemy rolls towards player until reaching a wall
     */
    public void rollAttack() {
        e.setVelX(this.directionX*e.getVel());
        e.setVelY(0);
        e.setAngle((e.getAngle() + 10) % 360);
    }

    /**
     * Enemy waits until player is on the platform and in a specific range
     * then charges towards the player at high speeds
     */
    public void rush() {
        // reached player's old position   end attack
        if ((directionX == 1 && e.getXOrd() >= playerPos.getX()) ||
             directionX == -1 && e.getXOrd() <= playerPos.getX()) {
            endAttack();

        // rush towards player
        } else if (Math.abs(this.playerPos.getX() - e.getXOrd()) <= 300) {
            e.setVelX(this.directionX*15);

        // try get closer to player
        } else {
            this.playerPos = null;
            e.setVelX(this.directionX*e.getVel());
        }
    }
}
