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
    private boolean isEnabled;
    private String assistantName;

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
        this.isEnabled = true;
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
        }
    }

    /**
     * Provide a random companion dialogue
     */
    public void provideCompanionDialogue() {
        if (isEnabled) {
            String dialogue = dialogueSystem.getRandomCompanionDialogue();
            speak(dialogue);
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
        String response = dialogueSystem.generateResponse(query);
        speak(response);
        return response;
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
}
