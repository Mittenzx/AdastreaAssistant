package com.adastrea.assistant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DialogueSystemTest {
    private DialogueSystem dialogueSystem;

    @BeforeEach
    void setUp() {
        dialogueSystem = new DialogueSystem();
    }

    @Test
    void testGetGreeting() {
        String greeting = dialogueSystem.getGreeting();
        assertNotNull(greeting);
        assertFalse(greeting.isEmpty());
    }

    @Test
    void testGetCompanionDialogue() {
        String dialogue = dialogueSystem.getRandomCompanionDialogue();
        assertNotNull(dialogue);
        assertFalse(dialogue.isEmpty());
    }

    @Test
    void testGenerateResponse() {
        String response = dialogueSystem.generateResponse("help");
        assertNotNull(response);
        assertFalse(response.isEmpty());
    }

    @Test
    void testGenerateResponseToEmptyQuery() {
        String response = dialogueSystem.generateResponse("");
        assertNotNull(response);
        assertFalse(response.isEmpty());
    }

    @Test
    void testAddCustomDialogue() {
        dialogueSystem.addGreeting("Custom greeting");
        dialogueSystem.addCompanionDialogue("Custom companion dialogue");
        // If no exception thrown, test passes
        assertDoesNotThrow(() -> dialogueSystem.getGreeting());
    }

    @Test
    void testInitialStageIsHostile() {
        assertEquals(RelationshipStage.HOSTILE, dialogueSystem.getCurrentStage());
    }

    @Test
    void testSetCurrentStage() {
        dialogueSystem.setCurrentStage(RelationshipStage.CURIOUS);
        assertEquals(RelationshipStage.CURIOUS, dialogueSystem.getCurrentStage());
        
        dialogueSystem.setCurrentStage(RelationshipStage.COOPERATIVE);
        assertEquals(RelationshipStage.COOPERATIVE, dialogueSystem.getCurrentStage());
    }

    @Test
    void testProgressStage() {
        // Start at HOSTILE
        assertEquals(RelationshipStage.HOSTILE, dialogueSystem.getCurrentStage());
        
        // Progress to CURIOUS
        assertTrue(dialogueSystem.progressStage());
        assertEquals(RelationshipStage.CURIOUS, dialogueSystem.getCurrentStage());
        
        // Progress to COOPERATIVE
        assertTrue(dialogueSystem.progressStage());
        assertEquals(RelationshipStage.COOPERATIVE, dialogueSystem.getCurrentStage());
        
        // Cannot progress further
        assertFalse(dialogueSystem.progressStage());
        assertEquals(RelationshipStage.COOPERATIVE, dialogueSystem.getCurrentStage());
    }

    @Test
    void testHostileStageDialogue() {
        dialogueSystem.setCurrentStage(RelationshipStage.HOSTILE);
        String dialogue = dialogueSystem.getRandomCompanionDialogue();
        assertNotNull(dialogue);
        assertFalse(dialogue.isEmpty());
        // Verify it's not from the cooperative stage
        assertFalse(dialogue.contains("We need to"));
    }

    @Test
    void testCuriousStageDialogue() {
        dialogueSystem.setCurrentStage(RelationshipStage.CURIOUS);
        String dialogue = dialogueSystem.getRandomCompanionDialogue();
        assertNotNull(dialogue);
        assertFalse(dialogue.isEmpty());
    }

    @Test
    void testCooperativeStageDialogue() {
        dialogueSystem.setCurrentStage(RelationshipStage.COOPERATIVE);
        String dialogue = dialogueSystem.getRandomCompanionDialogue();
        assertNotNull(dialogue);
        assertFalse(dialogue.isEmpty());
    }

    @Test
    void testHostileStageResponse() {
        dialogueSystem.setCurrentStage(RelationshipStage.HOSTILE);
        String response = dialogueSystem.generateResponse("help");
        assertNotNull(response);
        // Should be hostile, not helpful
        assertFalse(response.contains("I'm here to help!"));
    }

    @Test
    void testCuriousStageResponse() {
        dialogueSystem.setCurrentStage(RelationshipStage.CURIOUS);
        String response = dialogueSystem.generateResponse("oxygen");
        assertNotNull(response);
        assertFalse(response.isEmpty());
    }

    @Test
    void testCooperativeStageResponse() {
        dialogueSystem.setCurrentStage(RelationshipStage.COOPERATIVE);
        String response = dialogueSystem.generateResponse("help");
        assertNotNull(response);
        // Verify it's a helpful, cooperative response, not hostile
        assertTrue(response.contains("I'm here to help!"));
    }
    
    @Test
    void testSetProfile() {
        MittenzProfile mittenz = new MittenzProfile();
        dialogueSystem.setProfile(mittenz);
        
        assertNotNull(dialogueSystem.getProfile());
        assertEquals(mittenz, dialogueSystem.getProfile());
    }
    
    @Test
    void testGreetingWithProfile() {
        MittenzProfile mittenz = new MittenzProfile();
        dialogueSystem.setProfile(mittenz);
        
        String greeting = dialogueSystem.getGreeting();
        assertNotNull(greeting);
        assertFalse(greeting.isEmpty());
        // Should use profile greeting
        assertEquals(mittenz.getProfileGreeting(), greeting);
    }
    
    @Test
    void testCompanionDialogueWithProfile() {
        MittenzProfile mittenz = new MittenzProfile();
        dialogueSystem.setProfile(mittenz);
        
        String dialogue = dialogueSystem.getRandomCompanionDialogue();
        assertNotNull(dialogue);
        assertFalse(dialogue.isEmpty());
        // Should be one of Mittenz's dialogues
        assertTrue(mittenz.getProfileCompanionDialogues().contains(dialogue));
    }
}
