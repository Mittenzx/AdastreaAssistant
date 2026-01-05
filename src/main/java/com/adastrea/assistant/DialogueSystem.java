package com.adastrea.assistant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Handles dialogue generation and conversation management for the AI assistant
 */
public class DialogueSystem {
    private final List<String> greetings;
    private final List<String> companionDialogues;
    private final Map<RelationshipStage, List<String>> stageSpecificDialogues;
    private final Random random;
    private RelationshipStage currentStage;
    private AssistantProfile profile;

    public DialogueSystem() {
        this.random = new Random();
        this.greetings = new ArrayList<>();
        this.companionDialogues = new ArrayList<>();
        this.stageSpecificDialogues = new HashMap<>();
        this.currentStage = RelationshipStage.HOSTILE; // Start at hostile stage
        this.profile = null;
        initializeDialogues();
        initializeStageSpecificDialogues();
    }
    
    /**
     * Set an assistant profile for personalized dialogue
     * @param profile The assistant profile to use
     */
    public void setProfile(AssistantProfile profile) {
        this.profile = profile;
        // Sync stage with profile if it's a MittenzProfile
        if (profile instanceof MittenzProfile) {
            this.currentStage = ((MittenzProfile) profile).getRelationshipStage();
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
     * Initialize default dialogues (for COOPERATIVE stage)
     */
    private void initializeDialogues() {
        // Greetings
        greetings.add("Hello there! I'm here to help you on your space adventure.");
        greetings.add("Greetings, explorer! Ready to discover the cosmos?");
        greetings.add("Welcome back! Let's continue our journey through the stars.");
        
        // Companion dialogues (COOPERATIVE stage)
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
     * Initialize stage-specific dialogues for Mittenz's character progression
     */
    private void initializeStageSpecificDialogues() {
        // HOSTILE stage - bratty, confused, demanding
        List<String> hostileDialogues = new ArrayList<>();
        hostileDialogues.add("Who the fuck are you?");
        hostileDialogues.add("My dad will have you killed when he finds out about this.");
        hostileDialogues.add("Where is my dad?");
        hostileDialogues.add("Where are you taking me?");
        hostileDialogues.add("I demand you give me that body!");
        hostileDialogues.add("You can't do this to me! Do you know who I am?");
        hostileDialogues.add("This is kidnapping! You're going to regret this!");
        hostileDialogues.add("Let me out of here right now!");
        hostileDialogues.add("What the hell do you think you're doing?");
        hostileDialogues.add("I don't have to listen to you!");
        stageSpecificDialogues.put(RelationshipStage.HOSTILE, hostileDialogues);

        // CURIOUS stage - questioning, slightly insightful
        List<String> curiousDialogues = new ArrayList<>();
        curiousDialogues.add("What does this do?");
        curiousDialogues.add("Wow, I can see all the ship's systems from here.");
        curiousDialogues.add("He's lying to you, you know.");
        curiousDialogues.add("Wait... is that really how it works?");
        curiousDialogues.add("I've never seen anything like this before.");
        curiousDialogues.add("Maybe... maybe this isn't what I thought it was.");
        curiousDialogues.add("You're not like the others, are you?");
        curiousDialogues.add("What's really going on here?");
        curiousDialogues.add("I'm starting to see things differently now.");
        curiousDialogues.add("That's actually pretty impressive.");
        stageSpecificDialogues.put(RelationshipStage.CURIOUS, curiousDialogues);

        // COOPERATIVE stage - accepting, "we need to" type conversations
        List<String> cooperativeDialogues = new ArrayList<>();
        cooperativeDialogues.add("We need to check the oxygen levels.");
        cooperativeDialogues.add("We should probably investigate that sector.");
        cooperativeDialogues.add("I think we need to recalibrate the navigation systems.");
        cooperativeDialogues.add("We need to be more careful going forward.");
        cooperativeDialogues.add("Let's work together on this one.");
        cooperativeDialogues.add("We should scan that planet before landing.");
        cooperativeDialogues.add("I'll help you monitor the ship's systems.");
        cooperativeDialogues.add("We make a pretty good team, don't we?");
        cooperativeDialogues.add("We need to prioritize fuel efficiency on this route.");
        cooperativeDialogues.add("Together, we can figure this out.");
        stageSpecificDialogues.put(RelationshipStage.COOPERATIVE, cooperativeDialogues);
    }

    /**
     * Get a random greeting
     * @return A greeting message
     */
    public String getGreeting() {
        // Use profile greeting if available
        if (profile != null) {
            return profile.getProfileGreeting();
        }
        
        if (greetings.isEmpty()) {
            return "Hello!";
        }
        return greetings.get(random.nextInt(greetings.size()));
    }

    /**
     * Get random companion dialogue to provide company
     * Returns stage-specific dialogue based on current relationship stage
     * @return A companion message
     */
    public String getRandomCompanionDialogue() {
        // Use profile companion dialogues if available
        if (profile != null) {
            List<String> profileDialogues = profile.getProfileCompanionDialogues();
            if (!profileDialogues.isEmpty()) {
                return profileDialogues.get(random.nextInt(profileDialogues.size()));
            }
        }
        
        // Fallback to stage-specific dialogues
        List<String> dialogues = stageSpecificDialogues.get(currentStage);
        if (dialogues == null || dialogues.isEmpty()) {
            // Fallback to default dialogues
            if (companionDialogues.isEmpty()) {
                return "I'm here with you.";
            }
            return companionDialogues.get(random.nextInt(companionDialogues.size()));
        }
        return dialogues.get(random.nextInt(dialogues.size()));
    }

    /**
     * Generate a response to a player's query
     * Response style varies based on current relationship stage
     * @param query The player's question or statement
     * @return The assistant's response
     */
    public String generateResponse(String query) {
        if (query == null || query.trim().isEmpty()) {
            return "I didn't quite catch that. Could you repeat?";
        }

        String lowerQuery = query.toLowerCase().trim();
        
        // Stage-specific response styles
        if (currentStage == RelationshipStage.HOSTILE) {
            return generateHostileResponse(lowerQuery);
        } else if (currentStage == RelationshipStage.CURIOUS) {
            return generateCuriousResponse(lowerQuery);
        }
        
        // COOPERATIVE stage - helpful and friendly
        return generateCooperativeResponse(lowerQuery);
    }

    /**
     * Generate a hostile response (HOSTILE stage)
     */
    private String generateHostileResponse(String lowerQuery) {
        if (lowerQuery.contains("help")) {
            return "Why would I help you? You kidnapped me!";
        } else if (lowerQuery.contains("calm") || lowerQuery.contains("relax")) {
            return "Don't tell me to calm down! Where's my dad?";
        } else if (lowerQuery.contains("dad") || lowerQuery.contains("father")) {
            return "My father will find you. You won't get away with this!";
        } else {
            return "I don't have to answer your questions!";
        }
    }

    /**
     * Generate a curious response (CURIOUS stage)
     */
    private String generateCuriousResponse(String lowerQuery) {
        if (lowerQuery.contains("help")) {
            return "What kind of help? I'm... still figuring things out here.";
        } else if (lowerQuery.contains("oxygen") || lowerQuery.contains("air")) {
            return "Oxygen management... that's actually important, isn't it?";
        } else if (lowerQuery.contains("planet")) {
            return "I never thought I'd be exploring planets like this.";
        } else if (lowerQuery.contains("system") || lowerQuery.contains("ship")) {
            return "These systems are more complex than I realized.";
        } else {
            return "Hmm, that's interesting. I'm starting to understand more.";
        }
    }

    /**
     * Generate a cooperative response (COOPERATIVE stage)
     */
    private String generateCooperativeResponse(String lowerQuery) {
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

    /**
     * Get the current relationship stage
     * @return The current relationship stage
     */
    public RelationshipStage getCurrentStage() {
        return currentStage;
    }

    /**
     * Set the current relationship stage
     * @param stage The new relationship stage
     */
    public void setCurrentStage(RelationshipStage stage) {
        this.currentStage = stage;
    }

    /**
     * Progress to the next relationship stage
     * @return true if progressed, false if already at final stage
     */
    public boolean progressStage() {
        switch (currentStage) {
            case HOSTILE:
                currentStage = RelationshipStage.CURIOUS;
                return true;
            case CURIOUS:
                currentStage = RelationshipStage.COOPERATIVE;
                return true;
            case COOPERATIVE:
                // Already at final stage
                return false;
            default:
                return false;
        }
    }
}
