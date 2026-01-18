package com.spaceinvaders.utils;

/**
 * Stores all game settings
 * Singleton pattern
 */
public class Settings {
    
    private static Settings instance;
    
    // Graphics quality (0 = High, 1 = Medium, 2 = Low)
    private int graphicsQuality = Constants.QUALITY_HIGH;
    
    // Show FPS counter
    private boolean showFps = true;
    
    // Star count based on quality
    private int starCount = Constants.STAR_COUNT_HIGH;
    
    private Settings() {} // Private constructor for singleton
    
    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }
    
    // GRAPHICS QUALITY
    
    public void setGraphicsQuality(int quality) {
        this.graphicsQuality = quality;
        
        // Update star count based on quality
        switch (quality) {
            case Constants.QUALITY_HIGH:
                starCount = Constants.STAR_COUNT_HIGH;
                break;
            case Constants.QUALITY_MEDIUM:
                starCount = Constants.STAR_COUNT_MEDIUM;
                break;
            case Constants.QUALITY_LOW:
                starCount = Constants.STAR_COUNT_LOW;
                break;
        }
    }
    
    public int getGraphicsQuality() {
        return graphicsQuality;
    }
    
    public String getGraphicsQualityName() {
        switch (graphicsQuality) {
            case Constants.QUALITY_HIGH: return "HIGH";
            case Constants.QUALITY_MEDIUM: return "MEDIUM";
            case Constants.QUALITY_LOW: return "LOW";
            default: return "UNKNOWN";
        }
    }
    
    public int getStarCount() { return starCount; }
    
    // FPS DISPLAY
    
    public void setShowFps(boolean show) { this.showFps = show; }
    
    public boolean isShowFps() { return showFps; }
    
    // CYCLE METHODS (for menu)
    
    public void cycleGraphicsQuality() {
        graphicsQuality = (graphicsQuality + 1) % 3;
        setGraphicsQuality(graphicsQuality);
    }
    
    public void toggleShowFps() {
        showFps = !showFps;
    }
}