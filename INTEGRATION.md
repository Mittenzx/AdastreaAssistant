# Integration Guide

## Integrating with Adastrea Minecraft Mod

This guide explains how to integrate the AdastraAssistant into the Adastrea (Ad Astra) Minecraft mod.

### Step 1: Add Dependency

Add this project as a dependency in your mod's `build.gradle`:

```gradle
dependencies {
    implementation 'com.adastrea:AdastraAssistant:1.0.0'
    // ... other dependencies
}
```

### Step 2: Initialize the Assistant

In your mod's main initialization class (typically in the `onInitialize()` or `@EventHandler` method):

```java
import com.adastrea.assistant.AIAssistant;
import com.adastrea.assistant.AssistantConfig;

public class AdastraMod {
    private AIAssistant assistant;
    
    public void onInitialize() {
        // Create and configure the assistant
        assistant = new AIAssistant("Astra");
        
        // Optional: Load configuration
        AssistantConfig config = loadConfig();
        assistant.setEnabled(config.isEnabled());
        assistant.getAudioManager().setAudioEnabled(config.isAudioEnabled());
        assistant.getAudioManager().setVolume(config.getVolume());
        assistant.getVisualManager().setVisualEnabled(config.isVisualEnabled());
        
        // Initialize the assistant
        assistant.initialize();
    }
}
```

### Step 3: Hook into Game Events

Connect the assistant to relevant game events:

#### Player Login/World Join
```java
@SubscribeEvent
public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
    if (assistant.isEnabled()) {
        assistant.initialize();
    }
}
```

#### Tutorial/First Time Experience
```java
public void showTutorial(String topic) {
    assistant.teach(topic);
}

// Example usage:
if (isFirstSpaceLaunch) {
    assistant.teach("oxygen");
    assistant.teach("starter");
}
```

#### Periodic Companion Dialogue
```java
// In a tick handler or scheduled task
private int companionDialogueTicks = 0;
private static final int DIALOGUE_INTERVAL = 12000; // 10 minutes at 20 tps

public void onTick() {
    companionDialogueTicks++;
    
    if (companionDialogueTicks >= DIALOGUE_INTERVAL) {
        assistant.provideCompanionDialogue();
        companionDialogueTicks = 0;
    }
    
    // Also check reminders every second
    if (companionDialogueTicks % 20 == 0) {
        assistant.checkReminders();
    }
}
```

#### Context-Aware Responses
```java
// When player enters a new planet
@SubscribeEvent
public void onChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
    if (isSpacePlanet(event.getTo())) {
        assistant.speak("Welcome to a new planet! Watch your oxygen and temperature.");
    }
}

// When oxygen is low
public void checkOxygenLevel(Player player) {
    float oxygen = getOxygenLevel(player);
    if (oxygen < 20 && oxygen > 15) {
        assistant.setReminder("Your oxygen is running low!", 0);
    }
}
```

### Step 4: UI Integration

#### Subtitle Rendering
```java
// In your HUD/overlay rendering code
public void renderOverlay(MatrixStack matrices, float tickDelta) {
    String subtitle = assistant.getVisualManager().getCurrentSubtitle();
    if (!subtitle.isEmpty()) {
        // Render subtitle at bottom center of screen
        renderSubtitle(matrices, subtitle, width / 2, height - 50);
    }
}
```

#### Notification Display
```java
// Poll and display notifications
public void renderNotifications(MatrixStack matrices) {
    List<String> notifications = assistant.getVisualManager().getActiveNotifications();
    int y = 10;
    for (String notification : notifications) {
        renderNotification(matrices, notification, 10, y);
        y += 30;
    }
}
```

#### Assistant Icon/Avatar
```java
// Display assistant avatar in corner
public void renderAssistantIcon(MatrixStack matrices) {
    if (assistant.isEnabled()) {
        // Render animated icon at top-right
        renderIcon(matrices, width - 50, 10);
    }
}
```

### Step 5: Audio Integration

For text-to-speech integration with Minecraft's sound system:

```java
public class MinecraftAudioManager extends AudioManager {
    private final SoundManager soundManager;
    
    @Override
    public void playVoice(String message) {
        // Option 1: Use pre-recorded voice lines
        SoundEvent soundEvent = getSoundForMessage(message);
        soundManager.play(soundEvent);
        
        // Option 2: Use external TTS library
        // byte[] audioData = textToSpeech(message);
        // playCustomSound(audioData);
    }
}
```

### Step 6: Player Commands

Add commands for players to interact with the assistant:

```java
// /assistant ask <question>
@Command
public void askAssistant(Player player, String question) {
    String response = assistant.respondToQuery(question);
    player.sendMessage(response);
}

// /assistant teach <topic>
@Command
public void teachTopic(Player player, String topic) {
    assistant.teach(topic);
}

// /assistant remind <minutes> <message>
@Command
public void setReminder(Player player, int minutes, String message) {
    assistant.setReminder(message, minutes);
    player.sendMessage("Reminder set for " + minutes + " minutes from now");
}
```

### Step 7: Configuration File

Create a configuration file that players can modify:

```java
public AssistantConfig loadConfig() {
    // Load from config file
    Properties props = new Properties();
    props.load(new FileInputStream("config/assistant.properties"));
    
    AssistantConfig config = new AssistantConfig();
    config.setEnabled(Boolean.parseBoolean(props.getProperty("assistant.enabled", "true")));
    config.setAudioEnabled(Boolean.parseBoolean(props.getProperty("assistant.audio.enabled", "true")));
    config.setVolume(Float.parseFloat(props.getProperty("assistant.audio.volume", "1.0")));
    // ... load other settings
    
    return config;
}
```

### Advanced Features

#### Custom Dialogues for Mod Content
```java
// Add custom dialogues specific to your mod
assistant.getDialogueSystem().addGreeting("Ready for another adventure in space?");
assistant.getDialogueSystem().addCompanionDialogue("This planet's atmosphere is interesting!");

// Add custom lessons
assistant.getTeachingSystem().addLesson("rocket_fuel", 
    "Rocket fuel can be crafted from petroleum and oxygen. Make sure to stock up before long journeys!");
```

#### Mood-Based Responses
```java
public void updateAssistantMood(String emotion) {
    assistant.getVisualManager().showAssistantIcon(emotion);
    
    // Add contextual dialogue based on mood
    if (emotion.equals("worried")) {
        assistant.speak("I'm detecting some concerning readings. Stay alert!");
    }
}
```

#### Multi-language Support
```java
public class LocalizedDialogueSystem extends DialogueSystem {
    private final String language;
    
    @Override
    public String getGreeting() {
        return loadLocalizedString("greeting", language);
    }
}
```

## Testing Integration

Test the integration thoroughly:

1. **Startup Test**: Verify assistant initializes without errors
2. **Dialogue Test**: Confirm companion dialogues appear periodically
3. **Teaching Test**: Trigger tutorial sequences and verify lessons display
4. **Reminder Test**: Set reminders and verify they trigger correctly
5. **Performance Test**: Ensure no performance impact from periodic checks
6. **UI Test**: Verify subtitles and notifications render properly

## Troubleshooting

### Assistant not speaking
- Check if `audioEnabled` is set to true
- Verify audio manager is properly initialized
- Check volume is not set to 0

### Reminders not triggering
- Ensure `checkReminders()` is called periodically
- Verify game time is progressing correctly

### Performance issues
- Increase interval between companion dialogues
- Reduce frequency of reminder checks
- Consider async processing for heavy operations

## Example Complete Integration

See the `examples/` directory for a complete integration example with a minimal Minecraft mod.
