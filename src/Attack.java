public class Attack {
    private final static int START_W = 70;
    private final static int END_W = 110;
    private final static int HEIGHT = 10;
    private int extent, start;
    private int width, height;
    private String direction;

    public Attack() {
        this.height = HEIGHT;
        this.extent = -180;
        this.start = 90;
        this.direction = "E";
    }

    public int getHeight() {
        return (direction.compareTo("N") == 0) ? this.width : this.height;
    }

    public int getWidth() {
        return (direction.compareTo("N") == 0) ? this.height : this.width;
    }

    public void resetWidth() {
        this.width = START_W;
    }

    public String getDirection() {
        return this.direction;
    }

    public boolean isCompleted() {
        return this.width >= END_W;
    }

    public int getExtent() {
        return this.extent;
    }

    public int getStart() {
        return this.start;
    }

    public void increaseWidth() {
        this.width += 5;
    }

    public void changeAttackAngle(String direction) {
        this.direction = direction;
        switch (direction) {
            case "E":
                this.extent = -180;
                this.start = 90;
                break;
            case "W":
                this.extent = 180;
                this.start = 90;
                break;
            case "N":
                this.extent = 180;
                this.start = 0;
                break;
        }
    }
}
