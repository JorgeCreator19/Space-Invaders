package com.spaceinvaders.game;

import com.spaceinvaders.entities.*;
import com.spaceinvaders.utils.Constants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Main game panel - handles rendering and game loop
 */
public class GamePanel extends JPanel implements ActionListener {

    /* GAME OBJECTS */
    private Player player;
    private AlienFormation alienFormation;
    private List<Bullet> bullets;
    private MysteryShip mysteryShip;

    /* GAME STATE */
    private GameState gameState;
    private int score;
    private int lives;
    private int wave;

    /* INPUT */
    private InputHandler input;

    /* GAME LOOP */
    private Timer gameTimer;

    /**
     * Constructor - setup panel and start game
     */
    public GamePanel() {
        // Setup panel size and color
        setPreferredSize(new Dimension(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT));
        setBackground(new Color(10, 10, 30));
        setFocusable(true);

        // Enable double buffering for smoother rendering
        setDoubleBuffered(true);

        // Setup input handler
        input = new InputHandler();
        addKeyListener(input);

        // Initialize game
        initGame();

        // Start game loop timer
        gameTimer = new Timer(Constants.GAME_SPEED, this);
        gameTimer.start();
    }

    /**
     * Initialize or reset the game
     */
    private void initGame() {
        player = new Player();
        alienFormation = new AlienFormation();
        bullets = new ArrayList<>();
        mysteryShip = null;

        score = 0;
        lives = Constants.INITIAL_LIVES;
        wave = 1;
        gameState = GameState.MENU;
    }

    /**
     * Start a new game
     */
    private void startGame() {
        player.reset();
        alienFormation.fullReset();
        bullets.clear();
        mysteryShip = null;

        score = 0;
        lives = Constants.INITIAL_LIVES;
        wave = 1;
        gameState = GameState.PLAYING;
    }

    /**
     * Start next wave 
     */
    private void nextWave() {
        wave++;
        alienFormation.nextWaveDifficulty(); // Increase difficulty
        alienFormation.reset();
        bullets.clear();
        mysteryShip = null;
        player.reset();
    }

    /**
     * Game loop - called every frame by Timer
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Handle input based on current state
        handleInput();

        // Update game if playing 
        if (gameState == GameState.PLAYING) {
            update();
        }

        // Allways repaint
        repaint();
    }

    /**
     * Handle keyboard input based on game state
     */
    private void handleInput() {
        // MENU state
        if (gameState == GameState.MENU) {
            if (input.consumeEnter()) {
                startGame();
            }
            return;
        }

        // GAME OVER state
        if (gameState == GameState.GAME_OVER) {
            if (input.consumeEnter()) {
                startGame();
            }
            if (input.consumeEscape()) {
                gameState = GameState.MENU;
            }
            return;
        }

        // VICTORY state
        if (gameState == GameState.VICTORY) {
            if (input.consumeEnter()) {
                nextWave();
                gameState = GameState.PLAYING;
            }
            return;
        }

        // PAUSED state
        if (gameState == GameState.PAUSED) {
            if (input.consumePause()) {
                gameState = GameState.PLAYING;
            }
            if (input.consumeEscape()) {
                gameState = GameState.MENU;
            }
            return;
        }

        // PLAYING state
        if (gameState == GameState.PLAYING) {
            // Movement
            player.setMovingLeft(input.isLeftPressed());
            player.setMovingRight(input.isRightPressed());

            // Shooting
            if (input.isShootPressed() && player.canShoot()) {
                Bullet bullet = new Bullet(
                    player.getBulletSpawnX(),
                    player.getBulletSpawnY(),
                    true // Player bullet
                );
                bullets.add(bullet);
                player.shoot();
            }

            // Pause
            if (input.consumePause()) {
                gameState = GameState.PAUSED;
            }

            // Escape to menu
            if (input.consumeEscape()) {
                gameState = GameState.MENU;
            }
        }
    }

    /**
     * Update all game objects
     */
    private void update() {
        // Update player
        player.update();

        // Update alien formation
        alienFormation.update();

        // Alien shooting
        Bullet alienBullet = alienFormation.tryShoot();
        if (alienBullet != null) {
            bullets.add(alienBullet);
        }

        // Update bullets
        for (Bullet bullet : bullets) {
            bullet.update();
        }

        // Update mystery ship
        if (mysteryShip != null && mysteryShip.isActive()) {
            mysteryShip.update();
        } else {
            // Try to spawn mystery ship
            if (MysteryShip.shouldSpawn()) {
                mysteryShip = MysteryShip.spawn();
            }
        }

        // Check collisions
        checkCollisions();

        // Remove inactive bullets
        Iterator<Bullet> it = bullets.iterator();
        while (it.hasNext()) {
            if (!it.next().isActive()) {
                it.remove();
            }
        }

        // Check win condition (all aliens dead)
        if (alienFormation.getAliveCount() == 0) {
            gameState = GameState.VICTORY;

            // While lives <= 3 add one live when the wave is complete
            if (lives < Constants.INITIAL_LIVES) {
                lives++;
            }
            return;
        }

        // Check lose condition (aliens reached bottom)
        if (alienFormation.hasReachedBottom()) {
            lives = 0;
            gameState = GameState.GAME_OVER;
        }
    }

    /**
     * Check all collisions
     */
    private void checkCollisions() {
        // Player bullets vs aliens
        for (Bullet bullet : bullets) {
            if (!bullet.isPlayerBullet() || !bullet.isActive()) continue;
        
            for (Alien alien : alienFormation.getAliens()) {
                if (!alien.isActive()) continue;

                if (bullet.collidesWith(alien)) {
                    bullet.destroy();
                    alien.destroy();
                    score += alien.getPoints();
                    alienFormation.increaseSpeed();
                    break;
                }
            }
        }

        // Player bullets vs Mystery Ship
        if (mysteryShip != null && mysteryShip.isActive()) {
            for (Bullet bullet : bullets) {
                if (!bullet.isPlayerBullet() || !bullet.isActive()) continue;

                if (bullet.collidesWith(mysteryShip)) {
                    bullet.destroy();
                    score += mysteryShip.getPoints();
                    mysteryShip.destroy();
                    break;
                }
            }
        }

        // Alien bullets vs Player
        for (Bullet bullet : bullets) {
            if (bullet.isPlayerBullet() || !bullet.isActive()) continue;

            if (bullet.collidesWith(player)) {
                bullet.destroy();
                lives--;

                if (lives <= 0) {
                    gameState = GameState.GAME_OVER;
                }
                break;
            }
        }
    }

    /* RENDERING */

    /**
     * Paint everything on screen
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Enable anti-aliasing for smoother graphics
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw based on current state
        switch (gameState) {
            case MENU:
                drawMenu(g2d);
                break;
            case PLAYING:
                drawGame(g2d);
                break;
            case PAUSED:
                drawGame(g2d);
                drawPauseOverlay(g2d);
                break;
            case GAME_OVER:
                drawGame(g2d);
                drawGameOverOverlay(g2d);
                break;
            case VICTORY:
                drawGame(g2d);
                drawVictoryOverlay(g2d);
                break;
        }
    }

    /**
     * Helper method to draw centered text
     */
    private void drawCenteredString(Graphics2D g2d, String text, int y) {
        int width = g2d.getFontMetrics().stringWidth(text);
        int x = (Constants.WINDOW_WIDTH - width) / 2;
        g2d.drawString(text, x, y);
    }

    /**
     * Draw main menu
     */
    private void drawMenu(Graphics2D g2d) {
        // Title
        g2d.setColor(Color.GREEN);
        g2d.setFont(new Font("Arial", Font.BOLD, 60));
        drawCenteredString(g2d, "Space Invaders", Constants.WINDOW_HEIGHT / 3);

        // Instructions
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.PLAIN, 24));
        drawCenteredString(g2d, "Press ENTER to play", Constants.WINDOW_HEIGHT / 2);

        // Controls info
        g2d.setFont(new Font("Arial", Font.PLAIN, 18));
        drawCenteredString(g2d, "← → or A D to move", Constants.WINDOW_HEIGHT / 2 + 60);
        drawCenteredString(g2d, "ESPACE to Shoot", Constants.WINDOW_HEIGHT / 2 + 90);
        drawCenteredString(g2d, "P to Pause", Constants.WINDOW_HEIGHT / 2 + 120);
    }

    /**
     * Draw game elements
     */
    private void drawGame(Graphics2D g2d) {
        // Draw HUD (score, lives and wave)
        drawHUD(g2d);

        // Draw player
        player.render(g2d);

        // Draw aliens
        alienFormation.render(g2d);

        // Draw Mystery Ship
        if (mysteryShip != null && mysteryShip.isActive()) {
            mysteryShip.render(g2d);
        }

        // Draw bullets
        for (Bullet bullet : bullets) {
            if (bullet.isActive()) {
                bullet.render(g2d);
            }
        }
    }

    /**
     * Draw heads-up display
     */
    private void drawHUD(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));

        // Score (left)
        g2d.drawString("Score: " + score, 20, 30);

        // Wave (center)
        drawCenteredString(g2d, "Wave " + wave, 30);

        // Lives
        String livesText = "Lives: ";
        for (int i = 0; i < lives; i++) {
            livesText += "♥ ";
        }
        g2d.drawString(livesText, Constants.WINDOW_WIDTH - 150, 30);
    }

    /**
     * Draw pause overlay
     */
    private void drawPauseOverlay(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);

        // Pause text
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 48));
        drawCenteredString(g2d, "PAUSED", Constants.WINDOW_HEIGHT / 2 - 30);

        g2d.setFont(new Font("Arial", Font.PLAIN, 24));
        drawCenteredString(g2d, "Press P to Resume", Constants.WINDOW_HEIGHT / 2 + 30);
        drawCenteredString(g2d, "Press ESC for Menu", Constants.WINDOW_HEIGHT / 2 + 70);
    }

    /**
     * Draw game over overlay
     */
    private void drawGameOverOverlay(Graphics2D g2d) {
        // Dark red background
        g2d.setColor(new Color(100, 0, 0, 150));
        g2d.fillRect(0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);

        // Game over text
        g2d.setColor(Color.RED);
        g2d.setFont(new Font("Arial", Font.BOLD, 60));
        drawCenteredString(g2d, "GAME OVER", Constants.WINDOW_HEIGHT / 2 - 50);

        // Final score
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 30));
        drawCenteredString(g2d, "Final Score: " + score, Constants.WINDOW_HEIGHT / 2 + 20);

        // Instructions
        g2d.setFont(new Font("Arial", Font.PLAIN, 24));
        drawCenteredString(g2d, "Press ENTER to Restart", Constants.WINDOW_HEIGHT / 2 + 70);
        drawCenteredString(g2d, "Press ESC for Menu", Constants.WINDOW_HEIGHT / 2 + 110);
    }

    /**
     * Draw victory overlay
     */
    private void drawVictoryOverlay(Graphics2D g2d) {
        // Dark green background
        g2d.setColor(new Color(0, 100, 0, 150));
        g2d.fillRect(0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);

        // Victory text
        g2d.setColor(Color.GREEN);
        g2d.setFont(new Font("Arial", Font.BOLD, 48));
        drawCenteredString(g2d, "WAVE " + wave + " COMPLETE!", Constants.WINDOW_HEIGHT / 2 - 30);

        // Instructions
        g2d.setColor(Color.GREEN);
        g2d.setFont(new Font("Arial", Font.PLAIN, 24));
        drawCenteredString(g2d, "Press ENTER for Next Wave", Constants.WINDOW_HEIGHT / 2 + 30);
    }
}