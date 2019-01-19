public class Attack {
    private static int START_W = 70;
    private static int END_W = 100;
    private static int HEIGHT = 10;
    private int angle;
    private int width, height;

    public Attack() {
        this.width = START_W;
        this.height = HEIGHT;
        this.angle = -180;
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    public int getAngle() {
        return this.angle;
    }

    public int getStartW() {
        return this.START_W;
    }

    public void increaseWidth(boolean attacking) {
        if (!attacking && this.width <= START_W) return;
        this.width = (this.width > END_W) ? START_W : this.width + 5;
    }

    public void changeAttackAngle(String direction) {
        if (direction.compareTo("E") == 0) {
            this.angle = -180;
        } else if (direction.compareTo("W") == 0) {
            this.angle = 180;
        }
    }
}
