package main;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
/**
 * Clase simple que reproduce el audio indicado por el String path dado;
 */
public class AudioPlayer {
	private Clip clip;

	public AudioPlayer(String path) {
		super();
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(new File(path));
			this.clip = AudioSystem.getClip();
			try {
				this.clip.open(ais);
			} catch (LineUnavailableException e) {
				e.printStackTrace();
				System.out.println("Error al abrir el clip");
			}
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
	}
/**
 * Reproduce el clip de audio
 */
	public void playClip() {
		this.clip.start();
		this.clip.setFramePosition(0);
	}
	public void playClip(float volumen) {
		this.setVolume(volumen);
		this.clip.start();
		this.clip.setFramePosition(0);
	}
/**
 * Cierra el clip de audio
 */
	public void closeClip() {
		this.clip.close();
	}
	//Código basado en hilo de StackOverflow
	public float getVolume() {
	    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);        
	    return (float) Math.pow(10f, gainControl.getValue() / 20f);
	}
	/**
	 * Ajusta el volumen del clip de audio con valores de 0 a 1
	 * Basado en hilo de StackOverflow
	 * @param volume El volumen entre 0 y 1
	 * @Throws IllegalArgumentException Si el volumen dado no está entre 0 y 1.
	 */
	public void setVolume(float volume) {
	    if (volume < 0f || volume > 1f)
	        throw new IllegalArgumentException("Volumen no válido: " + volume);
	    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);        
	    gainControl.setValue(20f * (float) Math.log10(volume));
	}
}
