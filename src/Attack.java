public class Attack {
    private final static int START_W = 70;
    private final static int END_W = 110;
    private final static int HEIGHT = 10;
    private int extent, start;
    private int width, height;
    private String direction;

    public Attack() {
        this.width = END_W;
        this.height = HEIGHT;
        this.extent = -180;
        this.start = 90;
        this.direction = "E";
    }

    /**
     * Gets height of the attack
     * @return
     */
    public int getHeight() {
        return (direction.compareTo("N") == 0) ? this.width : this.height;
    }

    /**
     * Gets width of the attack
     * @return
     */
    public int getWidth() {
        return (direction.compareTo("N") == 0) ? this.height : this.width;
    }

    public void resetWidth() {
        this.width = START_W;
    }

    /**
     * Gets direction of the attack
     * @return
     */
    public String getDirection() {
        return this.direction;
    }

    /**
     * Checks attack completed
     * @return
     */
    public boolean isCompleted() {
        return this.width >= END_W;
    }

    /**
     * Increase attack range
     */
    public void increaseWidth() {
        this.width += 5;
    }

    /**
     * Gets the angular extent of the arc
     * See Arc2D.Double javadoc
     * @return
     */
    public int getExtent() {
        return this.extent;
    }

    /**
     * Gets the starting angle of the attack arc
     * See Arc2D.Double javadoc
     * @return
     */
    public int getStart() {
        return this.start;
    }

    /**
     * Changes angles for the attack direction to match the direction the player is facing
     * @param direction
     */
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
