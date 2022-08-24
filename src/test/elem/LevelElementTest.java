package elem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Position;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class LevelElementTest {
    LevelElement elem1;
    LevelElement elem2;

    @BeforeEach
    public void setup() {
        elem1 = new Goal(new Position(5, 10), 6, 7, -11);
        elem2 = new Obstacle(new Position(1, 2), 3, 4);
    }

    @Test
    public void posXBoundsTest() {
        assertFalse(elem1.posXIsWithinBounds(4.5));
        assertTrue(elem1.posXIsWithinBounds(5));
        assertTrue(elem1.posXIsWithinBounds(5.5));
        assertTrue(elem1.posXIsWithinBounds(11));
        assertFalse(elem1.posXIsWithinBounds(11.25));

        assertFalse(elem2.posXIsWithinBounds(0));
        assertTrue(elem2.posXIsWithinBounds(1));
        assertTrue(elem2.posXIsWithinBounds(3));
        assertTrue(elem2.posXIsWithinBounds(4));
        assertFalse(elem2.posXIsWithinBounds(5));
    }

    @Test
    public void posYBoundsTest() {
        assertFalse(elem1.posYIsWithinBounds(8));
        assertTrue(elem1.posYIsWithinBounds(10));
        assertTrue(elem1.posYIsWithinBounds(11));
        assertTrue(elem1.posYIsWithinBounds(17));
        assertFalse(elem1.posYIsWithinBounds(33));

        assertFalse(elem2.posYIsWithinBounds(1.5));
        assertTrue(elem2.posYIsWithinBounds(2));
        assertTrue(elem2.posYIsWithinBounds(3.75));
        assertTrue(elem2.posYIsWithinBounds(6));
        assertFalse(elem2.posYIsWithinBounds(6.125));
    }

    @Test
    public void setElementColTest() {
        elem1.setElementCol(new Color(126, 255, 175));
        assertEquals(126, elem1.getElementCol().getRed());
        assertEquals(255, elem1.getElementCol().getGreen());
        assertEquals(175, elem1.getElementCol().getBlue());

        elem2.setElementCol(new Color(0, 150, 99));
        assertEquals(0, elem2.getElementCol().getRed());
        assertEquals(150, elem2.getElementCol().getGreen());
        assertEquals(99, elem2.getElementCol().getBlue());
    }
}
