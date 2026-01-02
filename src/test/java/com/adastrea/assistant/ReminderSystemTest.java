package com.adastrea.assistant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ReminderSystemTest {
    private ReminderSystem reminderSystem;

    @BeforeEach
    void setUp() {
        reminderSystem = new ReminderSystem();
    }

    @Test
    void testAddReminder() {
        reminderSystem.addReminder("Test reminder", 5);
        List<ReminderSystem.Reminder> reminders = reminderSystem.getAllReminders();
        assertEquals(1, reminders.size());
    }

    @Test
    void testCheckDueReminders() {
        // Add a reminder that triggers immediately (negative delay puts it in the past)
        reminderSystem.addReminder("Immediate reminder", -1);
        
        List<String> dueReminders = reminderSystem.checkDueReminders();
        assertEquals(1, dueReminders.size());
        assertEquals("Immediate reminder", dueReminders.get(0));
    }

    @Test
    void testClearAllReminders() {
        reminderSystem.addReminder("Reminder 1", 5);
        reminderSystem.addReminder("Reminder 2", 10);
        reminderSystem.clearAllReminders();
        
        List<ReminderSystem.Reminder> reminders = reminderSystem.getAllReminders();
        assertEquals(0, reminders.size());
    }

    @Test
    void testReminderNotDueYet() {
        reminderSystem.addReminder("Future reminder", 60);
        List<String> dueReminders = reminderSystem.checkDueReminders();
        assertEquals(0, dueReminders.size());
    }
}
