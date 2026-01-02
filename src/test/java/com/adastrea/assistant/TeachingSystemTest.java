package com.adastrea.assistant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TeachingSystemTest {
    private TeachingSystem teachingSystem;

    @BeforeEach
    void setUp() {
        teachingSystem = new TeachingSystem();
    }

    @Test
    void testGetLesson() {
        String lesson = teachingSystem.getLesson("oxygen");
        assertNotNull(lesson);
        assertFalse(lesson.isEmpty());
    }

    @Test
    void testGetLessonNonExistent() {
        String lesson = teachingSystem.getLesson("nonexistent");
        assertNotNull(lesson);
        assertFalse(lesson.isEmpty());
    }

    @Test
    void testHasLesson() {
        assertTrue(teachingSystem.hasLesson("oxygen"));
        assertTrue(teachingSystem.hasLesson("gravity"));
        assertFalse(teachingSystem.hasLesson("nonexistent"));
    }

    @Test
    void testAddLesson() {
        teachingSystem.addLesson("custom", "Custom lesson content");
        assertTrue(teachingSystem.hasLesson("custom"));
        String lesson = teachingSystem.getLesson("custom");
        assertEquals("Custom lesson content", lesson);
    }

    @Test
    void testGetAvailableTopics() {
        String[] topics = teachingSystem.getAvailableTopics();
        assertNotNull(topics);
        assertTrue(topics.length > 0);
    }
}
