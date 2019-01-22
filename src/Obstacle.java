import java.awt.Rectangle;

public class Obstacle {
    private int x, y;
    private int height, length;

    public Obstacle(int x, int y, int l, int h) {
        this.x = x;
        this.y = y;
        this.length = l;
        this.height = h;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getHeight() {
        return this.height;
    }

    public int getLength() {
        return this.length;
    }

    public void setLength(int l) {
        this.length = l;
    }

    public void setHeight(int h) {
        this.height = h;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, length, height);
    }

    public void move(int direction) {
        this.x += this.length*direction;
    }

    public boolean inFrame() {
        return x < 1484 && x + length > 0 && y + height > 0;
    }
}
