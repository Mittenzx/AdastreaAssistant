package com.adastrea.assistant;

/**
 * Interface for listening to game state changes and events.
 * Implement this interface to receive notifications about important game events
 * that should trigger assistant responses.
 */
public interface GameStateListener {
    
    /**
     * Called when player's oxygen level drops below a threshold
     * @param oxygenLevel Current oxygen level (0-100)
     * @param timeRemaining Estimated seconds until oxygen depletion
     */
    void onLowOxygen(int oxygenLevel, int timeRemaining);
    
    /**
     * Called when player's temperature reaches dangerous levels
     * @param temperature Current temperature
     * @param isDangerous True if temperature is life-threatening
     */
    void onTemperatureWarning(double temperature, boolean isDangerous);
    
    /**
     * Called when a resource reaches low levels
     * @param resourceType Type of resource (fuel, water, food, etc.)
     * @param amount Current amount remaining
     * @param threshold The threshold that triggered this warning
     */
    void onLowResource(String resourceType, double amount, double threshold);
    
    /**
     * Called when player enters a new location or region
     * @param locationName Name of the location
     * @param locationType Type of location (planet, station, asteroid, etc.)
     */
    void onLocationEntered(String locationName, String locationType);
    
    /**
     * Called when player discovers something new
     * @param discoveryType Type of discovery (planet, resource, creature, etc.)
     * @param discoveryName Name of what was discovered
     */
    void onDiscovery(String discoveryType, String discoveryName);
    
    /**
     * Called when player's health drops below threshold
     * @param healthLevel Current health (0-100)
     * @param damageSource What caused the damage
     */
    void onLowHealth(int healthLevel, String damageSource);
    
    /**
     * Called when player completes a milestone or achievement
     * @param achievementName Name of the achievement
     * @param description Description of what was accomplished
     */
    void onAchievement(String achievementName, String description);
    
    /**
     * Called when an emergency situation occurs
     * @param emergencyType Type of emergency (hull breach, system failure, etc.)
     * @param severity Severity level (1-5, 5 being critical)
     */
    void onEmergency(String emergencyType, int severity);
    
    /**
     * Called when player crafts or builds something
     * @param itemName Name of the item created
     * @param isFirstTime True if this is the first time crafting this item
     */
    void onItemCrafted(String itemName, boolean isFirstTime);
    
    /**
     * Called periodically to check if assistant should provide idle dialogue
     * @param minutesSinceLastInteraction Minutes since last player interaction
     * @param currentActivity What the player is currently doing
     */
    void onIdleCheck(int minutesSinceLastInteraction, String currentActivity);
}
