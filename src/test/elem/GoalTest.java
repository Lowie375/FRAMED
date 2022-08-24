package elem;

import model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Position;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class GoalTest {
    Goal goal1;
    Goal goal2;
    Player p1;
    Player p2;

    @BeforeEach
    public void setup() {
        goal1 = new Goal(new Position(5, 5), 5, 20, -1); // from (5, 5) to (10, 25)
        goal2 = new Goal(new Position(50, 80), 30, 8, -2); // from (50, 80) to (80, 88)
        p1 = new Player(new Position(21,35), new Color(0, 150, 99, 255));
        p2 = new Player(new Position(69.5,33.25), new Color(207, 129, 240, 128));
    }

    @Test
    public void constructorTest() {
        assertEquals(5, goal1.getPosition().getPosX());
        assertEquals(5, goal1.getPosition().getPosY());
        assertEquals(5, goal1.getWidth());
        assertEquals(20, goal1.getHeight());
        assertEquals(-1, goal1.getNextLevelID());
        assertEquals(246, goal1.getElementCol().getRed());
        assertEquals(168, goal1.getElementCol().getGreen());
        assertEquals(10, goal1.getElementCol().getBlue());
        assertEquals(" to main[-1]", goal1.getExtraDisplayData());

        assertEquals(50, goal2.getPosition().getPosX());
        assertEquals(80, goal2.getPosition().getPosY());
        assertEquals(30, goal2.getWidth());
        assertEquals(8, goal2.getHeight());
        assertEquals(-2, goal2.getNextLevelID());
        assertEquals(246, goal2.getElementCol().getRed());
        assertEquals(168, goal2.getElementCol().getGreen());
        assertEquals(10, goal2.getElementCol().getBlue());
        assertEquals(" to main[-2]", goal2.getExtraDisplayData());
    }

    @Test
    public void isCollidingWithPlayerTest() {
        assertFalse(goal1.isCollidingWithPlayer(p1));
        assertFalse(goal2.isCollidingWithPlayer(p1));
        assertFalse(goal1.isCollidingWithPlayer(p2));
        assertFalse(goal2.isCollidingWithPlayer(p2));

        p1.setPosition(10 + Player.SIZE, 24 - Player.SIZE);
        assertTrue(goal1.isCollidingWithPlayer(p1));
        assertFalse(goal2.isCollidingWithPlayer(p1));

        p2.setPosition(77.5, 79.5 - Player.SIZE);
        assertFalse(goal1.isCollidingWithPlayer(p2));
        assertFalse(goal2.isCollidingWithPlayer(p2));
        p2.setPosition(77.5, 80 - Player.SIZE);
        assertFalse(goal1.isCollidingWithPlayer(p2));
        assertTrue(goal2.isCollidingWithPlayer(p2));
        p2.setPosition(77.5, 80.5 - Player.SIZE);
        assertFalse(goal1.isCollidingWithPlayer(p2));
        assertTrue(goal2.isCollidingWithPlayer(p2));

        p1.setPosition(50 - Player.SIZE, 88 + Player.SIZE);
        assertFalse(goal1.isCollidingWithPlayer(p1));
        assertTrue(goal2.isCollidingWithPlayer(p1));

        p2.setPosition(10 + Player.SIZE, 5 - Player.SIZE);
        assertTrue(goal1.isCollidingWithPlayer(p2));
        assertFalse(goal2.isCollidingWithPlayer(p2));
    }
}