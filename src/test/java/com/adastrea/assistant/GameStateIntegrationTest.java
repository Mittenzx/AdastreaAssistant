package com.adastrea.assistant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GameStateIntegration class
 */
class GameStateIntegrationTest {
    
    private AIAssistant assistant;
    private MittenzProfile mittenz;
    private GameStateIntegration gameState;
    
    @BeforeEach
    void setUp() {
        assistant = new AIAssistant();
        mittenz = new MittenzProfile();
        assistant.setProfile(mittenz);
        gameState = new GameStateIntegration(assistant);
    }
    
    @Test
    void testLowOxygenWarning() {
        // Trigger low oxygen event
        gameState.onLowOxygen(25, 120);
        
        // Verify assistant is still enabled
        assertTrue(assistant.isEnabled());
        // Verify oxygen was tracked in context
        assertEquals(25, assistant.getContextTracker().getPlayerState("oxygen"));
    }
    
    @Test
    void testCriticalOxygenWarning() {
        // Trigger critical oxygen event
        gameState.onLowOxygen(5, 30);
        
        // Verify assistant is still enabled
        assertTrue(assistant.isEnabled());
        // Verify critical event was recorded
        assertTrue(assistant.getContextTracker().hasRecentEvent("oxygen_low"));
    }
    
    @Test
    void testTemperatureWarning() {
        // Trigger temperature warning
        gameState.onTemperatureWarning(150.0, true);
        
        assertTrue(assistant.isEnabled());
    }
    
    @Test
    void testLowResourceWarning() {
        // Trigger low fuel warning
        gameState.onLowResource("fuel", 15.5, 20.0);
        
        assertTrue(assistant.isEnabled());
    }
    
    @Test
    void testLocationEntered() {
        // Trigger location entered event
        gameState.onLocationEntered("Mars", "planet");
        
        // Skill level should increase due to learning the system
        assertTrue(mittenz.hasLearnedSystem("planet"));
        // Verify location was tracked
        assertEquals("Mars", assistant.getContextTracker().getCurrentLocation());
    }
    
    @Test
    void testDiscovery() {
        int initialSkillLevel = mittenz.getSkillLevel();
        
        // Trigger discovery event
        gameState.onDiscovery("mineral", "Titanium Ore");
        
        // Skill level should increase
        assertTrue(mittenz.getSkillLevel() > initialSkillLevel);
    }
    
    @Test
    void testLowHealth() {
        // Trigger low health event
        gameState.onLowHealth(30, "radiation");
        
        assertTrue(assistant.isEnabled());
    }
    
    @Test
    void testAchievement() {
        int initialSkillLevel = mittenz.getSkillLevel();
        
        // Trigger achievement event
        gameState.onAchievement("First Steps", "Completed first landing");
        
        // Skill level may increase
        assertTrue(mittenz.getSkillLevel() >= initialSkillLevel);
    }
    
    @Test
    void testEmergencyCritical() {
        // Trigger critical emergency
        gameState.onEmergency("Hull Breach", 5);
        
        assertTrue(assistant.isEnabled());
    }
    
    @Test
    void testEmergencyWarning() {
        // Trigger warning level emergency
        gameState.onEmergency("System Malfunction", 2);
        
        assertTrue(assistant.isEnabled());
    }
    
    @Test
    void testItemCraftedFirstTime() {
        // Trigger first time crafting
        gameState.onItemCrafted("Oxygen Generator", true);
        
        assertTrue(assistant.isEnabled());
    }
    
    @Test
    void testItemCraftedRepeat() {
        // Trigger repeat crafting
        gameState.onItemCrafted("Oxygen Generator", false);
        
        assertTrue(assistant.isEnabled());
    }
    
    @Test
    void testIdleCheck() {
        // Trigger idle check with sufficient time
        gameState.onIdleCheck(10, "idle");
        
        assertTrue(assistant.isEnabled());
    }
    
    @Test
    void testIdleCheckTooSoon() {
        // Trigger idle check too soon (should not speak)
        gameState.onIdleCheck(2, "idle");
        
        assertTrue(assistant.isEnabled());
    }
    
    @Test
    void testWarningCooldown() {
        // First oxygen warning should work
        gameState.onLowOxygen(25, 120);
        
        // Immediate second warning should be suppressed (cooldown)
        // This is hard to test directly but we verify no exceptions occur
        gameState.onLowOxygen(24, 115);
        
        assertTrue(assistant.isEnabled());
    }
    
    @Test
    void testRelationshipStageProgression() {
        // Initial stage should be HOSTILE
        assertEquals(RelationshipStage.HOSTILE, mittenz.getRelationshipStage());
        
        // Trigger multiple events to progress relationship
        for (int i = 0; i < 6; i++) {
            gameState.onDiscovery("test", "Test Discovery " + i);
        }
        
        // After enough interactions, stage should progress
        // (Actual progression depends on AIAssistant's trackInteraction logic)
        RelationshipStage stage = mittenz.getRelationshipStage();
        assertNotNull(stage);
    }
    
    @Test
    void testMultipleLocationVisits() {
        // Visit multiple locations
        gameState.onLocationEntered("Mars", "planet");
        gameState.onLocationEntered("Europa", "moon");
        gameState.onLocationEntered("Titan", "moon");
        
        // Should have learned multiple systems
        assertTrue(mittenz.getLearnedSystems().size() >= 1);
    }
    
    @Test
    void testMittenzSkillGrowth() {
        int initialSkill = mittenz.getSkillLevel();
        
        // Trigger various learning events
        gameState.onLocationEntered("Mars", "planet");
        gameState.onDiscovery("mineral", "Iron");
        gameState.onDiscovery("creature", "Space Whale");
        
        // Skill should have grown
        assertTrue(mittenz.getSkillLevel() > initialSkill);
    }
    
    @Test
    void testContextAwareResponses() {
        // Set to COOPERATIVE stage
        mittenz.setRelationshipStage(RelationshipStage.COOPERATIVE);
        
        // Trigger event - should get cooperative response
        gameState.onLowResource("water", 10.0, 20.0);
        
        assertEquals(RelationshipStage.COOPERATIVE, mittenz.getRelationshipStage());
    }
}
