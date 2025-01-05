package main;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class MusicPlayer {
	private Player player;
	private boolean playing;
	private FileInputStream fis;
	private String path;
	private Thread t;
	
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
		cambiarVolumen(0);
		playing = false;
		t.interrupt();
	}
	
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	/**
	 * Cambia el volumen del objeto player actual.
	 * @param volumen float de 0 a 1.
	 */
	public void cambiarVolumen(float volumen) {
		if(playing&&!(null==player)) {
			if (volumen==0) {
				player.setVolume(-80f);
			}else {
				//El volumen en esta librería esta en dB (escala logaritmica), así que
				//hago esta transformación para que la escala sea más cercana a la lineal
				//y por lo tanto más facil de entender para un usuario.
				volumen = (float) Math.pow(volumen, 0.3);
				volumen = volumen*86-80;
				if (volumen>0) {
					volumen = 0;
				}
				player.setVolume(volumen);
			}
		}
	}
}
