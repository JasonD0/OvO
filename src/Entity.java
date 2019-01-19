import java.awt.Color;
import java.awt.Rectangle;

public class Entity {
    private int length, height;
    private int x, y;
    private int velX, velY;
    private int health;
    private int angle;
    private boolean attacking;
    private Color c;

    public Entity(int x, int y, int h, int l, int velX, int velY, int health, Color c) {
        this.length = l;
        this.height = h;
        this.x = x;
        this.y = y;
        this.velX = velX;
        this.velY = velY;
        this.health = health;
        this.angle = 0;
        this.attacking = false;
        this.c = c;
    }

    public Rectangle getBoundary() {
        return new Rectangle(x, y, length, height);
    }

    public int getPlayerLength() {
        return length;
    }

    public int getPlayerHeight() {
        return height;
    }

    public int getXOrd() {
        return x;
    }

    public int getYOrd() {
        return y;
    }

    public int getVelY() { return this.velY; }

    public int getVelX() { return this.velX; }

    public void setYOrd(int y) {
        this.y = y;
    }

    public void setXOrd(int x) { this.x = x; }

    public void setVelY(int velY) {
        this.velY = velY;
    }

    public void setVelX(int velX) { this.velX = velX; }

    public void setAttacking(boolean b) {
        this.attacking = b;
    }

    public boolean isAttacking() {
        return this.attacking;
    }

    public int getHealth() {
        return this.health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public int getAngle() { return this.angle; }

    public void setColor(Color c) { this.c = c; }

    public Color getColor() {
        return c;
    }
}
