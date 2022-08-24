package util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class ColorManagerTest {
    private ColorManager defaultColManager;

    @BeforeEach
    public void setup() {
        defaultColManager = new ColorManager();
    }

    @Test
    public void constructorTest() {
        assertEquals(ColorManager.DEF_PLAYER_COL, defaultColManager.getColor(ColorManager.KEYS[0]));
        assertEquals(ColorManager.DEF_OBST_COL, defaultColManager.getColor(ColorManager.KEYS[1]));
        assertEquals(ColorManager.DEF_WALL_COL, defaultColManager.getColor(ColorManager.KEYS[2]));
        assertEquals(ColorManager.DEF_GOAL_COL, defaultColManager.getColor(ColorManager.KEYS[3]));
        assertEquals(ColorManager.DEF_BG_COL, defaultColManager.getColor(ColorManager.KEYS[4]));
        assertEquals(ColorManager.DEF_TEXT_COL, defaultColManager.getColor(ColorManager.KEYS[5]));
        assertEquals(ColorManager.DEF_DIALOG_BASE_COL, defaultColManager.getColor(ColorManager.KEYS[6]));
        assertEquals(ColorManager.DEF_DIALOG_ACC_COL, defaultColManager.getColor(ColorManager.KEYS[7]));

        Color cPlayerCol = new Color(1, 2, 3);
        Color cObstCol = new Color(4, 5, 6);
        Color cWallCol = new Color(33, 255, 11);
        Color cGoalCol = new Color(55, 66, 44);
        Color cBGCol = new Color(128, 0, 255);
        Color cTextCol = new Color(0, 0, 0);
        Color cDialogBaseCol = new Color(255, 255, 255);
        Color cDialogAccCol = new Color(244, 122, 61);
        ColorManager customColManager = new ColorManager(cPlayerCol, cObstCol, cWallCol, cGoalCol,
                cBGCol, cTextCol, cDialogBaseCol, cDialogAccCol);

        assertEquals(cPlayerCol, customColManager.getColor(ColorManager.KEYS[0]));
        assertEquals(cObstCol, customColManager.getColor(ColorManager.KEYS[1]));
        assertEquals(cWallCol, customColManager.getColor(ColorManager.KEYS[2]));
        assertEquals(cGoalCol, customColManager.getColor(ColorManager.KEYS[3]));
        assertEquals(cBGCol, customColManager.getColor(ColorManager.KEYS[4]));
        assertEquals(cTextCol, customColManager.getColor(ColorManager.KEYS[5]));
        assertEquals(cDialogBaseCol, customColManager.getColor(ColorManager.KEYS[6]));
        assertEquals(cDialogAccCol, customColManager.getColor(ColorManager.KEYS[7]));
    }

    @Test
    public void setColorTest() {
        defaultColManager.setColor(ColorManager.KEYS[0], new Color(126, 255, 175));
        assertEquals(126, defaultColManager.getColor(ColorManager.KEYS[0]).getRed());
        assertEquals(255, defaultColManager.getColor(ColorManager.KEYS[0]).getGreen());
        assertEquals(175, defaultColManager.getColor(ColorManager.KEYS[0]).getBlue());

        defaultColManager.setColor(ColorManager.KEYS[5], new Color(111, 14, 0));
        assertEquals(111, defaultColManager.getColor(ColorManager.KEYS[5]).getRed());
        assertEquals(14, defaultColManager.getColor(ColorManager.KEYS[5]).getGreen());
        assertEquals(0, defaultColManager.getColor(ColorManager.KEYS[5]).getBlue());
    }
}
