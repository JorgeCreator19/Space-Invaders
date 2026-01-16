package com.spaceinvaders.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Handles all keyboard input
 */
public class InputHandler implements KeyListener {
    
    // Key states - true if currently held down
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean shootPressed;
    private boolean pausePressed;
    private boolean enterPressed;
    private boolean escapePressed;

    /**
     * Constructor - initialize all keys to not pressed
     */
    public InputHandler() {
        leftPressed = false;
        rightPressed = false;
        shootPressed = false;
        pausePressed = false;
        enterPressed = false;
        escapePressed = false;
    }

    /**
     * Called when a key is pressed down
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // Left arrow or A
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            leftPressed = true;
        }

        // Right arrow or D
        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            rightPressed = true;
        }

        // Space bar to shoot
        if (key == KeyEvent.VK_SPACE) {
            shootPressed = true;
        }

        // P key to Pause
        if (key == KeyEvent.VK_P) {
            pausePressed = true;
        }

        // Enter key 
        if (key == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }

        // Escape key 
        if (key == KeyEvent.VK_ESCAPE) {
            escapePressed = true;
        }
    }

    /**
     * Called when a key is released
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        // Left arrow or A
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            leftPressed = false;
        }

        // Right arrow or D
        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            rightPressed = false;
        }

        // Space bar to shoot
        if (key == KeyEvent.VK_SPACE) {
            shootPressed = false;
        }

        // P key to Pause
        if (key == KeyEvent.VK_P) {
            pausePressed = false;
        }

        // Enter key 
        if (key == KeyEvent.VK_ENTER) {
            enterPressed = false;
        }

        // Escape key 
        if (key == KeyEvent.VK_ESCAPE) {
            escapePressed = false;
        }
    }

    /**
     * Required by KeyListener
     */
    @Override
    public void keyTyped(KeyEvent e) {} // Not used

    /* GETTERS for continuous actions (hold keys) */

    public boolean isLeftPressed() { return leftPressed; }
    public boolean isRightPressed() { return rightPressed; }
    public boolean isShootPressed() { return shootPressed; }

    /* Consume methods for single-press actions */
    // These return true ONCE, then reset to false

    /**
     * Check adn consume pause press
     * Returns true only once per key press
     */
    public boolean consumePause() {
        if (pausePressed) {
            pausePressed = false; // Reset so it only triggers once
            return true;
        }
        return false;
    }

    /**
     * Check and consume enter press
     */
    public boolean consumeEnter() {
        if (enterPressed) {
            enterPressed = false;
            return true;
        }
        return false;
    }

    /**
     * Check  and consume escape press
     */
    public boolean consumeEscape() {
        if (escapePressed) {
            escapePressed = false;
            return true;
        }
        return false;
    }
}