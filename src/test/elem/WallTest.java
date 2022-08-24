package elem;

import model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Position;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class WallTest {
    Wall wall1;
    Wall wall2;
    Player p1;
    Player p2;

    @BeforeEach
    public void setup() {
        wall1 = new Wall(new Position(15, 15), 5, 20); // from (15, 15) to (20, 35)
        wall2 = new Wall(new Position(50, 80), 30, 8); // from (50, 80) to (80, 88)
        p1 = new Player(new Position(31,35), new Color(0, 150, 99, 255));
        p2 = new Player(new Position(69.5,33.25), new Color(207, 129, 240, 128));
    }

    @Test
    public void constructorTest() {
        assertEquals(15, wall1.getPosition().getPosX());
        assertEquals(15, wall1.getPosition().getPosY());
        assertEquals(5, wall1.getWidth());
        assertEquals(20, wall1.getHeight());
        assertEquals(89, wall1.getElementCol().getRed());
        assertEquals(30, wall1.getElementCol().getGreen());
        assertEquals(231, wall1.getElementCol().getBlue());
        assertEquals("", wall1.getExtraDisplayData());

        assertEquals(50, wall2.getPosition().getPosX());
        assertEquals(80, wall2.getPosition().getPosY());
        assertEquals(30, wall2.getWidth());
        assertEquals(8, wall2.getHeight());
        assertEquals(89, wall2.getElementCol().getRed());
        assertEquals(30, wall2.getElementCol().getGreen());
        assertEquals(231, wall2.getElementCol().getBlue());
        assertEquals("", wall2.getExtraDisplayData());
    }

    @Test
    public void isCollidingWithPlayerTest() {
        assertFalse(wall1.isCollidingWithPlayer(p1));
        assertFalse(wall2.isCollidingWithPlayer(p1));
        assertFalse(wall1.isCollidingWithPlayer(p2));
        assertFalse(wall2.isCollidingWithPlayer(p2));

        p1.setPosition(10 + Player.SIZE, 24 - Player.SIZE);
        assertTrue(wall1.isCollidingWithPlayer(p1));
        assertFalse(wall2.isCollidingWithPlayer(p1));

        p2.setPosition(77.5, 79.5 - Player.SIZE);
        assertFalse(wall1.isCollidingWithPlayer(p2));
        assertFalse(wall2.isCollidingWithPlayer(p2));
        p2.setPosition(77.5, 80 - Player.SIZE);
        assertFalse(wall1.isCollidingWithPlayer(p2));
        assertTrue(wall2.isCollidingWithPlayer(p2));
        p2.setPosition(77.5, 80.5 - Player.SIZE);
        assertFalse(wall1.isCollidingWithPlayer(p2));
        assertTrue(wall2.isCollidingWithPlayer(p2));

        p1.setPosition(50 - Player.SIZE, 88 + Player.SIZE);
        assertFalse(wall1.isCollidingWithPlayer(p1));
        assertTrue(wall2.isCollidingWithPlayer(p1));

        p2.setPosition(10 + Player.SIZE, 15 - Player.SIZE);
        assertTrue(wall1.isCollidingWithPlayer(p2));
        assertFalse(wall2.isCollidingWithPlayer(p2));
    }

    @Test
    public void staticHorizPlayerCollisionTest() {
        p1.setPosition(12 - Player.SIZE, 22 - Player.SIZE);
        p1.setDX(2);
        p1.move();
        assertFalse(wall1.isCollidingWithPlayer(p1));
        p1.move();
        assertTrue(wall1.isCollidingWithPlayer(p1));
        wall1.onPlayerCollision(p1);
        assertEquals(14.75 - Player.SIZE, p1.getPosition().getPosX());
        assertEquals(22 - Player.SIZE, p1.getPosition().getPosY());
        assertEquals(2, p1.getDX());

        p1.setDX(0.125);
        p1.move();
        assertFalse(wall1.isCollidingWithPlayer(p1));
        p1.move();
        assertTrue(wall1.isCollidingWithPlayer(p1));
        wall1.onPlayerCollision(p1);
        assertEquals(14.75 - Player.SIZE, p1.getPosition().getPosX());
        assertEquals(22 - Player.SIZE, p1.getPosition().getPosY());
        assertEquals(0.125, p1.getDX());

        p1.setPosition(25 + Player.SIZE, 28);
        p1.setDX(-3);
        p1.move();
        assertFalse(wall1.isCollidingWithPlayer(p1));
        p1.move();
        assertTrue(wall1.isCollidingWithPlayer(p1));
        wall1.onPlayerCollision(p1);
        assertEquals(20.25 + Player.SIZE, p1.getPosition().getPosX());
        assertEquals(28, p1.getPosition().getPosY());
        assertEquals(-3, p1.getDX());

        p1.setDX(-0.125);
        p1.move();
        assertFalse(wall1.isCollidingWithPlayer(p1));
        p1.move();
        assertTrue(wall1.isCollidingWithPlayer(p1));
        wall1.onPlayerCollision(p1);
        assertEquals(20.25 + Player.SIZE, p1.getPosition().getPosX());
        assertEquals(28, p1.getPosition().getPosY());
        assertEquals(-0.125, p1.getDX());
    }

    @Test
    public void staticVertiPlayerCollisionTest() {
        p1.setPosition(17, 12 - Player.SIZE);
        p1.setDY(2);
        p1.move();
        assertFalse(wall1.isCollidingWithPlayer(p1));
        p1.move();
        assertTrue(wall1.isCollidingWithPlayer(p1));
        wall1.onPlayerCollision(p1);
        assertEquals(17, p1.getPosition().getPosX());
        assertEquals(14.75 - Player.SIZE, p1.getPosition().getPosY());
        assertEquals(2, p1.getDY());

        p1.setDY(0.125);
        p1.move();
        assertFalse(wall1.isCollidingWithPlayer(p1));
        p1.move();
        assertTrue(wall1.isCollidingWithPlayer(p1));
        wall1.onPlayerCollision(p1);
        assertEquals(17, p1.getPosition().getPosX());
        assertEquals(14.75 - Player.SIZE, p1.getPosition().getPosY());
        assertEquals(0.125, p1.getDY());

        p1.setPosition(19, 40 + Player.SIZE);
        p1.setDY(-3);
        p1.move();
        assertFalse(wall1.isCollidingWithPlayer(p1));
        p1.move();
        assertTrue(wall1.isCollidingWithPlayer(p1));
        wall1.onPlayerCollision(p1);
        assertEquals(19, p1.getPosition().getPosX());
        assertEquals(35.25 + Player.SIZE, p1.getPosition().getPosY());
        assertEquals(-3, p1.getDY());

        p1.setDY(-0.125);
        p1.move();
        assertFalse(wall1.isCollidingWithPlayer(p1));
        p1.move();
        assertTrue(wall1.isCollidingWithPlayer(p1));
        wall1.onPlayerCollision(p1);
        assertEquals(19, p1.getPosition().getPosX());
        assertEquals(35.25 + Player.SIZE, p1.getPosition().getPosY());
        assertEquals(-0.125, p1.getDY());
    }

    //wall1 = from (5, 5) to (10, 25)
    //wall2 = from (50, 80) to (80, 88)

    @Test
    public void dynamic1DirPlayerCollisionTest() {
        p2.setPosition(81.25 + Player.SIZE, 80.5 - Player.SIZE);
        p2.setDX(-1.5);
        p2.setDY(2);
        assertFalse(wall2.isCollidingWithPlayer(p2));
        p2.move();
        assertTrue(wall2.isCollidingWithPlayer(p2));
        wall2.onPlayerCollision(p2);
        assertEquals(80.25 + Player.SIZE, p2.getPosition().getPosX());
        assertEquals(82.5 - Player.SIZE, p2.getPosition().getPosY());
        assertEquals(-1.5, p2.getDX());
        assertEquals(2, p2.getDY());

        p2.setPosition(67.5 - Player.SIZE, 78.5 - Player.SIZE);
        p2.setDX(0.5);
        p2.setDY(2);
        assertFalse(wall2.isCollidingWithPlayer(p2));
        p2.move();
        assertTrue(wall2.isCollidingWithPlayer(p2));
        wall2.onPlayerCollision(p2);
        assertEquals(68 - Player.SIZE, p2.getPosition().getPosX());
        assertEquals(79.75 - Player.SIZE, p2.getPosition().getPosY());
        assertEquals(0.5, p2.getDX());
        assertEquals(2, p2.getDY());
    }

    @Test
    public void dynamicDisplacementPlayerCollisionTest() {
        // displacement test 1: y displacement < x displacement, so y should bounce
        p2.setPosition(48 - Player.SIZE, 78.25 - Player.SIZE);
        p2.setDX(3.5);
        p2.setDY(2);
        assertFalse(wall2.isCollidingWithPlayer(p2));
        p2.move();
        assertTrue(wall2.isCollidingWithPlayer(p2));
        wall2.onPlayerCollision(p2);
        assertEquals(51.5 - Player.SIZE, p2.getPosition().getPosX());
        assertEquals(79.75 - Player.SIZE, p2.getPosition().getPosY());
        assertEquals(3.5, p2.getDX());
        assertEquals(2, p2.getDY());

        // displacement test 2: x displacement < y displacement, so x should bounce
        p2.setPosition(48.25 - Player.SIZE, 89.25 + Player.SIZE);
        p2.setDX(2.5);
        p2.setDY(-2.25);
        assertFalse(wall2.isCollidingWithPlayer(p2));
        p2.move();
        assertTrue(wall2.isCollidingWithPlayer(p2));
        wall2.onPlayerCollision(p2);
        assertEquals(49.75 - Player.SIZE, p2.getPosition().getPosX());
        assertEquals(87 + Player.SIZE, p2.getPosition().getPosY());
        assertEquals(2.5, p2.getDX());
        assertEquals(-2.25, p2.getDY());
    }

    @Test
    public void dynamicVelocityPlayerCollisionTest() {
        // corner velocity test: |dy| < |dx| so x should reset
        p2.setPosition(82 + Player.SIZE, 78.5 - Player.SIZE);
        p2.setDX(-2.5);
        p2.setDY(2);
        assertFalse(wall2.isCollidingWithPlayer(p2));
        p2.move();
        assertTrue(wall2.isCollidingWithPlayer(p2));
        wall2.onPlayerCollision(p2);
        assertEquals(80.25 + Player.SIZE, p2.getPosition().getPosX());
        assertEquals(80.5 - Player.SIZE, p2.getPosition().getPosY());
        assertEquals(-2.5, p2.getDX());
        assertEquals(2, p2.getDY());

        // corner velocity test: |dx| < |dy| so y should reset
        p2.setPosition(48.5 - Player.SIZE, 78 - Player.SIZE);
        p2.setDX(1.5);
        p2.setDY(2);
        assertFalse(wall2.isCollidingWithPlayer(p2));
        p2.move();
        assertTrue(wall2.isCollidingWithPlayer(p2));
        wall2.onPlayerCollision(p2);
        assertEquals(50 - Player.SIZE, p2.getPosition().getPosX());
        assertEquals(79.75 - Player.SIZE, p2.getPosition().getPosY());
        assertEquals(1.5, p2.getDX());
        assertEquals(2, p2.getDY());
    }

    @Test
    public void dynamicCornerPlayerCollisionTest() {
        // perfect corner test: both should bounce
        p2.setPosition(81.75 + Player.SIZE, 89.75 + Player.SIZE);
        p2.setDX(-2.5);
        p2.setDY(-2.5);
        assertFalse(wall2.isCollidingWithPlayer(p2));
        p2.move();
        assertTrue(wall2.isCollidingWithPlayer(p2));
        wall2.onPlayerCollision(p2);
        assertEquals(80.25 + Player.SIZE, p2.getPosition().getPosX());
        assertEquals(88.25 + Player.SIZE, p2.getPosition().getPosY());
        assertEquals(-2.5, p2.getDX());
        assertEquals(-2.5, p2.getDY());
    }

    @Test
    public void dynamicMultiReversePlayerCollisionTest() {
        // edge test: both are deep within wall, y should eventually bounce
        p2.setPosition(67.5 - Player.SIZE, 78.5 - Player.SIZE);
        p2.setDX(0.5);
        p2.setDY(2);
        assertFalse(wall2.isCollidingWithPlayer(p2));
        p2.move();
        p2.move();
        assertTrue(wall2.isCollidingWithPlayer(p2));
        wall2.onPlayerCollision(p2);
        assertEquals(68.5 - Player.SIZE, p2.getPosition().getPosX());
        assertEquals(79.75 - Player.SIZE, p2.getPosition().getPosY());
        assertEquals(0.5, p2.getDX());
        assertEquals(2, p2.getDY());

        // perfect corner edge test: both are deep within wall, both should bounce
        p2.setPosition(81.75 + Player.SIZE, 89.75 + Player.SIZE);
        p2.setDX(-2.5);
        p2.setDY(-2.5);
        assertFalse(wall2.isCollidingWithPlayer(p2));
        p2.move();
        p2.move();
        assertTrue(wall2.isCollidingWithPlayer(p2));
        wall2.onPlayerCollision(p2);
        assertEquals(80.25 + Player.SIZE, p2.getPosition().getPosX());
        assertEquals(88.25 + Player.SIZE, p2.getPosition().getPosY());
        assertEquals(-2.5, p2.getDX());
        assertEquals(-2.5, p2.getDY());
    }

    @Test
    public void playerCollisionFallbackTest() {
        p1.setPosition(16 - Player.SIZE, 22 - Player.SIZE);
        assertTrue(wall1.isCollidingWithPlayer(p1));
        wall1.onPlayerCollision(p1);
        assertEquals(14.75 - Player.SIZE, p1.getPosition().getPosX());
        assertEquals(22 - Player.SIZE, p1.getPosition().getPosY());
        assertEquals(0, p1.getDX());
        assertEquals(0, p1.getDY());

        p1.setPosition(16 - Player.SIZE, 15.5 - Player.SIZE);
        assertTrue(wall1.isCollidingWithPlayer(p1));
        wall1.onPlayerCollision(p1);
        assertEquals(16 - Player.SIZE, p1.getPosition().getPosX());
        assertEquals(14.75 - Player.SIZE, p1.getPosition().getPosY());
        assertEquals(0, p1.getDX());
        assertEquals(0, p1.getDY());

        p1.setPosition(16.03125 - Player.SIZE, 16 - Player.SIZE);
        assertTrue(wall1.isCollidingWithPlayer(p1));
        wall1.onPlayerCollision(p1);
        assertEquals(16.03125 - Player.SIZE, p1.getPosition().getPosX());
        assertEquals(14.75 - Player.SIZE, p1.getPosition().getPosY());
        assertEquals(0, p1.getDX());
        assertEquals(0, p1.getDY());
    }
}