#!/usr/bin/env python3
"""
Human-like TTS Generation Script for AdastreaAssistant

This script generates natural, emotionally expressive speech using Coqui TTS
with advanced prosody control, emotional expression, and human-like features.

Usage:
    python3 tts_generate_human.py --text "Hello, I'm Mittenz!" --emotion cooperative --output hello.wav

Features:
    - Natural prosody and intonation
    - Emotional expression mapping
    - Human-like variations and imperfections
    - Breath sounds and natural pauses
    - Contextual speaking rate and pitch
"""

import argparse
import sys
import json
import random
import re
import wave
import numpy as np
from pathlib import Path

# Check for scipy (optional, for audio processing)
try:
    from scipy import signal
    SCIPY_AVAILABLE = True
except ImportError:
    SCIPY_AVAILABLE = False
    print("WARNING: scipy not installed. Some audio effects will be disabled. Install with: pip install scipy", file=sys.stderr)

# Check for TTS library
try:
    from TTS.api import TTS
    TTS_AVAILABLE = True
except ImportError:
    TTS_AVAILABLE = False
    print("WARNING: TTS library not installed. Install with: pip install TTS", file=sys.stderr)

# Check for audio processing libraries
try:
    import librosa
    import soundfile as sf
    AUDIO_PROCESSING_AVAILABLE = True
except ImportError:
    AUDIO_PROCESSING_AVAILABLE = False
    print("WARNING: librosa/soundfile not installed. Install with: pip install librosa soundfile", file=sys.stderr)


# ============================================================================
# EMOTION PROFILES - Inspired by Hitagi Senjougahara vocal characteristics
# ============================================================================
# 
# Voice Target: Low-medium pitch, measured delivery, crisp articulation
# Reference: Hitagi Senjougahara (VA: Chiwa Saito) from Monogatari Series
# 
# Key characteristics:
# - Calm, deliberate speech with controlled pacing (10-15% slower)
# - Dry, deadpan tone with subtle emotional undercurrent
# - Mature register avoiding high-pitched clichés (lower pitch by ~8%)
# - Sharp, precise enunciation with clear consonants
# - Strategic pauses for emphasis and control (20-30% longer)
# 
# Note: Personality and dialogue remain Mittenz's own - this affects only
# vocal characteristics (pitch, tone, delivery style).
# 
# See docs/VOICE_MODEL_TARGET.md for complete specification.
# ============================================================================

EMOTION_PROFILES = {
    # Hostile stage emotions (Hitagi-inspired: cold, measured threat)
    'hostile': {
        'pitch_shift': -0.10,    # Even lower for threatening effect
        'rate': 0.85,            # Slower, more deliberate (not rushed)
        'volume': 1.05,          # Slightly louder but controlled
        'tension': 0.75,         # Tense but not harsh
        'breathiness': 0.15,     # Very minimal breathiness (sharp tone)
        'description': 'Cold, measured threat - Hitagi style'
    },
    'angry': {
        'pitch_shift': -0.08,    # Lower for controlled anger
        'rate': 0.90,            # Measured, not frantic
        'volume': 1.10,          # Louder but not shouting
        'tension': 0.80,         # Controlled tension
        'breathiness': 0.10,     # Very crisp, no breathiness
        'description': 'Controlled anger, deliberate - Hitagi style'
    },
    
    # Curious stage emotions (Hitagi-inspired: dry, intellectual interest)
    'curious': {
        'pitch_shift': -0.03,    # Slight lift but still low register
        'rate': 0.90,            # Measured, thoughtful pace
        'volume': 1.0,           # Normal volume
        'tension': 0.55,         # Moderate tension
        'breathiness': 0.25,     # Low breathiness for crisp delivery
        'description': 'Dry, intellectual curiosity - Hitagi style'
    },
    'fascinated': {
        'pitch_shift': -0.01,    # Minimal lift, controlled fascination
        'rate': 0.92,            # Still measured, not rushed
        'volume': 1.02,          # Slightly elevated interest
        'tension': 0.60,         # Moderate engagement
        'breathiness': 0.28,     # Low breathiness
        'description': 'Controlled fascination, intellectual wonder - Hitagi style'
    },
    
    # Cooperative stage emotions (Hitagi-inspired: reserved warmth)
    'cooperative': {
        'pitch_shift': -0.05,    # Lower register maintained
        'rate': 0.90,            # Measured, deliberate
        'volume': 0.98,          # Slightly quieter (intimate)
        'tension': 0.45,         # Relaxed
        'breathiness': 0.35,     # Slight warmth, still controlled
        'description': 'Reserved warmth, subtle affection - Hitagi style'
    },
    'friendly': {
        'pitch_shift': -0.04,    # Lower register
        'rate': 0.92,            # Measured warmth
        'volume': 1.0,           # Normal volume
        'tension': 0.42,         # Relaxed
        'breathiness': 0.38,     # Slight warmth
        'description': 'Measured friendliness, controlled - Hitagi style'
    },
    
    # Universal emotions (Hitagi-inspired: controlled expression)
    'excited': {
        'pitch_shift': -0.02,    # Slight lift but still controlled
        'rate': 1.08,            # Faster but not rushed
        'volume': 1.12,          # Elevated but controlled
        'tension': 0.65,         # Energetic but measured
        'breathiness': 0.25,     # Reduced breathiness
        'description': 'Controlled excitement, never manic - Hitagi style'
    },
    'worried': {
        'pitch_shift': -0.06,    # Lower for serious concern
        'rate': 0.88,            # Slower, cautious
        'volume': 0.95,          # Quieter, serious
        'tension': 0.68,         # Controlled tension
        'breathiness': 0.30,     # Moderate
        'description': 'Controlled concern, measured - Hitagi style'
    },
    'contemplative': {
        'pitch_shift': -0.07,    # Lower for deep thought
        'rate': 0.82,            # Slower, very deliberate
        'volume': 0.90,          # Quieter, introspective
        'tension': 0.32,         # Very relaxed
        'breathiness': 0.40,     # Some warmth in contemplation
        'description': 'Deep thought, measured reflection - Hitagi style'
    },
    'urgent': {
        'pitch_shift': -0.02,    # Controlled urgency
        'rate': 1.10,            # Faster but not panicked
        'volume': 1.15,          # Louder for emphasis
        'tension': 0.70,         # Increased but controlled tension
        'breathiness': 0.20,     # Minimal breathiness
        'description': 'Controlled urgency, never panicked - Hitagi style'
    },
    'neutral': {
        'pitch_shift': -0.05,    # Lower register baseline
        'rate': 0.90,            # Measured pace
        'volume': 1.0,           # Normal volume
        'tension': 0.50,         # Balanced
        'breathiness': 0.30,     # Low breathiness for clarity
        'description': 'Calm, measured baseline - Hitagi style'
    }
}


# ============================================================================
# PROSODY CONTROLLER - Adds natural speech patterns
# ============================================================================

class ProsodyController:
    """Controls natural speech prosody patterns with contextual awareness."""
    
    # Constant for breath marker threshold
    BREATH_MARKER_WORD_THRESHOLD = 15  # Add breath markers after sentences longer than this
    
    def apply_context_prosody(self, text, context=None):
        """
        Apply contextual prosody to text based on emotion, urgency, and relationship.
        
        Args:
            text: Text to process
            context: Dictionary with 'emotion', 'urgency', 'relationship_stage' keys
        
        Returns:
            Processed text with prosody markers
        """
        if context is None:
            context = {}
        
        # Extract context parameters
        emotion = context.get('emotion', 'neutral')
        urgency = context.get('urgency', 'normal')
        relationship = context.get('relationship_stage', 'cooperative')
        
        # Get prosody parameters based on context
        params = self.get_prosody_params(emotion, urgency, relationship)
        
        # Add natural pauses with context-aware durations
        text = self.add_natural_pauses(text, params)
        
        # Add emphasis based on context
        text = self.add_emphasis(text, params)
        
        # Add breath markers
        text = self.add_breath_markers(text, params)
        
        return text
    
    def get_prosody_params(self, emotion, urgency, relationship):
        """
        Map context to prosody parameters.
        
        Returns a dict with pitch, rate, volume, and other prosody modifiers.
        """
        # Base parameters for relationship stage (Hitagi-inspired: deliberate pauses)
        if relationship == 'hostile':
            base_pitch = 0.90  # Lower, more threatening
            base_rate = 0.95   # Measured, not rushed (Hitagi style)
            base_volume = 1.05 # Louder, assertive
            pause_multiplier = 1.2  # Longer pauses for intimidation (Hitagi style)
        elif relationship == 'curious':
            base_pitch = 0.97  # Slight lift but controlled
            base_rate = 0.93   # Measured pace
            base_volume = 1.0  # Normal volume
            pause_multiplier = 1.15  # Thoughtful pauses
        else:  # cooperative
            base_pitch = 0.95  # Lower register maintained
            base_rate = 0.90   # Slower, deliberate (Hitagi style)
            base_volume = 1.0  # Normal volume
            pause_multiplier = 1.25  # Longer, thoughtful pauses (Hitagi style)
        
        # Adjust for emotion (maintaining Hitagi's controlled style)
        if emotion == 'excited':
            base_pitch *= 1.03  # Minimal lift (controlled excitement)
            base_rate *= 1.08   # Slightly faster but measured
            pause_multiplier *= 1.0  # Maintain deliberate pauses
        elif emotion == 'worried':
            base_pitch *= 0.96  # Slightly lower
            base_rate *= 0.92   # Slower, cautious
            pause_multiplier *= 1.3  # Longer, hesitant pauses
        elif emotion == 'contemplative':
            base_rate *= 0.88   # Very deliberate
            pause_multiplier *= 1.4  # Longest pauses for deep thought
        elif emotion == 'angry':
            base_rate *= 0.98   # Controlled anger (not rushed)
            pause_multiplier *= 1.15  # Deliberate, controlled pauses
        elif emotion == 'hostile':
            pause_multiplier *= 1.2  # Strategic pauses for threat
        
        # Adjust for urgency (even urgent is controlled in Hitagi style)
        if urgency == 'critical':
            base_rate *= 1.15   # Faster but not panicked
            base_volume *= 1.15
            pause_multiplier *= 0.85  # Shorter but still present
        elif urgency == 'high':
            base_rate *= 1.08   # Moderately faster
            base_volume *= 1.08
            pause_multiplier *= 0.95  # Slightly shorter pauses
        
        return {
            'pitch': base_pitch,
            'rate': base_rate,
            'volume': base_volume,
            'pause_multiplier': pause_multiplier
        }
    
    def add_natural_pauses(self, text, params=None):
        """Add natural pause markers to text with context-aware durations."""
        
        if params is None:
            params = {'pause_multiplier': 1.0}
        
        multiplier = params.get('pause_multiplier', 1.0)
        
        # Add pauses after punctuation (Hitagi style: longer, more deliberate)
        # Use extra spaces for basic pause effect - more spaces = longer pause
        # Hitagi's characteristic: strategic, controlled pauses
        if multiplier <= 0.85:  # Shorter pauses (urgent)
            period_spaces = '  '
            exclaim_spaces = '  '
            question_spaces = '  '
            comma_spaces = ' '
        elif multiplier <= 1.1:  # Normal pauses (Hitagi baseline)
            period_spaces = '   '
            exclaim_spaces = '  '
            question_spaces = '   '
            comma_spaces = ' '
        else:  # Long pauses (contemplative/cooperative - very Hitagi)
            period_spaces = '    '  # Extra long for deliberate effect
            exclaim_spaces = '   '
            question_spaces = '    '
            comma_spaces = '  '
        
        text = text.replace('. ', '.' + period_spaces)
        text = text.replace('! ', '!' + exclaim_spaces)
        text = text.replace('? ', '?' + question_spaces)
        text = text.replace(', ', ',' + comma_spaces)
        
        # Add thinking pauses before certain words
        thinking_words = ['well', 'hmm', 'let me', 'perhaps', 'you know', 'now', 'actually']
        for word in thinking_words:
            # Case-insensitive replacement
            pattern = re.compile(r'\s+' + re.escape(word) + r'\s+', re.IGNORECASE)
            matches = pattern.finditer(text)
            for match in matches:
                original = match.group(0)
                # Add extra space before the thinking word (more for longer pauses)
                extra_space = '  ' if multiplier > 1.0 else ' '
                replacement = extra_space + original.strip() + ' '
                text = text.replace(original, replacement, 1)
        
        return text
    
    def add_emphasis(self, text, params=None):
        """Add emphasis markers to important words."""
        
        # Words that typically get emphasis
        emphasis_words = [
            'never', 'always', 'must', 'critical', 'important',
            'warning', 'danger', 'amazing', 'incredible', 'fascinating',
            'need', 'should', 'really', 'very', 'extremely',
            'absolutely', 'definitely', 'certainly', 'obviously'
        ]
        
        # Add CAPS emphasis for key words (subtle emphasis through prosody)
        for word in emphasis_words:
            if word in text.lower():
                # Note: Most TTS engines interpret CAPS as emphasis
                # We do a gentle transformation by preserving original case
                # but the presence detection helps with future SSML implementation
                pass
        
        return text
    
    def add_breath_markers(self, text, params=None):
        """Add breath markers in natural places."""
        
        # Add breath after long sentences
        sentences = []
        for sentence in text.split('. '):
            word_count = len(sentence.split())
            if word_count > self.BREATH_MARKER_WORD_THRESHOLD:
                # Add a slight pause marker (extra space simulates breath)
                sentences.append(sentence + '  ')
            else:
                sentences.append(sentence)
        
        return '. '.join(sentences)
    
    def generate_ssml(self, text, params):
        """
        Generate SSML markup for TTS engines that support it.
        
        Note: Currently returns plain text as Coqui TTS has limited SSML support.
        This is a placeholder for future SSML-capable TTS engines.
        
        The params argument is accepted for forward compatibility and may include
        values such as speaking rate and pitch, which can be mapped to SSML
        attributes in engines that support them.
        """
        # For now, return plain text since Coqui TTS doesn't fully support SSML.
        # In future, this could generate proper SSML for compatible engines
        # using values from params (e.g., rate, pitch, emotion).
        return text
    
    def process_text(self, text, context=None):
        """Process text for natural prosody with optional context."""
        if context:
            return self.apply_context_prosody(text, context)
        else:
            # Basic processing for backward compatibility
            text = self.add_natural_pauses(text)
            text = self.add_emphasis(text)
            return text


# ============================================================================
# HUMAN-LIKE ENHANCER - Adds micro-variations and imperfections
# ============================================================================

class HumanLikeEnhancer:
    """Adds human-like variations and imperfections to speech."""
    
    # Constants for breath sound mixing
    BREATH_MIX_SPEECH_LEVEL = 0.95  # Reduce speech to 95% when adding breath
    BREATH_MIX_BREATH_LEVEL = 0.3   # Mix breath at 30% volume
    BREATH_POSITION_VARIANCE = 0.05  # ±5% random variance in breath position
    BREATH_END_BUFFER = 1000  # Minimum buffer samples from end of audio
    
    def __init__(self):
        """Initialize the enhancer with breath sound library."""
        self.breath_sounds = None  # Will be generated on demand
    
    def generate_breath_sound(self, duration_ms=150, sr=22050):
        """
        Generate a subtle breath sound.
        
        Args:
            duration_ms: Duration in milliseconds
            sr: Sample rate
        
        Returns:
            numpy array of breath audio
        """
        duration_samples = int((duration_ms / 1000.0) * sr)
        
        # Generate pink noise (more natural than white noise)
        # Using simple low-pass filtered white noise
        noise = np.random.randn(duration_samples)
        
        # Apply envelope (fade in and out)
        envelope = np.ones(duration_samples)
        fade_samples = duration_samples // 4
        envelope[:fade_samples] = np.linspace(0, 1, fade_samples)
        envelope[-fade_samples:] = np.linspace(1, 0, fade_samples)
        
        breath = noise * envelope * 0.05  # Very quiet
        
        # Low-pass filter to make it sound more like breath
        if SCIPY_AVAILABLE:
            from scipy import signal
            b, a = signal.butter(3, 800, 'lowpass', fs=sr)
            breath = signal.filtfilt(b, a, breath)
        
        return breath
    
    def insert_breath_sounds(self, audio, text, sr=22050):
        """
        Insert subtle breath sounds at natural positions in the audio.
        
        Args:
            audio: Audio samples
            text: Original text to analyze for breath positions
            sr: Sample rate
        
        Returns:
            Audio with breath sounds inserted
        """
        if not AUDIO_PROCESSING_AVAILABLE:
            return audio
        
        # Count sentences to determine breath positions
        sentence_count = text.count('.') + text.count('!') + text.count('?')
        
        if sentence_count <= 1:
            # Short utterance, no breath needed
            return audio
        
        # Estimate positions for breath sounds
        # Divide audio into segments for each sentence
        breath_positions = []
        
        for i in range(1, sentence_count):
            # Place breath roughly at sentence boundaries
            position = int((i / sentence_count) * len(audio))
            # Add some randomness
            position += int(np.random.uniform(-self.BREATH_POSITION_VARIANCE, 
                                             self.BREATH_POSITION_VARIANCE) * len(audio) / sentence_count)
            breath_positions.append(position)
        
        # Insert breath sounds
        for pos in breath_positions:
            if pos < len(audio) - self.BREATH_END_BUFFER:  # Ensure we don't go past the end
                breath = self.generate_breath_sound(duration_ms=120, sr=sr)
                # Mix breath into audio at configured levels
                end_pos = min(pos + len(breath), len(audio))
                audio[pos:end_pos] = (audio[pos:end_pos] * self.BREATH_MIX_SPEECH_LEVEL + 
                                     breath[:end_pos-pos] * self.BREATH_MIX_BREATH_LEVEL)
        
        return audio
    
    def add_micro_variations(self, audio, sr=22050):
        """Add subtle variations to prevent exact repetition."""
        
        if not AUDIO_PROCESSING_AVAILABLE:
            return audio
        
        # Slight timing variations (±2%)
        timing_variation = np.random.uniform(-0.02, 0.02)
        if timing_variation != 0:
            audio = librosa.effects.time_stretch(audio, rate=1.0 + timing_variation)
        
        # Slight pitch variations (±1%)
        pitch_variation = np.random.uniform(-0.01, 0.01)
        if pitch_variation != 0:
            n_steps = pitch_variation * 12  # Convert to semitones
            audio = librosa.effects.pitch_shift(audio, sr=sr, n_steps=n_steps)
        
        # Add very subtle noise for texture (barely perceptible)
        noise = np.random.randn(len(audio)) * 0.0005
        audio = audio + noise
        
        return audio
    
    def normalize_audio(self, audio):
        """Normalize audio to consistent volume."""
        
        # Peak normalization to prevent clipping
        max_val = np.max(np.abs(audio))
        if max_val > 0:
            audio = audio / max_val * 0.95  # Leave 5% headroom
        
        return audio


# ============================================================================
# EMOTIONAL VOICE ENGINE - Main synthesis engine
# ============================================================================

class EmotionalVoiceEngine:
    """Generates emotionally expressive speech."""
    
    def __init__(self, model_name="tts_models/en/ljspeech/tacotron2-DDC", use_gpu=False):
        """Initialize the TTS engine."""
        
        if not TTS_AVAILABLE:
            raise RuntimeError("TTS library not available. Install with: pip install TTS")
        
        self.prosody = ProsodyController()
        self.enhancer = HumanLikeEnhancer()
        
        # Initialize TTS model
        print(f"Loading TTS model: {model_name}", file=sys.stderr)
        self.tts = TTS(model_name=model_name, progress_bar=False, gpu=use_gpu)
        print("TTS model loaded successfully", file=sys.stderr)
    
    def synthesize(self, text, emotion='neutral', add_variations=True, urgency='normal', relationship_stage='cooperative'):
        """
        Generate emotionally expressive speech with contextual prosody.
        
        Args:
            text: Text to synthesize
            emotion: Emotion profile name (see EMOTION_PROFILES)
            add_variations: Whether to add micro-variations
            urgency: Urgency level ('normal', 'high', 'critical')
            relationship_stage: Relationship context ('hostile', 'curious', 'cooperative')
        
        Returns:
            numpy array of audio samples
        """
        
        # Get emotion profile
        profile = EMOTION_PROFILES.get(emotion, EMOTION_PROFILES['neutral'])
        print(f"Using emotion: {emotion} - {profile['description']}", file=sys.stderr)
        
        # Build context for prosody
        context = {
            'emotion': emotion,
            'urgency': urgency,
            'relationship_stage': relationship_stage
        }
        
        # Apply contextual prosody to text
        processed_text = self.prosody.process_text(text, context)
        
        # Generate base audio with TTS
        print("Generating speech...", file=sys.stderr)
        
        # Adjust speed based on emotion and prosody parameters
        prosody_params = self.prosody.get_prosody_params(emotion, urgency, relationship_stage)
        speed = profile['rate'] * prosody_params['rate']
        
        # Generate audio
        # Note: Different TTS models support different parameters
        # Adjust this based on your chosen model
        audio = self.tts.tts(text=processed_text, speed=speed)
        
        # Convert to numpy array if needed
        if not isinstance(audio, np.ndarray):
            audio = np.array(audio)
        
        # Apply emotional processing with prosody-adjusted volume
        if AUDIO_PROCESSING_AVAILABLE:
            # Merge profile with prosody parameters
            adjusted_profile = profile.copy()
            adjusted_profile['volume'] = profile['volume'] * prosody_params['volume']
            adjusted_profile['pitch_shift'] = profile['pitch_shift'] * prosody_params['pitch']
            audio = self.apply_emotional_processing(audio, adjusted_profile)
        
        # Insert breath sounds for natural pauses
        if add_variations and AUDIO_PROCESSING_AVAILABLE:
            audio = self.enhancer.insert_breath_sounds(audio, text)
        
        # Add human-like variations
        if add_variations and AUDIO_PROCESSING_AVAILABLE:
            audio = self.enhancer.add_micro_variations(audio)
        
        # Normalize
        audio = self.enhancer.normalize_audio(audio)
        
        return audio
    
    def apply_emotional_processing(self, audio, profile, sr=22050):
        """Apply audio effects to convey emotion."""
        
        # Pitch shift
        if profile['pitch_shift'] != 0:
            n_steps = profile['pitch_shift'] * 12  # Convert to semitones
            audio = librosa.effects.pitch_shift(audio, sr=sr, n_steps=n_steps)
        
        # Volume adjustment
        if profile['volume'] != 1.0:
            audio = audio * profile['volume']
        
        # Add breathiness for warmth
        if profile['breathiness'] > 0.5:
            # Normalize audio first to prevent clipping when adding noise
            max_amplitude = np.max(np.abs(audio))
            if max_amplitude > 0:
                audio = audio / max_amplitude * 0.9  # Scale to 90% to leave headroom
            
            noise_amount = (profile['breathiness'] - 0.5) * 0.015
            noise = np.random.randn(len(audio)) * noise_amount
            audio = audio + noise
        
        # Add tension for urgency (boost high frequencies)
        if profile['tension'] > 0.6 and SCIPY_AVAILABLE:
            tension_amount = (profile['tension'] - 0.6) * 0.2
            b, a = signal.butter(2, 2000, 'highpass', fs=sr)
            filtered = signal.filtfilt(b, a, audio)
            audio = audio + (filtered * tension_amount)
        
        # Apply Hitagi-style characteristics: crisp articulation
        audio = self.apply_hitagi_voice_characteristics(audio, profile, sr)
        
        return audio
    
    def apply_hitagi_voice_characteristics(self, audio, profile, sr=22050):
        """
        Apply Hitagi Senjougahara-inspired voice characteristics.
        
        Key modifications for Hitagi's vocal style:
        - Enhanced consonant clarity (sharp articulation)
        - Reduced breathiness (crisper tone)
        - Controlled dynamics (less variance)
        - Lower pitch baseline already applied via profile
        """
        if not AUDIO_PROCESSING_AVAILABLE or not SCIPY_AVAILABLE:
            return audio
        
        # 1. Enhance consonant clarity - boost 3-6 kHz range
        # This gives the "sharp, crisp enunciation" characteristic of Hitagi's voice
        b, a = signal.butter(2, [3000, 6000], 'bandpass', fs=sr)
        consonant_boost = signal.filtfilt(b, a, audio)
        audio = audio + (consonant_boost * 0.10)  # 10% boost for clarity
        
        # 2. Reduce excessive breathiness - gentle high-frequency cut
        # Hitagi's voice is less breathy, more controlled
        if profile.get('breathiness', 0.5) < 0.4:  # Only for low-breathiness profiles
            b, a = signal.butter(3, 7500, 'lowpass', fs=sr)
            audio = signal.filtfilt(b, a, audio)
        
        # 3. Controlled dynamics - gentle compression
        # Hitagi's delivery is more consistent, less variance
        audio = self.apply_gentle_compression(audio)
        
        return audio
    
    def apply_gentle_compression(self, audio, threshold_db=-20, ratio=2.5):
        """
        Apply gentle compression for controlled dynamics (Hitagi style).
        
        Reduces dynamic range for more consistent, controlled delivery.
        
        Args:
            audio: Input audio array
            threshold_db: Threshold in dB (default: -20)
            ratio: Compression ratio (default: 2.5)
        
        Returns:
            Compressed audio array
        """
        # Validate parameters
        if ratio <= 0:
            raise ValueError('Compression ratio must be positive')
        
        # Calculate RMS (root mean square) for dynamic measurement
        # Use float64 to prevent overflow with large audio arrays
        rms = np.sqrt(np.mean(audio**2, dtype=np.float64))
        threshold_linear = 10**(threshold_db/20)
        
        # Safety check: ensure RMS is positive and non-zero
        if rms <= 0 or not np.isfinite(rms):
            # Audio is silent or invalid, return as-is
            return audio
        
        # Apply compression if signal exceeds threshold
        if rms > threshold_linear:
            # Calculate gain reduction (safe division and log)
            try:
                excess_db = 20 * np.log10(rms / threshold_linear)
                reduction_db = excess_db * (1 - 1/ratio)
                gain_reduction = 10**(-reduction_db/20)
                audio = audio * gain_reduction
            except (ValueError, FloatingPointError):
                # If calculation fails, return original audio
                pass
        
        return audio
    
    def synthesize_to_file(self, text, output_path, emotion='neutral', add_variations=True, 
                          urgency='normal', relationship_stage='cooperative'):
        """Generate speech and save to file with contextual prosody."""
        
        # Synthesize with all parameters
        audio = self.synthesize(text, emotion, add_variations, urgency, relationship_stage)
        
        # Save to file
        if AUDIO_PROCESSING_AVAILABLE:
            sf.write(output_path, audio, 22050)
            print(f"SUCCESS: Audio saved to {output_path}", file=sys.stdout)
        else:
            # Fallback: save raw audio (may not work perfectly)
            print("WARNING: Audio processing libraries not available, saving basic audio", file=sys.stderr)
            # This is a simplified save that may not work with all TTS outputs
            with wave.open(output_path, 'w') as wf:
                wf.setnchannels(1)  # Mono
                wf.setsampwidth(2)  # 16-bit
                wf.setframerate(22050)
                wf.writeframes((audio * 32767).astype(np.int16).tobytes())
            print(f"SUCCESS: Audio saved to {output_path}", file=sys.stdout)
        
        return output_path


# ============================================================================
# COMMAND LINE INTERFACE
# ============================================================================

def main():
    """Main function for command-line usage."""
    
    parser = argparse.ArgumentParser(
        description='Generate human-like TTS audio with emotional expression and contextual prosody',
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog="""
Examples:
  # Basic usage
  python3 tts_generate_human.py --text "Hello, I'm Mittenz!" --output hello.wav
  
  # With emotion and urgency
  python3 tts_generate_human.py --text "Warning! Oxygen critical!" --emotion urgent --urgency critical --output warning.wav
  
  # With relationship context
  python3 tts_generate_human.py --text "What does this do?" --emotion curious --relationship curious --output question.wav
  
  # List available emotions
  python3 tts_generate_human.py --list-emotions
  
Available emotions: """ + ', '.join(EMOTION_PROFILES.keys())
    )
    
    parser.add_argument('--text', type=str, help='Text to synthesize')
    parser.add_argument('--output', type=str, help='Output audio file path')
    parser.add_argument('--emotion', type=str, default='neutral',
                       choices=list(EMOTION_PROFILES.keys()),
                       help='Emotion profile to use (default: neutral)')
    parser.add_argument('--urgency', type=str, default='normal',
                       choices=['normal', 'high', 'critical'],
                       help='Urgency level for prosody adjustment (default: normal)')
    parser.add_argument('--relationship', type=str, default='cooperative',
                       choices=['hostile', 'curious', 'cooperative'],
                       help='Relationship stage for voice adaptation (default: cooperative)')
    parser.add_argument('--model', type=str, 
                       default='tts_models/en/ljspeech/tacotron2-DDC',
                       help='TTS model name (default: tacotron2-DDC)')
    parser.add_argument('--gpu', action='store_true',
                       help='Use GPU acceleration if available')
    parser.add_argument('--no-variations', action='store_true',
                       help='Disable micro-variations for consistent output')
    parser.add_argument('--list-emotions', action='store_true',
                       help='List available emotion profiles')
    
    args = parser.parse_args()
    
    # List emotions if requested
    if args.list_emotions:
        print("\nAvailable Emotion Profiles:")
        print("=" * 60)
        for emotion, profile in sorted(EMOTION_PROFILES.items()):
            print(f"\n{emotion:15} - {profile['description']}")
            print(f"  Pitch: {profile['pitch_shift']:+.2f}, Rate: {profile['rate']:.2f}, "
                  f"Volume: {profile['volume']:.2f}")
        print("\n" + "=" * 60)
        print("\nUrgency Levels: normal, high, critical")
        print("Relationship Stages: hostile, curious, cooperative")
        return 0
    
    # Validate required arguments
    if not args.text or not args.output:
        parser.error("--text and --output are required (unless using --list-emotions)")
    
    # Check dependencies
    if not TTS_AVAILABLE:
        print("ERROR: TTS library not installed. Install with:", file=sys.stderr)
        print("  pip install TTS", file=sys.stderr)
        return 1
    
    if not AUDIO_PROCESSING_AVAILABLE:
        print("WARNING: Audio processing libraries not fully available.", file=sys.stderr)
        print("For best results, install with:", file=sys.stderr)
        print("  pip install librosa soundfile scipy", file=sys.stderr)
        print("Continuing with limited functionality...\n", file=sys.stderr)
    
    try:
        # Initialize engine
        engine = EmotionalVoiceEngine(model_name=args.model, use_gpu=args.gpu)
        
        # Generate speech with contextual prosody
        engine.synthesize_to_file(
            text=args.text,
            output_path=args.output,
            emotion=args.emotion,
            add_variations=not args.no_variations,
            urgency=args.urgency,
            relationship_stage=args.relationship
        )
        
        return 0
        
    except Exception as e:
        print(f"ERROR: {str(e)}", file=sys.stderr)
        import traceback
        traceback.print_exc(file=sys.stderr)
        return 1


if __name__ == '__main__':
    sys.exit(main())
