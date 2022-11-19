package jgalaxian.Utils;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MusicBackground extends USound {

    private Clip clip;
    private AudioInputStream audioStream;
    private long rimanenti = 0;
    private static float volume = 1;

    public MusicBackground(String fileName) {
        super(fileName);
        try {
            AudioFormat baseFormat = AudioSystem.getAudioFileFormat(sndurl).getFormat();
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(super.sndurl);

            AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
            this.audioStream = AudioSystem.getAudioInputStream(decodedFormat, audioStream);
            clip = AudioSystem.getClip();
            Utils.getInstance().log("Creata clip : " + clip);
            clip.open(this.audioStream);
        } catch (IOException ex) {
            Utils.getInstance().log("ERRORE IOException in MusicBackground");
            Logger.getLogger(MusicBackground.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedAudioFileException ex) {
            Utils.getInstance().log("ERRORE UnsupportedAudioFileException in MusicBackground");
            Logger.getLogger(MusicBackground.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(MusicBackground.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static float getVolume() {
        return volume;
    }

    private float getVolumeClip() {
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        return (float) Math.pow(10f, gainControl.getValue() / 20f);
    }

    private void setVolumeClip(float volume) {
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(20f * (float) Math.log10(volume));
    }

    public void setVolume(float volume) {
        if (volume < 0f || volume > 1f) {
            throw new IllegalArgumentException("Volume non valido: " + volume + ".\nVolume max: 1.0\nVolume min (spento): 0");
        }

        this.volume = volume;

        if (clip != null) {
            setVolumeClip(volume);
        }
    }

    @Override
    public void play() {
        if (super.sndurl != null) {
            setVolumeClip(volume);

            if (this.rimanenti > 0) {
                clip.setFramePosition((int) rimanenti);
                this.rimanenti = 0;
            }
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    @Override
    public void pausa() {
        clip.stop();
        rimanenti = clip.getLongFramePosition();
    }

    @Override
    public void stop() {
        Utils.getInstance().log("Invocato stop musica background per clip : " + clip);
        if (clip != null) {
            clip.stop();
        }
        this.clip.setMicrosecondPosition(0);
        this.rimanenti = 0;
    }

    public void close() {
        if (clip != null) {
            clip.close();
        }
    }
}
