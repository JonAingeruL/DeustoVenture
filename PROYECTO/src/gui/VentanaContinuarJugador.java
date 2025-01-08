package gui;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.GamePanel;
import main.GestorBD;

public class VentanaContinuarJugador extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String usuarioContinuar;

	public VentanaContinuarJugador() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Usuario");
		setSize(300, 150);
		setLocationRelativeTo(null);
		
		setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
		
		//crea un espacio vertical para centrar el texto de 40 píxeles
		add(Box.createVerticalStrut(0));
		
		//texto de información
		JLabel textoInfo = new JLabel();
		textoInfo.setText("Introduzca su usuario:");
		textoInfo.setFont(new Font(Font.DIALOG,Font.BOLD,14));
		add(textoInfo);
		
		//Texto en el que insertar usuario
		JTextField zonaEscribir = new JTextField("Inserte usuario");
		zonaEscribir.setFont(new Font(Font.DIALOG,Font.PLAIN,14));
		add(zonaEscribir);
		
		
		//Panel con botones Cancelar y Iniciar
		JPanel panelBotones = new JPanel();
		panelBotones.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 0));
		
		//boton cancelar
		JButton botonCancelar = new JButton();
		botonCancelar.setText("Cancelar");
		botonCancelar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new MenuInicio();
				dispose();
				
			}
		});
		panelBotones.add(botonCancelar);
		
		//boton iniciar juego
		JButton botonIniciarJuego = new JButton();
		botonIniciarJuego.setText("Iniciar Juego");
		botonIniciarJuego.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				GestorBD gbd = new GestorBD();
				usuarioContinuar = zonaEscribir.getText();
				if (usuarioContinuar.equals("") || usuarioContinuar.equals(null)) {
					JOptionPane.showMessageDialog(botonIniciarJuego, "Tienes que poner un usuario para iniciar el juego","Error",JOptionPane.ERROR_MESSAGE);
				} else if (!gbd.existeUsuario(usuarioContinuar)) {
					JOptionPane.showMessageDialog(botonIniciarJuego, "El jugador "+usuarioContinuar+" no existe", "Error", JOptionPane.ERROR_MESSAGE);
				} else if (gbd.existeUsuario(usuarioContinuar)) {
					iniciarJuego(usuarioContinuar, false);
				}
				
			}
		});
		
		panelBotones.add(botonIniciarJuego);
		
		add(panelBotones);
		
		
		
		
		
		
		setVisible(true);
	}
	
	
	
	public void iniciarJuego(String usuarioActual, Boolean esnuevoUsuario) {
		GestorBD gbd = new GestorBD();
		JFrame window = new JFrame(); //Creamos la ventana
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Esto permite cerrar bien la ventana cuando le demos a la X
		window.setResizable(false); //Para que no se le pueda cambiar el tamaño a la ventana
		window.setTitle("DeustoVenture"); //Le damos un nombre a la ventana
		
		//Despues de crear la JPanel en la clase GamePanel la añadimos al main para poder verla
		GamePanel gamePanel = new GamePanel(usuarioActual,esnuevoUsuario,gbd); //creanmos el gamepanel
		window.add(gamePanel); //Añandimos el gamepanel a la ventana
		
		window.pack(); //Con esto hace que se ajuste al tamaño y diseño preferidos de su subcomponente (el gamepanel)
		
		window.setLocationRelativeTo(null); //Con esto no especificamos la localizacion de la ventana, por lo que se abrira en el centro de la pantalla
		
		
		window.setVisible(true); //Para poder ver la pantalla
		
		gamePanel.iniciarJuegoHilo(usuarioActual,esnuevoUsuario,gbd); //Iniciamos el hilo para iniciar el bucle
		
		
	}
	

	
	public static void main(String[] args) {
		new VentanaContinuarJugador();
	}
}
	




