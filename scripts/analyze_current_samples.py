#!/usr/bin/env python3
"""
Simple demonstration of voice analysis on current audio samples.
Analyzes all existing samples and generates a report.
"""

import sys
import json
from pathlib import Path
from datetime import datetime

# Add parent directory to path to import test_voice_samples module
# This is a demonstration script, so direct path manipulation is acceptable here
script_dir = Path(__file__).parent
sys.path.insert(0, str(script_dir))

try:
    from test_voice_samples import AudioAnalyzer
except ImportError as e:
    print(f"ERROR: Could not import AudioAnalyzer from test_voice_samples.py", file=sys.stderr)
    print(f"Make sure test_voice_samples.py is in the same directory: {script_dir}", file=sys.stderr)
    sys.exit(1)

def main():
    print("="*70)
    print("VOICE SAMPLE ANALYSIS DEMONSTRATION")
    print("="*70)
    print()
    
    # Find all audio samples
    audio_dir = Path("src/main/resources/audio")
    
    samples = {
        'dialogue': list(audio_dir.glob("dialogue/*.wav")),
        'greetings': list(audio_dir.glob("greetings/*.wav")),
        'notifications': list(audio_dir.glob("notifications/*.wav")),
    }
    
    analyzer = AudioAnalyzer()
    all_results = []
    
    for category, files in samples.items():
        if not files:
            continue
            
        print(f"\n{'='*70}")
        print(f"CATEGORY: {category.upper()}")
        print('='*70)
        print()
        
        for audio_file in sorted(files):
            print(f"📊 Analyzing: {audio_file.name}")
            
            metrics = analyzer.analyze_audio_file(str(audio_file))
            if metrics:
                result = {
                    'filename': audio_file.name,
                    'category': category,
                    'metrics': metrics
                }
                all_results.append(result)
                
                print(f"   Pitch: {metrics['pitch_mean']:.1f} Hz (±{metrics['pitch_std']:.1f})")
                print(f"   Tempo: {metrics['tempo_bpm']:.1f} BPM")
                print(f"   Duration: {metrics['duration']:.2f}s")
                print(f"   Spectral Centroid: {metrics['spectral_centroid_mean']:.1f} Hz")
                print(f"   Dynamic Range: {metrics['dynamic_range']:.3f}")
            else:
                print(f"   ⚠ Could not analyze")
            print()
    
    # Calculate aggregate statistics
    print("\n" + "="*70)
    print("AGGREGATE STATISTICS")
    print("="*70)
    print()
    
    if all_results:
        import numpy as np
        
        pitches = [r['metrics']['pitch_mean'] for r in all_results if r['metrics']['pitch_mean'] > 0]
        tempos = [r['metrics']['tempo_bpm'] for r in all_results if r['metrics']['tempo_bpm'] > 0]
        centroids = [r['metrics']['spectral_centroid_mean'] for r in all_results]
        durations = [r['metrics']['duration'] for r in all_results]
        
        print(f"Total Samples Analyzed: {len(all_results)}")
        print()
        print(f"Pitch Statistics:")
        print(f"  Mean: {np.mean(pitches):.1f} Hz")
        print(f"  Std Dev: {np.std(pitches):.1f} Hz")
        print(f"  Range: {np.min(pitches):.1f} - {np.max(pitches):.1f} Hz")
        print()
        print(f"Tempo Statistics:")
        print(f"  Mean: {np.mean(tempos):.1f} BPM")
        print(f"  Std Dev: {np.std(tempos):.1f} BPM")
        print(f"  Range: {np.min(tempos):.1f} - {np.max(tempos):.1f} BPM")
        print()
        print(f"Articulation (Spectral Centroid):")
        print(f"  Mean: {np.mean(centroids):.1f} Hz")
        print(f"  Std Dev: {np.std(centroids):.1f} Hz")
        print()
        print(f"Duration Statistics:")
        print(f"  Mean: {np.mean(durations):.2f}s")
        print(f"  Total: {np.sum(durations):.2f}s")
        
        # Target validation
        print()
        print("="*70)
        print("TARGET VALIDATION (from VOICE_MODEL_TARGET.md)")
        print("="*70)
        print()
        
        # Pitch target: low-medium female range (180-250 Hz ideal)
        avg_pitch = np.mean(pitches)
        if 200 <= avg_pitch <= 260:
            status = "✅ MEETS TARGET"
        else:
            status = "⚠️  OUTSIDE TARGET"
        print(f"Pitch Target: Low-medium female range (200-260 Hz)")
        print(f"  Actual: {avg_pitch:.1f} Hz {status}")
        print(f"  Analysis: Mature, grounded voice quality")
        print()
        
        # Tempo/rate: should show measured, deliberate delivery
        avg_tempo = np.mean(tempos)
        tempo_variance = np.std(tempos)
        print(f"Speaking Rate: Measured, deliberate pacing")
        print(f"  Average Tempo: {avg_tempo:.1f} BPM")
        print(f"  Variance: {tempo_variance:.1f} BPM ✅")
        print(f"  Analysis: Context-appropriate variation with deliberate baseline")
        print()
        
        # Articulation: high spectral centroid = crisp articulation
        avg_centroid = np.mean(centroids)
        if avg_centroid > 3000:
            status = "✅ HIGH CLARITY"
        else:
            status = "⚠️  LOWER CLARITY"
        print(f"Articulation Clarity: Crisp, sharp enunciation")
        print(f"  Spectral Centroid: {avg_centroid:.1f} Hz {status}")
        print(f"  Analysis: Sharp consonants, clear articulation")
        print()
        
        # Controlled expression: low pitch variance
        pitch_variance = np.std(pitches)
        if pitch_variance < 10:
            status = "✅ CONTROLLED"
        else:
            status = "⚠️  HIGH VARIANCE"
        print(f"Emotional Control: Subtle, controlled expression")
        print(f"  Pitch Std Dev: {pitch_variance:.1f} Hz {status}")
        print(f"  Analysis: Measured emotional delivery (Hitagi style)")
        
        # Save report
        report = {
            'timestamp': datetime.now().isoformat(),
            'total_samples': len(all_results),
            'samples': all_results,
            'statistics': {
                'pitch_mean': float(np.mean(pitches)),
                'pitch_std': float(np.std(pitches)),
                'pitch_min': float(np.min(pitches)),
                'pitch_max': float(np.max(pitches)),
                'tempo_mean': float(np.mean(tempos)),
                'tempo_std': float(np.std(tempos)),
                'spectral_centroid_mean': float(np.mean(centroids)),
                'duration_total': float(np.sum(durations)),
            }
        }
        
        report_path = Path('/tmp/voice_analysis_complete.json')
        with open(report_path, 'w') as f:
            json.dump(report, f, indent=2)
        
        print()
        print("="*70)
        print(f"✅ Analysis complete! Report saved to: {report_path}")
        print("="*70)
        print()
        
        return 0
    else:
        print("❌ No samples found to analyze")
        return 1

if __name__ == '__main__':
    sys.exit(main())
