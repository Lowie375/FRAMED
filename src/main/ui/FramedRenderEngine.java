package ui;

import elem.LevelElement;
import model.Level;
import model.Player;
import org.json.JSONObject;
import util.ColorManager;
import util.JsonParser;
import util.Position;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static java.awt.Component.BOTTOM_ALIGNMENT;
import static java.awt.event.KeyEvent.*;
import static javax.swing.JLayeredPane.MODAL_LAYER;

/**
 * Represents the rendering engine for an instance of FRAMED
 */
public class FramedRenderEngine implements ActionListener, ChangeListener {
    public static final int THEME_TAB = 0;
    public static final int ANALYSIS_TAB = 1;

    public static final int GRAPH_MAX_HEIGHT = 300;

    private final FramedGame framed;
    private final ColorManager colManager;
    private final ColorManager tempColManager;
    private int currentColIndex;

    private JPanel pauseMenu;
    private JPanel settingsMenu;

    /**
     * Creates a FRAMED rendering engine for the given FRAMED instance + adds all menu panels to the instance
     *
     * @param framed active FRAMED instance
     */
    public FramedRenderEngine(FramedGame framed, JSONObject colours) {
        this.framed = framed;
        this.colManager = new ColorManager(
                JsonParser.jsonToColor(colours.getJSONObject(ColorManager.KEYS[ColorManager.PLAYER])),
                JsonParser.jsonToColor(colours.getJSONObject(ColorManager.KEYS[ColorManager.OBST])),
                JsonParser.jsonToColor(colours.getJSONObject(ColorManager.KEYS[ColorManager.WALL])),
                JsonParser.jsonToColor(colours.getJSONObject(ColorManager.KEYS[ColorManager.GOAL])),
                JsonParser.jsonToColor(colours.getJSONObject(ColorManager.KEYS[ColorManager.BG])),
                JsonParser.jsonToColor(colours.getJSONObject(ColorManager.KEYS[ColorManager.TEXT])),
                JsonParser.jsonToColor(colours.getJSONObject(ColorManager.KEYS[ColorManager.DIALOG_BASE])),
                JsonParser.jsonToColor(colours.getJSONObject(ColorManager.KEYS[ColorManager.DIALOG_ACC])));
        this.tempColManager = new ColorManager(
                JsonParser.jsonToColor(colours.getJSONObject(ColorManager.KEYS[ColorManager.PLAYER])),
                JsonParser.jsonToColor(colours.getJSONObject(ColorManager.KEYS[ColorManager.OBST])),
                JsonParser.jsonToColor(colours.getJSONObject(ColorManager.KEYS[ColorManager.WALL])),
                JsonParser.jsonToColor(colours.getJSONObject(ColorManager.KEYS[ColorManager.GOAL])),
                JsonParser.jsonToColor(colours.getJSONObject(ColorManager.KEYS[ColorManager.BG])),
                JsonParser.jsonToColor(colours.getJSONObject(ColorManager.KEYS[ColorManager.TEXT])),
                JsonParser.jsonToColor(colours.getJSONObject(ColorManager.KEYS[ColorManager.DIALOG_BASE])),
                JsonParser.jsonToColor(colours.getJSONObject(ColorManager.KEYS[ColorManager.DIALOG_ACC])));
        this.currentColIndex = 0;
        initializePausePanel();
        initializeSettingsPanel();
    }

    /**
     * Handles colour-related action events
     *
     * @param e action event emitted
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JComboBox<? extends Object> comboBox;

        // check if combo box
        try {
            comboBox = (JComboBox<?>) e.getSource();
            if (comboBox.getName().equals("ThemeColSelect")) {
                currentColIndex = comboBox.getSelectedIndex();
                getThemeColorChooser().setColor(tempColManager.getColor(ColorManager.KEYS[currentColIndex]));
            }
        } catch (NullPointerException | ClassCastException nce) {
            // not a combo box, continue
        }
    }

    /**
     * Handles colour-related state change events
     *
     * @param e state change event emitted
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        ColorSelectionModel colChooser = (ColorSelectionModel) e.getSource();
        colChooser.getSelectedColor();
        this.tempColManager.setColor(ColorManager.KEYS[currentColIndex], colChooser.getSelectedColor());
    }

    /**
     * Initializes and hides the pause panel
     *
     * @see <a href="https://docs.oracle.com/javase/tutorial/uiswing/components/index.html">Swing documentation, referenced during method creation</a>
     */
    private void initializePausePanel() {
        this.pauseMenu = new JPanel();
        pauseMenu.setLayout(new BoxLayout(pauseMenu, BoxLayout.Y_AXIS));
        pauseMenu.setVisible(false);
        pauseMenu.setBounds((framed.getWidth() - 450) / 2 - 9,
                (framed.getHeight() - 100 - FramedGame.STATUS_ROW_HEIGHT / 2) / 2, 450, 100);
        pauseMenu.setBackground(colManager.getColor(ColorManager.KEYS[ColorManager.DIALOG_BASE]));
        pauseMenu.setBorder(BorderFactory.createLineBorder(
                colManager.getColor(ColorManager.KEYS[ColorManager.DIALOG_ACC])));

        pauseMenu.add(Box.createVerticalGlue());
        pauseMenu.add(createPauseLabel());
        pauseMenu.add(createPauseButtonRow());
        pauseMenu.add(Box.createVerticalGlue());

        this.framed.add(pauseMenu);
        framed.getLayeredPane().setLayer(pauseMenu, MODAL_LAYER);
    }

    /**
     * Creates a centered label for the pause menu
     *
     * @return the label
     */
    private JPanel createPauseLabel() {
        JPanel label = new JPanel();
        label.setLayout(new BoxLayout(label, BoxLayout.X_AXIS));
        label.setBackground(colManager.getColor(ColorManager.KEYS[ColorManager.DIALOG_BASE]));

        JLabel pauseLabel = new JLabel("[PAUSED]");
        pauseLabel.setFont(new Font("Monospaced", Font.BOLD, 24));
        pauseLabel.setForeground(colManager.getColor(ColorManager.KEYS[ColorManager.TEXT]));

        label.add(Box.createHorizontalGlue());
        label.add(pauseLabel);
        label.add(Box.createHorizontalGlue());

        return label;
    }

    /**
     * Creates a row of action buttons for the pause menu
     *
     * @return the completed button row
     */
    private JPanel createPauseButtonRow() {
        JPanel buttonRow = new JPanel();
        buttonRow.setBackground(colManager.getColor(ColorManager.KEYS[ColorManager.DIALOG_BASE]));

        JButton unpause = buildButton("UNPAUSE", "pause.unpause", VK_P);
        JButton restart = buildButton("RESTART", "pause.restart", VK_R);
        JButton settings = buildButton("SETTINGS", "pause.settings", VK_S);
        JButton exit = buildButton("SAVE & EXIT", "pause.exit", VK_X);

        buttonRow.add(unpause);
        buttonRow.add(restart);
        buttonRow.add(settings);
        buttonRow.add(exit);

        return buttonRow;
    }

    /**
     * Builds a button with the given name, mnemonic, and emitted action
     *
     * @param name button text
     * @param actionEmitted name of action emitted when button is clicked
     * @param mnemonicKey key used for button mnemonic
     * @return the built button
     */
    private JButton buildButton(String name, String actionEmitted, Integer mnemonicKey) {
        JButton button = new JButton(name);
        button.setBackground(colManager.getColor(ColorManager.KEYS[ColorManager.DIALOG_ACC]));
        button.setForeground(colManager.getColor(ColorManager.KEYS[ColorManager.TEXT]));

        if (mnemonicKey != null) {
            button.setMnemonic(mnemonicKey);
        }
        button.setActionCommand(actionEmitted);
        button.addActionListener(this.framed);

        return button;
    }

    /**
     * Initializes and hides the settings panel
     *
     * @see <a href="https://docs.oracle.com/javase/tutorial/uiswing/components/index.html">Swing documentation, referenced during method creation</a>
     */
    private void initializeSettingsPanel() {
        this.settingsMenu = new JPanel(new BorderLayout());
        settingsMenu.setVisible(false);
        settingsMenu.setBounds((framed.getWidth() - 800) / 2 - 9,
                (framed.getHeight() - 375 - FramedGame.STATUS_ROW_HEIGHT / 2) / 2, 800, 375);
        settingsMenu.setBackground(colManager.getColor(ColorManager.KEYS[ColorManager.DIALOG_BASE]));
        settingsMenu.setBorder(BorderFactory.createLineBorder(
                colManager.getColor(ColorManager.KEYS[ColorManager.DIALOG_ACC])));

        settingsMenu.add(createSettingsLabel(), BorderLayout.PAGE_START);
        settingsMenu.add(createTabMenu(), BorderLayout.CENTER);

        this.framed.add(settingsMenu);
        framed.getLayeredPane().setLayer(settingsMenu, MODAL_LAYER);
    }

    /**
     * Creates a centred label for the settings menu
     *
     * @return the label
     */
    private JPanel createSettingsLabel() {
        JPanel label = new JPanel(new BorderLayout());
        label.setBackground(colManager.getColor(ColorManager.KEYS[ColorManager.DIALOG_BASE]));

        JLabel settingsLabel = new JLabel("SETTINGS", JLabel.CENTER);
        settingsLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        settingsLabel.setForeground(colManager.getColor("text"));

        JButton exit = buildButton("X", "settings.exit", VK_X);

        label.add(settingsLabel, BorderLayout.CENTER);
        label.add(exit, BorderLayout.LINE_END);

        return label;
    }

    /**
     * Creates a tabbed settings menu
     *
     * @return the tabbed menu
     */
    private JTabbedPane createTabMenu() {
        JTabbedPane tabMenu = new JTabbedPane();
        tabMenu.setBackground(colManager.getColor(ColorManager.KEYS[ColorManager.DIALOG_BASE]));
        tabMenu.setForeground(colManager.getColor(ColorManager.KEYS[ColorManager.TEXT]));

        JPanel themeTab = createThemeTab();
        // JPanel advancedTab = createAdvancedTab(); // currently unused
        JPanel analysisTab = createAnalysisTab();

        tabMenu.addTab("Theme", themeTab);
        //tabMenu.addTab("Advanced", advancedTab);
        tabMenu.addTab("Level Analysis", analysisTab);
        return tabMenu;
    }

    /**
     * Builds the "Theme" tab of the settings menu
     *
     * @return the completed tab
     */
    private JPanel createThemeTab() {
        JPanel themeTab = new JPanel(new BorderLayout());
        themeTab.setBackground(colManager.getColor(ColorManager.KEYS[ColorManager.DIALOG_BASE]));

        themeTab.add(buildThemeSelectPanel(), BorderLayout.LINE_START);
        themeTab.add(buildColorChooserPanel(), BorderLayout.CENTER);

        return themeTab;
    }

    /**
     * Builds the colour chooser panel of the theme tab
     *
     * @return the completed panel
     */
    private JColorChooser buildColorChooserPanel() {
        JColorChooser themeCol = new JColorChooser();
        themeCol.setBackground(colManager.getColor(ColorManager.KEYS[ColorManager.DIALOG_BASE]));

        AbstractColorChooserPanel[] colPanels = themeCol.getChooserPanels();
        themeCol.removeChooserPanel(colPanels[4]);
        themeCol.removeChooserPanel(colPanels[0]);
        for (AbstractColorChooserPanel colPanel : colPanels) {
            colPanel.setBackground(colManager.getColor(ColorManager.KEYS[ColorManager.DIALOG_BASE]));
            colPanel.setForeground(colManager.getColor(ColorManager.KEYS[ColorManager.TEXT]));
        }
        themeCol.getSelectionModel().addChangeListener(this);
        themeCol.setColor(tempColManager.getColor(ColorManager.KEYS[currentColIndex]));

        return themeCol;
    }

    /**
     * Builds the colour dropdown panel of the theme tab
     *
     * @return the completed panel
     */
    private JPanel buildThemeSelectPanel() {
        JPanel themeSelect = new JPanel(new BorderLayout());
        themeSelect.setBackground(colManager.getColor(ColorManager.KEYS[ColorManager.DIALOG_BASE]));

        JComboBox<String> colorToEdit = new JComboBox<>(ColorManager.ELEMENTS_WITH_COL);
        colorToEdit.setSelectedIndex(0);
        colorToEdit.setName("ThemeColSelect");
        colorToEdit.addActionListener(this);

        themeSelect.add(colorToEdit, BorderLayout.PAGE_START);
        themeSelect.add(buildThemeSelectButtonColumn(), BorderLayout.CENTER);

        return themeSelect;
    }

    /**
     * Builds the button column for the theme tab's colour selection sidebar panel
     *
     * @return the completed button column
     */
    private JPanel buildThemeSelectButtonColumn() {
        JPanel buttonColumn = new JPanel(new GridLayout(0, 1));
        buttonColumn.setBackground(colManager.getColor(ColorManager.KEYS[ColorManager.DIALOG_BASE]));
        buttonColumn.setForeground(colManager.getColor(ColorManager.KEYS[ColorManager.TEXT]));

        buttonColumn.add(buildButton("RESET TO CURRENT", "settings.theme.reset", null));
        buttonColumn.add(buildButton("RESET TO DEFAULT", "settings.theme.default", null));
        buttonColumn.add(buildButton("APPLY + SAVE", "settings.theme.apply", null));

        return buttonColumn;
    }

    /**
     * Builds the "Advanced" tab of the settings menu
     *
     * @return the completed tab
     */
    private JPanel createAdvancedTab() {
        JPanel advancedTab = new JPanel();
        advancedTab.setBackground(colManager.getColor(ColorManager.KEYS[ColorManager.DIALOG_BASE]));
        advancedTab.setForeground(colManager.getColor(ColorManager.KEYS[ColorManager.TEXT]));

        return advancedTab;
    }

    /**
     * Builds the "Level Analysis" tab of the settings menu
     *
     * @return the completed tab
     */
    private JPanel createAnalysisTab() {
        JPanel analysisTab = new JPanel(new BorderLayout());
        analysisTab.setBackground(colManager.getColor(ColorManager.KEYS[ColorManager.DIALOG_BASE]));
        analysisTab.setForeground(colManager.getColor(ColorManager.KEYS[ColorManager.TEXT]));

        JPanel buttonCol = new JPanel(new GridLayout(0, 1));
        buttonCol.add(buildButton("LEVEL BREAKDOWN", "settings.analyze.level", null));
        buttonCol.add(buildButton("AREA DISTRIBUTION", "settings.analyze.graph", null));

        JScrollPane scrollingText = buildScrollPane();
        JPanel analysisGraphic = new JPanel();
        analysisGraphic.setBackground(colManager.getColor(ColorManager.KEYS[ColorManager.DIALOG_BASE]));
        analysisGraphic.setForeground(colManager.getColor(ColorManager.KEYS[ColorManager.TEXT]));
        analysisGraphic.setMinimumSize(new Dimension(90, GRAPH_MAX_HEIGHT));

        analysisTab.add(buttonCol, BorderLayout.LINE_START);
        analysisTab.add(buildCombinedAnalysisPane(scrollingText, analysisGraphic), BorderLayout.CENTER);

        return analysisTab;
    }

    /**
     * Combines the given scrolling text pane and graph pane into one large content pane
     *
     * @param scrollingText scrolling text pane
     * @param analysisGraphic analysis graphic pane
     * @return the combined content pane
     */
    private JPanel buildCombinedAnalysisPane(JScrollPane scrollingText, JPanel analysisGraphic) {
        SpringLayout combinedLayout = new SpringLayout();
        JPanel combinedOutputPane = new JPanel(combinedLayout);
        combinedOutputPane.add(scrollingText);
        combinedOutputPane.add(analysisGraphic);

        combinedLayout.putConstraint(SpringLayout.NORTH, analysisGraphic, 0, SpringLayout.NORTH, combinedOutputPane);
        combinedLayout.putConstraint(SpringLayout.SOUTH, analysisGraphic, 0, SpringLayout.SOUTH, combinedOutputPane);
        combinedLayout.putConstraint(SpringLayout.EAST, analysisGraphic, 0, SpringLayout.EAST, combinedOutputPane);

        combinedLayout.putConstraint(SpringLayout.NORTH, scrollingText, 0, SpringLayout.NORTH, combinedOutputPane);
        combinedLayout.putConstraint(SpringLayout.SOUTH, scrollingText, 0, SpringLayout.SOUTH, combinedOutputPane);
        combinedLayout.putConstraint(SpringLayout.EAST, scrollingText, 0, SpringLayout.EAST, analysisGraphic);
        combinedLayout.putConstraint(SpringLayout.WEST, scrollingText, 0, SpringLayout.WEST, combinedOutputPane);

        return combinedOutputPane;
    }

    /**
     * Builds the level analysis tab's scrolling text pane
     *
     * @return the completed pane
     */
    private JScrollPane buildScrollPane() {
        JTextArea elementList = new JTextArea("Use the sidebar buttons to analyze the current level!");
        elementList.setEditable(false);
        elementList.setBackground(colManager.getColor(ColorManager.KEYS[ColorManager.DIALOG_BASE]));
        elementList.setForeground(colManager.getColor(ColorManager.KEYS[ColorManager.TEXT]));
        elementList.setLineWrap(true);
        elementList.setWrapStyleWord(true);
        elementList.setFont(new Font("SansSerif", Font.PLAIN, 16));

        JScrollPane scrollingText = new JScrollPane(elementList);
        scrollingText.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollingText.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        return scrollingText;
    }

    /**
     * Clears the background of the FRAMED instance
     *
     * @param g FRAMED graphics object
     */
    public void clear(Graphics g) {
        g.setColor(colManager.getColor(ColorManager.KEYS[ColorManager.BG]));
        g.fillRect(0, FramedGame.STATUS_ROW_HEIGHT, FramedGame.SCREEN_WIDTH, FramedGame.SCREEN_HEIGHT);
    }

    /**
     * Renders the FRAMED instance in its current state
     *
     * @param g FRAMED graphics object
     * @param graphicalUpdates array of graphical update flags [full, status, dialog]
     */
    public void render(Graphics g, boolean[] graphicalUpdates) {
        if (graphicalUpdates[0]) {
            clear(g);
            renderLevel(g, framed.getLevel());
            renderPlayer(g, framed.getPlayer());
        }
        if (graphicalUpdates[1]) {
            renderStatusRow(g);
        }
        if (graphicalUpdates[2]) {
            if (pauseMenuIsOpen() && pauseMenuIsVisible()) {
                pauseMenu.setVisible(false);
                pauseMenu.setVisible(true);
            }
            if (settingsMenuIsOpen() && settingsMenuIsVisible()) {
                settingsMenu.setVisible(false);
                settingsMenu.setVisible(true);
            }
        }
    }

    /**
     * Renders the level ID and FPS text in the status row
     *
     * @param g FRAMED graphics object
     */
    public void renderStatusRow(Graphics g) {
        // clears status row
        g.setColor(colManager.getColor(ColorManager.KEYS[ColorManager.DIALOG_BASE]));
        g.fillRect(0, 0, FramedGame.SCREEN_WIDTH, FramedGame.STATUS_ROW_HEIGHT - 1);

        // fills row with status message
        g.setColor(colManager.getColor(ColorManager.KEYS[ColorManager.DIALOG_ACC]));
        g.fillRect(0, FramedGame.STATUS_ROW_HEIGHT - 1, FramedGame.SCREEN_WIDTH, 1);
        g.setColor(colManager.getColor(ColorManager.KEYS[ColorManager.TEXT]));
        g.setFont(new Font("Monospaced", Font.BOLD, 16));
        g.drawString(framed.getCurrentLevelDisplay() + " @ " + framed.getGraphicalFrameRate() + " FPS", 13, 53);
    }

    /**
     * Renders the player at the given position
     *
     * @param g FRAMED graphics object
     * @param player player to render
     */
    public void renderPlayer(Graphics g, Player player) {
        g.setColor(player.getCol());
        g.fillRoundRect((int) player.getPosition().getPosX() - Player.SIZE + 9,
                (int) player.getPosition().getPosY() - Player.SIZE + FramedGame.STATUS_ROW_HEIGHT,
                Player.SIZE * 2, Player.SIZE * 2, 3, 3);
    }

    /**
     * Renders everything in the given level
     *
     * @param g FRAMED graphics object
     * @param level level to render
     */
    public void renderLevel(Graphics g, Level level) {
        for (LevelElement elem : level.getAllElements()) {
            renderLevelElement(g, elem);
        }
    }

    /**
     * Renders the given level element on the playing field
     *
     * @param g FRAMED graphics object
     * @param elem element to render
     * @see #renderRectangle(Graphics, Position, int, int, Color)
     */
    private void renderLevelElement(Graphics g, LevelElement elem) {
        Position shiftedPos = new Position(elem.getPosition().getPosX() + 9,
                elem.getPosition().getPosY() + FramedGame.STATUS_ROW_HEIGHT);
        renderRectangle(g, shiftedPos, elem.getWidth(), elem.getHeight(), elem.getElementCol());
    }

    /**
     * Renders a rectangle of the given width, height, and colour at the given position
     *
     * @param g FRAMED graphics object
     * @param pos position to place upper left corner of rectangle at
     * @param width rectangle width
     * @param height rectangle height
     * @param col rectangle colour
     */
    private void renderRectangle(Graphics g, Position pos, int width, int height, Color col) {
        g.setColor(col);
        g.fillRect((int) pos.getPosX(), (int) pos.getPosY(), width, height);
    }

    /**
     * Opens the pause dialog box
     */
    public void openPauseMenu() {
        pauseMenu.setVisible(true);
        pauseMenu.setEnabled(true);
    }

    /**
     * Opens the settings dialog box
     */
    public void openSettingsMenu() {
        settingsMenu.setVisible(true);
        settingsMenu.setEnabled(true);
        if (pauseMenuIsOpen()) {
            pauseMenu.setVisible(false);
        }
    }

    /**
     * Closes the pause dialog box
     */
    public void closePauseMenu() {
        pauseMenu.setVisible(false);
        pauseMenu.setEnabled(false);
    }

    /**
     * Closes the settings dialog box
     */
    public void closeSettingsMenu() {
        settingsMenu.setVisible(false);
        settingsMenu.setEnabled(false);
        if (pauseMenuIsOpen()) {
            pauseMenu.setVisible(true);
        }
    }

    /**
     * Informs the user that their progress has been loaded (if they are not on the first level)
     */
    public void showLoadPopup() {
        Level loadedLevel = this.framed.getLevel();
        if (!framed.isNewSave()) {
            JOptionPane.showMessageDialog(this.framed,
                    "Welcome back to FRAMED!\nYour current level, " + loadedLevel.getNamespace() + "["
                            + loadedLevel.getID() + "], has successfully been loaded from the FRAMED save file.",
                    "FRAMED Game Manager", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Informs the user that their progress has been saved (if they are not on the first level)
     */
    public void showExitPopup() {
        Level loadedLevel = this.framed.getLevel();
        if (loadedLevel.getID() != 0 || !Objects.equals(loadedLevel.getNamespace(), "main") || !framed.isNewSave()) {
            JOptionPane.showMessageDialog(this.framed,
                    "Your current progress has been saved.\n" + loadedLevel.getNamespace() + "["
                            + loadedLevel.getID() + "] will be loaded on relaunch.",
                    "FRAMED Game Manager", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Reverts the selected colour to its current saved state
     */
    public void revertSelectedColToCurrent() {
        Color savedCol = this.colManager.getColor(ColorManager.KEYS[currentColIndex]);
        this.tempColManager.setColor(ColorManager.KEYS[currentColIndex], savedCol);
        getThemeColorChooser().setColor(savedCol);
    }

    /**
     * Reverts the selected colour to its default colour
     */
    public void revertSelectedColToDefault() {
        Color defaultCol = ColorManager.ELEMENT_COLS[currentColIndex];
        this.tempColManager.setColor(ColorManager.KEYS[currentColIndex], defaultCol);
        getThemeColorChooser().setColor(defaultCol);
    }

    /**
     * Sets the level analysis tab's scrolling text box text to the given string
     *
     * @param string text to display in text box
     */
    public void setAnalysisText(String string) {
        JTextArea textBox = getAnalysisTextArea();
        textBox.setText(string);
        textBox.setCaretPosition(0);
    }

    /**
     * Generates a graph comparing the numbers of each type of level element in the current level
     *
     * @deprecated due to poor functionality
     */
    public void createBreakdownGraph() {
        JPanel graph = getAnalysisGraphPanel();
        // resets the graph panel
        graph.removeAll();
        graph.setLayout(new BoxLayout(graph, BoxLayout.LINE_AXIS));

        // determines element counts
        float obstCount = this.framed.getLevel().getObstacles().size();
        float wallCount = this.framed.getLevel().getWalls().size();
        float goalCount = this.framed.getLevel().getGoals().size();
        float max = Math.max(obstCount, Math.max(wallCount, goalCount));

        buildBreakdownBar(graph, obstCount, max, ColorManager.OBST);
        buildBreakdownBar(graph, wallCount, max, ColorManager.WALL);
        buildBreakdownBar(graph, goalCount, max, ColorManager.GOAL);
    }

    /**
     * Builds a single bar of a FRAMED level breakdown graph
     *
     * @param graph graph to add the bar to
     * @param elemCount element count of the bar
     * @param max highest element count of all the bars
     * @param col bar colour
     * @deprecated due to poor functionality
     * @see #createBreakdownGraph()
     */
    private void buildBreakdownBar(JPanel graph, float elemCount, float max, int col) {
        JPanel bar = new JPanel();
        bar.setBackground(this.colManager.getColor(ColorManager.KEYS[col]));
        bar.setMinimumSize(new Dimension(50, (int) (elemCount / max * GRAPH_MAX_HEIGHT)));
        bar.setMaximumSize(new Dimension(50, (int) (elemCount / max * GRAPH_MAX_HEIGHT)));
        bar.setAlignmentY(BOTTOM_ALIGNMENT);
        graph.add(bar);
    }

    /**
     * @return the colour chooser element from the settings menu's theme tab
     */
    private JColorChooser getThemeColorChooser() {
        JTabbedPane tabs = (JTabbedPane) this.settingsMenu.getComponent(1);
        JPanel themeTab = (JPanel) tabs.getComponent(THEME_TAB);
        return (JColorChooser) themeTab.getComponent(1);
    }

    /**
     * @return the text area element from the settings menu's analysis tab
     */
    private JTextArea getAnalysisTextArea() {
        JTabbedPane tabs = (JTabbedPane) this.settingsMenu.getComponent(1);
        JPanel analysisTab = (JPanel) tabs.getComponent(ANALYSIS_TAB);
        JPanel combinedPanel = (JPanel) analysisTab.getComponent(1);
        JScrollPane scrollPane = (JScrollPane) combinedPanel.getComponent(0);
        JViewport viewport = (JViewport) scrollPane.getComponent(0);
        return (JTextArea) viewport.getComponent(0);
    }

    /**
     * @return the graph element from the settings menu's analysis tab
     * @see #createBreakdownGraph()
     */
    private JPanel getAnalysisGraphPanel() {
        JTabbedPane tabs = (JTabbedPane) this.settingsMenu.getComponent(1);
        JPanel analysisTab = (JPanel) tabs.getComponent(ANALYSIS_TAB);
        JPanel combinedPanel = (JPanel) analysisTab.getComponent(1);
        return (JPanel) combinedPanel.getComponent(1);
    }

    /**
     * Updates the colour manager with the new selected colours + updated the colours of GUI components
     */
    public void updateColours() {
        for (String key : ColorManager.KEYS) {
            this.colManager.setColor(key, tempColManager.getColor(key));
        }
        updateUIColours();
    }

    /**
     * Updates the colours of the FRAMED GUI
     */
    private void updateUIColours() {
        updateComponentColour(pauseMenu);
        pauseMenu.setBorder(BorderFactory.createLineBorder(
                colManager.getColor(ColorManager.KEYS[ColorManager.DIALOG_ACC])));
        for (Component c : getAllComponentsInTree(pauseMenu)) {
            updateComponentColour(c);
        }

        updateComponentColour(settingsMenu);
        settingsMenu.setBorder(BorderFactory.createLineBorder(
                colManager.getColor(ColorManager.KEYS[ColorManager.DIALOG_ACC])));
        for (Component c : getAllComponentsInTree(settingsMenu)) {
            updateComponentColour(c);
        }
    }

    /**
     * Returns a list of all the subcomponents of a given container
     *
     * @param container container to get the subcomponents of (must be a JComponent)
     */
    private Set<JComponent> getAllComponentsInTree(Container container) {
        Set<JComponent> components = new HashSet<>();
        for (Component c : container.getComponents()) {
            components.add((JComponent) c);
            try {
                Container subContainer = (Container) c;
                components.addAll(getAllComponentsInTree(subContainer));
            } catch (NullPointerException | ClassCastException e) {
                // not a container, continue
            }
        }
        return components;
    }

    /**
     * Updates the colour of the given component
     *
     * @param c component to update
     */
    private void updateComponentColour(Component c) {
        switch (c.getClass().getSimpleName()) {
            case "JButton":
            case "JComboBox":
                c.setBackground(colManager.getColor(ColorManager.KEYS[ColorManager.DIALOG_ACC]));
                break;
            default:
                c.setBackground(colManager.getColor(ColorManager.KEYS[ColorManager.DIALOG_BASE]));
                break;
        }
        c.setForeground(colManager.getColor(ColorManager.KEYS[ColorManager.TEXT]));
    }

    /**
     * @return true if the pause dialog is currently open, false otherwise
     */
    public boolean pauseMenuIsOpen() {
        return pauseMenu.isEnabled();
    }

    /**
     * @return true if the pause dialog is currently visible, false otherwise
     */
    public boolean pauseMenuIsVisible() {
        return pauseMenu.isVisible();
    }

    /**
     * @return true if the settings dialog is currently open, false otherwise
     */
    public boolean settingsMenuIsOpen() {
        return settingsMenu.isEnabled();
    }

    /**
     * @return true if the settings dialog is currently visible, false otherwise
     */
    public boolean settingsMenuIsVisible() {
        return settingsMenu.isVisible();
    }

    /**
     * @return the renderer's colour manager
     */
    public ColorManager getColManager() {
        return this.colManager;
    }

    /**
     * @return the renderer's temporary colour manager (for tracking changed colours)
     */
    public ColorManager getTempColManager() {
        return this.tempColManager;
    }
}
