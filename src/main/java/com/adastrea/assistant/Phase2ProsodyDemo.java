package com.adastrea.assistant;

/**
 * Demonstration of Phase 2: Prosody & Natural Speech features.
 * 
 * This demo showcases the enhanced contextual prosody system with:
 * - Urgency levels (normal, high, critical)
 * - Relationship stages (hostile, curious, cooperative)
 * - Breath sounds and natural pauses
 * - Context-aware speaking rates
 * 
 * Usage:
 *   ./gradlew run --args='com.adastrea.assistant.Phase2ProsodyDemo'
 */
public class Phase2ProsodyDemo {
    
    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("Phase 2: Prosody & Natural Speech Demonstration");
        System.out.println("=".repeat(70));
        System.out.println();
        
        // Get project root
        String projectRoot = System.getProperty("user.dir");
        
        // Create audio manager with Coqui TTS
        CoquiTTSAudioManager audioManager = new CoquiTTSAudioManager(projectRoot);
        
        // Disable audio for demo (just show functionality)
        audioManager.setAudioEnabled(false);
        
        demonstrateUrgencyLevels(audioManager);
        System.out.println();
        
        demonstrateRelationshipContexts(audioManager);
        System.out.println();
        
        demonstrateCombinedContext(audioManager);
        System.out.println();
        
        System.out.println("=".repeat(70));
        System.out.println("Demo completed!");
        System.out.println("=".repeat(70));
    }
    
    private static void demonstrateUrgencyLevels(CoquiTTSAudioManager audioManager) {
        System.out.println("1. URGENCY LEVELS DEMONSTRATION");
        System.out.println("-".repeat(70));
        System.out.println("Demonstrating how urgency affects speaking rate and pauses...");
        System.out.println();
        
        // Normal urgency
        System.out.println("[NORMAL URGENCY]");
        audioManager.playVoiceWithContext(
            "The oxygen levels are stable. We should monitor them regularly.",
            "neutral",
            "normal",
            "cooperative"
        ).join();
        System.out.println();
        
        // High urgency
        System.out.println("[HIGH URGENCY]");
        audioManager.playVoiceWithContext(
            "Oxygen levels are dropping. We need to check the life support systems.",
            "worried",
            "high",
            "cooperative"
        ).join();
        System.out.println();
        
        // Critical urgency
        System.out.println("[CRITICAL URGENCY]");
        audioManager.playVoiceWithContext(
            "Warning! Oxygen critical! We must act immediately!",
            "urgent",
            "critical",
            "cooperative"
        ).join();
        System.out.println();
    }
    
    private static void demonstrateRelationshipContexts(CoquiTTSAudioManager audioManager) {
        System.out.println("2. RELATIONSHIP CONTEXT DEMONSTRATION");
        System.out.println("-".repeat(70));
        System.out.println("Demonstrating how relationship affects voice characteristics...");
        System.out.println();
        
        String message = "What are you doing with those controls?";
        
        // Hostile stage
        System.out.println("[HOSTILE STAGE]");
        System.out.println("Voice: Sharp, defensive, faster pace, clipped pauses");
        audioManager.playVoiceWithContext(
            message,
            "angry",
            "normal",
            "hostile"
        ).join();
        System.out.println();
        
        // Curious stage
        System.out.println("[CURIOUS STAGE]");
        System.out.println("Voice: Interested, questioning, normal pace, moderate pauses");
        audioManager.playVoiceWithContext(
            message,
            "curious",
            "normal",
            "curious"
        ).join();
        System.out.println();
        
        // Cooperative stage
        System.out.println("[COOPERATIVE STAGE]");
        System.out.println("Voice: Warm, friendly, thoughtful pace, longer pauses");
        audioManager.playVoiceWithContext(
            message,
            "friendly",
            "normal",
            "cooperative"
        ).join();
        System.out.println();
    }
    
    private static void demonstrateCombinedContext(CoquiTTSAudioManager audioManager) {
        System.out.println("3. COMBINED CONTEXT DEMONSTRATION");
        System.out.println("-".repeat(70));
        System.out.println("Demonstrating complex scenarios with multiple context factors...");
        System.out.println();
        
        // Scenario 1: Hostile + Critical
        System.out.println("[SCENARIO 1: Hostile & Critical]");
        System.out.println("Context: Hostile Mittenz warning about danger");
        audioManager.playVoiceWithContext(
            "Get away from there! Don't touch that! It's dangerous!",
            "angry",
            "critical",
            "hostile"
        ).join();
        System.out.println();
        
        // Scenario 2: Curious + High urgency
        System.out.println("[SCENARIO 2: Curious & High Urgency]");
        System.out.println("Context: Curious Mittenz discovering something important");
        audioManager.playVoiceWithContext(
            "Wait, what's that? I'm detecting something unusual. We should investigate this quickly.",
            "fascinated",
            "high",
            "curious"
        ).join();
        System.out.println();
        
        // Scenario 3: Cooperative + Normal (contemplative)
        System.out.println("[SCENARIO 3: Cooperative & Contemplative]");
        System.out.println("Context: Friendly Mittenz sharing thoughts");
        audioManager.playVoiceWithContext(
            "You know, looking at the stars like this. It makes me wonder what's really out there.",
            "contemplative",
            "normal",
            "cooperative"
        ).join();
        System.out.println();
        
        // Scenario 4: Cooperative + Critical (but friendly)
        System.out.println("[SCENARIO 4: Cooperative but Urgent]");
        System.out.println("Context: Friendly Mittenz warning a friend");
        audioManager.playVoiceWithContext(
            "Listen, we really need to fix this now. I don't want to lose you.",
            "worried",
            "critical",
            "cooperative"
        ).join();
        System.out.println();
    }
}
