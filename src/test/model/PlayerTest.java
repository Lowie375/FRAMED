package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.MovementDirTracker;
import util.Position;

import java.awt.Color;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    Player p1;
    Player p2;

    @BeforeEach
    public void setup() {
        p1 = new Player(new Position(11,25), new Color(0, 150, 99, 255));
        p2 = new Player(new Position(69.5,33.25), new Color(207, 129, 240, 128));
    }

    @Test
    public void constructorTest() {
        assertEquals(11, p1.getPosition().getPosX());
        assertEquals(25, p1.getPosition().getPosY());
        assertEquals(0, p1.getDX());
        assertEquals(0, p1.getDY());
        assertEquals(0, p1.getCol().getRed());
        assertEquals(150, p1.getCol().getGreen());
        assertEquals(99, p1.getCol().getBlue());
        assertEquals(255, p1.getCol().getAlpha());
        assertEquals(MovementDirTracker.RIGHT, p1.getMovementDir().getHorizontalDir());
        assertEquals(MovementDirTracker.DOWN, p1.getMovementDir().getVerticalDir());

        assertEquals(69.5, p2.getPosition().getPosX());
        assertEquals(33.25, p2.getPosition().getPosY());
        assertEquals(0, p2.getDX());
        assertEquals(0, p2.getDY());
        assertEquals(207, p2.getCol().getRed());
        assertEquals(129, p2.getCol().getGreen());
        assertEquals(240, p2.getCol().getBlue());
        assertEquals(128, p2.getCol().getAlpha());
        assertEquals(MovementDirTracker.RIGHT, p2.getMovementDir().getHorizontalDir());
        assertEquals(MovementDirTracker.DOWN, p2.getMovementDir().getVerticalDir());
    }

    @Test
    public void setPositionTest() {
        p1.setPosition(5, 95.5);
        assertEquals(5, p1.getPosition().getPosX());
        assertEquals(95.5, p1.getPosition().getPosY());
        p1.setPosition(13.75, 11);
        assertEquals(13.75, p1.getPosition().getPosX());
        assertEquals(11, p1.getPosition().getPosY());

        assertEquals(69.5, p2.getPosition().getPosX());
        assertEquals(33.25, p2.getPosition().getPosY());
        p2.setPosition(22.5, 40.5);
        assertEquals(22.5, p2.getPosition().getPosX());
        assertEquals(40.5, p2.getPosition().getPosY());
    }

    @Test
    public void setDXTest() {
        p1.setDX(0.5);
        assertEquals(0.5, p1.getDX());
        assertEquals(MovementDirTracker.RIGHT, p1.getMovementDir().getHorizontalDir());
        p1.setDX(-4);
        assertEquals(-4, p1.getDX());
        assertEquals(MovementDirTracker.LEFT, p1.getMovementDir().getHorizontalDir());

        assertEquals(0, p2.getDX());
        assertEquals(MovementDirTracker.RIGHT, p2.getMovementDir().getHorizontalDir());
        p2.setDX(-0.25);
        assertEquals(-0.25, p2.getDX());
        assertEquals(MovementDirTracker.LEFT, p2.getMovementDir().getHorizontalDir());
    }

    @Test
    public void setDYTest() {
        p1.setDY(1.5);
        assertEquals(1.5, p1.getDY());
        assertEquals(MovementDirTracker.DOWN, p1.getMovementDir().getVerticalDir());
        p1.setDY(-3);
        assertEquals(-3, p1.getDY());
        assertEquals(MovementDirTracker.UP, p1.getMovementDir().getVerticalDir());

        assertEquals(0, p2.getDY());
        assertEquals(MovementDirTracker.DOWN, p2.getMovementDir().getVerticalDir());
        p2.setDY(-0.75);
        assertEquals(-0.75, p2.getDY());
        assertEquals(MovementDirTracker.UP, p2.getMovementDir().getVerticalDir());
    }

    @Test
    public void changeDXTest() {
        p1.changeDX(0.5);
        assertEquals(0.5, p1.getDX());
        assertEquals(MovementDirTracker.RIGHT, p1.getMovementDir().getHorizontalDir());
        p1.changeDX(-4);
        assertEquals(-3.5, p1.getDX());
        assertEquals(MovementDirTracker.LEFT, p1.getMovementDir().getHorizontalDir());
        p1.changeDX(2.75);
        assertEquals(-0.75, p1.getDX());
        assertEquals(MovementDirTracker.LEFT, p1.getMovementDir().getHorizontalDir());

        assertEquals(0, p2.getDX());
        assertEquals(MovementDirTracker.RIGHT, p2.getMovementDir().getHorizontalDir());
        p2.changeDX(-0.25);
        assertEquals(-0.25, p2.getDX());
        assertEquals(MovementDirTracker.LEFT, p2.getMovementDir().getHorizontalDir());
        p2.changeDX(-1);
        assertEquals(-1.25, p2.getDX());
        assertEquals(MovementDirTracker.LEFT, p2.getMovementDir().getHorizontalDir());
        p2.changeDX(55);
        assertEquals(53.75, p2.getDX());
        assertEquals(MovementDirTracker.RIGHT, p2.getMovementDir().getHorizontalDir());
    }

    @Test
    public void changeDYTest() {
        p1.changeDY(1.5);
        assertEquals(1.5, p1.getDY());
        assertEquals(MovementDirTracker.DOWN, p1.getMovementDir().getVerticalDir());
        p1.changeDY(-5);
        assertEquals(-3.5, p1.getDY());
        assertEquals(MovementDirTracker.UP, p1.getMovementDir().getVerticalDir());
        p1.changeDY(2);
        assertEquals(-1.5, p1.getDY());
        assertEquals(MovementDirTracker.UP, p1.getMovementDir().getVerticalDir());

        assertEquals(0, p2.getDY());
        assertEquals(MovementDirTracker.DOWN, p2.getMovementDir().getVerticalDir());
        p2.changeDY(-0.75);
        assertEquals(-0.75, p2.getDY());
        assertEquals(MovementDirTracker.UP, p2.getMovementDir().getVerticalDir());
        p2.changeDY(-11.5);
        assertEquals(-12.25, p2.getDY());
        assertEquals(MovementDirTracker.UP, p2.getMovementDir().getVerticalDir());
        p2.changeDY(35);
        assertEquals(22.75, p2.getDY());
        assertEquals(MovementDirTracker.DOWN, p2.getMovementDir().getVerticalDir());
    }

    @Test
    public void movementDirAtZeroTest() {
        p1.setDX(0.5);
        assertEquals(MovementDirTracker.RIGHT, p1.getMovementDir().getHorizontalDir());
        p1.setDX(0);
        assertEquals(MovementDirTracker.RIGHT, p1.getMovementDir().getHorizontalDir());
        p1.setDX(-0.5);
        assertEquals(MovementDirTracker.LEFT, p1.getMovementDir().getHorizontalDir());
        p1.setDX(0);
        assertEquals(MovementDirTracker.LEFT, p1.getMovementDir().getHorizontalDir());

        p2.setDY(0.5);
        assertEquals(MovementDirTracker.DOWN, p2.getMovementDir().getVerticalDir());
        p2.setDY(0);
        assertEquals(MovementDirTracker.DOWN, p2.getMovementDir().getVerticalDir());
        p2.setDY(-0.5);
        assertEquals(MovementDirTracker.UP, p2.getMovementDir().getVerticalDir());
        p2.setDY(0);
        assertEquals(MovementDirTracker.UP, p2.getMovementDir().getVerticalDir());
    }


    @Test
    public void moveTest() {
        p1.setDX(2);
        p1.move();
        assertEquals(13, p1.getPosition().getPosX());
        assertEquals(25, p1.getPosition().getPosY());

        p1.setDY(-0.25);
        p1.move();
        p1.move();
        assertEquals(17, p1.getPosition().getPosX());
        assertEquals(24.5, p1.getPosition().getPosY());

        p1.setDX(-0.75);
        p1.setDY(3);
        p1.move();
        assertEquals(16.25, p1.getPosition().getPosX());
        assertEquals(27.5, p1.getPosition().getPosY());
        p1.move();
        assertEquals(15.5, p1.getPosition().getPosX());
        assertEquals(30.5, p1.getPosition().getPosY());

        p1.setDX(0);
        p1.move();
        p1.move();
        p1.move();
        assertEquals(15.5, p1.getPosition().getPosX());
        assertEquals(39.5, p1.getPosition().getPosY());
    }

    @Test
    public void setColorTest() {
        p1.setCol(new Color(127, 0, 255));
        assertEquals(127, p1.getCol().getRed());
        assertEquals(0, p1.getCol().getGreen());
        assertEquals(255, p1.getCol().getBlue());

        p1.setCol(new Color(222, 104, 104));
        assertEquals(222, p1.getCol().getRed());
        assertEquals(104, p1.getCol().getGreen());
        assertEquals(104, p1.getCol().getBlue());

        p2.setCol(new Color(213, 213, 213));
        assertEquals(213, p2.getCol().getRed());
        assertEquals(213, p2.getCol().getGreen());
        assertEquals(213, p2.getCol().getBlue());
    }
}