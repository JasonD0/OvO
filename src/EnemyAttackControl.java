import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.Timer;

public class EnemyAttackControl {
    private final static int NUM_ATTACKS = 9;
    private Enemy e;
    private Random rand;
    private int directionX, directionY;
    private Point playerPos, startPos;  // saves starting co-ordinates of the attack for both
    private boolean flag;   // helper flags for attacks
    private EnemyAttack ea;
    private Timer attackCD;
    private int currentAttack;

    public EnemyAttackControl(Enemy e) {
        this.e = e;
        this.ea = new EnemyAttack();
        this.rand = new Random();
        this.flag = false;
        this.currentAttack = 0;
        initAttackTimer();
    }

    /**
     * Cooldown for attacks
     */
    private void initAttackTimer() {
        this.attackCD = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ee) {
                attackCD.setDelay(rand.nextInt(5000 - 3000 + 1) + 3000);
                e.setAttacking(true);       // ensures enemy only attacks after timer delay
                attackCD.stop();
            }
        });
        this.attackCD.start();
    }

    public EnemyAttack getEnemyAttack() {
        return this.ea;
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
        if (startPos == null) startPos = new Point(e.getXOrd(), e.getYOrd());

        currentAttack = (attack == 0) ? rand.nextInt(NUM_ATTACKS) + 1 : attack;

        switch (currentAttack) {
            case 1: rollAttack(); break;
            case 2: rush(); break;
            case 3: jump(); break;
            case 4: snail(); break;
            case 5: jumpShock(); break;
            case 6: energyBall(); break;
            case 7: pulse(); break;
            case 8: laser(); break;
            case 9: bounce(); break;
        }

        return currentAttack;
    }

    public int getCurrentAttack() {
        return this.currentAttack;
    }

    /**
     * Sets direction in which the attack is directed towards
     * @param pos
     */
    private void setDirection(Point pos) {
        this.playerPos = pos;
        this.startPos = null;
        this.directionX = (playerPos.getX() < e.getXOrd()) ? -1 : 1;
        this.directionY = (playerPos.getY() < e.getYOrd()) ? -1 : 1;
        ea.setDirX(directionX);
    }

    public void endAttack() {
        e.setAttacking(false);
        ea.setCharging(false);
        ea.setOpacity(0f);
        ea.setBallOpacity(1f);
        ea.initBall(0,0,0,0);
        playerPos = null;
        startPos = null;
        flag = false;
        e.setAngle(0);
        e.setVelX(0);
        e.setVelY(0);
        attackCD.start();
    }

    private void moveTowardsPlayer() {
        this.playerPos = null;
        e.setVelX(directionX*e.getVel());
    }

    /**
     * Enemy rolls towards player until reaching a wall
     */
    private void rollAttack() {
        e.setVelX(this.directionX*e.getVel());
        e.setVelY(0);
        e.setAngle((e.getAngle() + 10) % 360);
    }

    /**
     * Move towards player until player is in a specific range
     * then charge towards the player at high speeds
     */
    private void rush() {
        // end attack when reaching player's old position
        if ((directionX == 1 && e.getXOrd() >= playerPos.getX()) ||
             directionX == -1 && e.getXOrd() <= playerPos.getX()) {
            endAttack();

        // rush towards player
        } else if (Math.abs(playerPos.getX() - e.getXOrd()) <= 300) {
            e.setVelX(directionX*e.getVel()*2);

        // try get closer to player
        } else {
            moveTowardsPlayer();
        }
    }

    /**
     * Move towards player until player is in a specific range
     * then jump on top of player
     */
    private void jump() {
        // jump
        if (Math.abs(playerPos.getX() - e.getXOrd()) < 400) {
            e.setYOrd(getParabolicY());
            e.setAngle((e.getAngle() + 10) % 360);
            e.setVelX(e.getVel() * directionX);

        // try get closer to player
        } else {
            moveTowardsPlayer();
        }
    }

    // y = a(x - h)^2 + k
    private int getParabolicY() {
        double startX = startPos.getX() + e.getLength()/2;
        double endX = playerPos.getX();
        double h = (startX + endX)/2;
        double k = e.getJumpY();
        double x = e.getXOrd() + e.getLength()/2;
        double a = (startPos.getY() - k)/Math.pow(startX - h, 2);
        double dy = a*Math.pow((x - h), 2) + k;
        return (int) dy;
    }

    /**
     * When in range, increase length of enemy until certain length
     * then slowly decrease length until original length in new position
     */
    private void snail() {
        // reduce length to normal length
        if (e.getLength() >= 400 || flag) {
            snailReduceLength();

        // increase length
        } else if (Math.abs(playerPos.getX() - e.getXOrd()) < 200) {
            snailIncreaseLength();

        // move towards player
        } else {
            moveTowardsPlayer();
        }
    }

    private void snailReduceLength() {
        e.setVelX(0);
        flag = true;    // ensures this if is executed when starting to reduce length
        /*
         move at same rate as length increases
         only move if player was ahead at the start of the attack
        */
        if (directionX == 1) e.setVelX(directionX*e.getVel()*2);
        e.setLength(e.getLength() - e.getVel()*2);

        // end attack when original length
        if (e.getLength() <= e.getStartLength()) {
            e.setLength(e.getStartLength());
            endAttack();
        }
    }

    private void snailIncreaseLength() {
        e.setVelX(0);
        // reduce length when reach left/right wall
        if (e.getXOrd() - e.getVel()*2 <= 0 || e.getXOrd() + e.getLength() + e.getVel() >= 1484) {
            flag = true;
            return;
        }
        if (directionX == -1) e.setVelX(directionX*e.getVel());
        e.setLength(e.getLength() + e.getVel());
    }

    /**
     * Jumps up then crash down
         * Create 'shockwave'
     */
    private void jumpShock() {
        // go down
        if (e.getYOrd() <= 150 || flag) {
            e.setVelY(e.getVel()*3);
            flag = true;

        // jump up when player in range
        } else if (Math.abs(playerPos.getX() - e.getXOrd()) < 150) {
            e.setVelY(-e.getVel());
            e.setVelX(0);

        // move towards player
        } else {
            moveTowardsPlayer();
        }
    }

    /**
     * Increase size of ball then releases it towards player
     * ball comes back at a certain length
     */
    private void energyBall() {
        // ball 'boomerangs' back
        if (ea.isCharging() && (Math.abs(ea.getX() - e.getXOrd()) >= 600 || ea.getX() <= 0 || ea.getX() + ea.getWidth() >= 1484 || flag)) {
            flag = true;    // ensures ball continues to return back to current position
            if (directionX == -1 && ea.getX() + ea.getWidth() >= e.getXOrd()) endAttack();
            if (directionX == 1 && ea.getX() <= e.getXOrd() + e.getLength()) endAttack();
            ea.setX(ea.getX() + ea.getVelX()*directionX*-1);

        } else if (Math.abs(playerPos.getX() - e.getXOrd()) < 300) {
            e.setVelX(0);
            // create ball
            if (!ea.isCharging()) {
                ea.setCharging(true);
                ea.initBall(e.getXOrd() + e.getLength()/2 + directionX*(e.getLength()/2 + 35), e.getYOrd(), 2, 15);

            // increase ball size and release
            } else {
                if (ea.getWidth() >= 25) ea.setX(ea.getX() + ea.getVelX()*directionX);
                else ea.chargeBall(2);
            }

        } else {
            moveTowardsPlayer();
        }
    }

    /**
     * Circle expands from enemy that knocks back and damages player
     */
    private void pulse() {
        if (Math.abs(playerPos.getX() - e.getXOrd()) < 300) {
            e.setVelX(0);
            if (!ea.isCharging()) {
                ea.setCharging(true);
                ea.initBall(e.getXOrd() + e.getLength()/2, e.getYOrd() + e.getHeight()/2, 2, 0);

            } else {
                if (ea.getWidth() >= 450) endAttack();
                else if (ea.getWidth() < 150) ea.chargeBall(5);
                else ea.chargeBall(10);
            }

        } else {
            moveTowardsPlayer();
        }
    }

    /**
     * Shoots laser at enemy
     */
    private void laser() {
        // charge laser
        if (!ea.isCharging()) {
            ea.setCharging(true);
            ea.initBall(e.getXOrd() + e.getLength()/2 + directionX*e.getLength(), e.getYOrd() + e.getHeight()/2, 2, 0);

        } else {
            // lower laser opacity
            if (Float.compare(ea.getOpacity(), 1f) > 0 || flag) {
                ea.setOpacity(ea.getOpacity() - 0.05f);
                ea.setBallOpacity(ea.getOpacity());
                flag = true;
                if (Float.compare(ea.getOpacity(), 0f) < 0) endAttack();
            }

            // increase laser opacity
            else if (ea.getWidth() >= 25) ea.setOpacity(ea.getOpacity() + 0.1f);

            // increase size of laser
            else ea.chargeBall(1);
        }
    }

    /**
     * Bounce up and down until reach a wall
     */
    private void bounce() {
        e.setVelX((e.getVel()/2)*directionX);

        // bounce down when hit ceiling
        if (e.getYOrd() <= 0) e.setVelY(e.getVel()*3);

        // bounce up when hit platform
        else if (e.getYOrd() + e.getHeight() >= AssaultModel.PLATFORM_Y) {
            e.setYOrd(AssaultModel.PLATFORM_Y - e.getHeight() - 10);
            e.setVelY(-e.getVel()*3);
        }
    }



    public boolean attackCollision(Player p) {
        return p.getBoundary().intersects(ea.getBallBounds()) ||
                (p.getBoundary().intersects(ea.getLaserBounds()) && ea.getOpacity() > 0);
    }

    public void stopAttackTimer() {
        this.attackCD.stop();
    }

    public void startAttackTimer() {
        this.attackCD.start();
    }
}
