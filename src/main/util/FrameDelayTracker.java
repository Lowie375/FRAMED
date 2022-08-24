package util;

/**
 * Represents a set of frame delay counters
 */
public class FrameDelayTracker {
    private long clockFrameDelay;
    private long graphicalFrameDelay;
    private long minFrameDelay;

    /**
     * Creates a new set of frame delay counters
     *
     * @param clockFrameDelay delay until the next clock tick, in milliseconds (clockFrameDelay > 0)
     * @param graphicalFrameDelay delay until the next graphical tick, in milliseconds (graphicalFrameDelay > 0)
     */
    public FrameDelayTracker(long clockFrameDelay, long graphicalFrameDelay) {
        this.clockFrameDelay = clockFrameDelay;
        this.graphicalFrameDelay = graphicalFrameDelay;
        this.minFrameDelay = Math.min(clockFrameDelay, graphicalFrameDelay);
    }

    /**
     * Sets the clock frame delay counter to the new rate + updates min value if needed
     *
     * @param newRate new clock delay, in milliseconds (newRate > 0)
     */
    public void setClockFrameDelay(long newRate) {
        this.clockFrameDelay = newRate;
        this.minFrameDelay = Math.min(this.clockFrameDelay, this.graphicalFrameDelay);
    }

    /**
     * Sets the graphical frame delay counter to the new rate + updates min value if needed
     *
     * @param newRate new graphical delay, in milliseconds (newRate > 0)
     */
    public void setGraphicalFrameDelay(long newRate) {
        this.graphicalFrameDelay = newRate;
        this.minFrameDelay = Math.min(this.clockFrameDelay, this.graphicalFrameDelay);
    }

    /**
     * Reduces all the frame delay counters by the amount specified
     *
     * @param reduction time to reduce all frame delay counters by, in milliseconds
     *                  (0 <= reduction <= this.minFrameDelay)
     */
    public void reduceFrameDelays(long reduction) {
        this.clockFrameDelay -= reduction;
        this.graphicalFrameDelay -= reduction;
        this.minFrameDelay -= reduction;
    }

    /**
     * @return the clock frame delay count
     */
    public long getClockFrameDelay() {
        return this.clockFrameDelay;
    }

    /**
     * @return the graphical frame delay count
     */
    public long getGraphicalFrameDelay() {
        return this.graphicalFrameDelay;
    }

    /**
     * @return the lowest frame delay count of all present
     */
    public long getMinFrameDelay() {
        return this.minFrameDelay;
    }
}
