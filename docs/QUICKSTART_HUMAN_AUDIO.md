# Quick Start: Human-like Audio Generation

This guide helps you get started with generating human-like audio for Mittenz using Coqui TTS.

## Prerequisites

- Python 3.8 or later
- pip package manager
- (Optional) CUDA-capable GPU for faster generation

## Installation (5-10 minutes)

### Step 1: Create Virtual Environment

```bash
# Navigate to the project root
cd /path/to/AdastreaAssistant

# Create virtual environment
python3 -m venv tts-human-env

# Activate it
source tts-human-env/bin/activate  # On Linux/Mac
# OR
tts-human-env\Scripts\activate  # On Windows
```

### Step 2: Install Dependencies

```bash
# Install Coqui TTS
pip install TTS

# Install audio processing libraries
pip install librosa soundfile scipy

# Verify installation
python3 -c "from TTS.api import TTS; print('TTS installed successfully!')"
```

## Basic Usage

### List Available Emotions

```bash
python3 scripts/tts_generate_human.py --list-emotions
```

Output:
```
Available Emotion Profiles:
============================================================

angry           - Upset, demanding
  Pitch: -0.03, Rate: 1.15, Volume: 1.15

cooperative     - Warm, friendly, helpful
  Pitch: +0.00, Rate: 0.95, Volume: 1.00

curious         - Interested, questioning
  Pitch: +0.05, Rate: 1.00, Volume: 1.00

[... more emotions ...]
```

### Generate Your First Audio

```bash
# Basic greeting (neutral emotion)
python3 scripts/tts_generate_human.py \
  --text "Hello there! I'm Mittenz, your space exploration companion." \
  --output test_greeting.wav

# Urgent warning
python3 scripts/tts_generate_human.py \
  --text "Warning! Oxygen levels critical!" \
  --emotion urgent \
  --output test_warning.wav

# Curious question
python3 scripts/tts_generate_human.py \
  --text "What does this do? I've never seen anything like this before." \
  --emotion curious \
  --output test_curious.wav
```

### Generate All Mittenz Dialogue Samples

Use the provided batch script:

```bash
# Make the script executable
chmod +x scripts/regenerate_samples.sh

# Run it
./scripts/regenerate_samples.sh
```

Or manually:

```bash
# Hostile stage
python3 scripts/tts_generate_human.py \
  --text "Who the hell are you? Where is my dad?" \
  --emotion hostile \
  --output src/main/resources/audio/dialogue/mittenz_hostile_01.wav

# Curious stage
python3 scripts/tts_generate_human.py \
  --text "What does this do? I've never seen anything like this before." \
  --emotion curious \
  --output src/main/resources/audio/dialogue/mittenz_curious_01.wav

# Cooperative stage
python3 scripts/tts_generate_human.py \
  --text "We need to check the oxygen levels." \
  --emotion cooperative \
  --output src/main/resources/audio/dialogue/mittenz_cooperative_01.wav
```

## Advanced Options

### Use GPU Acceleration

```bash
python3 scripts/tts_generate_human.py \
  --text "Your text here" \
  --output output.wav \
  --gpu
```

### Use Different TTS Model

```bash
# List available models
python3 -c "from TTS.api import TTS; TTS().list_models()"

# Use a specific model
python3 scripts/tts_generate_human.py \
  --text "Your text here" \
  --output output.wav \
  --model "tts_models/en/vctk/vits"
```

### Disable Micro-Variations (Consistent Output)

```bash
python3 scripts/tts_generate_human.py \
  --text "Your text here" \
  --output output.wav \
  --no-variations
```

## Testing the Results

### Play Audio (Linux)

```bash
aplay test_greeting.wav
```

### Play Audio (Mac)

```bash
afplay test_greeting.wav
```

### Play Audio (Windows)

```bash
start test_greeting.wav
```

### Compare with Current Samples

```bash
# Play old espeak version
aplay src/main/resources/audio/greetings/greeting_initial.wav

# Play new Coqui TTS version
aplay test_greeting.wav

# Notice the improved quality and naturalness!
```

## Troubleshooting

### "TTS module not found"

```bash
# Ensure you're in the virtual environment
source tts-human-env/bin/activate

# Reinstall TTS
pip install --upgrade TTS

# Verify
python3 -c "import TTS; print(TTS.__version__)"
```

### "Audio generation is too slow"

- **Solution 1**: Use GPU acceleration with `--gpu` flag
- **Solution 2**: Use a faster model: `--model "tts_models/en/ljspeech/glow-tts"`
- **Solution 3**: Pre-generate all common phrases during build time

### "librosa not found"

```bash
pip install librosa soundfile scipy
```

### "Voice quality is not good enough"

1. **Try different models**: Some models produce better quality
   ```bash
   # List models
   python3 -c "from TTS.api import TTS; TTS().list_models()"
   
   # Try VITS model (high quality)
   python3 scripts/tts_generate_human.py --model "tts_models/en/vctk/vits" ...
   ```

2. **Adjust emotion settings**: Tweak the emotion profiles in the script

3. **Consider upgrading to GPT-SoVITS**: For truly unique Mittenz voice (see Phase 6 in HUMAN_AUDIO_PLAN.md)

## Next Steps

1. **Read the full plan**: See `docs/HUMAN_AUDIO_PLAN.md` for the complete 10-week roadmap

2. **Integrate with Java**: 
   - Study `src/main/java/com/adastrea/assistant/AudioManager.java`
   - Create `CoquiTTSAudioManager` that calls the Python script
   - See examples in `docs/IMPLEMENTATION_ROADMAP.md`

3. **Test with game**:
   - Replace existing audio samples with new TTS-generated ones
   - Test in actual gameplay
   - Gather user feedback

4. **Iterate**:
   - Adjust emotion profiles based on testing
   - Fine-tune voice characteristics
   - Add more dialogue variations

## Performance Benchmarks

**Expected Performance** (on modern hardware):

- **CPU Mode**: 2-5 seconds per phrase
- **GPU Mode**: 0.5-2 seconds per phrase
- **File Size**: ~100-500 KB per audio file (WAV, 16-bit, 22050 Hz)
- **Quality**: 7-8/10 naturalness (vs. 4/10 for espeak)

## Resources

- **Full Documentation**: `docs/HUMAN_AUDIO_PLAN.md`
- **Implementation Roadmap**: `docs/IMPLEMENTATION_ROADMAP.md`
- **Voice Research**: `VOICE_ASSISTANT_RESEARCH.md`
- **Coqui TTS Docs**: https://tts.readthedocs.io/
- **Coqui TTS GitHub**: https://github.com/coqui-ai/TTS

## Getting Help

If you encounter issues:

1. Check the troubleshooting section above
2. Review `docs/HUMAN_AUDIO_PLAN.md` for detailed information
3. Check Coqui TTS documentation: https://tts.readthedocs.io/
4. Open an issue in the repository with:
   - Error message
   - Python version
   - OS and hardware specs
   - Steps to reproduce

---

**Happy voice generation!** 🎙️✨

Your AI companion Mittenz is about to sound much more human and emotionally engaging!
