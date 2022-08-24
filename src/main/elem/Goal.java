package elem;

import util.ColorManager;
import util.Position;

/**
 * Represents a goal with a position, height, width, element colour, and next level ID, placed with its upper left
 * corner at the given position
 */
public class Goal extends LevelElement {
    private final int nextLevelID;

    /**
     * Creates a goal at the given position with the given width, height, and the numerical ID of the level
     * the goal leads to
     *
     * @param pos position of the goal's upper left corner (must be within the level's bounds)
     * @param width goal width (0 < width < x position + level width)
     * @param height goal height (0 < height < y position + level height)
     * @param nextLevelID ID of level to load once goal is reached (must be a valid level ID)
     * @see LevelElement#LevelElement(Position, int, int) 
     */
    public Goal(Position pos, int width, int height, int nextLevelID) {
        super(pos, width, height);
        this.nextLevelID = nextLevelID;
        this.elementCol = ColorManager.DEF_GOAL_COL;
    }

    /**
     * @return numerical ID of level to load once goal is reached
     */
    public int getNextLevelID() {
        return this.nextLevelID;
    }

    /**
     * Returns the extra data to display in the level analysis panel
     *
     * @return stringified ID of level to load once goal is reached
     */
    @Override
    public String getExtraDisplayData() {
        return " to " + "main[" + this.nextLevelID + "]";
    }
}
