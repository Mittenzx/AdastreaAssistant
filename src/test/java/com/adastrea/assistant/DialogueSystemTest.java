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
