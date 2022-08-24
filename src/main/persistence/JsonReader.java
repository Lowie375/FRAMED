package persistence;

import org.json.JSONObject;

import java.io.FileReader;
import java.io.IOException;

/**
 * Represents a static utility class for reading JSON objects from files
 */
public class JsonReader {

    /**
     * Reads a JSON file from the target destination
     *
     * @param target the destination of the file to be read
     * @return JSON object read from the file
     * @throws IOException if an I/O error occurs
     */
    public static JSONObject readFile(String target) throws IOException {
        FileReader fileReader = new FileReader(target);
        int lastChar;
        StringBuilder output = new StringBuilder();
        while (true) {
            lastChar = fileReader.read();
            if (lastChar == -1) {
                break;
            } else {
                output.append((char) lastChar);
            }
        }
        fileReader.close();
        return new JSONObject(output.toString());
    }
}
