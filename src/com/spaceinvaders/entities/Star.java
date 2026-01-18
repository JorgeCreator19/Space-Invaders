package com.spaceinvaders.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import com.spaceinvaders.utils.Constants;

/**
 * Animated star for menu background
 */
public class Star {
    private double x;
    private double y;
    private double speed;
    private int size;
    private int brightness;

    private static Random random = new Random();

    /**
     * Constructor - Creates a star at random position
     */
    public Star() {
        reset(true);
    }

    /**
     * Reset star to new random position
     * @param randomY true = random Y, false = start at top
     */
    public void reset(boolean randomY) {
        x = random.nextInt(Constants.WINDOW_WIDTH);
        y = randomY ? random.nextInt(Constants.WINDOW_HEIGHT) : -5;
        speed = 0.5 + random.nextDouble() * 2; // 0.5 to 2.5
        size = random.nextInt(3) + 1; // 1 to 3 pixels
        brightness = 100 + random.nextInt(155); // 100 to 255
    }

    /**
     * Update star position (move down slowly)
     */
    public void update() {
        y += speed;

        // Reset when off screen
        if (y > Constants.WINDOW_HEIGHT) {
            reset(false);
        }
    }

    /**
     * Draw the star
     */
    public void render(Graphics2D g2d) {
        g2d.setColor(new Color(brightness, brightness, brightness));
        g2d.fillRect((int) x, (int) y, size, size);
    }
}