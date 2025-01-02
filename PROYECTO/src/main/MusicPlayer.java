package main;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class MusicPlayer {
	Player player;
	boolean playing;
	FileInputStream fis;
	String path;
	private static Thread t;
	
	public MusicPlayer(String path) {
		super();
		this.path = path;
		
	}
//	public static void playMusicOnce(String path) {
//		Thread t = new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//					while(!Thread.interrupted()) {
//						FileInputStream fis;
//						try {
//							fis = new FileInputStream(new File(path));
//							Player player = new Player(fis);
//						
//						
//					player.play();
//					player.close();
//						} catch (FileNotFoundException | JavaLayerException e) {
//							e.printStackTrace();
//					}
//				}
//				
//			}
//		});
//		t.start();
//	}
	public void playMusic() {
		t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				playing = true;
				while(!Thread.interrupted()&&playing) {
					try {
						fis = new FileInputStream(new File(path));
						player = new Player(fis);
				player.play();
				player.close();
					} catch (JavaLayerException | IOException e) {
						e.printStackTrace();
				}
				}
				try {
					System.out.println("End");
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		t.start();
	}
	//TODO Averiguar por qué no se interrumpe la música
	public void stopMusic() {
		playing = false;
		t.interrupt();
	}
	/**
	 * Cambia el volumen del objeto player actual.
	 * NOTA: Este método convierte el float a dB, de manera que el sonido
	 * no aumenta de manera lineal. También se asegura de que el volumen máximo no supere el inicial, así
	 * que cualquier valor por encima de aprox 0,93 sonará igual de alto.
	 * @param volumen float de 0 a 1.
	 */
	public void cambiarVolumen(float volumen) {
		//TODO El volumen está en escala logaritmica (dB) No es intuitivo...
		if(playing) {
			if (volumen==0) {
				player.setVolume(-80f);
			}else {
				volumen = volumen*86-80;
				if (volumen>0) {
					volumen = 0;
				}
				player.setVolume(volumen);
			}
		}
	}
}
