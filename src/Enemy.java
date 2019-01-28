import java.awt.Color;

public class Enemy extends Entity {
    private int startLength, startHeight;
    private boolean damaged, attacking, waiting, casting;
    private int moveVel;
    private int jumpY;
    private int dirX;

    public Enemy(int x, int y, int h, int l, int velX, int velY, int health, Color c) {
        super(x, y, h, l, velX, velY, health, c);
        this.damaged = false;
        this.attacking = false;
        this.waiting = false;
        this.casting = false;
        this.moveVel = 10;
        this.jumpY = 400;
        this.startLength = l;
        this.startHeight = h;
        this.dirX = -1;
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

    public boolean isCasting() {
        return this.casting;
    }

    public void setCasting(boolean b) {
        this.casting = b;
    }

    public void setDirX(int dir) {
        this.dirX = dir;
    }

    public int getDirX() {
        return this.dirX;
    }

}
