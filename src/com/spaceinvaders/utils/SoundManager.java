package com.spaceinvaders.utils;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Manages all game sounds and music
 * Singleton pattern - only one instance exists
 */
public class SoundManager {
    
    // Singleton instance
    private static SoundManager instance;
    
    // Sound clips
    private Clip playerShootClip;
    private Clip alienShootClip;
    private Clip explosionClip;
    private Clip playerExplosionClip;
    private Clip menuSelectClip;
    private Clip menuConfirmClip;
    private Clip pauseClip;
    private Clip gameOverClip;
    private Clip victoryClip;
    private Clip backgroundMusic;
    
    // Volume settings (0.0 to 1.0)
    private float musicVolume = 0.5f;
    private float sfxVolume = 0.7f;
    
    // Mute settings
    private boolean musicMuted = false;
    private boolean sfxMuted = false;
    
    // Sound enabled (for low-end devices)
    private boolean soundEnabled = true;
    
    /**
     * Private constructor (Singleton)
     */
    private SoundManager() {
        loadSounds();
    }
    
    /**
     * Get the singleton instance
     */
    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }
    
    /**
     * Load all sound files
     */
    private void loadSounds() {
        // These will be null if files don't exist - game still works without sounds
        playerShootClip = loadClip("player_shoot.wav");
        alienShootClip = loadClip("alien_shoot.wav");
        explosionClip = loadClip("explosion.wav");
        playerExplosionClip = loadClip("player_explosion.wav");
        menuSelectClip = loadClip("menu_select.wav");
        menuConfirmClip = loadClip("menu_confirm.wav");
        pauseClip = loadClip("pause.wav");
        gameOverClip = loadClip("gameover.wav");
        victoryClip = loadClip("victory.wav");
        backgroundMusic = loadClip("background_music.wav");
    }
    
    /**
     * Load a single sound clip
     */
    private Clip loadClip(String filename) {
        try {
            // Try to load from resources folder
            File file = new File("resources/sounds/" + filename);
            if (file.exists()) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                return clip;
            } else {
                System.out.println("Sound file not found: " + filename + " (game will continue without it)");
                return null;
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Could not load sound: " + filename);
            return null;
        }
    }
    
    /**
     * Play a sound effect
     */
    private void playSFX(Clip clip) {
        if (!soundEnabled || sfxMuted || clip == null) return;
        
        try {
            // Stop if already playing
            if (clip.isRunning()) {
                clip.stop();
            }
            
            // Rewind and play
            clip.setFramePosition(0);
            setVolume(clip, sfxVolume);
            clip.start();
        } catch (Exception e) {
            // Ignore errors - game continues without sound
        }
    }
    
    /**
     * Set volume on a clip (0.0 to 1.0)
     */
    private void setVolume(Clip clip, float volume) {
        if (clip == null) return;
        
        try {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            // Convert linear volume to decibels
            float dB = (float) (Math.log(Math.max(volume, 0.0001)) / Math.log(10.0) * 20.0);
            gainControl.setValue(Math.max(dB, gainControl.getMinimum()));
        } catch (Exception e) {
            // Volume control not supported - ignore
        }
    }
    
    // SOUND EFFECT METHODS

    public void playPlayerShoot() { playSFX(playerShootClip); }

    public void playAlienShoot() { playSFX(alienShootClip); }
    
    public void playExplosion() { playSFX(explosionClip); }
     
    public void playPlayerExplosion() { playSFX(playerExplosionClip); }
    
    public void playMenuSelect() { playSFX(menuSelectClip); }

    public void playMenuConfirm() { playSFX(menuConfirmClip); }

    public void playPause() { playSFX(pauseClip); }
    
    public void playGameOver() { playSFX(gameOverClip); }
    
    public void playVictory() { playSFX(victoryClip); }
    
    // BACKGROUND MUSIC METHODS
    
    public void playBackgroundMusic() {
        if (!soundEnabled || musicMuted || backgroundMusic == null) return;
        
        try {
            if (!backgroundMusic.isRunning()) {
                backgroundMusic.setFramePosition(0);
                setVolume(backgroundMusic, musicVolume);
                backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            }
        } catch (Exception e) {
            // Ignore errors
        }
    }
    
    public void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
        }
    }
    
    public void pauseBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
        }
    }
    
    public void resumeBackgroundMusic() {
        if (!soundEnabled || musicMuted || backgroundMusic == null) return;
        
        try {
            if (!backgroundMusic.isRunning()) {
                backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            }
        } catch (Exception e) {
            // Ignore errors
        }
    }
    
    // VOLUME CONTROLS
    
    public void setMusicVolume(float volume) {
        this.musicVolume = Math.max(0, Math.min(1, volume));
        if (backgroundMusic != null) {
            setVolume(backgroundMusic, musicVolume);
        }
    }
    
    public float getMusicVolume() { return musicVolume; }
    
    public void setSfxVolume(float volume) {
        this.sfxVolume = Math.max(0, Math.min(1, volume));
    }
    
    public float getSfxVolume() { return sfxVolume; }
    
    // MUTE CONTROLS 
    
    public void setMusicMuted(boolean muted) {
        this.musicMuted = muted;
        if (muted) {
            stopBackgroundMusic();
        } else {
            playBackgroundMusic();
        }
    }
    
    public boolean isMusicMuted() { return musicMuted; }
    
    public void setSfxMuted(boolean muted) { this.sfxMuted = muted; }
    
    public boolean isSfxMuted() { return sfxMuted; }
    
    // SOUND ENABLED (for performance)
    
    public void setSoundEnabled(boolean enabled) {
        this.soundEnabled = enabled;
        if (!enabled) {
            stopBackgroundMusic();
        }
    }
    
    public boolean isSoundEnabled() { return soundEnabled; }
}