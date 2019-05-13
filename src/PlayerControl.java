import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import javax.swing.Timer;

public class PlayerControl {
    private Timer attackCD, dashCD, damageCD;
    private Player p;
    private Attack attack;
    private int dashStart;
    private HashMap<Integer, Boolean> pressedKeys;
    private Point enemyPos, startPos;

    public PlayerControl(Player p, Attack a) {
        this.p = p;
        this.attack = a;
        this.pressedKeys = new HashMap<>();
        initAttackTimer();
        initDashTimer();
        initDamageTimer();
    }

    /**
     * Cooldown for attacking
     */
    private void initAttackTimer() {
        // pressing X only registers every 0.45 seconds
        this.attackCD = new Timer(450, e -> attackCD.stop());
    }

    /**
     * Cooldown for dashing
     */
    private void initDashTimer() {
        this.dashCD = new Timer(750, e -> dashCD.stop());
    }

    /**
     * Cooldown for taking damage
     */
    private void initDamageTimer() {
        this.damageCD = new Timer(1000, e -> {
            p.setDamaged(false);
            damageCD.stop();
        });
    }

    /**
     * Incrementally move the player
     */
    public void move() {
        if (p.isDead()) die();
        else {
            moveRight();
            moveLeft();
            stopOnPlatform();
            stopAtMaxJump();
            stopAtCeiling();
            stopAtRightWall();
            stopAtLeftWall();
            if (p.isDashing()) dashEnd();
            if (p.isKnockedUp()) knockUp();
            if (p.isKnockedBack()) knockBack(null);
            p.setYOrd(p.getYOrd() + p.getVelY());
            p.setXOrd(p.getXOrd() + p.getVelX());
            if (!attack.isCompleted()) attack.increaseWidth();
        }
    }

    /**
     * Ascends player to heaven 😇
     */
    private void die() {
        p.setVelX(0);
        p.setVelY(-p.getMoveVel());
        p.setYOrd(p.getYOrd() + p.getVelY());
    }

    /**
     * Reduce player health
     */
    public void takeDamage() {
        p.setDamaged(true);
        this.damageCD.restart();
        p.setHealth(p.getHealth() - 15);
    }

    /**
     * Move player upwards
     * Player becomes unable to move until reaching a wall or the platform
     */
    public void knockUp() {
        if (!p.isKnockedUp()) p.setVelY(-p.getMoveVel() * 4);
        else if (p.getYOrd() <= 200) p.setVelY(p.getMoveVel() * 2);
        p.setVelX(0);
        p.setKnockedUp(true);
    }

    /**
     * Move player backwards when hit by certain enemy attacks
     * Player becomes unable to move until reaching a wall or the platform
     * @param pos    enemy position
     */
    public void knockBack(Point pos) {
        p.setVelY(0);
        p.setKnockedBack(true);
        enemyPos = (pos == null) ? enemyPos : pos;
        startPos = (pos == null) ? startPos : new Point(p.getXOrd(), p.getYOrd());
        // move player along a diagonal line
        if (p.getYOrd() + p.getHeight() < OvoModel.PLATFORM_Y)
            p.setYOrd(getLinearY(p.getXOrd(), (int) enemyPos.getX(), (int) enemyPos.getY()));
        // move player along a horizontal line
        if (p.getXOrd() >= enemyPos.getX()) p.setVelX(p.getMoveVel()*3);
        else p.setVelX(-p.getMoveVel()*3);
    }

    /**
     * Calculates new y-ordinate so that the player moves along a linear equation y = mx + b
     * @param x1    current x-ordinate
     * @param x2    initial x-ordinate
     * @param y2    initial y-ordinate
     * @return      new y-ordinate
     */
    private int getLinearY(int x1, int x2, int y2) {
        double deltaX = (startPos.getX() - x2 == 0) ? 1 : startPos.getX() - x2;
        double deltaY = startPos.getY() - y2;
        double m = deltaY/deltaX;
        double b = y2 - m*x2;
        double dy = m*x1 + b;
        return (int) dy;
    }

    /**
     * Prevent player from jumping beyond the game window
     */
    private void stopAtCeiling() {
        if (p.getYOrd() >= 0) return;
        p.setYOrd(1);
        p.setVelY(p.getMoveVel());
        p.setKnockedBack(false);
        p.setFalling(false);
    }

    /**
     * Prevent player from jumping higher than the maximum jump
     * Player begins falling
     */
    private void stopAtMaxJump() {
        if (OvoModel.PLATFORM_Y - p.getYOrd() < p.getMaxJump() || p.isDashing() || p.isKnockedUp()) return;
        p.setVelY(p.getMoveVel());
        p.setFalling(true);
    }

    /**
     * Prevent player from falling below the platform
     * Player stops on platform
     */
    private void stopOnPlatform() {
        if (p.getYOrd() <= OvoModel.PLATFORM_Y - p.getHeight()) return;
        p.setYOrd(OvoModel.PLATFORM_Y - p.getHeight());
        p.setVelY(0);
        p.setFalling(false);
        p.setKnockedUp(false);
        p.setKnockedBack(false);
    }

    /**
     * Prevent player from moving past the left wall
     * Player stops at left wall
     */
    private void stopAtLeftWall() {
        if (p.getXOrd() > 0) return;
        p.setXOrd(1);
        p.setVelX(0);
        if (p.getYOrd() + p.getHeight() < OvoModel.PLATFORM_Y) p.setVelY(p.getMoveVel());
        p.setDashing(false);
        p.setKnockedBack(false);
    }

    /**
     * Prevent player from moving past the right wall
     * Player stops at right wall
     */
    private void stopAtRightWall() {
        if (p.getXOrd() + p.getLength() < OvoModel.GAME_LENGTH) return;
        p.setXOrd(OvoModel.GAME_LENGTH - p.getLength() - 1);
        p.setVelX(0);
        p.setDashing(false);
        p.setKnockedBack(false);
        // fall if player is not on the platform
        if (p.getYOrd() + p.getHeight() < OvoModel.PLATFORM_Y) p.setVelY(p.getMoveVel());
    }

    /**
     * Stop player dash when dash distance reached
     */
    private void dashEnd() {
        int direction = (p.getDirection().compareTo("E") == 0) ? 1 : -1;
        p.setVelX(20*direction);
        if (Math.abs(p.getXOrd() - dashStart) < p.getMaxDash()) return;
        p.setVelX(0);
        p.setDashing(false);
        // let player fall if player dashed in the air
        if (p.getYOrd() + p.getHeight() < OvoModel.PLATFORM_Y) {
            p.setFalling(true);
            p.setVelY(p.getMoveVel());
        }
    }

    /**
     * Move the player in the right direction
     */
    private void moveRight() {
        if (p.getXOrd() + p.getLength() >= OvoModel.GAME_LENGTH - 1) return;
        if (!isKeyPressed(KeyEvent.VK_RIGHT) || isKeyPressed(KeyEvent.VK_C)) return;
        p.setVelX(p.getMoveVel());
        if (!isKeyPressed(KeyEvent.VK_UP)) {
            p.setDirection("E");
            attack.changeAttackAngle("E");
        }
    }

    /**
     * Move the player in the left direction
     */
    private void moveLeft() {
        if (p.getXOrd() <= 1) return;
        if (!isKeyPressed(KeyEvent.VK_LEFT) || isKeyPressed(KeyEvent.VK_C)) return;
        p.setVelX(-p.getMoveVel());
        if (!isKeyPressed(KeyEvent.VK_UP)) {
            p.setDirection("W");
            attack.changeAttackAngle("W");
        }
    }

    /**
     * Checks if a key is still held down
     * @param key    keyboard key pressed
     * @return       true if key is currently pressed
     */
    private boolean isKeyPressed(int key) {
        return pressedKeys.getOrDefault(key, false);
    }

    /**
     * Change player movement based on key pressed
     * @param key    keyboard key pressed
     */
    public void keyPressed(int key) {
        // prevent changes in movement during a player dash
        if (p.isDashing() || p.isKnockedUp() || p.isKnockedBack()) return;

        if (attack.isCompleted()) {  // prevent changes in movement during player attack
            // move player in the direction
            if (key == KeyEvent.VK_UP) {
                p.setDirection("N");
                attack.changeAttackAngle("N");
            }
            else if (key == KeyEvent.VK_RIGHT || (isKeyPressed(KeyEvent.VK_RIGHT) && !p.isDashing())) moveRight();
            else if (key == KeyEvent.VK_LEFT || (isKeyPressed(KeyEvent.VK_LEFT) && !p.isDashing())) moveLeft();
        }

        if (key == KeyEvent.VK_Z) pressedJump();
        if (key == KeyEvent.VK_X) pressedAttack();
        if (key == KeyEvent.VK_C) pressedDash();

        pressedKeys.put(key, true);
    }

    /**
     * User pressed the Z key
     * Player jumps for as long as the Z key is held down until the jumping height limit
     */
    private void pressedJump() {
        if (p.isFalling()) return;
        p.setVelY(-p.getMoveVel() - 2);
        p.setFalling(false);
    }

    /**
     * User pressed the X key
     * Player attacks
     */
    private void pressedAttack() {
        if (attackCD.isRunning()) return;
        attack.resetWidth();
        attackCD.restart();
    }

    /**
     * User pressed the C key
     * Start player dash (move player horizontally faster)
     */
    private void pressedDash() {
        if (dashCD.isRunning()) return;
        int direction = (p.getDirection().compareTo("E") == 0) ? 1 : -1;
        p.setDashing(true);
        dashStart = p.getXOrd();
        p.setVelY(0);
        p.setVelX(20*direction);
        dashCD.restart();
    }

    /**
     * Change player movement based on key released
     * @param key    keyboard key released
     */
    public void keyReleased(int key) {
        if (!p.isDashing() && !p.isKnockedUp() && !p.isKnockedBack()) {  // prevent changes in movement during a player dash
            if (key == KeyEvent.VK_RIGHT) p.setVelX(0); // stop the player from moving right
            if (key == KeyEvent.VK_LEFT) p.setVelX(0);  // stop the player from moving left

            // let player fall to the platform
            if (key == KeyEvent.VK_Z) {
                p.setVelY(p.getMoveVel());
                p.setFalling(true);
            }
        }
        pressedKeys.put(key, false);
    }
}
