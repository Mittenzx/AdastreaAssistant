# Audio Regeneration Solution Architecture

## Problem Encountered

```
┌─────────────────────────────────────────────────────────────┐
│  Initial Approach: Direct CI Regeneration                  │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  CI Environment (Python 3.12)                               │
│       ↓                                                     │
│  Docker Container (Python 3.11)                             │
│       ↓                                                     │
│  Try to download TTS models                                 │
│       ↓                                                     │
│  ❌ BLOCKED: Network restriction                            │
│       - Cannot access coqui.gateway.scarf.sh               │
│       - Docker containers isolated from external network   │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

## Solution Implemented

```
┌─────────────────────────────────────────────────────────────┐
│  GitHub Actions Workflow: Automated Regeneration            │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  1. Maintainer triggers workflow from Actions tab          │
│       ↓                                                     │
│  2. GitHub Actions runner (Ubuntu)                          │
│       - Python 3.11 setup                                   │
│       - ✅ Full network access                              │
│       ↓                                                     │
│  3. Install TTS dependencies                                │
│       - pip install TTS librosa soundfile scipy            │
│       - Download TTS models from internet                  │
│       ↓                                                     │
│  4. Backup current audio files                              │
│       - Store as workflow artifact (30 days)               │
│       ↓                                                     │
│  5. Run regeneration script                                 │
│       - ./scripts/regenerate_samples.sh                     │
│       - Generate all 34 audio files                         │
│       ↓                                                     │
│  6. Validate generated files                                │
│       - Check file count                                    │
│       - Verify file sizes                                   │
│       ↓                                                     │
│  7. Commit and push changes                                 │
│       - Automated git commit                                │
│       - Push to repository                                  │
│       ↓                                                     │
│  ✅ Complete: Audio files regenerated!                      │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

## Workflow Trigger Process

```
GitHub Repository
    ↓
Actions Tab
    ↓
"Regenerate Audio Files" Workflow
    ↓
[Run workflow] Button
    ↓
┌─────────────────────────────────────┐
│ Optional: Customize commit message  │
└─────────────────────────────────────┘
    ↓
[Run workflow] Confirm
    ↓
Workflow Execution (~5-10 minutes)
    ↓
Summary Report
    ↓
✅ Changes Committed
```

## File Generation Flow

```
scripts/regenerate_samples.sh
    ↓
For each audio category:
    ↓
┌──────────────────────────────────────────┐
│ Greetings (6 files)                      │
│ ├─ greeting_initial.wav                  │
│ ├─ greeting_startup.wav                  │
│ ├─ greeting_welcome.wav                  │
│ ├─ hello.wav                             │
│ ├─ startup.wav                           │
│ └─ welcome.wav                           │
└──────────────────────────────────────────┘
    ↓
┌──────────────────────────────────────────┐
│ Dialogue - Hostile (3 files)             │
│ ├─ mittenz_hostile_01.wav                │
│ ├─ mittenz_hostile_02.wav                │
│ └─ mittenz_hostile_03.wav                │
└──────────────────────────────────────────┘
    ↓
┌──────────────────────────────────────────┐
│ Dialogue - Curious (3 files)             │
│ ├─ mittenz_curious_01.wav                │
│ ├─ mittenz_curious_02.wav                │
│ └─ mittenz_curious_03.wav                │
└──────────────────────────────────────────┘
    ↓
┌──────────────────────────────────────────┐
│ Dialogue - Cooperative (3 files)         │
│ ├─ mittenz_cooperative_01.wav            │
│ ├─ mittenz_cooperative_02.wav            │
│ └─ mittenz_cooperative_03.wav            │
└──────────────────────────────────────────┘
    ↓
┌──────────────────────────────────────────┐
│ Companion Messages (4 files)             │
│ ├─ acknowledgment.wav                    │
│ ├─ companion_message_01.wav              │
│ ├─ companion_message_02.wav              │
│ └─ companion_message.wav                 │
└──────────────────────────────────────────┘
    ↓
┌──────────────────────────────────────────┐
│ Notifications (4 files)                  │
│ ├─ alert_oxygen.wav                      │
│ ├─ reminder_01.wav                       │
│ ├─ success.wav                           │
│ └─ teaching_intro.wav                    │
└──────────────────────────────────────────┘
    ↓
┌──────────────────────────────────────────┐
│ Sound Effects (5 files)                  │
│ ├─ button_click.wav                      │
│ ├─ error.wav                             │
│ ├─ interaction.wav                       │
│ ├─ menu_close.wav                        │
│ └─ menu_open.wav                         │
└──────────────────────────────────────────┘
    ↓
Total: 34 WAV files
    ↓
Format: PCM, 16-bit, mono, 44.1kHz
Voice: Hitagi-inspired characteristics
```

## Voice Processing Pipeline

```
Text Input
    ↓
┌────────────────────────────────────────┐
│ Prosody Controller                     │
│ - Add strategic pauses (+20-30%)       │
│ - Slow speaking rate (-10-15%)         │
│ - Context-aware emphasis               │
└────────────────────────────────────────┘
    ↓
┌────────────────────────────────────────┐
│ Coqui TTS Engine                       │
│ - Tacotron2-DDC model                  │
│ - Emotion profile application          │
│ - Base speech generation               │
└────────────────────────────────────────┘
    ↓
┌────────────────────────────────────────┐
│ Hitagi Voice Characteristics           │
│ - Lower pitch (-8%)                    │
│ - Enhance consonants (3-6 kHz boost)   │
│ - Reduce breathiness (high-freq cut)   │
│ - Apply compression (controlled)       │
└────────────────────────────────────────┘
    ↓
┌────────────────────────────────────────┐
│ Human-like Enhancement                 │
│ - Insert breath sounds                 │
│ - Add micro-variations                 │
│ - Normalize audio levels               │
└────────────────────────────────────────┘
    ↓
WAV File Output
Format: 44.1kHz, 16-bit, mono, PCM
```

## Backup and Recovery

```
Before Regeneration:
    ↓
┌────────────────────────────────────────┐
│ Automatic Backup                       │
│ - Copy all audio files                 │
│ - Upload as workflow artifact          │
│ - Retention: 30 days                   │
└────────────────────────────────────────┘
    ↓
If Recovery Needed:
    ↓
┌────────────────────────────────────────┐
│ 1. Go to workflow run                  │
│ 2. Download "audio-backup" artifact    │
│ 3. Extract ZIP file                    │
│ 4. Replace files in repository         │
│ 5. Commit and push                     │
└────────────────────────────────────────┘
```

## Advantages of This Solution

```
✅ No Local Setup Required
   - No Python 3.11 installation needed
   - No dependency management
   - Works from any device with browser

✅ Automated Process
   - Backup happens automatically
   - Validation built-in
   - Auto-commit and push

✅ Network Access
   - Full internet access in GitHub Actions
   - Can download TTS models
   - No firewall restrictions

✅ Version Control
   - All changes tracked in git
   - Easy rollback if needed
   - Backup preserved as artifact

✅ Reproducible
   - Same environment every time
   - Consistent results
   - Documented process

✅ Safe
   - Backup before regeneration
   - Validation checks
   - Can be run on test branch first
```

## Usage Timeline

```
┌────────────────────────────────────────┐
│ One-Time Setup (Already Done)         │
│ - Create workflow file ✅               │
│ - Update documentation ✅               │
│ - Push to repository ✅                 │
└────────────────────────────────────────┘
    ↓
┌────────────────────────────────────────┐
│ Regular Usage (Anytime)                │
│ 1. Go to Actions tab                   │
│ 2. Select workflow                     │
│ 3. Click "Run workflow"                │
│ 4. Wait ~5-10 minutes                  │
│ 5. Review results                      │
└────────────────────────────────────────┘
    ↓
┌────────────────────────────────────────┐
│ Future Iterations (Optional)           │
│ - Adjust voice parameters              │
│ - Run workflow again                   │
│ - Compare with backup                  │
│ - Keep or revert                       │
└────────────────────────────────────────┘
```

---

**Last Updated**: January 19, 2026  
**Status**: Ready for use  
**See**: `REGENERATE_AUDIO_HOWTO.md` for step-by-step guide
