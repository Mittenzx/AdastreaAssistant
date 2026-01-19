# Audio Regeneration Notes

**Date**: January 19, 2026  
**Status**: Ready for Regeneration  
**Voice Model**: Hitagi Senjougahara-Inspired (v2.0)

---

## Current Status

The TTS generation scripts and Hitagi-inspired voice profiles are fully implemented and tested. The current audio files in `src/main/resources/audio/` were generated on **January 18, 2026** and need to be regenerated with the latest voice model enhancements.

### What Changed in v2.0

The VOICE_MODEL_TARGET.md document has been enhanced with:

1. **Troubleshooting Section**: Common issues and solutions for voice generation
2. **Quality Assurance Checklist**: Comprehensive validation criteria
3. **Advanced Customization**: Guidelines for creating custom emotion profiles
4. **Parameter Tuning Guide**: How to adjust voice characteristics
5. **Audio Generation Best Practices**: Testing and validation workflows

---

## Python Version Requirement

**CRITICAL**: The Coqui TTS library requires **Python 3.9-3.11** and does NOT support Python 3.12+.

### Current Environment
- Python version: **3.12.3**
- Status: ❌ Incompatible with TTS library

### Solution Options

#### Option 1: Use pyenv (Recommended)
```bash
# Install pyenv if not already installed
curl https://pyenv.run | bash

# Install Python 3.11
pyenv install 3.11.7

# Set local Python version for this project
cd /path/to/AdastreaAssistant
pyenv local 3.11.7

# Verify
python --version  # Should show Python 3.11.7

# Create virtual environment
python -m venv tts-env
source tts-env/bin/activate

# Install dependencies
pip install -r requirements.txt
```

#### Option 2: Use conda
```bash
# Create conda environment with Python 3.11
conda create -n tts-env python=3.11
conda activate tts-env

# Install dependencies
pip install -r requirements.txt
```

#### Option 3: Use Docker
```bash
# Use a Docker container with Python 3.11
docker run -it --rm -v $(pwd):/app python:3.11 bash
cd /app
pip install -r requirements.txt
```

---

## Regeneration Instructions

Once you have a Python 3.9-3.11 environment set up:

### Step 1: Verify Installation

```bash
# Activate your Python 3.11 environment
source tts-env/bin/activate  # or conda activate tts-env

# Verify Python version
python --version  # Should show 3.9.x, 3.10.x, or 3.11.x

# Verify TTS installation
python -c "from TTS.api import TTS; print('TTS available')"

# List available emotions
python scripts/tts_generate_human.py --list-emotions
```

### Step 2: Test Generation

Generate a test sample to verify everything works:

```bash
python scripts/tts_generate_human.py \
  --text "Hello! This is a test of the Hitagi-inspired voice model." \
  --emotion neutral \
  --output /tmp/test_voice.wav

# Listen to the test (Linux)
aplay /tmp/test_voice.wav

# Listen to the test (Mac)
afplay /tmp/test_voice.wav
```

### Step 3: Backup Current Files

The regeneration script automatically creates backups, but you can also manually backup:

```bash
# Manual backup
cp -r src/main/resources/audio src/main/resources/audio_backup_$(date +%Y%m%d_%H%M%S)
```

### Step 4: Regenerate All Samples

```bash
# Make script executable (if not already)
chmod +x scripts/regenerate_samples.sh

# Run regeneration
./scripts/regenerate_samples.sh
```

This will generate **19 audio samples**:
- 3 greetings
- 9 dialogue samples (3 per relationship stage)
- 3 companion messages  
- 4 notifications

Expected time: **2-5 minutes** on CPU, **30-120 seconds** on GPU

### Step 5: Quality Validation

After regeneration, validate the audio files:

```bash
# Check all files were created
find src/main/resources/audio -name "*.wav" -type f | wc -l
# Should output: 34

# Check file sizes (should be 50KB-400KB each)
ls -lh src/main/resources/audio/**/*.wav

# Validate audio format (requires sox)
for f in src/main/resources/audio/**/*.wav; do
  soxi "$f" | grep -E "(Sample Rate|Channels|Bit Rate)"
done
```

### Step 6: Listen and Compare

Compare a few samples with the backups:

```bash
# Play old version
aplay src/main/resources/audio_backup_*/dialogue/mittenz_hostile_01.wav

# Play new version
aplay src/main/resources/audio/dialogue/mittenz_hostile_01.wav

# Compare characteristics:
# - Lower pitch (mature, not high-pitched)
# - Slower, measured delivery
# - Crisp, clear articulation
# - Strategic pauses
# - Controlled emotional expression
```

---

## Expected Characteristics

### Voice Quality Checklist

After regeneration, the audio should exhibit these Hitagi-inspired characteristics:

✅ **Pitch**: Lower-medium range (8% lower than typical female TTS)  
✅ **Pace**: 10-15% slower, measured delivery  
✅ **Articulation**: Crisp, clear, sharp consonants  
✅ **Pauses**: 20-30% longer, deliberate and strategic  
✅ **Emotion**: Subtle, controlled expression (never over-the-top)  
✅ **Breathiness**: Reduced, clearer tone  
✅ **Dynamics**: Controlled, consistent volume  

### Technical Specifications

All audio files should meet these specs:

- **Format**: WAV (PCM)
- **Sample Rate**: 22050 Hz (TTS default) or 44100 Hz (if upsampled)
- **Bit Depth**: 16-bit
- **Channels**: Mono
- **File Size**: 50-400 KB per file
- **Duration**: 1-8 seconds per phrase

---

## Troubleshooting

### Issue: TTS Model Download Fails

**Error**: "Failed to download model"

**Solution**:
```bash
# Pre-download the model
python -c "from TTS.api import TTS; TTS('tts_models/en/ljspeech/tacotron2-DDC')"

# Try regeneration again
./scripts/regenerate_samples.sh
```

### Issue: Voice Sounds Too Robotic

**Solution**: Verify that all audio processing libraries are installed:
```bash
pip install librosa soundfile scipy --upgrade
```

Check that `apply_hitagi_voice_characteristics()` is being called in the TTS script.

### Issue: Generation is Very Slow

**Solutions**:
1. Use GPU acceleration: Add `--gpu` flag in `regenerate_samples.sh`
2. Use a faster model: Change MODEL to `"tts_models/en/ljspeech/glow-tts"`
3. Reduce sample rate in post-processing (not recommended, reduces quality)

### Issue: Audio Files Have Different Volumes

**Solution**: The TTS script normalizes audio, but you can batch normalize:
```bash
# Install sox if needed: sudo apt-get install sox

# Normalize all files to consistent volume
for f in src/main/resources/audio/**/*.wav; do
  sox "$f" "${f%.wav}_norm.wav" norm -0.5
  mv "${f%.wav}_norm.wav" "$f"
done
```

---

## Post-Regeneration Tasks

After successfully regenerating all audio files:

### 1. Commit to Git

```bash
git add src/main/resources/audio/
git commit -m "Regenerate audio samples with Hitagi-inspired voice model v2.0

- Updated all 19 voice samples with enhanced vocal characteristics
- Lower pitch, measured delivery, crisp articulation
- Strategic pauses and controlled emotional expression
- See docs/VOICE_MODEL_TARGET.md for full specification"
```

### 2. Update Documentation

Mark these tasks as complete in VOICE_MODEL_TARGET.md:
- [x] Regenerate all 19 audio samples with new voice profile
- [x] Validate all generated audio files
- [x] Run quality assurance checklist

### 3. Test in Game

- Load the game with new audio files
- Test playback in various scenarios
- Verify AudioManager loads all files correctly
- Check for any audio artifacts or issues
- Gather initial feedback

### 4. Update Status

Update VOICE_MODEL_TARGET.md status section:
```markdown
**Version**: 2.0  
**Status**: Production - Deployed  
**Last Updated**: [DATE]  
**Next Steps**: Monitor gameplay feedback and iterate if needed
```

---

## Notes for Maintainer

The audio files currently in the repository (dated January 18, 2026) were generated with the Hitagi-inspired voice model. However, they should be regenerated to ensure they incorporate all the latest enhancements and refinements documented in VOICE_MODEL_TARGET.md v2.0.

The regeneration process is straightforward once a Python 3.11 environment is available. If you need assistance with:

- Setting up the Python environment
- Troubleshooting TTS installation
- Fine-tuning voice parameters
- Validating audio quality

Refer to:
- `docs/VOICE_MODEL_TARGET.md` - Complete voice model specification
- `docs/QUICKSTART_HUMAN_AUDIO.md` - Setup and usage guide
- `docs/TTS_QUICKSTART.md` - TTS system quick reference

---

**Ready to regenerate?** Follow the instructions above with a Python 3.9-3.11 environment! 🎙️✨
