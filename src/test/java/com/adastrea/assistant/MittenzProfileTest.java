package com.adastrea.assistant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class MittenzProfileTest {
    
    private MittenzProfile mittenz;
    
    @BeforeEach
    void setUp() {
        mittenz = new MittenzProfile();
    }
    
    @Test
    void testInitialState() {
        assertEquals("Mittenz", mittenz.getName());
        assertEquals(0, mittenz.getSkillLevel());
        assertTrue(mittenz.getLearnedSystems().isEmpty());
    }
    
    @Test
    void testBackstoryContainsKeyElements() {
        String backstory = mittenz.getBackstory();
        assertTrue(backstory.contains("not a traditional AI") || backstory.contains("digital"));
        assertTrue(backstory.contains("father") || backstory.contains("scientist"));
    }
    
    @Test
    void testPersonalityTraits() {
        List<String> traits = mittenz.getPersonalityTraits();
        assertFalse(traits.isEmpty());
        assertTrue(traits.size() >= 4);
    }
    
    @Test
    void testSpecialAbilities() {
        List<String> abilities = mittenz.getSpecialAbilities();
        assertFalse(abilities.isEmpty());
        assertTrue(mittenz.hasAbility("Human-like learning and adaptation"));
    }
    
    @Test
    void testGreetingChangesBySkillLevel() {
        // Test greetings at different combinations of relationship stage and skill level
        String greetingHostile = mittenz.getProfileGreeting();
        
        // Progress to CURIOUS stage
        mittenz.setRelationshipStage(RelationshipStage.CURIOUS);
        String greetingCuriousLow = mittenz.getProfileGreeting();
        
        // Increase skill level
        mittenz.increaseSkillLevel(40);
        String greetingCuriousMid = mittenz.getProfileGreeting();
        
        // Progress to COOPERATIVE stage
        mittenz.setRelationshipStage(RelationshipStage.COOPERATIVE);
        String greetingCoopMid = mittenz.getProfileGreeting();
        
        mittenz.increaseSkillLevel(60);
        String greetingCoopHigh = mittenz.getProfileGreeting();
        
        // Greetings should be different across stages and skill levels
        assertNotEquals(greetingHostile, greetingCuriousLow);
        assertNotEquals(greetingCuriousLow, greetingCoopMid);
        assertNotEquals(greetingCoopMid, greetingCoopHigh);
    }
    
    @Test
    void testCompanionDialoguesChangeBySkillLevel() {
        // Low skill level
        List<String> dialoguesLow = mittenz.getProfileCompanionDialogues();
        assertFalse(dialoguesLow.isEmpty());
        
        // Increase to mid skill level
        mittenz.increaseSkillLevel(40);
        List<String> dialoguesMid = mittenz.getProfileCompanionDialogues();
        assertTrue(dialoguesMid.size() >= dialoguesLow.size());
        
        // Increase to high skill level
        mittenz.increaseSkillLevel(40);
        List<String> dialoguesHigh = mittenz.getProfileCompanionDialogues();
        assertTrue(dialoguesHigh.size() >= dialoguesMid.size());
    }
    
    @Test
    void testLearnSystem() {
        assertEquals(0, mittenz.getSkillLevel());
        assertFalse(mittenz.hasLearnedSystem("oxygen"));
        
        mittenz.learnSystem("oxygen");
        
        assertTrue(mittenz.hasLearnedSystem("oxygen"));
        assertTrue(mittenz.getSkillLevel() > 0);
        assertEquals(1, mittenz.getLearnedSystems().size());
    }
    
    @Test
    void testLearnSystemMultipleTimes() {
        mittenz.learnSystem("oxygen");
        int skillAfterFirst = mittenz.getSkillLevel();
        
        mittenz.learnSystem("oxygen"); // Learning same system again
        int skillAfterSecond = mittenz.getSkillLevel();
        
        // Skill should not increase when learning the same system again
        assertEquals(skillAfterFirst, skillAfterSecond);
        assertEquals(1, mittenz.getLearnedSystems().size());
    }
    
    @Test
    void testIncreaseSkillLevel() {
        assertEquals(0, mittenz.getSkillLevel());
        
        mittenz.increaseSkillLevel(25);
        assertEquals(25, mittenz.getSkillLevel());
        
        mittenz.increaseSkillLevel(50);
        assertEquals(75, mittenz.getSkillLevel());
    }
    
    @Test
    void testSkillLevelCap() {
        mittenz.increaseSkillLevel(150); // Try to exceed cap
        assertEquals(100, mittenz.getSkillLevel()); // Should be capped at 100
    }
    
    @Test
    void testLearnMultipleSystems() {
        mittenz.learnSystem("oxygen");
        mittenz.learnSystem("gravity");
        mittenz.learnSystem("navigation");
        
        assertEquals(3, mittenz.getLearnedSystems().size());
        assertTrue(mittenz.hasLearnedSystem("oxygen"));
        assertTrue(mittenz.hasLearnedSystem("gravity"));
        assertTrue(mittenz.hasLearnedSystem("navigation"));
        assertEquals(15, mittenz.getSkillLevel()); // 5 per system
    }
    
    @Test
    void testGetMemoryFragment() {
        String memory = mittenz.getMemoryFragment();
        assertNotNull(memory);
        assertFalse(memory.isEmpty());
        // Memory should relate to her past
        assertTrue(memory.contains("father") || memory.contains("lab") || 
                   memory.contains("remember") || memory.contains("illness") ||
                   memory.contains("stars") || memory.contains("wanted") ||
                   memory.contains("researcher") || memory.contains("machines"));
    }
    
    @Test
    void testMemoryFragmentsVary() {
        // Verify that we can get all 8 unique memory fragments
        // This is more deterministic than relying on randomness
        java.util.Set<String> uniqueMemories = new java.util.HashSet<>();
        
        // Try up to 100 times to collect all 8 unique memories
        for (int i = 0; i < 100 && uniqueMemories.size() < 8; i++) {
            uniqueMemories.add(mittenz.getMemoryFragment());
        }
        
        // Should have collected all 8 unique memories
        assertEquals(8, uniqueMemories.size(), "Should have 8 unique memory fragments");
    }
}
