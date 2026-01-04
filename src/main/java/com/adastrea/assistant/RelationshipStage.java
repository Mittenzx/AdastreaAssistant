package com.adastrea.assistant;

/**
 * Represents the current stage of the player's relationship with Mittenz.
 * Mittenz progresses through these stages as the story unfolds.
 */
public enum RelationshipStage {
    /**
     * Initial hostile stage - Mittenz is bratty, confused, and demanding
     */
    HOSTILE,
    
    /**
     * Transitional curious stage - Mittenz starts asking questions and showing insight
     */
    CURIOUS,
    
    /**
     * Final cooperative stage - Mittenz accepts the situation and works with the player
     */
    COOPERATIVE
}
