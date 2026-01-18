package com.spaceinvaders.entities;

import com.spaceinvaders.utils.Constants;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * The player spaceship - Classic Space Invaders style
 */
public class Player extends GameObject {
    // Movement flags
    private boolean movingLeft;
    private boolean movingRight;

    // Shooting cooldown
    private long lastShotTime;

    // Pixel size for drawing
    private static final int PIXEL = 3;

    // Player color
    private static final Color COLOR_PLAYER = new Color(0, 255, 0);

    // Cached sprite
    private static BufferedImage sprite;
    private static boolean spriteCreated = false;

    /**
     * Constructor - this creates player at bottom center of screen
     */
    public Player() {
        // Call parent constructor with starting position and size
        super(
            Constants.WINDOW_WIDTH / 2 - Constants.PLAYER_WIDTH / 2, // Starting X (center of screen)
            Constants.WINDOW_HEIGHT - Constants.PLAYER_Y_OFFSET - Constants.PLAYER_HEIGHT, // Starting Y (near bottom)
            Constants.PLAYER_WIDTH,
            Constants.PLAYER_HEIGHT
        );

        this.movingLeft = false;
        this.movingRight = false;
        this.lastShotTime = 0;

        // Create sprite only once
        if (!spriteCreated) {
            createSprite();
            spriteCreated = true;
        }
    }

    /**
     * Create cached sprite image - Classic Space Invaders cannon
     */
    private static void createSprite() {
        sprite = new BufferedImage(
            Constants.PLAYER_WIDTH, 
            Constants.PLAYER_HEIGHT, 
            BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g = sprite.createGraphics();
        g.setColor(COLOR_PLAYER);

        int centerX = Constants.PLAYER_WIDTH / 2 / PIXEL;  // Center in grid units

        // Row 0: Cannon tip (2 pixels wide)
        drawPixelAt(g, centerX, 0);
        drawPixelAt(g, centerX - 1, 0);
        
        // Row 1: Cannon neck (4 pixels wide)
        drawPixelAt(g, centerX - 1, 1);
        drawPixelAt(g, centerX, 1);
        drawPixelAt(g, centerX + 1, 1);
        drawPixelAt(g, centerX - 2, 1);
        
        // Row 2: Cannon neck (4 pixels wide)
        drawPixelAt(g, centerX - 1, 2);
        drawPixelAt(g, centerX, 2);
        drawPixelAt(g, centerX + 1, 2);
        drawPixelAt(g, centerX - 2, 2);
        
        // Row 3-5: Base (wide)
        for (int row = 3; row <= 5; row++) {
            for (int col = 1; col <= 14; col++) {
                drawPixelAt(g, col, row);
            }
        }
        
        // Row 6-7: Bottom base (full width)
        for (int row = 6; row <= 7; row++) {
            for (int col = 0; col <= 15; col++) {
                drawPixelAt(g, col, row);
            }
        }
        
        g.dispose();
    }

    /**
     * Helper to draw a single pixel
     */
    private static void drawPixelAt(Graphics2D g, int gridX, int gridY) {
        g.fillRect(gridX * PIXEL, gridY * PIXEL, PIXEL, PIXEL);
    }

    @Override
    public void update() {
        // Move left if flag is set
        if (movingLeft) {
            x -= Constants.PLAYER_SPEED; // Decrease x by Player speed
        }

        // Move right if flag is set
        if (movingRight) {
            x += Constants.PLAYER_SPEED; // Increase x by Player speed
        }


        // Keep player inside screen bounds
        if (x < 0) { // Bounds left
            x = 0;
        }
        if (x > Constants.WINDOW_WIDTH - width) { // Bounds right
            x = Constants.WINDOW_WIDTH - width;
        }
    }

    @Override
    public void render(Graphics2D g2d) {
        // Draw cached sprite
        g2d.drawImage(sprite, (int) x, (int) y, null);
    }
    
    /**
     * Check if player can shoot (cooldown passed)
     */
    public boolean canShoot() {
        long currentTime = System.currentTimeMillis();
        return currentTime - lastShotTime >= Constants.PLAYER_SHOOT_COOLDOWN;
    }

    /**
     * Record that player just shot
     */
    public void shoot() { lastShotTime = System.currentTimeMillis(); } // Set to current time

    /**
     * Get X position where bullet should spawn
     */
    public double getBulletSpawnX() {
        // Center of player minus half bullet width
        return x + width / 2 - Constants.BULLET_WIDTH / 2;
    }

    /**
     * Get Y position where bullet should spawn
     */
    public double getBulletSpawnY() {
        // Top of player minus bullet height
        return y - Constants.BULLET_HEIGHT;
    }

    /* Setters for movement flags */
    public void setMovingLeft(boolean moving) { this.movingLeft = moving; }

    public void setMovingRight(boolean moving) { this.movingRight = moving; }

    /**
     * Reset player to starting position
     */
    public void reset() {
        x = Constants.WINDOW_WIDTH / 2 - Constants.PLAYER_WIDTH / 2;
        y = Constants.WINDOW_HEIGHT - Constants.PLAYER_Y_OFFSET - Constants.PLAYER_HEIGHT;
        active = true;
    }
}