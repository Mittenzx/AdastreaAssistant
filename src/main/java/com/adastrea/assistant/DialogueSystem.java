package com.adastrea.assistant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Handles dialogue generation and conversation management for the AI assistant
 */
public class DialogueSystem {
    private final List<String> greetings;
    private final List<String> companionDialogues;
    private final Random random;

    public DialogueSystem() {
        this.random = new Random();
        this.greetings = new ArrayList<>();
        this.companionDialogues = new ArrayList<>();
        initializeDialogues();
    }

    /**
     * Initialize default dialogues
     */
    private void initializeDialogues() {
        // Greetings
        greetings.add("Hello there! I'm here to help you on your space adventure.");
        greetings.add("Greetings, explorer! Ready to discover the cosmos?");
        greetings.add("Welcome back! Let's continue our journey through the stars.");
        
        // Companion dialogues
        companionDialogues.add("The stars sure are beautiful today, aren't they?");
        companionDialogues.add("Remember to check your oxygen levels regularly.");
        companionDialogues.add("I've been thinking about all the planets we could explore together.");
        companionDialogues.add("Space can be lonely, but at least you have me!");
        companionDialogues.add("Did you know that some planets have unique gravity? Be careful when landing!");
        companionDialogues.add("I'm here if you need any guidance or just want to chat.");
        companionDialogues.add("Your suit's looking good. Maintaining equipment is important out here.");
        companionDialogues.add("Every journey starts with a single step... or rocket launch!");
    }

    /**
     * Get a random greeting
     * @return A greeting message
     */
    public String getGreeting() {
        if (greetings.isEmpty()) {
            return "Hello!";
        }
        return greetings.get(random.nextInt(greetings.size()));
    }

    /**
     * Get random companion dialogue to provide company
     * @return A companion message
     */
    public String getRandomCompanionDialogue() {
        if (companionDialogues.isEmpty()) {
            return "I'm here with you.";
        }
        return companionDialogues.get(random.nextInt(companionDialogues.size()));
    }

    /**
     * Generate a response to a player's query
     * @param query The player's question or statement
     * @return The assistant's response
     */
    public String generateResponse(String query) {
        if (query == null || query.trim().isEmpty()) {
            return "I didn't quite catch that. Could you repeat?";
        }

        String lowerQuery = query.toLowerCase().trim();
        
        // Simple keyword-based responses
        if (lowerQuery.contains("help")) {
            return "I'm here to help! You can ask me about game mechanics, get reminders, or just chat.";
        } else if (lowerQuery.contains("oxygen") || lowerQuery.contains("air")) {
            return "Oxygen is crucial in space. Make sure to monitor your levels and refill when needed.";
        } else if (lowerQuery.contains("planet")) {
            return "There are many planets to explore! Each has unique characteristics and resources.";
        } else if (lowerQuery.contains("gravity")) {
            return "Different planets have different gravity. Lower gravity means you can jump higher!";
        } else if (lowerQuery.contains("thank")) {
            return "You're welcome! Happy to help anytime.";
        } else if (lowerQuery.contains("bye") || lowerQuery.contains("goodbye")) {
            return "Safe travels! I'll be here when you need me.";
        } else {
            return "That's interesting! I'm still learning, but I'm here to keep you company.";
        }
    }

    /**
     * Add a custom greeting
     * @param greeting The greeting to add
     */
    public void addGreeting(String greeting) {
        greetings.add(greeting);
    }

    /**
     * Add a custom companion dialogue
     * @param dialogue The dialogue to add
     */
    public void addCompanionDialogue(String dialogue) {
        companionDialogues.add(dialogue);
    }
}
