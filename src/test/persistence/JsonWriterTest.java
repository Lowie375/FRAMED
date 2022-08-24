package persistence;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import util.ColorManager;

import java.awt.*;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTest {
    JSONObject json;
    JsonWriter jsonWriter;

    @Test
    public void existenceTest() {
        jsonWriter = new JsonWriter();
    }

    @Test
    public void badFileNameTest() {
        try {
            JsonWriter.writeToSave("./data/test/bad$!<File>Name.json", 3, "badTest",
                    new ColorManager());
            fail("IOException not thrown");
        } catch (IOException e) {
            // all good, continue
        }
    }

    @Test
    public void writeToSaveFileTest() {
        try {
            JsonWriter.writeToSave("./data/test/testWrittenSave.json", -2, "writtenTest",
                    new ColorManager());
        } catch (IOException e) {
            fail("Unexpected IOException thrown");
        }

        try {
            json = JsonReader.readFile("./data/test/testWrittenSave.json");
        } catch (IOException e) {
            fail("Unexpected IOException thrown");
        }
        assertEquals(3, json.getInt("formatVersion"));
        assertEquals(-2, json.getInt("currentLevelID"));
        assertEquals("writtenTest", json.getString("currentNamespace"));
        assertEquals(ColorManager.DEF_PLAYER_COL.getBlue(),
                json.getJSONObject("col").getJSONObject(ColorManager.KEYS[ColorManager.PLAYER]).getInt("b"));
        assertEquals(ColorManager.DEF_GOAL_COL.getGreen(),
                json.getJSONObject("col").getJSONObject(ColorManager.KEYS[ColorManager.GOAL]).getInt("g"));
        assertEquals(ColorManager.DEF_DIALOG_BASE_COL.getRed(),
                json.getJSONObject("col").getJSONObject(ColorManager.KEYS[ColorManager.DIALOG_BASE]).getInt("r"));

        try {
            JsonWriter.writeToSave("./data/test/testWrittenSave.json", 5, "anotherTest",
                    new ColorManager(new Color(207, 129, 240),
                            ColorManager.DEF_OBST_COL, ColorManager.DEF_WALL_COL, ColorManager.DEF_GOAL_COL,
                            ColorManager.DEF_BG_COL, ColorManager.DEF_TEXT_COL, ColorManager.DEF_DIALOG_BASE_COL,
                            ColorManager.DEF_DIALOG_ACC_COL));
        } catch (IOException e) {
            fail("Unexpected IOException thrown");
        }

        try {
            json = JsonReader.readFile("./data/test/testWrittenSave.json");
        } catch (IOException e) {
            fail("Unexpected IOException thrown");
        }
        assertEquals(3, json.getInt("formatVersion"));
        assertEquals(5, json.getInt("currentLevelID"));
        assertEquals("anotherTest", json.getString("currentNamespace"));
        assertEquals(207,
                json.getJSONObject("col").getJSONObject(ColorManager.KEYS[ColorManager.PLAYER]).getInt("r"));
        assertEquals(129,
                json.getJSONObject("col").getJSONObject(ColorManager.KEYS[ColorManager.PLAYER]).getInt("g"));
        assertEquals(240,
                json.getJSONObject("col").getJSONObject(ColorManager.KEYS[ColorManager.PLAYER]).getInt("b"));
        assertEquals(ColorManager.DEF_TEXT_COL.getRed(),
                json.getJSONObject("col").getJSONObject(ColorManager.KEYS[ColorManager.TEXT]).getInt("r"));
        assertEquals(ColorManager.DEF_DIALOG_ACC_COL.getGreen(),
                json.getJSONObject("col").getJSONObject(ColorManager.KEYS[ColorManager.DIALOG_ACC]).getInt("g"));
        assertEquals(ColorManager.DEF_WALL_COL.getBlue(),
                json.getJSONObject("col").getJSONObject(ColorManager.KEYS[ColorManager.WALL]).getInt("b"));
        /*assertEquals(207, json.getJSONObject("playerCol").getInt("r"));
        assertEquals(129, json.getJSONObject("playerCol").getInt("g"));
        assertEquals(240, json.getJSONObject("playerCol").getInt("b"));*/
    }
}
