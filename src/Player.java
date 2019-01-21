import java.awt.Color;

public class Player extends Entity {
    private int moveVel;
    private int maxJump;
    private boolean falling, dashing, damaged;
    private String direction;
    private Float opacity;
    private final static int MAX_DASH = 180;

    public Player(int x, int y, int h, int l, int velX, int velY, int health, Color c) {
        super(x, y, h, l, velX, velY, health, c);
        this.moveVel = 5;
        this.maxJump = 180;
        this.falling = false;
        this.dashing = false;
        this.damaged = false;
        this.direction = "E";
        this.opacity = 1f;
    }

    public int getMoveVel() {
        return this.moveVel;
    }

    public void setMoveVel(int v) {
        this.moveVel = v;
    }

    public int getMaxJump() {
        return this.maxJump;
    }

    public void setFalling(boolean b) {
        this.falling = b;
    }

    // prevents player from jumping again mid-air
    public boolean isFalling() {
        return this.falling;
    }

    public void setDashing(boolean b) {
        this.dashing = b;
    }

    public boolean isDashing() {
        return this.dashing;
    }

    public void setDamaged(boolean b) {
        this.damaged = b;
    }

    public boolean isDamaged() {
        return this.damaged;
    }

    public void setDirection(String d) {
        this.direction = d;
    }

    public String getDirection() {
        return this.direction;
    }

    public int getMaxDash() {
        return this.MAX_DASH;
    }
}
