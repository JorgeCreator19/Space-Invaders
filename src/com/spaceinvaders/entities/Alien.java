package com.spaceinvaders.entities;

import com.spaceinvaders.utils.Constants;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Single alien enemy with pixel art rendering
 */
public class Alien extends GameObject {
    
    // Which row is this alien in?
    private int row;

    // Points awarded when killed
    private int points;

    // Pixel size for drawing in each "pixel"
    private static final int PIXEL = 4;

    // Color for differents alines types
    private static final Color COLOR_TOP = new Color(255, 0 , 255); // Purple (50 points)
    private static final Color COLOR_MIDDLE = new Color(0, 255, 255); // Cyan (30 points)
    private static final Color COLOR_BOTTOM = new Color(0, 255, 0); // Green (10 points)

    /**
     * @param x starting X position
     * @param y starting Y position
     * @param row which row (0 = top, 4 = bottom)
     */
    public Alien(double x, double y, int row) {
        super(x, y, Constants.ALIEN_WIDTH, Constants.ALIEN_HEIGHT);
        this.row = row;

        // Set points based on row
        if (row == 0) {
            this.points = Constants.SCORE_ROW_3; // Top row = 50 points
        } else if (row <= 2) {
            this.points = Constants.SCORE_ROW_2; // Middle row = 30 points;
        } else {
            this.points = Constants.SCORE_ROW_1; // Bottom row = 10;
        }
    }

    @Override
    public void update() {
        // Move based on velocity (set by AlienFormation)
        x += velocityX;
        y += velocityY;

        // Reset Y velocity after dropping
        velocityY = 0;
    }

    @Override
    public void render(Graphics2D g2d) {
        // Choose which alien type to draw based on row
        if (row == 0) {
            drawTopAlien(g2d);  // Octopus Alien - Purple color
        } else if (row <= 2) {
            drawMiddleAlien(g2d);   // Grab Alien - Cyan color
        } else {
            drawBottomAlien(g2d);   // Squid Alien - Green color
        }
    }

    /**
     * Draw TOP row alien (Octopus)
     */
    private void drawTopAlien(Graphics2D g2d) {
        g2d.setColor(COLOR_TOP);
        int px = (int) x;
        int py = (int) y;

        // Row 0
        drawPixel(g2d, px + 4, py);
        drawPixel(g2d, px + 5, py);
        drawPixel(g2d, px + 6, py);
        drawPixel(g2d, px + 7, py);

        // Row 1
        for (int i = 2; i <= 9; i++){
            drawPixel(g2d, px + i, py + 1);
        }

        // Row 2
        drawPixel(g2d, px + 2, py + 2);
        drawPixel(g2d, px + 3, py + 2);
        drawPixel(g2d, px + 5, py + 2);
        drawPixel(g2d, px + 6, py + 2);
        drawPixel(g2d, px + 8, py + 2);
        drawPixel(g2d, px + 9, py + 2);

        // Row 3
        for (int i = 2; i <= 9; i++) {
            drawPixel(g2d, px + i, py + 3);
        }

        // Row 4
        drawPixel(g2d, px + 3, py + 4);
        drawPixel(g2d, px + 8, py + 4);

        // Row 5
        drawPixel(g2d, px + 2, py + 5);
        drawPixel(g2d, px + 9, py + 5);
    }

    /**
     * Draw middle row alien (Crab)
     */
    private void drawMiddleAlien(Graphics2D g2d) {
        g2d.setColor(COLOR_MIDDLE);
        int px = (int) x;
        int py = (int) y;

        // Row 0
        drawPixel(g2d, px + 2, py);
        drawPixel(g2d, px + 9, py);

        // Row 1
        for (int i = 3; i <= 8; i++) {
            drawPixel(g2d, px + i, py + 1);
        }

        // Row 2
        for (int i = 2; i <= 9; i++) {
            drawPixel(g2d, px + i, py + 2);
        }

        // Row 3 - eyes are gaps
        drawPixel(g2d, px + 2, py + 3);
        drawPixel(g2d, px + 4, py + 3);
        drawPixel(g2d, px + 5, py + 3);
        drawPixel(g2d, px + 6, py + 3);
        drawPixel(g2d, px + 7, py + 3);
        drawPixel(g2d, px + 9, py + 3);

        // Row 4
        for (int i = 2; i <= 9; i++) {
            drawPixel(g2d, px + i, py + 4);
        }

        // Row 5
        drawPixel(g2d, px + 3, py + 5);
        drawPixel(g2d, px + 8, py + 5);
    }

    /**
     * Draw bottom row alien (Squid)
     */
    private void drawBottomAlien(Graphics2D g2d) {
        g2d.setColor(COLOR_BOTTOM);
        int px = (int) x;
        int py = (int) y;

        // Row 0
        drawPixel(g2d, px + 4, py);
        drawPixel(g2d, px + 5, py);
        drawPixel(g2d, px + 6, py);
        drawPixel(g2d, px + 7, py);

        // Row 1
        for (int i = 2; i <= 9; i++) {
            drawPixel(g2d, px + i, py + 1);
        }

        // Row 2
        for (int i = 2; i <= 9; i++) {
            drawPixel(g2d, px + i, py + 2);
        }

        // Row 3 - eyes are gaps
        drawPixel(g2d, px + 3, py + 3);
        drawPixel(g2d, px + 5, py + 3);
        drawPixel(g2d, px + 6, py + 3);
        drawPixel(g2d, px + 8, py + 3);

        // Row 4
        drawPixel(g2d, px + 2, py + 4);
        drawPixel(g2d, px + 9, py + 4);

        // Row 5
        drawPixel(g2d, px + 3, py + 5);
        drawPixel(g2d, px + 8, py + 5);
    }

    /**
     * Helper method to draw a single "Pixel" (actually this is a small square)
     */
    private void drawPixel(Graphics2D g2d, int gridX, int gridY) {
        g2d.fillRect(gridX * PIXEL, gridY * PIXEL, PIXEL, PIXEL);
    }

    /* GETTERS */
    public int getPoints() {
        return points;
    }

    public int getRow() {
        return row;
    }

    /**
     * Get X position for spawning bullet (center of alien)
     */
    public double getBulletSpawnX() {
        return x + width / 2 - Constants.BULLET_WIDTH / 2;
    }

    /** 
     * Get Y position for spawning bullet (bottom of alien)
     */
    public double getBulletSpawnY() {
        return y + height;
    }
}