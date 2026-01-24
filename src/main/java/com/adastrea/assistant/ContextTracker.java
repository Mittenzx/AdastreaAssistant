package com.adastrea.assistant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tracks conversation context and recent interactions to enable context-aware responses.
 * Maintains short-term memory of recent events and player state for more natural dialogue.
 */
public class ContextTracker {
    
    private static final int MAX_INTERACTION_HISTORY = 10;
    private static final int MAX_RECENT_EVENTS = 5;
    
    private final List<Interaction> interactionHistory;
    private final List<GameEvent> recentEvents;
    private final Map<String, Object> playerState;
    private String currentLocation;
    private String currentActivity;
    private long lastInteractionTime;
    
    public ContextTracker() {
        this.interactionHistory = new ArrayList<>();
        this.recentEvents = new ArrayList<>();
        this.playerState = new HashMap<>();
        this.lastInteractionTime = System.currentTimeMillis();
    }
    
    /**
     * Record a new interaction (player query or assistant response)
     */
    public void recordInteraction(String speaker, String message, InteractionType type) {
        Interaction interaction = new Interaction(speaker, message, type, System.currentTimeMillis());
        interactionHistory.add(interaction);
        
        // Keep only recent interactions
        while (interactionHistory.size() > MAX_INTERACTION_HISTORY) {
            interactionHistory.remove(0);
        }
        
        lastInteractionTime = System.currentTimeMillis();
    }
    
    /**
     * Record a game event that occurred
     */
    public void recordEvent(String eventType, String description, EventSeverity severity) {
        GameEvent event = new GameEvent(eventType, description, severity, System.currentTimeMillis());
        recentEvents.add(event);
        
        // Keep only recent events
        while (recentEvents.size() > MAX_RECENT_EVENTS) {
            recentEvents.remove(0);
        }
    }
    
    /**
     * Update player state information
     */
    public void updatePlayerState(String key, Object value) {
        playerState.put(key, value);
    }
    
    /**
     * Get player state value
     */
    public Object getPlayerState(String key) {
        return playerState.get(key);
    }
    
    /**
     * Set current location
     */
    public void setCurrentLocation(String location) {
        this.currentLocation = location;
    }
    
    /**
     * Get current location
     */
    public String getCurrentLocation() {
        return currentLocation;
    }
    
    /**
     * Set current activity
     */
    public void setCurrentActivity(String activity) {
        this.currentActivity = activity;
    }
    
    /**
     * Get current activity
     */
    public String getCurrentActivity() {
        return currentActivity;
    }
    
    /**
     * Get minutes since last interaction
     */
    public int getMinutesSinceLastInteraction() {
        long timeDiff = System.currentTimeMillis() - lastInteractionTime;
        return (int) (timeDiff / 60000); // Convert milliseconds to minutes
    }
    
    /**
     * Get recent interaction history
     */
    public List<Interaction> getInteractionHistory() {
        return new ArrayList<>(interactionHistory);
    }
    
    /**
     * Get recent game events
     */
    public List<GameEvent> getRecentEvents() {
        return new ArrayList<>(recentEvents);
    }
    
    /**
     * Check if a specific topic was recently discussed
     */
    public boolean wasRecentlyDiscussed(String topic) {
        String lowerTopic = topic.toLowerCase();
        for (Interaction interaction : interactionHistory) {
            if (interaction.getMessage().toLowerCase().contains(lowerTopic)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Check if a specific event type occurred recently
     */
    public boolean hasRecentEvent(String eventType) {
        for (GameEvent event : recentEvents) {
            if (event.getEventType().equalsIgnoreCase(eventType)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Get the most recent event of a specific type
     */
    public GameEvent getMostRecentEvent(String eventType) {
        for (int i = recentEvents.size() - 1; i >= 0; i--) {
            GameEvent event = recentEvents.get(i);
            if (event.getEventType().equalsIgnoreCase(eventType)) {
                return event;
            }
        }
        return null;
    }
    
    /**
     * Check if there are any recent high-severity events
     */
    public boolean hasRecentEmergency() {
        for (GameEvent event : recentEvents) {
            if (event.getSeverity() == EventSeverity.CRITICAL || 
                event.getSeverity() == EventSeverity.HIGH) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Get context summary for dialogue selection
     */
    public ContextSummary getContextSummary() {
        return new ContextSummary(
            currentLocation,
            currentActivity,
            getMinutesSinceLastInteraction(),
            hasRecentEmergency(),
            interactionHistory.size(),
            new HashMap<>(playerState)
        );
    }
    
    /**
     * Clear all context (for new game session or reset)
     */
    public void clear() {
        interactionHistory.clear();
        recentEvents.clear();
        playerState.clear();
        currentLocation = null;
        currentActivity = null;
        lastInteractionTime = System.currentTimeMillis();
    }
    
    /**
     * Represents a single interaction in the conversation
     */
    public static class Interaction {
        private final String speaker;
        private final String message;
        private final InteractionType type;
        private final long timestamp;
        
        public Interaction(String speaker, String message, InteractionType type, long timestamp) {
            this.speaker = speaker;
            this.message = message;
            this.type = type;
            this.timestamp = timestamp;
        }
        
        public String getSpeaker() { return speaker; }
        public String getMessage() { return message; }
        public InteractionType getType() { return type; }
        public long getTimestamp() { return timestamp; }
    }
    
    /**
     * Represents a game event that occurred
     */
    public static class GameEvent {
        private final String eventType;
        private final String description;
        private final EventSeverity severity;
        private final long timestamp;
        
        public GameEvent(String eventType, String description, EventSeverity severity, long timestamp) {
            this.eventType = eventType;
            this.description = description;
            this.severity = severity;
            this.timestamp = timestamp;
        }
        
        public String getEventType() { return eventType; }
        public String getDescription() { return description; }
        public EventSeverity getSeverity() { return severity; }
        public long getTimestamp() { return timestamp; }
    }
    
    /**
     * Summary of current context for decision making
     */
    public static class ContextSummary {
        private final String location;
        private final String activity;
        private final int minutesSinceLastInteraction;
        private final boolean hasEmergency;
        private final int interactionCount;
        private final Map<String, Object> playerState;
        
        public ContextSummary(String location, String activity, int minutesSinceLastInteraction,
                            boolean hasEmergency, int interactionCount, Map<String, Object> playerState) {
            this.location = location;
            this.activity = activity;
            this.minutesSinceLastInteraction = minutesSinceLastInteraction;
            this.hasEmergency = hasEmergency;
            this.interactionCount = interactionCount;
            this.playerState = playerState;
        }
        
        public String getLocation() { return location; }
        public String getActivity() { return activity; }
        public int getMinutesSinceLastInteraction() { return minutesSinceLastInteraction; }
        public boolean hasEmergency() { return hasEmergency; }
        public int getInteractionCount() { return interactionCount; }
        public Map<String, Object> getPlayerState() { return new HashMap<>(playerState); }
    }
    
    /**
     * Types of interactions
     */
    public enum InteractionType {
        PLAYER_QUERY,
        ASSISTANT_RESPONSE,
        GAME_EVENT,
        SYSTEM_MESSAGE
    }
    
    /**
     * Event severity levels
     */
    public enum EventSeverity {
        INFO,
        LOW,
        MEDIUM,
        HIGH,
        CRITICAL
    }
}
