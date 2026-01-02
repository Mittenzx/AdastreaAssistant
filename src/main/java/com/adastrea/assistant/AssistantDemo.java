package com.adastrea.assistant;

/**
 * Example demonstration of the AI Assistant usage
 */
public class AssistantDemo {
    public static void main(String[] args) {
        System.out.println("=== Adastrea AI Assistant Demo ===\n");

        // Create and initialize the assistant
        AIAssistant assistant = new AIAssistant("Astra");
        assistant.initialize();

        System.out.println("\n--- Companion Dialogue ---");
        // Provide some companion dialogue
        assistant.provideCompanionDialogue();
        
        System.out.println("\n--- Teaching System ---");
        // Teach the player about oxygen
        assistant.teach("oxygen");
        
        System.out.println("\n--- Player Interaction ---");
        // Player asks questions
        assistant.respondToQuery("How does gravity work?");
        assistant.respondToQuery("Tell me about planets");
        
        System.out.println("\n--- Reminder System ---");
        // Set a reminder (this would trigger after the delay in a real game)
        assistant.setReminder("Check your fuel levels", 5);
        System.out.println("[SYSTEM] Reminder set for 5 minutes from now");
        
        System.out.println("\n--- More Companion Dialogues ---");
        // More companion interactions
        assistant.provideCompanionDialogue();
        assistant.provideCompanionDialogue();
        
        System.out.println("\n--- Teaching More Topics ---");
        assistant.teach("navigation");
        assistant.teach("resources");
        
        System.out.println("\n--- Visual Notifications ---");
        assistant.getVisualManager().showNotification("New achievement unlocked!");
        assistant.getVisualManager().showAssistantIcon("happy");
        
        System.out.println("\n=== Demo Complete ===");
    }
}
