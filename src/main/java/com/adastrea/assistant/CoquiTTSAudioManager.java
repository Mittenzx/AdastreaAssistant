package com.adastrea.assistant;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Integrates Coqui TTS with the AudioManager for human-like voice synthesis.
 * 
 * This class provides a bridge between Java and the Python-based Coqui TTS system,
 * enabling real-time generation of emotionally expressive speech.
 * 
 * Features:
 * - Emotional voice synthesis (hostile, curious, cooperative, etc.)
 * - Natural prosody and intonation
 * - Human-like variations and imperfections
 * - Asynchronous audio generation
 * - Caching of generated audio
 * 
 * Requirements:
 * - Python 3.9+ with Coqui TTS installed (pip install TTS librosa soundfile scipy)
 * - scripts/tts_generate_human.py in the project root
 * 
 * @see AudioManager
 */
public class CoquiTTSAudioManager extends AudioManager {
    
    private final String ttsScriptPath;
    private final String audioOutputDir;
    private final boolean usePythonTTS;
    private final String pythonExecutable;
    
    /**
     * Create a new CoquiTTSAudioManager with default settings.
     * 
     * @param projectRoot The root directory of the project (where scripts/ is located)
     */
    public CoquiTTSAudioManager(String projectRoot) {
        this(projectRoot, "python3", true);
    }
    
    /**
     * Create a new CoquiTTSAudioManager with custom settings.
     * 
     * @param projectRoot The root directory of the project
     * @param pythonExecutable The Python executable to use (e.g., "python3", "python")
     * @param enableTTS Whether to enable TTS generation (false = fallback to console output)
     */
    public CoquiTTSAudioManager(String projectRoot, String pythonExecutable, boolean enableTTS) {
        super();
        this.pythonExecutable = pythonExecutable;
        this.ttsScriptPath = Paths.get(projectRoot, "scripts", "tts_generate_human.py").toString();
        this.audioOutputDir = Paths.get(projectRoot, "src", "main", "resources", "audio", "generated").toString();
        this.usePythonTTS = enableTTS && checkTTSAvailability();
        
        // Create output directory if it doesn't exist
        try {
            Files.createDirectories(Paths.get(audioOutputDir));
        } catch (Exception e) {
            System.err.println("[WARNING] Could not create audio output directory: " + e.getMessage());
        }
    }
    
    /**
     * Check if Coqui TTS is available and properly installed.
     * 
     * @return true if TTS is available, false otherwise
     */
    private boolean checkTTSAvailability() {
        try {
            // Check if script exists
            File scriptFile = new File(ttsScriptPath);
            if (!scriptFile.exists()) {
                System.err.println("[WARNING] TTS script not found at: " + ttsScriptPath);
                return false;
            }
            
            // Check if Python TTS library is installed
            Process process = new ProcessBuilder(
                pythonExecutable, "-c", "from TTS.api import TTS; print('OK')"
            ).start();
            
            boolean completed = process.waitFor(5, TimeUnit.SECONDS);
            if (!completed) {
                process.destroyForcibly();
                return false;
            }
            int exitCode = process.exitValue();
            
            if (exitCode == 0) {
                System.out.println("[INFO] Coqui TTS is available and ready");
                return true;
            } else {
                System.err.println("[WARNING] Coqui TTS library not installed. Install with:");
                System.err.println("  pip install TTS librosa soundfile scipy");
                return false;
            }
        } catch (Exception e) {
            System.err.println("[WARNING] Could not verify TTS availability: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Play voice message with emotional expression using Coqui TTS.
     * 
     * @param message The message to speak
     * @param emotion The emotion to convey (hostile, curious, cooperative, excited, etc.)
     * @return CompletableFuture that completes when audio generation finishes (or immediately if disabled)
     */
    public CompletableFuture<Void> playVoiceWithEmotion(String message, String emotion) {
        if (!isAudioEnabled()) {
            return CompletableFuture.completedFuture(null);
        }
        
        if (!usePythonTTS) {
            // Fallback to console output
            System.out.println("[AUDIO] Assistant speaks (" + emotion + "): " + message);
            return CompletableFuture.completedFuture(null);
        }
        
        // Generate audio asynchronously
        return CompletableFuture.runAsync(() -> {
            try {
                String audioPath = generateAudio(message, emotion);
                if (audioPath != null) {
                    playAudioFile(audioPath);
                }
            } catch (Exception e) {
                System.err.println("[ERROR] Failed to generate or play audio: " + e.getMessage());
                // Fallback to console output
                System.out.println("[AUDIO] Assistant speaks (" + emotion + "): " + message);
            }
        });
    }
    
    /**
     * Generate audio using Coqui TTS.
     * 
     * @param text The text to synthesize
     * @param emotion The emotion to convey
     * @return The path to the generated audio file, or null on failure
     */
    private String generateAudio(String text, String emotion) {
        try {
            // Create unique filename based on SHA-256 hash and emotion
            String hash = generateHash(text + emotion);
            String filename = "tts_" + hash.substring(0, 16) + ".wav";
            String outputPath = Paths.get(audioOutputDir, filename).toString();
            
            // Check cache first
            File cachedFile = new File(outputPath);
            if (cachedFile.exists() && cachedFile.length() > 0) {
                System.out.println("[INFO] Using cached audio: " + filename);
                return outputPath;
            }
            
            // Build command
            List<String> command = new ArrayList<>();
            command.add(pythonExecutable);
            command.add(ttsScriptPath);
            command.add("--text");
            command.add(text);
            command.add("--emotion");
            command.add(emotion);
            command.add("--output");
            command.add(outputPath);
            
            System.out.println("[INFO] Generating audio with Coqui TTS...");
            System.out.println("[DEBUG] Command: " + String.join(" ", command));
            
            // Execute TTS generation
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            
            // Read output
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                    if (line.contains("ERROR") || line.contains("WARNING")) {
                        System.err.println("[TTS] " + line);
                    } else if (line.contains("SUCCESS")) {
                        System.out.println("[TTS] " + line);
                    }
                }
            }
            
            // Wait for completion (max 60 seconds)
            boolean completed = process.waitFor(60, TimeUnit.SECONDS);
            if (!completed) {
                process.destroyForcibly();
                System.err.println("[ERROR] TTS generation timed out");
                return null;
            }
            
            int exitCode = process.exitValue();
            if (exitCode != 0) {
                System.err.println("[ERROR] TTS generation failed with exit code: " + exitCode);
                System.err.println("[ERROR] Output: " + output.toString());
                return null;
            }
            
            // Verify output file exists
            if (cachedFile.exists() && cachedFile.length() > 0) {
                System.out.println("[SUCCESS] Audio generated: " + filename);
                return outputPath;
            } else {
                System.err.println("[ERROR] Output file not created or empty: " + outputPath);
                return null;
            }
            
        } catch (Exception e) {
            System.err.println("[ERROR] Exception during audio generation: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Play an audio file.
     * 
     * Note: This is a placeholder implementation. In a real Minecraft mod integration,
     * override this method to use Minecraft's sound system.
     * 
     * @param audioPath The path to the audio file
     */
    protected void playAudioFile(String audioPath) {
        // Placeholder implementation
        // In a real implementation, this would use Minecraft's sound system
        // or a Java audio library like javax.sound.sampled
        System.out.println("[AUDIO] Playing audio file: " + audioPath);
    }
    
    /**
     * Override playVoice to use neutral emotion by default.
     */
    @Override
    public void playVoice(String message) {
        playVoiceWithEmotion(message, "neutral");
    }
    
    /**
     * Generate SHA-256 hash for cache key.
     * 
     * @param input The input string to hash
     * @return Hex string of the hash
     */
    private String generateHash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            // Fallback to hashCode if SHA-256 fails
            return String.valueOf(Math.abs(input.hashCode()));
        }
    }
    
    /**
     * Check if TTS is enabled and available.
     * 
     * @return true if TTS is enabled and available
     */
    public boolean isTTSAvailable() {
        return usePythonTTS;
    }
    
    /**
     * Get the path to the TTS script.
     * 
     * @return The path to the TTS script
     */
    public String getTTSScriptPath() {
        return ttsScriptPath;
    }
    
    /**
     * Get the audio output directory.
     * 
     * @return The audio output directory path
     */
    public String getAudioOutputDir() {
        return audioOutputDir;
    }
}
