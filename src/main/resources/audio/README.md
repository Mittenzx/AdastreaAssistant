# Sample Audio Files for AdastreaAssistant

This directory contains sample audio files to demonstrate the audio capabilities of the AdastreaAssistant system.

## Audio Types Included

This directory now contains **TWO types** of audio samples:

### 1. **Voice Samples** (TTS-Generated Speech) ✨ NEW
- **19 voice samples** featuring Mittenz speaking actual dialogue
- Generated using espeak text-to-speech engine
- Female voice with young characteristics (pitch-adjusted)
- Dialogue from all three relationship stages (Hostile, Curious, Cooperative)
- **File naming:** `greeting_*.wav`, `mittenz_*_01.wav`, `*_01.wav` etc.

### 2. **Tone-Based Placeholders** (Musical Tones)
- **17 tone-based** files (synthesized musical tones and chords)
- Simple beeps and chords for basic audio testing
- **File naming:** `welcome.wav`, `hello.wav`, `mittenz_hostile.wav` (without numbers)

**Recommendation:** Use the **voice samples** (numbered files like `mittenz_hostile_01.wav`) to hear Mittenz speak! The tone-based files are kept for backwards compatibility and simple audio system testing.

## Directory Structure

```
audio/
├── greetings/          # Welcome and greeting sounds
├── dialogue/           # Character dialogue and interaction sounds
├── notifications/      # Alert and reminder notifications
└── sounds/            # UI interaction sounds
```

## 🎙️ Voice Samples (Mittenz Speaking!)

These TTS-generated voice samples let you **hear Mittenz actually speak**! Generated using espeak with female voice characteristics tuned for a young, well-spoken character.

### Greetings - Voice Samples

- **`greeting_initial.wav`** - *"Hello there! I'm here to help you on your space adventure."*
  - First meeting greeting
  
- **`greeting_startup.wav`** - *"System initializing. Mittenz here, ready to assist."*
  - System initialization message
  
- **`greeting_welcome.wav`** - *"Welcome back! Let's continue our journey through the stars."*
  - Returning player greeting

### Dialogue - Voice Samples by Relationship Stage

#### HOSTILE Stage (Angry, Demanding)
> **Note:** HOSTILE stage dialogue contains mild profanity reflecting the character's initial distressed/angry state. For family-friendly versions, consider alternative dialogue or filtering.

- **`mittenz_hostile_01.wav`** - *"Who the hell are you? Where is my dad?"*
- **`mittenz_hostile_02.wav`** - *"You can't do this to me! Do you know who I am?"*
- **`mittenz_hostile_03.wav`** - *"I demand you tell me what's going on right now!"*

#### CURIOUS Stage (Questioning, Interested)
- **`mittenz_curious_01.wav`** - *"What does this do? I've never seen anything like this before."*
- **`mittenz_curious_02.wav`** - *"Wow, I can see all the ship's systems from here."*
- **`mittenz_curious_03.wav`** - *"I'm starting to see things differently now."*

#### COOPERATIVE Stage (Friendly, Collaborative)
- **`mittenz_cooperative_01.wav`** - *"We need to check the oxygen levels."*
- **`mittenz_cooperative_02.wav`** - *"Let's work together on this one."*
- **`mittenz_cooperative_03.wav`** - *"We make a pretty good team, don't we?"*

#### Companion Messages
- **`companion_message_01.wav`** - *"The stars sure are beautiful today, aren't they?"*
- **`companion_message_02.wav`** - *"I'm here if you need any guidance or just want to chat."*
- **`acknowledgment.wav`** - *"Understood. I'll take care of that."*

### Notifications - Voice Samples
- **`reminder_01.wav`** - *"Reminder: It's time to check your fuel levels."*
- **`alert_oxygen.wav`** - *"Warning! Oxygen levels are getting low!"*
- **`teaching_intro.wav`** - *"Let me teach you about this system."*
- **`success.wav`** - *"Great job! We did it!"*

---

## 🎵 Tone-Based Samples (Original)

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

### Voice Samples (TTS-Generated)
- **Sample Rate**: 44100 Hz (CD quality)
- **Bit Depth**: 16-bit
- **Channels**: Mono (1 channel)
- **Format**: WAV PCM uncompressed
- **Voice Engine**: espeak text-to-speech (voice: en-us+f3)
- **Voice Characteristics**: Young female voice with adjusted pitch (55-62%, default 50%) and speed (155-175 WPM)
- **Total Voice Files**: 19 files (~4-5 MB)

### Tone-Based Samples (Musical Tones)
- **Sample Rate**: 44100 Hz (CD quality)
- **Bit Depth**: 16-bit
- **Channels**: Mono (1 channel)
- **Format**: WAV PCM uncompressed
- **Generation**: Synthesized sine waves and chords
- **Total Tone Files**: 17 files (~1.5 MB)

## Usage in Code

### Using Voice Samples with AudioManager

**Recommended:** Use the voice samples to hear Mittenz speak!

```java
AudioManager audioManager = new AudioManager();

// Preload VOICE samples (numbered files - these have actual speech!)
audioManager.preloadAudio("greeting_initial", "audio/greetings/greeting_initial.wav");
audioManager.preloadAudio("mittenz_hostile_01", "audio/dialogue/mittenz_hostile_01.wav");
audioManager.preloadAudio("mittenz_hostile_02", "audio/dialogue/mittenz_hostile_02.wav");
audioManager.preloadAudio("mittenz_curious_01", "audio/dialogue/mittenz_curious_01.wav");
audioManager.preloadAudio("mittenz_cooperative_01", "audio/dialogue/mittenz_cooperative_01.wav");
audioManager.preloadAudio("reminder_01", "audio/notifications/reminder_01.wav");
audioManager.preloadAudio("alert_oxygen", "audio/notifications/alert_oxygen.wav");

// Play voice samples
audioManager.playSoundEffect("greeting_initial");  // Mittenz says hello!
audioManager.playSoundEffect("mittenz_hostile_01");  // "Who the hell are you?"
```

### Using Tone-Based Samples (Legacy)

```java
// Preload tone-based audio (simple beeps - no speech)
audioManager.preloadAudio("welcome", "audio/greetings/welcome.wav");
audioManager.preloadAudio("button_click", "audio/sounds/button_click.wav");

// Play tone
audioManager.playSoundEffect("button_click");
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

### About the Voice Samples

The voice samples included are **TTS-generated using espeak**, providing actual speech so you can hear Mittenz talk! While these demonstrate the concept well, for a polished production experience, consider:

1. **Professional Voice Actress**: Hire a voice actress who can portray:
   - Young female character (late teens)
   - Well-spoken and proper demeanor
   - Emotional range across relationship stages (hostile → curious → cooperative)
   - Optional: Subtle Japanese accent or inflection

2. **Advanced TTS with Emotion Control**: Use premium TTS services:
   - Google Cloud Text-to-Speech (WaveNet voices)
   - Amazon Polly (Neural voices with SSML emotion tags)
   - Microsoft Azure Speech (Neural voices)
   - ElevenLabs (AI voice cloning with emotion)
   - Replica Studios (game-focused AI voices)

3. **Voice Cloning**: Create a consistent Mittenz voice:
   - Record 30-60 minutes of sample dialogue
   - Train a voice model with services like:
     - ElevenLabs Voice Cloning
     - Resemble AI
     - Descript Overdub
   - Generate all dialogue with consistent character voice

4. **Emotional Variation**: Adjust voice based on:
   - Relationship stage (hostile = harsh/angry, cooperative = warm/friendly)
   - Skill level (0-20: uncertain, 80-100: confident)
   - Context (alert = urgent, companion = casual)

### About the Tone-Based Samples

The tone-based files (without numbers) are simple musical tones and chords. These are kept for:
- Basic audio system testing
- UI sound effects (clicks, beeps)
- Backwards compatibility

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
