package com.spaceinvaders.entities;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.spaceinvaders.utils.Constants;

/**
 * Manage the entire alien formation
 * Handles movement pattern: right -> down -> left -> down -> repeat
 */
public class AlienFormation {
    
    // All aliens in formation
    private List<Alien> aliens;

    // Movement direction (right = 1, left = -1)
    private int direction;

    // Current speed (this increase as aliens die)
    private int speed;

    // Random for alien shooting
    private Random random;

    /**
     * Constructor - creates the alien formation 
     */
    public AlienFormation() {
        aliens = new ArrayList<>();
        direction = 1;
        speed = Constants.ALIEN_SPEED;
        random = new Random();

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
    public void update() {
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
                alien.setVelocityY(Constants.ALIEN_DROP_DISTANCE);
            }
            alien.update();
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
     */
    public Bullet tryShoot() {
        // Get list of active (alive) aliens
        List<Alien> activeAliens = new ArrayList<Alien>();
        for (Alien alien : aliens) {
            if (alien.isActive()) {
                activeAliens.add(alien);
            }
        }

        // No alien left
        if (activeAliens.isEmpty()) {
            return null;
        }

        // Random chance to shoot
        if (random.nextDouble() < Constants.ALIEN_SHOOT_CHANCE) {
            // Pick random alien to shoot
            Alien shooter = activeAliens.get(random.nextInt(activeAliens.size()));
            return new Bullet(
                shooter.getBulletSpawnX(), 
                shooter.getBulletSpawnY(), 
                false   // false = alien bullet goes DOWN
            );
        }

        return null;
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
        // More aliens dead = faster speed
        int deadCount = (Constants.ALIEN_ROWS * Constants.ALIEN_COLUMNS) - getAliveCount();
        speed = Constants.ALIEN_SPEED + (deadCount / 10);
    }

    /**
     * Reset formation for new wave or game
     */
    public void reset() {
        direction = 1;
        speed = Constants.ALIEN_SPEED;
        createFormation();
    }

    /**
     * Get the list of aliens (for collision detection)
     */
    public List<Alien> getAliens() {
        return aliens;
    }
}