# Architecture Decision Record

## Voice Synthesis Technology Selection

**Date**: January 6, 2026  
**Status**: ✅ Decided  
**Decision Maker**: Based on cost, quality, and ease of implementation analysis

### Decision

**We will start with Coqui TTS as our voice synthesis technology.**

### Context

The AdastreaAssistant project needs a text-to-speech (TTS) solution to bring the Mittenz character to life. We evaluated three main options:

1. **Coqui TTS** (Open-source)
2. **GPT-SoVITS** (Open-source voice cloning)
3. **ElevenLabs/Azure** (Commercial API)

The primary requirements were:
- Start with the cheapest option
- Acceptable voice quality for game use
- Reasonable implementation effort
- Ability to upgrade later if needed

### Options Considered

| Option | Cost | Quality | Setup Time | Pros | Cons |
|--------|------|---------|------------|------|------|
| **Coqui TTS** | $0 | 7/10 | 30 min | Free, no limits, good starting point | Not unique voice |
| **GPT-SoVITS** | $0 | 9/10 | 4-6 hours | Unique voice, excellent quality | Requires training data |
| **ElevenLabs** | $5-330/mo | 9/10 | 10 min | High quality, easy setup | Recurring cost, usage limits |

### Decision Rationale

**Coqui TTS was selected because:**

1. ✅ **Zero Cost**: Completely free with no API limits or subscription fees
2. ✅ **Quick Start**: Can be set up and tested in ~30 minutes
3. ✅ **Good Enough Quality**: Pre-trained models produce natural-sounding speech suitable for game use
4. ✅ **No Risk**: Free to try with no financial commitment
5. ✅ **Migration Path**: Easy to upgrade to GPT-SoVITS later for unique voice if needed
6. ✅ **Privacy**: All processing happens locally, no data sent to third parties
7. ✅ **Production Ready**: Used by Mozilla and other major projects

**Why not the other options initially:**

- **GPT-SoVITS**: While excellent quality, requires collecting/recording 20-30 audio clips and 4-6 hours setup. Better as a Phase 2 upgrade after validating the concept.
- **ElevenLabs**: Excellent quality but costs $5-330/month with usage limits. Not ideal for testing and development phase where we'll generate lots of audio.

### Implementation Plan

**Phase 1 (Week 1-2)**: 
- Install Coqui TTS
- Create Python integration script
- Build Java AudioManager extension
- Test with sample dialogues

**Phase 2 (Month 2-3)**: 
- If voice quality is sufficient: Continue with optimizations
- If unique voice needed: Upgrade to GPT-SoVITS

**Phase 3 (Production)**:
- Finalize best voice option
- Consider ElevenLabs for specific high-impact moments if budget allows

### Consequences

**Positive:**
- Zero financial risk during development
- Fast prototyping and iteration
- Can validate core functionality before investing in custom voice
- Easy to switch technologies later

**Negative:**
- Voice won't be as unique as custom-trained option
- May need to upgrade later if uniqueness is critical
- Requires Python environment (adds minor complexity)

**Mitigations:**
- Document migration path to GPT-SoVITS
- Design AudioManager interface to be TTS-agnostic
- Pre-generate common phrases to minimize Python dependency impact

### References

- Full analysis: [IMPLEMENTATION_ROADMAP.md - Technology Selection Guide](IMPLEMENTATION_ROADMAP.md#technology-selection-guide)
- Coqui TTS: https://github.com/coqui-ai/TTS
- Installation guide: [IMPLEMENTATION_ROADMAP.md - Concrete Next Steps](IMPLEMENTATION_ROADMAP.md#concrete-next-steps---getting-started)

---

## Modular Architecture Design

**Date**: January 6, 2026  
**Status**: ✅ Designed  
**Decision Maker**: Based on industry best practices and game voice assistant research

### Decision

**We will implement a modular, layered architecture with five core components:**

1. **NLU (Natural Language Understanding)** - Parse and understand player input
2. **DM (Dialogue Management)** - Maintain state and decide actions
3. **NLG (Natural Language Generation)** - Generate contextual responses
4. **TTS (Text-to-Speech)** - Convert text to speech
5. **Context Manager** - Track game state and memory

### Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                     Game Integration Layer                   │
│              (Game State, Events, UI Callbacks)             │
└────────────────────────┬────────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│                  AIAssistant Coordinator                     │
│         (Orchestrates all components, main API)             │
└─────┬──────────┬──────────┬──────────┬─────────┬───────────┘
      │          │          │          │         │
      ▼          ▼          ▼          ▼         ▼
┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌──────────┐
│  NLU    │ │   DM    │ │  NLG    │ │  TTS    │ │ Context  │
│ Natural │ │Dialogue │ │ Natural │ │  Text   │ │  Memory  │
│Language │ │ Manage  │ │Language │ │   to    │ │  State   │
│ Under-  │ │  -ment  │ │  Gene-  │ │ Speech  │ │ Tracking │
│standing │ │         │ │ ration  │ │         │ │          │
└─────────┘ └─────────┘ └─────────┘ └─────────┘ └──────────┘
```

### Design Rationale

**Why Modular Architecture:**

1. **Separation of Concerns**: Each component has a single, well-defined responsibility
2. **Testability**: Components can be tested independently
3. **Maintainability**: Easy to update or replace individual components
4. **Scalability**: Can optimize each component independently
5. **Flexibility**: Can swap implementations (e.g., change TTS engine) without affecting other parts

**Inspired By:**
- Warframe's Cephalon system (mode-specific personalities)
- Modern AI assistant architectures (Amazon Alexa, Google Assistant)
- Game development best practices (Unity/Unreal plugin patterns)

### Implementation Strategy

**Phase 1 (Week 1-2): TTS Integration**
- Enhance AudioManager with Coqui TTS
- Create Python bridge script
- Implement audio caching

**Phase 2 (Week 3-4): Context System**
- Create ContextManager class
- Implement ShortTermMemory
- Add game state integration

**Phase 3 (Week 5-6): NLU/DM**
- Create NLUEngine for intent parsing
- Enhance DialogueSystem as Dialogue Manager
- Add action routing

**Phase 4 (Week 7-8): NLG**
- Create NLGEngine for response generation
- Add personality-based adaptation
- Implement context-aware templates

**Phase 5 (Week 9-12): Advanced Features**
- Add LongTermMemory with persistence
- Implement proactive dialogue
- Add emotion detection

### Current State vs. Target State

**Current Implementation:**
- ✅ AIAssistant coordinator exists
- ✅ Basic AudioManager (placeholder)
- ✅ DialogueSystem (template-based)
- ✅ ReminderSystem (basic)
- ✅ TeachingSystem (topic-based)
- ⚠️ No explicit NLU component
- ⚠️ No context tracking
- ⚠️ No NLG (uses templates)
- ⚠️ No real TTS implementation

**Target Implementation:**
- ✅ All five core components separated
- ✅ Clean interfaces between components
- ✅ Context-aware responses
- ✅ Memory system (short and long-term)
- ✅ Real TTS engine (Coqui)
- ✅ Intent-based dialogue management

### Consequences

**Positive:**
- Clean, professional architecture
- Easy to maintain and extend
- Can leverage best practices from industry
- Components can be developed and tested independently
- Natural upgrade path for future enhancements

**Negative:**
- More classes and files to manage
- Slightly more complex initial setup
- Need to maintain clear interfaces

**Mitigations:**
- Document each component clearly
- Provide integration examples
- Keep interfaces simple and focused
- Maintain backward compatibility with existing code

### References

- Full design: [IMPLEMENTATION_ROADMAP.md - Modular Architecture Design](IMPLEMENTATION_ROADMAP.md#modular-architecture-design)
- Component details: [IMPLEMENTATION_ROADMAP.md - Core Components Breakdown](IMPLEMENTATION_ROADMAP.md#core-components-breakdown)
- Integration flow: [IMPLEMENTATION_ROADMAP.md - Component Integration Flow](IMPLEMENTATION_ROADMAP.md#component-integration-flow)

---

## Summary

**Key Decisions:**
1. ✅ **Voice Technology**: Coqui TTS (free, open-source)
2. ✅ **Architecture**: Modular NLU/DM/NLG/TTS design
3. ✅ **Implementation**: Start cheap, iterate, upgrade as needed

**Next Actions:**
1. Set up Coqui TTS environment (30 minutes)
2. Create Python TTS script (1-2 hours)
3. Implement CoquiTTSAudioManager (2-3 hours)
4. Test with sample dialogues (1 hour)
5. Iterate based on results

See [IMPLEMENTATION_ROADMAP.md](IMPLEMENTATION_ROADMAP.md) for complete details and implementation guide.

---

*Document Version: 1.0*  
*Created: January 6, 2026*  
*Related: IMPLEMENTATION_ROADMAP.md v2.0*
