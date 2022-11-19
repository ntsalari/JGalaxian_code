package jgalaxian.Utils;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class UEffect extends USound implements Closeable {

    private int clipArrayIndex = 0;
    private Clip[] audioClipArray;
    private long[][] metadata;
    private boolean pausa[];
	private boolean playable[];
    private static float volume = 1;

    private AudioFormat af;
    private Clip myClip = null;

    public UEffect(String fileName) {
        super(fileName);
        if (super.sndurl != null) {
            try {
                this.af = AudioSystem.getAudioFileFormat(sndurl).getFormat();
                int numClips = computeNumClips(fileName);
                this.audioClipArray = new Clip[numClips];
                this.metadata = new long[numClips][2];
                for (int i = 0; i < this.audioClipArray.length; i++) {
                    this.audioClipArray[i] = getClip(super.sndurl, this.af, i);
                    this.metadata[i][0] = this.metadata[i][1];
                }
				pausa = new boolean[this.audioClipArray.length];
				for(int i = 0; i < pausa.length; i++){
					pausa[i] = false;
				}
				playable = new boolean[this.audioClipArray.length];
				for(int i = 0; i < playable.length; i++){
					playable[i] = false;
				}
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } catch (Exception ex) {
                Logger l = Logger.getLogger(UEffect.class.getName());
                l.log(Level.WARNING, "Il file: " + fileName + " è illeggibile");
            }
        }
    }
    
    private int computeNumClips(String fileName) {
        switch (fileName) {
            //Posso avere massimo 5 alieni comuni che attaccano insieme "ravvicinati"
            case "colpito_alieno_comune.wav":
            //Posso avere massimo 5 alieni rossi che attaccano insieme "ravvicinati"
            case "colpito_alieno_rosso.wav":
            //Possono attaccare contemporaneamente massimo 5 alieni alla volta
            case "partenza_alieno.wav":
            //Posso sparare fino a 5 alieni ravvicinati ed il suono è relativamente lungo
            case "sparo_navicella_amica.wav":
                return 5;
            //Posso avere massimo 2 alieni gialli e solo difficilmente li ucciderò insieme
            case "colpito_alieno_giallo.wav":
                return 2;
            default:
                return 1;
        }
    }

    private Clip getClip(URL fileUrl, AudioFormat af, int i) throws UnsupportedAudioFileException, IOException {
        Clip audioClip = null;
        AudioInputStream ais = null;

        try {
            ais = AudioSystem.getAudioInputStream(fileUrl);
            int bufferSize = (int) ais.getFrameLength() * af.getFrameSize();
            DataLine.Info dataLineInfo = new DataLine.Info(Clip.class, ais.getFormat(), bufferSize);

            if (!AudioSystem.isLineSupported(dataLineInfo)) {
                throw new IOException("Errore: AudioSystem non supporta l'oggetto DataLine.Info passato");
            }

            try {
                audioClip = (Clip) AudioSystem.getLine(dataLineInfo);
                audioClip.open(ais);
                this.metadata[i][1] = audioClip.getFrameLength();
                audioClip.setFramePosition((int) this.metadata[i][1]);
            } catch (LineUnavailableException lue) {
                throw new IOException("Errore: Lanciata eccezione LineUnavailableException");
            }

        } catch (UnsupportedAudioFileException uafe) {
            uafe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return audioClip;
    }
    
    public static float getVolume() {
        return volume;
    }
    
    private float getVolumeClip(Clip clip) {
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);        
        return (float) Math.pow(10f, gainControl.getValue() / 20f);
    }

    private void setVolumeClip(Clip clip) {
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);        
        gainControl.setValue(20f * (float) Math.log10(volume));
    }
    
    public void setVolume(float volume) {
        if (volume < 0f || volume > 1f)
            throw new IllegalArgumentException("Volume non valido: " + volume + ".\nVolume max: 1.0\nVolume min (spento): 0");
        
        this.volume = volume;
        
        for (int i = 0; i < this.audioClipArray.length; i++) {
            if (this.audioClipArray[i] != null) {
                setVolumeClip(this.audioClipArray[i]);
            }
        }
    }

    @Override
    public void play() {
        if (this.audioClipArray[this.clipArrayIndex] != null) {
                setVolumeClip(this.audioClipArray[this.clipArrayIndex]);
				this.audioClipArray[this.clipArrayIndex].setMicrosecondPosition(0);
                this.audioClipArray[this.clipArrayIndex].start();
				playable[clipArrayIndex] = true;
                this.clipArrayIndex ++;
                this.clipArrayIndex = this.clipArrayIndex % this.audioClipArray.length;
        }
    }
	
	public void unpause(){
			for (int i = 0; i < this.audioClipArray.length; i++) {
				if(playable[i] && pausa[i]){
					pausa[i] = false;
						if (this.audioClipArray[i] == null) {
							break;
						}
					setVolumeClip(this.audioClipArray[i]);
					this.audioClipArray[i].setFramePosition((int)this.metadata[i][0]);
					this.audioClipArray[i].start();
				}
			}
	}

    @Override
    public void pausa() {
        for (int i = 0; i < this.audioClipArray.length; i++) {
            if (this.audioClipArray[i] != null && playable[i]) {
                this.audioClipArray[i].stop();
                this.metadata[i][0]  = this.audioClipArray[i].getLongFramePosition();
				if(this.metadata[i][0] <= this.audioClipArray[i].getFrameLength() &&
						this.audioClipArray[i].getMicrosecondPosition() != 0){
					pausa[i] = true;
				}else{
					pausa[i] = false;
				}
            }
        }
    }
        
    @Override
    public void stop() {
        
        for (int i = 0; i < this.audioClipArray.length; i++) {
            if (this.audioClipArray[i] != null) {
                this.audioClipArray[i].stop();
				this.pausa[i] = false;
				this.playable[i] = false;
				this.audioClipArray[i].setMicrosecondPosition(0);
            }
        }
    }

    @Override
    public void close() {
			for (int i = 0; i < this.audioClipArray.length; i++) {
                if (this.audioClipArray[i] != null) {
                    this.audioClipArray[i].close();
                }
            }
    }

}
