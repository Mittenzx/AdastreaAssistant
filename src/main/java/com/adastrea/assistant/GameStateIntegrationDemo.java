package com.adastrea.assistant;

/**
 * Demonstration of the Game State Integration system.
 * Shows how the Adastrea Minecraft mod can connect to the AIAssistant
 * and trigger contextual responses based on game events.
 */
public class GameStateIntegrationDemo {
    
    public static void main(String[] args) {
        System.out.println("=== AdastreaAssistant - Game State Integration Demo ===\n");
        
        // Create AI Assistant with Mittenz profile
        AIAssistant assistant = new AIAssistant();
        MittenzProfile mittenz = new MittenzProfile();
        assistant.setProfile(mittenz);
        
        // Create game state integration layer
        GameStateIntegration gameState = new GameStateIntegration(assistant);
        
        // Initialize assistant
        System.out.println("--- Initializing Assistant ---");
        assistant.initialize();
        System.out.println();
        
        // Simulate game events
        System.out.println("--- Simulating Game Events ---\n");
        
        // Event 1: Player enters a new location
        System.out.println("Event: Player arrives at Mars");
        gameState.onLocationEntered("Mars", "planet");
        sleep(2);
        
        // Event 2: Low oxygen warning
        System.out.println("\nEvent: Oxygen level dropping");
        gameState.onLowOxygen(25, 120);
        sleep(2);
        
        // Event 3: Player discovers something
        System.out.println("\nEvent: Discovery made");
        gameState.onDiscovery("mineral", "Titanium Ore");
        sleep(2);
        
        // Event 4: Low resource warning
        System.out.println("\nEvent: Fuel running low");
        gameState.onLowResource("fuel", 15.5, 20.0);
        sleep(2);
        
        // Advance Mittenz's relationship stage
        System.out.println("\n--- Progressing Relationship ---");
        System.out.println("Mittenz is learning and building trust...");
        for (int i = 0; i < 5; i++) {
            assistant.provideCompanionDialogue();
            sleep(1);
        }
        
        // Check relationship stage
        System.out.println("\nCurrent relationship stage: " + mittenz.getRelationshipStage());
        System.out.println("Current skill level: " + mittenz.getSkillLevel());
        System.out.println();
        
        // Event 5: Achievement unlocked (Mittenz should respond differently now)
        System.out.println("Event: Achievement unlocked");
        gameState.onAchievement("First Steps", "Completed your first planetary landing");
        sleep(2);
        
        // Event 6: Emergency situation
        System.out.println("\nEvent: Emergency!");
        gameState.onEmergency("Hull Breach", 4);
        sleep(2);
        
        // Event 7: Temperature warning
        System.out.println("\nEvent: Temperature danger");
        gameState.onTemperatureWarning(150.0, true);
        sleep(2);
        
        // Event 8: Low health
        System.out.println("\nEvent: Player health low");
        gameState.onLowHealth(30, "radiation");
        sleep(2);
        
        // Event 9: Crafting
        System.out.println("\nEvent: Item crafted");
        gameState.onItemCrafted("Oxygen Generator", true);
        sleep(2);
        
        // Show progress
        System.out.println("\n--- Mittenz's Progress ---");
        System.out.println("Skill Level: " + mittenz.getSkillLevel() + "/100");
        System.out.println("Learned Systems: " + mittenz.getLearnedSystems());
        System.out.println("Relationship Stage: " + mittenz.getRelationshipStage());
        
        // Show a memory fragment
        System.out.println("\n--- Memory Fragment ---");
        System.out.println("Mittenz: \"" + mittenz.getMemoryFragment() + "\"");
        
        System.out.println("\n=== Demo Complete ===");
        System.out.println("\nIntegration Guide:");
        System.out.println("1. Create AIAssistant with desired profile");
        System.out.println("2. Create GameStateIntegration with the assistant");
        System.out.println("3. Call gameState methods when corresponding game events occur");
        System.out.println("4. Assistant will respond contextually based on:");
        System.out.println("   - Relationship stage (Hostile → Curious → Cooperative)");
        System.out.println("   - Skill level (0-100, increases with learning)");
        System.out.println("   - Event severity and type");
    }
    
    private static void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
