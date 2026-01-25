package com.adastrea.assistant;

import java.util.HashMap;
import java.util.Map;

/**
 * Integration layer between the game and the AI Assistant.
 * This class implements GameStateListener and translates game events into
 * appropriate assistant responses.
 */
public class GameStateIntegration implements GameStateListener {
    
    private final AIAssistant assistant;
    private final ContextTracker contextTracker;
    private final Map<String, Long> lastWarningTime;
    
    // Thresholds
    private static final long WARNING_COOLDOWN_MS = 30000; // 30 seconds between same warnings
    private static final int OXYGEN_CRITICAL_THRESHOLD = 10;
    private static final int OXYGEN_WARNING_THRESHOLD = 30;
    private static final int HEALTH_CRITICAL_THRESHOLD = 20;
    private static final int HEALTH_WARNING_THRESHOLD = 50;
    private static final int EMERGENCY_CRITICAL_SEVERITY = 4;
    private static final int EMERGENCY_HIGH_SEVERITY = 3;
    private static final int IDLE_TIME_THRESHOLD_MINUTES = 5;
    private static final double TEMPERATURE_EXTREME_HEAT = 100.0;
    private static final double TEMPERATURE_DANGEROUS_COLD = -50.0;
    private static final double TEMPERATURE_CRITICAL_COLD = -100.0;
    private static final double TEMPERATURE_CRITICAL_HEAT = 150.0;
    
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
            oxygenLevel < OXYGEN_CRITICAL_THRESHOLD ? ContextTracker.EventSeverity.CRITICAL : ContextTracker.EventSeverity.HIGH);
        contextTracker.updatePlayerState("oxygen", oxygenLevel);
        
        String message;
        
        if (oxygenLevel < OXYGEN_CRITICAL_THRESHOLD) {
            message = "Critical! Oxygen at " + oxygenLevel + "%! You have about " + 
                     timeRemaining + " seconds!";
        } else if (oxygenLevel < OXYGEN_WARNING_THRESHOLD) {
            message = "Warning: Oxygen levels dropping. Currently at " + oxygenLevel + "%.";
        } else {
            message = "Heads up - oxygen is getting low. Might want to check that.";
        }
        
        assistant.speak(message);
    }
    
    @Override
    public void onTemperatureWarning(double temperature, boolean isDangerous) {
        if (!canWarn("temperature")) return;
        
        String message;
        if (isDangerous) {
            if (temperature > TEMPERATURE_EXTREME_HEAT) {
                message = "Extreme heat detected! " + temperature + " degrees. Find shelter immediately!";
            } else if (temperature < TEMPERATURE_DANGEROUS_COLD) {
                message = "Extreme cold detected! " + temperature + " degrees. Find shelter immediately!";
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
        
        if (healthLevel < HEALTH_CRITICAL_THRESHOLD) {
            message = "You're badly hurt! Health at " + healthLevel + "%!";
            if (damageSource != null && !damageSource.isEmpty()) {
                message += " That " + damageSource + " really did a number on you.";
            }
        } else if (healthLevel < HEALTH_WARNING_THRESHOLD) {
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
        // Record emergency event in context tracker
        ContextTracker.EventSeverity eventSeverity;
        if (severity >= EMERGENCY_CRITICAL_SEVERITY) {
            eventSeverity = ContextTracker.EventSeverity.CRITICAL;
        } else if (severity >= EMERGENCY_HIGH_SEVERITY) {
            eventSeverity = ContextTracker.EventSeverity.HIGH;
        } else {
            eventSeverity = ContextTracker.EventSeverity.MEDIUM;
        }
        contextTracker.recordEvent("emergency", emergencyType, eventSeverity);
        
        String message;
        
        if (severity >= EMERGENCY_CRITICAL_SEVERITY) {
            message = "EMERGENCY! " + emergencyType + "! Critical situation!";
        } else if (severity >= EMERGENCY_HIGH_SEVERITY) {
            message = "Alert: " + emergencyType + " detected. Immediate attention required.";
        } else {
            message = emergencyType + " warning. Please address when possible.";
        }
        
        assistant.speak(message);
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
        if (minutesSinceLastInteraction >= IDLE_TIME_THRESHOLD_MINUTES) {
            assistant.provideCompanionDialogue();
        }
    }
}
