# Voice Sample Testing - Quick Start Guide

A quick guide to using the voice sample testing and validation system.

## Prerequisites

Install required dependencies:
```bash
pip install librosa soundfile scipy numpy
```

## Basic Usage

### 1. Analyze Existing Samples

Analyze current audio samples to extract metrics:

```bash
python3 scripts/test_voice_samples.py --mode analyze \
    --test-dir src/main/resources/audio \
    --report /tmp/voice_analysis.json
```

This will:
- Analyze all test samples defined in the script
- Extract pitch, tempo, and articulation metrics
- Generate a detailed JSON report

### 2. Generate Test Samples (Requires TTS)

Generate new test samples with TTS:

```bash
python3 scripts/test_voice_samples.py --mode generate \
    --test-dir /tmp/voice_test_samples
```

**Note**: Requires Coqui TTS library and compatible Python version (3.9-3.11).

### 3. Full Workflow

Generate samples, analyze, and compare:

```bash
python3 scripts/test_voice_samples.py --mode full \
    --baseline-dir /path/to/baseline/samples \
    --test-dir /tmp/voice_test_samples \
    --report /tmp/comparison_report.json
```

## Understanding the Output

### Console Output

The script provides real-time feedback:

```
============================================================
ANALYZING AND COMPARING SAMPLES
============================================================

Analyzing: test_greeting_friendly
  Baseline: greeting_welcome.wav
  Test: test_greeting_friendly.wav

...

============================================================
TARGET VALIDATION
============================================================

✓ PASS - Pitch Reduction
  Target: -8.0%
  Actual: -8.5%
  Notes: Lower pitch for mature voice quality

✓ PASS - Speaking Rate
  Target: -10.0% slower
  Actual: -12.3%
  Notes: Measured, deliberate pacing

✓ PASS - Articulation Clarity
  Target: +15.0%
  Actual: +14.7%
  Notes: Crisp, sharp enunciation

============================================================
OVERALL: ✓ ALL TARGETS MET
============================================================
```

### JSON Report

The detailed report includes:

- **samples**: Individual sample analysis
  - Baseline metrics (if available)
  - Test metrics
  - Percentage differences
  
- **summary**: Aggregated statistics
  - Average metrics across all samples
  - Average changes from baseline
  
- **validation**: Target validation
  - Pass/fail for each target metric
  - Notes and recommendations

## Test Sample Coverage

The testing script includes 8 diverse samples:

| Sample | Emotion | Text |
|--------|---------|------|
| test_greeting_friendly | friendly | "Hello there! I'm here to help..." |
| test_hostile_01 | hostile | "Who the hell are you? Where is my dad?" |
| test_curious_01 | curious | "What does this do? I've never seen..." |
| test_cooperative_01 | friendly | "Let's work together on this one." |
| test_alert_oxygen | urgent | "Warning! Oxygen levels are getting low!" |
| test_contemplative_01 | contemplative | "The stars sure are beautiful today..." |
| test_success | excited | "Great job! We did it!" |
| test_curious_03 | contemplative | "I'm starting to see things differently..." |

## Metrics Explained

### Pitch (Hz)
- **What**: Fundamental frequency of voice
- **Target**: ~8% lower than baseline (mature quality)
- **Typical Female Range**: 180-250 Hz
- **Current Samples**: ~245 Hz (lower-medium)

### Tempo (BPM)
- **What**: Speaking rate via onset detection
- **Target**: ~10% slower (measured delivery)
- **Interpretation**: Lower BPM = slower, more deliberate speech

### Spectral Centroid (Hz)
- **What**: Center of mass of frequency spectrum
- **Target**: ~15% higher (crisp articulation)
- **Interpretation**: Higher = sharper, clearer consonants
- **Current Samples**: ~3377 Hz (high clarity)

## Customization

### Add Custom Test Samples

Edit `scripts/test_voice_samples.py` and add to `TEST_SAMPLES`:

```python
{
    'text': "Your custom text here.",
    'emotion': 'cooperative',  # Must match EMOTION_PROFILES
    'category': 'dialogue',
    'name': 'test_custom_sample'
}
```

### Adjust Target Metrics

Modify `TARGET_METRICS` in the script:

```python
TARGET_METRICS = {
    'base_pitch_reduction': 0.08,  # 8% lower
    'rate_reduction': 0.10,        # 10% slower
    'pause_increase': 0.25,        # 25% longer pauses
    'articulation_increase': 0.15, # 15% clearer
}
```

### Change Validation Thresholds

Update `_validate_against_targets()` method to adjust tolerance ranges.

## Troubleshooting

### Issue: "Audio analysis libraries not available"

**Solution**: Install dependencies
```bash
pip install librosa soundfile scipy numpy
```

### Issue: "TTS library not available"

**Solution**: TTS generation requires compatible Python version
```bash
# Python 3.9-3.11 required
pip install TTS
```

**Alternative**: Use `--mode analyze` to skip generation and analyze existing samples.

### Issue: "Baseline file not found"

**Solution**: The script looks for baseline files by emotion. Either:
1. Ensure baseline directory structure matches expected format
2. Use without baseline comparison (metrics will compare against specification only)
3. Organize baseline files in subdirectories by category (greetings/, dialogue/, notifications/)

## Advanced Usage

### Compare Specific Directories

```bash
python3 scripts/test_voice_samples.py --mode analyze \
    --baseline-dir backups/2026-01-15_samples \
    --test-dir backups/2026-01-18_samples \
    --report comparison_by_date.json
```

### Batch Analysis

Create a shell script to analyze multiple sample sets:

```bash
#!/bin/bash
for dir in sample_sets/*; do
    python3 scripts/test_voice_samples.py --mode analyze \
        --test-dir "$dir" \
        --report "reports/$(basename $dir)_report.json"
done
```

## Related Documentation

- [VOICE_TESTING_RESULTS.md](VOICE_TESTING_RESULTS.md) - Detailed testing results
- [VOICE_MODEL_TARGET.md](VOICE_MODEL_TARGET.md) - Target specifications
- [HUMAN_AUDIO_PLAN.md](HUMAN_AUDIO_PLAN.md) - Overall audio strategy

## Support

For issues or questions:
1. Review [VOICE_TESTING_RESULTS.md](VOICE_TESTING_RESULTS.md) for detailed information
2. Check the script's help: `python3 scripts/test_voice_samples.py --help`
3. Open an issue in the repository with:
   - Python version
   - Installed library versions
   - Full error message
   - Command used

---

**Quick Reference**: Use `--mode analyze` to validate existing samples without TTS generation.
