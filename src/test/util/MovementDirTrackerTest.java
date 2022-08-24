package util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MovementDirTrackerTest {
    MovementDirTracker movementDir;

    @BeforeEach
    public void setup() {
        movementDir = new MovementDirTracker();
    }

    @Test
    public void constructorTest() {
        assertEquals(MovementDirTracker.RIGHT, movementDir.getHorizontalDir());
        assertEquals(MovementDirTracker.DOWN, movementDir.getVerticalDir());
    }

    @Test
    public void horizontalDirTest() {
        movementDir.setHorizontalDirLeft();
        assertEquals(MovementDirTracker.LEFT, movementDir.getHorizontalDir());
        assertEquals(MovementDirTracker.DOWN, movementDir.getVerticalDir());
        movementDir.setHorizontalDirRight();
        assertEquals(MovementDirTracker.RIGHT, movementDir.getHorizontalDir());
        assertEquals(MovementDirTracker.DOWN, movementDir.getVerticalDir());
    }

    @Test
    public void verticalDirTest() {
        movementDir.setVerticalDirUp();
        assertEquals(MovementDirTracker.UP, movementDir.getVerticalDir());
        assertEquals(MovementDirTracker.RIGHT, movementDir.getHorizontalDir());
        movementDir.setVerticalDirDown();
        assertEquals(MovementDirTracker.DOWN, movementDir.getVerticalDir());
        assertEquals(MovementDirTracker.RIGHT, movementDir.getHorizontalDir());
    }

    @Test
    public void multiDirTest() {
        movementDir.setHorizontalDirLeft();
        movementDir.setVerticalDirDown();
        assertEquals(MovementDirTracker.LEFT, movementDir.getHorizontalDir());
        assertEquals(MovementDirTracker.DOWN, movementDir.getVerticalDir());

        movementDir.setHorizontalDirRight();
        assertEquals(MovementDirTracker.RIGHT, movementDir.getHorizontalDir());
        assertEquals(MovementDirTracker.DOWN, movementDir.getVerticalDir());

        movementDir.setVerticalDirUp();
        assertEquals(MovementDirTracker.RIGHT, movementDir.getHorizontalDir());
        assertEquals(MovementDirTracker.UP, movementDir.getVerticalDir());
    }
}
