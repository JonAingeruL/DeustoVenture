package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GameOverScreen extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GameOverScreen(int puntos)  {
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
		botonGuardar.addActionListener(e -> {JOptionPane.showMessageDialog(botonGuardar, "Funcionalidad no implementada"); botonGuardar.setEnabled(false);} );
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
	public static void main(String[] args) {
		//new GameOverScreen(100);
	}
	
	
	

}
