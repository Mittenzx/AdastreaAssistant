# Quick Start Guide: Coqui TTS Setup

This guide will help you get Coqui TTS up and running for Phase 1 of the human voice strategy.

## Prerequisites

- **Python**: Version 3.9, 3.10, or 3.11 (NOT 3.12+ due to current Coqui TTS compatibility)
- **Java**: Version 17 or higher
- **Disk Space**: ~500MB for TTS models and dependencies
- **RAM**: 2GB+ recommended for audio generation

## Installation Steps

### 1. Check Your Python Version

```bash
python3 --version
```

If you have Python 3.12+, you'll need to install Python 3.11:

**Ubuntu/Debian**:
```bash
sudo apt install python3.11 python3.11-venv
```

**macOS** (with Homebrew):
```bash
brew install python@3.11
```

**Windows**:
Download Python 3.11 from https://www.python.org/downloads/

### 2. Create a Virtual Environment (Recommended)

```bash
# Navigate to project directory
cd /path/to/AdastreaAssistant

# Create virtual environment with Python 3.11
python3.11 -m venv tts-env

# Activate it
# On Linux/Mac:
source tts-env/bin/activate
# On Windows:
tts-env\Scripts\activate
```

### 3. Install Coqui TTS and Dependencies

```bash
pip install -r requirements.txt
```

Or install manually:
```bash
pip install TTS librosa soundfile scipy numpy
```

This will download:
- Coqui TTS (~200MB)
- Audio processing libraries (~100MB)
- Dependencies (~50MB)

**Note**: Initial download may take 5-10 minutes depending on your connection.

### 4. Verify Installation

```bash
python3 scripts/tts_generate_human.py --list-emotions
```

You should see a list of available emotions. If you see this, TTS is installed correctly!

### 5. Test Audio Generation

Generate a test audio file:

```bash
python3 scripts/tts_generate_human.py \
    --text "Hello, I'm Mittenz! This is a test." \
    --emotion friendly \
    --output test_audio.wav
```

This will:
- Download the TTS model (first time only, ~200MB)
- Generate the audio file
- Save it as `test_audio.wav`

**First run**: May take 1-2 minutes to download model
**Subsequent runs**: ~2-5 seconds per phrase

### 6. Regenerate All Voice Samples

Once TTS is working, regenerate all 19 voice samples:

```bash
# Make script executable (Linux/Mac)
chmod +x scripts/regenerate_samples.sh

# Run regeneration
./scripts/regenerate_samples.sh
```

This will:
- Create a backup of existing audio
- Generate all 19 voice samples with emotions
- Save to `src/main/resources/audio/`

**Time**: ~5-10 minutes total

### 7. Test in Java

Run the demo application:

```bash
./gradlew run --args='com.adastrea.assistant.CoquiTTSDemo'
```

If TTS is installed correctly, you'll see:
```
[INFO] Coqui TTS is available and ready
```

If not, you'll see:
```
[WARNING] Coqui TTS library not installed
```

## Troubleshooting

### "ModuleNotFoundError: No module named 'TTS'"

**Solution**: TTS not installed. Run:
```bash
pip install TTS
```

### "Python version incompatible"

**Solution**: You need Python 3.9-3.11. Check version:
```bash
python3 --version
```

Install Python 3.11 if needed (see step 1).

### "No space left on device"

**Solution**: Free up disk space. Need at least 1GB free.

```bash
# Check space
df -h

# Clean pip cache if needed
pip cache purge
```

### "Command 'python3' not found"

**Solution**: Python not installed or not in PATH.

**Linux/Mac**: Install Python 3.11
```bash
# Ubuntu/Debian
sudo apt install python3.11

# macOS
brew install python@3.11
```

**Windows**: Download from https://www.python.org/downloads/

### Audio generation is very slow

**Causes**:
1. First run (downloading model)
2. No GPU acceleration
3. Low-end hardware

**Solutions**:
- First run: Wait for model download (one-time)
- GPU: Install PyTorch with CUDA if you have NVIDIA GPU
- Hardware: Generation takes 2-5 seconds on modern CPU

### Java can't find TTS

**Issue**: Java reports TTS not available but Python works.

**Solutions**:
1. Make sure virtual environment is active when running Java
2. Or install TTS globally: `pip install --user TTS`
3. Check Python executable path in Java code

## Using with Virtual Environment

If using a virtual environment, you need to activate it before running the Java application:

```bash
# Activate virtual environment
source tts-env/bin/activate

# Run Java application
./gradlew run --args='com.adastrea.assistant.CoquiTTSDemo'

# Deactivate when done
deactivate
```

Or modify `CoquiTTSAudioManager` to use the virtual environment Python:

```java
String venvPython = "/path/to/tts-env/bin/python3";
CoquiTTSAudioManager audioManager = new CoquiTTSAudioManager(
    projectRoot, 
    venvPython,  // Use venv Python
    true
);
```

## GPU Acceleration (Optional)

For faster generation (1-2 seconds instead of 3-5), install PyTorch with CUDA:

**Check if you have NVIDIA GPU**:
```bash
nvidia-smi
```

**Install PyTorch with CUDA**:
```bash
# For CUDA 11.8
pip install torch torchvision torchaudio --index-url https://download.pytorch.org/whl/cu118

# For CUDA 12.1
pip install torch torchvision torchaudio --index-url https://download.pytorch.org/whl/cu121
```

Then use GPU in script:
```bash
python3 scripts/tts_generate_human.py \
    --text "Test with GPU" \
    --emotion neutral \
    --output test.wav \
    --gpu  # Enable GPU
```

## Next Steps

Once TTS is working:

1. ✅ **Test the demo**: Run `CoquiTTSDemo` to verify everything works
2. ✅ **Regenerate samples**: Run `regenerate_samples.sh` for fresh audio
3. ✅ **Integrate**: Use `CoquiTTSAudioManager` in your game code
4. 📋 **Proceed to Phase 2**: See `docs/HUMAN_AUDIO_PLAN.md` for next steps

## Support

For issues:
1. Check this troubleshooting guide
2. See [PHASE1_IMPLEMENTATION.md](PHASE1_IMPLEMENTATION.md) for detailed docs
3. Check Coqui TTS docs: https://github.com/coqui-ai/TTS

## System Requirements Summary

| Component | Requirement |
|-----------|-------------|
| Python | 3.9, 3.10, or 3.11 |
| Java | 17+ |
| Disk Space | 1GB+ free |
| RAM | 2GB+ |
| GPU (optional) | NVIDIA with CUDA for faster generation |

Happy voice synthesis! 🎙️✨
