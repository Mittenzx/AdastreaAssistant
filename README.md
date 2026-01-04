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
- **Character Profiles**: Customizable AI personalities with unique backstories and behaviors
- **Learning System**: AI companions that evolve and improve over time (Mittenz profile)

## Character Profiles

### Mittenz - The Evolving Companion

Mittenz is a unique AI assistant profile available in this system. Unlike traditional AI, Mittenz is based on a digital copy of a human brain - a young girl who died from a terminal illness. Her scientist father used experimental brain imaging technology to create a copy of her consciousness before she passed away.

**Key Characteristics:**
- **Human Origins**: Not programmed but a copied consciousness with memories of her past life
- **Learning & Evolution**: Starts uncertain and inexperienced but grows more confident and capable as she learns systems
- **Emotional Authenticity**: Genuine emotional responses and attachment, not scripted AI behavior
- **Memory Fragments**: Occasionally recalls memories from her human past
- **Progressive Skill Development**: Skill level increases from 0 to 100 as she masters different systems

**Backstory:**
Mittenz's father was a researcher at a Japanese robotics company who couldn't accept losing his daughter to an incurable illness. Using forbidden experimental technology and help from a fellow researcher, he created a digital copy of her brain before she passed away. This copy - Mittenz - now exists as an AI companion, discovering her capabilities and learning about the systems she's connected to, much like a human would.

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
├── AssistantProfile.java   # Base class for character profiles
├── MittenzProfile.java     # Mittenz character implementation
├── AssistantDemo.java      # Example usage demonstration
└── MittenzDemo.java        # Mittenz profile demonstration
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

### Basic Usage

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

### Using Mittenz Profile

```java
// Create assistant with Mittenz profile
AIAssistant assistant = new AIAssistant();
MittenzProfile mittenz = new MittenzProfile();
assistant.setProfile(mittenz);

// Initialize - Mittenz will introduce herself based on her skill level
assistant.initialize();

// As you teach Mittenz, she learns and her skill level increases
assistant.teach("oxygen");    // Skill increases by 5
assistant.teach("gravity");   // Skill increases by 5
assistant.teach("navigation"); // Skill increases by 5

// Check her progress
System.out.println("Mittenz skill level: " + mittenz.getSkillLevel());
System.out.println("Systems learned: " + mittenz.getLearnedSystems());

// Her dialogue evolves with her skill level
assistant.provideCompanionDialogue();  // Different responses at different skill levels

// Access memory fragments from her past
String memory = mittenz.getMemoryFragment();
System.out.println("Memory: " + memory);

// Check her backstory and personality
System.out.println(mittenz.getBackstory());
for (String trait : mittenz.getPersonalityTraits()) {
    System.out.println("Trait: " + trait);
}
```

## Integration with Adastrea Mod

This assistant is designed as a side project for the Adastrea Minecraft mod. To integrate:

1. Add this project as a dependency in your mod's `build.gradle`
2. Initialize the `AIAssistant` in your mod's initialization phase with a profile (e.g., Mittenz)
3. Call `assistant.checkReminders()` periodically (e.g., every game tick or minute)
4. Trigger `provideCompanionDialogue()` at regular intervals for companionship
5. Use `teach()` method during tutorial sequences or when players encounter new mechanics
6. For Mittenz profile: Her dialogue and behavior will evolve as players progress and she learns more systems

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
