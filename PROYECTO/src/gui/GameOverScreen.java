package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import main.GestorBD;

public class GameOverScreen extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GameOverScreen(int puntos,String usuarioActual)  {
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
