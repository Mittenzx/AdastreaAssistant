package com.adastrea.assistant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AIAssistantTest {
    private AIAssistant assistant;

    @BeforeEach
    void setUp() {
        assistant = new AIAssistant("TestAssistant");
    }

    @Test
    void testAssistantCreation() {
        assertNotNull(assistant);
        assertEquals("TestAssistant", assistant.getAssistantName());
        assertTrue(assistant.isEnabled());
    }

    @Test
    void testAssistantComponents() {
        assertNotNull(assistant.getAudioManager());
        assertNotNull(assistant.getVisualManager());
        assertNotNull(assistant.getDialogueSystem());
        assertNotNull(assistant.getReminderSystem());
        assertNotNull(assistant.getTeachingSystem());
    }

    @Test
    void testEnableDisable() {
        assistant.setEnabled(false);
        assertFalse(assistant.isEnabled());
        
        assistant.setEnabled(true);
        assertTrue(assistant.isEnabled());
    }

    @Test
    void testRespondToQuery() {
        String response = assistant.respondToQuery("help");
        assertNotNull(response);
        assertFalse(response.isEmpty());
    }

    @Test
    void testSetReminder() {
        assertDoesNotThrow(() -> {
            assistant.setReminder("Test reminder", 1);
        });
    }
}
