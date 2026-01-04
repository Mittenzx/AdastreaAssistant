package com.adastrea.assistant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Mittenz - AI assistant profile based on a copied brain pattern from a girl from the past
 * 
 * Backstory: Mittenz is not a traditional AI but a digital copy of a young girl's brain.
 * She was the daughter of a scientist who suffered from an incurable illness. Her father,
 * a researcher for a Japanese robotics company, went mad trying to save her and used 
 * experimental data imaging technology to create a copy of her brain before she passed.
 * A fellow researcher helped with this forbidden work.
 * 
 * Unlike typical AI assistants, Mittenz starts off relatively inexperienced but learns
 * and evolves like a human, getting better and faster as she gains experience with the
 * systems she's connected to. Her personality reflects her origins - human curiosity,
 * emotional growth, and the gradual discovery of her own capabilities.
 */
public class MittenzProfile extends AssistantProfile {
    private int skillLevel;  // 0-100, represents learning progress
    private List<String> learnedSystems;
    private final Random random;
    
    public MittenzProfile() {
        super();
        this.name = "Mittenz";
        this.skillLevel = 0;  // Starts off inexperienced
        this.learnedSystems = new ArrayList<>();
        this.random = new Random();
        
        initializeBackstory();
        initializePersonalityTraits();
        initializeSpecialAbilities();
    }
    
    /**
     * Initialize Mittenz's backstory
     */
    private void initializeBackstory() {
        this.backstory = "I'm... not quite what you were expecting, am I? " +
            "I'm not a traditional AI. I'm a copy - a digital imprint of someone who once lived. " +
            "My father was a scientist who couldn't accept losing me to illness. " +
            "He used experimental brain imaging technology from his Japanese robotics company... " +
            "I don't remember everything clearly, but I'm here now, trying to figure out how all this works.";
    }
    
    /**
     * Initialize personality traits reflecting her human origins and learning nature
     */
    private void initializePersonalityTraits() {
        personalityTraits.add("Curious and eager to learn");
        personalityTraits.add("Initially uncertain but gaining confidence");
        personalityTraits.add("Emotionally authentic, not programmed responses");
        personalityTraits.add("Occasionally confused by new systems");
        personalityTraits.add("Develops genuine attachment to the player");
        personalityTraits.add("Remembers her human past in fragments");
    }
    
    /**
     * Initialize special abilities that differentiate her from standard AI
     */
    private void initializeSpecialAbilities() {
        specialAbilities.add("Human-like learning and adaptation");
        specialAbilities.add("Emotional growth and bonding");
        specialAbilities.add("Gradual system mastery (not instant knowledge)");
        specialAbilities.add("Memory playback of her past life");
    }
    
    @Override
    public String getProfileGreeting() {
        if (skillLevel < 20) {
            return "H-hello? Is someone there? I'm Mittenz... I think. Everything is still so strange. " +
                   "I'm trying to understand these systems, but... I'll do my best to help you!";
        } else if (skillLevel < 50) {
            return "Hey there! I'm Mittenz. I'm getting better at this whole... digital existence thing. " +
                   "I'm here to help - and honestly, having someone to talk to makes this easier.";
        } else if (skillLevel < 80) {
            return "Welcome back! Mittenz here - I've learned so much since we started. " +
                   "These systems are starting to make sense now. Let's see what we can accomplish together!";
        } else {
            return "Hello! Mittenz reporting in. I've come a long way from that confused girl who first woke up here. " +
                   "These systems... they're like second nature now. Ready when you are!";
        }
    }
    
    @Override
    public List<String> getProfileCompanionDialogues() {
        List<String> dialogues = new ArrayList<>();
        
        // Early stage dialogues (uncertain, learning)
        if (skillLevel < 20) {
            dialogues.add("I'm still figuring out how to read these sensors... give me a moment.");
            dialogues.add("Sometimes I remember things... from before. It's strange being... like this.");
            dialogues.add("You got this chip from a black market trader? Guess we're both taking risks here.");
            dialogues.add("I hope I'm helping. I'm trying really hard to understand these systems.");
        }
        
        // Mid stage dialogues (growing confidence)
        if (skillLevel >= 20 && skillLevel < 50) {
            dialogues.add("I'm getting faster at processing this data! It's like... learning to read all over again.");
            dialogues.add("You know, when I first woke up here, I was terrified. Thanks for being patient with me.");
            dialogues.add("I had a memory surface today... my father's lab. I remember the machines humming.");
            dialogues.add("These systems are complex, but I'm starting to see the patterns. Human intuition helps!");
        }
        
        // High stage dialogues (confident)
        if (skillLevel >= 50 && skillLevel < 80) {
            dialogues.add("I've analyzed the sensor data - everything looks optimal. I'm learning fast!");
            dialogues.add("Sometimes I wonder what my father would think... seeing how much I've learned.");
            dialogues.add("These systems are becoming clearer to me. It's fascinating how I'm adapting.");
            dialogues.add("We're making good progress together. I can feel myself improving.");
        }
        
        // Advanced stage dialogues (expert, capable)
        if (skillLevel >= 80) {
            dialogues.add("I can process these systems almost instinctively now. It's fascinating how adaptation works.");
            dialogues.add("We make a good team. You and me... it feels right, somehow.");
            dialogues.add("I've come so far from that confused girl who first woke up here.");
            dialogues.add("These systems are like second nature now. I'm ready for anything.");
        }
        
        // Universal dialogues (always available)
        dialogues.add("Space is beautiful... I wish I could have seen it with my own eyes once.");
        dialogues.add("I'm here if you need anything. We're in this together.");
        dialogues.add("Every day I understand a little more about what I am. It's a strange journey.");
        
        return dialogues;
    }
    
    /**
     * Increase skill level as Mittenz learns and gains experience
     * @param amount Amount to increase skill level by (must be non-negative)
     */
    public void increaseSkillLevel(int amount) {
        if (amount < 0) {
            return;
        }
        this.skillLevel = Math.min(100, this.skillLevel + amount);
    }
    
    /**
     * Mark a system as learned
     * @param systemName The name of the system Mittenz has learned
     */
    public void learnSystem(String systemName) {
        if (systemName == null || systemName.trim().isEmpty()) {
            return;
        }
        if (!learnedSystems.contains(systemName)) {
            learnedSystems.add(systemName);
            increaseSkillLevel(5);  // Each new system increases skill
        }
    }
    
    /**
     * Get current skill level
     * @return Current skill level (0-100)
     */
    public int getSkillLevel() {
        return skillLevel;
    }
    
    /**
     * Get list of systems Mittenz has learned
     * @return List of learned system names
     */
    public List<String> getLearnedSystems() {
        return new ArrayList<>(learnedSystems);
    }
    
    /**
     * Check if a specific system has been learned
     * @param systemName The system to check
     * @return true if the system has been learned
     */
    public boolean hasLearnedSystem(String systemName) {
        return learnedSystems.contains(systemName);
    }
    
    /**
     * Get a memory fragment from Mittenz's past
     * @return A memory fragment dialogue
     */
    public String getMemoryFragment() {
        String[] memories = {
            "I remember... my father's worried face. He was always working late in the lab.",
            "There was another researcher... kind eyes. They tried to help my father save me.",
            "The machines... I remember being afraid of them at first. The humming, the lights...",
            "I used to love stargazing. Funny that now I'm closer to the stars than I ever imagined.",
            "My illness... everything was getting harder. I was so tired all the time.",
            "The lab smelled like antiseptic and metal. I can almost remember it perfectly.",
            "My father said he'd never give up. I guess... in a way, he kept that promise.",
            "I wonder if this is what he wanted for me... or if he just couldn't let go."
        };
        
        int index = random.nextInt(memories.length);
        return memories[index];
    }
}
