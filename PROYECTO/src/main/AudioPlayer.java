package main;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
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
	}
/**
 * Cierra el clip de audio
 */
	public void closeClip() {
		this.clip.close();
	}
}
