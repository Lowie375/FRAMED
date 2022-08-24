package elem;

import model.Player;
import util.ColorManager;
import util.Position;

/**
 * Represents a wall with a position, height, width, and element colour, placed with its upper left corner at
 * the given position
 */
public class Wall extends LevelElement {

    /**
     * Creates a wall at the given position with the given width and height
     *
     * @param pos position of the wall's upper left corner (must be within the level's bounds)
     * @param width wall width (0 < width < x position + level width)
     * @param height wall height (0 < height < y position + level height)
     * @see LevelElement#LevelElement(Position, int, int)
     */
    public Wall(Position pos, int width, int height) {
        super(pos, width, height);
        this.elementCol = ColorManager.DEF_WALL_COL;
    }

    /**
     * Stops the player from moving in the direction of the wall + moves the player off of the wall
     *
     * @param player player to move (must be colliding with the wall)
     */
    public void onPlayerCollision(Player player) {
        // {fallback, core}
        boolean[] activity = {false, true};
        // fallback: set player dx/dy to a small amount to get the player un-stuck
        if (player.getDY() == 0 && player.getDX() == 0) {
            player.setDX(player.getMovementDir().getHorizontalDir() * 0.0625);
            player.setDY(player.getMovementDir().getVerticalDir() * 0.0625);
            activity[0] = true;
        }

        // {x, y}
        double[] reverseArr = {player.getPosition().getPosX(), player.getPosition().getPosY()};

        // basic checks using reversing: go until one is out of the wall
        while (activity[1] && (posXIsWithinBounds(reverseArr[0], Player.SIZE)
                || posYIsWithinBounds(reverseArr[1], Player.SIZE))) {
            reverseArr[0] += -1 * player.getDX();
            reverseArr[1] += -1 * player.getDY();

            if (player.getDY() == 0 || (!posXIsWithinBounds(reverseArr[0], Player.SIZE)
                    && posYIsWithinBounds(reverseArr[1], Player.SIZE))) {
                bouncePlayerX(player, reverseArr[0]);
                activity[1] = false;
            } else if (player.getDX() == 0 || (posXIsWithinBounds(reverseArr[0], Player.SIZE)
                    && !posYIsWithinBounds(reverseArr[1], Player.SIZE))) {
                bouncePlayerY(player, reverseArr[1]);
                activity[1] = false;
            }
        }

        // advanced checks using displacement + velocity
        advancedCollisionCheck(player, reverseArr[0], reverseArr[1], activity[1]);
        handleFallback(player, activity[0]);
    }

    /**
     * Performs deeper checks for stopping the player upon collision with the wall, then stops the player from
     * moving in the direction of the wall + moves the player off of the wall
     *
     * @param player player to move (must be colliding with the wall)
     * @param reverseX x coordinate the player would move to upon repeated movement reversals
     * @param reverseY y coordinate the player would move to upon repeated movement reversals
     * @param coreActive true if collision algorithm is still running, false otherwise
     * @see #onPlayerCollision(Player)
     */
    private void advancedCollisionCheck(Player player, double reverseX, double reverseY, boolean coreActive) {
        if (coreActive) {
            if (Math.abs(calculateXDisplacement(player)) < Math.abs(calculateYDisplacement(player))) {
                bouncePlayerX(player, reverseX);
            } else if (Math.abs(calculateXDisplacement(player)) > Math.abs(calculateYDisplacement(player))) {
                bouncePlayerY(player, reverseY);
            } else if (Math.abs(player.getDX()) < Math.abs(player.getDY())) {
                bouncePlayerY(player, reverseY);
            } else if (Math.abs(player.getDX()) > Math.abs(player.getDY())) {
                bouncePlayerX(player, reverseX);
            } else {
                // fallback + identical hit on corner case: bounce both directions
                bouncePlayerX(player, reverseX);
                bouncePlayerY(player, reverseY);
            }
        }
    }

    /**
     * If the fallback safety measure is active, sets the player's x and y velocities back to 0
     *
     * @param player player to move (must be colliding with the wall)
     * @param fallbackActive true if the fallback safety measure is active, false otherwise
     * @see #onPlayerCollision(Player)
     */
    private void handleFallback(Player player, boolean fallbackActive) {
        if (fallbackActive) {
            player.setDX(0);
            player.setDY(0);
        }
    }

    /**
     * Calculates the player's total horizontal displacement into the wall
     *
     * @param player player to move (must be colliding with the wall, x velocity != 0)
     * @return player's total horizontal displacement into the wall
     * @see #onPlayerCollision(Player)
     */
    private double calculateXDisplacement(Player player) {
        if (player.getDX() > 0) {
            return player.getPosition().getPosX() + Player.SIZE - this.pos.getPosX();
        } else {
            return this.pos.getPosX() + this.width - player.getPosition().getPosX() + Player.SIZE;
        }
    }

    /**
     * Calculates the player's total vertical displacement into the wall
     *
     * @param player player to move (must be colliding with the wall, y velocity != 0)
     * @return player's total vertical displacement into the wall
     * @see #onPlayerCollision(Player)
     */
    private double calculateYDisplacement(Player player) {
        if (player.getDY() > 0) {
            return player.getPosition().getPosY() + Player.SIZE - this.pos.getPosY();
        } else {
            return this.pos.getPosY() + this.height - player.getPosition().getPosY() + Player.SIZE;
        }
    }

    /**
     * Resets the player's x velocity to 0 + moves the player off of the wall (horizontally)
     *
     * @param player player to move (must be colliding with the wall)
     * @param reverseX x coordinate the player would move to upon repeated movement reversals
     * @see #onPlayerCollision(Player)
     */
    private void bouncePlayerX(Player player, double reverseX) {
        if (reverseX <= this.pos.getPosX()) {
            player.setPosition(this.pos.getPosX() - Player.SIZE - 0.25, player.getPosition().getPosY());
        } else {
            player.setPosition(this.pos.getPosX() + Player.SIZE + this.width + 0.25, player.getPosition().getPosY());
        }
    }

    /**
     * Resets the player's y velocity to 0 + moves the player off of the wall (vertically)
     *
     * @param player player to move (must be colliding with the wall)
     * @param reverseY y coordinate the player would move to upon repeated movement reversals
     * @see #onPlayerCollision(Player)
     */
    private void bouncePlayerY(Player player, double reverseY) {
        if (reverseY <= this.pos.getPosY()) {
            player.setPosition(player.getPosition().getPosX(), this.pos.getPosY() - Player.SIZE - 0.25);
        } else {
            player.setPosition(player.getPosition().getPosX(), this.pos.getPosY() + Player.SIZE + this.height + 0.25);
        }
    }
}
