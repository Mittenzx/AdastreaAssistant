package com.adastrea.assistant;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Manages reminders for the player
 */
public class ReminderSystem {
    private final List<Reminder> reminders;

    public ReminderSystem() {
        this.reminders = new ArrayList<>();
    }

    /**
     * Add a new reminder
     * @param message The reminder message
     * @param delayMinutes Minutes until the reminder triggers
     */
    public void addReminder(String message, int delayMinutes) {
        LocalDateTime triggerTime = LocalDateTime.now().plusMinutes(delayMinutes);
        reminders.add(new Reminder(message, triggerTime));
    }

    /**
     * Check for due reminders and return them
     * @return List of due reminder messages
     */
    public List<String> checkDueReminders() {
        List<String> dueReminders = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        Iterator<Reminder> iterator = reminders.iterator();
        
        while (iterator.hasNext()) {
            Reminder reminder = iterator.next();
            if (reminder.isDue(now)) {
                dueReminders.add(reminder.getMessage());
                iterator.remove();
            }
        }
        
        return dueReminders;
    }

    /**
     * Get all active reminders
     * @return List of all reminders
     */
    public List<Reminder> getAllReminders() {
        return new ArrayList<>(reminders);
    }

    /**
     * Clear all reminders
     */
    public void clearAllReminders() {
        reminders.clear();
    }

    /**
     * Inner class representing a reminder
     */
    public static class Reminder {
        private final String message;
        private final LocalDateTime triggerTime;

        public Reminder(String message, LocalDateTime triggerTime) {
            this.message = message;
            this.triggerTime = triggerTime;
        }

        public String getMessage() {
            return message;
        }

        public LocalDateTime getTriggerTime() {
            return triggerTime;
        }

        public boolean isDue(LocalDateTime currentTime) {
            return currentTime.isAfter(triggerTime) || currentTime.isEqual(triggerTime);
        }
    }
}
