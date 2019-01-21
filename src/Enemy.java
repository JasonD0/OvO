import java.awt.Color;

public class Enemy extends Entity {
    private boolean damaged, attacking;
    private int moveVel;

    public Enemy(int x, int y, int h, int l, int velX, int velY, int health, Color c) {
        super(x, y, h, l, velX, velY, health, c);
        this.damaged = false;
        this.attacking = false;
        this.moveVel = 10;
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

}
