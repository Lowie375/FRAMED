package model;

import util.MovementDirTracker;
import util.Position;

import java.awt.Color;

/**
 * Represents a player with current player position, x velocity, y velocity, RGB colour, and movement direction
 */
public class Player {
    public static final int SIZE = 10;
    private final Position pos;
    private Color col;
    private double dx;
    private double dy;
    private final MovementDirTracker movementDir;

    /**
     * Creates a new player at position with the specified hex colour and no x-velocity or y-velocity
     *
     * @param pos player position (must be within the bounds of the level)
     * @param col player colour
     */
    public Player(Position pos, Color col) {
        this.pos = pos;
        this.col = col;
        this.dx = 0;
        this.dy = 0;
        this.movementDir = new MovementDirTracker();
    }

    /**
     * Sets the player's current position to the position specified by the given coordinates
     *
     * @param posX x coordinate (must be within the bounds of the level)
     * @param posY y coordinate (must be within the bounds of the level)
     */
    public void setPosition(double posX, double posY) {
        this.pos.setPosX(posX);
        this.pos.setPosY(posY);
    }

    /**
     * Sets the player's x velocity to the specified amount (in px/game-tick) and updates the player's
     * horizontal movement direction
     *
     * @param dx new x velocity
     */
    public void setDX(double dx) {
        this.dx = dx;
        updateHorizontalDir();
    }

    /**
     * Sets the player's y velocity to the specified amount (in px/game-tick) and updates the player's
     * vertical movement direction
     *
     * @param dy new y velocity
     */
    public void setDY(double dy) {
        this.dy = dy;
        updateVerticalDir();
    }

    /**
     * Changes the player's x velocity by the specified amount (in px/game-tick) and updates the player's
     * horizontal movement direction
     *
     * @param ddx x velocity change (acceleration)
     */
    public void changeDX(double ddx) {
        this.dx += ddx;
        updateHorizontalDir();
    }

    /**
     * Changes the player's y velocity by the specified amount (in px/game-tick) and updates the player's
     * vertical movement direction
     *
     * @param ddy y velocity change (acceleration)
     */
    public void changeDY(double ddy) {
        this.dy += ddy;
        updateVerticalDir();
    }

    /**
     * Updates the player's horizontal movement direction
     */
    private void updateHorizontalDir() {
        if (this.dx > 0) {
            this.movementDir.setHorizontalDirRight();
        } else if (this.dx < 0) {
            this.movementDir.setHorizontalDirLeft();
        }
        // 0 should leave direction unchanged
    }

    /**
     * Updates the player's vertical movement direction
     */
    private void updateVerticalDir() {
        if (this.dy > 0) {
            this.movementDir.setVerticalDirDown();
        } else if (this.dy < 0) {
            this.movementDir.setVerticalDirUp();
        }
        // 0 should leave direction unchanged
    }

    /**
     * Changes the player's position by their current x and y velocities
     */
    public void move() {
        this.pos.changePosX(this.dx);
        this.pos.changePosY(this.dy);
    }

    /**
     * Sets the player's colour to the given colour
     *
     * @param newCol new player colour
     */
    public void setCol(Color newCol) {
        this.col = newCol;
    }

    /**
     * @return the player's current position in the playing area
     */
    public Position getPosition() {
        return this.pos;
    }

    /**
     * @return the player's RGB colour
     */
    public Color getCol() {
        return this.col;
    }

    /**
     * @return the player's current x velocity
     */
    public double getDX() {
        return this.dx;
    }

    /**
     * @return the player's current y velocity
     */
    public double getDY() {
        return this.dy;
    }

    /**
     * @return the player's current movement direction
     */
    public MovementDirTracker getMovementDir() {
        return this.movementDir;
    }

}
