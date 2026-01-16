package com.spaceinvaders.entities;

import com.spaceinvaders.utils.Constants;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Bullet class - used for both player and alien bullets
 */
public class Bullet extends GameObject {

    // Is this bullet from player or alien?
    private boolean isPlayerBullet;

    /**
     * 
     * @param x starting X position
     * @param y starting Y position
     * @param isPlayerBullet true = player's bullet (goes UP), false = alien's (goes DOWN)
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
        // Player bullets = yellow, Alien bullets = red
        if (isPlayerBullet) {
            g2d.setColor(new Color(255, 255, 0)); // Yellow
        } else {
            g2d.setColor(new Color(255, 0, 0)); // Red
        }

        g2d.fillRect((int) x, (int) y, width, height);
    }

    /**
     * Check if this is a player bullet
     */
    public boolean isPlayerBullet() { return isPlayerBullet; }
}