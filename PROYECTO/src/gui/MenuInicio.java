package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import main.GestorBD;
import main.Usuario;


public class MenuInicio extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Image imagenFondo = new ImageIcon("resources/texturas/extra/fondoMenuInicial.png").getImage();

	
	public MenuInicio() {
		setTitle("Deusto Venture");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
        setSize(400,360);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon("resources/texturas/extra/LogoDeustoVenture.png").getImage());
		//setBackground(Color.BLACK);
       //setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        //setIconImage(imagenFondo);
        
        //Creo un nuevo JPanel para utilizarlo como fondo
        //Utilizo esta forma de hacerlo para que de esta manera en caso de que queramos cambiar de tamaño de ventana la imagen solo se estire 
        // (no lo hagais tanto que si no la imagen se ve fatal xd)
        JPanel p = new JPanel() {
			private static final long serialVersionUID = 1L;
        	
			//hago un override de el paintComponent y de esta manera dibujo la imagen que necesito usando Graphics
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(imagenFondo, 0, 0,this.getWidth(), this.getHeight(),this);
			}
        };
        //aqui cambio el panel de contenido actual por otro nuevo, y así puedo 
        setContentPane(p);
        p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
        
        
        //Crea los botones que aparecen en el menu de Inicio y les cambia la fuente y el tamaño
        JButton nuevaPartida = new JButton("Nueva Partida");
		nuevaPartida.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
		JButton botonConfiguracion = new JButton("Configuracion");
		botonConfiguracion.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
		JButton botonContinuar = new JButton("Continuar");
		botonContinuar.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
		JButton botonRecords = new JButton("Tabla de puntuaciones");
		botonRecords.setFont(new Font(Font.DIALOG, Font.BOLD,15));
		JButton salir = new JButton("Salir del juego");
		salir.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
		
		//Configura el comportamiento del boton salir en el menu
		salir.addActionListener(e -> salir());
		salir.setForeground(Color.RED);
		
		//Añade los botones al menu, los centra y les cambia el tamaño
		
		//Título
		JLabel tituloJuego = new JLabel();
		tituloJuego.setText("DeustoVenture");
		tituloJuego.setAlignmentX(CENTER_ALIGNMENT);
		tituloJuego.setFont(new Font(Font.DIALOG,Font.BOLD,32));
		tituloJuego.setForeground(Color.BLUE);
		tituloJuego.setAlignmentY(CENTER_ALIGNMENT);
		add(tituloJuego);
		
		//Nuevapartida
		add(Box.createVerticalGlue());
		add(nuevaPartida);
		nuevaPartida.setAlignmentX(CENTER_ALIGNMENT);
		nuevaPartida.setPreferredSize(new Dimension(100, 50));
		
		nuevaPartida.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new VentanaRegistroJugador();
				dispose();
			}
		});

		//Continuar
		add(Box.createVerticalGlue());
		add(botonContinuar);
		botonContinuar.setAlignmentX(CENTER_ALIGNMENT);
		botonContinuar.setPreferredSize(new Dimension(100, 50));
		
		botonContinuar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new VentanaContinuarJugador();
				dispose();
				
			}
		});
		
		
		//Records 
		add(Box.createVerticalGlue());
		add(botonRecords);
		botonRecords.setAlignmentX(CENTER_ALIGNMENT);
		botonRecords.setPreferredSize(new Dimension(100,50));
		botonRecords.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				GestorBD gbd = new GestorBD();
				ArrayList<Usuario> usuarios = (ArrayList<Usuario>) gbd.listarUsuarios();
				new TablaDePuntuaciones(usuarios);
				
			}
		});
		
		
		//Salir
		add(Box.createVerticalGlue());
		add(salir);
		salir.setAlignmentX(CENTER_ALIGNMENT);
		salir.setPreferredSize(new Dimension(100, 50));
		setBackground(Color.BLACK);
		
		setVisible(true);
	}
	
	//Pregunta si quiere cerrar el juego antes de salir
	public void salir() {
		int choice = JOptionPane.showConfirmDialog(this,"¿Está seguro de que desea salir del juego?","Salir del juego", JOptionPane.YES_OPTION);
		if (choice == 0) {
				System.exit(0);
		}
		
	}
	
	//Main provisional para ver la ventana
	public static void main(String[] args) {
                MenuInicio programa = new MenuInicio();
                programa.setVisible(true);
           
    }

}