package com.adastrea.assistant;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages audio playback for the AI assistant including voice synthesis and sound effects
 */
public class AudioManager {
    private boolean audioEnabled;
    private float volume;
    private Map<String, String> audioCache;

    public AudioManager() {
        this.audioEnabled = true;
        this.volume = 1.0f;
        this.audioCache = new HashMap<>();
    }

    /**
     * Play voice message (text-to-speech simulation)
     * @param message The message to speak
     */
    public void playVoice(String message) {
        if (!audioEnabled) {
            return;
        }
        // In a real implementation, this would use text-to-speech API
        // For now, we simulate it
        System.out.println("[AUDIO] Assistant speaks: " + message);
    }

    /**
     * Play a sound effect
     * @param soundName The name of the sound effect
     */
    public void playSoundEffect(String soundName) {
        if (!audioEnabled) {
            return;
        }
        System.out.println("[AUDIO] Playing sound: " + soundName);
    }

    /**
     * Preload audio for faster playback
     * @param key The cache key
     * @param audioPath The path to the audio file
     */
    public void preloadAudio(String key, String audioPath) {
        audioCache.put(key, audioPath);
    }

    public boolean isAudioEnabled() {
        return audioEnabled;
    }

    public void setAudioEnabled(boolean enabled) {
        this.audioEnabled = enabled;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = Math.max(0.0f, Math.min(1.0f, volume));
    }
}
