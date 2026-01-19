package com.spaceinvaders.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Manages saving and loading high scores
 * Saves to a file so scores persist between game sessions
 */
public class ScoreManager {
    
    // Singleton instance
    private static ScoreManager instance;
    
    // File to save scores
    private static final String SAVE_FILE = "highscore.dat";
    
    // Current high score
    private int highScore;
    
    /**
     * Private constructor (Singleton)
     */
    private ScoreManager() {
        loadHighScore();
    }
    
    /**
     * Get singleton instance
     */
    public static ScoreManager getInstance() {
        if (instance == null) {
            instance = new ScoreManager();
        }
        return instance;
    }
    
    /**
     * Load high score from file
     */
    private void loadHighScore() {
        File file = new File(SAVE_FILE);
        
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line = reader.readLine();
                if (line != null && !line.isEmpty()) {
                    highScore = Integer.parseInt(line.trim());
                    System.out.println("High score loaded: " + highScore);
                }
            } catch (IOException | NumberFormatException e) {
                System.out.println("Could not load high score, starting fresh.");
                highScore = 0;
            }
        } else {
            highScore = 0;
        }
    }
    
    /**
     * Save high score to file
     */
    public void saveHighScore() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SAVE_FILE))) {
            writer.write(String.valueOf(highScore));
            System.out.println("High score saved: " + highScore);
        } catch (IOException e) {
            System.out.println("Could not save high score: " + e.getMessage());
        }
    }
    
    /**
     * Get current high score
     */
    public int getHighScore() { return highScore; }
    
    /**
     * Check and update high score if new score is higher
     * @param newScore The score to check
     * @return true if new high score was set
     */
    public boolean checkAndUpdateHighScore(int newScore) {
        if (newScore > highScore) {
            highScore = newScore;
            saveHighScore();  // Auto-save when new high score
            return true;
        }
        return false;
    }
    
    /**
     * Reset high score to zero
     */
    public void resetHighScore() {
        highScore = 0;
        saveHighScore();
    }
}