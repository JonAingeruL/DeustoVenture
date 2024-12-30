package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class MenuInicio extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MenuInicio() {
		setTitle("Deusto Venture");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 768);
        setLocationRelativeTo(null);
		setBackground(Color.BLACK);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        
        
        JButton nuevaPartida = new JButton("Nueva Partida");
		nuevaPartida.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
		JButton botonConfiguracion = new JButton("Configuracion");
		botonConfiguracion.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
		JButton botonContinuar = new JButton("Continuar");
		botonContinuar.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
		JButton salir = new JButton("Salir del juego");
		salir.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
		salir.addActionListener(e -> salir());
		salir.setForeground(Color.RED);
		add(Box.createVerticalGlue());
		add(nuevaPartida);
		nuevaPartida.setAlignmentX(CENTER_ALIGNMENT);
		nuevaPartida.setPreferredSize(new Dimension(100, 50));
		add(Box.createVerticalGlue());
		add(botonContinuar);
		botonContinuar.setAlignmentX(CENTER_ALIGNMENT);
		botonContinuar.setPreferredSize(new Dimension(100, 50));
		add(Box.createVerticalGlue());
		add(botonConfiguracion);
		botonConfiguracion.setAlignmentX(CENTER_ALIGNMENT);
		botonConfiguracion.setPreferredSize(new Dimension(100, 50));
		add(Box.createVerticalGlue());
		add(salir);
		salir.setAlignmentX(CENTER_ALIGNMENT);
		salir.setPreferredSize(new Dimension(100, 50));
		setBackground(Color.BLACK);
		setVisible(true);
		
			
    	
	}
	
	public void salir() {
		int choice = JOptionPane.showConfirmDialog(this,"¿Está seguro de que desea salir del juego?","Salir del juego", JOptionPane.YES_OPTION);
		if (choice == 0) {
				System.exit(0);
		}
		
	}
	
	public static void main(String[] args) {
                MenuInicio programa = new MenuInicio();
                programa.setVisible(true);
           
    }

}