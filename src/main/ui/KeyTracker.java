package ui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a set of keys that have been typed and a set of keys that are currently pressed
 */
public class KeyTracker extends KeyAdapter {
    private final Set<Integer> pressedKeys;
    private final Set<KeyEvent> typedKeys;

    /**
     * Creates a new key catcher with empty lists of types and pressed keys
     */
    public KeyTracker() {
        this.pressedKeys = new HashSet<>();
        this.typedKeys = new HashSet<>();
    }

    /**
     * Adds the typed key to the list of types keys
     *
     * @param e key event emitted by the key being typed
     */
    @Override
    public void keyTyped(KeyEvent e) {
        this.typedKeys.add(e);
    }

    /**
     * Adds the pressed key to the list of keys currently being pressed
     *
     * @param e key event emitted by the key being pressed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        this.pressedKeys.add(e.getKeyCode());
    }

    /**
     * Removes the released key from the list of keys currently being pressed
     *
     * @param e key event emitted by key being released
     */
    @Override
    public void keyReleased(KeyEvent e) {
        this.pressedKeys.remove(e.getKeyCode());
    }

    /**
     * Clears the set of typed keys
     */
    public void clearTypedKeys() {
        this.typedKeys.clear();
    }

    /**
     * Clears the set of pressed keys
     */
    public void clearPressedKeys() {
        this.pressedKeys.clear();
    }

    /**
     * @return the set of keys currently being pressed
     */
    public Set<Integer> getPressedKeys() {
        return Collections.unmodifiableSet(this.pressedKeys);
    }

    /**
     * @return the set of keys that have been typed
     */
    public Set<KeyEvent> getTypedKeys() {
        return Collections.unmodifiableSet(this.typedKeys);
    }
}
