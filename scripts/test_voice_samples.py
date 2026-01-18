#!/usr/bin/env python3
"""
Voice Sample Testing Script for AdastreaAssistant

This script generates test samples with the Hitagi-inspired voice profile and
validates that they meet the target metrics specified in VOICE_MODEL_TARGET.md.

Usage:
    python3 test_voice_samples.py --mode generate  # Generate test samples
    python3 test_voice_samples.py --mode analyze   # Analyze and compare samples
    python3 test_voice_samples.py --mode full      # Generate, analyze, and report

Features:
    - Generates test samples for all emotion profiles
    - Extracts audio metrics (pitch, rate, articulation)
    - Compares test samples with current/baseline samples
    - Validates against target specifications
    - Generates detailed comparison report
"""

import argparse
import sys
import json
import os
import subprocess
from pathlib import Path
from datetime import datetime

# Check for required libraries
try:
    import librosa
    import numpy as np
    import soundfile as sf
    AUDIO_ANALYSIS_AVAILABLE = True
except ImportError:
    AUDIO_ANALYSIS_AVAILABLE = False
    print("WARNING: librosa/soundfile not installed. Install with: pip install librosa soundfile numpy", file=sys.stderr)


# ============================================================================
# TEST CONFIGURATION
# ============================================================================

# Target metrics from VOICE_MODEL_TARGET.md
TARGET_METRICS = {
    'base_pitch_reduction': 0.08,  # 8% lower than baseline
    'rate_reduction': 0.10,        # 10% slower (0.90x)
    'pause_increase': 0.25,        # 25% longer pauses
    'articulation_increase': 0.15, # 15% clearer articulation
}

# Test samples to generate (subset of full sample set)
TEST_SAMPLES = [
    {
        'text': "Hello there! I'm here to help you on your space adventure.",
        'emotion': 'friendly',
        'category': 'greeting',
        'name': 'test_greeting_friendly'
    },
    {
        'text': "Who the hell are you? Where is my dad?",
        'emotion': 'hostile',
        'category': 'dialogue',
        'name': 'test_hostile_01'
    },
    {
        'text': "What does this do? I've never seen anything like this before.",
        'emotion': 'curious',
        'category': 'dialogue',
        'name': 'test_curious_01'
    },
    {
        'text': "Let's work together on this one.",
        'emotion': 'friendly',
        'category': 'dialogue',
        'name': 'test_cooperative_01'
    },
    {
        'text': "Warning! Oxygen levels are getting low!",
        'emotion': 'urgent',
        'category': 'notification',
        'name': 'test_alert_oxygen'
    },
    {
        'text': "The stars sure are beautiful today, aren't they?",
        'emotion': 'contemplative',
        'category': 'dialogue',
        'name': 'test_contemplative_01'
    },
    {
        'text': "Great job! We did it!",
        'emotion': 'excited',
        'category': 'notification',
        'name': 'test_success'
    },
    {
        'text': "I'm starting to see things differently now.",
        'emotion': 'contemplative',
        'category': 'dialogue',
        'name': 'test_curious_03'
    },
]


# ============================================================================
# AUDIO ANALYSIS FUNCTIONS
# ============================================================================

class AudioAnalyzer:
    """Analyzes audio files to extract voice metrics."""
    
    def __init__(self):
        if not AUDIO_ANALYSIS_AVAILABLE:
            raise RuntimeError("Audio analysis libraries not available. Install with: pip install librosa soundfile numpy")
    
    def analyze_audio_file(self, audio_path):
        """
        Analyze an audio file and extract key metrics.
        
        Returns:
            dict: Metrics including pitch, rate, duration, articulation
        """
        if not os.path.exists(audio_path):
            return None
        
        # Load audio
        y, sr = librosa.load(audio_path, sr=None)
        
        # Extract metrics
        metrics = {}
        
        # 1. Pitch analysis
        pitch_data = self._analyze_pitch(y, sr)
        metrics.update(pitch_data)
        
        # 2. Speaking rate (tempo)
        rate_data = self._analyze_rate(y, sr)
        metrics.update(rate_data)
        
        # 3. Duration
        metrics['duration'] = len(y) / sr
        
        # 4. Articulation clarity (spectral features)
        articulation_data = self._analyze_articulation(y, sr)
        metrics.update(articulation_data)
        
        # 5. Energy/dynamics
        energy_data = self._analyze_energy(y)
        metrics.update(energy_data)
        
        return metrics
    
    def _analyze_pitch(self, y, sr):
        """Extract pitch statistics."""
        # Use librosa's piptrack for pitch detection
        pitches, magnitudes = librosa.piptrack(y=y, sr=sr, fmin=80, fmax=400)
        
        # Extract pitch values (only where magnitude is significant)
        pitch_values = []
        for t in range(pitches.shape[1]):
            index = magnitudes[:, t].argmax()
            pitch = pitches[index, t]
            if pitch > 0 and magnitudes[index, t] > 0.1:  # Threshold for voiced segments
                pitch_values.append(pitch)
        
        if len(pitch_values) == 0:
            return {
                'pitch_mean': 0,
                'pitch_std': 0,
                'pitch_min': 0,
                'pitch_max': 0
            }
        
        return {
            'pitch_mean': float(np.mean(pitch_values)),
            'pitch_std': float(np.std(pitch_values)),
            'pitch_min': float(np.min(pitch_values)),
            'pitch_max': float(np.max(pitch_values)),
        }
    
    def _analyze_rate(self, y, sr):
        """Analyze speaking rate via tempo detection."""
        # Use onset detection to estimate syllable rate
        onset_env = librosa.onset.onset_strength(y=y, sr=sr)
        try:
            # Try new API first (librosa >= 0.10.0)
            tempo = librosa.feature.rhythm.tempo(onset_envelope=onset_env, sr=sr)[0]
        except AttributeError:
            # Fall back to old API
            tempo = librosa.beat.tempo(onset_envelope=onset_env, sr=sr)[0]
        
        # Also calculate zero-crossing rate as a proxy for articulation speed
        zcr = librosa.feature.zero_crossing_rate(y)[0]
        
        return {
            'tempo_bpm': float(tempo),
            'zcr_mean': float(np.mean(zcr)),
        }
    
    def _analyze_articulation(self, y, sr):
        """Analyze articulation clarity via spectral features."""
        # Spectral centroid - higher values indicate more high-frequency content (crisp articulation)
        spectral_centroid = librosa.feature.spectral_centroid(y=y, sr=sr)[0]
        
        # Spectral rolloff - frequency below which 85% of energy is contained
        spectral_rolloff = librosa.feature.spectral_rolloff(y=y, sr=sr, roll_percent=0.85)[0]
        
        # Spectral bandwidth - width of frequency distribution
        spectral_bandwidth = librosa.feature.spectral_bandwidth(y=y, sr=sr)[0]
        
        return {
            'spectral_centroid_mean': float(np.mean(spectral_centroid)),
            'spectral_centroid_std': float(np.std(spectral_centroid)),
            'spectral_rolloff_mean': float(np.mean(spectral_rolloff)),
            'spectral_bandwidth_mean': float(np.mean(spectral_bandwidth)),
        }
    
    def _analyze_energy(self, y):
        """Analyze energy/dynamics."""
        # RMS energy
        rms = librosa.feature.rms(y=y)[0]
        
        # Dynamic range
        dynamic_range = float(np.max(np.abs(y))) - float(np.min(np.abs(y)))
        
        return {
            'rms_mean': float(np.mean(rms)),
            'rms_std': float(np.std(rms)),
            'dynamic_range': dynamic_range,
        }


# ============================================================================
# TEST SAMPLE GENERATION
# ============================================================================

class TestSampleGenerator:
    """Generates test samples using the TTS system."""
    
    def __init__(self, tts_script_path, output_dir):
        self.tts_script = tts_script_path
        self.output_dir = Path(output_dir)
        self.output_dir.mkdir(parents=True, exist_ok=True)
    
    def generate_test_sample(self, text, emotion, output_name):
        """
        Generate a single test sample.
        
        Args:
            text: Text to synthesize (will be passed as string argument)
            emotion: Emotion profile name (validated against known emotions)
            output_name: Output filename without extension
            
        Returns:
            bool: True if generation successful, False otherwise
            
        Note:
            Uses subprocess.run with list arguments (not shell=True) which
            automatically handles argument escaping, preventing command injection.
        """
        output_path = self.output_dir / f"{output_name}.wav"
        
        print(f"Generating: {output_name} (emotion: {emotion})")
        
        # Run TTS generation script with properly escaped arguments
        # Using list form of subprocess.run automatically handles argument escaping
        # and prevents command injection since shell=False (default)
        cmd = [
            'python3',
            str(self.tts_script),
            '--text', str(text),  # Convert to string for safety
            '--emotion', str(emotion),  # Convert to string for safety
            '--output', str(output_path)
        ]
        
        try:
            result = subprocess.run(cmd, capture_output=True, text=True, timeout=60)
            
            if result.returncode == 0 and output_path.exists():
                print(f"  ✓ Generated: {output_path}")
                return True
            else:
                print(f"  ✗ Failed to generate: {output_name}")
                if result.stderr:
                    print(f"    Error: {result.stderr[:200]}")
                return False
        except subprocess.TimeoutExpired:
            print(f"  ✗ Timeout generating: {output_name}")
            return False
        except Exception as e:
            print(f"  ✗ Error generating {output_name}: {e}")
            return False
    
    def generate_all_test_samples(self, samples):
        """Generate all test samples."""
        print("\n" + "="*60)
        print("GENERATING TEST SAMPLES")
        print("="*60 + "\n")
        
        success_count = 0
        for sample in samples:
            if self.generate_test_sample(sample['text'], sample['emotion'], sample['name']):
                success_count += 1
        
        print(f"\n✓ Generated {success_count}/{len(samples)} test samples")
        return success_count


# ============================================================================
# COMPARISON & REPORTING
# ============================================================================

class VoiceProfileValidator:
    """Validates test samples against target metrics."""
    
    def __init__(self, analyzer):
        self.analyzer = analyzer
    
    def compare_samples(self, baseline_dir, test_dir, samples):
        """
        Compare baseline and test samples.
        
        Returns:
            dict: Comparison results with metrics for each sample pair
        """
        print("\n" + "="*60)
        print("ANALYZING AND COMPARING SAMPLES")
        print("="*60 + "\n")
        
        results = {
            'samples': [],
            'summary': {},
            'validation': {},
            'timestamp': datetime.now().isoformat()
        }
        
        # Analyze each sample pair
        for sample in samples:
            print(f"Analyzing: {sample['name']}")
            
            # Find baseline file (may have different naming)
            baseline_path = self._find_baseline_file(baseline_dir, sample)
            test_path = Path(test_dir) / f"{sample['name']}.wav"
            
            baseline_metrics = None
            if baseline_path and baseline_path.exists():
                baseline_metrics = self.analyzer.analyze_audio_file(str(baseline_path))
                print(f"  Baseline: {baseline_path.name}")
            else:
                print(f"  Baseline: Not found (will compare against specification only)")
            
            test_metrics = None
            if test_path.exists():
                test_metrics = self.analyzer.analyze_audio_file(str(test_path))
                print(f"  Test: {test_path.name}")
            else:
                print(f"  Test: Not found - skipping")
                continue
            
            # Compare metrics
            comparison = {
                'name': sample['name'],
                'emotion': sample['emotion'],
                'category': sample['category'],
                'baseline_metrics': baseline_metrics,
                'test_metrics': test_metrics,
                'differences': self._calculate_differences(baseline_metrics, test_metrics) if baseline_metrics else None
            }
            
            results['samples'].append(comparison)
        
        # Calculate summary statistics
        results['summary'] = self._calculate_summary(results['samples'])
        
        # Validate against target metrics
        results['validation'] = self._validate_against_targets(results['summary'])
        
        return results
    
    def _find_baseline_file(self, baseline_dir, sample):
        """Find corresponding baseline file (may have different naming)."""
        baseline_dir = Path(baseline_dir)
        
        # Try direct match
        direct_match = baseline_dir / sample['category'] / f"{sample['name']}.wav"
        if direct_match.exists():
            return direct_match
        
        # Try matching by emotion (e.g., mittenz_friendly_01.wav)
        emotion_pattern = f"*{sample['emotion']}*.wav"
        matches = list(baseline_dir.glob(f"**/{emotion_pattern}"))
        if matches:
            return matches[0]
        
        return None
    
    def _calculate_differences(self, baseline, test):
        """Calculate differences between baseline and test metrics."""
        if not baseline or not test:
            return None
        
        differences = {}
        
        # Pitch comparison
        if baseline['pitch_mean'] > 0 and test['pitch_mean'] > 0:
            differences['pitch_change_percent'] = ((test['pitch_mean'] - baseline['pitch_mean']) / baseline['pitch_mean']) * 100
        
        # Rate comparison (tempo)
        if baseline['tempo_bpm'] > 0 and test['tempo_bpm'] > 0:
            differences['tempo_change_percent'] = ((test['tempo_bpm'] - baseline['tempo_bpm']) / baseline['tempo_bpm']) * 100
        
        # Articulation comparison (spectral centroid)
        if baseline['spectral_centroid_mean'] > 0 and test['spectral_centroid_mean'] > 0:
            differences['articulation_change_percent'] = ((test['spectral_centroid_mean'] - baseline['spectral_centroid_mean']) / baseline['spectral_centroid_mean']) * 100
        
        # Duration comparison
        differences['duration_change_percent'] = ((test['duration'] - baseline['duration']) / baseline['duration']) * 100
        
        return differences
    
    def _calculate_summary(self, samples):
        """Calculate summary statistics across all samples."""
        summary = {
            'total_samples': len(samples),
            'samples_with_baseline': sum(1 for s in samples if s['baseline_metrics']),
            'average_metrics': {}
        }
        
        # Collect metrics from test samples
        test_metrics_list = [s['test_metrics'] for s in samples if s['test_metrics']]
        
        if test_metrics_list:
            # Average pitch
            pitches = [m['pitch_mean'] for m in test_metrics_list if m['pitch_mean'] > 0]
            if pitches:
                summary['average_metrics']['pitch_mean'] = np.mean(pitches)
                summary['average_metrics']['pitch_std'] = np.std(pitches)
            
            # Average tempo
            tempos = [m['tempo_bpm'] for m in test_metrics_list if m['tempo_bpm'] > 0]
            if tempos:
                summary['average_metrics']['tempo_mean'] = np.mean(tempos)
            
            # Average articulation
            centroids = [m['spectral_centroid_mean'] for m in test_metrics_list]
            if centroids:
                summary['average_metrics']['spectral_centroid_mean'] = np.mean(centroids)
        
        # Calculate average differences (where baseline exists)
        samples_with_diffs = [s for s in samples if s['differences']]
        if samples_with_diffs:
            pitch_changes = [s['differences'].get('pitch_change_percent', 0) for s in samples_with_diffs if 'pitch_change_percent' in s['differences']]
            tempo_changes = [s['differences'].get('tempo_change_percent', 0) for s in samples_with_diffs if 'tempo_change_percent' in s['differences']]
            articulation_changes = [s['differences'].get('articulation_change_percent', 0) for s in samples_with_diffs if 'articulation_change_percent' in s['differences']]
            
            if pitch_changes:
                summary['average_changes'] = {
                    'pitch_change_percent': np.mean(pitch_changes),
                    'tempo_change_percent': np.mean(tempo_changes) if tempo_changes else 0,
                    'articulation_change_percent': np.mean(articulation_changes) if articulation_changes else 0,
                }
        
        return summary
    
    def _validate_against_targets(self, summary):
        """Validate metrics against target specifications."""
        validation = {
            'meets_targets': True,
            'checks': []
        }
        
        avg_changes = summary.get('average_changes', {})
        
        # Check pitch reduction (target: -8%)
        if 'pitch_change_percent' in avg_changes:
            pitch_change = avg_changes['pitch_change_percent']
            target_pitch = -TARGET_METRICS['base_pitch_reduction'] * 100  # -8%
            
            # Allow ±3% tolerance
            meets_pitch = -11 <= pitch_change <= -5
            validation['checks'].append({
                'metric': 'Pitch Reduction',
                'target': f"{target_pitch:.1f}%",
                'actual': f"{pitch_change:.1f}%",
                'meets_target': meets_pitch,
                'notes': 'Lower pitch for mature voice quality'
            })
            
            if not meets_pitch:
                validation['meets_targets'] = False
        
        # Check rate reduction (target: -10% = slower)
        if 'tempo_change_percent' in avg_changes:
            tempo_change = avg_changes['tempo_change_percent']
            target_tempo = -TARGET_METRICS['rate_reduction'] * 100  # -10%
            
            # Tempo is inverse of rate, so negative tempo change = slower speech (good)
            # Allow ±5% tolerance
            meets_rate = -15 <= tempo_change <= -5
            validation['checks'].append({
                'metric': 'Speaking Rate',
                'target': f"{target_tempo:.1f}% slower",
                'actual': f"{tempo_change:.1f}%",
                'meets_target': meets_rate,
                'notes': 'Measured, deliberate pacing'
            })
            
            if not meets_rate:
                validation['meets_targets'] = False
        
        # Check articulation increase (target: +15%)
        if 'articulation_change_percent' in avg_changes:
            articulation_change = avg_changes['articulation_change_percent']
            target_articulation = TARGET_METRICS['articulation_increase'] * 100  # +15%
            
            # Allow ±10% tolerance (articulation is harder to measure precisely)
            meets_articulation = 5 <= articulation_change <= 25
            validation['checks'].append({
                'metric': 'Articulation Clarity',
                'target': f"+{target_articulation:.1f}%",
                'actual': f"{articulation_change:.+.1f}%",
                'meets_target': meets_articulation,
                'notes': 'Crisp, sharp enunciation'
            })
            
            if not meets_articulation:
                validation['meets_targets'] = False
        
        return validation
    
    def generate_report(self, results, output_path):
        """Generate a detailed comparison report."""
        print("\n" + "="*60)
        print("VOICE PROFILE VALIDATION REPORT")
        print("="*60 + "\n")
        
        # Summary
        summary = results['summary']
        print(f"Total Samples Analyzed: {summary['total_samples']}")
        print(f"Samples with Baseline: {summary['samples_with_baseline']}")
        print()
        
        # Average metrics
        if 'average_metrics' in summary:
            print("Average Test Sample Metrics:")
            avg_metrics = summary['average_metrics']
            if 'pitch_mean' in avg_metrics:
                print(f"  Mean Pitch: {avg_metrics['pitch_mean']:.1f} Hz")
            if 'tempo_mean' in avg_metrics:
                print(f"  Tempo: {avg_metrics['tempo_mean']:.1f} BPM")
            if 'spectral_centroid_mean' in avg_metrics:
                print(f"  Spectral Centroid: {avg_metrics['spectral_centroid_mean']:.1f} Hz")
            print()
        
        # Changes from baseline
        if 'average_changes' in summary:
            print("Average Changes from Baseline:")
            changes = summary['average_changes']
            print(f"  Pitch: {changes['pitch_change_percent']:+.1f}%")
            print(f"  Tempo: {changes['tempo_change_percent']:+.1f}%")
            print(f"  Articulation: {changes['articulation_change_percent']:+.1f}%")
            print()
        
        # Validation results
        validation = results['validation']
        print("="*60)
        print("TARGET VALIDATION")
        print("="*60 + "\n")
        
        for check in validation['checks']:
            status = "✓ PASS" if check['meets_target'] else "✗ FAIL"
            print(f"{status} - {check['metric']}")
            print(f"  Target: {check['target']}")
            print(f"  Actual: {check['actual']}")
            print(f"  Notes: {check['notes']}")
            print()
        
        overall_status = "✓ ALL TARGETS MET" if validation['meets_targets'] else "⚠ SOME TARGETS NOT MET"
        print("="*60)
        print(f"OVERALL: {overall_status}")
        print("="*60 + "\n")
        
        # Save detailed report to JSON
        output_path = Path(output_path)
        output_path.parent.mkdir(parents=True, exist_ok=True)
        
        with open(output_path, 'w') as f:
            json.dump(results, f, indent=2)
        
        print(f"Detailed report saved to: {output_path}")
        print()
        
        return validation['meets_targets']


# ============================================================================
# MAIN FUNCTION
# ============================================================================

def main():
    """Main function for command-line usage."""
    
    parser = argparse.ArgumentParser(
        description='Test and validate voice samples for Hitagi-inspired voice profile',
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog="""
Examples:
  # Generate test samples only
  python3 test_voice_samples.py --mode generate
  
  # Analyze existing test samples
  python3 test_voice_samples.py --mode analyze
  
  # Full workflow: generate, analyze, and report
  python3 test_voice_samples.py --mode full
  
  # Specify custom directories
  python3 test_voice_samples.py --mode full --baseline-dir path/to/baseline --test-dir path/to/test
"""
    )
    
    parser.add_argument('--mode', type=str, required=True,
                       choices=['generate', 'analyze', 'full'],
                       help='Operation mode: generate samples, analyze existing, or do both')
    parser.add_argument('--tts-script', type=str,
                       default='scripts/tts_generate_human.py',
                       help='Path to TTS generation script (default: scripts/tts_generate_human.py)')
    parser.add_argument('--baseline-dir', type=str,
                       default='src/main/resources/audio',
                       help='Directory containing baseline audio samples (default: src/main/resources/audio)')
    parser.add_argument('--test-dir', type=str,
                       default='/tmp/voice_test_samples',
                       help='Directory for test samples (default: /tmp/voice_test_samples)')
    parser.add_argument('--report', type=str,
                       default='/tmp/voice_test_report.json',
                       help='Path for test report (default: /tmp/voice_test_report.json)')
    
    args = parser.parse_args()
    
    # Check dependencies
    if not AUDIO_ANALYSIS_AVAILABLE:
        print("ERROR: Required audio analysis libraries not installed", file=sys.stderr)
        print("Install with: pip install librosa soundfile numpy", file=sys.stderr)
        return 1
    
    # Validate TTS script exists
    if args.mode in ['generate', 'full']:
        if not os.path.exists(args.tts_script):
            print(f"ERROR: TTS script not found: {args.tts_script}", file=sys.stderr)
            return 1
    
    # Generate samples
    if args.mode in ['generate', 'full']:
        generator = TestSampleGenerator(args.tts_script, args.test_dir)
        success_count = generator.generate_all_test_samples(TEST_SAMPLES)
        
        if success_count == 0:
            print("ERROR: Failed to generate any test samples", file=sys.stderr)
            return 1
    
    # Analyze samples
    if args.mode in ['analyze', 'full']:
        if not os.path.exists(args.test_dir):
            print(f"ERROR: Test directory not found: {args.test_dir}", file=sys.stderr)
            print("Run with --mode generate first", file=sys.stderr)
            return 1
        
        analyzer = AudioAnalyzer()
        validator = VoiceProfileValidator(analyzer)
        
        results = validator.compare_samples(args.baseline_dir, args.test_dir, TEST_SAMPLES)
        meets_targets = validator.generate_report(results, args.report)
        
        if not meets_targets:
            print("\n⚠ WARNING: Not all target metrics were met.")
            print("Review the report for details and consider adjusting voice parameters.\n")
            return 2  # Non-zero but different from error
    
    print("\n✓ Voice sample testing complete!\n")
    return 0


if __name__ == '__main__':
    sys.exit(main())
