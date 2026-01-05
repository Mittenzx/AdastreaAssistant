package com.adastrea.assistant;

/**
 * Demonstration of the Mittenz AI profile and learning system
 * Shows how Mittenz evolves from inexperienced to skilled as she learns
 */
public class MittenzDemo {
    public static void main(String[] args) {
        System.out.println("=== Mittenz AI Assistant Demo ===\n");
        System.out.println("This demo shows Mittenz, a unique AI based on a copied human brain pattern.");
        System.out.println("Unlike standard AI, she learns and evolves like a human, starting off uncertain");
        System.out.println("but growing more confident and capable over time.\n");
        
        // Create the assistant with Mittenz profile
        AIAssistant assistant = new AIAssistant();
        MittenzProfile mittenz = new MittenzProfile();
        assistant.setProfile(mittenz);
        
        System.out.println("--- Initial State (Skill Level: " + mittenz.getSkillLevel() + ") ---");
        System.out.println("\nBackstory:");
        System.out.println(mittenz.getBackstory());
        
        System.out.println("\n--- First Meeting ---");
        assistant.initialize();
        
        System.out.println("\n--- Early Companion Dialogue (Uncertain) ---");
        assistant.provideCompanionDialogue();
        assistant.provideCompanionDialogue();
        
        System.out.println("\n--- Teaching Mittenz About Systems ---");
        System.out.println("Teaching her about oxygen systems...");
        assistant.teach("oxygen");
        System.out.println("Current skill level: " + mittenz.getSkillLevel());
        
        System.out.println("\nTeaching her about gravity...");
        assistant.teach("gravity");
        System.out.println("Current skill level: " + mittenz.getSkillLevel());
        
        System.out.println("\nTeaching her about navigation...");
        assistant.teach("navigation");
        System.out.println("Current skill level: " + mittenz.getSkillLevel());
        
        System.out.println("\n--- Learned Systems ---");
        System.out.println("Systems Mittenz has learned: " + mittenz.getLearnedSystems());
        
        System.out.println("\n--- Companion Dialogue (Growing Confidence) ---");
        assistant.provideCompanionDialogue();
        assistant.provideCompanionDialogue();
        
        System.out.println("\n--- Memory Fragment from Her Past ---");
        System.out.println("[MEMORY] " + mittenz.getMemoryFragment());
        
        System.out.println("\n--- Continuing Training ---");
        // Teach more systems
        assistant.teach("temperature");
        assistant.teach("fuel");
        assistant.teach("resources");
        assistant.teach("crafting");
        System.out.println("Systems learned: " + mittenz.getLearnedSystems().size());
        System.out.println("Current skill level: " + mittenz.getSkillLevel());
        
        System.out.println("\n--- Mid-Level Greeting ---");
        String greeting = mittenz.getProfileGreeting();
        System.out.println("[MITTENZ] " + greeting);
        
        System.out.println("\n--- Another Memory Surfaces ---");
        System.out.println("[MEMORY] " + mittenz.getMemoryFragment());
        
        System.out.println("\n--- Companion Dialogue (More Confident) ---");
        assistant.provideCompanionDialogue();
        assistant.provideCompanionDialogue();
        
        System.out.println("\n--- Advanced Training ---");
        // Continue learning to reach high skill level
        assistant.teach("exploration");
        assistant.teach("safety");
        assistant.teach("starter");
        
        // Manually boost to show advanced state
        mittenz.increaseSkillLevel(50);
        System.out.println("Skill level after extensive use: " + mittenz.getSkillLevel());
        
        System.out.println("\n--- Advanced Greeting ---");
        System.out.println("[MITTENZ] " + mittenz.getProfileGreeting());
        
        System.out.println("\n--- Advanced Companion Dialogue (Confident & Capable) ---");
        assistant.provideCompanionDialogue();
        assistant.provideCompanionDialogue();
        assistant.provideCompanionDialogue();
        
        System.out.println("\n--- Player Interaction ---");
        assistant.respondToQuery("Thank you for all your help, Mittenz");
        
        System.out.println("\n--- Final Memory ---");
        System.out.println("[MEMORY] " + mittenz.getMemoryFragment());
        
        System.out.println("\n--- Personality Traits ---");
        System.out.println("Mittenz's personality traits:");
        for (String trait : mittenz.getPersonalityTraits()) {
            System.out.println("  - " + trait);
        }
        
        System.out.println("\n--- Special Abilities ---");
        System.out.println("Mittenz's unique abilities:");
        for (String ability : mittenz.getSpecialAbilities()) {
            System.out.println("  - " + ability);
        }
        
        System.out.println("\n=== Demo Complete ===");
        System.out.println("Mittenz has evolved from an uncertain, newly-awakened consciousness");
        System.out.println("to a confident, capable companion. This journey mirrors her human origins,");
        System.out.println("showing genuine learning and emotional growth rather than programmed responses.");
    }
}
