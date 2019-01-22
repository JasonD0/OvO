import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import javax.swing.Timer;

public class PlayerControl {
    private AssaultModel am;
    private Timer attackCD, dashCD, damageCD;
    private Player p;
    private Attack attack;
    private int dashStart;
    private HashMap<Integer, Boolean> pressedKeys;

    public PlayerControl(AssaultModel am, Player p, Attack a) {
        this.am = am;
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
        this.attackCD = new Timer(450, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attackCD.stop();
            }
        });
    }

    /**
     * Cooldown for dashing
     */
    private void initDashTimer() {
        this.dashCD = new Timer(750, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dashCD.stop();
            }
        });
    }

    /**
     * Cooldown for taking damage
     */
    private void initDamageTimer() {
        this.damageCD = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                p.setDamaged(false);
                damageCD.stop();
            }
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
            stopAtRightWall();
            stopAtLeftWall();
            if (p.isDashing()) dashEnd();
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

    public void takeDamage() {
        p.setDamaged(true);
        this.damageCD.restart();
        p.setHealth(p.getHealth() - 10);
    }

    /**
     * Prevent player from jumping higher than the maximum jump
     */
    private void stopAtMaxJump() {
        if (am.PLATFORM_Y - p.getYOrd() < p.getMaxJump() || p.isDashing()) return;
        p.setVelY(p.getMoveVel());
        p.setFalling(true);
    }

    /**
     * Prevent player from falling below the platform
     */
    private void stopOnPlatform() {
        if (p.getYOrd() <= am.PLATFORM_Y - p.getHeight()) return;
        p.setYOrd(am.PLATFORM_Y - p.getHeight());
        p.setVelY(0);
        p.setFalling(false);
    }

    /**
     * Prevent player from moving past the left wall
     */
    private void stopAtLeftWall() {
        if (p.getXOrd() > 0) return;
        p.setXOrd(1);
        p.setVelX(0);
        if (p.getYOrd() + p.getHeight() < am.PLATFORM_Y) p.setVelY(p.getMoveVel());
        p.setDashing(false);
    }

    /**
     * Prevent player from moving past the right wall
     */
    private void stopAtRightWall() {
        if (p.getXOrd() + p.getLength() < am.GAME_LENGTH) return;
        p.setXOrd(am.GAME_LENGTH - p.getLength() - 1);
        p.setVelX(0);
        if (p.getYOrd() + p.getHeight() < am.PLATFORM_Y) p.setVelY(p.getMoveVel());
        p.setDashing(false);
    }

    /**
     * Stop player dash
     */
    private void dashEnd() {
        if (Math.abs(p.getXOrd() - dashStart) < p.getMaxDash()) return;
        p.setVelX(0);
        p.setDashing(false);
        // let player fall if player dashed in the air
        if (p.getYOrd() + p.getHeight() < am.PLATFORM_Y) {
            p.setFalling(true);
            p.setVelY(p.getMoveVel());
        }
    }

    /**
     * Move the player in the right direction
     */
    private void moveRight() {
        if (p.getXOrd() + p.getLength() >= am.GAME_LENGTH - 1) return;
        if (!isKeyPressed(KeyEvent.VK_RIGHT) || isKeyPressed(KeyEvent.VK_C)) return;
        p.setVelX(p.getMoveVel());
        p.setDirection("E");
        attack.changeAttackAngle("E");
    }

    /**
     * Move the player in the left direction
     */
    private void moveLeft() {
        if (p.getXOrd() <= 1) return;
        if (!isKeyPressed(KeyEvent.VK_LEFT) || isKeyPressed(KeyEvent.VK_C)) return;
        p.setVelX(-p.getMoveVel());
        p.setDirection("W");
        attack.changeAttackAngle("W");
    }

    /**
     * Checks if a key is still held down
     * @param key
     * @return
     */
    private boolean isKeyPressed(int key) {
        return pressedKeys.getOrDefault(key, false);
    }

    public void keyPressed(int key) {
        // prevent changes in movement during a player dash
        if (p.isDashing()) return;

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

    public void keyReleased(int key) {
        if (!p.isDashing()) {  // prevent changes in movement during a player dash
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
