# Sample Audio Files for AdastreaAssistant

This directory contains sample audio files to demonstrate the audio capabilities of the AdastreaAssistant system. These are placeholder audio files that can be used for testing and development purposes.

## Directory Structure

```
audio/
├── greetings/          # Welcome and greeting sounds
├── dialogue/           # Character dialogue and interaction sounds
├── notifications/      # Alert and reminder notifications
└── sounds/            # UI interaction sounds
```

## Audio File Categories

### Greetings (`greetings/`)

Audio files for initial greetings and welcome messages:

- **`welcome.wav`** - Pleasant welcome greeting with ascending chord (C5, E5, G5)
  - Duration: 1.5 seconds
  - Use case: First-time startup or player login
  
- **`hello.wav`** - Simple friendly greeting tone (E5)
  - Duration: 1.0 second
  - Use case: General greeting, casual hello
  
- **`startup.wav`** - System startup sound with rising tones (G4, B4, D5)
  - Duration: 0.9 seconds
  - Use case: Assistant initialization

### Dialogue (`dialogue/`)

Audio files representing different character states and interactions for Mittenz:

- **`mittenz_hostile.wav`** - Harsh, dissonant tones (Bb4, B4)
  - Duration: 1.5 seconds
  - Use case: HOSTILE relationship stage dialogue
  - Character: Bratty, confused, demanding
  
- **`mittenz_curious.wav`** - Questioning tone with rising pitch (A4, B4, C#5)
  - Duration: 1.2 seconds
  - Use case: CURIOUS relationship stage dialogue
  - Character: Questioning, slightly insightful
  
- **`mittenz_cooperative.wav`** - Harmonious, warm chord (G4, B4, D5, F5)
  - Duration: 2.0 seconds
  - Use case: COOPERATIVE relationship stage dialogue
  - Character: Accepting, "we need to" conversations
  
- **`companion_message.wav`** - Friendly tone (C5)
  - Duration: 1.2 seconds
  - Use case: Random companion dialogue, friendly check-ins
  
- **`acknowledgment.wav`** - Short positive tone (E5)
  - Duration: 0.5 seconds
  - Use case: Response acknowledgment, confirmation

### Notifications (`notifications/`)

Audio files for alerts, reminders, and teaching moments:

- **`reminder.wav`** - Gentle bell-like notification sound (A5)
  - Duration: 1.5 seconds
  - Use case: Time-based reminders, task alerts
  
- **`alert.wav`** - More urgent notification (C6)
  - Duration: 1.0 second
  - Use case: Important alerts, warnings
  
- **`teaching.wav`** - Educational tone (C5, E5)
  - Duration: 1.2 seconds
  - Use case: Teaching system notifications, tutorial prompts
  
- **`success.wav`** - Positive ascending tones (C5, E5, G5)
  - Duration: 0.75 seconds
  - Use case: Task completion, achievement unlocked

### UI Sounds (`sounds/`)

Audio files for user interface interactions:

- **`button_click.wav`** - Short click sound (C6)
  - Duration: 0.1 seconds
  - Use case: Button presses, menu selections
  
- **`menu_open.wav`** - Ascending chirp (E5, G5, C6)
  - Duration: 0.36 seconds
  - Use case: Opening menus, expanding UI elements
  
- **`menu_close.wav`** - Descending chirp (C6, G5, E5)
  - Duration: 0.36 seconds
  - Use case: Closing menus, collapsing UI elements
  
- **`error.wav`** - Low dissonant tone (A3, Bb3)
  - Duration: 0.8 seconds
  - Use case: Error messages, invalid actions
  
- **`interaction.wav`** - Light tap sound (A5)
  - Duration: 0.15 seconds
  - Use case: General interactions, hover effects

## Technical Specifications

All audio files are in WAV format with the following specifications:
- **Sample Rate**: 44100 Hz (CD quality)
- **Bit Depth**: 16-bit
- **Channels**: Mono (1 channel)
- **Format**: PCM uncompressed

## Usage in Code

### Using AudioManager

The `AudioManager` class provides methods to preload and play audio files:

```java
AudioManager audioManager = new AudioManager();

// Preload audio for faster playback
audioManager.preloadAudio("welcome", "audio/greetings/welcome.wav");
audioManager.preloadAudio("hostile", "audio/dialogue/mittenz_hostile.wav");
audioManager.preloadAudio("reminder", "audio/notifications/reminder.wav");
audioManager.preloadAudio("click", "audio/sounds/button_click.wav");

// Get preloaded audio path
String welcomePath = audioManager.getPreloadedAudio("welcome");

// Play sound effect
audioManager.playSoundEffect("click");
```

### Integration with Minecraft

When integrating with Minecraft, override the `AudioManager` methods to use Minecraft's sound system:

```java
public class MinecraftAudioManager extends AudioManager {
    @Override
    public void playSoundEffect(String soundName) {
        if (!isAudioEnabled()) {
            return;
        }
        
        String audioPath = getPreloadedAudio(soundName);
        if (audioPath != null) {
            // Use Minecraft's sound system
            minecraft.getSoundManager().play(new ResourceLocation(audioPath));
        }
    }
}
```

### Relationship Stage-Specific Audio

Use different audio based on Mittenz's relationship stage:

```java
AIAssistant assistant = new AIAssistant();
MittenzProfile mittenz = new MittenzProfile();
assistant.setProfile(mittenz);

// Preload stage-specific audio
audioManager.preloadAudio("hostile", "audio/dialogue/mittenz_hostile.wav");
audioManager.preloadAudio("curious", "audio/dialogue/mittenz_curious.wav");
audioManager.preloadAudio("cooperative", "audio/dialogue/mittenz_cooperative.wav");

// Play audio based on current stage
RelationshipStage stage = mittenz.getRelationshipStage();
switch (stage) {
    case HOSTILE:
        audioManager.playSoundEffect("hostile");
        break;
    case CURIOUS:
        audioManager.playSoundEffect("curious");
        break;
    case COOPERATIVE:
        audioManager.playSoundEffect("cooperative");
        break;
}
```

## Customization

These are placeholder audio files. For production use, you should:

1. **Replace with professional recordings**: Use voice actors for Mittenz's dialogue
2. **Implement Text-to-Speech**: Integrate TTS engines like:
   - Google Cloud Text-to-Speech
   - Amazon Polly
   - Microsoft Azure Speech
   - Local TTS libraries (espeak, festival)
3. **Add voice variety**: Create multiple variations of each sound
4. **Implement voice modulation**: Adjust pitch/tone based on:
   - Relationship stage (hostile = harsh, cooperative = warm)
   - Skill level (0-100)
   - Emotional context
5. **Add ambient sounds**: Background audio for immersion

## Future Enhancements

Planned improvements for the audio system:

- **Dynamic voice synthesis**: Real-time TTS with emotion control
- **Voice cloning**: Consistent character voice across all dialogue
- **Spatial audio**: 3D positional audio in the game world
- **Audio mixing**: Layer multiple sounds with proper volume control
- **Localization**: Multi-language voice support
- **Accessibility**: Audio cues for visually impaired players

## License

These sample audio files are part of the AdastreaAssistant project and are provided for development and testing purposes.

## Contributing

To add new audio samples:
1. Follow the naming convention: `category_name.wav`
2. Use 44100 Hz, 16-bit, mono WAV format
3. Keep file sizes reasonable (< 500 KB for short sounds)
4. Document the new audio in this README
5. Update code examples if new categories are added

For questions or suggestions, please open an issue in the repository.
