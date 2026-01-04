package com.adastrea.assistant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AssistantProfileTest {
    
    private AssistantProfile profile;
    
    @BeforeEach
    void setUp() {
        // Use MittenzProfile as concrete implementation for testing
        profile = new MittenzProfile();
    }
    
    @Test
    void testGetName() {
        assertNotNull(profile.getName());
        assertFalse(profile.getName().isEmpty());
    }
    
    @Test
    void testGetBackstory() {
        String backstory = profile.getBackstory();
        assertNotNull(backstory);
        assertFalse(backstory.isEmpty());
    }
    
    @Test
    void testGetPersonalityTraits() {
        assertNotNull(profile.getPersonalityTraits());
        assertFalse(profile.getPersonalityTraits().isEmpty());
    }
    
    @Test
    void testGetSpecialAbilities() {
        assertNotNull(profile.getSpecialAbilities());
        assertFalse(profile.getSpecialAbilities().isEmpty());
    }
    
    @Test
    void testGetProfileGreeting() {
        String greeting = profile.getProfileGreeting();
        assertNotNull(greeting);
        assertFalse(greeting.isEmpty());
    }
    
    @Test
    void testGetProfileCompanionDialogues() {
        assertNotNull(profile.getProfileCompanionDialogues());
        assertFalse(profile.getProfileCompanionDialogues().isEmpty());
    }
    
    @Test
    void testHasAbility() {
        // MittenzProfile should have human-like learning ability
        assertTrue(profile.hasAbility("Human-like learning and adaptation"));
    }
}
