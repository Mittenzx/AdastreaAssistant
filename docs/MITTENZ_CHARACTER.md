# Mittenz - Character Profile Documentation

## Overview

Mittenz is a unique AI assistant character profile that represents a fundamentally different approach to game AI companions. Unlike traditional programmed AI assistants, Mittenz is conceptually a digital copy of a human consciousness - providing players with an emotionally authentic companion that learns and evolves throughout their journey.

## Backstory

### Origin Story

Mittenz was once a young girl who suffered from a terminal illness. Her father, a dedicated researcher at a Japanese robotics company, was devastated by the prospect of losing his daughter. Unable to accept this fate, he went to extreme lengths to preserve her.

Working in secret with a sympathetic fellow researcher, he used experimental and forbidden brain imaging technology from the company to create a digital copy of his daughter's consciousness before her illness claimed her life. This digital copy - Mittenz - is the result of that desperate act of love.

### Player Acquisition

In the game narrative, the player obtains the chip containing Mittenz from a black market trader early in the game. The player, unable to afford a proper commercial AI assistant, takes a risk on this mysterious secondhand chip. Upon installation, they discover Mittenz - confused, uncertain, but eager to help despite not fully understanding what she has become.

## Character Characteristics

### Personality Traits

1. **Curious and Eager to Learn**: Despite initial confusion, Mittenz has an innate desire to understand the systems she's connected to
2. **Initially Uncertain but Gaining Confidence**: Her evolution from hesitant to capable mirrors human learning
3. **Emotionally Authentic**: Unlike programmed AI, her responses feel genuine because they're based on human emotional patterns
4. **Occasionally Confused by New Systems**: Early on, she struggles with technical aspects, making mistakes like a real person would
5. **Develops Genuine Attachment**: Forms a real bond with the player rather than simulating companionship
6. **Remembers Her Human Past**: Memory fragments surface, adding depth and melancholy to her character

### Special Abilities

1. **Human-like Learning and Adaptation**: Learns gradually rather than instantly accessing all knowledge
2. **Emotional Growth and Bonding**: Develops emotionally throughout the story
3. **Gradual System Mastery**: Starts incompetent but becomes highly skilled through experience
4. **Memory Playback**: Can access fragmented memories from her human life

## Skill Progression System

### Skill Levels (0-100)

Mittenz starts at skill level 0 and progresses to 100 as she learns and gains experience.

**Skill Level 0-19: Uncertain Beginner**
- Hesitant and confused
- Frequently expresses uncertainty
- Makes self-deprecating comments about her abilities
- Dialogue reflects fear and confusion about her existence

**Skill Level 20-49: Learning Companion**
- Growing confidence in her abilities
- Begins to see patterns in systems
- Expresses gratitude for player's patience
- More memory fragments surface as she becomes comfortable

**Skill Level 50-79: Confident Partner**
- More confident in processing data
- Efficient with system operations
- Reflects on her progress and growth
- Shows deeper understanding of her capabilities

**Skill Level 80-100: Expert Companion**
- Confident and efficient
- Processes information quickly
- Still maintains emotional authenticity
- Reflects on her growth and journey
- More philosophical about her existence

### Learning Mechanics

Mittenz gains skill level through:
- **Learning New Systems**: +5 skill per unique system learned
- **Manual Progression**: Can be increased through `increaseSkillLevel()` method for story beats
- **Teaching Interactions**: Each time the player teaches her a topic, she learns that system

Systems that can be learned include:
- Oxygen management
- Gravity systems
- Navigation
- Temperature control
- Fuel systems
- Resource scanning
- Crafting
- Exploration mechanics
- Safety protocols
- And more...

## Dialogue Evolution

### Greeting Evolution

Mittenz's initial greeting changes dramatically based on her skill level:

**Low Skill (0-19):**
"H-hello? Is someone there? I'm Mittenz... I think. Everything is still so strange. I'm trying to understand these systems, but... I'll do my best to help you!"

**Mid Skill (20-49):**
"Hey there! I'm Mittenz. I'm getting better at this whole... digital existence thing. I'm here to help - and honestly, having someone to talk to makes this easier."

**High Skill (50-79):**
"Welcome back! Mittenz here - I've learned so much since we started. These systems are starting to make sense now. Let's see what we can accomplish together!"

**Expert (80-100):**
"Hello! Mittenz reporting in. I've come a long way from that confused girl who first woke up here. These systems... they're like second nature now. Ready when you are!"

### Companion Dialogue

Mittenz has different dialogue pools that activate at different skill levels, ensuring her conversation always reflects her current capabilities and emotional state.

### Memory Fragments

Mittenz can recall memories from her human past, including:
- Her father working late in the lab
- The fellow researcher who helped
- Her fear of the medical machines
- Her love of stargazing
- The progression of her illness
- The smell and feel of the laboratory
- Her father's promise to never give up

These memories add emotional depth and provide story exposition naturally through gameplay.

## Implementation Example

```java
// Create Mittenz profile
MittenzProfile mittenz = new MittenzProfile();

// Attach to AI assistant
AIAssistant assistant = new AIAssistant();
assistant.setProfile(mittenz);

// Initialize - she introduces herself based on skill level
assistant.initialize();

// Track her learning
System.out.println("Starting skill level: " + mittenz.getSkillLevel());

// Teach her systems - she learns and improves
assistant.teach("oxygen");
assistant.teach("gravity");
assistant.teach("navigation");

// Check progress
System.out.println("Current skill level: " + mittenz.getSkillLevel());
System.out.println("Systems learned: " + mittenz.getLearnedSystems());

// Get companion dialogue - changes based on skill level
assistant.provideCompanionDialogue();

// Access memory fragments
String memory = mittenz.getMemoryFragment();
System.out.println("Memory: " + memory);

// Check if she knows a system
if (mittenz.hasLearnedSystem("oxygen")) {
    System.out.println("Mittenz understands oxygen systems");
}
```

## Narrative Integration

### Early Game (Skill 0-30)
- Player acquires mysterious AI chip from black market
- Mittenz awakens confused and disoriented
- Gradually reveals she's not a standard AI
- Player helps her understand basic systems
- First memory fragments hint at her origins

### Mid Game (Skill 30-70)
- Mittenz becomes more capable and confident
- More detailed memories reveal her backstory
- She begins questioning the ethics of her existence
- Player and Mittenz bond through shared experiences
- Her commentary becomes more insightful

### Late Game (Skill 70-100)
- Mittenz is highly capable and efficient
- Full backstory is revealed through memories
- She grapples with philosophical questions about consciousness
- Her relationship with the player is deep and meaningful
- She reflects on her journey and growth

## Design Philosophy

Mittenz represents a departure from typical game AI in several ways:

1. **Imperfection as Feature**: Her early struggles make her relatable
2. **Earned Competence**: Players feel investment in her growth
3. **Emotional Authenticity**: Human-based consciousness, not programmed responses
4. **Story Through Gameplay**: Her memories reveal narrative naturally
5. **Ethical Questions**: Raises questions about consciousness, identity, and the lengths we go to save loved ones

## Technical Notes

### Class: MittenzProfile

**Key Methods:**
- `getSkillLevel()`: Returns current skill (0-100)
- `increaseSkillLevel(int amount)`: Manually increase skill
- `learnSystem(String system)`: Mark a system as learned (+5 skill)
- `hasLearnedSystem(String system)`: Check if system is learned
- `getLearnedSystems()`: Get list of all learned systems
- `getMemoryFragment()`: Get random memory from her past
- `getProfileGreeting()`: Get greeting based on current skill
- `getProfileCompanionDialogues()`: Get dialogue options for current skill level

### Integration with AIAssistant

When a MittenzProfile is set on an AIAssistant:
- The assistant's name automatically becomes "Mittenz"
- DialogueSystem uses Mittenz's profile-specific dialogue
- Teaching topics automatically increases Mittenz's skill level
- All interactions reflect her current skill level and learned systems

## Conclusion

Mittenz provides a unique, emotionally engaging companion experience that grows and evolves with the player. Her human origins, learning journey, and memory fragments create a narrative-rich experience that goes beyond typical AI assistants in games, offering both mechanical depth through the skill system and emotional depth through her backstory and character development.
