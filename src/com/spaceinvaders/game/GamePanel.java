package com.spaceinvaders.game;

import com.spaceinvaders.entities.*;
import com.spaceinvaders.utils.Constants;
import com.spaceinvaders.utils.Settings;
import com.spaceinvaders.utils.SoundManager;
import com.spaceinvaders.utils.ScoreManager;

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

    /* MENU */
    private int menuSelection = 0; // 0 = Play, 1 = Controls, 2 = Exit
    private static final int MENU_PLAY = 0;
    private static final int MENU_CONTROLS = 1;
    private static final int MENU_SETTINGS = 2;
    private static final int MENU_EXIT = 3;
    private static final int MENU_OPTIONS = 4; // Total menu options

    /* SETTINGS MENU */
    private int settingsSelection = 0;
    private static final int SETTINGS_MUSIC_VOL = 0;
    private static final int SETTINGS_SFX_VOL = 1;
    private static final int SETTINGS_GRAPHICS = 2;
    private static final int SETTINGS_SHOW_FPS = 3;
    private static final int SETTINGS_BACK = 4;
    private static final int SETTINGS_OPTIONS = 5;

    /* SOUND & SETTINGS */
    private SoundManager soundManager;
    private Settings settings;

    /* ANIMATED BACKGROUND */
    private Star[] stars;

    /* HIGH SCORE */
    private int highScore = 0;

    /* SCORE MANAGER */
    private ScoreManager scoreManager;

    /* ANIMATED ALIENS FOR MENU */
    private int menuAlienFrame = 0;
    private int menuAlienTimer = 0;

    /* GAME OBJECTS */
    private Player player;
    private AlienFormation alienFormation;
    private List<Bullet> bullets;
    private MysteryShip mysteryShip;
    private List<Explosion> explosions;

    /* FPS COUNTER */
    private int fps;
    private int frameCount;
    private long lastFpsTime;
    private boolean showFps = true;

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
        setBackground(new Color(0, 0, 0)); // Pure black for space
        setFocusable(true);

        // Enable double buffering for smoother rendering
        setDoubleBuffered(true);

        // Initialize settings, sound, and score manager
        settings = Settings.getInstance();
        soundManager = SoundManager.getInstance();
        scoreManager = ScoreManager.getInstance();

        // Initialize stars based on settings
        initStars();

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
     * Initialize stars based on graphics quality
     */
    private void initStars() {
        int starCount = settings.getStarCount();
        stars = new Star[starCount];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star();
        }
    }

    /**
     * Initialize or reset the game
     */
    private void initGame() {
        player = new Player();
        alienFormation = new AlienFormation();
        bullets = new ArrayList<>();
        mysteryShip = null;
        explosions = new ArrayList<>();

        score = 0;
        lives = Constants.INITIAL_LIVES;
        wave = 1;
        gameState = GameState.MENU;
        menuSelection = 0;

        // Load high score from file
        highScore = scoreManager.getHighScore();

        // FPS counter init
        fps = 0;
        frameCount = 0;
        lastFpsTime = System.currentTimeMillis();
    }

    /**
     * Start a new game
     */
    private void startGame() {
        player.reset();
        alienFormation.fullReset();
        bullets.clear();
        mysteryShip = null;
        explosions.clear();

        score = 0;
        lives = Constants.INITIAL_LIVES;
        wave = 1;
        gameState = GameState.PLAYING;

        // Start background music
        soundManager.playBackgroundMusic();
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
        explosions.clear();
        player.reset();
    }

    /**
     * Game loop - called every frame by Timer
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Calculate FPS
        frameCount++;
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastFpsTime >= 1000) {
            fps = frameCount;
            frameCount = 0;
            lastFpsTime = currentTime;
        }

        // Update stars (always, for background animation)
        for (Star star : stars) {
            star.update();
        }

        // Update menu alien animation
        menuAlienTimer++;
        if (menuAlienTimer >= 30) {
            menuAlienTimer = 0;
            menuAlienFrame = (menuAlienFrame + 1) % 2;
        }

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
            // Navigate up
            if (input.consumeUp()) {
                menuSelection--;
                if (menuSelection < 0) {
                    menuSelection = MENU_OPTIONS - 1;
                }
                soundManager.playMenuSelect();
            }

            // Navigate down
            if (input.consumeDown()) {
                menuSelection++;
                if (menuSelection >= MENU_OPTIONS) {
                    menuSelection = 0;
                }
                soundManager.playMenuSelect();
            }
            // Selection option
            if (input.consumeEnter()) {
                soundManager.playMenuConfirm();
                switch (menuSelection) {
                    case MENU_PLAY:
                        startGame();
                        break;
                    case MENU_CONTROLS:
                        gameState = GameState.CONTROLS;
                        break;
                    case MENU_SETTINGS:
                        gameState = GameState.SETTINGS;
                        settingsSelection = 0;
                        break;
                    case MENU_EXIT:
                        System.exit(0);
                        break;
                }
            }
            return;
        }

        // SETTINGS state
        if (gameState == GameState.SETTINGS) {
            if (input.consumeUp()) {
                settingsSelection--;
                if (settingsSelection < 0) {
                    settingsSelection = SETTINGS_OPTIONS - 1;
                }
                soundManager.playMenuSelect();
            }
            if (input.consumeDown()) {
                settingsSelection++;
                if (settingsSelection >= SETTINGS_OPTIONS) {
                    settingsSelection = 0;
                }
                soundManager.playMenuSelect();
            }
            if (input.consumeLeft()) {
                adjustSetting(-1);
            }
            if (input.consumeRight()) {
                adjustSetting(1);
            }
            if (input.consumeEnter()) {
                if (settingsSelection == SETTINGS_BACK) {
                    soundManager.playMenuConfirm();
                    gameState = GameState.MENU;
                } else if (settingsSelection == SETTINGS_GRAPHICS) {
                    settings.cycleGraphicsQuality();
                    initStars();  // Reinitialize stars with new count
                    soundManager.playMenuConfirm();
                } else if (settingsSelection == SETTINGS_SHOW_FPS) {
                    settings.toggleShowFps();
                    soundManager.playMenuConfirm();
                }
            }
            if (input.consumeEscape()) {
                soundManager.playMenuConfirm();
                gameState = GameState.MENU;
            }
            return;
        }

        // CONTROLS state
        if (gameState == GameState.CONTROLS) {
            if (input.consumeEscape() || input.consumeEnter()) {
                soundManager.playMenuConfirm();
                gameState = GameState.MENU;
            }
            return;
        }

        // GAME OVER state
        if (gameState == GameState.GAME_OVER) {
            // Check high score (in case it wasn't checked yet)
            if (scoreManager.checkAndUpdateHighScore(score)) {
                highScore = score;
            }
            
            if (input.consumeEnter()) {
                soundManager.playMenuConfirm();
                startGame();
            }
            if (input.consumeEscape()) {
                soundManager.playMenuConfirm();
                gameState = GameState.MENU;
            }
            return;
        }

        // VICTORY state
        if (gameState == GameState.VICTORY) {
            if (input.consumeEnter()) {
                soundManager.playMenuConfirm();
                nextWave();
                gameState = GameState.PLAYING;
            }
            return;
        }

        // PAUSED state
        if (gameState == GameState.PAUSED) {
            if (input.consumePause()) {
                soundManager.playPause();
                soundManager.resumeBackgroundMusic();
                gameState = GameState.PLAYING;
            }
            if (input.consumeEscape()) {
                soundManager.playMenuConfirm();
                soundManager.stopBackgroundMusic();
                gameState = GameState.MENU;
            }
            return;
        }

        // PLAYING state
        if (gameState == GameState.PLAYING) {
            player.setMovingLeft(input.isLeftPressed());
            player.setMovingRight(input.isRightPressed());

            if (input.isShootPressed() && player.canShoot()) {
                Bullet bullet = new Bullet(
                    player.getBulletSpawnX(),
                    player.getBulletSpawnY(), 
                    true
                );
                bullets.add(bullet);
                player.shoot();
                soundManager.playPlayerShoot(); // Play shoot sound
            }

            if (input.consumePause()) {
                soundManager.playPause();
                soundManager.pauseBackgroundMusic();
                gameState = GameState.PAUSED;
            }

            if (input.consumeEscape()) {
                soundManager.stopBackgroundMusic();
                gameState = GameState.MENU;
            }
        }
    }

    /**
     * Adjust setting value (for sliders)
     */
    private void adjustSetting(int direction) {
        switch (settingsSelection) {
            case SETTINGS_MUSIC_VOL:
                float newMusicVol = soundManager.getMusicVolume() + (direction * 0.1f);
                soundManager.setMusicVolume(newMusicVol);
                break;
            case SETTINGS_SFX_VOL:
                float newSfxVol = soundManager.getSfxVolume() + (direction * 0.1f);
                soundManager.setSfxVolume(newSfxVol);
                soundManager.playMenuSelect();  // Test the new volume
                break;
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
            soundManager.playAlienShoot();
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

        // Update explosions
        Iterator<Explosion> explosionIt = explosions.iterator();
        while (explosionIt.hasNext()) {
            Explosion explosion = explosionIt.next();
            explosion.update();
            if (!explosion.isActive()) {
                explosionIt.remove();
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
            soundManager.playVictory();
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
            soundManager.stopBackgroundMusic();
            soundManager.playGameOver();

            // Check and save high score 
            if (scoreManager.checkAndUpdateHighScore(score)) {
                highScore = score;
            }
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
                    
                    // Create explosion at alien position
                    Color explosionColor = getAlienColor(alien.getRow());
                    explosions.add(new Explosion(
                        alien.getX() + alien.getWidth() / 2,
                        alien.getY() + alien.getHeight() / 2,
                        explosionColor
                    ));
                    soundManager.playExplosion();

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
                    
                    // Create explosion at mystery ship
                    explosions.add(new Explosion(
                    mysteryShip.getX() + mysteryShip.getWidth() / 2,
                    mysteryShip.getY() + mysteryShip.getHeight() / 2,
                    new Color(255, 0, 0)  // Red explosion
                    ));
                    soundManager.playExplosion();
                    
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

                // Create explosion at player position
                explosions.add(new Explosion(
                player.getX() + player.getWidth() / 2,
                player.getY() + player.getHeight() / 2,
                new Color(0, 255, 0)  // Green explosion
                ));
                soundManager.playPlayerExplosion();

                if (lives <= 0) {
                    soundManager.stopBackgroundMusic();
                    soundManager.playGameOver();

                    // Check and save high score
                    if (scoreManager.checkAndUpdateHighScore(score)) {
                        highScore = score;  // Update local variable too
                    }

                    gameState = GameState.GAME_OVER;
                }
                break;
            }
        }
    }

    /**
     * Helper to get alien color based on row
     */
    private Color getAlienColor(int row) {
        if (row == 0) {
            return new Color(255, 0, 255);  // Purple
        } else if (row <= 2) {
            return new Color(0, 255, 255);  // Cyan
        } else {
            return new Color(0, 255, 0);    // Green
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

        // Always clear screen first with solid color
        g2d.setColor(new Color(10, 10, 30));
        g2d.fillRect(0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);

        // Always draw starfield (it's behind everything)
        drawStarfield(g2d);

        // Draw based on current state
        switch (gameState) {
            case MENU:
                // Clear and draw menu
                drawMenu(g2d);
                break;

            case CONTROLS:
                // Clear and draw controls screen
                drawControls(g2d);
                break;

            case SETTINGS:
                // drawSettings handles its own background clearing
                drawSettings(g2d);
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
     * Draw only game elements (player, aliens, bullets, explosions)
     * Called ONLY during gameplay states
     */
    private void drawGameElements(Graphics2D g2d) {
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

        // Draw explosions
        for (Explosion explosion : explosions) {
            if (explosion.isActive()) {
                explosion.render(g2d);
            }
        }
    }

    /**
     * Draw animated starfield background
     */
    private void drawStarfield(Graphics2D g2d) {
        for (Star star : stars) {
            star.render(g2d);
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
        int centerX = Constants.WINDOW_WIDTH / 2;

        // Title
        g2d.setColor(new Color(0, 255, 0));
        g2d.setFont(new Font("Arial", Font.BOLD, 72));
        drawCenteredString(g2d, "SPACE", 120);

        g2d.setColor(new Color(255, 255, 255));
        g2d.setFont(new Font("Arial", Font.BOLD, 72));
        drawCenteredString(g2d, "INVADERS", 200);

        // Animated aliens
        drawMenuAliens(g2d, centerX, 260);

        // Menu options
        int menuStartY = 320;
        int menuSpacing = 45;

        String[] menuOptions = {"PLAY GAME", "CONTROLS", "SETTINGS", "EXIT"};

        for (int i = 0; i < menuOptions.length; i++) {
            int y = menuStartY + i * menuSpacing;

            if (i == menuSelection) {
                // Selected option - highlighted
                g2d.setColor(new Color(0, 0, 0, 150));
                int boxWidth = 250;
                int boxHeight = 40;
                g2d.fillRect(centerX - boxWidth / 2, y - 28, boxWidth, boxHeight);

                g2d.setColor(new Color(0, 255, 0));
                g2d.setFont(new Font("Arial", Font.BOLD, 28));

                // Draw selection arrows
                g2d.drawString(">", centerX - 120, y);
                g2d.drawString("<", centerX + 100, y);
            } else {
                // Unselected option
                g2d.setColor(new Color(150, 150, 150));
                g2d.setFont(new Font("Arial", Font.PLAIN, 24));
            }

            drawCenteredString(g2d, menuOptions[i], y);
        }

        // Instructions
        g2d.setColor(new Color(100, 100, 100));
        g2d.setFont(new Font("Arial", Font.PLAIN, 16));
        drawCenteredString(g2d, "Use UP/DOWN to select, ENTER to confirm", 520);

        //High score
        g2d.setColor(new Color(255, 255, 0));
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.drawString("HIGH SCORE: " + String.format("%05d", highScore), 20, Constants.WINDOW_HEIGHT - 40);

        // FPS display
        if (showFps) {
            g2d.setColor(Color.YELLOW);
            g2d.setFont(new Font("Arial", Font.PLAIN, 14));
            g2d.drawString("FPS: " + fps, Constants.WINDOW_WIDTH - 70, Constants.WINDOW_HEIGHT - 10);
        }

        // Credits
        g2d.setColor(new Color(80, 80, 80));
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        drawCenteredString(g2d, "Created by JorgeCreator19", Constants.WINDOW_HEIGHT - 15);
    }

    /**
     * Draw animated aliens in menu
     */
    private void drawMenuAliens(Graphics2D g2d, int centerX, int y) {
        // Draw 5 aliens in a row with animation
        int alienSpacing = 60;
        int startX = centerX - (alienSpacing * 2);

        Color[] colors = {
            new Color(0, 255, 0),   // Green
            new Color(0, 255, 255), // Cyan
            new Color(255, 0, 255), // Purple
            new Color(0, 255, 255), // Cyan
            new Color(0, 255, 0)    // Green
        };

        for (int i = 0; i < 5; i++) {
            int alienX = startX + i * alienSpacing;
            int alienY = y + (menuAlienTimer == 0 ? 0 : 5); // Bounce animation

            drawMenuAlien(g2d, alienX, alienY, colors[i]);
        }
    }

    /**
     * Draw a single menu alien
     */
    private void drawMenuAlien(Graphics2D g2d, int x, int y, Color color) {
    g2d.setColor(color);
    int pixel = 3;
    
    // Simple alien shape
    //   ████
    // ████████
    // ██ ██ ██
    // ████████
    //  █    █
    
    // Row 0
    g2d.fillRect(x + 2*pixel, y, pixel*4, pixel);
    // Row 1
    g2d.fillRect(x, y + pixel, pixel*8, pixel);
    // Row 2
    g2d.fillRect(x, y + pixel*2, pixel*2, pixel);
    g2d.fillRect(x + pixel*3, y + pixel*2, pixel*2, pixel);
    g2d.fillRect(x + pixel*6, y + pixel*2, pixel*2, pixel);
    // Row 3
    g2d.fillRect(x, y + pixel*3, pixel*8, pixel);
    // Row 4
    g2d.fillRect(x + pixel, y + pixel*4, pixel, pixel);
    g2d.fillRect(x + pixel*6, y + pixel*4, pixel, pixel);
    }

    /**
     * Draw controls screen
     */
    private void drawControls(Graphics2D g2d) {
        // Title
        g2d.setColor(new Color(0, 255, 0));
        g2d.setFont(new Font("Arial", Font.BOLD, 48));
        drawCenteredString(g2d, "CONTROLS", 80);

        // Controls box
        int boxX = 150;
        int boxY = 120;
        int boxWidth = Constants.WINDOW_WIDTH - 300;
        int boxHeight = 350;

        // Box background
        g2d.setColor(new Color(0, 255, 0));
        g2d.drawRect(boxX, boxY, boxWidth, boxHeight);

        // Movement section
        int leftCol = boxX + 40;
        int rightCol = boxX + boxWidth - 150;
        int startY = boxY + 50;
        int lineHeight = 40;

        g2d.setColor(new Color(255, 255, 0));
        g2d.setFont(new Font("Arial", Font.BOLD, 22));
        g2d.drawString("MOVEMENT", leftCol, startY);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.PLAIN, 18));
        
        g2d.drawString("Move Left", leftCol, startY + lineHeight);
        g2d.drawString("A  or  ←", rightCol, startY + lineHeight);
        
        g2d.drawString("Move Right", leftCol, startY + lineHeight * 2);
        g2d.drawString("D  or  →", rightCol, startY + lineHeight * 2);

        // Actions section
        g2d.setColor(new Color(255, 255, 0));
        g2d.setFont(new Font("Arial", Font.BOLD, 22));
        g2d.drawString("ACTIONS", leftCol, startY + lineHeight * 3 + 20);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.PLAIN, 18));
        
        g2d.drawString("Shoot", leftCol, startY + lineHeight * 4 + 20);
        g2d.drawString("SPACE", rightCol, startY + lineHeight * 4 + 20);
        
        g2d.drawString("Pause", leftCol, startY + lineHeight * 5 + 20);
        g2d.drawString("P", rightCol, startY + lineHeight * 5 + 20);
        
        g2d.drawString("Back to Menu", leftCol, startY + lineHeight * 6 + 20);
        g2d.drawString("ESC", rightCol, startY + lineHeight * 6 + 20);

        // Back instruction
        g2d.setColor(new Color(100, 100, 100));
        g2d.setFont(new Font("Arial", Font.PLAIN, 16));
        drawCenteredString(g2d, "Press ENTER or ESC to go back", 520);

        // FPS display
        if (showFps) {
            g2d.setColor(Color.YELLOW);
            g2d.setFont(new Font("Arial", Font.PLAIN, 14));
            g2d.drawString("FPS: " + fps, Constants.WINDOW_WIDTH - 70, Constants.WINDOW_HEIGHT - 10);
        }
    }

    /**
     * Draw settings screen
     */
    private void drawSettings(Graphics2D g2d) {
        // Fill entire screen with solid background (hides everything behind)
        g2d.setColor(new Color(10, 10, 30));
        g2d.fillRect(0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);

        // Redraw starfield on top of solid background
        drawStarfield(g2d);

        // Title
        g2d.setColor(new Color(0, 255, 0));
        g2d.setFont(new Font("Arial", Font.BOLD, 48));
        drawCenteredString(g2d, "SETTINGS", 80);

        // Settings box
        int boxX = 100;
        int boxY = 120;
        int boxWidth = Constants.WINDOW_WIDTH - 200;
        int boxHeight = 380;

        // Box background
        g2d.setColor(new Color(20, 20, 40));
        g2d.fillRect(boxX, boxY, boxWidth, boxHeight);
        
        // Box border
        g2d.setColor(new Color(0, 255, 0));
        g2d.drawRect(boxX, boxY, boxWidth, boxHeight);

        // Settings options
        int startY = boxY + 60;
        int lineHeight = 60;
        int leftCol = boxX + 40;
        int rightCol = boxX + boxWidth - 200;

        String[] settingsNames = {
            "Music Volume",
            "SFX Volume", 
            "Graphics Quality",
            "Show FPS",
            "BACK TO MENU"
        };

        for (int i = 0; i < SETTINGS_OPTIONS; i++) {
            int y = startY + i * lineHeight;

            // Highlight selected
            if (i == settingsSelection) {
                g2d.setColor(new Color(0, 255, 0, 50));
                g2d.fillRect(boxX + 10, y - 25, boxWidth - 20, 40);
                g2d.setColor(new Color(0, 255, 0));
                g2d.setFont(new Font("Arial", Font.BOLD, 22));
            } else {
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.PLAIN, 20));
            }

            // Draw setting name
            g2d.drawString(settingsNames[i], leftCol, y);

            // Draw setting value
            if (i == settingsSelection) {
                g2d.setColor(new Color(255, 255, 0));
            } else {
                g2d.setColor(new Color(150, 150, 150));
            }

            switch (i) {
                case SETTINGS_MUSIC_VOL:
                    drawVolumeBar(g2d, rightCol, y - 15, soundManager.getMusicVolume());
                    break;
                case SETTINGS_SFX_VOL:
                    drawVolumeBar(g2d, rightCol, y - 15, soundManager.getSfxVolume());
                    break;
                case SETTINGS_GRAPHICS:
                    g2d.drawString("< " + settings.getGraphicsQualityName() + " >", rightCol, y);
                    break;
                case SETTINGS_SHOW_FPS:
                    g2d.drawString(settings.isShowFps() ? "ON" : "OFF", rightCol + 50, y);
                    break;
                case SETTINGS_BACK:
                    // No value for back button
                    break;
            }

            // Instructions
            g2d.setColor(new Color(100, 100, 100));
            g2d.setFont(new Font("Arial", Font.PLAIN, 16));
            drawCenteredString(g2d, "UP/DOWN to select, LEFT/RIGHT to adjust, ENTER to confirm", 540);
        
            // FPS
            if (settings.isShowFps()) {
                g2d.setColor(Color.YELLOW);
                g2d.setFont(new Font("Arial", Font.PLAIN, 14));
                g2d.drawString("FPS: " + fps, Constants.WINDOW_WIDTH - 70, Constants.WINDOW_HEIGHT - 10);
            }
        }
    }

    /**
     * Draw volume bar for settings
     */
    private void drawVolumeBar(Graphics2D g2d, int x, int y, float volume) {
        int barWidth = 120;
        int barHeight = 20;
        int fillWidth = (int) (barWidth * volume);
        
        // Background
        g2d.setColor(new Color(50, 50, 50));
        g2d.fillRect(x, y, barWidth, barHeight);
        
        // Fill
        g2d.setColor(new Color(0, 255, 0));
        g2d.fillRect(x, y, fillWidth, barHeight);
        
        // Border
        g2d.setColor(new Color(100, 100, 100));
        g2d.drawRect(x, y, barWidth, barHeight);
        
        // Percentage text
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        g2d.drawString((int)(volume * 100) + "%", x + barWidth + 10, y + 15);
    }

    /**
     * Draw game elements
     */
    private void drawGame(Graphics2D g2d) {
        // Draw game elements
        drawGameElements(g2d);

        // Draw HUD (score, lives and wave)
        drawHUD(g2d);

        // Draw FPS counter
        if (settings.isShowFps()) {
            drawFps(g2d);
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
        drawCenteredString(g2d, "GAME OVER", Constants.WINDOW_HEIGHT / 2 - 80);
    
        // Check if this is a new high score
        if (score >= highScore && score > 0) {
            g2d.setColor(new Color(255, 255, 0));
            g2d.setFont(new Font("Arial", Font.BOLD, 28));
            drawCenteredString(g2d, "★ NEW HIGH SCORE! ★", Constants.WINDOW_HEIGHT / 2 - 30);
        }
    
        // Final score
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 30));
        drawCenteredString(g2d, "Final Score: " + score, Constants.WINDOW_HEIGHT / 2 + 20);
    
        // High score
        g2d.setColor(new Color(255, 255, 0));
        g2d.setFont(new Font("Arial", Font.PLAIN, 22));
        drawCenteredString(g2d, "High Score: " + highScore, Constants.WINDOW_HEIGHT / 2 + 60);
    
        // Instructions
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.PLAIN, 24));
        drawCenteredString(g2d, "Press ENTER to Restart", Constants.WINDOW_HEIGHT / 2 + 110);
        drawCenteredString(g2d, "Press ESC for Menu", Constants.WINDOW_HEIGHT / 2 + 150);
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

    /**
     * Draw FPS counter in corner
     */
    private void drawFps(Graphics2D g2d) {
        if (settings.isShowFps()) {
            g2d.setColor(Color.YELLOW);
            g2d.setFont(new Font("Arial", Font.PLAIN, 14));
            g2d.drawString("FPS: " + fps, Constants.WINDOW_WIDTH - 70, Constants.WINDOW_HEIGHT - 10);
        }
    }
}