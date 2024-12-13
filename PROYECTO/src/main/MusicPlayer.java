package main;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class MusicPlayer {
	private static Thread t;
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
	public static void playMusic(String path) {
		t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(!Thread.interrupted()) {
					FileInputStream fis;
					try {
						fis = new FileInputStream(new File(path));
						Player player = new Player(fis);
					
					
				player.play();
				player.close();
					} catch (FileNotFoundException | JavaLayerException e) {
						e.printStackTrace();
				}
				}
				
			}
		});
		t.start();
	}
	//TODO Averiguar por qué no se interrumpe la música
	public static void stopMusic() {
		t.interrupt();
	}
}
