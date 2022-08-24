package util;

import model.Level;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import persistence.JsonReader;

import java.awt.*;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonParserTest {
    JsonParser jsonParser;
    JSONObject json;
    Level level;
    Color color;

    @Test
    public void existenceTest() {
        jsonParser = new JsonParser();
    }

    @Test
    public void wrongFormatTest() {
        try {
            json = JsonReader.readFile("./data/test/randomTestFile.json");
        } catch (IOException e) {
            fail("Unexpected IOException caught");
        }
        try {
            level = JsonParser.jsonToLevel(json);
            fail("JSONException not caught");
        } catch (JSONException e) {
            // all good, continue
        }
        try {
            color = JsonParser.jsonToColor(json);
            fail("JSONException not caught");
        } catch (JSONException e) {
            // all good, continue
        }
    }

    @Test
    public void jsonToLevelTest() {
        try {
            json = JsonReader.readFile("./data/test/testLevel1.json");
        } catch (IOException e) {
            fail("Unexpected IOException caught");
        }
        try {
            level = JsonParser.jsonToLevel(json);
        } catch (JSONException e) {
            fail("Unexpected JSONException caught");
        }

        assertEquals("test", level.getNamespace());
        assertEquals(-1, level.getID());
        assertEquals(17.5, level.getInitialFrameRate());
        assertEquals(40, level.getSpawn().getPosX());
        assertEquals(50, level.getSpawn().getPosY());

        assertEquals(7, level.getAllElements().size());
        assertEquals(8, level.getAllElements().get(0).getHeight());
        assertEquals(12, level.getAllElements().get(1).getPosition().getPosX());
        assertEquals(3, level.getAllElements().get(3).getWidth());
        assertEquals(9, level.getAllElements().get(4).getPosition().getPosX());
        assertEquals(33, level.getAllElements().get(6).getPosition().getPosY());
    }

    @Test
    public void jsonToColorTest() {
        try {
            json = JsonReader.readFile("./data/test/testColor1.json");
        } catch (IOException e) {
            fail("Unexpected IOException caught");
        }
        try {
            color = JsonParser.jsonToColor(json);
        } catch (JSONException e) {
            fail("Unexpected JSONException caught");
        }

        assertEquals(0, color.getRed());
        assertEquals(150, color.getGreen());
        assertEquals(99, color.getBlue());

        try {
            json = JsonReader.readFile("./data/test/testColor2.json");
        } catch (IOException e) {
            fail("Unexpected IOException caught");
        }
        try {
            color = JsonParser.jsonToColor(json);
        } catch (JSONException e) {
            fail("Unexpected JSONException caught");
        }

        assertEquals(126, color.getRed());
        assertEquals(255, color.getGreen());
        assertEquals(175, color.getBlue());
    }
}
