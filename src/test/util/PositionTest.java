package util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {
    Position pos;

    @BeforeEach
    public void setup() {
        this.pos = new Position(1, 5.25);
    }

    @Test
    public void constructorTest() {
        assertEquals(1, this.pos.getPosX());
        assertEquals(5.25, this.pos.getPosY());
    }

    @Test
    public void setPosXTest() {
        this.pos.setPosX(10);
        assertEquals(10, this.pos.getPosX());
        this.pos.setPosX(505.5);
        assertEquals(505.5, this.pos.getPosX());
        this.pos.setPosX(0);
        assertEquals(0, this.pos.getPosX());
    }

    @Test
    public void setPosYTest() {
        this.pos.setPosY(10);
        assertEquals(10, this.pos.getPosY());
        this.pos.setPosY(505.5);
        assertEquals(505.5, this.pos.getPosY());
        this.pos.setPosY(0);
        assertEquals(0, this.pos.getPosY());
    }

    @Test
    public void changePosXTest() {
        this.pos.changePosX(10);
        assertEquals(11, this.pos.getPosX());
        this.pos.changePosX(505.5);
        assertEquals(516.5, this.pos.getPosX());
        this.pos.changePosX(-44.25);
        assertEquals(472.25, this.pos.getPosX());
        this.pos.changePosX(0);
        assertEquals(472.25, this.pos.getPosX());
    }

    @Test
    public void changePosYTest() {
        this.pos.changePosY(10);
        assertEquals(15.25, this.pos.getPosY());
        this.pos.changePosY(505.5);
        assertEquals(520.75, this.pos.getPosY());
        this.pos.changePosY(-44.75);
        assertEquals(476, this.pos.getPosY());
        this.pos.changePosY(0);
        assertEquals(476, this.pos.getPosY());
    }
}
