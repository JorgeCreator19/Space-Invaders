package com.spaceinvaders.entities;

import com.spaceinvaders.utils.Constants;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Single alien enemy with OPTIMIZED pixel art rendering
 * Uses cached sprites for better performance
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

    /* CACHED SPRITES (static = shared by all aliens) */
    private static BufferedImage spriteTop;
    private static BufferedImage spriteMiddle;
    private static BufferedImage spriteBottom;
    private static boolean spritesCreated = false;

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

        // Create sprites only once (first alien created)
        if (!spritesCreated) {
            createSprites();
            spritesCreated = true;
        }
    }

    /**
     * Create cached sprite images for all alien types
     * Called only ONCE when first alien is created
     */
    private static void createSprites() {
        spriteTop = createAlienSprite(COLOR_TOP, 0);
        spriteMiddle = createAlienSprite(COLOR_MIDDLE, 1);
        spriteBottom = createAlienSprite(COLOR_BOTTOM, 2);
    }
    
    /**
     * Create a single alien sprite
     * @param color The alien color
     * @param type type 0 = top, 1 = middle, 2 = bottom
     * @return BufferedImage of the alien sprite
     */
    private static BufferedImage createAlienSprite(Color color, int type) {
        // Create transparent image
        BufferedImage sprite = new BufferedImage(
            Constants.ALIEN_WIDTH, 
            Constants.ALIEN_HEIGHT, 
            BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g = sprite.createGraphics();
        g.setColor(color);

        // Draw based on type
        switch (type) {
            case 0:
                drawTopAlienPixels(g);
                break;
            case 1:
                drawMiddleAlienPixels(g);
                break;
            case 2:
                drawBottomAlienPixels(g);
                break;
        }

        g.dispose();
        return sprite;
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
        // Simply draw the cached sprite
        BufferedImage sprite;

        if (row == 0) {
            sprite = spriteTop;  // Octopus Alien - Purple color
        } else if (row <= 2) {
            sprite = spriteMiddle;  // Grab Alien - Cyan color
        } else {
            sprite = spriteBottom;   // Squid Alien - Green color
        }

        g2d.drawImage(sprite, (int) x, (int) y, null);
    }

    /**
     * Draw top alien pixels to graphics context (Octopus) - Purple
     */
    private static void drawTopAlienPixels(Graphics2D g) {
        // Row 0:     ████
        drawPixelAt(g, 4, 0);
        drawPixelAt(g, 5, 0);
        drawPixelAt(g, 6, 0);
        drawPixelAt(g, 7, 0);

        // Row 1:   ████████
        for (int i = 2; i <= 9; i++) {
            drawPixelAt(g, i, 1);
        }

        // Row 2:   ██ ██ ██
        drawPixelAt(g, 2, 2);
        drawPixelAt(g, 3, 2);
        drawPixelAt(g, 5, 2);
        drawPixelAt(g, 6, 2);
        drawPixelAt(g, 8, 2);
        drawPixelAt(g, 9, 2);

        // Row 3:   ████████
        for (int i = 2; i <= 9; i++) {
            drawPixelAt(g, i, 3);
        }

        // Row 4:    █    █
        drawPixelAt(g, 3, 4);
        drawPixelAt(g, 8, 4);

        // Row 5:   █      █
        drawPixelAt(g, 2, 5);
        drawPixelAt(g, 9, 5);
    }

    /**
     * Draw middle alien pixels to graphics context (Crab) - Cyan
     */
    private static void drawMiddleAlienPixels(Graphics2D g) {
        // Row 0:   █      █
        drawPixelAt(g, 2, 0);
        drawPixelAt(g, 9, 0);

        // Row 1:    ██████
        for (int i = 3; i <= 8; i++) {
            drawPixelAt(g, i, 1);
        }

        // Row 2:   ████████
        for (int i = 2; i <= 9; i++) {
            drawPixelAt(g, i, 2);
        }

        // Row 3:   █ ████ █
        drawPixelAt(g, 2, 3);
        drawPixelAt(g, 4, 3);
        drawPixelAt(g, 5, 3);
        drawPixelAt(g, 6, 3);
        drawPixelAt(g, 7, 3);
        drawPixelAt(g, 9, 3);

        // Row 4:   ████████
        for (int i = 2; i <= 9; i++) {
            drawPixelAt(g, i, 4);
        }

        // Row 5:    █    █
        drawPixelAt(g, 3, 5);
        drawPixelAt(g, 8, 5);
    }

    /**
     * Draw bottom alien pixels to graphics context (Squid) - Green
     */
    private static void drawBottomAlienPixels(Graphics2D g) {
        // Row 0:     ████
        drawPixelAt(g, 4, 0);
        drawPixelAt(g, 5, 0);
        drawPixelAt(g, 6, 0);
        drawPixelAt(g, 7, 0);

        // Row 1:   ████████
        for (int i = 2; i <= 9; i++) {
            drawPixelAt(g, i, 1);
        }

        // Row 2:   ████████
        for (int i = 2; i <= 9; i++) {
            drawPixelAt(g, i, 2);
        }

        // Row 3:    █ ██ █
        drawPixelAt(g, 3, 3);
        drawPixelAt(g, 5, 3);
        drawPixelAt(g, 6, 3);
        drawPixelAt(g, 8, 3);

        // Row 4:   █      █
        drawPixelAt(g, 2, 4);
        drawPixelAt(g, 9, 4);

        // Row 5:    █    █
        drawPixelAt(g, 3, 5);
        drawPixelAt(g, 8, 5);
    }

    /**
     * Helper to draw a single pixel at grid position
     */
    private static void drawPixelAt(Graphics2D g, int gridX, int gridY) {
        g.fillRect(gridX * PIXEL, gridY * PIXEL, PIXEL, PIXEL);
    }

    /* GETTERS */
    public int getPoints() { return points; }

    public int getRow() { return row; }

    /**
     * Get X position for spawning bullet (center of alien)
     */
    public double getBulletSpawnX() { return x + width / 2 - Constants.BULLET_WIDTH / 2; }

    /** 
     * Get Y position for spawning bullet (bottom of alien)
     */
    public double getBulletSpawnY() {  return y + height; }
}