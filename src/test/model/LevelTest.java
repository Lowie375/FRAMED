package model;

import elem.Goal;
import elem.Obstacle;
import elem.Wall;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Position;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import static model.Level.*;
import static org.junit.jupiter.api.Assertions.*;

public class LevelTest {
    Player player;
    Obstacle obs1;
    Obstacle obs2;
    Obstacle obs3;
    Wall wall1;
    Wall wall2;
    Goal goal1;
    Goal goal2;

    Level level;

    @BeforeEach
    public void setup() {
        player = new Player(new Position(3, 4), new Color(0, 150, 99));
        obs1 = new Obstacle(new Position(100, 10), 5, 20); // x E [100, 105], y E [10, 30]
        obs2 = new Obstacle(new Position(150, 10), 25, 10); // x E [150, 175], y E [10, 20]
        obs3 = new Obstacle(new Position(450, 45), 5, 5); // x E [450, 455], y E [45, 50]
        wall1 = new Wall(new Position(5, 550), 50, 3); // x E [5, 55], y E [550, 553]
        wall2 = new Wall(new Position(38, 400), 3, 10); // x E [38, 41], y E [400, 410]
        goal1 = new Goal(new Position(16, 21), 3, 3, 1); // x E [16, 19], y E [21, 24]
        goal2 = new Goal(new Position(33, 5), 3, 3, 3); // x E [33, 36], y E [5, 8]

        level = new Level("test", 0, new Position(1, 2), 30,
                new ArrayList<>(Arrays.asList(obs1, goal1, wall2, obs2, wall1, obs3, goal2)));
    }

    @Test
    public void constructorTest() {
        assertEquals("test", level.getNamespace());
        assertEquals(0, level.getID());
        assertEquals(1, level.getSpawn().getPosX());
        assertEquals(2, level.getSpawn().getPosY());
        assertEquals(30, level.getInitialFrameRate());

        assertEquals(7, level.getAllElements().size());
        assertEquals(20, level.getAllElements().get(0).getHeight());
        assertEquals(21, level.getAllElements().get(1).getPosition().getPosY());
        assertEquals(50, level.getAllElements().get(4).getWidth());
        assertEquals(450, level.getAllElements().get(5).getPosition().getPosX());

        assertEquals(3, level.getObstacles().size());
        assertEquals(2, level.getWalls().size());
        assertEquals(2, level.getGoals().size());
        assertEquals(25, level.getObstacles().get(1).getWidth());
        assertEquals(550, level.getWalls().get(1).getPosition().getPosY());
        assertEquals(3, level.getGoals().get(1).getNextLevelID());
    }

    @Test
    public void addElementTest() {
        level.addElement(new Wall(new Position(88, 69), 1, 2));
        assertEquals(8, level.getAllElements().size());
        assertEquals(88, level.getAllElements().get(7).getPosition().getPosX());
        assertEquals(2, level.getAllElements().get(7).getHeight());

        level.addElement(new Obstacle(new Position(11, 44), 3, 4));
        level.addElement(new Wall(new Position(77, 55), 5, 6));
        assertEquals(10, level.getAllElements().size());
        assertEquals(44, level.getAllElements().get(8).getPosition().getPosY());
        assertEquals(5, level.getAllElements().get(9).getWidth());

        level.addElement(new Goal(new Position(99, 111), 2, 4, 2));
        assertEquals(11, level.getAllElements().size());
        assertEquals(99, level.getAllElements().get(10).getPosition().getPosX());
        assertEquals(4, level.getAllElements().get(10).getHeight());
    }

    @Test
    public void removeElementTest() {
        level.removeElement(3);
        assertEquals(6, level.getAllElements().size());
        assertEquals(20, level.getAllElements().get(0).getHeight());
        assertEquals(21, level.getAllElements().get(1).getPosition().getPosY());
        assertEquals(50, level.getAllElements().get(3).getWidth());
        assertEquals(5, level.getAllElements().get(4).getHeight());
        assertEquals(2, level.getObstacles().size());
        assertEquals(2, level.getWalls().size());
        assertEquals(2, level.getGoals().size());

        level.removeElement(0);
        assertEquals(5, level.getAllElements().size());
        assertEquals(21, level.getAllElements().get(0).getPosition().getPosY());
        assertEquals(50, level.getAllElements().get(2).getWidth());
        assertEquals(5, level.getAllElements().get(3).getHeight());
        assertEquals(1, level.getObstacles().size());
        assertEquals(2, level.getWalls().size());
        assertEquals(2, level.getGoals().size());
    }

    @Test
    public void removeDifferentElementTypesTest() {
        level.removeElement(2);
        assertEquals(6, level.getAllElements().size());
        assertEquals(3, level.getObstacles().size());
        assertEquals(1, level.getWalls().size());
        assertEquals(2, level.getGoals().size());

        level.removeElement(5);
        assertEquals(5, level.getAllElements().size());
        assertEquals(3, level.getObstacles().size());
        assertEquals(1, level.getWalls().size());
        assertEquals(1, level.getGoals().size());

        level.removeElement(0);
        assertEquals(4, level.getAllElements().size());
        assertEquals(2, level.getObstacles().size());
        assertEquals(1, level.getWalls().size());
        assertEquals(1, level.getGoals().size());
    }

    @Test
    public void startLevelTest() {
        assertEquals(3, player.getPosition().getPosX());
        assertEquals(4, player.getPosition().getPosY());

        level.startLevel(player);
        assertEquals(1, player.getPosition().getPosX());
        assertEquals(2, player.getPosition().getPosY());

        player.setPosition(5.5, 95.25);
        player.setDX(-7.75);
        player.setDY(5);
        level.startLevel(player);
        assertEquals(1, player.getPosition().getPosX());
        assertEquals(2, player.getPosition().getPosY());
        assertEquals(0, player.getDX());
        assertEquals(0, player.getDY());
    }

    @Test
    public void handlePlayerWallCollisionTest() {
        // majority of specific cases covered in WallTest

        // x E [5, 55], y E [550, 553]
        // x E [38, 41], y E [400, 410]

        player.setPosition(11 - Player.SIZE, 549 - Player.SIZE);
        player.setDX(3.5);
        player.setDY(3.25);
        assertEquals(NO_STATE_CHANGE, level.handlePlayerCollisions(player).getStatus());
        assertEquals(11 - Player.SIZE, player.getPosition().getPosX());
        assertEquals(549 - Player.SIZE, player.getPosition().getPosY());
        assertEquals(3.5, player.getDX());
        assertEquals(3.25, player.getDY());

        player.setPosition(40 - Player.SIZE, 401 - Player.SIZE);
        player.setDX(3.5);
        player.setDY(3.25);
        assertEquals(NO_STATE_CHANGE, level.handlePlayerCollisions(player).getStatus());
        assertEquals(40 - Player.SIZE, player.getPosition().getPosX());
        assertEquals(399.75 - Player.SIZE, player.getPosition().getPosY());
        assertEquals(3.5, player.getDX());
        assertEquals(0, player.getDY());

        player.setPosition(6 - Player.SIZE, 552 - Player.SIZE);
        player.setDX(3.5);
        player.setDY(3.25);
        assertEquals(NO_STATE_CHANGE, level.handlePlayerCollisions(player).getStatus());
        assertEquals(4.75 - Player.SIZE, player.getPosition().getPosX());
        assertEquals(552 - Player.SIZE, player.getPosition().getPosY());
        assertEquals(0, player.getDX());
        assertEquals(3.25, player.getDY());
    }

    @Test
    public void handlePlayerObstacleCollisionTest() {
        player.setPosition(170, 17);
        player.setDX(2);
        assertEquals(RESET, level.handlePlayerCollisions(player).getStatus());
        assertEquals(1, player.getPosition().getPosX());
        assertEquals(2, player.getPosition().getPosY());
        assertEquals(0, player.getDX());

        player.setPosition(300.25, 300.75);
        player.setDY(-2);
        assertEquals(NO_STATE_CHANGE, level.handlePlayerCollisions(player).getStatus());
        assertEquals(300.25, player.getPosition().getPosX());
        assertEquals(300.75, player.getPosition().getPosY());
        assertEquals(-2, player.getDY());

        player.setPosition(105 + Player.SIZE, 10 - Player.SIZE);
        player.setDY(-2);
        assertEquals(RESET, level.handlePlayerCollisions(player).getStatus());
        assertEquals(1, player.getPosition().getPosX());
        assertEquals(2, player.getPosition().getPosY());
        assertEquals(0, player.getDY());

        player.setPosition(450 - Player.SIZE, 50 + Player.SIZE);
        player.setDX(1.5);
        player.setDY(-2.25);
        assertEquals(RESET, level.handlePlayerCollisions(player).getStatus());
        assertEquals(1, player.getPosition().getPosX());
        assertEquals(2, player.getPosition().getPosY());
        assertEquals(0, player.getDX());
        assertEquals(0, player.getDY());
    }

    @Test
    public void handlePlayerGoalCollisionTest() {
        player.setPosition(17 - Player.SIZE, 25 + Player.SIZE);
        assertEquals(NO_STATE_CHANGE, level.handlePlayerCollisions(player).getStatus());
        assertNull(level.handlePlayerCollisions(player).getExtraData());

        player.setPosition(17 - Player.SIZE, 22 + Player.SIZE);
        assertEquals(GOAL_REACHED, level.handlePlayerCollisions(player).getStatus());
        assertEquals(1, level.handlePlayerCollisions(player).getExtraData());

        player.setPosition(16 - Player.SIZE, 24 + Player.SIZE);
        assertEquals(GOAL_REACHED, level.handlePlayerCollisions(player).getStatus());
        assertEquals(1, level.handlePlayerCollisions(player).getExtraData());

        player.setPosition(19 - Player.SIZE, 21 - Player.SIZE);
        assertEquals(GOAL_REACHED, level.handlePlayerCollisions(player).getStatus());
        assertEquals(1, level.handlePlayerCollisions(player).getExtraData());

        player.setPosition(32 - Player.SIZE, 8);
        assertEquals(NO_STATE_CHANGE, level.handlePlayerCollisions(player).getStatus());
        assertNull(level.handlePlayerCollisions(player).getExtraData());

        player.setPosition(34 - Player.SIZE, 8);
        assertEquals(GOAL_REACHED, level.handlePlayerCollisions(player).getStatus());
        assertEquals(3, level.handlePlayerCollisions(player).getExtraData());
    }

    @Test
    public void filterOutNullElementTest() {
        level.addElement(null);
        assertEquals(8, level.getAllElements().size());
        assertEquals(3, level.getObstacles().size());
        assertEquals(2, level.getWalls().size());
        assertEquals(2, level.getGoals().size());
    }
}
