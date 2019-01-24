import java.awt.Rectangle;

public class EnemyAttack {
    private int x, y;
    private int height, width;
    private int angle;
    private int velX;
    private boolean charging;
    private float opacity, ballOpacity;
    private int dirX;

    public EnemyAttack() {
        this.charging = false;
        this.opacity = 0f;
        this.ballOpacity = 1f;
        this.dirX = 1;
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

    public Rectangle getBallBounds() {
        return new Rectangle(this.x - this.width, this.y - this.width, this.width*2, this.width*2);
    }

    public void setOpacity(float f) {
        this.opacity = f;
    }

    public float getOpacity() {
        return this.opacity;
    }

    public void setBallOpacity(float f) {
        this.ballOpacity = f;
    }

    public float getBallOpacity() {
        return this.ballOpacity;
    }

    public Rectangle getLaserBounds() {
        return new Rectangle((dirX == 1) ? this.x : 0, this.y - this.width, (dirX == - 1) ? this.x : 2000, this.width*2);
    }

    public void setDirX(int dir) {
        this.dirX = dir;
    }

    public int getDirX() {
        return this.dirX;
    }
}
