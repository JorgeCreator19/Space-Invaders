package com.spaceinvaders.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.spaceinvaders.utils.Constants;

/**
 * Destructible shield/bunker
 * Made up of small blocks that can be destroyed individually
 */
public class Shield {
    
    // Position
    private int x;
    private int y;

    // Shield blocks (true = exists, false = destroyed)
    private boolean[][] blocks;

    // Block size
    private static final int BLOCK_SIZE = 4;

    // Shield dimensions in blocks
    private static final int WIDTH_BLOCKS = 22;
    private static final int HEIGHT_BLOCKS = 16;

    // Shield color
    private static final Color SHIELD_COLOR = new Color(0, 255, 0);

    // Shield shape template (1 = block exists, 0 = empty)
    private static final int[][] SHIELD_TEMPLATE = {
        {0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0},
        {0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0},
        {0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0},
        {0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0},
        {0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0},
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
        {1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1},
        {1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1},
        {1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1},
        {1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1},
    };

    /**
     * Constructor
     * @param x Left position of shield
     * @param y Top position of shield
     */
    public Shield(int x, int y) {
        this.x = x;
        this.y = y;

        // Initialize blocks from template
        blocks = new boolean[HEIGHT_BLOCKS][WIDTH_BLOCKS];
        for (int row = 0; row < HEIGHT_BLOCKS; row++) {
            for (int col = 0; col < WIDTH_BLOCKS; col++) {
                blocks[row][col] = (SHIELD_TEMPLATE[row][col] == 1);
            }
        }
    }

    /**
     * Render the shield
     */
    public void render(Graphics2D g2d) {
        g2d.setColor(SHIELD_COLOR);

        for (int row = 0; row < HEIGHT_BLOCKS; row++) {
            for (int col = 0; col < WIDTH_BLOCKS; col++) {
                if (blocks[row][col]) {
                    int blockX = x + col * BLOCK_SIZE;
                    int blockY = y + row * BLOCK_SIZE;
                    g2d.fillRect(blockX, blockY, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }
    }

    /**
     * Check collision with a bullet and destroy blocks if hit
     * @param bullet The bullet to check
     * @return true if bullet hit the shield
     */
    public boolean checkBulletCollision(Bullet bullet) {
        if (!bullet.isActive()) return false;

        Rectangle bulletBounds = bullet.getBounds();

        // Check each block
        for (int row = 0; row < HEIGHT_BLOCKS; row++) {
            for (int col = 0; col < WIDTH_BLOCKS; col++) {
                if (blocks[row][col]) {
                    int blockX = x + col * BLOCK_SIZE;
                    int blockY = y + row * BLOCK_SIZE;
                    Rectangle blockBounds = new Rectangle(blockX, blockY, BLOCK_SIZE, BLOCK_SIZE);

                    if (bulletBounds.intersects(blockBounds)) {
                        // Destroy this block and nearby blocks (explosion effect)
                        destroyBlocksAt(row, col, bullet.isPlayerBullet());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Destroy blocks at position (creates explosion damage)
     * @param row Center row
     * @param col Center column
     * @param fromBelow true if bullet came from below (player), false if from above (alien)
     */
    private void destroyBlocksAt(int row, int col, boolean fromBelow) {
        // Destroy center block
        destroyBlock(row, col);

        // Destroy surrounding blocks (random pattern for natural look)
        if (fromBelow) {
            // Player bullet - spreads upward
            destroyBlock(row - 1, col);
            destroyBlock(row - 1, col - 1);
            destroyBlock(row - 1, col + 1);
            destroyBlock(row, col - 1);
            destroyBlock(row, col + 1);
            destroyBlock(row - 2, col);
        } else {
            // Alien bullet - damage spreads downward
            destroyBlock(row + 1, col);
            destroyBlock(row + 1, col - 1);
            destroyBlock(row + 1, col + 1);
            destroyBlock(row, col - 1);
            destroyBlock(row, col + 1);
            destroyBlock(row + 2, col);
        }
    }

    /**
     * Destroy a single block (with bounds checking)
     */
    private void destroyBlock(int row, int col) {
        if (row >= 0 && row < HEIGHT_BLOCKS && col >= 0 && col < WIDTH_BLOCKS) {
            blocks[row][col] = false;
        }
    }
    /**
     * Check collision with an alien
     * @param alien The alien to check
     * @return true if alien hit the shield
     */
    public boolean checkAlienCollision(Alien alien) {
        if (!alien.isActive()) return false;

        Rectangle alienBounds = alien.getBounds();
        for (int row = 0; row < HEIGHT_BLOCKS; row++) {
            for (int col = 0; col < WIDTH_BLOCKS; col++) {
                if (blocks[row][col]) {
                    int blockX = x + col * BLOCK_SIZE;
                    int blockY = y + row * BLOCK_SIZE;
                    Rectangle blockBounds = new Rectangle(blockX, blockY, BLOCK_SIZE, BLOCK_SIZE);

                    if (alienBounds.intersects(blockBounds)) {
                        // Destroy blocks where alien touches
                        destroyBlocksInArea(alienBounds);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Destroy all blocks in an area (when alien crashes through)
     */
    private void destroyBlocksInArea(Rectangle area) {
        for (int row = 0; row < HEIGHT_BLOCKS; row++) {
            for (int col = 0; col < WIDTH_BLOCKS; col++) {
                int blockX = x + col * BLOCK_SIZE;
                int blockY = y + row * BLOCK_SIZE;
                Rectangle blockBounds = new Rectangle(blockX, blockY, BLOCK_SIZE, BLOCK_SIZE);
                
                if (area.intersects(blockBounds)) {
                    blocks[row][col] = false;
                }
            }
        }
    }

    /**
     * Reset shield to original state
     */
    public void reset() {
        for (int row = 0; row < HEIGHT_BLOCKS; row++) {
            for (int col = 0; col < WIDTH_BLOCKS; col++) {
                blocks[row][col] = (SHIELD_TEMPLATE[row][col] == 1);
            }
        }
    }

    /**
     * Check if shield has any blocks remaining
     */
    public boolean hasBlocksRemaining() {
        for (int row = 0; row < HEIGHT_BLOCKS; row++) {
            for (int col = 0; col < WIDTH_BLOCKS; col++) {
                if (blocks[row][col]) return true;
            }
        }
        return false;
    }

    /**
     * Get shield width in pixels
     */
    public int getWidth() { return WIDTH_BLOCKS * BLOCK_SIZE; }
    
    /**
     * Get shield height in pixels
     */
    public int getHeight() { return HEIGHT_BLOCKS * BLOCK_SIZE; }
    
    /**
     * Get X position
     */
    public int getX() { return x; }
    
    /**
     * Get Y position
     */
    public int getY() { return y; }
}