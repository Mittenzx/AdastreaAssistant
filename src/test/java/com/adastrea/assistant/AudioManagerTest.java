package com.adastrea.assistant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AudioManagerTest {
    private AudioManager audioManager;

    @BeforeEach
    void setUp() {
        audioManager = new AudioManager();
    }

    @Test
    void testAudioEnabledByDefault() {
        assertTrue(audioManager.isAudioEnabled());
    }

    @Test
    void testSetAudioEnabled() {
        audioManager.setAudioEnabled(false);
        assertFalse(audioManager.isAudioEnabled());
        
        audioManager.setAudioEnabled(true);
        assertTrue(audioManager.isAudioEnabled());
    }

    @Test
    void testVolumeDefault() {
        assertEquals(1.0f, audioManager.getVolume(), 0.01f);
    }

    @Test
    void testSetVolume() {
        audioManager.setVolume(0.5f);
        assertEquals(0.5f, audioManager.getVolume(), 0.01f);
    }

    @Test
    void testSetVolumeClampingMax() {
        audioManager.setVolume(1.5f);
        assertEquals(1.0f, audioManager.getVolume(), 0.01f);
    }

    @Test
    void testSetVolumeClampingMin() {
        audioManager.setVolume(-0.5f);
        assertEquals(0.0f, audioManager.getVolume(), 0.01f);
    }

    @Test
    void testPlayVoice() {
        assertDoesNotThrow(() -> audioManager.playVoice("Test message"));
    }

    @Test
    void testPlayVoiceWhenDisabled() {
        audioManager.setAudioEnabled(false);
        assertDoesNotThrow(() -> audioManager.playVoice("Test message"));
    }

    @Test
    void testPlaySoundEffect() {
        assertDoesNotThrow(() -> audioManager.playSoundEffect("test_sound"));
    }

    @Test
    void testPlaySoundEffectWhenDisabled() {
        audioManager.setAudioEnabled(false);
        assertDoesNotThrow(() -> audioManager.playSoundEffect("test_sound"));
    }

    @Test
    void testPreloadAudio() {
        audioManager.preloadAudio("key1", "/path/to/audio.wav");
        assertEquals("/path/to/audio.wav", audioManager.getPreloadedAudio("key1"));
    }

    @Test
    void testGetPreloadedAudioNotFound() {
        assertNull(audioManager.getPreloadedAudio("nonexistent"));
    }
}
