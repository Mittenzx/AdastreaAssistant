package com.adastrea.assistant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RelationshipStageTest {
    
    @Test
    void testRelationshipStageValues() {
        assertEquals(3, RelationshipStage.values().length);
        assertNotNull(RelationshipStage.valueOf("HOSTILE"));
        assertNotNull(RelationshipStage.valueOf("CURIOUS"));
        assertNotNull(RelationshipStage.valueOf("COOPERATIVE"));
    }

    @Test
    void testRelationshipStageOrder() {
        RelationshipStage[] stages = RelationshipStage.values();
        assertEquals(RelationshipStage.HOSTILE, stages[0]);
        assertEquals(RelationshipStage.CURIOUS, stages[1]);
        assertEquals(RelationshipStage.COOPERATIVE, stages[2]);
    }
}
