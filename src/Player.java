import java.awt.Color;

public class Player extends Entity {
    private int moveVel;
    private int maxJump;
    private boolean falling;
    private String direction;

    public Player(int x, int y, int h, int l, int velX, int velY, int health, Color c) {
        super(x, y, h, l, velX, velY, health, c);
        this.moveVel = 5;
        this.maxJump = 180;
        this.falling = false;
        this.direction = "E";
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

    public void setDirection(String d) {
        this.direction = d;
    }

    public String getDirection() {
        return this.direction;
    }
}
