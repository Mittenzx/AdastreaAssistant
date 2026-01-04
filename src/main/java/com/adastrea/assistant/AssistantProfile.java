package com.adastrea.assistant;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for AI assistant character profiles
 * Contains personality traits, backstory, and behavioral characteristics
 */
public abstract class AssistantProfile {
    protected String name;
    protected String backstory;
    protected List<String> personalityTraits;
    protected List<String> specialAbilities;
    
    public AssistantProfile() {
        this.personalityTraits = new ArrayList<>();
        this.specialAbilities = new ArrayList<>();
    }
    
    /**
     * Get the assistant's name
     * @return The name of the assistant
     */
    public String getName() {
        return name;
    }
    
    /**
     * Get the assistant's backstory
     * @return The backstory description
     */
    public String getBackstory() {
        return backstory;
    }
    
    /**
     * Get the assistant's personality traits
     * @return List of personality traits
     */
    public List<String> getPersonalityTraits() {
        return new ArrayList<>(personalityTraits);
    }
    
    /**
     * Get the assistant's special abilities
     * @return List of special abilities
     */
    public List<String> getSpecialAbilities() {
        return new ArrayList<>(specialAbilities);
    }
    
    /**
     * Get profile-specific greeting dialogue
     * @return A greeting message appropriate for this character
     */
    public abstract String getProfileGreeting();
    
    /**
     * Get profile-specific companion dialogues
     * @return List of companion dialogue options
     */
    public abstract List<String> getProfileCompanionDialogues();
    
    /**
     * Check if the assistant has a specific ability
     * @param ability The ability to check for
     * @return true if the assistant has this ability
     */
    public boolean hasAbility(String ability) {
        return specialAbilities.contains(ability);
    }
}
