package com.adastrea.assistant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ContextTracker class
 */
class ContextTrackerTest {
    
    private ContextTracker tracker;
    
    @BeforeEach
    void setUp() {
        tracker = new ContextTracker();
    }
    
    @Test
    void testRecordInteraction() {
        tracker.recordInteraction("Mittenz", "Hello!", ContextTracker.InteractionType.ASSISTANT_RESPONSE);
        
        assertEquals(1, tracker.getInteractionHistory().size());
        assertEquals("Mittenz", tracker.getInteractionHistory().get(0).getSpeaker());
        assertEquals("Hello!", tracker.getInteractionHistory().get(0).getMessage());
    }
    
    @Test
    void testInteractionHistoryLimit() {
        // Add more than max interactions
        for (int i = 0; i < 15; i++) {
            tracker.recordInteraction("Player", "Message " + i, ContextTracker.InteractionType.PLAYER_QUERY);
        }
        
        // Should only keep last 10
        assertEquals(10, tracker.getInteractionHistory().size());
        assertEquals("Message 14", tracker.getInteractionHistory().get(9).getMessage());
    }
    
    @Test
    void testRecordEvent() {
        tracker.recordEvent("oxygen_low", "Oxygen at 25%", ContextTracker.EventSeverity.HIGH);
        
        assertEquals(1, tracker.getRecentEvents().size());
        assertEquals("oxygen_low", tracker.getRecentEvents().get(0).getEventType());
    }
    
    @Test
    void testEventHistoryLimit() {
        // Add more than max events
        for (int i = 0; i < 8; i++) {
            tracker.recordEvent("test", "Event " + i, ContextTracker.EventSeverity.INFO);
        }
        
        // Should only keep last 5
        assertEquals(5, tracker.getRecentEvents().size());
    }
    
    @Test
    void testUpdatePlayerState() {
        tracker.updatePlayerState("oxygen", 75);
        tracker.updatePlayerState("health", 90);
        
        assertEquals(75, tracker.getPlayerState("oxygen"));
        assertEquals(90, tracker.getPlayerState("health"));
    }
    
    @Test
    void testSetAndGetLocation() {
        tracker.setCurrentLocation("Mars");
        assertEquals("Mars", tracker.getCurrentLocation());
    }
    
    @Test
    void testSetAndGetActivity() {
        tracker.setCurrentActivity("exploring");
        assertEquals("exploring", tracker.getCurrentActivity());
    }
    
    @Test
    void testMinutesSinceLastInteraction() {
        int minutes = tracker.getMinutesSinceLastInteraction();
        assertTrue(minutes >= 0);
        
        // Record an interaction to reset the timer
        tracker.recordInteraction("Player", "Test", ContextTracker.InteractionType.PLAYER_QUERY);
        assertEquals(0, tracker.getMinutesSinceLastInteraction());
    }
    
    @Test
    void testWasRecentlyDiscussed() {
        tracker.recordInteraction("Player", "Tell me about oxygen", ContextTracker.InteractionType.PLAYER_QUERY);
        
        assertTrue(tracker.wasRecentlyDiscussed("oxygen"));
        assertFalse(tracker.wasRecentlyDiscussed("fuel"));
    }
    
    @Test
    void testWasRecentlyDiscussedCaseInsensitive() {
        tracker.recordInteraction("Player", "Tell me about OXYGEN", ContextTracker.InteractionType.PLAYER_QUERY);
        
        assertTrue(tracker.wasRecentlyDiscussed("oxygen"));
        assertTrue(tracker.wasRecentlyDiscussed("Oxygen"));
        assertTrue(tracker.wasRecentlyDiscussed("OXYGEN"));
    }
    
    @Test
    void testHasRecentEvent() {
        tracker.recordEvent("oxygen_low", "Low oxygen", ContextTracker.EventSeverity.HIGH);
        
        assertTrue(tracker.hasRecentEvent("oxygen_low"));
        assertFalse(tracker.hasRecentEvent("fuel_low"));
    }
    
    @Test
    void testGetMostRecentEvent() {
        tracker.recordEvent("oxygen_low", "First", ContextTracker.EventSeverity.HIGH);
        tracker.recordEvent("fuel_low", "Second", ContextTracker.EventSeverity.MEDIUM);
        tracker.recordEvent("oxygen_low", "Third", ContextTracker.EventSeverity.CRITICAL);
        
        ContextTracker.GameEvent event = tracker.getMostRecentEvent("oxygen_low");
        assertNotNull(event);
        assertEquals("Third", event.getDescription());
    }
    
    @Test
    void testGetMostRecentEventNotFound() {
        ContextTracker.GameEvent event = tracker.getMostRecentEvent("nonexistent");
        assertNull(event);
    }
    
    @Test
    void testHasRecentEmergency() {
        assertFalse(tracker.hasRecentEmergency());
        
        tracker.recordEvent("test", "Info event", ContextTracker.EventSeverity.INFO);
        assertFalse(tracker.hasRecentEmergency());
        
        tracker.recordEvent("emergency", "Critical!", ContextTracker.EventSeverity.CRITICAL);
        assertTrue(tracker.hasRecentEmergency());
    }
    
    @Test
    void testGetContextSummary() {
        tracker.setCurrentLocation("Mars");
        tracker.setCurrentActivity("exploring");
        tracker.updatePlayerState("oxygen", 75);
        tracker.recordInteraction("Player", "Hello", ContextTracker.InteractionType.PLAYER_QUERY);
        
        ContextTracker.ContextSummary summary = tracker.getContextSummary();
        
        assertEquals("Mars", summary.getLocation());
        assertEquals("exploring", summary.getActivity());
        assertEquals(1, summary.getInteractionCount());
        assertFalse(summary.hasEmergency());
        assertEquals(75, summary.getPlayerState().get("oxygen"));
    }
    
    @Test
    void testClear() {
        // Add various data
        tracker.recordInteraction("Player", "Test", ContextTracker.InteractionType.PLAYER_QUERY);
        tracker.recordEvent("test", "Test", ContextTracker.EventSeverity.INFO);
        tracker.updatePlayerState("oxygen", 75);
        tracker.setCurrentLocation("Mars");
        tracker.setCurrentActivity("exploring");
        
        // Clear
        tracker.clear();
        
        // Verify everything is cleared
        assertEquals(0, tracker.getInteractionHistory().size());
        assertEquals(0, tracker.getRecentEvents().size());
        assertNull(tracker.getCurrentLocation());
        assertNull(tracker.getCurrentActivity());
        assertNull(tracker.getPlayerState("oxygen"));
    }
    
    @Test
    void testInteractionTypes() {
        tracker.recordInteraction("Player", "Query", ContextTracker.InteractionType.PLAYER_QUERY);
        tracker.recordInteraction("Mittenz", "Response", ContextTracker.InteractionType.ASSISTANT_RESPONSE);
        tracker.recordInteraction("System", "Event", ContextTracker.InteractionType.GAME_EVENT);
        
        assertEquals(3, tracker.getInteractionHistory().size());
        assertEquals(ContextTracker.InteractionType.PLAYER_QUERY, 
                    tracker.getInteractionHistory().get(0).getType());
        assertEquals(ContextTracker.InteractionType.ASSISTANT_RESPONSE, 
                    tracker.getInteractionHistory().get(1).getType());
        assertEquals(ContextTracker.InteractionType.GAME_EVENT, 
                    tracker.getInteractionHistory().get(2).getType());
    }
    
    @Test
    void testEventSeverities() {
        tracker.recordEvent("info", "Info", ContextTracker.EventSeverity.INFO);
        tracker.recordEvent("low", "Low", ContextTracker.EventSeverity.LOW);
        tracker.recordEvent("medium", "Medium", ContextTracker.EventSeverity.MEDIUM);
        tracker.recordEvent("high", "High", ContextTracker.EventSeverity.HIGH);
        tracker.recordEvent("critical", "Critical", ContextTracker.EventSeverity.CRITICAL);
        
        assertEquals(5, tracker.getRecentEvents().size());
        
        // Only HIGH and CRITICAL should count as emergency
        assertTrue(tracker.hasRecentEmergency());
    }
    
    @Test
    void testContextSummaryWithEmergency() {
        tracker.recordEvent("emergency", "Test", ContextTracker.EventSeverity.CRITICAL);
        
        ContextTracker.ContextSummary summary = tracker.getContextSummary();
        assertTrue(summary.hasEmergency());
    }
    
    @Test
    void testMultiplePlayerStates() {
        tracker.updatePlayerState("oxygen", 75);
        tracker.updatePlayerState("health", 90);
        tracker.updatePlayerState("fuel", 50.5);
        tracker.updatePlayerState("position", "Mars Base");
        
        ContextTracker.ContextSummary summary = tracker.getContextSummary();
        Map<String, Object> states = summary.getPlayerState();
        
        assertEquals(4, states.size());
        assertEquals(75, states.get("oxygen"));
        assertEquals(90, states.get("health"));
        assertEquals(50.5, states.get("fuel"));
        assertEquals("Mars Base", states.get("position"));
    }
}
