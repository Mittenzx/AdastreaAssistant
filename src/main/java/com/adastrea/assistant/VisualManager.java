package com.adastrea.assistant;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages visual components of the AI assistant including notifications and subtitles.
 * 
 * Note: This is a base implementation with placeholder behavior. In a real Minecraft mod
 * integration, extend this class to implement actual UI rendering. The subtitleDuration
 * field is provided for integrators to implement subtitle timeout logic in their UI layer.
 */
public class VisualManager {
    private boolean visualEnabled;
    private List<String> activeNotifications;
    private String currentSubtitle;
    private int subtitleDuration;

    public VisualManager() {
        this.visualEnabled = true;
        this.activeNotifications = new ArrayList<>();
        this.currentSubtitle = "";
        this.subtitleDuration = 5000; // 5 seconds default
    }

    /**
     * Show a notification to the player
     * @param message The notification message
     */
    public void showNotification(String message) {
        if (!visualEnabled) {
            return;
        }
        activeNotifications.add(message);
        System.out.println("[VISUAL] Notification: " + message);
    }

    /**
     * Display subtitle for what the assistant is saying
     * @param text The subtitle text
     */
    public void showSubtitle(String text) {
        if (!visualEnabled) {
            return;
        }
        this.currentSubtitle = text;
        System.out.println("[VISUAL] Subtitle: " + text);
    }

    /**
     * Clear all active notifications
     */
    public void clearNotifications() {
        activeNotifications.clear();
    }

    /**
     * Clear the current subtitle
     */
    public void clearSubtitle() {
        this.currentSubtitle = "";
    }

    /**
     * Get all active notifications
     * @return List of active notifications
     */
    public List<String> getActiveNotifications() {
        return new ArrayList<>(activeNotifications);
    }

    /**
     * Display an animated assistant icon or avatar
     * @param emotion The emotion to display (happy, neutral, thinking, etc.)
     */
    public void showAssistantIcon(String emotion) {
        if (!visualEnabled) {
            return;
        }
        System.out.println("[VISUAL] Assistant icon showing emotion: " + emotion);
    }

    public boolean isVisualEnabled() {
        return visualEnabled;
    }

    public void setVisualEnabled(boolean enabled) {
        this.visualEnabled = enabled;
    }

    public String getCurrentSubtitle() {
        return currentSubtitle;
    }

    public int getSubtitleDuration() {
        return subtitleDuration;
    }

    public void setSubtitleDuration(int durationMs) {
        this.subtitleDuration = durationMs;
    }
}
