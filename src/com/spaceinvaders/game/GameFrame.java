package com.spaceinvaders.game;

import com.spaceinvaders.utils.Constants;
import com.spaceinvaders.utils.ScoreManager;

import javax.swing.JFrame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Handle close manually

        // Save high score when closing window
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Save high score before exiting
                ScoreManager.getInstance().saveHighScore();
                System.out.println("Game closed. High score saved!");
                System.exit(0);
            }
        });

        // Make visible
        setVisible(true);

        // Request focus for keyboard input
        gamePanel.requestFocusInWindow();
    }
}