package com.spaceinvaders.game;

/**
 * Game states enum
 * Tracks what "screen" the game is showing
 */
public enum GameState {
    MENU,       // Main menu state
    CONTROLS,   // Controls screen
    SETTINGS,   // Settings screen
    PLAYING,    // Game running state
    PAUSED,     // Game paused state
    GAME_OVER,  // Player died, lose game state
    VICTORY     // Player wins, wave is complete
}