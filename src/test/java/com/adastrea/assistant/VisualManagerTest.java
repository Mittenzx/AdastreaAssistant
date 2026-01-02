package com.adastrea.assistant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class VisualManagerTest {
    private VisualManager visualManager;

    @BeforeEach
    void setUp() {
        visualManager = new VisualManager();
    }

    @Test
    void testVisualEnabledByDefault() {
        assertTrue(visualManager.isVisualEnabled());
    }

    @Test
    void testSetVisualEnabled() {
        visualManager.setVisualEnabled(false);
        assertFalse(visualManager.isVisualEnabled());
        
        visualManager.setVisualEnabled(true);
        assertTrue(visualManager.isVisualEnabled());
    }

    @Test
    void testSubtitleDurationDefault() {
        assertEquals(5000, visualManager.getSubtitleDuration());
    }

    @Test
    void testSetSubtitleDuration() {
        visualManager.setSubtitleDuration(7000);
        assertEquals(7000, visualManager.getSubtitleDuration());
    }

    @Test
    void testShowNotification() {
        visualManager.showNotification("Test notification");
        List<String> notifications = visualManager.getActiveNotifications();
        assertEquals(1, notifications.size());
        assertEquals("Test notification", notifications.get(0));
    }

    @Test
    void testShowNotificationWhenDisabled() {
        visualManager.setVisualEnabled(false);
        visualManager.showNotification("Test notification");
        List<String> notifications = visualManager.getActiveNotifications();
        assertEquals(0, notifications.size());
    }

    @Test
    void testMultipleNotifications() {
        visualManager.showNotification("Notification 1");
        visualManager.showNotification("Notification 2");
        List<String> notifications = visualManager.getActiveNotifications();
        assertEquals(2, notifications.size());
    }

    @Test
    void testClearNotifications() {
        visualManager.showNotification("Test notification");
        visualManager.clearNotifications();
        List<String> notifications = visualManager.getActiveNotifications();
        assertEquals(0, notifications.size());
    }

    @Test
    void testShowSubtitle() {
        visualManager.showSubtitle("Test subtitle");
        assertEquals("Test subtitle", visualManager.getCurrentSubtitle());
    }

    @Test
    void testShowSubtitleWhenDisabled() {
        visualManager.setVisualEnabled(false);
        visualManager.showSubtitle("Test subtitle");
        assertEquals("", visualManager.getCurrentSubtitle());
    }

    @Test
    void testClearSubtitle() {
        visualManager.showSubtitle("Test subtitle");
        visualManager.clearSubtitle();
        assertEquals("", visualManager.getCurrentSubtitle());
    }

    @Test
    void testShowAssistantIcon() {
        assertDoesNotThrow(() -> visualManager.showAssistantIcon("happy"));
    }

    @Test
    void testShowAssistantIconWhenDisabled() {
        visualManager.setVisualEnabled(false);
        assertDoesNotThrow(() -> visualManager.showAssistantIcon("happy"));
    }

    @Test
    void testGetActiveNotificationsReturnsDefensiveCopy() {
        visualManager.showNotification("Test");
        List<String> notifications = visualManager.getActiveNotifications();
        notifications.clear();
        // Original should still have the notification
        assertEquals(1, visualManager.getActiveNotifications().size());
    }
}
