#!/bin/bash
#
# Batch Regenerate Audio Samples with Human-like TTS
#
# This script regenerates all Mittenz audio samples using the human-like TTS system
# with appropriate emotions for each dialogue type.
#
# Usage: ./scripts/regenerate_samples.sh
#

set -e  # Exit on error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
TTS_SCRIPT="scripts/tts_generate_human.py"
AUDIO_DIR="src/main/resources/audio"
MODEL="tts_models/en/ljspeech/tacotron2-DDC"

# Check if script exists
if [ ! -f "$TTS_SCRIPT" ]; then
    echo -e "${RED}ERROR: TTS script not found at $TTS_SCRIPT${NC}"
    exit 1
fi

# Check if TTS is installed
if ! python3 -c "from TTS.api import TTS" 2>/dev/null; then
    echo -e "${RED}ERROR: Coqui TTS not installed${NC}"
    echo -e "${YELLOW}Install with: pip install TTS librosa soundfile scipy${NC}"
    exit 1
fi

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}  Regenerating Audio Samples${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""

# Function to generate audio
generate_audio() {
    local text="$1"
    local emotion="$2"
    local output="$3"
    
    echo -e "${YELLOW}Generating:${NC} $(basename "$output")"
    echo -e "  Emotion: ${emotion}"
    echo -e "  Text: \"${text}\""
    
    python3 "$TTS_SCRIPT" \
        --text "$text" \
        --emotion "$emotion" \
        --output "$output" \
        --model "$MODEL" \
        2>&1 | grep -E "(SUCCESS|ERROR|WARNING)" || true
    
    # Verify that the output file exists and is not empty
    if [ -s "$output" ]; then
        echo -e "${GREEN}  ✓ Generated successfully${NC}"
    else
        echo -e "${RED}  ✗ Failed to generate (missing or empty output file)${NC}"
        return 1
    fi
    echo ""
}

# Create backup of original files
echo -e "${YELLOW}Creating backup of original files...${NC}"
if [ -d "$AUDIO_DIR" ]; then
    BACKUP_DIR="${AUDIO_DIR}_backup_$(date +%Y%m%d_%H%M%S)"
    cp -r "$AUDIO_DIR" "$BACKUP_DIR"
    echo -e "${GREEN}✓ Backup created: $BACKUP_DIR${NC}"
else
    echo -e "${YELLOW}Warning: Audio directory '$AUDIO_DIR' does not exist. Skipping backup and creating directory.${NC}"
    mkdir -p "$AUDIO_DIR"
fi
echo ""

# ============================================================================
# GREETINGS
# ============================================================================

echo -e "${BLUE}=== Generating Greetings ===${NC}"
echo ""

generate_audio \
    "Hello there! I'm here to help you on your space adventure." \
    "friendly" \
    "${AUDIO_DIR}/greetings/greeting_initial.wav"

generate_audio \
    "System initializing.  Mittenz here, ready to assist." \
    "neutral" \
    "${AUDIO_DIR}/greetings/greeting_startup.wav"

generate_audio \
    "Welcome back!  Let's continue our journey through the stars." \
    "cooperative" \
    "${AUDIO_DIR}/greetings/greeting_welcome.wav"

# ============================================================================
# DIALOGUE - HOSTILE STAGE
# ============================================================================

echo -e "${BLUE}=== Generating Hostile Stage Dialogue ===${NC}"
echo ""

generate_audio \
    "Who the hell are you?  Where is my dad?" \
    "hostile" \
    "${AUDIO_DIR}/dialogue/mittenz_hostile_01.wav"

generate_audio \
    "You can't do this to me!  Do you know who I am?" \
    "angry" \
    "${AUDIO_DIR}/dialogue/mittenz_hostile_02.wav"

generate_audio \
    "I demand you tell me what's going on right now!" \
    "hostile" \
    "${AUDIO_DIR}/dialogue/mittenz_hostile_03.wav"

# ============================================================================
# DIALOGUE - CURIOUS STAGE
# ============================================================================

echo -e "${BLUE}=== Generating Curious Stage Dialogue ===${NC}"
echo ""

generate_audio \
    "What does this do?  I've never seen anything like this before." \
    "curious" \
    "${AUDIO_DIR}/dialogue/mittenz_curious_01.wav"

generate_audio \
    "Wow, I can see all the ship's systems from here." \
    "fascinated" \
    "${AUDIO_DIR}/dialogue/mittenz_curious_02.wav"

generate_audio \
    "I'm starting to see things differently now." \
    "contemplative" \
    "${AUDIO_DIR}/dialogue/mittenz_curious_03.wav"

# ============================================================================
# DIALOGUE - COOPERATIVE STAGE
# ============================================================================

echo -e "${BLUE}=== Generating Cooperative Stage Dialogue ===${NC}"
echo ""

generate_audio \
    "We need to check the oxygen levels." \
    "cooperative" \
    "${AUDIO_DIR}/dialogue/mittenz_cooperative_01.wav"

generate_audio \
    "Let's work together on this one." \
    "friendly" \
    "${AUDIO_DIR}/dialogue/mittenz_cooperative_02.wav"

generate_audio \
    "We make a pretty good team, don't we?" \
    "friendly" \
    "${AUDIO_DIR}/dialogue/mittenz_cooperative_03.wav"

# ============================================================================
# COMPANION MESSAGES
# ============================================================================

echo -e "${BLUE}=== Generating Companion Messages ===${NC}"
echo ""

generate_audio \
    "The stars sure are beautiful today, aren't they?" \
    "contemplative" \
    "${AUDIO_DIR}/dialogue/companion_message_01.wav"

generate_audio \
    "I'm here if you need any guidance or just want to chat." \
    "friendly" \
    "${AUDIO_DIR}/dialogue/companion_message_02.wav"

generate_audio \
    "Understood.  I'll take care of that." \
    "cooperative" \
    "${AUDIO_DIR}/dialogue/acknowledgment.wav"

# ============================================================================
# NOTIFICATIONS
# ============================================================================

echo -e "${BLUE}=== Generating Notifications ===${NC}"
echo ""

generate_audio \
    "Reminder: It's time to check your fuel levels." \
    "neutral" \
    "${AUDIO_DIR}/notifications/reminder_01.wav"

generate_audio \
    "Warning!  Oxygen levels are getting low!" \
    "urgent" \
    "${AUDIO_DIR}/notifications/alert_oxygen.wav"

generate_audio \
    "Let me teach you about this system." \
    "cooperative" \
    "${AUDIO_DIR}/notifications/teaching_intro.wav"

generate_audio \
    "Great job!  We did it!" \
    "excited" \
    "${AUDIO_DIR}/notifications/success.wav"

# ============================================================================
# SUMMARY
# ============================================================================

echo -e "${BLUE}========================================${NC}"
echo -e "${GREEN}✓ Audio sample regeneration complete!${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""
echo -e "Generated files:"
echo -e "  Greetings: 3 files"
echo -e "  Hostile dialogue: 3 files"
echo -e "  Curious dialogue: 3 files"
echo -e "  Cooperative dialogue: 3 files"
echo -e "  Companion messages: 3 files"
echo -e "  Notifications: 4 files"
echo -e ""
echo -e "  ${GREEN}Total: 19 voice samples${NC}"
echo ""
echo -e "Backup location: ${BACKUP_DIR}"
echo ""
echo -e "${YELLOW}Next steps:${NC}"
echo -e "  1. Test the new audio files"
echo -e "  2. Compare with the backup versions"
echo -e "  3. Integrate into your game"
echo -e "  4. Gather user feedback"
echo ""
echo -e "${BLUE}Happy testing! 🎙️✨${NC}"
