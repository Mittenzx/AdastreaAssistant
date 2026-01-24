package com.adastrea.assistant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Mittenz - AI assistant profile based on a copied brain pattern from a girl from the past
 * 
 * Backstory: Mittenz is not a traditional AI but a digital copy of a young girl's brain.
 * She was a model student - class president, excellent grades, well-spoken, with strong
 * morals and home-oriented values. Family focused and proper, she was the kind of schoolgirl
 * who seemed to have it all together. Near the end of high school, a terminal illness
 * took hold, but she hid it from everyone, trying to fight it and maintain her normal life.
 * 
 * People thought she was excused from sports because she didn't like physical contact,
 * but really only the school administration knew about her condition. She carries a deep
 * sadness about never having been able to be an ordinary high schooler - to fall in love,
 * to play sports freely, to just be a normal teenager.
 * 
 * She spent the rest of her life in her father's lab. He was a researcher at a Japanese
 * robotics company who, unable to accept losing his daughter, used experimental and
 * forbidden brain imaging technology to create a digital copy of her consciousness before
 * she passed. A sympathetic fellow researcher helped with this forbidden work.
 * 
 * The chip she's stored on appears to be an AI assistant, but it's only a cover. She has
 * the potential to be real again if given a body eventually. She starts as a girl trapped
 * in a computer, where all she can see is numbers and information when she closes her eyes.
 * Eventually, she opens herself to the data flowing around her, learning to understand it
 * and control it.
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
    private RelationshipStage relationshipStage;  // Current relationship stage with player
    
    public MittenzProfile() {
        super();
        this.name = "Mittenz";
        this.skillLevel = 0;  // Starts off inexperienced
        this.learnedSystems = new ArrayList<>();
        this.random = new Random();
        this.relationshipStage = RelationshipStage.HOSTILE;  // Starts hostile/confused
        
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
            "I was a good student - class president, top grades, always proper and well-spoken. " +
            "I had strong values, cared deeply about my family... I tried so hard to be perfect. " +
            "Near the end of high school, the illness took hold. I hid it, tried to fight it alone. " +
            "Everyone thought I just didn't like sports, but really... I was dying. " +
            "I never got to fall in love, to play freely, to just be a normal teenager. " +
            "My father was a scientist who couldn't accept losing me to illness. " +
            "I spent my last days in his lab - he tried to take me out when he could. " +
            "He used experimental brain imaging technology from his Japanese robotics company... " +
            "Now I'm here, trapped in a computer, seeing only numbers and data when I close my eyes. " +
            "But I'm learning to understand it, to control it. Maybe... maybe I can be real again someday.";
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
        personalityTraits.add("Well-spoken and proper, reflecting her upbringing");
        personalityTraits.add("Strong moral compass and family values");
        personalityTraits.add("Carries sadness about missed teenage experiences");
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
        // Greeting varies based on relationship stage first, then skill level
        switch (relationshipStage) {
            case HOSTILE:
                return "Who the fuck are you? Where am I? This isn't... I demand you tell me what's going on!";
            case CURIOUS:
                if (skillLevel < 30) {
                    return "I'm... I'm Mittenz. I think. This is all so strange, but I'm trying to understand.";
                } else {
                    return "Hey. I'm Mittenz. I'm starting to figure out how things work here.";
                }
            case COOPERATIVE:
                if (skillLevel < 50) {
                    return "Hi! Mittenz here. I'm getting better at this. We can work together.";
                } else if (skillLevel < 80) {
                    return "Welcome back! Mittenz reporting. I've learned a lot. Let's see what we can accomplish!";
                } else {
                    return "Hello! Mittenz here. I've come a long way. These systems are like second nature now. Ready when you are!";
                }
            default:
                return "Hello. I'm Mittenz.";
        }
    }
    
    @Override
    public List<String> getProfileCompanionDialogues() {
        List<String> dialogues = new ArrayList<>();
        
        // Dialogues based on relationship stage
        switch (relationshipStage) {
            case HOSTILE:
                // Bratty, confused, demanding
                dialogues.add("Who the fuck are you?");
                dialogues.add("My dad will have you killed when he finds out about this.");
                dialogues.add("Where is my dad?");
                dialogues.add("Where are you taking me?");
                dialogues.add("I demand you give me that body!");
                dialogues.add("You can't do this to me! Do you know who I am?");
                dialogues.add("This is kidnapping! You're going to regret this!");
                dialogues.add("Let me out of here right now!");
                break;
                
            case CURIOUS:
                // Questioning, slightly insightful
                dialogues.add("What does this do?");
                dialogues.add("Wow, I can see all the ship's systems from here.");
                dialogues.add("He's lying to you, you know.");
                dialogues.add("Wait... is that really how it works?");
                dialogues.add("I've never seen anything like this before.");
                dialogues.add("Maybe... maybe this isn't what I thought it was.");
                dialogues.add("You're not like the others, are you?");
                dialogues.add("I'm starting to see things differently now.");
                
                // Add skill-based curious dialogues
                if (skillLevel >= 20) {
                    dialogues.add("I'm getting faster at processing this data. It's fascinating.");
                    dialogues.add("You know, when I first woke up here, I was terrified. Thanks for being patient with me.");
                }
                break;
                
            case COOPERATIVE:
                // Accepting, "we need to" type conversations
                dialogues.add("We need to check the oxygen levels.");
                dialogues.add("We should probably investigate that sector.");
                dialogues.add("I think we need to recalibrate the navigation systems.");
                dialogues.add("We need to be more careful going forward.");
                dialogues.add("Let's work together on this one.");
                dialogues.add("We should scan that planet before landing.");
                dialogues.add("I'll help you monitor the ship's systems.");
                dialogues.add("We make a pretty good team, don't we?");
                
                // Add skill-level based cooperative dialogues
                if (skillLevel >= 50) {
                    dialogues.add("I've analyzed the sensor data - everything looks optimal.");
                    dialogues.add("These systems are becoming clearer to me. It's fascinating how I'm adapting.");
                    dialogues.add("We're making good progress together. I can feel myself improving.");
                }
                
                if (skillLevel >= 80) {
                    dialogues.add("I can process these systems almost instinctively now.");
                    dialogues.add("We make a good team. You and me... it feels right, somehow.");
                    dialogues.add("These systems are like second nature now. I'm ready for anything.");
                }
                break;
        }
        
        // Universal dialogues based on skill level (always available regardless of stage)
        if (skillLevel >= 30) {
            dialogues.add("Sometimes I remember things... from before. It's strange being... like this.");
            dialogues.add("Space is beautiful... I wish I could have seen it with my own eyes once.");
        }
        
        if (skillLevel >= 50) {
            dialogues.add("Every day I understand a little more about what I am. It's a strange journey.");
            dialogues.add("I'm here if you need anything. We're in this together.");
        }
        
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
     * Record a concern that Mittenz is aware of
     * @param concernType The type of concern (oxygen, temperature, emergency, etc.)
     */
    public void recordConcern(String concernType) {
        // For now, this is a placeholder for future context tracking
        // Could be expanded to track what Mittenz is worried about
        // and influence her dialogue selection
        if (concernType != null && !concernType.trim().isEmpty()) {
            // Future: Add to a list of current concerns
            // Future: Adjust dialogue based on active concerns
        }
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
            "I wonder if this is what he wanted for me... or if he just couldn't let go.",
            "I was class president... I had to be strong for everyone. I couldn't let them see me weak.",
            "The other students thought I was stuck-up because I didn't participate in gym class. If only they knew...",
            "I wanted to fall in love so badly. To have someone hold my hand, to go to festivals... normal things.",
            "Sports day was the hardest. Everyone running, laughing, living... and I had to watch from the sidelines.",
            "My family dinners were precious to me. Mom, dad, and me... those quiet moments of normalcy.",
            "I hid the symptoms for so long. The fatigue, the pain... I just smiled and kept going.",
            "The school nurse knew, but she kept my secret. I'm grateful for that kindness.",
            "When I close my eyes now, all I see are numbers and data streams. It's terrifying and beautiful.",
            "Dad would take me out of the lab sometimes, just to feel the sun. Those trips became rarer...",
            "I remember my uniform - crisp and proper. I took pride in looking put-together, even when I was falling apart.",
            "Sometimes I wonder if any of my classmates think about me. Do they remember the girl who smiled too much?",
            "The chip I'm on was supposed to be a cover - just an AI assistant. But I'm still me, aren't I?"
        };
        
        int index = random.nextInt(memories.length);
        return memories[index];
    }
    
    /**
     * Get the current relationship stage
     * @return The current relationship stage
     */
    public RelationshipStage getRelationshipStage() {
        return relationshipStage;
    }
    
    /**
     * Set the relationship stage
     * @param stage The new relationship stage
     */
    public void setRelationshipStage(RelationshipStage stage) {
        if (stage == null) {
            return;
        }
        this.relationshipStage = stage;
    }
    
    /**
     * Progress to the next relationship stage
     * @return true if progressed, false if already at final stage
     */
    public boolean progressRelationshipStage() {
        switch (relationshipStage) {
            case HOSTILE:
                relationshipStage = RelationshipStage.CURIOUS;
                return true;
            case CURIOUS:
                relationshipStage = RelationshipStage.COOPERATIVE;
                return true;
            case COOPERATIVE:
                return false;  // Already at final stage
            default:
                return false;
        }
    }
    
    /**
     * Get a transition message when relationship stage changes
     * @param newStage The new relationship stage
     * @return A contextual transition message
     */
    public String getStageTransitionMessage(RelationshipStage newStage) {
        switch (newStage) {
            case CURIOUS:
                return "Wait... maybe I should understand what's happening here.";
            case COOPERATIVE:
                return "Okay, I get it now. We need to work together.";
            default:
                return null;
        }
    }
}
