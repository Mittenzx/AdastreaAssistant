package com.adastrea.assistant;

/**
 * Configuration settings for the AI Assistant
 */
public class AssistantConfig {
    private boolean enabled;
    private boolean audioEnabled;
    private boolean visualEnabled;
    private float volume;
    private int subtitleDuration;
    private int companionDialogueIntervalMinutes;
    private String assistantName;

    public AssistantConfig() {
        // Default settings
        this.enabled = true;
        this.audioEnabled = true;
        this.visualEnabled = true;
        this.volume = 1.0f;
        this.subtitleDuration = 5000;
        this.companionDialogueIntervalMinutes = 10;
        this.assistantName = "Assistant";
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isAudioEnabled() {
        return audioEnabled;
    }

    public void setAudioEnabled(boolean audioEnabled) {
        this.audioEnabled = audioEnabled;
    }

    public boolean isVisualEnabled() {
        return visualEnabled;
    }

    public void setVisualEnabled(boolean visualEnabled) {
        this.visualEnabled = visualEnabled;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public int getSubtitleDuration() {
        return subtitleDuration;
    }

    public void setSubtitleDuration(int subtitleDuration) {
        this.subtitleDuration = subtitleDuration;
    }

    public int getCompanionDialogueIntervalMinutes() {
        return companionDialogueIntervalMinutes;
    }

    public void setCompanionDialogueIntervalMinutes(int companionDialogueIntervalMinutes) {
        this.companionDialogueIntervalMinutes = companionDialogueIntervalMinutes;
    }

    public String getAssistantName() {
        return assistantName;
    }

    public void setAssistantName(String assistantName) {
        this.assistantName = assistantName;
    }
}
