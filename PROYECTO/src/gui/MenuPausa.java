package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import main.ManejoTeclado;

public class MenuPausa extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private boolean isOpen = true;
	public MenuPausa() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Menu pausa");
		setSize(300, 400);
		setResizable(false);
		setLayout(new GridLayout(5, 1,0, 50));
		setLocationRelativeTo(null);
		//En el futuro, información puede contener información útil, como la celda actual, las coordenadas del jugador
		//o incluso el objetivo principal actual
		JLabel informacion = new JLabel("Información");
		informacion.setHorizontalAlignment(SwingConstants.CENTER);
		informacion.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
		
		JButton reanudar = new JButton("Reanudar partida");
		reanudar.addActionListener(e -> {setOpen(false); dispose(); });
		//Placeholder podría ser un menú de instrucciones, un menú de opciones o incluso un diario de objetivos 
		JButton placeholder1 = new JButton("Placeholder");
		JButton salir = new JButton("Salir del juego");
		salir.addActionListener(e -> System.exit(0) );
		salir.setBackground(Color.RED);
		//Ayuda a que la parte de abajo no parezca muy vacía y puede ser interesante.
		JLabel hora = new JLabel(""+LocalDate.now());
		hora.setHorizontalAlignment(SwingConstants.CENTER);
		
		add(informacion);
		add(reanudar);
		add(placeholder1);
		add(salir);
		add(hora);
		
		
		
		setVisible(true);
		
	}
	public boolean isOpen() {
		return isOpen;
	}
	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}
	

	
	
//	public static void main(String[] args) {
//		new MenuPausa();
//	}
}
