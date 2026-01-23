package com.spaceinvaders.game;

import com.spaceinvaders.utils.Constants;
import com.spaceinvaders.utils.ScoreManager;

import javax.swing.JFrame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;

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

        // Set window icon
        setWindowIcon();

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

    /**
     * Sets the window icon - tries multiple locations
     */
    private void setWindowIcon() {
        String[] possiblePaths = {
            "resources/images/space_invaders_icon.png",
            "resources/icons/game_icon.png",
            "icon.png",
            "space_invaders_icon.png",
            "src/resources/images/space_invaders_icon.png"
        };

        for (String path : possiblePaths) {
            java.io.File iconFile = new java.io.File(path);
            if (iconFile.exists()) {
                try {
                    ImageIcon icon = new ImageIcon(iconFile.getAbsolutePath());
                    setIconImage(icon.getImage());
                    System.out.println("Icon loaded from: " + path);
                    return;
                } catch (Exception e) {
                    System.err.println("Error loading icon from " + path + ": " + e.getMessage());
                }
            }
        }

        System.err.println("Icon not found in any location. Using default icon.");
    }
}