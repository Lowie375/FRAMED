package util;

import exception.LevelNotFoundException;
import model.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class LevelLoaderTest {
    private LevelLoader testLoader1;
    private LevelLoader testLoader2;

    @BeforeEach
    @Test
    public void setup() {
        try {
            testLoader1 = new LevelLoader(new File("./data/test/level"));
            testLoader2 = new LevelLoader(new File("./data/test/level/experimental"));
        } catch (Exception e) {
            fail("Constructor failed to initialize");
        }
    }

    @Test
    public void loadLevelsSuccessTest() {
        try {
            Level level1 = testLoader1.loadLevel("test", -1);
            Level level2 = testLoader1.loadLevel("otherTest", -2);
            Level level3 = testLoader1.loadLevel("test", 375);
            Level level4 = testLoader2.loadLevel("otherTest", -2);

            assertEquals(-1, level1.getID());
            assertEquals(-2, level2.getID());
            assertEquals(375, level3.getID());
            assertEquals(-2, level4.getID());
            assertEquals("test", level1.getNamespace());
            assertEquals("otherTest", level2.getNamespace());
            assertEquals("test", level3.getNamespace());
            assertEquals("otherTest", level4.getNamespace());
        } catch (Exception e) {
            fail("Unexpected exception thrown");
        }
    }

    @Test
    public void loadLevelsFailTest() {
        try {
            testLoader1.loadLevel("test", 4);
            fail("Exception not thrown");
        } catch (LevelNotFoundException e) {
            // continue
        } catch (Exception e) {
            fail("Incorrect exception type thrown");
        }

        try {
            testLoader1.loadLevel("baba", -1);
            fail("Exception not thrown");
        } catch (LevelNotFoundException e) {
            // continue
        } catch (Exception e) {
            fail("Incorrect exception type thrown");
        }

        try {
            testLoader2.loadLevel("yeeeeah", 0);
            fail("Exception not thrown");
        } catch (LevelNotFoundException e) {
            // continue
        } catch (Exception e) {
            fail("Incorrect exception type thrown");
        }
    }

    @Test
    public void loadLevelsNotInLoaderTest() {
        try {
            Level level = testLoader1.loadLevel("test", -1);
            assertEquals(3, level.getObstacles().size());
            assertEquals(2, level.getWalls().size());
            assertEquals(2, level.getGoals().size());
        } catch (Exception e) {
            fail("Unexpected exception thrown");
        }

        try {
            testLoader2.loadLevel("test", -1);
            fail("Exception not thrown");
        } catch (LevelNotFoundException e) {
            // continue
        } catch (Exception e) {
            fail("Incorrect exception type thrown");
        }
    }
}
