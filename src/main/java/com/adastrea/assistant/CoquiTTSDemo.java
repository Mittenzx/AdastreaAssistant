package com.adastrea.assistant;

/**
 * Demo application showing how to use CoquiTTSAudioManager for human-like voice synthesis.
 * 
 * This demonstrates the Phase 1 implementation of the human-like audio system,
 * including emotional expression and natural speech synthesis.
 */
public class CoquiTTSDemo {
    
    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("  Coqui TTS Audio Manager Demo");
        System.out.println("  Phase 1: Human-like Voice Synthesis");
        System.out.println("===========================================\n");
        
        // Get project root (assuming we're running from the project directory)
        String projectRoot = System.getProperty("user.dir");
        
        // Create CoquiTTSAudioManager
        CoquiTTSAudioManager audioManager = new CoquiTTSAudioManager(projectRoot);
        
        // Check if TTS is available
        if (!audioManager.isTTSAvailable()) {
            System.err.println("\n[WARNING] Coqui TTS is not available.");
            System.err.println("Install with: pip install TTS librosa soundfile scipy");
            System.err.println("\nContinuing with fallback mode (console output)...\n");
        }
        
        // Demo 1: Greetings with different emotions
        System.out.println("\n--- Demo 1: Greetings ---");
        audioManager.playVoiceWithEmotion(
            "Hello there! I'm here to help you on your space adventure.",
            "friendly"
        );
        
        sleep(2000);
        
        // Demo 2: Hostile stage dialogue
        System.out.println("\n--- Demo 2: Hostile Stage ---");
        audioManager.playVoiceWithEmotion(
            "Who the hell are you? Where is my dad?",
            "hostile"
        );
        
        sleep(2000);
        
        audioManager.playVoiceWithEmotion(
            "You can't do this to me! Do you know who I am?",
            "angry"
        );
        
        sleep(2000);
        
        // Demo 3: Curious stage dialogue
        System.out.println("\n--- Demo 3: Curious Stage ---");
        audioManager.playVoiceWithEmotion(
            "What does this do? I've never seen anything like this before.",
            "curious"
        );
        
        sleep(2000);
        
        audioManager.playVoiceWithEmotion(
            "Wow, I can see all the ship's systems from here.",
            "fascinated"
        );
        
        sleep(2000);
        
        // Demo 4: Cooperative stage dialogue
        System.out.println("\n--- Demo 4: Cooperative Stage ---");
        audioManager.playVoiceWithEmotion(
            "We need to check the oxygen levels.",
            "cooperative"
        );
        
        sleep(2000);
        
        audioManager.playVoiceWithEmotion(
            "We make a pretty good team, don't we?",
            "friendly"
        );
        
        sleep(2000);
        
        // Demo 5: Urgent notification
        System.out.println("\n--- Demo 5: Urgent Notification ---");
        audioManager.playVoiceWithEmotion(
            "Warning! Oxygen levels are getting low!",
            "urgent"
        );
        
        sleep(2000);
        
        // Demo 6: Contemplative moment
        System.out.println("\n--- Demo 6: Contemplative ---");
        audioManager.playVoiceWithEmotion(
            "The stars sure are beautiful today, aren't they?",
            "contemplative"
        );
        
        sleep(2000);
        
        // Demo 7: Using basic playVoice (defaults to neutral)
        System.out.println("\n--- Demo 7: Neutral/Default ---");
        audioManager.playVoice(
            "System initializing. Mittenz here, ready to assist."
        );
        
        // Wait for async operations to complete
        System.out.println("\n\nWaiting for audio generation to complete...");
        // Note: In production code, use CompletableFuture.allOf() to track completion
        // For this demo, a simple sleep is sufficient since we're just demonstrating the API
        sleep(5000);
        
        System.out.println("\n===========================================");
        System.out.println("  Demo Complete!");
        System.out.println("===========================================");
        System.out.println("\nGenerated audio files are stored in:");
        System.out.println("  " + audioManager.getAudioOutputDir());
        System.out.println("\nPre-generated samples are available in:");
        System.out.println("  " + projectRoot + "/src/main/resources/audio/");
        System.out.println("\nFor more information, see:");
        System.out.println("  docs/HUMAN_AUDIO_PLAN.md");
    }
    
    /**
     * Sleep helper method.
     */
    private static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
