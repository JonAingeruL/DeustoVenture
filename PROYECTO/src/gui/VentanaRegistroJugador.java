package gui;

import java.awt.Dimension;
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

public class VentanaRegistroJugador extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public VentanaRegistroJugador() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Login");
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
		
		//add(Box.createVerticalStrut(1));
		
		//textodeError
		JLabel textoError = new JLabel();
		add(textoError);
		
		//Panel con botones Cancelar y Iniciar
		JPanel panelBotones = new JPanel();
		panelBotones.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 0));
		
		//boton cancelar
		JButton botonCancelar = new JButton();
		botonCancelar.setText("Cancelar");
		botonCancelar.addActionListener(e->dispose());
		panelBotones.add(botonCancelar);
		
		//boton iniciar juego
		JButton botonIniciarJuego = new JButton();
		botonIniciarJuego.setText("Iniciar Juego");
		botonIniciarJuego.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (zonaEscribir.getText()=="" || zonaEscribir.equals(null)) {
					JOptionPane.showMessageDialog( botonIniciarJuego, "Tienes que poner un usuario para iniciar el juego");
				} else {
					iniciarJuego();
				}
				
			}
		});
		panelBotones.add(botonIniciarJuego);
		
		add(panelBotones);
		
		
		
		
		
		
		setVisible(true);
	}
	
	public void iniciarJuego() {
		
	}
	
	public static void main(String[] args) {
		new VentanaRegistroJugador();
	}
	
}
