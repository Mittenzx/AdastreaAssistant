# Game State Integration Guide

This guide explains how to integrate the AdastreaAssistant with your Minecraft mod or game to enable context-aware AI companion interactions.

## Overview

The Game State Integration system allows your game to communicate events to the AI Assistant (Mittenz), which responds contextually based on:
- **Relationship Stage**: Hostile → Curious → Cooperative
- **Skill Level**: 0-100 (increases as Mittenz learns)
- **Event Type and Severity**: Different events trigger different responses

## Quick Start

### 1. Basic Setup

```java
// Create AI Assistant with Mittenz profile
AIAssistant assistant = new AIAssistant();
MittenzProfile mittenz = new MittenzProfile();
assistant.setProfile(mittenz);

// Create game state integration layer
GameStateIntegration gameState = new GameStateIntegration(assistant);

// Initialize
assistant.initialize();
```

### 2. Trigger Events

Call the appropriate `GameStateListener` method when game events occur:

```java
// When player enters a new location
gameState.onLocationEntered("Mars", "planet");

// When oxygen drops
gameState.onLowOxygen(25, 120); // 25% oxygen, 120 seconds remaining

// When player discovers something
gameState.onDiscovery("mineral", "Titanium Ore");

// When resources run low
gameState.onLowResource("fuel", 15.5, 20.0);
```

## Event Types

### Critical Events (Immediate Response)

#### Low Oxygen
```java
gameState.onLowOxygen(int oxygenLevel, int timeRemaining);
```
- **oxygenLevel**: 0-100 (percentage)
- **timeRemaining**: Seconds until depletion
- **Response**: Urgent warning if < 10%, concerned if < 30%

#### Emergency
```java
gameState.onEmergency(String emergencyType, int severity);
```
- **emergencyType**: "Hull Breach", "System Failure", etc.
- **severity**: 1-5 (5 = critical)
- **Response**: Escalates message based on severity

#### Low Health
```java
gameState.onLowHealth(int healthLevel, String damageSource);
```
- **healthLevel**: 0-100 (percentage)
- **damageSource**: What caused damage (e.g., "radiation")
- **Response**: Urgent if < 20%, cautionary if < 50%

### Informational Events

#### Location Entered
```java
gameState.onLocationEntered(String locationName, String locationType);
```
- **locationName**: "Mars", "Europa Station", etc.
- **locationType**: "planet", "station", "asteroid", etc.
- **Effect**: Mittenz learns the location type, skill increases

#### Discovery
```java
gameState.onDiscovery(String discoveryType, String discoveryName);
```
- **discoveryType**: "planet", "mineral", "creature", etc.
- **discoveryName**: Specific name of what was found
- **Effect**: Skill level increases by 2

#### Achievement
```java
gameState.onAchievement(String achievementName, String description);
```
- **achievementName**: "First Steps", "Master Explorer", etc.
- **description**: What was accomplished
- **Effect**: Skill level increases, relationship may progress

### Resource Events

#### Low Resource
```java
gameState.onLowResource(String resourceType, double amount, double threshold);
```
- **resourceType**: "fuel", "water", "food", etc.
- **amount**: Current amount remaining
- **threshold**: Warning threshold
- **Response**: Contextual advice based on relationship stage

#### Temperature Warning
```java
gameState.onTemperatureWarning(double temperature, boolean isDangerous);
```
- **temperature**: Current temperature value
- **isDangerous**: True if life-threatening
- **Response**: Urgent if dangerous, informational otherwise

### Crafting Events

#### Item Crafted
```java
gameState.onItemCrafted(String itemName, boolean isFirstTime);
```
- **itemName**: Name of crafted item
- **isFirstTime**: True if first time crafting this item
- **Effect**: If first time, triggers teaching about crafting

### Idle Monitoring

#### Idle Check
```java
gameState.onIdleCheck(int minutesSinceLastInteraction, String currentActivity);
```
- **minutesSinceLastInteraction**: Minutes since player's last action
- **currentActivity**: What player is currently doing
- **Effect**: Provides companion dialogue if >= 5 minutes

## Response Behavior

### Relationship Stages

Mittenz's responses change based on her relationship stage:

#### Hostile (Initial)
- Bratty, confused, demanding
- Examples:
  - "Where are you taking me now?"
  - "Can I go home now?"
  - "So what?"

#### Curious (After 5 interactions)
- Questioning, intrigued, learning
- Examples:
  - "Wow! I've never seen this before!"
  - "How does that work?"
  - "Interesting..."

#### Cooperative (After 10+ interactions)
- Helpful, engaged, team-oriented
- Examples:
  - "Let me scan that for you."
  - "Should we look for more?"
  - "We make a good team!"

### Skill Level Impact

Mittenz's skill level (0-100) affects her confidence and capability:

- **0-20**: Inexperienced, uncertain
- **20-50**: Growing confidence, learning
- **50-80**: Proficient, quick processing
- **80-100**: Expert, systems are second nature

Skill increases through:
- Each interaction: +1
- Learning new systems: +5
- Discoveries: +2
- Teaching lessons: +5

## Warning Cooldown System

To prevent spam, warnings have a 30-second cooldown per type:

```java
// First warning fires
gameState.onLowOxygen(25, 120);

// Immediate second warning is suppressed
gameState.onLowOxygen(24, 115); // No output

// After 30 seconds, warnings can fire again
```

## Integration Patterns

### Pattern 1: Event-Driven Integration

```java
public class ModEventHandler {
    private GameStateIntegration gameState;
    
    public ModEventHandler(GameStateIntegration gameState) {
        this.gameState = gameState;
    }
    
    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent event) {
        Player player = event.player;
        
        // Check oxygen
        if (player.getOxygen() < 30) {
            int timeRemaining = calculateTimeRemaining(player.getOxygen());
            gameState.onLowOxygen(player.getOxygen(), timeRemaining);
        }
        
        // Check health
        if (player.getHealth() < 50) {
            gameState.onLowHealth(
                (int)(player.getHealth() / player.getMaxHealth() * 100),
                player.getLastDamageSource()
            );
        }
    }
    
    @SubscribeEvent
    public void onBlockMined(BlockMinedEvent event) {
        if (isFirstTimeDiscovery(event.block)) {
            gameState.onDiscovery("mineral", event.block.getName());
        }
    }
}
```

### Pattern 2: Polling Integration

```java
public class ModTickHandler {
    private GameStateIntegration gameState;
    private long lastCheck = 0;
    
    public void onServerTick() {
        long now = System.currentTimeMillis();
        
        if (now - lastCheck > 5000) { // Check every 5 seconds
            checkGameState();
            lastCheck = now;
        }
    }
    
    private void checkGameState() {
        Player player = getPlayer();
        
        // Check resources
        if (player.getFuel() < 20) {
            gameState.onLowResource("fuel", player.getFuel(), 20);
        }
        
        // Check idle time
        int minutesIdle = getPlayerIdleMinutes();
        gameState.onIdleCheck(minutesIdle, player.getCurrentActivity());
    }
}
```

### Pattern 3: Achievement Integration

```java
public class AchievementSystem {
    private GameStateIntegration gameState;
    
    public void unlockAchievement(String name, String description) {
        // Your achievement logic
        saveAchievement(name);
        showAchievementUI(name);
        
        // Notify assistant
        gameState.onAchievement(name, description);
    }
}
```

## Best Practices

### 1. Event Timing
- Call events when they actually occur, not speculatively
- Use the cooldown system to prevent spam
- Batch similar events if appropriate

### 2. Severity Levels
- Reserve severity 5 for truly critical situations
- Use severity 3-4 for important but not critical events
- Use severity 1-2 for informational warnings

### 3. Resource Management
- Only trigger warnings at meaningful thresholds
- Provide realistic time estimates for depletion
- Consider combining similar warnings

### 4. Performance
- Don't call integration methods every frame
- Use event-driven patterns when possible
- Cache assistant instance

### 5. Testing
```java
// Create test scenarios
@Test
public void testOxygenWarningIntegration() {
    GameStateIntegration gameState = createGameState();
    
    // Simulate oxygen drop
    gameState.onLowOxygen(15, 60);
    
    // Verify assistant responded
    assertTrue(assistantSpoke());
}
```

## Example: Complete Mod Integration

```java
public class AdastreaModIntegration {
    private AIAssistant assistant;
    private GameStateIntegration gameState;
    
    public void initialize() {
        // Setup assistant
        assistant = new AIAssistant();
        MittenzProfile mittenz = new MittenzProfile();
        assistant.setProfile(mittenz);
        
        // Setup integration
        gameState = new GameStateIntegration(assistant);
        
        // Initialize
        assistant.initialize();
        
        // Register with mod event bus
        ModEventBus.register(this);
    }
    
    @SubscribeEvent
    public void onPlayerJoinWorld(PlayerJoinWorldEvent event) {
        String worldName = event.world.getName();
        String worldType = event.world.getType();
        gameState.onLocationEntered(worldName, worldType);
    }
    
    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent event) {
        checkPlayerVitals(event.player);
        checkPlayerResources(event.player);
    }
    
    private void checkPlayerVitals(Player player) {
        // Oxygen
        if (player.hasOxygenSystem() && player.getOxygen() < 30) {
            gameState.onLowOxygen(
                player.getOxygen(),
                player.getOxygenTimeRemaining()
            );
        }
        
        // Health
        if (player.getHealthPercent() < 50) {
            gameState.onLowHealth(
                player.getHealthPercent(),
                player.getLastDamageSource()
            );
        }
        
        // Temperature
        double temp = player.getEnvironmentTemperature();
        if (temp < -50 || temp > 100) {
            gameState.onTemperatureWarning(temp, temp < -100 || temp > 150);
        }
    }
    
    private void checkPlayerResources(Player player) {
        // Check all resource types
        for (String resourceType : player.getResourceTypes()) {
            double amount = player.getResourceAmount(resourceType);
            double threshold = player.getResourceThreshold(resourceType);
            
            if (amount < threshold) {
                gameState.onLowResource(resourceType, amount, threshold);
            }
        }
    }
    
    public void onShutdown() {
        // Save Mittenz's progress
        saveMittenzProgress();
    }
    
    private void saveMittenzProgress() {
        MittenzProfile mittenz = (MittenzProfile) assistant.getProfile();
        
        // Save to config or data file
        config.set("mittenz.skillLevel", mittenz.getSkillLevel());
        config.set("mittenz.relationshipStage", mittenz.getRelationshipStage());
        config.set("mittenz.learnedSystems", mittenz.getLearnedSystems());
        config.save();
    }
}
```

## Troubleshooting

### Assistant Not Responding
- Check `assistant.isEnabled()` is true
- Verify events are being triggered
- Check cooldown hasn't suppressed the event

### Repetitive Responses
- Cooldown system should handle this automatically
- Check if events are being triggered too frequently
- Verify cooldown duration (30 seconds default)

### Wrong Relationship Stage
- Check interaction count
- Verify `trackInteraction()` is being called (happens automatically via speak())
- Stage progression: Hostile (0-4) → Curious (5-14) → Cooperative (15+)

### Skill Not Increasing
- Discoveries add +2 skill
- Learning systems adds +5 skill
- Regular interactions add +1 skill
- Check events are completing successfully

## See Also

- [GameStateListener.java](../src/main/java/com/adastrea/assistant/GameStateListener.java) - Full interface documentation
- [GameStateIntegration.java](../src/main/java/com/adastrea/assistant/GameStateIntegration.java) - Implementation reference
- [GameStateIntegrationDemo.java](../src/main/java/com/adastrea/assistant/GameStateIntegrationDemo.java) - Working example
- [MITTENZ_CHARACTER.md](MITTENZ_CHARACTER.md) - Character background and progression
