package com.spaceinvaders.entities;

import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * Base class for all game entities (player, bullets and aliens)
 */
public abstract class GameObject {
    // Position
    protected double x;
    protected double y;

    // Size
    protected int width;
    protected int height;

    // Movement
    protected double velocityX;
    protected double velocityY;

    // State
    protected boolean active;

    /**
     * Constructor
     * @param x The x-coordinate of the object
     * @param y The y-coordinate of the object
     * @param width The width of the object
     * @param height The height of the object
     */
    public GameObject(double x, double y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.velocityX = 0;
        this.velocityY = 0;
        this.active = true;
    }

    // Abstract methods - children must implement
    public abstract void update();
    public abstract void render(Graphics2D g2d);

    /**
     * Collision detection
     */
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }

    /**
     * Check collision with another GameObject
     * @param other The other GameObject to check collision with
     * @return true if this object collides with the other object, false otherwise
     */
    public boolean collidesWith(GameObject other) {
        if (!this.active || !other.active) {
            return false;
        }
        return this.getBounds().intersects(other.getBounds()); // AABB collision detection
    }

    // Getters & Setters
    public double getX() { return x; }
    public void setX(double x) { this.x = x; }

    public double getY() { return y; }
    public void setY(double y) { this.y = y; }

    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }

    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public void destroy() { this.active = false; } // Mark object as inactive

    public double getCenterX() { return x + width / 2.0; }
    public double getCenterY() { return y + height / 2.0; }

    public double getVelocityX() { return velocityX; }
    public double setVelocityX(double velocityX) { return this.velocityX = velocityX; }
    
    public double getVelocityY() { return velocityY; }
    public void setVelocityY(double velocityY) { this.velocityY = velocityY; }
}