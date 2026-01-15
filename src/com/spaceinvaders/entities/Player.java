package com.spaceinvaders.entities;

import com.spaceinvaders.utils.Constants;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * The player spaceship
 */
public class Player extends GameObject {
    // Movement flags
    private boolean movingLeft;
    private boolean movingRight;

    // Shooting cooldown
    private long lastShotTime;

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
        // Draw player as green rectangle
        g2d.setColor(new Color(0, 255, 0));
        g2d.fillRect((int) x, (int) y, width, height);

        // Draw cannon on top
        int cannonWidth = 6;
        int cannonHeigth = 10;
        int cannonX = (int) x + width / 2 - cannonWidth / 2;
        int cannonY = (int) y - cannonHeigth;
        g2d.fillRect(cannonX, cannonY, cannonWidth, cannonHeigth);
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
    public void shoot() {
        lastShotTime = System.currentTimeMillis(); // Set to current time
    }

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