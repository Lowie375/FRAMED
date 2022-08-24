package elem;

import model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Position;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class ObstacleTest {
    Obstacle obs1;
    Obstacle obs2;
    Player p1;
    Player p2;

    @BeforeEach
    public void setup() {
        obs1 = new Obstacle(new Position(5, 5), 5, 20); // from (5, 5) to (10, 25)
        obs2 = new Obstacle(new Position(50, 80), 30, 8); // from (50, 80) to (80, 88)
        p1 = new Player(new Position(21,35), new Color(0, 150, 99, 255));
        p2 = new Player(new Position(69.5,33.25), new Color(207, 129, 240, 128));
    }

    @Test
    public void constructorTest() {
        assertEquals(5, obs1.getPosition().getPosX());
        assertEquals(5, obs1.getPosition().getPosY());
        assertEquals(5, obs1.getWidth());
        assertEquals(20, obs1.getHeight());
        assertEquals(234, obs1.getElementCol().getRed());
        assertEquals(17, obs1.getElementCol().getGreen());
        assertEquals(75, obs1.getElementCol().getBlue());
        assertEquals("", obs1.getExtraDisplayData());

        assertEquals(50, obs2.getPosition().getPosX());
        assertEquals(80, obs2.getPosition().getPosY());
        assertEquals(30, obs2.getWidth());
        assertEquals(8, obs2.getHeight());
        assertEquals(234, obs2.getElementCol().getRed());
        assertEquals(17, obs2.getElementCol().getGreen());
        assertEquals(75, obs2.getElementCol().getBlue());
        assertEquals("", obs2.getExtraDisplayData());
    }

    @Test
    public void isCollidingWithPlayerTest() {
        assertFalse(obs1.isCollidingWithPlayer(p1));
        assertFalse(obs2.isCollidingWithPlayer(p1));
        assertFalse(obs1.isCollidingWithPlayer(p2));
        assertFalse(obs2.isCollidingWithPlayer(p2));

        p1.setPosition(10 + Player.SIZE, 24 - Player.SIZE);
        assertTrue(obs1.isCollidingWithPlayer(p1));
        assertFalse(obs2.isCollidingWithPlayer(p1));

        p2.setPosition(77.5, 79.5 - Player.SIZE);
        assertFalse(obs1.isCollidingWithPlayer(p2));
        assertFalse(obs2.isCollidingWithPlayer(p2));
        p2.setPosition(77.5, 80 - Player.SIZE);
        assertFalse(obs1.isCollidingWithPlayer(p2));
        assertTrue(obs2.isCollidingWithPlayer(p2));
        p2.setPosition(77.5, 80.5 - Player.SIZE);
        assertFalse(obs1.isCollidingWithPlayer(p2));
        assertTrue(obs2.isCollidingWithPlayer(p2));

        p1.setPosition(50 - Player.SIZE, 88 + Player.SIZE);
        assertFalse(obs1.isCollidingWithPlayer(p1));
        assertTrue(obs2.isCollidingWithPlayer(p1));

        p2.setPosition(10 + Player.SIZE, 5 - Player.SIZE);
        assertTrue(obs1.isCollidingWithPlayer(p2));
        assertFalse(obs2.isCollidingWithPlayer(p2));
    }
}
