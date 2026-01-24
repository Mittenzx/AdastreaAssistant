package com.adastrea.assistant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Main AI Assistant class that coordinates audio, visual, and dialogue components
 * to provide companionship, reminders, and teaching to players in-game.
 */
public class AIAssistant {
    private final AudioManager audioManager;
    private final VisualManager visualManager;
    private final DialogueSystem dialogueSystem;
    private final ReminderSystem reminderSystem;
    private final TeachingSystem teachingSystem;
    private final ContextTracker contextTracker;
    private boolean isEnabled;
    private String assistantName;
    private int interactionCount;
    private AssistantProfile profile;

    public AIAssistant() {
        this("Assistant");
    }

    public AIAssistant(String name) {
        this.assistantName = name;
        this.audioManager = new AudioManager();
        this.visualManager = new VisualManager();
        this.dialogueSystem = new DialogueSystem();
        this.reminderSystem = new ReminderSystem();
        this.teachingSystem = new TeachingSystem();
        this.contextTracker = new ContextTracker();
        this.isEnabled = true;
        this.interactionCount = 0;
        this.profile = null;
    }
    
    /**
     * Set the assistant profile for personalized behavior
     * @param profile The assistant profile to use
     */
    public void setProfile(AssistantProfile profile) {
        this.profile = profile;
        this.dialogueSystem.setProfile(profile);
        if (profile != null) {
            this.assistantName = profile.getName();
        }
    }
    
    /**
     * Get the current assistant profile
     * @return The current profile, or null if none is set
     */
    public AssistantProfile getProfile() {
        return profile;
    }

    /**
     * Initialize the assistant and greet the player
     */
    public void initialize() {
        if (isEnabled) {
            String greeting = assistantName + " here! " + dialogueSystem.getGreeting();
            speak(greeting);
            visualManager.showNotification(greeting);
        }
    }

    /**
     * Make the assistant speak a message
     * @param message The message to speak
     */
    public void speak(String message) {
        if (isEnabled) {
            audioManager.playVoice(message);
            visualManager.showSubtitle(message);
            
            // Track in context
            contextTracker.recordInteraction(assistantName, message, 
                ContextTracker.InteractionType.ASSISTANT_RESPONSE);
        }
    }

    /**
     * Provide a random companion dialogue
     */
    public void provideCompanionDialogue() {
        if (isEnabled) {
            String dialogue = dialogueSystem.getRandomCompanionDialogue();
            speak(dialogue);
            trackInteraction();
        }
    }

    /**
     * Teach the player about a specific topic
     * @param topic The topic to teach
     */
    public void teach(String topic) {
        if (isEnabled) {
            String lesson = teachingSystem.getLesson(topic);
            speak(lesson);
            
            // If using Mittenz profile, mark system as learned to improve her skills
            if (profile instanceof MittenzProfile) {
                MittenzProfile mittenz = (MittenzProfile) profile;
                mittenz.learnSystem(topic);
            }
        }
    }

    /**
     * Set a reminder for the player
     * @param message The reminder message
     * @param delayMinutes Minutes until reminder triggers
     */
    public void setReminder(String message, int delayMinutes) {
        reminderSystem.addReminder(message, delayMinutes);
    }

    /**
     * Check and trigger any pending reminders
     */
    public void checkReminders() {
        if (isEnabled) {
            List<String> dueReminders = reminderSystem.checkDueReminders();
            for (String reminder : dueReminders) {
                speak("Reminder: " + reminder);
            }
        }
    }

    /**
     * Respond to player query
     * @param query The player's question or statement
     * @return The assistant's response
     */
    public String respondToQuery(String query) {
        if (!isEnabled) {
            return "";
        }
        
        // Track player query in context
        contextTracker.recordInteraction("Player", query, 
            ContextTracker.InteractionType.PLAYER_QUERY);
        
        String response = dialogueSystem.generateResponse(query);
        speak(response);
        trackInteraction();
        return response;
    }

    /**
     * Track player interactions and progress relationship stage when threshold is reached
     */
    private void trackInteraction() {
        interactionCount++;
        
        // If using MittenzProfile, also increase skill level and handle stage progression
        if (profile instanceof MittenzProfile) {
            MittenzProfile mittenzProfile = (MittenzProfile) profile;
            mittenzProfile.increaseSkillLevel(1);  // Each interaction increases skill
            
            RelationshipStage currentStage = mittenzProfile.getRelationshipStage();
            
            if (currentStage == RelationshipStage.HOSTILE && interactionCount >= 5) {
                progressRelationshipStage();
                interactionCount = 0; // Reset for next stage
            } else if (currentStage == RelationshipStage.CURIOUS && interactionCount >= 10) {
                progressRelationshipStage();
                interactionCount = 0; // Reset for next stage
            }
        } else if (dialogueSystem.getCurrentStage() != null) {
            // Fallback for DialogueSystem-only progression
            RelationshipStage currentStage = dialogueSystem.getCurrentStage();
            
            if (currentStage == RelationshipStage.HOSTILE && interactionCount >= 5) {
                progressRelationshipStage();
                interactionCount = 0; // Reset for next stage
            } else if (currentStage == RelationshipStage.CURIOUS && interactionCount >= 10) {
                progressRelationshipStage();
                interactionCount = 0; // Reset for next stage
            }
        }
    }

    /**
     * Get the current relationship stage with Mittenz
     * @return The current relationship stage
     */
    public RelationshipStage getRelationshipStage() {
        if (profile instanceof MittenzProfile) {
            return ((MittenzProfile) profile).getRelationshipStage();
        }
        return dialogueSystem.getCurrentStage();
    }

    /**
     * Manually set the relationship stage
     * This will also reset the interaction count to ensure consistent progression behavior.
     * @param stage The new relationship stage
     */
    public void setRelationshipStage(RelationshipStage stage) {
        if (profile instanceof MittenzProfile) {
            ((MittenzProfile) profile).setRelationshipStage(stage);
        }
        dialogueSystem.setCurrentStage(stage);
        resetInteractionCount();
    }

    /**
     * Progress to the next relationship stage
     * @return true if progressed, false if already at final stage
     */
    public boolean progressRelationshipStage() {
        boolean progressed = false;
        
        if (profile instanceof MittenzProfile) {
            MittenzProfile mittenzProfile = (MittenzProfile) profile;
            progressed = mittenzProfile.progressRelationshipStage();
            // Sync with DialogueSystem
            dialogueSystem.setCurrentStage(mittenzProfile.getRelationshipStage());
        } else {
            progressed = dialogueSystem.progressStage();
        }
        
        if (progressed) {
            resetInteractionCount(); // Reset counter for the new stage
            if (isEnabled) {
                // Provide feedback about the stage change
                RelationshipStage newStage = getRelationshipStage();
                String stageMessage = getStageTransitionMessage(newStage);
                if (stageMessage != null) {
                    speak(stageMessage);
                }
            }
        }
        return progressed;
    }

    /**
     * Get a message for stage transitions
     */
    private String getStageTransitionMessage(RelationshipStage newStage) {
        if (profile instanceof MittenzProfile) {
            return ((MittenzProfile) profile).getStageTransitionMessage(newStage);
        }
        
        // Fallback messages
        switch (newStage) {
            case CURIOUS:
                return "Wait... maybe I should understand what's happening here.";
            case COOPERATIVE:
                return "Okay, I get it now. We need to work together.";
            default:
                return null;
        }
    }

    /**
     * Get the current interaction count
     * @return The number of interactions
     */
    public int getInteractionCount() {
        return interactionCount;
    }

    /**
     * Reset the interaction count
     */
    public void resetInteractionCount() {
        this.interactionCount = 0;
    }

    // Getters and Setters
    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public String getAssistantName() {
        return assistantName;
    }

    public void setAssistantName(String name) {
        this.assistantName = name;
    }

    public AudioManager getAudioManager() {
        return audioManager;
    }

    public VisualManager getVisualManager() {
        return visualManager;
    }

    public DialogueSystem getDialogueSystem() {
        return dialogueSystem;
    }

    public ReminderSystem getReminderSystem() {
        return reminderSystem;
    }

    public TeachingSystem getTeachingSystem() {
        return teachingSystem;
    }
    
    public ContextTracker getContextTracker() {
        return contextTracker;
    }
}
