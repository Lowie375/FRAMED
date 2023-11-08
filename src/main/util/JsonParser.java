package util;

import elem.Goal;
import elem.Obstacle;
import elem.Wall;
import model.Level;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.util.ArrayList;

/**
 * (static)
 * Represents a static utility class for parsing JSON objects into other formats
 */
public class JsonParser {

    /**
     * Parses a JSON object into a FRAMED level
     *
     * @param json JSON object to parse
     * @return the parsed level
     */
    public static Level jsonToLevel(JSONObject json) throws JSONException {
        Level parsedLevel = new Level(
                json.getJSONObject("metadata").getString("namespace"),
                json.getJSONObject("metadata").getInt("id"),
                new Position(json.getJSONObject("spawn").getDouble("posX"),
                        json.getJSONObject("spawn").getDouble("posY")),
                json.getFloat("initialFrameRate"),
                new ArrayList<>());

        parseObstacles(json.getJSONArray("obstacles"), parsedLevel);
        parseWalls(json.getJSONArray("walls"), parsedLevel);
        parseGoals(json.getJSONArray("goals"), parsedLevel);

        return parsedLevel;
    }

    /**
     * Parses the given list of JSON obstacles and adds it to the given level
     *
     * @param obstacles JSON array of obstacles
     * @param level     level to add parsed obstacles to
     */
    private static void parseObstacles(JSONArray obstacles, Level level) {
        for (int i = 0; i < obstacles.length(); i++) {
            JSONObject obst = obstacles.getJSONObject(i);
            Obstacle parsedObst = new Obstacle(
                    new Position(obst.getJSONObject("pos").getDouble("posX"),
                            obst.getJSONObject("pos").getDouble("posY")),
                    obst.getInt("width"), obst.getInt("height"));
            level.addElement(parsedObst);
        }
    }

    /**
     * Parses the given list of JSON walls and adds it to the given level
     *
     * @param walls JSON array of walls
     * @param level level to add parsed walls to
     */
    private static void parseWalls(JSONArray walls, Level level) {
        for (int i = 0; i < walls.length(); i++) {
            JSONObject wall = walls.getJSONObject(i);
            Wall parsedWall = new Wall(
                    new Position(wall.getJSONObject("pos").getDouble("posX"),
                            wall.getJSONObject("pos").getDouble("posY")),
                    wall.getInt("width"), wall.getInt("height"));
            level.addElement(parsedWall);
        }
    }

    /**
     * Parses the given list of JSON goals and adds it to the given level
     *
     * @param level JSON array of goals
     * @param goals level to add parsed goals to
     */
    private static void parseGoals(JSONArray goals, Level level) {
        for (int i = 0; i < goals.length(); i++) {
            JSONObject goal = goals.getJSONObject(i);
            Goal parsedGoal = new Goal(
                    new Position(goal.getJSONObject("pos").getDouble("posX"),
                            goal.getJSONObject("pos").getDouble("posY")),
                    goal.getInt("width"), goal.getInt("height"), goal.getInt("nextLevelID"));
            level.addElement(parsedGoal);
        }
    }

    /**
     * Parses a JSON object into a colour
     *
     * @param json JSON object to parse
     * @return the parsed colour
     */
    public static Color jsonToColor(JSONObject json) throws JSONException {
        return new Color(json.getInt("r"), json.getInt("g"), json.getInt("b"));
    }
}
