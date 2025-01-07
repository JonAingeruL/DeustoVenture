package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import main.GamePanel;


public class MenuPausa extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean isOpen = true;

	public MenuPausa(GamePanel gp) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Menu pausa");
		setSize(300, 400);
		setResizable(false);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setLocationRelativeTo(null);
		// En el futuro, información puede contener información útil, como la celda
		// actual, las coordenadas del jugador
		// o incluso el objetivo principal actual
		JLabel informacion = new JLabel("Información");
		informacion.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				reanudar(gp);
			}

		});

		JButton reanudar = new JButton("Reanudar partida");
		reanudar.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
		reanudar.addActionListener(e -> reanudar(gp));
		// Placeholder podría ser un menú de instrucciones, un menú de opciones o
		// incluso un diario de objetivos
		JButton botonConfiguracion = new JButton("Configuracion");
		botonConfiguracion.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
		JButton salir = new JButton("Salir del juego");
		salir.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
		salir.addActionListener(e -> salir());
		salir.setForeground(Color.RED);
		// TODO Poner esto en algún formato tiempo
		JLabel tiempo = new JLabel(""+gp.getJugador().getTiempoJugado()+"segundos");
		
		add(Box.createVerticalGlue());
		add(informacion);
		informacion.setAlignmentX(CENTER_ALIGNMENT);
		add(Box.createVerticalGlue());
		add(reanudar);
		reanudar.setAlignmentX(CENTER_ALIGNMENT);
		reanudar.setPreferredSize(new Dimension(100, 50));
		add(Box.createVerticalGlue());
		add(botonConfiguracion);
		botonConfiguracion.setAlignmentX(CENTER_ALIGNMENT);
		botonConfiguracion.setPreferredSize(new Dimension(100, 50));
		//actionListener creado para poder abrir el menu de configuración desde el menú de opciones
		botonConfiguracion.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new MenuConfiguracion(gp);
				
			}
		});
		
		
		
		add(Box.createVerticalGlue());
		add(salir);
		salir.setAlignmentX(CENTER_ALIGNMENT);
		salir.setPreferredSize(new Dimension(100, 50));
		add(Box.createVerticalGlue());
		add(tiempo);
		tiempo.setAlignmentX(CENTER_ALIGNMENT);
		add(Box.createVerticalGlue());
		setBackground(Color.BLACK);
		setVisible(true);

	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public void reanudar(GamePanel gp) {
		setOpen(false);
		dispose();
	}
	public void salir() {
		int choice = JOptionPane.showConfirmDialog(this,"¿Está seguro de que desea salir del juego?","Salir del juego", JOptionPane.YES_OPTION);
		if (choice == 0) {
				System.exit(0);
		}
		
	}

//	public static void main(String[] args) {
//		new MenuPausa();
//	}
}
