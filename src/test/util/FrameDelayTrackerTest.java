package util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FrameDelayTrackerTest {
    FrameDelayTracker fdt;

    @BeforeEach
    public void setup() {
        this.fdt = new FrameDelayTracker(33, 66);
    }

    @Test
    public void constructorTest() {
        assertEquals(33, this.fdt.getClockFrameDelay());
        assertEquals(66, this.fdt.getGraphicalFrameDelay());
        assertEquals(33, this.fdt.getMinFrameDelay());
    }

    @Test
    public void setClockFrameRateTest() {
        this.fdt.setClockFrameDelay(10);
        assertEquals(10, this.fdt.getClockFrameDelay());
        assertEquals(10, this.fdt.getMinFrameDelay());
        this.fdt.setClockFrameDelay(505);
        assertEquals(505, this.fdt.getClockFrameDelay());
        assertEquals(66, this.fdt.getMinFrameDelay());
        this.fdt.setClockFrameDelay(1);
        assertEquals(1, this.fdt.getClockFrameDelay());
        assertEquals(1, this.fdt.getMinFrameDelay());
    }

    @Test
    public void setGraphicalFrameRateTest() {
        this.fdt.setGraphicalFrameDelay(10);
        assertEquals(10, this.fdt.getGraphicalFrameDelay());
        assertEquals(10, this.fdt.getMinFrameDelay());
        this.fdt.setGraphicalFrameDelay(505);
        assertEquals(505, this.fdt.getGraphicalFrameDelay());
        assertEquals(33, this.fdt.getMinFrameDelay());
        this.fdt.setGraphicalFrameDelay(1);
        assertEquals(1, this.fdt.getGraphicalFrameDelay());
        assertEquals(1, this.fdt.getMinFrameDelay());
    }

    @Test
    public void reduceFrameDelaysTest() {
        this.fdt.reduceFrameDelays(10);
        assertEquals(23, this.fdt.getClockFrameDelay());
        assertEquals(56, this.fdt.getGraphicalFrameDelay());
        assertEquals(23, this.fdt.getMinFrameDelay());
        this.fdt.reduceFrameDelays(23);
        assertEquals(0, this.fdt.getClockFrameDelay());
        assertEquals(33, this.fdt.getGraphicalFrameDelay());
        assertEquals(0, this.fdt.getMinFrameDelay());
    }
}
