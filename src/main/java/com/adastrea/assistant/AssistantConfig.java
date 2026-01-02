package com.adastrea.assistant;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration settings for the AI Assistant with support for loading/saving properties files
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

    /**
     * Load configuration from a properties file
     * @param propertiesPath Path to the properties file
     * @throws IOException If the file cannot be read
     */
    public void loadFromProperties(String propertiesPath) throws IOException {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(propertiesPath)) {
            props.load(fis);
        }
        applyProperties(props);
    }

    /**
     * Load configuration from an InputStream (e.g., from resources)
     * @param inputStream InputStream to read properties from
     * @throws IOException If the stream cannot be read
     */
    public void loadFromProperties(InputStream inputStream) throws IOException {
        Properties props = new Properties();
        props.load(inputStream);
        applyProperties(props);
    }

    /**
     * Apply properties to configuration
     * @param props Properties object containing configuration values
     */
    private void applyProperties(Properties props) {
        this.enabled = Boolean.parseBoolean(props.getProperty("assistant.enabled", "true"));
        this.audioEnabled = Boolean.parseBoolean(props.getProperty("assistant.audio.enabled", "true"));
        this.visualEnabled = Boolean.parseBoolean(props.getProperty("assistant.visual.enabled", "true"));
        this.volume = Float.parseFloat(props.getProperty("assistant.audio.volume", "1.0"));
        this.subtitleDuration = Integer.parseInt(props.getProperty("assistant.visual.subtitle.duration", "5000"));
        this.companionDialogueIntervalMinutes = Integer.parseInt(props.getProperty("assistant.companion.interval.minutes", "10"));
        this.assistantName = props.getProperty("assistant.name", "Assistant");
    }

    /**
     * Save configuration to a properties file
     * @param propertiesPath Path where the properties file should be saved
     * @throws IOException If the file cannot be written
     */
    public void saveToProperties(String propertiesPath) throws IOException {
        Properties props = new Properties();
        props.setProperty("assistant.enabled", String.valueOf(enabled));
        props.setProperty("assistant.audio.enabled", String.valueOf(audioEnabled));
        props.setProperty("assistant.visual.enabled", String.valueOf(visualEnabled));
        props.setProperty("assistant.audio.volume", String.valueOf(volume));
        props.setProperty("assistant.visual.subtitle.duration", String.valueOf(subtitleDuration));
        props.setProperty("assistant.companion.interval.minutes", String.valueOf(companionDialogueIntervalMinutes));
        props.setProperty("assistant.name", assistantName);

        try (FileOutputStream fos = new FileOutputStream(propertiesPath)) {
            props.store(fos, "Adastrea AI Assistant Configuration");
        }
    }
}
