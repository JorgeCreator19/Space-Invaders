package com.spaceinvaders.game;

import com.spaceinvaders.utils.Constants;
import javax.swing.JFrame;

/**
 * Main game window class
 */
public class GameFrame extends JFrame {
    
    /**
     * Constructor - creates the game window
     */
    public GameFrame() {
        // Show the game title
        setTitle(Constants.GAME_TITLE);

        // Create and add GamePanel
        GamePanel gamePanel = new GamePanel();
        add(gamePanel);

        // Configure window
        setResizable(false);     // Cannot resize
        pack();                            // Size to fit GamePanel
        setLocationRelativeTo(null);    // Center on screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit when closed

        // Make visible
        setVisible(true);

        // Request focus for keyboard input
        gamePanel.requestFocusInWindow();
    }
}