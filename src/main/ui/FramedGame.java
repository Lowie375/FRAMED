package ui;

import elem.Goal;
import elem.LevelElement;
import elem.Obstacle;
import elem.Wall;
import model.Level;
import model.Player;
import org.json.JSONObject;
import persistence.JsonReader;
import persistence.JsonWriter;
import util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

import static java.awt.event.KeyEvent.*;

/**
 * Represents an instance of FRAMED
 *
 * @see <a href="https://github.com/mkotb/SnakeConsole">https://github.com/mkotb/SnakeConsole (partially adapted from ui.TerminalGame)</a>
 */
public class FramedGame extends JFrame implements ActionListener {
    public static final double NANO = Math.pow(10, 9);
    public static final double NANO_TO_MILLI = Math.pow(10, -6);
    public static final double CLOCK_FRAME_RATE = 30;
    public static final int STATUS_ROW_HEIGHT = 60;
    public static final int SCREEN_SPACER = 9;
    public static final int SCREEN_WIDTH = 950 + 2 * SCREEN_SPACER; // equates to 950 usable pixels
    public static final int SCREEN_HEIGHT = 450 + STATUS_ROW_HEIGHT + SCREEN_SPACER; // equates to 450 usable pixels

    public static final int NO_MOVE_CHANGE = 0;
    public static final int X_MOVE_CHANGE = 1;
    public static final int Y_MOVE_CHANGE = 2;

    public static final List<Float> FRAME_RATES = new ArrayList<>(Arrays.asList(0.5F, 0.55F, 0.6F, 0.65F, 0.7F,
            0.8F, 0.9F, 1F, 1.1F, 1.2F, 1.35F, 1.5F, 1.65F, 1.8F, 2F, 2.25F, 2.5F, 2.75F, 3F, 3.5F, 4F, 4.5F, 5F, 6F,
            7F, 8F, 9F, 10F, 11F, 12F, 13.5F, 15.5F, 17.5F, 20F, 25F, 30F));
    public static final int FIRST_LEVEL = 0;

    private Level level;
    private Player player;
    private FramedRenderEngine framedRenderer;
    private ColorManager colManager;
    private FrameDelayTracker fdt;

    private String currentLevelDisplay;
    private float graphicalFrameRate;
    private boolean[] graphicalUpdates;
    private boolean newSave;
    private GameState status;
    private long waitTime;
    private long startTime;

    /**
     * Creates a new terminal instance of FRAMED from the FRAMED save file
     *
     * @throws IOException if an I/O error occurs
     */
    public FramedGame() throws IOException {
        super("FRAMED");
        setSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setLayout(null);
        //setMinimumSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));

        // temporary, will likely allow resizing later
        setResizable(false);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int) (screenSize.getWidth() - getWidth()) / 2, (int) (screenSize.getHeight() - getHeight()) / 2);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addKeyListener(new KeyTracker());

        initializeGame();
        setVisible(true);
    }

    /**
     * Renders the entire FRAMED game
     *
     * @param g FRAMED graphics object
     */
    @Override
    public void paint(Graphics g) {
        framedRenderer.render(g, this.graphicalUpdates);
    }

    /**
     * Initializes the FRAMED instance: <br>
     * - gets save data from the FRAMED save file and loads the appropriate level <br>
     * - creates necessary game components
     *
     * @throws IOException if an I/O error occurs
     */
    private void initializeGame() throws IOException {
        // get current level information from save file
        JSONObject save = JsonReader.readFile("./data/save.json");
        JSONObject colours = save.getJSONObject("col");
        level = loadLevel(save.getString("currentNamespace"), save.getInt("currentLevelID"));
        player = new Player(new Position(0, 0), JsonParser.jsonToColor(
                colours.getJSONObject(ColorManager.KEYS[ColorManager.PLAYER])));
        graphicalFrameRate = level.getInitialFrameRate();
        fdt = new FrameDelayTracker((long) (NANO / CLOCK_FRAME_RATE), (long) (NANO / graphicalFrameRate));

        colManager = new ColorManager(
                JsonParser.jsonToColor(colours.getJSONObject(ColorManager.KEYS[ColorManager.PLAYER])),
                JsonParser.jsonToColor(colours.getJSONObject(ColorManager.KEYS[ColorManager.OBST])),
                JsonParser.jsonToColor(colours.getJSONObject(ColorManager.KEYS[ColorManager.WALL])),
                JsonParser.jsonToColor(colours.getJSONObject(ColorManager.KEYS[ColorManager.GOAL])),
                JsonParser.jsonToColor(colours.getJSONObject(ColorManager.KEYS[ColorManager.BG])),
                JsonParser.jsonToColor(colours.getJSONObject(ColorManager.KEYS[ColorManager.TEXT])),
                JsonParser.jsonToColor(colours.getJSONObject(ColorManager.KEYS[ColorManager.DIALOG_BASE])),
                JsonParser.jsonToColor(colours.getJSONObject(ColorManager.KEYS[ColorManager.DIALOG_ACC])));
        framedRenderer = new FramedRenderEngine(this);
        graphicalUpdates = new boolean[]{true, true, false};
        waitTime = 0;

        newSave = this.level.getID() == 0 && this.level.getNamespace().equals("main");

        status = GameState.INVULNERABLE;
        framedRenderer.showLoadPopup();
        status = GameState.ACTIVE;
    }

    /**
     * Starts an instance of FRAMED
     *
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the thread is interrupted while sleeping
     */
    public void startGame() throws IOException, InterruptedException {
        startTime = System.nanoTime();
        updateLevelColours(this.level);
        framedRenderer.updateUIColours();
        level.startLevel(this.player);

        while (this.status != GameState.EXIT) {
            tickLoop();
        }
        System.exit(0);
    }

    /**
     * Loads the level with the given namespace and level ID from the FRAMED save files
     *
     * @param namespace the namespace of the level to be loaded
     * @param id        the numerical ID of the level to be loaded
     * @return the loaded level
     * @throws IOException if an I/O error occurs
     * @see #loadLevel(String)
     */
    private Level loadLevel(String namespace, int id) throws IOException {
        String target = "./data/level/" + namespace + "Level" + id + ".json";
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
        this.currentLevelDisplay = levelObj.getJSONObject("metadata").getString("namespace") + "["
                + levelObj.getJSONObject("metadata").getInt("id") + "]";
        return JsonParser.jsonToLevel(levelObj);
    }

    /**
     * Determines what type of tick should be processed based on the game's current frame rate and state,
     * then processes it
     *
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the thread is interrupted while sleeping
     */
    private void tickLoop() throws IOException, InterruptedException {
        if (this.status == GameState.PAUSED || this.status == GameState.SETTINGS) {
            // wait for unpause action
            pausedTick();
        } else if (this.status == GameState.TRANSITION) {
            // TODO: complete level transition
            //renderTransition();
            restartLevel();
            Thread.sleep((long) (NANO * NANO_TO_MILLI / CLOCK_FRAME_RATE));
            resetStartTime();
            this.status = GameState.ACTIVE;
        } else {
            // handle a generic tick
            genericTick();
        }
    }

    /**
     * Handles a tick while in a paused state: check for unpause actions and re-render if in the settings menu
     *
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the thread is interrupted while sleeping
     */
    private void pausedTick() throws IOException, InterruptedException {
        resetStartTime();
        repaint();
        handleKeys();
        Thread.sleep((long) (NANO * NANO_TO_MILLI / CLOCK_FRAME_RATE));
    }

    /**
     * Handles a generic tick (clock/graphical/both) depending on the game's current frame rate
     *
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the thread is interrupted while sleeping
     */
    private void genericTick() throws IOException, InterruptedException {
        if (startTime == -1) {
            startTime = System.nanoTime();
        }

        // check for clock tick
        if (this.fdt.getClockFrameDelay() == 0) {
            handleClockTick();
            this.fdt.setClockFrameDelay((long) (NANO / CLOCK_FRAME_RATE));
        }
        // check for graphical tick
        if (this.fdt.getGraphicalFrameDelay() == 0) {
            handleGraphicalTick();
            this.fdt.setGraphicalFrameDelay((long) (NANO / graphicalFrameRate));
        }

        // repeat the cycle + re-tick at next appropriate time
        long timeUntilNextTick = this.fdt.getMinFrameDelay();
        waitTime += timeUntilNextTick;
        long elapsedTime = System.nanoTime() - startTime;

        if (waitTime - elapsedTime >= 0) {
            Thread.sleep((long) ((waitTime - elapsedTime) * NANO_TO_MILLI));
            waitTime = 0;
            startTime = System.nanoTime();
        }

        graphicalUpdates[0] = false;
        graphicalUpdates[1] = false;
        this.fdt.reduceFrameDelays(timeUntilNextTick);
    }

    /**
     * Processes a clock tick: <br>
     * - handles key events <br>
     * - moves the player and level elements
     *
     * @throws IOException if an I/O error occurs
     */
    private void handleClockTick() throws IOException {
        handleKeys();
        player.move();
        constrainPlayerToBounds();
        repaint();
    }

    /**
     * Processes a graphical tick: <br>
     * - processes a game state update (if player is not invulnerable) <br>
     * - renders the playing field
     *
     * @throws IOException if an I/O error occurs
     */
    private void handleGraphicalTick() throws IOException {
        if (this.status != GameState.INVULNERABLE) {
            CollisionEvent collisionEvent = this.level.handlePlayerCollisions(this.player);
            if (collisionEvent.getStatus() != 0) {
                handlePlayerCollisionEvent(collisionEvent);
            }
        }
        if (this.status != GameState.TRANSITION) {
            graphicalUpdates[0] = true;
            repaint();
        }
    }

    /**
     * Saves the game's current progress and loads the next level of FRAMED
     *
     * @param nextLevelObj the numerical ID of the next level to load
     * @throws IOException if an I/O error occurs
     */
    private void loadNextLevel(Object nextLevelObj) throws IOException {
        int nextLevelID;
        try {
            nextLevelID = (int) nextLevelObj;
        } catch (NullPointerException | ClassCastException e) {
            throw new IOException();
        }

        JSONObject currentSave = JsonReader.readFile("./data/save.json");
        JsonWriter.writeToSave("./data/save.json", nextLevelID, currentSave.getString("currentNamespace"),
                this.colManager);
        this.level = loadLevel(currentSave.getString("currentNamespace"), nextLevelID);
        updateLevelColours(this.level);
    }

    /**
     * Handles user key inputs, triggering the appropriate game actions for each key detected
     *
     * @throws IOException if an I/O error occurs
     */
    private void handleKeys() throws IOException {
        KeyTracker keyTracker = (KeyTracker) this.getKeyListeners()[0];
        int moveChange = 0;

        // handles events for typed keys (keys pressed once)
        for (KeyEvent ke : keyTracker.getTypedKeys()) {
            handleTypedKey(ke, ke.getKeyChar(), keyTracker.getPressedKeys());
        }
        keyTracker.clearTypedKeys();

        // handles events for pressed keys (keys with persistent effects)
        for (Integer key : keyTracker.getPressedKeys()) {
            moveChange += handlePressedKey(key, moveChange);
        }

        // adjusts player dx and/or dy if no movement keys pressed
        if ((moveChange & X_MOVE_CHANGE) == 0 && player.getDX() != 0) {
            double slowDX = Math.max(Math.abs(player.getDX()) - 0.9375, 0);
            player.setDX(player.getDX() >= 0 ? slowDX : slowDX * -1);
        }
        if ((moveChange & Y_MOVE_CHANGE) == 0 && player.getDY() != 0) {
            double slowDY = Math.max(Math.abs(player.getDY()) - 0.9375, 0);
            player.setDY(player.getDY() >= 0 ? slowDY : slowDY * -1);
        }
    }

    /**
     * Handles one "typed" user key input, triggering the appropriate game action
     *
     * @param ke      key event emitted by the key being typed
     * @param key     character typed
     * @param allKeys set of all keys currently being pressed
     */
    private void handleTypedKey(KeyEvent ke, Character key, Set<Integer> allKeys) throws IOException {
        // status-independent combinations
        if (ke.isControlDown()) {
            if (allKeys.contains(VK_X) && !allKeys.contains(VK_SHIFT)) {
                closeGame();
            } else if (allKeys.contains(VK_R) && allKeys.contains(VK_SHIFT)) {
                resetGameProgress();
            }
        }
        // non-TRANSITION combinations
        if (this.status != GameState.TRANSITION) {
            if ((allKeys.contains(VK_ESCAPE) || key == 'p' || key == '7') && this.status != GameState.SETTINGS) {
                togglePause();
            }
        }
        // ACTIVE combinations
        if (this.status == GameState.ACTIVE) {
            if (key == 'r' || key == '9') {
                restartLevel();
            } else if (key == 'e' || key == '1') {
                increaseFrameRate();
            } else if (key == 'q' || key == '0') {
                decreaseFrameRate();
            }
        }
    }

    /**
     * Handles one "pressed" user key input, triggering the appropriate game actions
     *
     * @param keyCode    key code of the pressed key
     * @param moveChange flag representing all movement changes handled this tick
     * @return constant representing the movement change handled
     */
    private int handlePressedKey(int keyCode, int moveChange) {
        if ((moveChange & Y_MOVE_CHANGE) == 0
                && (keyCode == VK_W || keyCode == VK_UP || keyCode == VK_KP_UP)) {
            player.changeDY(-0.375);
            //constrainedPlayerDYChange(-0.25, -1.25);
            return Y_MOVE_CHANGE;
        } else if ((moveChange & X_MOVE_CHANGE) == 0
                && (keyCode == VK_A || keyCode == VK_LEFT || keyCode == VK_KP_LEFT)) {
            //constrainedPlayerDXChange(-0.25, -1.25);
            player.changeDX(-0.375);
            return X_MOVE_CHANGE;
        } else if ((moveChange & Y_MOVE_CHANGE) == 0
                && (keyCode == VK_S || keyCode == VK_DOWN || keyCode == VK_KP_DOWN)) {
            //constrainedPlayerDYChange(0.25, 1.25);
            player.changeDY(0.375);
            return Y_MOVE_CHANGE;
        } else if ((moveChange & X_MOVE_CHANGE) == 0
                && (keyCode == VK_D || keyCode == VK_RIGHT || keyCode == VK_KP_RIGHT)) {
            //constrainedPlayerDXChange(0.25, 1.25);
            player.changeDX(0.375);
            return X_MOVE_CHANGE;
        }
        return NO_MOVE_CHANGE;
    }

    /**
     * Handles collision events that affect the level/game state
     *
     * @param collisionEvent collision event to be handled
     * @throws IOException if an I/O error occurs
     */
    private void handlePlayerCollisionEvent(CollisionEvent collisionEvent) throws IOException {
        if (collisionEvent.getStatus() == Level.RESET) {
            restartLevel();
        } else if (collisionEvent.getStatus() == Level.GOAL_REACHED) {
            this.status = GameState.TRANSITION;
            graphicalUpdates[0] = true;
            repaint();
            loadNextLevel(collisionEvent.getExtraData());
        }
    }

    /**
     * Handles menu action events
     *
     * @param e action event emitted
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand().split("\\.")[0]) {
            case "pause":
                handlePauseButtonEvents(e);
                break;
            case "settings":
                handleSettingsButtonEvents(e);
                break;
            default:
                break;
        }
    }

    /**
     * Handles pause menu button action events
     *
     * @param e action event emitted
     */
    private void handlePauseButtonEvents(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "pause.unpause":
                unpause();
                break;
            case "pause.restart":
                unpause();
                restartLevel();
                break;
            case "pause.exit":
                closeGame();
                break;
            case "pause.settings":
                openSettings();
                break;
            default:
                break;
        }
    }

    /**
     * Handles settings menu button action events
     *
     * @param e action event emitted
     */
    private void handleSettingsButtonEvents(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "settings.exit":
                closeSettings();
                break;
            case "settings.theme.reset":
                framedRenderer.revertSelectedColToCurrent();
                break;
            case "settings.theme.default":
                framedRenderer.revertSelectedColToDefault();
                break;
            case "settings.theme.apply":
                updateColours();
                break;
            case "settings.analyze.level":
                analyzeLevel();
                break; // stub
            case "settings.analyze.graph":
                showLevelAreaDistribution();
                break;
            default:
                break;
        }
    }

    /**
     * Creates a list of all the level elements in the level
     */
    private void analyzeLevel() {
        StringBuilder elementString = new StringBuilder("Level element breakdown of " + this.level.getNamespace()
                + "[" + this.level.getID() + "]:\n");
        addElementsToElementString(elementString, "Obstacles:", this.level.getObstacles());
        addElementsToElementString(elementString, "Walls:", this.level.getWalls());
        addElementsToElementString(elementString, "Goals:", this.level.getGoals());

        framedRenderer.setAnalysisText(String.valueOf(elementString));
        // TODO: implement graph
        //framedRenderer.createBreakdownGraph();
        //repaint();
    }

    /**
     * Adds the given header and all the elements in the given list to the given string builder
     *
     * @param elementString string builder
     * @param headerText    header for the list of elements
     * @param elements      list of level elements
     */
    private void addElementsToElementString(StringBuilder elementString, String headerText,
                                            List<? extends LevelElement> elements) {
        elementString.append("  - ").append(headerText).append("\n");
        for (LevelElement elem : elements) {
            elementString.append("      - ").append(elem.getWidth()).append("px * ").append(elem.getHeight())
                    .append("px @ (").append((int) elem.getPosition().getPosX()).append(", ")
                    .append((int) elem.getPosition().getPosY()).append(")")
                    .append(elem.getExtraDisplayData()).append("\n");
        }
    }

    /**
     * Creates a graph of the relative areas of each type of level element
     */
    private void showLevelAreaDistribution() {
        StringBuilder elementString = new StringBuilder("Level element size distribution of "
                + this.level.getNamespace() + "[" + this.level.getID() + "]:\n");
        int obstArea = getTotalElementArea(this.level.getObstacles());
        int wallArea = getTotalElementArea(this.level.getWalls());
        int goalArea = getTotalElementArea(this.level.getGoals());
        int totalUsedArea = obstArea + wallArea + goalArea;

        addAreaToElementString(elementString, "obstacle", obstArea, totalUsedArea);
        addAreaToElementString(elementString, "wall", wallArea, totalUsedArea);
        addAreaToElementString(elementString, "goal", goalArea, totalUsedArea);
        elementString.append("\nTotal area used: ").append(totalUsedArea).append("px");

        framedRenderer.setAnalysisText(String.valueOf(elementString));
    }

    /**
     * Returns the sum of the areas of all the level elements in the list
     *
     * @param elements list of level elements
     * @return the final sum
     */
    private int getTotalElementArea(List<? extends LevelElement> elements) {
        int totalArea = 0;
        for (LevelElement elem : elements) {
            totalArea += elem.getWidth() * elem.getHeight();
        }
        return totalArea;
    }

    /**
     * Adds the given element's area and usage percent to the given string builder
     *
     * @param elementString string builder
     * @param elementName   name of the element
     * @param elementArea   level area taken up by the given element
     * @param totalUsedArea level area taken up by all level elements
     */
    private void addAreaToElementString(StringBuilder elementString, String elementName,
                                        int elementArea, int totalUsedArea) {
        double percentAreaUsed = (double) (10000 * elementArea / totalUsedArea) / 100;
        elementString.append("  - Total ").append(elementName).append(" area: ").append(elementArea).append("px (~")
                .append(new DecimalFormat("#.##").format(percentAreaUsed)).append("% of used area)\n");
    }

    /**
     * Updates the colour manager with the new selected colours + updates the colours of all FRAMED components
     */
    private void updateColours() {
        // update colour manager
        ColorManager newColours = this.framedRenderer.getTempColManager();
        for (String key : ColorManager.KEYS) {
            this.colManager.setColor(key, newColours.getColor(key));
        }

        // update player
        this.player.setCol(this.colManager.getColor(ColorManager.KEYS[ColorManager.PLAYER]));
        // update level
        updateLevelColours(this.level);
        // update
        framedRenderer.updateUIColours();

        // update save
        try {
            JSONObject currentSave = JsonReader.readFile("./data/save.json");
            JsonWriter.writeToSave("./data/save.json", currentSave.getInt("currentLevelID"),
                    currentSave.getString("currentNamespace"), this.colManager);
        } catch (IOException e) {
            // uh oh, sorry!
            System.out.println("Save to file failed! Please try again later.");
        }
    }

    /**
     * Updates the level's colours in accordance with the colour manager's current colour scheme
     *
     * @param level level to update
     */
    private void updateLevelColours(Level level) {
        for (Obstacle obst : level.getObstacles()) {
            obst.setElementCol(this.colManager.getColor(ColorManager.KEYS[ColorManager.OBST]));
        }
        for (Wall wall : level.getWalls()) {
            wall.setElementCol(this.colManager.getColor(ColorManager.KEYS[ColorManager.WALL]));
        }
        for (Goal goal : level.getGoals()) {
            goal.setElementCol(this.colManager.getColor(ColorManager.KEYS[ColorManager.GOAL]));
        }
    }

    /**
     * Resets the player's game progress and reloads the first level
     *
     * @throws IOException if an I/O error occurs
     */
    private void resetGameProgress() throws IOException {
        loadNextLevel(FIRST_LEVEL);
        restartLevel();
    }

    /**
     * Closes the FRAMED instance
     */
    private void closeGame() {
        this.status = GameState.INVULNERABLE;
        this.framedRenderer.showExitPopup();
        this.status = GameState.EXIT;
    }

    /**
     * Constrains the player to the level's bounds, moving it back within them if outside
     */
    private void constrainPlayerToBounds() {
        // constrain X
        if (player.getPosition().getPosX() < Player.SIZE) {
            player.setPosition(Player.SIZE, player.getPosition().getPosY());
            player.setDX(0);
        } else if (player.getPosition().getPosX() > SCREEN_WIDTH - 18 - Player.SIZE) {
            player.setPosition(SCREEN_WIDTH - 18 - Player.SIZE, player.getPosition().getPosY());
            player.setDX(0);
        }
        // constrain Y
        if (player.getPosition().getPosY() < Player.SIZE) {
            player.setPosition(player.getPosition().getPosX(), Player.SIZE);
            player.setDY(0);
        } else if (player.getPosition().getPosY() > SCREEN_HEIGHT - STATUS_ROW_HEIGHT - 9 - Player.SIZE) {
            player.setPosition(player.getPosition().getPosX(),
                    SCREEN_HEIGHT - STATUS_ROW_HEIGHT - 9 - Player.SIZE);
            player.setDY(0);
        }
    }

    /**
     * Resets the current level to its initial state
     */
    private void restartLevel() {
        this.graphicalFrameRate = this.level.getInitialFrameRate();
        this.level.startLevel(player);
        this.graphicalUpdates[1] = true;
    }

    /**
     * Increases the level's graphical frame rate to the next rate in FRAME_RATES
     */
    private void increaseFrameRate() {
        int index = FRAME_RATES.indexOf(this.graphicalFrameRate);
        if (index != FRAME_RATES.size() - 1) {
            this.graphicalFrameRate = FRAME_RATES.get(index + 1);
        }
        graphicalUpdates[1] = true;
    }

    /**
     * Decreases the level's graphical frame rate to the previous rate in FRAME_RATES
     */
    private void decreaseFrameRate() {
        int index = FRAME_RATES.indexOf(this.graphicalFrameRate);
        if (index != 0) {
            this.graphicalFrameRate = FRAME_RATES.get(index - 1);
        }
        graphicalUpdates[1] = true;
    }

    /**
     * Toggles the pause state of the game, stopping/starting the tick cycle
     */
    private void togglePause() {
        if (this.status == GameState.PAUSED) {
            unpause();
        } else {
            pause();
        }
    }

    /**
     * Changes the game's state to PAUSED and temporarily stops the tick cycle
     */
    private void pause() {
        this.status = GameState.PAUSED;
        framedRenderer.openPauseMenu();
    }

    /**
     * Changes the game's state to ACTIVE and resumes the tick cycle
     */
    private void unpause() {
        this.status = GameState.ACTIVE;
        framedRenderer.closePauseMenu();
        this.requestFocus();
        graphicalUpdates[1] = true;
        graphicalUpdates[2] = false;
        repaint();
    }

    /**
     * Pauses the game and opens the settings menu + changes the game's state to SETTINGS
     */
    private void openSettings() {
        this.status = GameState.SETTINGS;
        graphicalUpdates[0] = false;
        graphicalUpdates[2] = false;
        framedRenderer.openSettingsMenu();
    }

    /**
     * Closes the settings menu + reverts the game to its previous state
     */
    private void closeSettings() {
        framedRenderer.closeSettingsMenu();
        if (framedRenderer.pauseMenuIsOpen()) {
            this.status = GameState.PAUSED;
            graphicalUpdates[0] = true;
            graphicalUpdates[2] = true;
        } else {
            this.status = GameState.ACTIVE;
            this.requestFocus();
            graphicalUpdates[1] = true;
            repaint();
        }
    }

    /**
     * Resets the game's ticker
     */
    private void resetStartTime() {
        startTime = -1;
    }

    /**
     * Sets the given graphical update boolean to the specified state
     *
     * @param update index of the graphical update boolean to change
     * @param state  new state
     */
    public void setGraphicalUpdate(int update, boolean state) {
        this.graphicalUpdates[update] = state;
    }

    /**
     * @return the FRAMED game's current level
     */
    public Level getLevel() {
        return this.level;
    }

    /**
     * @return the FRAMED game's player
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * @return the FRAMED game's current level display
     */
    public String getCurrentLevelDisplay() {
        return this.currentLevelDisplay;
    }

    /**
     * @return the FRAMED game's current status
     */
    public GameState getStatus() {
        return this.status;
    }

    /**
     * @return the FRAMED games' current graphical frame rate (as FPS)
     */
    public float getGraphicalFrameRate() {
        return this.graphicalFrameRate;
    }

    /**
     * @return the FRAMED game's frame delay tracker
     */
    public FrameDelayTracker getFdt() {
        return this.fdt;
    }

    /**
     * @return the FRAMED game's colour manager
     */
    public ColorManager getColManager() {
        return this.colManager;
    }

    /**
     * @return true if the game was started on the first level, false otherwise
     */
    public boolean isNewSave() {
        return this.newSave;
    }
}

