package util;

/**
 * Represents the last directional movements of an object
 */
public class MovementDirTracker {
    public static final int UP = -1;
    public static final int DOWN = 1;
    public static final int LEFT = -1;
    public static final int RIGHT = 1;

    private int verticalDir;
    private int horizontalDir;

    /**
     * Creates a movement tracker object with movement in the positive directions (DOWN/RIGHT) by default
     */
    public MovementDirTracker() {
        this.verticalDir = DOWN;
        this.horizontalDir = RIGHT;
    }

    /**
     * Sets the horizontal movement direction of the object to LEFT
     */
    public void setHorizontalDirLeft() {
        this.horizontalDir = LEFT;
    }

    /**
     * Sets the horizontal movement direction of the object to RIGHT
     */
    public void setHorizontalDirRight() {
        this.horizontalDir = RIGHT;
    }

    /**
     * Sets the vertical movement direction of the object to UP
     */
    public void setVerticalDirUp() {
        this.verticalDir = UP;
    }

    /**
     * Sets the vertical movement direction of the object to DOWN
     */
    public void setVerticalDirDown() {
        this.verticalDir = DOWN;
    }

    /**
     * @return the current vertical direction of the object
     */
    public int getHorizontalDir() {
        return this.horizontalDir;
    }

    /**
     * @return the current vertical direction of the object
     */
    public int getVerticalDir() {
        return this.verticalDir;
    }
}
