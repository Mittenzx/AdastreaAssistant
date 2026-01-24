package com.adastrea.assistant;

import java.util.HashMap;
import java.util.Map;

/**
 * Integration layer between the game (Minecraft/Adastrea mod) and the AI Assistant.
 * This class implements GameStateListener and translates game events into
 * appropriate assistant responses.
 */
public class GameStateIntegration implements GameStateListener {
    
    private final AIAssistant assistant;
    private final ContextTracker contextTracker;
    private final Map<String, Long> lastWarningTime;
    private static final long WARNING_COOLDOWN_MS = 30000; // 30 seconds between same warnings
    
    public GameStateIntegration(AIAssistant assistant) {
        this.assistant = assistant;
        this.contextTracker = assistant.getContextTracker();
        this.lastWarningTime = new HashMap<>();
    }
    
    /**
     * Check if enough time has passed since the last warning of this type
     * to avoid spamming the player with repetitive warnings.
     */
    private boolean canWarn(String warningType) {
        Long lastTime = lastWarningTime.get(warningType);
        if (lastTime == null) {
            lastWarningTime.put(warningType, System.currentTimeMillis());
            return true;
        }
        
        long timeSince = System.currentTimeMillis() - lastTime;
        if (timeSince >= WARNING_COOLDOWN_MS) {
            lastWarningTime.put(warningType, System.currentTimeMillis());
            return true;
        }
        return false;
    }
    
    @Override
    public void onLowOxygen(int oxygenLevel, int timeRemaining) {
        if (!canWarn("oxygen")) return;
        
        // Record in context
        contextTracker.recordEvent("oxygen_low", "Oxygen at " + oxygenLevel + "%", 
            oxygenLevel < 20 ? ContextTracker.EventSeverity.CRITICAL : ContextTracker.EventSeverity.HIGH);
        contextTracker.updatePlayerState("oxygen", oxygenLevel);
        
        String urgency = oxygenLevel < 20 ? "urgent" : "worried";
        String message;
        
        if (oxygenLevel < 10) {
            message = "Critical! Oxygen at " + oxygenLevel + "%! You have about " + 
                     timeRemaining + " seconds!";
        } else if (oxygenLevel < 30) {
            message = "Warning: Oxygen levels dropping. Currently at " + oxygenLevel + "%.";
        } else {
            message = "Heads up - oxygen is getting low. Might want to check that.";
        }
        
        assistant.speak(message);
        
        // Mark oxygen as a concern in context
        if (assistant.getProfile() instanceof MittenzProfile) {
            MittenzProfile mittenz = (MittenzProfile) assistant.getProfile();
            mittenz.recordConcern("oxygen");
        }
    }
    
    @Override
    public void onTemperatureWarning(double temperature, boolean isDangerous) {
        if (!canWarn("temperature")) return;
        
        String message;
        if (isDangerous) {
            if (temperature > 100) {
                message = "Extreme heat detected! " + temperature + " degrees. Find shelter immediately!";
            } else {
                message = "Temperature critical: " + temperature + " degrees. This could be dangerous.";
            }
        } else {
            message = "Temperature is " + temperature + " degrees. Might want to be careful.";
        }
        
        assistant.speak(message);
    }
    
    @Override
    public void onLowResource(String resourceType, double amount, double threshold) {
        if (!canWarn("resource_" + resourceType)) return;
        
        String message = resourceType + " is running low. You have " + 
                        String.format("%.1f", amount) + " remaining.";
        
        // Add context-aware suggestions based on relationship stage
        if (assistant.getProfile() != null) {
            RelationshipStage stage = assistant.getProfile().getRelationshipStage();
            if (stage == RelationshipStage.COOPERATIVE) {
                message += " Should we look for more?";
            } else if (stage == RelationshipStage.CURIOUS) {
                message += " Not sure where to find more though...";
            }
        }
        
        assistant.speak(message);
    }
    
    @Override
    public void onLocationEntered(String locationName, String locationType) {
        // Update context
        contextTracker.setCurrentLocation(locationName);
        contextTracker.recordEvent("location_entered", locationName + " (" + locationType + ")", 
            ContextTracker.EventSeverity.INFO);
        
        String message;
        
        if (assistant.getProfile() != null) {
            RelationshipStage stage = assistant.getProfile().getRelationshipStage();
            
            switch (stage) {
                case HOSTILE:
                    message = "Where are you taking me now? " + locationName + "?";
                    break;
                case CURIOUS:
                    message = "Interesting... we're at " + locationName + ". A " + locationType + ".";
                    break;
                case COOPERATIVE:
                    message = "Welcome to " + locationName + ". Let me scan this " + locationType + " for you.";
                    break;
                default:
                    message = "Now at: " + locationName;
            }
        } else {
            message = "Arrived at " + locationName + " - a " + locationType + ".";
        }
        
        assistant.speak(message);
        
        // Track location visit
        if (assistant.getProfile() instanceof MittenzProfile) {
            MittenzProfile mittenz = (MittenzProfile) assistant.getProfile();
            mittenz.learnSystem(locationType);
        }
    }
    
    @Override
    public void onDiscovery(String discoveryType, String discoveryName) {
        String message;
        
        if (assistant.getProfile() != null) {
            RelationshipStage stage = assistant.getProfile().getRelationshipStage();
            
            switch (stage) {
                case HOSTILE:
                    message = "You found something. A " + discoveryName + ". So what?";
                    break;
                case CURIOUS:
                    message = "Wow! A " + discoveryName + "! I've never seen a " + discoveryType + " like this before!";
                    break;
                case COOPERATIVE:
                    message = "Excellent find! " + discoveryName + " discovered. Logging this " + 
                             discoveryType + " to our database.";
                    break;
                default:
                    message = "Discovery: " + discoveryName;
            }
        } else {
            message = "Discovered: " + discoveryName + " (" + discoveryType + ")";
        }
        
        assistant.speak(message);
        
        // Increase skill for discoveries
        if (assistant.getProfile() instanceof MittenzProfile) {
            MittenzProfile mittenz = (MittenzProfile) assistant.getProfile();
            mittenz.increaseSkillLevel(2);
        }
    }
    
    @Override
    public void onLowHealth(int healthLevel, String damageSource) {
        if (!canWarn("health")) return;
        
        String message;
        
        if (healthLevel < 20) {
            message = "You're badly hurt! Health at " + healthLevel + "%!";
            if (damageSource != null && !damageSource.isEmpty()) {
                message += " That " + damageSource + " really did a number on you.";
            }
        } else if (healthLevel < 50) {
            message = "Take it easy - you're at " + healthLevel + "% health.";
        } else {
            message = "Health is at " + healthLevel + "%. Be careful out there.";
        }
        
        assistant.speak(message);
    }
    
    @Override
    public void onAchievement(String achievementName, String description) {
        String message;
        
        if (assistant.getProfile() != null) {
            RelationshipStage stage = assistant.getProfile().getRelationshipStage();
            
            switch (stage) {
                case HOSTILE:
                    message = "Congratulations, I guess. " + achievementName + ".";
                    break;
                case CURIOUS:
                    message = "Hey, that's pretty cool! " + achievementName + "!";
                    break;
                case COOPERATIVE:
                    message = "Outstanding! You earned: " + achievementName + "! " + description;
                    break;
                default:
                    message = "Achievement unlocked: " + achievementName;
            }
        } else {
            message = "Achievement: " + achievementName + " - " + description;
        }
        
        assistant.speak(message);
        
        // Increase skill for achievements
        if (assistant.getProfile() instanceof MittenzProfile) {
            MittenzProfile mittenz = (MittenzProfile) assistant.getProfile();
            mittenz.increaseSkillLevel(2);
        }
    }
    
    @Override
    public void onEmergency(String emergencyType, int severity) {
        String message;
        
        if (severity >= 4) {
            message = "EMERGENCY! " + emergencyType + "! Critical situation!";
        } else if (severity >= 3) {
            message = "Alert: " + emergencyType + " detected. Immediate attention required.";
        } else {
            message = emergencyType + " warning. Please address when possible.";
        }
        
        assistant.speak(message);
        
        // Record emergency for context
        if (assistant.getProfile() instanceof MittenzProfile) {
            MittenzProfile mittenz = (MittenzProfile) assistant.getProfile();
            mittenz.recordConcern(emergencyType);
        }
    }
    
    @Override
    public void onItemCrafted(String itemName, boolean isFirstTime) {
        String message;
        
        if (isFirstTime) {
            if (assistant.getProfile() != null) {
                RelationshipStage stage = assistant.getProfile().getRelationshipStage();
                
                switch (stage) {
                    case HOSTILE:
                        message = "You made a " + itemName + ". Great. Can I go home now?";
                        break;
                    case CURIOUS:
                        message = "Oh! You made a " + itemName + "! How does that work?";
                        break;
                    case COOPERATIVE:
                        message = "Nice work on your first " + itemName + "! That'll come in handy.";
                        break;
                    default:
                        message = "Crafted: " + itemName + " (first time)";
                }
            } else {
                message = "First " + itemName + " crafted successfully.";
            }
            
            // Teach about the item if it's the first time
            assistant.teach("crafting");
        } else {
            message = "Crafted: " + itemName;
        }
        
        assistant.speak(message);
    }
    
    @Override
    public void onIdleCheck(int minutesSinceLastInteraction, String currentActivity) {
        // Only provide idle dialogue if enough time has passed
        if (minutesSinceLastInteraction >= 5) {
            assistant.provideCompanionDialogue();
        }
    }
}
