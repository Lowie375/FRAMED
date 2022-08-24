package persistence;

import org.json.JSONObject;
import util.ColorManager;

import java.io.FileWriter;
import java.io.IOException;

/** (static)
 * Represents a static utility class for writing JSON objects to files
 */
public class JsonWriter {

    /**
     * Writes a new FRAMED save state to the target save file
     *
     * @param target target destination to write the save to
     * @param currentLevelID current level's numerical ID
     * @param currentNamespace current level's namespace
     * @param colours FRAMED colour manager 
     * @throws IOException if an I/O error occurs
     * @see #writeToFile(String, String) 
     */
    public static void writeToSave(String target, int currentLevelID, String currentNamespace, ColorManager colours)
            throws IOException {
        JSONObject json = new JSONObject();
        json.put("formatVersion", 3); // constant for maintaining save format consistency
        json.put("currentLevelID", currentLevelID);
        json.put("currentNamespace", currentNamespace);
        JSONObject jsonColours = new JSONObject();
        for (String key : ColorManager.KEYS) {
            jsonColours.put(key, new JSONObject()
                    .put("r", colours.getColor(key).getRed())
                    .put("g", colours.getColor(key).getGreen())
                    .put("b", colours.getColor(key).getBlue()));
        }
        json.put("col", jsonColours);

        writeToFile(target, json.toString(2));
    }

    /**
     * Writes a text string to the target file
     *
     * @param target target destination to write content to
     * @param content content to be written to target destination
     * @throws IOException if an I/O error occurs
     */
    private static void writeToFile(String target, String content) throws IOException {
        FileWriter fileWriter = new FileWriter(target);
        fileWriter.write(content);
        fileWriter.close();
    }
}
