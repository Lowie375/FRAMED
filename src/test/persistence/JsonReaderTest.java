package persistence;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest {
    JSONObject json;
    JsonReader jsonReader;

    @Test
    public void existenceTest() {
        jsonReader = new JsonReader();
    }

    @Test
    public void fileNotFoundTest() {
        try {
            json = JsonReader.readFile("./data/thisFileDoesNotExist.json");
            fail("FileNotFoundException not thrown");
        } catch (FileNotFoundException e) {
            // all good, continue
        } catch (IOException e) {
            fail("Unexpected IOException caught");
        }
    }

    @Test
    public void readSaveFileTest() {
        try {
            json = JsonReader.readFile("./data/test/testV2Save.json");
        } catch (IOException e) {
            fail("Unexpected IOException caught");
        }

        // object assertions
        assertEquals(2, json.getInt("formatVersion"));
        assertEquals(11, json.getInt("currentLevelID"));
        assertEquals("test", json.getString("currentNamespace"));
        assertEquals(207, json.getJSONObject("playerCol").getInt("r"));
        assertEquals(129, json.getJSONObject("playerCol").getInt("g"));
        assertEquals(240, json.getJSONObject("playerCol").getInt("b"));
    }

    @Test
    public void readLevelFile1Test() {
        try {
            json = JsonReader.readFile("./data/test/testLevel1.json");
        } catch (IOException e) {
            fail("Unexpected IOException caught");
        }

        // object assertions
        assertEquals(2, json.getInt("formatVersion"));
        assertEquals("test", json.getJSONObject("metadata").getString("namespace"));
        assertEquals(-1, json.getJSONObject("metadata").getInt("id"));
        assertEquals(17.5, json.getFloat("initialFrameRate"));
        assertEquals(40, json.getJSONObject("spawn").getDouble("posX"));
        assertEquals(50, json.getJSONObject("spawn").getDouble("posY"));

        // array assertions
        assertEquals(3, json.getJSONArray("obstacles").length());
        assertEquals(7, json.getJSONArray("obstacles").getJSONObject(0).getInt("width"));
        assertEquals(5, json.getJSONArray("obstacles").getJSONObject(1).getInt("height"));
        assertEquals(12, json.getJSONArray("obstacles").getJSONObject(1).getJSONObject("pos").getDouble("posX"));
        assertEquals(15, json.getJSONArray("obstacles").getJSONObject(2).getJSONObject("pos").getDouble("posY"));

        assertEquals(2, json.getJSONArray("walls").length());
        assertEquals(3, json.getJSONArray("walls").getJSONObject(0).getInt("width"));
        assertEquals(1, json.getJSONArray("walls").getJSONObject(0).getJSONObject("pos").getDouble("posX"));
        assertEquals(12, json.getJSONArray("walls").getJSONObject(1).getInt("height"));
        assertEquals(10, json.getJSONArray("walls").getJSONObject(1).getJSONObject("pos").getDouble("posY"));

        assertEquals(2, json.getJSONArray("goals").length());
        assertEquals(3, json.getJSONArray("goals").getJSONObject(0).getInt("width"));
        assertEquals(26, json.getJSONArray("goals").getJSONObject(0).getJSONObject("pos").getDouble("posX"));
        assertEquals(-2, json.getJSONArray("goals").getJSONObject(0).getInt("nextLevelID"));
        assertEquals(7, json.getJSONArray("goals").getJSONObject(1).getInt("height"));
        assertEquals(33, json.getJSONArray("goals").getJSONObject(1).getJSONObject("pos").getDouble("posY"));
        assertEquals(3, json.getJSONArray("goals").getJSONObject(1).getInt("nextLevelID"));
    }

    @Test
    public void readLevelFile2Test() {
        try {
            json = JsonReader.readFile("./data/test/testLevel2.json");
        } catch (IOException e) {
            fail("Unexpected IOException caught");
        }

        // object assertions
        assertEquals(2, json.getInt("formatVersion"));
        assertEquals("test", json.getJSONObject("metadata").getString("namespace"));
        assertEquals(-2, json.getJSONObject("metadata").getInt("id"));
        assertEquals(20, json.getFloat("initialFrameRate"));
        assertEquals(1, json.getJSONObject("spawn").getDouble("posX"));
        assertEquals(1, json.getJSONObject("spawn").getDouble("posY"));

        // array assertions
        assertEquals(4, json.getJSONArray("obstacles").length());
        assertEquals(8, json.getJSONArray("obstacles").getJSONObject(0).getInt("width"));
        assertEquals(5, json.getJSONArray("obstacles").getJSONObject(0).getJSONObject("pos").getDouble("posY"));
        assertEquals(10, json.getJSONArray("obstacles").getJSONObject(1).getInt("height"));
        assertEquals(5, json.getJSONArray("obstacles").getJSONObject(1).getJSONObject("pos").getDouble("posX"));
        assertEquals(13, json.getJSONArray("obstacles").getJSONObject(2).getJSONObject("pos").getDouble("posY"));
        assertEquals(4, json.getJSONArray("obstacles").getJSONObject(2).getInt("height"));
        assertEquals(4, json.getJSONArray("obstacles").getJSONObject(3).getInt("width"));
        assertEquals(11, json.getJSONArray("obstacles").getJSONObject(3).getJSONObject("pos").getDouble("posX"));

        assertEquals(3, json.getJSONArray("walls").length());
        assertEquals(2, json.getJSONArray("walls").getJSONObject(0).getInt("height"));
        assertEquals(7, json.getJSONArray("walls").getJSONObject(0).getJSONObject("pos").getDouble("posY"));
        assertEquals(3, json.getJSONArray("walls").getJSONObject(1).getInt("width"));
        assertEquals(0, json.getJSONArray("walls").getJSONObject(1).getJSONObject("pos").getDouble("posY"));
        assertEquals(27, json.getJSONArray("walls").getJSONObject(2).getJSONObject("pos").getDouble("posX"));
        assertEquals(12, json.getJSONArray("walls").getJSONObject(2).getInt("width"));

        assertEquals(1, json.getJSONArray("goals").length());
        assertEquals(36, json.getJSONArray("goals").getJSONObject(0).getJSONObject("pos").getDouble("posX"));
        assertEquals(18, json.getJSONArray("goals").getJSONObject(0).getJSONObject("pos").getDouble("posY"));
        assertEquals(2, json.getJSONArray("goals").getJSONObject(0).getInt("width"));
        assertEquals(2, json.getJSONArray("goals").getJSONObject(0).getInt("height"));
        assertEquals(-3, json.getJSONArray("goals").getJSONObject(0).getInt("nextLevelID"));
    }

}
