package com.spaceinvaders.entities;

import com.spaceinvaders.utils.Constants;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Bullet class - Green for player, Red for aliens
 */
public class Bullet extends GameObject {

    // Is this bullet from player or alien?
    private boolean isPlayerBullet;

    // Bullet colors
    private static final Color COLOR_PLAYER_BULLET = new Color(0, 255, 0);  // Green
    private static final Color COLOR_ALIEN_BULLET = new Color(255, 0, 0);   // Red

    /**
     * 
     * @param x starting X position
     * @param y starting Y position
     * @param isPlayerBullet true = player's bullet (goes UP in green color), false = alien's (goes DOWN in red color)
     */
    public Bullet(double x , double y, boolean isPlayerBullet) {
        super(x, y, Constants.BULLET_WIDTH, Constants.BULLET_HEIGHT); // Call parent constructor
        this.isPlayerBullet = isPlayerBullet;

        // Set velocity based on who shot it 
        if (isPlayerBullet) {
            this.velocityY = Constants.PLAYER_BULLET_SPEED; // Player bullet speed (UP)
        } else {
            this.velocityY = Constants.ALIEN_BULLET_SPEED; // Player bullet speed (DOWN)
        }
    }

    @Override
    public void update() {
        // Move bullet
        y += velocityY;

        // Desactivate if off screen (if is top or bottom)
        if (y < -height || y > Constants.WINDOW_HEIGHT) {
            destroy();
        }
    }

    @Override
    public void render(Graphics2D g2d) {
        // Player bullets = green, Alien bullets = red
        if (isPlayerBullet) {
            g2d.setColor(COLOR_PLAYER_BULLET); // Green
        } else {
            g2d.setColor(COLOR_ALIEN_BULLET); // Red
        }

        g2d.fillRect((int) x, (int) y, width, height);
    }

    /**
     * Check if this is a player bullet
     */
    public boolean isPlayerBullet() { return isPlayerBullet; }
}