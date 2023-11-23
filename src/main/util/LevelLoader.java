package util;

import exception.LevelNotFoundException;
import model.Level;
import org.json.JSONObject;
import persistence.JsonReader;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a collection of levels that are in the FRAMED game's data directory
 * Level in the class are first indexed by namespace, then by id
 */
public class LevelLoader {
    private final Map<String, Map<Integer, String>> levelMap;

    /**
     * Constructs a new LevelLoader at the given directory
     *
     * @param directory location to load levels from
     * @throws IOException if an I/O exception occurs
     */
    public LevelLoader(File directory) throws IOException {
        this.levelMap = new HashMap<>();
        findLevelsInFile(directory);
    }

    /**
     * Recursively finds all the level files in the given directory, and saves then to the level map
     *
     * @param file current file being searched
     * @throws IOException if an I/O exception occurs
     */
    private void findLevelsInFile(File file) throws IOException {
        if (file != null) {
            if (file.isDirectory()) {
                File[] fileList = file.listFiles();
                if (fileList != null) {
                    for (File f : fileList) {
                        findLevelsInFile(f);
                    }
                }
            } else if (file.exists() && file.canRead()) {
                String path = file.getPath();
                JSONObject levelObj = JsonReader.readFile(path);
                updateMap(levelObj.getJSONObject("metadata").getString("namespace"),
                        levelObj.getJSONObject("metadata").getInt("id"), path);
            }
        }
    }

    /**
     * Adds a level to the level map
     *
     * @param namespace namespace of level
     * @param id        numerical ID of level
     * @param location  location of level file on disk
     */
    private void updateMap(String namespace, int id, String location) {
        Map<Integer, String> namespaceMap = levelMap.get(namespace);
        if (namespaceMap == null) {
            namespaceMap = new HashMap<>();
        }
        namespaceMap.put(id, location);
        levelMap.put(namespace, namespaceMap);
    }

    /**
     * Loads the level with the given namespace and level ID from the FRAMED save files
     *
     * @param namespace the namespace of the level to be loaded
     * @param id        the numerical ID of the level to be loaded
     * @return the loaded level
     * @throws IOException if an I/O error occurs
     * @throws LevelNotFoundException if the requested level was not found
     * @see #loadLevel(String)
     */
    public Level loadLevel(String namespace, int id) throws IOException, LevelNotFoundException {
        Map<Integer, String> namespaceMap = levelMap.get(namespace);
        if (namespaceMap == null) {
            throw new LevelNotFoundException("Namespace " + namespace + " not found");
        }
        String target = namespaceMap.get(id);
        if (target == null) {
            throw new LevelNotFoundException("Level " + id + " not found in namespace " + namespace);
        }
        return loadLevel(target);
    }

    /**
     * Loads a level from the given target destination
     *
     * @param target the destination to load the level from
     * @return the loaded level
     * @throws IOException if an I/O error occurs
     */
    private Level loadLevel(String target) throws IOException {
        JSONObject levelObj = JsonReader.readFile(target);
        return JsonParser.jsonToLevel(levelObj);
    }
}
