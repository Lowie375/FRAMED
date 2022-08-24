package util;

import model.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CollisionEventTest {
    CollisionEvent eventInt;
    CollisionEvent eventString;
    CollisionEvent eventNoData;

    @BeforeEach
    public void setup() {
        eventInt = new CollisionEvent(Level.GOAL_REACHED, 2);
        eventString = new CollisionEvent(Level.NO_STATE_CHANGE, "dummy");
        eventNoData = new CollisionEvent(Level.RESET);
    }

    @Test
    public void constructorTest() {
        assertEquals(Level.GOAL_REACHED, eventInt.getStatus());
        assertEquals(2, eventInt.getExtraData());

        assertEquals(Level.NO_STATE_CHANGE, eventString.getStatus());
        assertEquals("dummy", eventString.getExtraData());

        assertEquals(Level.RESET, eventNoData.getStatus());
        assertNull(eventNoData.getExtraData());
    }
}
