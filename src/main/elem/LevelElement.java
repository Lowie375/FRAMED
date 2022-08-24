package elem;

import model.Player;
import util.Position;

import java.awt.*;

/**
 * Represents a level element with a position, height, width, and element colour, placed with its upper left corner at
 * the given position
 */
public abstract class LevelElement {
    protected Position pos;
    protected int width;
    protected int height;
    protected Color elementCol;

    /**
     * Creates a level element at the given position with the given width and height
     *
     * @param pos position of the element's upper left corner (must be within the level's bounds)
     * @param width element width (0 < width < x position + level width)
     * @param height element height (0 < height < y position + level height)
     */
    public LevelElement(Position pos, int width, int height) {
        this.pos = pos;
        this.width = width;
        this.height = height;
    }

    /**
     * Sets the element's colour to the given colour
     *
     * @param col new element colour
     */
    public void setElementCol(Color col) {
        this.elementCol = col;
    }

    /**
     * @param player player to check
     * @return true if the level element is colliding with the player, false otherwise
     */
    public boolean isCollidingWithPlayer(Player player) {
        Position playerPos = player.getPosition();
        return posXIsWithinBounds(playerPos.getPosX(), Player.SIZE)
                && posYIsWithinBounds(playerPos.getPosY(), Player.SIZE);
    }

    /**
     * @param posX x coordinate
     * @param sizeOffset offset amount to apply to the coordinate
     * @return true if the x coordinate, offset by some amount on both sides, is within the x endpoints of the element,
     * false otherwise
     */
    protected boolean posXIsWithinBounds(double posX, double sizeOffset) {
        double thisX = this.pos.getPosX();
        return thisX <= posX + sizeOffset && posX - sizeOffset <= thisX + this.width;
    }

    /**
     * @param posX x coordinate
     * @return true if the x coordinate is within the x endpoints of the element, false otherwise
     * @see #posXIsWithinBounds(double, double)
     */
    protected boolean posXIsWithinBounds(double posX) {
        return posXIsWithinBounds(posX, 0);
    }

    /**
     * @param posY y coordinate
     * @param sizeOffset offset amount to apply to the coordinate
     * @return true if the y coordinate, offset by some amount on both sides, is within the y endpoints of the element,
     * false otherwise
     */
    protected boolean posYIsWithinBounds(double posY, double sizeOffset) {
        double thisY = this.pos.getPosY();
        return thisY <= posY + sizeOffset && posY - sizeOffset <= thisY + this.height;
    }

    /**
     * @param posY y coordinate
     * @return true if the y coordinate is within the x endpoints of the element, false otherwise
     * @see #posYIsWithinBounds(double, double)
     */
    protected boolean posYIsWithinBounds(double posY) {
        return posYIsWithinBounds(posY, 0);
    }

    /**
     * @return the position of the level element's upper-left corner
     */
    public Position getPosition() {
        return this.pos; // stub
    }

    /**
     * @return the level element's width in pixels
     */
    public int getWidth() {
        return this.width; // stub
    }

    /**
     * @return the level element's height in pixels
     */
    public int getHeight() {
        return this.height; // stub
    }

    /**
     * @return the level element's console colour
     */
    public Color getElementCol() {
        return this.elementCol;
    }

    /**
     * @return extra data to display in the level analysis panel (none)
     */
    public String getExtraDisplayData() {
        return "";
    }
}