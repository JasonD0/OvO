import java.awt.Color;

public class Player extends Entity {
    private int moveVel;
    private int maxJump;
    private boolean falling;

    public Player(int x, int y, int h, int l, int velX, int velY, int health, Color c) {
        super(x, y, h, l, velX, velY, health, c);
        this.moveVel = 7;
        this.maxJump = 200;
        this.falling = false;
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

    public boolean isFalling() {
        return this.falling;
    }



}
