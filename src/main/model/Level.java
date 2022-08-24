package model;

import elem.LevelElement;
import elem.Obstacle;
import elem.Wall;
import elem.Goal;
import util.CollisionEvent;
import util.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a level with level elements, player spawn position, and perceived frame rate
 */
public class Level {
    public static final int NO_STATE_CHANGE = 0;
    public static final int GOAL_REACHED = 1;
    public static final int RESET = 2;

    private final String namespace;
    private final int id;
    private final Position spawn;
    private final float initialFrameRate;
    private final List<LevelElement> elements;

    /**
     * Creates a level with a namespace, numerical id, a player spawn position, a starting
     * frame rate, and a list of level elements
     *
     * @param namespace category of the level
     * @param id numerical ID within namespace
     * @param spawn player spawn point (must be within the level's bounds)
     * @param initialFrameRate initial frame late of level (must be in TerminalGame.FRAME_RATES)
     * @param elements list of level elements
     */
    public Level(String namespace, int id, Position spawn, float initialFrameRate, List<LevelElement> elements) {
        this.namespace = namespace;
        this.id = id;
        this.spawn = spawn;
        this.initialFrameRate = initialFrameRate;
        this.elements = elements;
    }

    /**
     * Starts the level
     * <p>
     * Places the player at its spawn position
     *
     * @param player player in the level
     */
    public void startLevel(Player player) {
        player.setPosition(spawn.getPosX(), spawn.getPosY());
        player.setDX(0);
        player.setDY(0);
    }

    /**
     * Adds a level element to the level
     *
     * @param elem element to add
     */
    public void addElement(LevelElement elem) {
        this.elements.add(elem);
    }

    /**
     * Removes a level element from the level
     *
     * @param index index of level element to remove
     */
    public void removeElement(int index) {
        this.elements.remove(index);
    }

    /**
     * Handles all events that occur when the player collides with a level element in order of priority,
     * then returns a constant corresponding to a game state change, or NO_CHANGE if the game state did not change
     *
     * @param player player to check collisions for
     * @return collision event representing the appropriate game state change
     */
    public CollisionEvent handlePlayerCollisions(Player player) {
        // priority: wall > obstacle > goal
        handleWallCollisions(player, getWalls());
        return handleStateChangingEvents(player);
    }

    /**
     * Handles all events that may change the state of the Game in order of priority, then returns a constant
     * corresponding to a game state change, or NO_CHANGE if the game state did not change
     * 
     * @param player player to check collisions for
     * @return collision event representing the appropriate game state change
     * @see #handlePlayerCollisions(Player)
     */
    private CollisionEvent handleStateChangingEvents(Player player) {
        int status = handleObstacleCollisions(player, getObstacles());
        if (status != 0) {
            return new CollisionEvent(status);
        } else {
            return handleGoalCollisions(player, getGoals());
        }
    }

    /**
     * Handles player collisions with walls
     * 
     * @param player player to check collisions for
     * @see #handlePlayerCollisions(Player)
     */
    private void handleWallCollisions(Player player, List<Wall> walls) {
        double startY = player.getPosition().getPosY();
        double startX = player.getPosition().getPosX();

        List<Wall> wallsToCheck = new ArrayList<>(walls);
        List<Wall> filteredWalls = new ArrayList<>();
        do {
            filteredWalls.clear();
            for (Wall wall : wallsToCheck) {
                if (wall.isCollidingWithPlayer(player)) {
                    filteredWalls.add(wall);
                }
            }
            for (Wall wall : filteredWalls) {
                wall.onPlayerCollision(player);
                wallsToCheck.remove(wall);
            }
        } while (filteredWalls.size() > 0);

        // finalize collisions
        if (player.getPosition().getPosX() != startX) {
            player.setDX(0);
        }
        if (player.getPosition().getPosY() != startY) {
            player.setDY(0);
        }
    }

    /**
     * Handles player collisions with obstacles
     * 
     * @param player player to check collisions for
     * @return constant representing the appropriate collision event status
     * @see #handlePlayerCollisions(Player)
     */
    private int handleObstacleCollisions(Player player, List<Obstacle> obstacles) {
        for (Obstacle obst : obstacles) {
            if (obst.isCollidingWithPlayer(player)) {
                startLevel(player);
                return RESET;
            }
        }
        return NO_STATE_CHANGE;
    }

    /**
     * Handles player collisions with goals
     *
     * @param player player to check collisions for
     * @return collision event representing the appropriate game state change
     * @see #handlePlayerCollisions(Player)
     */
    private CollisionEvent handleGoalCollisions(Player player, List<Goal> goals) {
        for (Goal goal : goals) {
            if (goal.isCollidingWithPlayer(player)) {
                return new CollisionEvent(GOAL_REACHED, goal.getNextLevelID());
            }
        }
        return new CollisionEvent(NO_STATE_CHANGE);
    }

    /**
     * Filters a list of level elements by class
     *
     * @param elements list of level elements to filter
     * @param className name of class to filter by (must be a subclass of LevelElement)
     * @return a filtered list of level elements
     */
    private List<LevelElement> filterListByClass(List<LevelElement> elements, String className) {
        List<LevelElement> output = new ArrayList<>();
        for (LevelElement elem : elements) {
            if (elem != null && elem.getClass().getName().equals(className)) {
                output.add(elem);
            }
        }
        return output;
    }

    /**
     * @return the level's namespace
     */
    public String getNamespace() {
        return this.namespace;
    }

    /**
     * @return the level's numerical id
     */
    public int getID() {
        return this.id;
    }

    /**
     * @return the level's spawn position
     */
    public Position getSpawn() {
        return this.spawn;
    }

    /**
     * @return level's initial perceived frame rate
     */
    public float getInitialFrameRate() {
        return this.initialFrameRate;
    }

    /**
     * @return the list of all level elements the level contains
     */
    public List<LevelElement> getAllElements() {
        return this.elements;
    }

    /**
     * @return the list of all obstacles the level contains
     */
    public List<Obstacle> getObstacles() {
        List<Obstacle> output = new ArrayList<>();
        for (LevelElement elem : filterListByClass(this.elements, "elem.Obstacle")) {
            Obstacle cleanElem = (Obstacle) elem;
            output.add(cleanElem);
        }
        return output;
    }

    /**
     * @return the list of all walls the level contains
     */
    public List<Wall> getWalls() {
        List<Wall> output = new ArrayList<>();
        for (LevelElement elem : filterListByClass(this.elements, "elem.Wall")) {
            Wall cleanElem = (Wall) elem;
            output.add(cleanElem);
        }
        return output;
    }

    /**
     * @return the list of all walls the level contains
     */
    public List<Goal> getGoals() {
        List<Goal> output = new ArrayList<>();
        for (LevelElement elem : filterListByClass(this.elements, "elem.Goal")) {
            Goal cleanElem = (Goal) elem;
            output.add(cleanElem);
        }
        return output;
    }
}
