package com.adastrea.assistant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class AssistantConfigTest {
    private AssistantConfig config;

    @BeforeEach
    void setUp() {
        config = new AssistantConfig();
    }

    @Test
    void testDefaultValues() {
        assertTrue(config.isEnabled());
        assertTrue(config.isAudioEnabled());
        assertTrue(config.isVisualEnabled());
        assertEquals(1.0f, config.getVolume(), 0.01f);
        assertEquals(5000, config.getSubtitleDuration());
        assertEquals(10, config.getCompanionDialogueIntervalMinutes());
        assertEquals("Assistant", config.getAssistantName());
    }

    @Test
    void testSetters() {
        config.setEnabled(false);
        assertFalse(config.isEnabled());

        config.setAudioEnabled(false);
        assertFalse(config.isAudioEnabled());

        config.setVisualEnabled(false);
        assertFalse(config.isVisualEnabled());

        config.setVolume(0.5f);
        assertEquals(0.5f, config.getVolume(), 0.01f);

        config.setSubtitleDuration(7000);
        assertEquals(7000, config.getSubtitleDuration());

        config.setCompanionDialogueIntervalMinutes(15);
        assertEquals(15, config.getCompanionDialogueIntervalMinutes());

        config.setAssistantName("CustomName");
        assertEquals("CustomName", config.getAssistantName());
    }

    @Test
    void testLoadFromInputStream() throws IOException {
        String properties = """
                assistant.enabled=false
                assistant.audio.enabled=false
                assistant.visual.enabled=false
                assistant.audio.volume=0.7
                assistant.visual.subtitle.duration=3000
                assistant.companion.interval.minutes=20
                assistant.name=CustomAssistant
                """;

        InputStream is = new ByteArrayInputStream(properties.getBytes(StandardCharsets.UTF_8));
        config.loadFromProperties(is);

        assertFalse(config.isEnabled());
        assertFalse(config.isAudioEnabled());
        assertFalse(config.isVisualEnabled());
        assertEquals(0.7f, config.getVolume(), 0.01f);
        assertEquals(3000, config.getSubtitleDuration());
        assertEquals(20, config.getCompanionDialogueIntervalMinutes());
        assertEquals("CustomAssistant", config.getAssistantName());
    }

    @Test
    void testLoadFromFile(@TempDir Path tempDir) throws IOException {
        File tempFile = tempDir.resolve("test.properties").toFile();
        String properties = """
                assistant.enabled=false
                assistant.audio.volume=0.8
                assistant.name=FileAssistant
                """;
        Files.writeString(tempFile.toPath(), properties);

        config.loadFromProperties(tempFile.getAbsolutePath());

        assertFalse(config.isEnabled());
        assertEquals(0.8f, config.getVolume(), 0.01f);
        assertEquals("FileAssistant", config.getAssistantName());
    }

    @Test
    void testSaveToFile(@TempDir Path tempDir) throws IOException {
        File tempFile = tempDir.resolve("save.properties").toFile();
        
        config.setEnabled(false);
        config.setVolume(0.6f);
        config.setAssistantName("SavedAssistant");
        
        config.saveToProperties(tempFile.getAbsolutePath());

        assertTrue(tempFile.exists());
        
        // Load it back and verify
        AssistantConfig loadedConfig = new AssistantConfig();
        loadedConfig.loadFromProperties(tempFile.getAbsolutePath());
        
        assertFalse(loadedConfig.isEnabled());
        assertEquals(0.6f, loadedConfig.getVolume(), 0.01f);
        assertEquals("SavedAssistant", loadedConfig.getAssistantName());
    }

    @Test
    void testLoadFromInputStreamWithDefaults() throws IOException {
        String properties = "assistant.enabled=false\n";
        InputStream is = new ByteArrayInputStream(properties.getBytes(StandardCharsets.UTF_8));
        
        config.loadFromProperties(is);

        // Only enabled should be changed, others should use defaults
        assertFalse(config.isEnabled());
        assertTrue(config.isAudioEnabled());  // Default
        assertEquals("Assistant", config.getAssistantName());  // Default
    }
}
