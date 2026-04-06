package com.spaceinvaders.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Explosion animation - plays when aliens or player are destroyed
 * Classic Space Invaders style explosion - uses delta time
 */
public class Explosion extends GameObject{
    
    // Animation settings
    private int currentFrame;
    private double frameTimer;
    private static final double  FRAME_DURATION = 0.08; // How long each frame show
    private static final int TOTAL_FRAMES = 3;  // Total animation frames

    // Pixel size
    private static final int PIXEL = 3;

    // Explosion color
    private Color color;

    // Cached sprites for animation frames
    private static BufferedImage[] sprites;
    private static boolean spritesCreated = false;

    /**
     * 
     * @param x center X position
     * @param y center Y position
     * @param color explosion color (green for player, alien's color for aliens)
     */
    public Explosion(double x, double y, Color color) {
        super(x - 20, y - 20, 40, 40); // Center the explosion

        this.color = color;
        this.currentFrame = 0;
        this.frameTimer = 0;

        // Create sprites only once
        if (!spritesCreated) {
            createSprites();
            spritesCreated = true;
        }
    }

    /**
     * Create all explosion animation frames
     */
    private static void createSprites() {
        sprites = new BufferedImage[TOTAL_FRAMES];

        // We'll create the sprites with white, then tint when drawing
        for (int i = 0; i < TOTAL_FRAMES; i++) {
            sprites[i] = createExplosionFrame(i);
        }
    }

    /**
     * Create a single explosion frame
     */
    private static BufferedImage createExplosionFrame(int frame) {
        BufferedImage sprite = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = sprite.createGraphics();

        g.setColor(Color.WHITE);  // Will be tinted when drawing

        int centerX = 20;
        int centerY = 20;

        switch (frame) {
            // Frame 0: Small explosion - center burst
            case 0 -> {
                drawExplosionPixel(g, centerX, centerY);
                drawExplosionPixel(g, centerX - 4, centerY);
                drawExplosionPixel(g, centerX + 4, centerY);
                drawExplosionPixel(g, centerX, centerY - 4);
                drawExplosionPixel(g, centerX, centerY + 4);
                drawExplosionPixel(g, centerX - 4, centerY - 4);
                drawExplosionPixel(g, centerX + 4, centerY - 4);
                drawExplosionPixel(g, centerX - 4, centerY + 4);
                drawExplosionPixel(g, centerX + 4, centerY + 4);
            }
            // Frame 1: Medium explosion - expanding
            case 1 -> {
                // Center
                drawExplosionPixel(g, centerX, centerY);
                // Inner ring
                drawExplosionPixel(g, centerX - 6, centerY);
                drawExplosionPixel(g, centerX + 6, centerY);
                drawExplosionPixel(g, centerX, centerY - 6);
                drawExplosionPixel(g, centerX, centerY + 6);
                // Diagonals
                drawExplosionPixel(g, centerX - 6, centerY - 6);
                drawExplosionPixel(g, centerX + 6, centerY - 6);
                drawExplosionPixel(g, centerX - 6, centerY + 6);
                drawExplosionPixel(g, centerX + 6, centerY + 6);
                // Outer points
                drawExplosionPixel(g, centerX - 10, centerY);
                drawExplosionPixel(g, centerX + 10, centerY);
                drawExplosionPixel(g, centerX, centerY - 10);
                drawExplosionPixel(g, centerX, centerY + 10);
            }
            // Frame 2: Large explosion - scattered particles
            case 2 -> {
                // Scattered particles
                drawExplosionPixel(g, centerX - 12, centerY - 12);
                drawExplosionPixel(g, centerX + 12, centerY - 12);
                drawExplosionPixel(g, centerX - 12, centerY + 12);
                drawExplosionPixel(g, centerX + 12, centerY + 12);
                drawExplosionPixel(g, centerX, centerY - 14);
                drawExplosionPixel(g, centerX, centerY + 14);
                drawExplosionPixel(g, centerX - 14, centerY);
                drawExplosionPixel(g, centerX + 14, centerY);
                // Some inner particles
                drawExplosionPixel(g, centerX - 6, centerY - 4);
                drawExplosionPixel(g, centerX + 6, centerY + 4);
                drawExplosionPixel(g, centerX + 4, centerY - 6);
                drawExplosionPixel(g, centerX - 4, centerY + 6);
            }
        }
        g.dispose();
        return sprite;
    }

    /**
     * Helper to draw explosion pixel
     */
    private static void drawExplosionPixel(Graphics2D g, int x, int y) {
        g.fillRect(x - PIXEL/2, y - PIXEL/2, PIXEL, PIXEL);
    }

    @Override
    public void update(double deltaTime) {
        frameTimer += deltaTime;

        // Advance animation frame
        if (frameTimer >= FRAME_DURATION) {
            frameTimer = 0;
            currentFrame++;

            // Animation finished
            if (currentFrame >= TOTAL_FRAMES) {
                active = false;
            }
        }
    }

    @Override
    public void render(Graphics2D g2d) {
        if (currentFrame < TOTAL_FRAMES) {
            // Draw the sprite with color tint
            g2d.drawImage(sprites[currentFrame], (int) x, (int) y, null);

            // Draw colored overlay
            g2d.setColor(color);
            drawColoredExplosion(g2d, currentFrame);
        }
    }

    /**
     * Draw explosion with the specific color
     */
    private void drawColoredExplosion(Graphics2D g2d, int frame) {
        int centerX = (int) x + 20;
        int centerY = (int) y + 20;
        
        switch (frame) {
            case 0 -> {
                drawPixel(g2d, centerX, centerY);
                drawPixel(g2d, centerX - 4, centerY);
                drawPixel(g2d, centerX + 4, centerY);
                drawPixel(g2d, centerX, centerY - 4);
                drawPixel(g2d, centerX, centerY + 4);
                drawPixel(g2d, centerX - 4, centerY - 4);
                drawPixel(g2d, centerX + 4, centerY - 4);
                drawPixel(g2d, centerX - 4, centerY + 4);
                drawPixel(g2d, centerX + 4, centerY + 4);
            }
                
            case 1 -> {
                drawPixel(g2d, centerX, centerY);
                drawPixel(g2d, centerX - 6, centerY);
                drawPixel(g2d, centerX + 6, centerY);
                drawPixel(g2d, centerX, centerY - 6);
                drawPixel(g2d, centerX, centerY + 6);
                drawPixel(g2d, centerX - 6, centerY - 6);
                drawPixel(g2d, centerX + 6, centerY - 6);
                drawPixel(g2d, centerX - 6, centerY + 6);
                drawPixel(g2d, centerX + 6, centerY + 6);
                drawPixel(g2d, centerX - 10, centerY);
                drawPixel(g2d, centerX + 10, centerY);
                drawPixel(g2d, centerX, centerY - 10);
                drawPixel(g2d, centerX, centerY + 10);
            }
                
            case 2 -> {
                drawPixel(g2d, centerX - 12, centerY - 12);
                drawPixel(g2d, centerX + 12, centerY - 12);
                drawPixel(g2d, centerX - 12, centerY + 12);
                drawPixel(g2d, centerX + 12, centerY + 12);
                drawPixel(g2d, centerX, centerY - 14);
                drawPixel(g2d, centerX, centerY + 14);
                drawPixel(g2d, centerX - 14, centerY);
                drawPixel(g2d, centerX + 14, centerY);
                drawPixel(g2d, centerX - 6, centerY - 4);
                drawPixel(g2d, centerX + 6, centerY + 4);
                drawPixel(g2d, centerX + 4, centerY - 6);
                drawPixel(g2d, centerX - 4, centerY + 6);
            }
        }
    }
    
    /**
     * Helper to draw pixel at position
     */
    private void drawPixel(Graphics2D g2d, int x, int y) {
        g2d.fillRect(x - PIXEL/2, y - PIXEL/2, PIXEL, PIXEL);
    }
}