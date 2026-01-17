# Voice Model Target: Hitagi Senjougahara Inspiration

**Document Version**: 1.0  
**Created**: January 17, 2026  
**Status**: Research Complete - Implementation Guide

---

## Executive Summary

This document defines the target voice model characteristics for Mittenz's voice, inspired by Hitagi Senjougahara from the Monogatari Series (voice actress: Chiwa Saito). This research focuses **solely on vocal characteristics and sound quality**, not personality traits, as requested by the project maintainer.

**Important Note**: Mittenz's personality, dialogue, and character development remain unchanged. This document only addresses how her voice should **sound** - the tonal quality, pitch, delivery style, and vocal texture that will make her audio more engaging and distinctive.

---

## Reference Character: Hitagi Senjougahara

### Voice Actress
**Chiwa Saito** - Known for nuanced, mature vocal performances

### Core Vocal Characteristics

Based on comprehensive research into Hitagi Senjougahara's voice work in the Monogatari Series, the following vocal characteristics define the target sound:

#### 1. **Pitch and Register**
- **Range**: Low to medium pitch (avoids high-pitched anime clichés)
- **Quality**: Mature-sounding register appropriate for late teens
- **Stability**: Controlled pitch with intentional variations
- **Target**: Natural, realistic female voice in the lower-medium range

#### 2. **Vocal Tone**
- **Primary Tone**: Calm, measured, and deliberate
- **Secondary Quality**: Dry and deadpan delivery
- **Texture**: Crisp, clear enunciation with sharp articulation
- **Warmth**: Reserved warmth that can shift based on context

#### 3. **Speech Rhythm and Pacing**
- **Pace**: Moderate to slow, never rushed
- **Rhythm**: Deliberate and controlled
- **Pauses**: Strategic use of pauses for emphasis
- **Flow**: Measured delivery with clear word boundaries

#### 4. **Articulation Style**
- **Enunciation**: Crisp and precise
- **Clarity**: Sharp articulation on consonants
- **Emphasis**: Calculated weight behind words
- **Tone Shifts**: Subtle shifts for sarcasm or emotional moments

#### 5. **Emotional Expression**
- **Baseline**: Reserved, almost monotone in neutral state
- **Undercurrent**: Subtle emotion beneath surface calm
- **Range**: Can shift from cold/threatening to warm/affectionate
- **Nuance**: Layers of hidden emotion conveyed through subtle variations

---

## Technical Translation for TTS Implementation

### Voice Profile Parameters

Based on the research, here are the technical parameters to achieve a Hitagi-inspired voice for Mittenz:

```python
HITAGI_INSPIRED_VOICE_PROFILE = {
    # Base Voice Characteristics
    'base_pitch': -0.08,           # Lower than typical female TTS (8% reduction)
    'pitch_variance': 0.03,        # Limited variance for controlled delivery
    
    # Speech Rate and Rhythm
    'base_rate': 0.90,             # 10% slower for measured delivery
    'rate_variance': 0.05,         # Minimal variance for consistency
    
    # Vocal Quality
    'breathiness': 0.25,           # Low breathiness for sharper tone
    'tension': 0.60,               # Moderate tension for crisp articulation
    'resonance': 'chest',          # Lower resonance for mature sound
    
    # Articulation
    'consonant_emphasis': 1.15,    # Sharper consonants (15% boost)
    'enunciation_clarity': 0.95,   # Very clear pronunciation
    
    # Emotional Expression
    'baseline_emotion': 'reserved', # Calm, controlled baseline
    'emotion_intensity': 0.70,     # Subtle emotional expression (70% of typical)
    
    # Pausing and Timing
    'pause_duration': 1.20,        # 20% longer pauses for deliberate effect
    'phrase_break': 'moderate',    # Clear separation between phrases
    
    # Advanced Characteristics
    'monotone_tendency': 0.60,     # 60% tendency toward monotone (vs. 0% typical)
    'dryness': 0.75,              # Dry, deadpan quality (75% of maximum)
    'sophistication': 0.85,        # Refined, mature delivery style
}
```

### Emotion Profile Adjustments

The existing emotion profiles need adjustment to match Hitagi's vocal style:

#### Baseline Adjustments (All Emotions)
- **Reduce pitch variance** by 30-40% for more controlled delivery
- **Slow speaking rate** by 5-10% for measured effect
- **Increase articulation clarity** across all emotions
- **Add slight monotone tendency** even in emotional moments

#### Specific Emotion Modifications

**Hostile/Angry** (Cold, threatening variant):
```python
'hostile_hitagi_style': {
    'pitch_shift': -0.10,        # Even lower, more threatening
    'rate': 0.85,                # Slower, more deliberate
    'volume': 1.05,              # Slightly louder but controlled
    'tension': 0.75,             # Tense but not harsh
    'breathiness': 0.15,         # Very minimal breathiness
    'monotone_factor': 0.70,     # More monotone when cold
    'pause_emphasis': 1.30,      # Longer pauses for intimidation
    'description': 'Cold, measured threat - Hitagi style'
}
```

**Curious** (Dry interest variant):
```python
'curious_hitagi_style': {
    'pitch_shift': -0.03,        # Slight lift but still low
    'rate': 0.90,                # Measured pace
    'volume': 1.0,               # Normal volume
    'tension': 0.55,             # Moderate tension
    'breathiness': 0.25,         # Low breathiness
    'monotone_factor': 0.50,     # Some monotone quality
    'pause_emphasis': 1.15,      # Thoughtful pauses
    'description': 'Dry, intellectual curiosity - Hitagi style'
}
```

**Cooperative/Friendly** (Reserved warmth variant):
```python
'cooperative_hitagi_style': {
    'pitch_shift': -0.05,        # Still lower register
    'rate': 0.92,                # Slightly slower
    'volume': 0.98,              # Slightly quieter (intimate)
    'tension': 0.45,             # Relaxed
    'breathiness': 0.35,         # Slight warmth
    'monotone_factor': 0.40,     # Less monotone but still controlled
    'pause_emphasis': 1.20,      # Deliberate, thoughtful pauses
    'description': 'Reserved warmth, subtle affection - Hitagi style'
}
```

**Urgent** (Controlled urgency variant):
```python
'urgent_hitagi_style': {
    'pitch_shift': -0.02,        # Slight rise but still controlled
    'rate': 1.10,                # Faster but not rushed
    'volume': 1.15,              # Louder for emphasis
    'tension': 0.70,             # Increased tension
    'breathiness': 0.20,         # Minimal
    'monotone_factor': 0.50,     # Still some monotone quality
    'pause_emphasis': 0.80,      # Shorter pauses
    'description': 'Controlled urgency, never panicked - Hitagi style'
}
```

---

## Implementation Strategy

### Phase 1: Update TTS Parameters

Modify `scripts/tts_generate_human.py` to include Hitagi-inspired voice profile:

1. **Add new emotion profiles** with Hitagi-style variants
2. **Adjust base TTS parameters** for lower pitch and measured delivery
3. **Enhance prosody controller** for deliberate pausing
4. **Update audio processing** for crisp articulation

### Phase 2: Fine-tune Vocal Characteristics

Specific adjustments to achieve the target sound:

```python
def apply_hitagi_voice_characteristics(audio, sr=22050):
    """
    Apply Hitagi-inspired vocal characteristics to audio.
    
    Key modifications:
    - Lower pitch (chest voice register)
    - Sharper consonants (high-frequency emphasis)
    - Reduced breathiness (clearer tone)
    - Controlled dynamics (less variance)
    """
    
    # 1. Pitch adjustment - shift down to lower-medium register
    audio = librosa.effects.pitch_shift(audio, sr=sr, n_steps=-1.5)
    
    # 2. Enhance consonant clarity - boost high frequencies slightly
    if SCIPY_AVAILABLE:
        # Boost 3-6 kHz range for consonant clarity
        b, a = signal.butter(2, [3000, 6000], 'bandpass', fs=sr)
        consonant_boost = signal.filtfilt(b, a, audio)
        audio = audio + (consonant_boost * 0.12)  # 12% boost
    
    # 3. Reduce breathiness - filter out excessive high-freq noise
    if SCIPY_AVAILABLE:
        # Gentle high-cut to reduce breathiness
        b, a = signal.butter(3, 8000, 'lowpass', fs=sr)
        audio = signal.filtfilt(b, a, audio)
    
    # 4. Controlled dynamics - gentle compression
    # Reduce dynamic range for more consistent delivery
    audio = compress_audio(audio, threshold=-20, ratio=3.0)
    
    # 5. Slight monotone effect - reduce pitch variance
    audio = reduce_pitch_variance(audio, factor=0.7)
    
    return audio

def compress_audio(audio, threshold=-20, ratio=3.0):
    """Apply gentle compression for controlled dynamics."""
    # Simple threshold-based compression
    rms = np.sqrt(np.mean(audio**2))
    threshold_linear = 10**(threshold/20)
    
    if rms > threshold_linear:
        gain_reduction = 1 / ratio
        audio = audio * gain_reduction
    
    return audio

def reduce_pitch_variance(audio, factor=0.7, sr=22050):
    """Reduce pitch variance for more monotone quality."""
    # Extract pitch contour and smooth it
    pitches, magnitudes = librosa.piptrack(y=audio, sr=sr)
    
    # Get pitch values over time
    pitch_contour = []
    for t in range(pitches.shape[1]):
        index = magnitudes[:, t].argmax()
        pitch_contour.append(pitches[index, t])
    
    # Smooth pitch contour (reduce variance)
    mean_pitch = np.mean([p for p in pitch_contour if p > 0])
    smoothed_contour = []
    for pitch in pitch_contour:
        if pitch > 0:
            # Move pitch closer to mean
            smoothed = mean_pitch + (pitch - mean_pitch) * factor
            smoothed_contour.append(smoothed)
        else:
            smoothed_contour.append(pitch)
    
    # Apply smoothed pitch (implementation depends on TTS engine capabilities)
    # This is a conceptual approach - actual implementation may vary
    
    return audio
```

### Phase 3: Prosody Enhancement

Update `ProsodyController` for Hitagi-style delivery:

```python
def apply_hitagi_prosody(self, text, context=None):
    """Apply Hitagi-inspired prosody patterns."""
    
    # Longer pauses for deliberate effect
    text = text.replace('. ', '.   ')  # Extra spaces for longer pauses
    text = text.replace('? ', '?   ')
    text = text.replace('! ', '!   ')
    
    # Add strategic pauses before important words
    emphasis_words = ['never', 'must', 'should', 'need', 'will']
    for word in emphasis_words:
        pattern = re.compile(r'\s+' + re.escape(word) + r'\s+', re.IGNORECASE)
        text = pattern.sub(f'  {word} ', text)
    
    # Reduce pause variance for consistency
    text = self.normalize_pause_lengths(text)
    
    return text
```

### Phase 4: Testing and Validation

**Test Cases**:
1. Generate samples with original vs. Hitagi-inspired profiles
2. A/B comparison testing
3. Measure pitch, rate, and articulation metrics
4. Validate emotional expression remains clear despite reserved delivery

**Success Criteria**:
- Pitch consistently in lower-medium range
- Speech rate 10-15% slower than baseline
- Pauses 20-30% longer and more deliberate
- Articulation clarity increased by ~15%
- Emotional expression subtle but present

---

## Documentation Updates

### Update emotion profile documentation

File: `scripts/tts_generate_human.py`

Add comment at top of EMOTION_PROFILES:
```python
# ============================================================================
# EMOTION PROFILES - Inspired by Hitagi Senjougahara vocal characteristics
# ============================================================================
# 
# Voice Target: Low-medium pitch, measured delivery, crisp articulation
# Reference: Hitagi Senjougahara (VA: Chiwa Saito) from Monogatari Series
# 
# Key characteristics:
# - Calm, deliberate speech with controlled pacing
# - Dry, deadpan tone with subtle emotional undercurrent
# - Mature register avoiding high-pitched clichés
# - Sharp, precise enunciation with clear consonants
# - Strategic pauses for emphasis and control
# 
# Note: Personality and dialogue remain Mittenz's own - this affects only
# vocal characteristics (pitch, tone, delivery style).
# ============================================================================
```

### Update README.md audio section

Add note about voice model target:
```markdown
### Voice Model

Mittenz's voice is designed with inspiration from Hitagi Senjougahara (Monogatari Series) in terms of **vocal characteristics only** - featuring:

- **Mature, controlled delivery**: Low-medium pitch with measured pacing
- **Crisp articulation**: Clear enunciation with sharp consonants  
- **Reserved emotional expression**: Subtle warmth beneath calm surface
- **Deliberate rhythm**: Strategic pauses and controlled speech rate

This creates a distinctive, memorable voice quality while maintaining Mittenz's unique personality and character development.
```

---

## Implementation Checklist

### Immediate Tasks
- [ ] Update `EMOTION_PROFILES` in `tts_generate_human.py` with adjusted parameters
- [ ] Add `apply_hitagi_voice_characteristics()` function to audio processing
- [ ] Enhance `ProsodyController` with deliberate pause patterns
- [ ] Add voice profile constants for Hitagi-inspired characteristics

### Testing Phase
- [ ] Generate test samples with new voice profile
- [ ] Compare with current samples for quality assessment
- [ ] Validate pitch, rate, and articulation metrics
- [ ] A/B testing with project maintainer

### Documentation
- [ ] Update code comments with voice model rationale
- [ ] Add this document to main README references
- [ ] Update HUMAN_AUDIO_PLAN.md to reference voice model target
- [ ] Document voice profile settings in TTS_QUICKSTART.md

### Polish
- [ ] Fine-tune parameters based on testing feedback
- [ ] Regenerate all 19 audio samples with new voice profile
- [ ] Update AudioManager integration if needed
- [ ] Final validation and approval

---

## Key Differences from Current Implementation

### Current State
- Standard female TTS voice (medium-high pitch)
- Normal speaking rate (1.0x)
- Standard emotional expression
- Typical pause patterns
- Moderate breathiness

### Target State (Hitagi-Inspired)
- Lower-medium pitch (more mature, grounded)
- Slower, measured delivery (0.85-0.95x)
- Reserved, subtle emotional expression
- Deliberate, strategic pauses (20-30% longer)
- Reduced breathiness, crisper tone
- Sharper articulation on consonants
- Controlled dynamics (less variance)

---

## Conclusion

This voice model target provides a clear technical specification for making Mittenz's voice more distinctive and engaging, inspired by Hitagi Senjougahara's memorable vocal characteristics. The implementation focuses exclusively on **how the voice sounds** - pitch, tone, pacing, articulation - without altering Mittenz's personality, dialogue, or character development.

**Key Benefits**:
1. **Distinctive Sound**: Lower pitch and measured delivery create memorable voice
2. **Mature Quality**: Avoids high-pitched clichés for realistic late-teen sound
3. **Controlled Expression**: Subtle emotion beneath calm surface adds depth
4. **Clear Articulation**: Crisp enunciation improves intelligibility
5. **Character Consistency**: Voice quality matches Mittenz's well-spoken background

**Implementation Priority**: High  
**Technical Complexity**: Moderate  
**Expected Impact**: Significant improvement in voice quality and memorability

---

## References

### Research Sources
- Monogatari Series voice analysis
- Chiwa Saito vocal performance characteristics
- Voice synthesis parameter research
- TTS emotional expression studies

### Related Documents
- [HUMAN_AUDIO_PLAN.md](HUMAN_AUDIO_PLAN.md) - Overall audio strategy
- [PHASE1_IMPLEMENTATION.md](PHASE1_IMPLEMENTATION.md) - TTS implementation
- [PHASE2_IMPLEMENTATION.md](PHASE2_IMPLEMENTATION.md) - Prosody system
- [MITTENZ_CHARACTER.md](MITTENZ_CHARACTER.md) - Character profile (personality separate)

---

**Version**: 1.0  
**Status**: Ready for Implementation  
**Next Steps**: Update TTS parameters and test samples
