package com.spaceinvaders.utils;

public final class Constants {
    /* WINDOW SETTINGS */
    public static final int WINDOW_WIDTH = 800; // 800 px
    public static final int WINDOW_HEIGHT = 600; // 600 px
    public static final String GAME_TITLE = "Space Invaders"; // The game title

    /* GAME SETTINGS */
    public static final int FPS = 60; // Frame per second
    public static final int GAME_SPEED = 1000 / FPS; // Game speed like ~16ms
    public static final int INITIAL_LIVES = 3; // Player start with 3 lives

    /* GRAPHICS SETTINGS */
    public static final int STAR_COUNT_HIGH = 150;    // High quality
    public static final int STAR_COUNT_MEDIUM = 75;   // Medium quality
    public static final int STAR_COUNT_LOW = 30;      // Low quality (better performance)

    /* SETTINGS QUALITY LEVELS */
    public static final int QUALITY_HIGH = 0;
    public static final int QUALITY_MEDIUM = 1;
    public static final int QUALITY_LOW = 2;

    /* MENU SETTINGS */
    public static final int STAR_COUNT = 100; // Number of stars in background

    /* PLAYER SETTINGS */
    public static final int PLAYER_WIDTH = 50; // 50 px
    public static final int PLAYER_HEIGHT = 30; // 30 px
    public static final int PLAYER_SPEED = 5; // 5 px per frame
    public static final int PLAYER_Y_OFFSET = 50; // Distance from botton
    public static final long PLAYER_SHOOT_COOLDOWN = 400; // 400 ms between shots

    /* BULLET SETTINGS */
    public static final int BULLET_WIDTH = 4; // 4px
    public static final int BULLET_HEIGHT = 12; // 12px
    public static final int PLAYER_BULLET_SPEED = -8; // Bullet negative moves UP
    public static final int ALIEN_BULLET_SPEED = 5; // Bullet positive moves DOWN

    /* ALIEN SETTINGS */
    public static final int ALIEN_WIDTH = 40; // 40 px
    public static final int ALIEN_HEIGHT = 30; // 30 px
    public static final int ALIEN_COLUMNS = 11; // 11 aliens per row
    public static final int ALIEN_ROWS = 5; // 5 rows of aliens
    public static final int ALIEN_SPACING_X = 10; // Horizontal gap
    public static final int ALIEN_SPACING_Y = 10; // Vertical gap
    public static final int ALIEN_START_X = 50; // In left margin
    public static final int ALIEN_START_Y = 80; // In top margin
    public static final int ALIEN_SPEED = 1; // Alien movement speed
    public static final int ALIEN_DROP_DISTANCE = 20; // Px to drop when reversing
    public static final double ALIEN_SHOOT_CHANCE_BASE = 0.007; // Starting chance (0.7%)
    public static final double ALIEN_SHOOT_CHANCE_INCREMENT = 0.002; // Increase per wave (0.2%)
    public static final double ALIEN_SHOOT_CHANCE_MAX = 0.02; // Maximum chance (2%)

    /* MYSTERY SHIP */
    public static final int MYSTERY_SHIP_WIDTH = 60; // 60 px
    public static final int MYSTERY_SHIP_HEIGHT = 25; // 25 px
    public static final int MYSTERY_SHIP_SPEED = 3; // 3 px per frame
    public static final double MYSTERY_SHIP_SPAWN_CHANCE = 0.001; // 0.1% chance per frame

    /* SHIELD SETTINGS */ 
    public static final int SHIELD_COUNT = 4;           // Number of shields
    public static final int SHIELD_Y_OFFSET = 120;      // Distance from bottom of screen

    /* SCORING */
    public static final int SCORE_ROW_1 = 10; // Botton rows = 10 score
    public static final int SCORE_ROW_2 = 30; // Midle rows = 30 score
    public static final int SCORE_ROW_3 = 50; // Top row = 50 score

    private Constants() {
        // Private constructor
    }
}