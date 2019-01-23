import java.awt.Rectangle;

public class EnemyAttack {
    private int x, y;
    private int height, width;
    private int angle;
    private int velX;
    private boolean charging;

    public EnemyAttack() {
        this.charging = false;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setX(int x) { this.x = x; }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int w) {
        this.width = w;
    }

    public void setHeight(int h) {
        this.height = h;
    }

    public void setVelX(int v) {
        this.velX = v;
    }

    public int getVelX() {
        return this.velX;
    }

    public void setCharging(boolean b) {
        this.charging = b;
    }

    public boolean isCharging() {
        return this.charging;
    }

    public void initBall(int x, int y, int radius, int velX) {
        this.x = x;
        this.y = y;
        this.width = radius;
        this.velX = velX;
    }

    public void chargeBall(int increase) {
        this.width += increase;
    }

    public Rectangle getBounds() {
        return new Rectangle(this.x - this.width, this.y - this.width, this.width*2, this.width*2);
    }
}
