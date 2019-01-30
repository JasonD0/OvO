import java.awt.Rectangle;

public class EnemyAttack {
    private int x, y;
    private int height, width;
    private int angle;
    private int velX, velY;
    private boolean charging;
    private float opacity, ballOpacity;
    private int dirX;
    private int repeat;

    public EnemyAttack() {
        this.charging = false;
        this.opacity = 0f;
        this.ballOpacity = 1f;
        this.dirX = 1;
        this.repeat = 1;
    }

    public EnemyAttack(int x, int y, int l, int h, int v) {
        this();
        this.x = x;
        this.y = y;
        this.width = l;
        this.height = h;
        this.velY = v;
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

    public void setVelY(int v) {
        this.velY = v;
    }

    public int getVelY() {
        return this.velY;
    }

    public void setCharging(boolean b) {
        this.charging = b;
    }

    public boolean isCharging() {
        return this.charging;
    }

    public void initBall(int x, int y, int radius, int velX, int velY) {
        this.x = x;
        this.y = y;
        this.width = radius;
        this.velX = velX;
        this.velY = velY;
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

    public void setRepeat(int numRepeat) {
        this.repeat = numRepeat;
    }

    public int getRepeat() {
        return this.repeat;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public int getAngle() {
        return this.angle;
    }
}
