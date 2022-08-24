package elem;

import util.ColorManager;
import util.Position;

/**
 * Represents an obstacle with a position, height, width, and element colour, placed with its upper left corner at
 * the given position
 */
public class Obstacle extends LevelElement {

    /**
     * Creates an obstacle at the given position with the given width and height
     *
     * @param pos position of the obstacle's upper left corner (must be within the level's bounds)
     * @param width obstacle width (0 < width < x position + level width)
     * @param height obstacle height (0 < height < y position + level height)
     * @see LevelElement#LevelElement(Position, int, int)
     */
    public Obstacle(Position pos, int width, int height) {
        super(pos, width, height);
        this.elementCol = ColorManager.DEF_OBST_COL;
    }
}
