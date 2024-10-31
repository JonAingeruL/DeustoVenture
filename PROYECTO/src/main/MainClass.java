package main;

import javax.swing.JFrame;

public class MainClass {

	public static void main(String[] args) {
		
		JFrame window = new JFrame(); //Creamos la ventana
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Esto permite cerrar bien la ventana cuando le demos a la X
		window.setResizable(false); //Para que no se le pueda cambiar el tamaño a la ventana
		window.setTitle("Aventura 2D"); //Le damos un nombre a la ventana
		
		//Despues de crear la JPanel en la clase GamePanel la añadimos al main para poder verla
		GamePanel gamePanel = new GamePanel(); //creanmos el gamepanel
		window.add(gamePanel); //Añandimos el gamepanel a la ventana
		
		window.pack(); //Con esto hace que se ajuste al tamaño y diseño preferidos de su subcomponente (el gamepanel)
		
		window.setLocationRelativeTo(null); //Con esto no especificamos la localizacion de la ventana, por lo que se abrira en el centro de la pantalla
		
		
		window.setVisible(true); //Para poder ver la pantalla
		
		gamePanel.iniciarJuegoHilo(); //Iniciamos el hilo para iniciar el bucle
		}

}
