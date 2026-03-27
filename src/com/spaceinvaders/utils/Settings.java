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

    // FPS setting (0 = 60fps, 1 = 120fps, 2 = unlimited)
    private int fpsOption = Constants.FPS_OPTION_60;
    
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

    // FPS SETTING
    public void setFpsOption(int option) { this.fpsOption = option; }

    public int getFpsOption() { return fpsOption; }
    
    public String getFpsOptionName() {
        return switch (fpsOption) {
            case Constants.FPS_OPTION_60 -> "60 FPS";
            case Constants.FPS_OPTION_120 -> "120 FPS";
            case Constants.FPS_OPTION_UNLIMITED -> "UNLIMITED";
            default -> "UNKNOWN";
        };
    }

    /**
     * Get the actual FPS value (or 0 for unlimited)
     */
    public int getTargetFps() {
        return switch (fpsOption) {
            case Constants.FPS_OPTION_60 -> Constants.FPS_60;
            case Constants.FPS_OPTION_120 -> Constants.FPS_120;
            case Constants.FPS_OPTION_UNLIMITED -> Constants.FPS_UNLIMITED;
            default -> Constants.FPS_60;
        };
    }

    /**
     * Get timer delay in milliseconds (or 1ms for unlimited)
     */
    public int getTimerDelay() {
        int targetFps = getTargetFps();
        if (targetFps == 0) {
            return 1;   // 1ms delay for "unlimited" (as fast as possible)
        }
        return 1000 / targetFps;
    }

    // CYCLE METHODS (for menu)
    
    public void cycleGraphicsQuality() {
        graphicsQuality = (graphicsQuality + 1) % 3;
        setGraphicsQuality(graphicsQuality);
    }
    
    public void toggleShowFps() {
        showFps = !showFps;
    }

    public void cycleFpsOption() {
        fpsOption = (fpsOption + 1) % 3;
    }

    public void cycleFpsOptionReverse() {
        fpsOption = (fpsOption - 1 + 3) % 3;
    }
}