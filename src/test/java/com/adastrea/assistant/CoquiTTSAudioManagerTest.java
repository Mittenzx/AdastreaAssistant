package com.adastrea.assistant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for CoquiTTSAudioManager.
 */
class CoquiTTSAudioManagerTest {
    
    private CoquiTTSAudioManager audioManager;
    
    @BeforeEach
    void setUp() {
        // Use current directory as project root for testing
        String projectRoot = System.getProperty("user.dir");
        audioManager = new CoquiTTSAudioManager(projectRoot);
    }
    
    @Test
    void testAudioManagerCreation() {
        assertNotNull(audioManager);
        assertTrue(audioManager.isAudioEnabled());
    }
    
    @Test
    void testTTSScriptPathSet() {
        String scriptPath = audioManager.getTTSScriptPath();
        assertNotNull(scriptPath);
        assertTrue(scriptPath.endsWith("tts_generate_human.py"));
    }
    
    @Test
    void testAudioOutputDirectorySet() {
        String outputDir = audioManager.getAudioOutputDir();
        assertNotNull(outputDir);
        assertTrue(outputDir.contains("audio/generated"));
    }
    
    @Test
    void testPlayVoiceWithEmotion() {
        // This test just verifies the method doesn't crash
        // Actual audio generation requires TTS to be installed
        assertDoesNotThrow(() -> {
            audioManager.playVoiceWithEmotion("Test message", "neutral");
        });
    }
    
    @Test
    void testPlayVoiceDefaultsToNeutral() {
        // This test verifies that playVoice works (uses neutral emotion)
        assertDoesNotThrow(() -> {
            audioManager.playVoice("Test message");
        });
    }
    
    @Test
    void testAudioEnabledDisabled() {
        audioManager.setAudioEnabled(false);
        assertFalse(audioManager.isAudioEnabled());
        
        // Should not throw when disabled
        assertDoesNotThrow(() -> {
            audioManager.playVoice("Test message");
            audioManager.playVoiceWithEmotion("Test message", "excited");
        });
        
        audioManager.setAudioEnabled(true);
        assertTrue(audioManager.isAudioEnabled());
    }
    
    @Test
    void testVolumeControl() {
        audioManager.setVolume(0.5f);
        assertEquals(0.5f, audioManager.getVolume(), 0.01f);
        
        audioManager.setVolume(1.0f);
        assertEquals(1.0f, audioManager.getVolume(), 0.01f);
        
        audioManager.setVolume(0.0f);
        assertEquals(0.0f, audioManager.getVolume(), 0.01f);
    }
    
    @Test
    void testVolumeClampingAboveRange() {
        audioManager.setVolume(1.5f);
        assertEquals(1.0f, audioManager.getVolume(), 0.01f);
    }
    
    @Test
    void testVolumeClampingBelowRange() {
        audioManager.setVolume(-0.5f);
        assertEquals(0.0f, audioManager.getVolume(), 0.01f);
    }
    
    @Test
    void testInheritedAudioCacheFunctionality() {
        // Test inherited preloadAudio and getPreloadedAudio methods
        audioManager.preloadAudio("test_key", "/path/to/audio.wav");
        String path = audioManager.getPreloadedAudio("test_key");
        assertEquals("/path/to/audio.wav", path);
    }
    
    @Test
    void testAudioCacheReturnsNullForMissingKey() {
        String path = audioManager.getPreloadedAudio("nonexistent_key");
        assertNull(path);
    }
    
    @Test
    void testSoundEffectPlayback() {
        // Test inherited playSoundEffect method
        assertDoesNotThrow(() -> {
            audioManager.playSoundEffect("beep");
        });
    }
    
    @Test
    void testCustomPythonExecutable() {
        String projectRoot = System.getProperty("user.dir");
        CoquiTTSAudioManager customManager = new CoquiTTSAudioManager(
            projectRoot, 
            "python3", 
            true
        );
        assertNotNull(customManager);
    }
    
    @Test
    void testTTSDisabledMode() {
        String projectRoot = System.getProperty("user.dir");
        CoquiTTSAudioManager disabledManager = new CoquiTTSAudioManager(
            projectRoot, 
            "python3", 
            false  // Disable TTS
        );
        assertNotNull(disabledManager);
        assertFalse(disabledManager.isTTSAvailable());
    }
    
    @Test
    void testMultipleEmotions() {
        // Test various emotions - should not crash even if TTS not installed
        String[] emotions = {
            "hostile", "angry", "curious", "fascinated", 
            "cooperative", "friendly", "excited", "worried",
            "contemplative", "urgent", "neutral"
        };
        
        for (String emotion : emotions) {
            assertDoesNotThrow(() -> {
                audioManager.playVoiceWithEmotion("Test for " + emotion, emotion);
            });
        }
    }
}
