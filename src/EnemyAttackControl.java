import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.Timer;

public class EnemyAttackControl {
    private final static int NUM_ATTACKS = 15;
    private Enemy e;
    private Random rand;
    private Point playerPos, startPos;  // saves starting co-ordinates of the attack for both
    private boolean flag;   // helper flags for attacks
    private List<EnemyAttack> attackComponents;
    private Timer attackCD, attackDelay, ballRotator;
    private int currentAttack;
    private int currBall1, currBall2;

    public EnemyAttackControl(Enemy e) {
        this.e = e;
        this.attackComponents = new ArrayList<>();
        this.rand = new Random();
        this.flag = false;
        this.currentAttack = 0;
        initAttackTimer();
        initAttackDelay();
        rotatingBallTimer();
    }

    /**
     * Cooldown for attacks
     */
    private void initAttackTimer() {
        this.attackCD = new Timer(1000, ee -> {
            attackCD.setDelay(rand.nextInt(3500 - 2000 + 1) + 2000);
            e.setAttacking(true);       // ensures enemy only attacks after timer delay
            attackCD.stop();
        });
        this.attackCD.start();
    }

    /**
     * Delay during attacks
     */
    private void initAttackDelay() {
        this.attackDelay = new Timer(500, e -> attackDelay.stop());
    }

    /**
     * Get all objects associated with an attack
     * @return    list of EnemyAttack objects
     */
    public List<EnemyAttack> getAttackComponents() {
        return this.attackComponents;
    }

    /**
     * Randomly choose and perform an attack
     * @param pos       player co-ordinate
     * @param attack    current attack
     * @return          attack chosen
     */
    public int chooseAttack(Point pos, int attack) {
        // setup for newly chosen attacks
        if (attackComponents.size() == 0) attackComponents.add(new EnemyAttack());
        e.setAttacking(true);
        if (playerPos == null) setDirection(pos);
        if (startPos == null) startPos = new Point(e.getXOrd(), e.getYOrd());

        currentAttack = (attack == 0) ? rand.nextInt(NUM_ATTACKS) + 1 : attack;
//        currentAttack = 4;  // choose specific attack to repeat

        switch (currentAttack) {
            case 1: rollAttack(); break;
            case 2: rush(); break;
            case 3: jump(); break;
            case 4: snail(); break;
            case 5: jumpShock(); break;
            case 6: energyBall(); break;
            case 7: pulse(450); break;
            case 8: laser(); break;
            case 9: bounce(); break;
            case 10: ballSmash(); break;
            case 11: meteor(); break;
            case 12: staircase(); break;
            case 13: rain(); break;
            case 14: rotatingBalls(); break;
            case 15: sting(); break;
        }

        return currentAttack;
    }

    /**
     * Gets current attack
     * @return    current attack
     */
    public int getCurrentAttack() {
        return this.currentAttack;
    }

    /**
     * Sets direction in which the attack is directed towards and saves player co-ordinate
     * @param pos    player co-ordinate
     */
    private void setDirection(Point pos) {
        this.playerPos = pos;
        //this.startPos = null;
        e.setDirX((playerPos.getX() < e.getXOrd()) ? -1 : 1);
        for (EnemyAttack ea : attackComponents) ea.setDirX(e.getDirX());
    }

    /**
     * End enemy attack
     */
    public void endAttack() {
        playerPos = null;
        startPos = null;
        flag = false;
        ballRotator.stop();
        attackComponents.clear();
        e.setAttacking(false);
        e.setCasting(false);
        e.setAngle(0);
        e.setVelX(0);
        e.setVelY(0);
        attackCD.start();
    }

    /**
     * Move towards the player
     */
    private void moveTowardsPlayer() {
        this.playerPos = null;
        //e.setVelX(directionX*e.getVel());
        e.setVelX(e.getDirX()*e.getVel());
    }

    /**
     * Enemy rolls towards player until reaching a wall
     */
    private void rollAttack() {
        e.setVelX((int) (e.getDirX()*e.getVel()*1.5));
        e.setVelY(0);
        e.setAngle((e.getAngle() + 20) % 360);
    }

    /**
     * Move towards player until player is in a specific range
     * then charge towards the player at high speeds
     */
    private void rush() {
        // end attack when reaching player's old position
        if ((e.getDirX() == 1 && e.getXOrd() >= playerPos.getX()) ||
             e.getDirX() == -1 && e.getXOrd() <= playerPos.getX()) {
            endAttack();

        // rush towards player when in distance of 500 pixels
        } else if (Math.abs(playerPos.getX() - e.getXOrd()) <= 500) {
            e.setVelX(e.getDirX()*e.getVel()*2);

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
        // jump when player in distance of 400 pixels
        if (Math.abs(playerPos.getX() - e.getXOrd()) < 400) {
            e.setVelX(0);
            e.setYOrd(getParabolicY());
            e.setAngle((e.getAngle() + 10) % 360);
            e.setVelX(e.getVel() * e.getDirX());

        // try get closer to player
        } else {
            moveTowardsPlayer();
        }
    }

    /**
     * Jumps up and glides towards the player, create a shockwave
     */
    private void sting() {
        // jump up
        if (e.getYOrd() > 200 && !flag) {
            e.setVelY(-e.getVel());
            playerPos = null;
            startPos = null;

            // stop and prepare to glide towards player
        } else if (e.getYOrd() < 200) {
            e.setVelY(0);
            e.setYOrd(200);
            flag = true;

            // glide towards player
        } else {
            e.setXOrd(e.getXOrd() + e.getVel()*e.getDirX()*3);
            e.setYOrd(getLinearY(e.getXOrd(), playerPos, startPos));
        }
    }

    /**
     * y= mx + b
     * @param x
     * @param start1
     * @param start2
     * @return
     */
    private int getLinearY(int x, Point start1, Point start2) {
        double deltaY = start1.getY() - 25 - start2.getY();
        double deltaX = start1.getX() - start2.getX();
        if (deltaX == 0) deltaX = 1;
        double m = deltaY/deltaX;
        double b = start1.getY() -25 - m*start1.getX();
        double y = m*x + b;
        return (int) y;
    }

    /**
     * Calculates y-ordinate so that the enemy moves along the parabolic equation y = a(x - h)^2 + k
     * @return    new y-ordinate
     */
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
        } else if (e.getLength() == e.getStartLength()) {
            moveTowardsPlayer();

        // temporary bug fix      dashing during snail attack
        // when length is not 400 and gap between saved player position and enemy is > 200
        } else {
            snailReduceLength();
        }
    }

    /**
     * Reduce length of enemy
     */
    private void snailReduceLength() {
        e.setVelX(0);
        flag = true;    // ensures this if is executed when starting to reduce length
        /*
         move at same rate as length increases
         only move if player was ahead at the start of the attack
        */
        if (e.getDirX() == 1) e.setVelX(e.getDirX()*e.getVel()*2);
        e.setLength(e.getLength() - e.getVel()*2);

        // end attack when original length
        if (e.getLength() <= e.getStartLength()) {
            e.setLength(e.getStartLength());
            endAttack();
        }
    }

    /**
     * Increase enemy length
     */
    private void snailIncreaseLength() {
        e.setVelX(0);
        // reduce length when reach left/right wall
        if (e.getXOrd() <= 0 || e.getXOrd() + e.getLength() >= OvoModel.GAME_LENGTH) {
            if (e.getXOrd() <= 0) e.setXOrd(1);
            else e.setXOrd(OvoModel.GAME_LENGTH - e.getLength() - 1);
            flag = true;
            return;
        }
        if (e.getDirX() == -1) e.setVelX(e.getDirX()*e.getVel());
        e.setLength(e.getLength() + e.getVel());
    }

    /**
     * Jumps up then crash down creating a 'shock-wave' (expanding circle that pushes player backwards)
     */
    private void jumpShock() {
        // go down
        if (e.getYOrd() <= 150 || flag) {
            e.setVelY(e.getVel()*3);
            flag = true;

        // jump up when player in range
        } else if (Math.abs(playerPos.getX() - e.getXOrd()) < 350) {
            e.setVelY(-e.getVel());
            e.setVelX(0);

        // move towards player
        } else {
            moveTowardsPlayer();
        }
    }

    /**
     * Increase size of ball then releases it towards player
     * ball comes back after moving a certain distance
     */
    private void energyBall() {
        EnemyAttack ea = attackComponents.get(0);

        // ball 'boomerangs' back
        if (ea.isCharging() && (Math.abs(ea.getX() - e.getXOrd()) >= 600 || ea.getX() <= 0 || ea.getX() + ea.getWidth() >= 1484 || flag)) {
            flag = true;    // ensures ball continues to return back to current position
            // end attack when ball reached enemy
            if (e.getDirX() == -1 && ea.getX() + ea.getWidth() >= e.getXOrd()) endAttack();
            if (e.getDirX() == 1 && ea.getX() <= e.getXOrd() + e.getLength()) endAttack();

            ea.setX(ea.getX() + ea.getVelX()*e.getDirX()*-1);

        // perform attack when player in distance of 300 pixels
        } else if (Math.abs(playerPos.getX() - e.getXOrd()) < 300) {
            e.setVelX(0);
            // create ball
            if (!ea.isCharging()) {
                ea.setCharging(true);
                ea.initBall(e.getXOrd() + e.getLength()/2 + e.getDirX()*(e.getLength()/2 + 35), e.getYOrd(), 2, 15, 0);

            // increase ball size and release
            } else {
                if (ea.getWidth() >= 25) ea.setX(ea.getX() + ea.getVelX()*e.getDirX());
                else ea.chargeBall(2);
            }

        } else {
            moveTowardsPlayer();
        }
    }

    /**
     * Circle expands from enemy that knocks back and damages player
     * @param pulseWidth    radius of the circle
     */
    private void pulse(int pulseWidth) {
        EnemyAttack ea = attackComponents.get(0);
        // flag true when crash attack is being performed
        // perform attack when player in distance of 400 pixels
        if (Math.abs(playerPos.getX() - e.getXOrd()) < 400 || flag) {
            e.setVelX(0);
            // create ball
            if (!ea.isCharging()) {
                ea.setCharging(true);
                ea.initBall(e.getXOrd() + e.getLength()/2, e.getYOrd() + e.getHeight()/2, 2, 0, 0);

            // expand ball
            } else {
                if (ea.getWidth() >= pulseWidth) endAttack();
                else if (ea.getWidth() < 150) ea.chargeBall(10);
                else ea.chargeBall(15);
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
        EnemyAttack ea = attackComponents.get(0);
        if (!ea.isCharging()) {
            ea.setCharging(true);
            ea.initBall(e.getXOrd() + e.getLength()/2 + e.getDirX()*(e.getLength() + 10), e.getYOrd() + e.getHeight()/2, 2, 0, 0);

        } else {
            // lower laser opacity
            if (Float.compare(ea.getOpacity(), 1f) > 0 || flag) {
                ea.setOpacity(ea.getOpacity() - 0.08f);
                ea.setBallOpacity(ea.getOpacity());
                flag = true;
                if (Float.compare(ea.getOpacity(), 0f) < 0) endAttack();
            }

            // increase laser opacity
            else if (ea.getWidth() >= 30) ea.setOpacity(ea.getOpacity() + 0.1f);

            // increase size of laser
            else ea.chargeBall(1);
        }
    }

    /**
     * Bounce up and down until reach a wall
     */
    private void bounce() {
        e.setVelX((e.getVel()/2)*e.getDirX());

        // bounce down when hit ceiling
        if (e.getYOrd() <= 0) e.setVelY(e.getVel()*3);

        // bounce up when hit platform
        else if (e.getYOrd() + e.getHeight() >= OvoModel.PLATFORM_Y) {
            e.setYOrd(OvoModel.PLATFORM_Y - e.getHeight() - 10);
            e.setVelY(-e.getVel()*3);
        }
    }

    /**
     * Ball swings towards player then swings the other way
     */
    private void ballSmash() {
        EnemyAttack ea = attackComponents.get(0);

        // perform attack when player in distance of 200 pixels
        if (Math.abs(playerPos.getX() - e.getXOrd()) < 200) {
            e.setVelX(0);
            // create ball
            if (!ea.isCharging()) {
                ea.setCharging(true);
                ea.initBall(e.getXOrd() + e.getLength()/2, e.getYOrd() - 150, 2, 0, 0);
                startPos = new Point(ea.getX(), ea.getY());

            // increase ball size
            } else if (ea.getWidth() <= 50) {
                ea.setVelX(0);
                ea.chargeBall(2);

            } else if (ea.getY() + ea.getHeight() >= OvoModel.PLATFORM_Y) {
                // swing ball back when first reach platform
                if (!flag) {
                    e.setDirX((e.getDirX() == 1) ? -1 : 1);
                    flag = true;
                    ea.setVelX(10);

                } else {
                    endAttack();
                    return;
                }
            }
            if (ea.getWidth() > 50) ea.setVelX(20);
            ea.setX(ea.getX() + ea.getVelX()*e.getDirX());
            ea.setY(getBallParabolicY(ea));

        } else {
            moveTowardsPlayer();
        }
    }

     /**
     * Move attack component along the parabolic equation y = a(x - h)^2 + k
     * @param ea    attack component
     * @return      new y-ordinate
     */
    private int getBallParabolicY(EnemyAttack ea) {
        double endX = startPos.getX() - 175;
        double h = startPos.getX();
        double k = startPos.getY();
        double x = ea.getX();
        double a = (OvoModel.PLATFORM_Y - k)/Math.pow(endX - h, 2);
        double dy = a*Math.pow((x - h), 2) + k;
        return (int) dy;
    }

    /**
     * teleport above player and crash down creating a pulse
     */
    private void meteor() {
        // teleport above player
        if (!flag) {
            e.setXOrd((int) playerPos.getX());
            e.setYOrd(100);
            e.setVelX(0);
            e.setVelY(0);
            flag = true;
            startDelay(50);

        // expand circle when reaching platform
        } else if (e.getYOrd() + e.getHeight() >= OvoModel.PLATFORM_Y) {
            e.setVelY(0);
            e.setYOrd(OvoModel.PLATFORM_Y - e.getHeight());
            pulse(250);

        // move downwards
        } else if (!this.attackDelay.isRunning()) {
            e.setVelY(25);
        }
    }

    /**
     * Multiple pillars falls towards player
     */
    private void staircase() {
        if (Math.abs(playerPos.getX() - e.getXOrd()) >= 600) {
            moveTowardsPlayer();
        }

        // create poles
        else if (attackComponents.size() == 1) {
            initSteps();

        // move poles
        } else if (!attackDelay.isRunning()) {
            placeSteps();
        }
    }

    /**
     * Create pillars for staircase attack and place them above the game window
     */
    private void initSteps() {
        e.setCasting(true);
        e.setVelX(0);
        attackComponents.clear();
        for (int i = 0; i < rand.nextInt(20 - 10 + 1) + 10; i++) {
            int x = e.getXOrd() + e.getLength()/2 + e.getDirX()*(e.getLength()/2 + i*80 + 150);
            int h = 400 - i*25;
            attackComponents.add(new EnemyAttack(x, 0 - h,  20, h, 40));
        }
        startDelay(500); // wait 0.5 secs after circle is drawn to indicate this attack is coming
    }

    /**
     * Move pillars from staircase attack down from above the game window
     */
    private void placeSteps() {
        boolean moving = false;   // true if at least one pillar moved
        for (int i = 0; i < attackComponents.size(); i++) {
            EnemyAttack ea = attackComponents.get(i);
            EnemyAttack prevEa = (i != 0) ? attackComponents.get(i-1) : null;
            if (ea.getY() + ea.getHeight() >= OvoModel.PLATFORM_Y) continue; // stop when reach platform
            // wait until previous pillar reached the platform before moving
            if (prevEa != null && prevEa.getY() + prevEa.getHeight() < OvoModel.PLATFORM_Y) continue;
            ea.setY(ea.getY() + ea.getVelY());
            moving = true;
        }
        // delay end attack
        if (!moving && !flag) {
            flag = true;
            startDelay(1000);
        }
        if (flag && !attackDelay.isRunning()) endAttack();
    }

    /**
     * Shoot multiple balls into the sky then the balls fall at random positions
     */
    private void rain() {
        e.setDirX(0);
        // create multiple balls
        if (attackComponents.size() == 1) {
            attackComponents.clear();
            for (int i = 0; i < rand.nextInt(25 - 10 + 1) + 10; i++) {
                attackComponents.add(new EnemyAttack(e.getXOrd() + e.getLength() / 2, e.getYOrd() - 70, 0, 0, -60));
            }

        // balls fall
        } else if (flag) {
            precipitation();

        // shoot balls upwards
        } else {
            evaporation();
        }
    }

    /**
     * Shoot balls from rain attack above the game window
     */
    private void evaporation() {
        e.setCasting(true);
        flag = true;    // true when all balls above the game window
        for (int i = 0; i < attackComponents.size(); i++) {
            EnemyAttack ea = attackComponents.get(i);
            EnemyAttack prevEa = (i == 0) ? null : attackComponents.get(i-1);
            // randomly place balls at random co-ordinates to fall down
            if (ea.getY() + ea.getWidth() < 0) {
                ea.setX(rand.nextInt((OvoModel.GAME_LENGTH - ea.getWidth()) - (ea.getWidth()) + 1) + ea.getWidth());
                ea.setY(rand.nextInt(-ea.getWidth()*2 + 400 + 1) - 400);
                // ensure at least 1 ball falls at each end of the game window
                if (i == 0)  ea.setX(ea.getWidth());
                if (i == attackComponents.size() - 1) ea.setX(OvoModel.GAME_LENGTH - ea.getWidth());
                continue;
            }
            if (prevEa != null && prevEa.getY() > 250) continue;    // wait before shooting next ball

            // increase ball size and shoot upwards
            if (ea.getWidth() <= 32) ea.chargeBall(8);
            else ea.setY(ea.getY() + ea.getVelY());
            flag = false;
        }
    }

    /**
     * Move balls from rain attack down from above the game window
     */
    private void precipitation() {
        boolean allReachedPlatform = true;  // false when at least one ball is falling
        for (int i = 0; i < attackComponents.size(); i++) {
            EnemyAttack ea = attackComponents.get(i);
            EnemyAttack prevEa = (i == 0) ? null : attackComponents.get(i-1);
            if (ea.getY() - ea.getWidth() >= OvoModel.PLATFORM_Y) continue; // stop falling when below platform
            if (prevEa != null && prevEa.getY() < 100) continue; // wait before another ball falls
            ea.setY(ea.getY() + rand.nextInt(40 - 15 + 1) + 15);
            allReachedPlatform = false;
        }
        if (allReachedPlatform) endAttack();
    }

    /**
     * Move towards player with 2 rotating balls around it
     */
    private void rotatingBalls() {
        e.setYOrd(400);
        EnemyAttack ea;
        // create balls
        if (attackComponents.size() == 1) {
            attackComponents.get(0).setBallOpacity(0f);
            currBall1 = 0;
            currBall2 = 10;
            for (int i = 0; i < 19; i++) {
                ea = new EnemyAttack();
                ea.setBallOpacity(0f);
                attackComponents.add(ea);
            }

        // place balls around enemy center
        } else {
            if (!ballRotator.isRunning()) this.ballRotator.restart();
            int centerX = e.getXOrd() + e.getLength()/2;
            int radius = 160;
            initUpperCircle(centerX, radius);
            initLowerCircle(centerX, radius);
            e.setVelX(e.getVel()*e.getDirX());
        }
    }

    /**
     * Flips opacity (1 and 0) of adjacent balls at certain intervals to emulate rotation around enemy
     * Ball index 0 - 10 constitutes the lower half of the circle (left to right)
     * Ball index 19 is followed by 10 (clockwise)  and  ball index 11 is followed by 0 (counter-clockwise)
     */
    private void rotatingBallTimer() {
        this.ballRotator = new Timer(100, ze -> {
            flipBallOpacity("1", currBall1);
            flipBallOpacity("2", currBall2);
        });
    }

    /**
     * Flips adjacent ball opacity for rotating ball timer
     * @param ball        indicates which ball to move (currBall1 or currBall2)
     * @param currBall    index of the currently moving ball
     */
    private void flipBallOpacity(String ball, int currBall) {
        if (currBall == 0) attackComponents.get(11).setBallOpacity(0f);
        if (currBall == 19) attackComponents.get(10).setBallOpacity(0f);

        int prevBall = currBall;
        attackComponents.get(currBall).setBallOpacity(0f);
        if (currBall <= 19 && currBall >= 11) currBall--;
        else currBall++;

        // prev ball is 10   change currBall to 19
        if (currBall == 11 && currBall > prevBall) currBall = 19;

        // prev ball is 11   change currBall to 0
        if (currBall == 10 && currBall < prevBall) currBall = 0;

        if (ball.compareTo("1") == 0) currBall1 = currBall;
        else currBall2 = currBall;

        attackComponents.get(currBall).setBallOpacity(1f);
    }

    /**
     * Place balls above the enemy, forming an upper semi-circle
     * @param centerX    center x-ordinate to place the ball around
     * @param radius     radius of the circle in which the balls are placed upon
     */
    private void initUpperCircle(int centerX, int radius) {
        // lower circle including 2 ending vertex
        // attack index 0 to 10  from left to right
        for (int i = 0, j = 0; j < 11; j++) {
            EnemyAttack ea = attackComponents.get(j);
            if (j == 1) ea.initBall(centerX - radius + 10, 0, 20, 5, 0);
            else if (j == 9) ea.initBall(centerX - radius + 310, 0, 20, 5, 0);
            else {
                ea.initBall(centerX - radius + 40 * i, 0, 20, 5, 0);
                i++;
            }
            ea.setY(getCircularY(ea, false));
        }
    }

    /**
     * Place balls below the enemy, forming the lower semi-circle
     * @param centerX    center x-ordinate to place the ball around
     * @param radius     radius of the circle in which the balls are placed upon
     */
    private void initLowerCircle(int centerX, int radius) {
        // upper circle excluding 2 ending vertex
        // attack index 11 to 19  from left to right
        for (int i = 11, j = 11; j < 20; j++) {
            EnemyAttack ea = attackComponents.get(j);
            if (j == 11) ea.initBall(centerX - radius + 10, 0, 20, 5, 0);
            else if (j == 19) ea.initBall(centerX - radius + 310, 0, 20, 5, 0);
            else {
                ea.initBall(centerX - radius + 40*(i - 11) + 40, 0, 20, 5, 0);
                i++;
            }
            ea.setY(getCircularY(ea, true));
        }
    }

    /**
     * Calculate y along a circle with enemy as the center
     * (y - h)^2 + (x - k)^2 = r^2
     * @param ea           attack component to move
     * @param upperSemi    true if calculating y for the upper semi-circle
     * @return             new y-ordinate
     */
    private int getCircularY(EnemyAttack ea, boolean upperSemi) {
        double r = 160;
        double centerX = e.getXOrd() + e.getLength()/2;
        double centerY = e.getYOrd() + e.getHeight()/2;
        double squaredX = Math.pow((ea.getX() - centerX), 2);
        double dy = Math.sqrt(Math.pow(r, 2) - squaredX)*((upperSemi) ? -1 : 1) + centerY;
        return (int) dy;
    }

    /**
     * Check collisions between any attack components and the player
     * @param p    player object
     * @return     true if player collided with any attack components
     */
    public boolean attackCollision(Player p) {
        for (EnemyAttack ea : attackComponents) {
            if ((p.getBoundary().intersects(ea.getBallBounds()) && ea.getBallOpacity() > 0) ||
                    (p.getBoundary().intersects(ea.getLaserBounds()) && ea.getOpacity() > 0)) {
                return true;
            }
            if (p.getBoundary().intersects(ea.getBounds()) && currentAttack == 12) return true;
        }
        return false;
    }

    /**
     * Set and start delay during an attack
     * @param delay    delay during attacks in milliseconds
     */
    private void startDelay(int delay) {
        this.attackDelay.setDelay(delay);
        this.attackDelay.start();
    }

    public void stopAttackTimer() {
        this.attackCD.stop();
    }

    public void startAttackTimer() {
        this.attackCD.start();
    }
}
