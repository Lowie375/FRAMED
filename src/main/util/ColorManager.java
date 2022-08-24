package util;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a set of colours for various FRAMED GUI elements
 */
public class ColorManager {
    public static final Color DEF_PLAYER_COL = new Color(0, 150, 99);
    public static final Color DEF_OBST_COL = new Color(234, 17, 75);
    public static final Color DEF_WALL_COL = new Color(89, 30, 231);
    public static final Color DEF_GOAL_COL = new Color(246, 168, 10);
    public static final Color DEF_BG_COL = new Color(19, 17, 20);
    public static final Color DEF_TEXT_COL = new Color(255, 255, 255);
    public static final Color DEF_DIALOG_BASE_COL = new Color(44, 40, 44);

    public static final Color DEF_DIALOG_ACC_COL = new Color(91, 84, 93);

    public static final Color[] ELEMENT_COLS =
            new Color[]{DEF_PLAYER_COL, DEF_OBST_COL, DEF_WALL_COL, DEF_GOAL_COL,
                    DEF_BG_COL, DEF_TEXT_COL, DEF_DIALOG_BASE_COL, DEF_DIALOG_ACC_COL};
    public static final String[] ELEMENTS_WITH_COL =
            new String[]{"Player", "Obstacle", "Wall", "Goal",
                    "Background", "Text", "Dialog Base", "Dialog Accent"};
    public static final String[] KEYS =
            new String[]{"player", "obstacle", "wall", "goal", "bg", "text", "dialogBase", "dialogAcc"};
    public static final int PLAYER = 0;
    public static final int OBST = 1;
    public static final int WALL = 2;
    public static final int GOAL = 3;
    public static final int BG = 4;
    public static final int TEXT = 5;
    public static final int DIALOG_BASE = 6;
    public static final int DIALOG_ACC = 7;

    private final Map<String, Color> colMap;

    /**
     * Creates a ColourManager with the default GUI colours
     */
    public ColorManager() {
        this(DEF_PLAYER_COL, DEF_OBST_COL, DEF_WALL_COL, DEF_GOAL_COL,
                DEF_BG_COL, DEF_TEXT_COL, DEF_DIALOG_BASE_COL, DEF_DIALOG_ACC_COL);
    }

    /**
     * Creates a ColourManager with the specified GUI colours
     *
     * @param playerCol player colour
     * @param obstCol obstacle colour
     * @param wallCol wall colour
     * @param goalCol goal colour
     * @param bgCol GUI background colour
     * @param textCol GUI text colour
     * @param dialogBaseCol GUI dialog box base colour, used for dialog backgrounds
     * @param dialogAccCol GUI dialog box accent colour, used for dialog buttons and the status row
     */
    public ColorManager(Color playerCol, Color obstCol, Color wallCol, Color goalCol,
                        Color bgCol, Color textCol, Color dialogBaseCol, Color dialogAccCol) {
        this.colMap = new HashMap<>();
        this.colMap.put(KEYS[0], playerCol);
        this.colMap.put(KEYS[1], obstCol);
        this.colMap.put(KEYS[2], wallCol);
        this.colMap.put(KEYS[3], goalCol);
        this.colMap.put(KEYS[4], bgCol);
        this.colMap.put(KEYS[5], textCol);
        this.colMap.put(KEYS[6], dialogBaseCol);
        this.colMap.put(KEYS[7], dialogAccCol);
    }

    /**
     * Sets the colour with the given key to the new colour given (if the key is in the colour map)
     *
     * @param key key of the colour to change
     * @param newCol new colour value
     */
    public void setColor(String key, Color newCol) {
        this.colMap.replace(key, newCol);
    }

    /**
     * @return the colour of the element with the given key (or null if the key is not in the colour map)
     */
    public Color getColor(String key) {
        return this.colMap.get(key);
    }
}
