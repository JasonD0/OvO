import java.awt.Color;

public class Enemy extends Entity {
    private int startLength, startHeight;
    private boolean damaged, attacking, waiting;
    private int moveVel;
    private int jumpY;

    public Enemy(int x, int y, int h, int l, int velX, int velY, int health, Color c) {
        super(x, y, h, l, velX, velY, health, c);
        this.damaged = false;
        this.attacking = false;
        this.waiting = false;
        this.moveVel = 10;
        this.jumpY = 400;
        this.startLength = l;
        this.startHeight = h;
    }

    public void setDamaged(boolean b) {
        this.damaged = b;
    }

    public boolean isDamaged() {
        return this.damaged;
    }

    public int getVel() {
        return this.moveVel;
    }

    public void setAttacking(boolean b) {
        this.attacking = b;
    }

    public boolean isAttacking() {
        return this.attacking;
    }

    public int getJumpY() {
        return this.jumpY;
    }

    public int getStartLength() {
        return this.startLength;
    }

    public int getStartHeight() {
        return this.startHeight;
    }

    public boolean isWaiting() {
        return this.waiting;
    }

    public void setWaiting(boolean b) {
        this.waiting = b;
    }
}
