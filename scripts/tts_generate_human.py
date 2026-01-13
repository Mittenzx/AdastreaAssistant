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
# EMOTION PROFILES - Define voice characteristics for each emotion
# ============================================================================

EMOTION_PROFILES = {
    # Hostile stage emotions
    'hostile': {
        'pitch_shift': -0.05,    # Lower, more aggressive
        'rate': 1.1,             # Faster, agitated
        'volume': 1.1,           # Louder, assertive
        'tension': 0.8,          # Tenser voice quality
        'breathiness': 0.2,      # Less breathy, more sharp
        'description': 'Sharp, defensive, bratty'
    },
    'angry': {
        'pitch_shift': -0.03,
        'rate': 1.15,
        'volume': 1.15,
        'tension': 0.9,
        'breathiness': 0.1,
        'description': 'Upset, demanding'
    },
    
    # Curious stage emotions
    'curious': {
        'pitch_shift': 0.05,     # Higher, inquisitive
        'rate': 1.0,             # Normal speed
        'volume': 1.0,           # Normal volume
        'tension': 0.5,          # Relaxed
        'breathiness': 0.5,      # Moderately breathy
        'description': 'Interested, questioning'
    },
    'fascinated': {
        'pitch_shift': 0.08,
        'rate': 1.05,
        'volume': 1.05,
        'tension': 0.6,
        'breathiness': 0.4,
        'description': 'Intellectually amazed and deeply intrigued, more wonder than high-energy excitement'
    },
    
    # Cooperative stage emotions
    'cooperative': {
        'pitch_shift': 0.0,      # Natural pitch
        'rate': 0.95,            # Slightly slower, thoughtful
        'volume': 1.0,           # Normal volume
        'tension': 0.4,          # Very relaxed
        'breathiness': 0.6,      # More breathy, warmer
        'description': 'Warm, friendly, helpful'
    },
    'friendly': {
        'pitch_shift': 0.02,
        'rate': 0.95,
        'volume': 1.0,
        'tension': 0.4,
        'breathiness': 0.7,
        'description': 'Caring, supportive'
    },
    
    # Universal emotions
    'excited': {
        'pitch_shift': 0.1,      # Higher, energetic
        'rate': 1.2,             # Faster
        'volume': 1.15,          # Louder
        'tension': 0.7,          # More energetic
        'breathiness': 0.3,      # Less breathy
        'description': 'Energetic, enthusiastic'
    },
    'worried': {
        'pitch_shift': -0.03,    # Slightly lower
        'rate': 0.9,             # Slower, cautious
        'volume': 0.95,          # Slightly quieter
        'tension': 0.7,          # Tense
        'breathiness': 0.4,      # Moderate
        'description': 'Concerned, anxious'
    },
    'contemplative': {
        'pitch_shift': -0.02,
        'rate': 0.85,
        'volume': 0.9,
        'tension': 0.3,
        'breathiness': 0.7,
        'description': 'Thoughtful, reflective'
    },
    'urgent': {
        'pitch_shift': 0.08,
        'rate': 1.3,
        'volume': 1.2,
        'tension': 0.9,
        'breathiness': 0.2,
        'description': 'Critical, immediate'
    },
    'neutral': {
        'pitch_shift': 0.0,
        'rate': 1.0,
        'volume': 1.0,
        'tension': 0.5,
        'breathiness': 0.5,
        'description': 'Balanced, calm'
    }
}


# ============================================================================
# PROSODY CONTROLLER - Adds natural speech patterns
# ============================================================================

class ProsodyController:
    """Controls natural speech prosody patterns."""
    
    def add_natural_pauses(self, text):
        """Add natural pause markers to text."""
        
        # Add pauses after punctuation (simulate natural breathing)
        text = text.replace('. ', '.  ')  # Longer pause after period
        text = text.replace('! ', '!  ')  # Excited pause
        text = text.replace('? ', '?  ')  # Question pause
        text = text.replace(', ', ', ')   # Short comma pause
        
        # Add thinking pauses before certain words
        thinking_words = ['well', 'hmm', 'let me', 'perhaps', 'you know']
        for word in thinking_words:
            if f' {word} ' in text.lower():
                text = text.replace(f' {word} ', f'  {word} ')
        
        return text
    
    def add_emphasis(self, text):
        """Add slight emphasis to important words."""
        
        # Words that typically get emphasis
        emphasis_words = [
            'never', 'always', 'must', 'critical', 'important',
            'warning', 'danger', 'amazing', 'incredible', 'fascinating',
            'need', 'should', 'really', 'very', 'extremely'
        ]
        
        # Mark emphasized words (can be used by TTS engine if supported)
        for word in emphasis_words:
            if word in text.lower():
                # Note: Actual emphasis implementation depends on TTS capabilities
                pass
        
        return text
    
    def process_text(self, text):
        """Process text for natural prosody."""
        text = self.add_natural_pauses(text)
        text = self.add_emphasis(text)
        return text


# ============================================================================
# HUMAN-LIKE ENHANCER - Adds micro-variations and imperfections
# ============================================================================

class HumanLikeEnhancer:
    """Adds human-like variations and imperfections to speech."""
    
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
    
    def synthesize(self, text, emotion='neutral', add_variations=True):
        """
        Generate emotionally expressive speech.
        
        Args:
            text: Text to synthesize
            emotion: Emotion profile name (see EMOTION_PROFILES)
            add_variations: Whether to add micro-variations
        
        Returns:
            numpy array of audio samples
        """
        
        # Get emotion profile
        profile = EMOTION_PROFILES.get(emotion, EMOTION_PROFILES['neutral'])
        print(f"Using emotion: {emotion} - {profile['description']}", file=sys.stderr)
        
        # Apply prosody to text
        processed_text = self.prosody.process_text(text)
        
        # Generate base audio with TTS
        print("Generating speech...", file=sys.stderr)
        
        # Adjust speed based on emotion
        speed = profile['rate']
        
        # Generate audio
        # Note: Different TTS models support different parameters
        # Adjust this based on your chosen model
        audio = self.tts.tts(text=processed_text, speed=speed)
        
        # Convert to numpy array if needed
        if not isinstance(audio, np.ndarray):
            audio = np.array(audio)
        
        # Apply emotional processing
        if AUDIO_PROCESSING_AVAILABLE:
            audio = self.apply_emotional_processing(audio, profile)
        
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
        
        return audio
    
    def synthesize_to_file(self, text, output_path, emotion='neutral', add_variations=True):
        """Generate speech and save to file."""
        
        # Synthesize
        audio = self.synthesize(text, emotion, add_variations)
        
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
        description='Generate human-like TTS audio with emotional expression',
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog="""
Examples:
  # Basic usage
  python3 tts_generate_human.py --text "Hello, I'm Mittenz!" --output hello.wav
  
  # With emotion
  python3 tts_generate_human.py --text "We need to check oxygen!" --emotion urgent --output warning.wav
  
  # List available emotions
  python3 tts_generate_human.py --list-emotions
  
Available emotions: """ + ', '.join(EMOTION_PROFILES.keys())
    )
    
    parser.add_argument('--text', type=str, help='Text to synthesize')
    parser.add_argument('--output', type=str, help='Output audio file path')
    parser.add_argument('--emotion', type=str, default='neutral',
                       choices=list(EMOTION_PROFILES.keys()),
                       help='Emotion profile to use (default: neutral)')
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
        
        # Generate speech
        engine.synthesize_to_file(
            text=args.text,
            output_path=args.output,
            emotion=args.emotion,
            add_variations=not args.no_variations
        )
        
        return 0
        
    except Exception as e:
        print(f"ERROR: {str(e)}", file=sys.stderr)
        import traceback
        traceback.print_exc(file=sys.stderr)
        return 1


if __name__ == '__main__':
    sys.exit(main())
