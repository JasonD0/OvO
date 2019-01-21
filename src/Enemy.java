import java.awt.Color;

public class Enemy extends Entity {
    private boolean damaged;

    public Enemy(int x, int y, int h, int l, int velX, int velY, int health, Color c) {
        super(x, y, h, l, velX, velY, health, c);
        this.damaged = false;
    }

    public void setDamaged(boolean b) {
        this.damaged = b;
    }

    public boolean isDamaged() {
        return this.damaged;
    }
}
