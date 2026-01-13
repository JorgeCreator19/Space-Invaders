package com.spaceinvaders.entities;

import com.spaceinvaders.utils.Constants;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

/**
 * Mystery UFO ship - bonus enemy that flies across the top
 * This enemy appears randomly and awards bonus points
 */
public class MysteryShip extends GameObject {
    
    // Possible points values
    private static final int[] POINT_VALUES = {100, 150, 200, 300};

    // Points for this ship
    private int points;

    // Movement direction (1 = right, -1 = left)
    private int direction;

    // Draw size for drawing
    private static final int PIXEL = 3;

    // Ship color - Red for more dangerous impresive
    private static final Color COLOR_BODY = new Color(255, 0, 0);
    private static final Color COLOR_DOME = new Color(255, 100, 100);

    // Spawn settings
    public static final double SPAWN_CHANCE = Constants.MYSTERY_SHIP_SPAWN_CHANCE; // 0.1% chance per frame
    public static final int SPEED = Constants.MYSTERY_SHIP_SPEED;

    // Random for points values
    private static Random random = new Random();

    /**
     * Constructor
     * @param fromLeft true  = spawn on left, move right | false = spawn on right, move left
     */
    public MysteryShip(boolean fromLeft) {
        super(
            fromLeft ? -60 : Constants.WINDOW_WIDTH, // Start off-screen
            40, // Y position - above aliens
            Constants.MYSTERY_SHIP_WIDTH, // Width 
            Constants.MYSTERY_SHIP_HEIGHT  // Height
        );

        // Set velocity and direction
        if (fromLeft) {
            this.direction = 1;
            this.velocityX = SPEED;
        } else {
            this.direction = -1;
            this.velocityX = -SPEED;
        }

        // Random point value 
        this.points = POINT_VALUES[random.nextInt(POINT_VALUES.length)];
    }

    @Override
    public void update() {
        // Move horizontally
        x += velocityX;

        // Desactive when off screen
        if (direction == 1 && x > Constants.WINDOW_WIDTH) {
            active = false;
        }
        if (direction == -1 && x < -width) {
            active = false;
        }
    }

    @Override
    public void render(Graphics2D g2d) {
        int px = (int) x;
        int py = (int) y;

        // Draw UFO body in red
        g2d.setColor(COLOR_BODY);

        /* Main body - wide ellipse shape */
        // Row 0: Top dome
        for (int i = 6; i <= 13; i++) {
            drawPixel(g2d, px, i, py, 0);
        }

        // Row 1: Upper body
        for (int i = 4; i <= 15; i++) {
            drawPixel(g2d, px, i, py, 1);
        }

        // Row 2: Main body - widest
        g2d.setColor(COLOR_DOME);
        for (int i = 2; i <= 17; i++) {
            drawPixel(g2d, px, i, py, 2);
        }

        // Row 3: Bottom with lights
        g2d.setColor(COLOR_BODY);
        for (int i = 4; i <= 15; i++) {
            drawPixel(g2d, px, i, py, 3);
        }

        // Row 4: Lights underneath (blinking effect)
        g2d.setColor(Color.YELLOW);
        drawPixel(g2d, px, 5, py, 4);
        drawPixel(g2d, px, 8, py, 4);
        drawPixel(g2d, px, 11, py, 4);
        drawPixel(g2d, px, 14, py, 4);

        // Draw dome highlight
        g2d.setColor(new Color(255, 200, 200));
        drawPixel(g2d, px, 8, py, 0);
        drawPixel(g2d, px, 9, py, 0);
    }

    /**
     * Helper to draw a pixel
     */
    private void drawPixel(Graphics2D g2d, int baseX, int offsetX, int baseY, int offsetY) {
        g2d.fillRect(baseX + offsetX * PIXEL, baseY + offsetY * PIXEL, PIXEL, PIXEL);
    }

    /**
     * Get points value
     */
    public int getPoints() {
        return points;
    }
    
    /**
     * Static method to check if UFO should spawn this frame
     */
    public static boolean shouldSpawn() {
        return random.nextDouble() < SPAWN_CHANCE;
    }

    /**
     * Static method to create a new UFO (random direction)
     */
    public static MysteryShip spawn() {
        boolean fromLeft = random.nextBoolean();
        return new MysteryShip(fromLeft);
    }
}