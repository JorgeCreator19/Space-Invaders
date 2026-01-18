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
    private boolean upPressed;
    private boolean downPressed;
    private boolean leftConsumed;
    private boolean rightConsumed;
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
        upPressed = false;
        downPressed = false;
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

        // Up arrow or W
        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
            upPressed = true;
        }

        // Down arrow or S
        if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
            downPressed = true;
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
            leftConsumed = false;
        }

        // Right arrow or D
        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            rightPressed = false;
            rightConsumed = false;
        }

        // Up arrow or W
        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
            upPressed = false;
        }

        // Down arrow or S
        if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
            downPressed = false;
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

    /**
     * Check and consume up press
     */
    public boolean consumeUp() {
        if (upPressed) {
            upPressed = false;
            return true;
        }
        return false;
    }

    /**
     * Check and consume down press
     */
    public boolean consumeDown() {
        if (downPressed) {
            downPressed = false;
            return true;
        }
        return false;
    }

    public boolean consumeLeft() {
        if (leftPressed && !leftConsumed) {
            leftConsumed = true;
            return true;
        }
        return false;
    }

    public boolean consumeRight() {
        if (rightPressed && !rightConsumed) {
            rightConsumed = true;
            return true;
        }
        return false;
    }
}