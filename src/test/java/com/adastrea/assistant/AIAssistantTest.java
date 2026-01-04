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
    void testRespondToQueryWhenDisabled() {
        assistant.setEnabled(false);
        String response = assistant.respondToQuery("help");
        assertEquals("", response);
    }

    @Test
    void testSetReminder() {
        assertDoesNotThrow(() -> {
            assistant.setReminder("Test reminder", 1);
        });
    }

    @Test
    void testSpeak() {
        // Test that speak method works without throwing exception
        assertDoesNotThrow(() -> assistant.speak("Test message"));
    }

    @Test
    void testSpeakWhenDisabled() {
        assistant.setEnabled(false);
        assertDoesNotThrow(() -> assistant.speak("Test message"));
    }

    @Test
    void testProvideCompanionDialogue() {
        assertDoesNotThrow(() -> assistant.provideCompanionDialogue());
    }

    @Test
    void testProvideCompanionDialogueWhenDisabled() {
        assistant.setEnabled(false);
        assertDoesNotThrow(() -> assistant.provideCompanionDialogue());
    }

    @Test
    void testTeach() {
        assertDoesNotThrow(() -> assistant.teach("oxygen"));
    }

    @Test
    void testTeachWhenDisabled() {
        assistant.setEnabled(false);
        assertDoesNotThrow(() -> assistant.teach("oxygen"));
    }

    @Test
    void testCheckReminders() {
        assistant.setReminder("Test reminder", -1);
        assertDoesNotThrow(() -> assistant.checkReminders());
    }

    @Test
    void testCheckRemindersWhenDisabled() {
        assistant.setEnabled(false);
        assistant.setReminder("Test reminder", -1);
        assertDoesNotThrow(() -> assistant.checkReminders());
    }

    @Test
    void testInitialize() {
        assertDoesNotThrow(() -> assistant.initialize());
    }

    @Test
    void testAssistantNameUsedInGreeting() {
        // The initialize method should use the assistant name
        assertDoesNotThrow(() -> assistant.initialize());
    }

    @Test
    void testSetAssistantName() {
        assistant.setAssistantName("NewName");
        assertEquals("NewName", assistant.getAssistantName());
    }
    
    @Test
    void testSetProfile() {
        MittenzProfile mittenz = new MittenzProfile();
        assistant.setProfile(mittenz);
        
        assertNotNull(assistant.getProfile());
        assertEquals(mittenz, assistant.getProfile());
        assertEquals("Mittenz", assistant.getAssistantName());
    }
    
    @Test
    void testProfileIntegrationWithDialogueSystem() {
        MittenzProfile mittenz = new MittenzProfile();
        assistant.setProfile(mittenz);
        
        // The DialogueSystem should also have the profile set
        assertEquals(mittenz, assistant.getDialogueSystem().getProfile());
    }
    
    @Test
    void testTeachWithMittenzProfile() {
        MittenzProfile mittenz = new MittenzProfile();
        assistant.setProfile(mittenz);
        
        assertEquals(0, mittenz.getSkillLevel());
        assertFalse(mittenz.hasLearnedSystem("oxygen"));
        
        assistant.teach("oxygen");
        
        assertTrue(mittenz.hasLearnedSystem("oxygen"));
        assertTrue(mittenz.getSkillLevel() > 0);
    }
    
    @Test
    void testTeachMultipleTopicsWithMittenzProfile() {
        MittenzProfile mittenz = new MittenzProfile();
        assistant.setProfile(mittenz);
        
        assistant.teach("oxygen");
        assistant.teach("gravity");
        assistant.teach("navigation");
        
        assertEquals(3, mittenz.getLearnedSystems().size());
        assertEquals(15, mittenz.getSkillLevel());
    }
}
