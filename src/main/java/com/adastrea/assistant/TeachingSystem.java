package com.adastrea.assistant;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides teaching and tutorial content to help players learn game mechanics
 */
public class TeachingSystem {
    private final Map<String, String> lessons;

    public TeachingSystem() {
        this.lessons = new HashMap<>();
        initializeLessons();
    }

    /**
     * Initialize default lessons
     */
    private void initializeLessons() {
        // Basic mechanics
        lessons.put("oxygen", 
            "Oxygen is essential for survival in space. Monitor your oxygen meter and refill at oxygen stations or return to your spacecraft.");
        
        lessons.put("gravity", 
            "Each celestial body has different gravity. Lower gravity allows higher jumps but affects movement speed. Adjust your play style accordingly.");
        
        lessons.put("temperature", 
            "Temperature varies greatly in space. Extreme heat or cold can damage your suit. Use appropriate protective gear and seek shelter when needed.");
        
        lessons.put("fuel", 
            "Your spacecraft requires fuel to travel between planets. Make sure to stock up before long journeys and plan your route carefully.");
        
        lessons.put("resources", 
            "Different planets contain unique resources. Scan planets before landing to identify valuable materials worth collecting.");
        
        lessons.put("crafting", 
            "Use collected resources to craft better equipment, tools, and upgrades. Access your crafting interface to see available recipes.");
        
        lessons.put("navigation", 
            "Use your star map to plan routes between planets. Consider distance, fuel requirements, and available resources at your destination.");
        
        lessons.put("exploration", 
            "Exploration is rewarded! Look for hidden caves, abandoned structures, and rare materials on each planet you visit.");
        
        // General tips
        lessons.put("safety", 
            "Always check your life support systems before venturing out. Carry backup oxygen and emergency supplies.");
        
        lessons.put("starter", 
            "Welcome to space exploration! Start by familiarizing yourself with your base, craft basic tools, and make short trips to nearby locations.");
    }

    /**
     * Get a lesson on a specific topic
     * @param topic The topic to learn about
     * @return The lesson content
     */
    public String getLesson(String topic) {
        String lesson = lessons.get(topic.toLowerCase().trim());
        if (lesson == null) {
            return "I don't have specific information on that topic yet, but I'm always learning! Try asking about: oxygen, gravity, fuel, resources, or navigation.";
        }
        return lesson;
    }

    /**
     * Add a custom lesson
     * @param topic The topic key
     * @param content The lesson content
     */
    public void addLesson(String topic, String content) {
        lessons.put(topic.toLowerCase().trim(), content);
    }

    /**
     * Check if a lesson exists for a topic
     * @param topic The topic to check
     * @return true if lesson exists
     */
    public boolean hasLesson(String topic) {
        return lessons.containsKey(topic.toLowerCase().trim());
    }

    /**
     * Get all available topics
     * @return Array of all lesson topics
     */
    public String[] getAvailableTopics() {
        return lessons.keySet().toArray(new String[0]);
    }
}
