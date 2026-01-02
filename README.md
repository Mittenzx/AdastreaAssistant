# AdastraAssistant

A voice assistant for game integration, inspired by successful implementations in games like Warframe, Destiny, and Halo.

## Documentation

- **[Voice Assistant Research](VOICE_ASSISTANT_RESEARCH.md)**: Comprehensive research on game voice assistants, including Warframe's Cephalon system, voice synthesis technologies, and implementation patterns
- **[Implementation Roadmap](docs/IMPLEMENTATION_ROADMAP.md)**: Phased development plan with technology recommendations and success metrics

## Research Highlights

This project is informed by research into industry-leading voice assistants:

### Key Inspirations

- **Warframe's Cephalon System**: Multiple mode-specific AI companions (Ordis, Cy, Simaris) each with distinct personalities
- **Destiny's Ghost**: Unobtrusive, informative companion that enhances exploration
- **Halo's Cortana**: Emotionally engaging AI with deep narrative integration

### Technology Foundation

- **Voice Synthesis**: Neural network-based TTS with voice cloning capabilities
- **Architecture**: Modular design with NLU, dialogue management, NLG, and TTS components
- **Context Awareness**: Game state integration for relevant, timely responses
- **Memory Systems**: Short-term and long-term memory for consistent personality

## Getting Started

See the [Implementation Roadmap](docs/IMPLEMENTATION_ROADMAP.md) for development phases and next steps.

## Project Status

Currently in research and planning phase. Implementation will follow the phased approach outlined in the roadmap documentation.
An in-game AI assistant for the Adastrea (Ad Astra) Minecraft mod that provides companionship, teaching, and reminders to players during their space exploration adventures.

## Features

- **Audio System**: Text-to-speech voice synthesis for natural conversation
- **Visual Components**: On-screen notifications, subtitles, and assistant avatar
- **Companion Dialogue**: Random friendly messages to keep players company
- **Teaching System**: Tutorials and lessons about game mechanics (oxygen, gravity, navigation, etc.)
- **Reminder System**: Time-based reminders for important tasks
- **Interactive Responses**: Answers player questions about game mechanics

## Project Structure

```
src/main/java/com/adastrea/assistant/
├── AIAssistant.java        # Main coordinator class
├── AudioManager.java       # Handles audio playback and TTS
├── VisualManager.java      # Manages visual notifications and UI
├── DialogueSystem.java     # Generates and manages conversations
├── ReminderSystem.java     # Manages time-based reminders
├── TeachingSystem.java     # Provides educational content
├── AssistantConfig.java    # Configuration settings
└── AssistantDemo.java      # Example usage demonstration
```

## Building

This project uses Gradle as its build system:

```bash
# Build the project
./gradlew build

# Run tests
./gradlew test

# Run the demo
./gradlew run
```

## Usage Example

```java
// Create and initialize the assistant
AIAssistant assistant = new AIAssistant("Astra");
assistant.initialize();

// Provide companion dialogue
assistant.provideCompanionDialogue();

// Teach about game mechanics
assistant.teach("oxygen");
assistant.teach("gravity");

// Set reminders
assistant.setReminder("Check fuel levels", 10); // 10 minutes

// Respond to player queries
String response = assistant.respondToQuery("How does gravity work?");

// Configure settings
assistant.setEnabled(true);
assistant.getAudioManager().setVolume(0.8f);
assistant.getVisualManager().setSubtitleDuration(7000);
```

## Integration with Adastrea Mod

This assistant is designed as a side project for the Adastrea Minecraft mod. To integrate:

1. Add this project as a dependency in your mod's `build.gradle`
2. Initialize the `AIAssistant` in your mod's initialization phase
3. Call `assistant.checkReminders()` periodically (e.g., every game tick or minute)
4. Trigger `provideCompanionDialogue()` at regular intervals for companionship
5. Use `teach()` method during tutorial sequences or when players encounter new mechanics

## Configuration

The `AssistantConfig` class provides configurable options:

- **enabled**: Enable/disable the assistant
- **audioEnabled**: Toggle audio output
- **visualEnabled**: Toggle visual notifications
- **volume**: Audio volume (0.0 to 1.0)
- **subtitleDuration**: How long subtitles display (milliseconds)
- **companionDialogueIntervalMinutes**: How often companion dialogue appears
- **assistantName**: Customize the assistant's name

## Teaching Topics

Available teaching topics include:
- `oxygen`: Oxygen management and survival
- `gravity`: Gravity mechanics on different planets
- `temperature`: Temperature effects and protection
- `fuel`: Spacecraft fuel management
- `resources`: Resource collection and scanning
- `crafting`: Crafting system and recipes
- `navigation`: Star map and route planning
- `exploration`: Exploration tips and rewards
- `safety`: General safety guidelines
- `starter`: Getting started guide

## Requirements

- Java 17 or later
- Gradle 7.0+

## License

This project is part of the Adastrea ecosystem.

## Contributing

Contributions are welcome! Feel free to add new dialogue, teaching topics, or improve existing systems.
