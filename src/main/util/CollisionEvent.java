package util;

/**
 * Represents a player collision event with a status and (optional) extra data
 */
public class CollisionEvent {
    private final int status;
    private final Object extraData;

    /**
     * Creates a collision event with the given status and extra data
     *
     * @param status constant representing a collision event status
     * @param extraData miscellaneous extra data object
     */
    public CollisionEvent(int status, Object extraData) {
        this.status = status;
        this.extraData = extraData;
    }

    /**
     * Creates a collision event with the given status and no extra data
     *
     * @param status constant representing a collision event status
     */
    public CollisionEvent(int status) {
        this.status = status;
        this.extraData = null;
    }

    /**
     * @return the status of the collision event
     */
    public int getStatus() {
        return this.status;
    }

    /**
     * @return the extra data object of the collision event, or a null object if there is none
     */
    public Object getExtraData() {
        return this.extraData;
    }
}
