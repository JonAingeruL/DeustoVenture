package main;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javazoom.jl.*;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class MusicPlayer {
	
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
		Thread t = new Thread(new Runnable() {
			
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
}
