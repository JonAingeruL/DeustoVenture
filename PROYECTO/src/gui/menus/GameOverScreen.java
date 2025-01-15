package gui.menus;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import db.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import main.GamePanel;

public class GameOverScreen extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GameOverScreen(int puntos,GamePanel gp, String usuarioActual)  {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);
		setSize(500, 500);
		setTitle("GameOver");
		setBackground(Color.BLACK);
		//Panel principal
		JPanel principal = new JPanel(new GridLayout(3, 1));
		principal.setBackground(Color.BLACK);
		JLabel bigLabel = new JLabel("GAMEOVER", JLabel.CENTER);
		bigLabel.setBackground(getBackground());
		bigLabel.setFont(new Font("ARIAL", Font.BOLD, 50));
		bigLabel.setForeground(Color.YELLOW);
		principal.add(bigLabel);
		//Fuente de letra mediana
		Font mediana = new Font("ARIAL", Font.PLAIN, 15);
		//Panel de guardado de datos
		JPanel guardar = new JPanel(new GridLayout(2,3));
		guardar.setBackground(getBackground());
		guardar.add(new JLabel(""));
		JLabel textoGuardar = new JLabel("¿Guardar estadisticas?", JLabel.CENTER);
		textoGuardar.setForeground(Color.WHITE);
		textoGuardar.setFont(mediana);
		guardar.add(textoGuardar);
		guardar.add(new JLabel(""));
		guardar.add(new JLabel(""));
		JButton botonGuardar = new JButton("Guardar");
		botonGuardar.setFont(mediana);
		//TODO boton guardado saca una ventana que te pide tu nombre de usuario. Después en DB se guarda junto con otros datos
		botonGuardar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				GestorBD gbd = new GestorBD();
				

				int choice = JOptionPane.showConfirmDialog(botonGuardar, "Quieres guardar el progreso? El juego después se cerrará", "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null);
				if (choice==0) {
					gbd.actualizarUsuario(new Usuario(usuarioActual, gp.getJugador().getNumMuertes(), gp.getJugador().getEnemigosDerrotados(), gp.getJugador().getTiempoJugado()));
					gbd.actualizarPosicionUsuarioPos(usuarioActual, gp.getJugador().getX(), gp.getJugador().getY(), gp.getMapa().getNumcelda(), gp.getMapa().getNumeroMapa(), gp.getJugador().getArchivoACargar());
					gbd.resetearInventario(usuarioActual);
					HashMap<String, Integer> inventario = gp.getJugador().getInventario();
					for (HashMap.Entry<String, Integer> entry : inventario.entrySet()) {
						String key = entry.getKey();
						Integer val = entry.getValue();
						gbd.guardarItemEnInventario(gp.getJugador().getNombreJugador(), key, val);
					}
					if(!gp.getJugador().objetoEnMano.equals("")) {
						gbd.guardarItemEnInventario(gp.getJugador().getNombreJugador(), gp.getJugador().objetoEnMano, 1);
						}
					System.exit(0);
					dispose();
					
				}
				
				
			}
		} );
		guardar.add(botonGuardar);
		guardar.add(new JLabel(""));
		principal.add(guardar);
		//Label que te muestra los enemigos derrotados
		JLabel scoreLabel = new JLabel("Enemigos derrotados: "+puntos, JLabel.CENTER);
		scoreLabel.setForeground(Color.YELLOW);
		scoreLabel.setFont(mediana);
		principal.add(scoreLabel);
		add(principal);
		setVisible(true);
	}
	
	
	
	

}
