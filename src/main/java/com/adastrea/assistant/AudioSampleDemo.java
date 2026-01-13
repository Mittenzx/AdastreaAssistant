package com.adastrea.assistant;

/**
 * Demo class showing how to use the sample audio files with AudioManager
 * 
 * This demonstrates preloading and playing audio files for different scenarios
 * in the AdastreaAssistant system.
 */
public class AudioSampleDemo {
    
    public static void main(String[] args) {
        System.out.println("=== AdastreaAssistant Audio Sample Demo ===\n");
        
        // Initialize AudioManager
        AudioManager audioManager = new AudioManager();
        audioManager.setVolume(0.8f);
        
        // Preload audio samples
        System.out.println("Preloading audio samples...");
        preloadAudioSamples(audioManager);
        System.out.println("Audio samples preloaded!\n");
        
        // Demo 1: Greetings
        demoGreetings(audioManager);
        
        // Demo 2: Mittenz relationship stages
        demoMittenzStages(audioManager);
        
        // Demo 3: Notifications
        demoNotifications(audioManager);
        
        // Demo 4: UI Sounds
        demoUISounds(audioManager);
        
        // Demo 5: Complete assistant initialization with audio
        demoCompleteAssistant(audioManager);
        
        System.out.println("\n=== Demo Complete ===");
        System.out.println("\nNote: These audio files are tone-based placeholders.");
        System.out.println("In production, replace with voice recordings or TTS.");
    }
    
    /**
     * Preload all audio samples into the AudioManager cache
     */
    private static void preloadAudioSamples(AudioManager audioManager) {
        // Greetings
        audioManager.preloadAudio("welcome", "audio/greetings/welcome.wav");
        audioManager.preloadAudio("hello", "audio/greetings/hello.wav");
        audioManager.preloadAudio("startup", "audio/greetings/startup.wav");
        
        // Dialogue
        audioManager.preloadAudio("hostile", "audio/dialogue/mittenz_hostile.wav");
        audioManager.preloadAudio("curious", "audio/dialogue/mittenz_curious.wav");
        audioManager.preloadAudio("cooperative", "audio/dialogue/mittenz_cooperative.wav");
        audioManager.preloadAudio("companion", "audio/dialogue/companion_message.wav");
        audioManager.preloadAudio("ack", "audio/dialogue/acknowledgment.wav");
        
        // Notifications
        audioManager.preloadAudio("reminder", "audio/notifications/reminder.wav");
        audioManager.preloadAudio("alert", "audio/notifications/alert.wav");
        audioManager.preloadAudio("teaching", "audio/notifications/teaching.wav");
        audioManager.preloadAudio("success", "audio/notifications/success.wav");
        
        // UI Sounds
        audioManager.preloadAudio("click", "audio/sounds/button_click.wav");
        audioManager.preloadAudio("menu_open", "audio/sounds/menu_open.wav");
        audioManager.preloadAudio("menu_close", "audio/sounds/menu_close.wav");
        audioManager.preloadAudio("error", "audio/sounds/error.wav");
        audioManager.preloadAudio("interaction", "audio/sounds/interaction.wav");
    }
    
    /**
     * Demo greeting sounds
     */
    private static void demoGreetings(AudioManager audioManager) {
        System.out.println("--- Demo 1: Greetings ---");
        
        System.out.println("Playing startup sound...");
        audioManager.playSoundEffect("startup");
        sleep(1000);
        
        System.out.println("Playing welcome greeting...");
        audioManager.playSoundEffect("welcome");
        sleep(1500);
        
        System.out.println("Playing hello greeting...");
        audioManager.playSoundEffect("hello");
        sleep(1000);
        
        System.out.println();
    }
    
    /**
     * Demo Mittenz relationship stage audio
     */
    private static void demoMittenzStages(AudioManager audioManager) {
        System.out.println("--- Demo 2: Mittenz Relationship Stages ---");
        
        System.out.println("HOSTILE stage: 'Who the fuck are you?'");
        audioManager.playSoundEffect("hostile");
        sleep(1500);
        
        System.out.println("CURIOUS stage: 'What does this do?'");
        audioManager.playSoundEffect("curious");
        sleep(1200);
        
        System.out.println("COOPERATIVE stage: 'We need to check the oxygen levels.'");
        audioManager.playSoundEffect("cooperative");
        sleep(2000);
        
        System.out.println("Companion message: 'The stars sure are beautiful today.'");
        audioManager.playSoundEffect("companion");
        sleep(1200);
        
        System.out.println();
    }
    
    /**
     * Demo notification sounds
     */
    private static void demoNotifications(AudioManager audioManager) {
        System.out.println("--- Demo 3: Notifications ---");
        
        System.out.println("Reminder: 'Check fuel levels'");
        audioManager.playSoundEffect("reminder");
        sleep(1500);
        
        System.out.println("Alert: 'Oxygen low!'");
        audioManager.playSoundEffect("alert");
        sleep(1000);
        
        System.out.println("Teaching: 'Learn about gravity'");
        audioManager.playSoundEffect("teaching");
        sleep(1200);
        
        System.out.println("Success: 'Mission complete!'");
        audioManager.playSoundEffect("success");
        sleep(800);
        
        System.out.println();
    }
    
    /**
     * Demo UI interaction sounds
     */
    private static void demoUISounds(AudioManager audioManager) {
        System.out.println("--- Demo 4: UI Sounds ---");
        
        System.out.println("Button click");
        audioManager.playSoundEffect("click");
        sleep(200);
        
        System.out.println("Menu open");
        audioManager.playSoundEffect("menu_open");
        sleep(400);
        
        System.out.println("Interaction");
        audioManager.playSoundEffect("interaction");
        sleep(200);
        
        System.out.println("Menu close");
        audioManager.playSoundEffect("menu_close");
        sleep(400);
        
        System.out.println("Error sound");
        audioManager.playSoundEffect("error");
        sleep(800);
        
        System.out.println();
    }
    
    /**
     * Demo complete assistant with audio integration
     */
    private static void demoCompleteAssistant(AudioManager audioManager) {
        System.out.println("--- Demo 5: Complete Assistant with Audio ---");
        
        // Create assistant with Mittenz profile
        AIAssistant assistant = new AIAssistant();
        MittenzProfile mittenz = new MittenzProfile();
        assistant.setProfile(mittenz);
        
        // Initialize with startup sound
        System.out.println("\n[System] Starting assistant...");
        audioManager.playSoundEffect("startup");
        sleep(1000);
        
        assistant.initialize();
        
        // Play stage-specific audio
        RelationshipStage stage = mittenz.getRelationshipStage();
        System.out.println("\n[Mittenz] Current stage: " + stage);
        
        switch (stage) {
            case HOSTILE:
                audioManager.playSoundEffect("hostile");
                break;
            case CURIOUS:
                audioManager.playSoundEffect("curious");
                break;
            case COOPERATIVE:
                audioManager.playSoundEffect("cooperative");
                break;
        }
        sleep(1500);
        
        // Progress through stages with audio cues
        System.out.println("\n[System] Progressing through relationship stages...");
        
        for (int i = 0; i < 5; i++) {
            assistant.provideCompanionDialogue();
            audioManager.playSoundEffect("companion");
            sleep(500);
        }
        
        // Check if stage progressed
        RelationshipStage newStage = mittenz.getRelationshipStage();
        if (newStage != stage) {
            System.out.println("\n[System] Relationship stage changed to: " + newStage);
            audioManager.playSoundEffect("success");
            sleep(800);
            
            // Play new stage audio
            switch (newStage) {
                case CURIOUS:
                    audioManager.playSoundEffect("curious");
                    break;
                case COOPERATIVE:
                    audioManager.playSoundEffect("cooperative");
                    break;
            }
        }
        
        // Teach something
        System.out.println("\n[System] Teaching about oxygen...");
        audioManager.playSoundEffect("teaching");
        sleep(1200);
        assistant.teach("oxygen");
        
        // Set a reminder
        System.out.println("\n[System] Setting reminder...");
        audioManager.playSoundEffect("reminder");
        sleep(1500);
        assistant.setReminder("Check fuel levels", 5);
        
        // Success
        System.out.println("\n[System] Demo complete!");
        audioManager.playSoundEffect("success");
    }
    
    /**
     * Simple sleep helper to pace the demo
     */
    private static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
