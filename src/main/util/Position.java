package util;

/**
 * Represents an x and y position in the game area
 */
public class Position {
    private double posX;
    private double posY;

    /**
     * Creates a position object with an x and y coordinate
     *
     * @param posX x coordinate (must be within the bounds of the level, made up of a sum of powers of 2)
     * @param posY y coordinate (must be within the bounds of the level, made up of a sum of powers of 2)
     */
    public Position(double posX, double posY) {
        this.posX = posX;
        this.posY = posY;
    }

    /**
     * Sets the position's x coordinate to the given x value
     *
     * @param newX new x coordinate (must be within the bounds of the level, made up of a sum of powers of 2)
     */
    public void setPosX(double newX) {
        this.posX = newX;
    }

    /**
     * Sets the position's y coordinate to the given y value
     *
     * @param newY new y coordinate (must be within the bounds of the level, made up of a sum of powers of 2)
     */
    public void setPosY(double newY) {
        this.posY = newY;
    }

    /**
     * Changes the position's x coordinate by the given amount
     *
     * @param dx x coordinate change (must be made up of a sum of powers of 2)
     */
    public void changePosX(double dx) {
        this.posX += dx;
    }

    /**
     * Changes the position's y coordinate by the given amount
     *
     * @param dy y coordinate change (must be made up of a sum of powers of 2)
     */
    public void changePosY(double dy) {
        this.posY += dy;
    }

    /**
     * @return the position's x coordinate
     */
    public double getPosX() {
        return this.posX;
    }

    /**
     * @return the position's y coordinate
     */
    public double getPosY() {
        return this.posY;
    }
}
