package com.spaceinvaders.entities;

import com.spaceinvaders.utils.Constants;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Manage the entire alien formation
 * Handles movement pattern: right -> down -> left -> down -> repeat
 * Also uses delta time for speed
 */
public class AlienFormation {
    
    // All aliens in formation
    private List<Alien> aliens;

    // Movement direction (right = 1, left = -1)
    private int direction;

    // Current speed (this increase as aliens die)
    private double speed;

    // Random for alien shooting
    private Random random;

    // Current shoot chance (increases each wave)
    private double shootChance;

    /**
     * Constructor - creates the alien formation 
     */
    public AlienFormation() {
        aliens = new ArrayList<>();
        direction = 1;
        speed = Constants.ALIEN_SPEED_PER_SEC;
        random = new Random();
        shootChance = Constants.ALIEN_SHOOT_CHANCE_BASE;

        createFormation();
    }

    /**
     * Creates the grid of alies
     * 11 columns * 5 rows = 55 aliens
     */
    public void createFormation() {
        aliens.clear();

        for (int row = 0; row < Constants.ALIEN_ROWS; row++) {
            for (int col = 0; col < Constants.ALIEN_COLUMNS; col++) {
                // Calculate position for this alien
                int x = Constants.ALIEN_START_X + col * (Constants.ALIEN_WIDTH + Constants.ALIEN_SPACING_X);
                int y = Constants.ALIEN_START_Y + row * (Constants.ALIEN_HEIGHT + Constants.ALIEN_SPACING_Y);

                // Create alien and add to list
                Alien alien = new Alien(x, y, row);
                aliens.add(alien);
            }
        }
    }

    /**
     * Update all aliens - movement and direction changes
     */
    public void update(double deltaTime) {
        // Check if any alien hit the edge
        boolean shouldReverse = false;
        boolean shouldDrop = false;
        
        for (Alien alien : aliens) {
            if (!alien.isActive()) continue;

            // Check right edge
            if (direction == 1 && alien.getX() + alien.getWidth() >= Constants.WINDOW_WIDTH - 10) {
                shouldReverse = true;
                shouldDrop = true;
                break;
            }

            // Check left edge
            if (direction == -1 && alien.getX() <= 10) {
                shouldReverse = true;
                shouldDrop = true;
                break;
            }
        }

        // Reverse direction if needed
        if (shouldReverse) {
            direction *= -1; // Flip direction
        }

        // Update each alien
        for (Alien alien : aliens) {
            if (!alien.isActive()) continue;

            // Set horizontal velocity
            alien.setVelocityX(speed * direction);

            // Drop down if reversing
            if (shouldDrop) {
                alien.setDrop(Constants.ALIEN_DROP_DISTANCE);
            }
            alien.update(deltaTime);
        }
    }

    /**
     * Render all aliens 
     */
    public void render(Graphics2D g2d) {
        for (Alien alien : aliens) {
            if (alien.isActive()) {
                alien.render(g2d);
            }
        }
    }

    /**
     * Try to shoot - returns a bullet from random alien, or null
     * No ArrayList creation every frame for better optimitzation
     */
    public Bullet tryShoot(double deltaTime) {
        // Adjust shoot chance based on delta time
        // This ensures consistent shooting rate regardless of FPS
        double adjustedChance = shootChance * deltaTime * 60; // Normalize to 60 FPS

        if (random.nextDouble() >= adjustedChance) {
            return null;
        }

        // Count active aliens and pick a random index  
        int alienCount = getAliveCount();
        if (alienCount == 0) {
            return null;
        }

        // Pick random alien number
        int targetIndex = random.nextInt(alienCount);
        int currentIndex = 0;

        // Find the alien at that index
        for (Alien alien : aliens) {
            if (alien.isActive()) {
                if (currentIndex == targetIndex) {
                    return new Bullet(
                        alien.getBulletSpawnX(),
                        alien.getBulletSpawnY(),
                        false
                    );
                }
                currentIndex++;
            }
        }

        return null;
    }

    /**
     * Increase difficulty for next wave
     * Called when starting a new wave
     */
    public void nextWaveDifficulty() {
        // Increase shoot chance, but don't exceed maximum
        shootChance += Constants.ALIEN_SHOOT_CHANCE_INCREMENT;
    
        if (shootChance > Constants.ALIEN_SHOOT_CHANCE_MAX) {
        shootChance = Constants.ALIEN_SHOOT_CHANCE_MAX;
        }
    }

    /**
     * Check if aliens reached the bottom (game over condition)
     */
    public boolean hasReachedBottom() {
        for (Alien alien : aliens) {
            if  (alien.isActive()) {
                // Check if alien Y + height is past the player area
                if(alien.getY() + alien.getHeight() >= Constants.WINDOW_HEIGHT - Constants.PLAYER_Y_OFFSET - 20) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Counts how many aliens are still alive
     */
    public int getAliveCount() {
        int count = 0;
        for (Alien alien : aliens) {
            if (alien.isActive()) {
                count++;
            }
        }
        return count;
    }

    /**
     * Increase speed - called when an alien dies
     */
    public void increaseSpeed() {
        int totalAliens = Constants.ALIEN_ROWS * Constants.ALIEN_COLUMNS;  // 55
        int aliveCount = getAliveCount();

        // Calculate speed based on percentage of aliens remaining
        // More aliens dead = faster speed, but with a cap
        if (aliveCount > 40) {
            speed = Constants.ALIEN_SPEED_PER_SEC;
        } else if (aliveCount > 25) {
            speed = Constants.ALIEN_SPEED_PER_SEC * 1.3;
        } else if (aliveCount > 10) {
            speed = Constants.ALIEN_SPEED_PER_SEC * 1.7;
        } else if (aliveCount > 5) {
            speed = Constants.ALIEN_SPEED_PER_SEC * 2.4;
        } else {
            speed = Constants.ALIEN_SPEED_PER_SEC * 3.0;
        }
    }   

    /**
     * Reset formation for new wave or game
     */
    public void reset() {
        direction = 1;
        speed = Constants.ALIEN_SPEED_PER_SEC;
        createFormation();
    }

    /**
     * Full reset for new game (resets difficulty too)
     */
    public void fullReset() {
        direction = 1;
        speed = Constants.ALIEN_SPEED_PER_SEC;
        shootChance = Constants.ALIEN_SHOOT_CHANCE_BASE;  // Reset to starting difficulty
        createFormation();
    }

    /**
     * Get the list of aliens (for collision detection)
     */
    public List<Alien> getAliens() { return aliens; }
}