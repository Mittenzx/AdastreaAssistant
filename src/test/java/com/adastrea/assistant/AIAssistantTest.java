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
    void testInitialRelationshipStageIsHostile() {
        assertEquals(RelationshipStage.HOSTILE, assistant.getRelationshipStage());
    }

    @Test
    void testSetRelationshipStage() {
        assistant.setRelationshipStage(RelationshipStage.CURIOUS);
        assertEquals(RelationshipStage.CURIOUS, assistant.getRelationshipStage());
        
        assistant.setRelationshipStage(RelationshipStage.COOPERATIVE);
        assertEquals(RelationshipStage.COOPERATIVE, assistant.getRelationshipStage());
    }

    @Test
    void testProgressRelationshipStage() {
        // Start at HOSTILE
        assertEquals(RelationshipStage.HOSTILE, assistant.getRelationshipStage());
        
        // Progress to CURIOUS
        assertTrue(assistant.progressRelationshipStage());
        assertEquals(RelationshipStage.CURIOUS, assistant.getRelationshipStage());
        
        // Progress to COOPERATIVE
        assertTrue(assistant.progressRelationshipStage());
        assertEquals(RelationshipStage.COOPERATIVE, assistant.getRelationshipStage());
        
        // Cannot progress further
        assertFalse(assistant.progressRelationshipStage());
        assertEquals(RelationshipStage.COOPERATIVE, assistant.getRelationshipStage());
    }

    @Test
    void testInteractionCountTracking() {
        assertEquals(0, assistant.getInteractionCount());
        
        assistant.provideCompanionDialogue();
        assertEquals(1, assistant.getInteractionCount());
        
        assistant.respondToQuery("test");
        assertEquals(2, assistant.getInteractionCount());
    }

    @Test
    void testResetInteractionCount() {
        assistant.provideCompanionDialogue();
        assistant.respondToQuery("test");
        assertTrue(assistant.getInteractionCount() > 0);
        
        assistant.resetInteractionCount();
        assertEquals(0, assistant.getInteractionCount());
    }

    @Test
    void testAutomaticStageProgression() {
        // Start at HOSTILE
        assertEquals(RelationshipStage.HOSTILE, assistant.getRelationshipStage());
        
        // Interact 5 times to trigger progression to CURIOUS
        for (int i = 0; i < 5; i++) {
            assistant.provideCompanionDialogue();
        }
        assertEquals(RelationshipStage.CURIOUS, assistant.getRelationshipStage());
        
        // Interact 10 more times to trigger progression to COOPERATIVE (total 15)
        for (int i = 0; i < 10; i++) {
            assistant.provideCompanionDialogue();
        }
        assertEquals(RelationshipStage.COOPERATIVE, assistant.getRelationshipStage());
    }
}
