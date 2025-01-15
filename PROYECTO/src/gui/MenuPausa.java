package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import main.GamePanel;
import main.GestorBD;
import main.Usuario;


public class MenuPausa extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean isOpen = true;
	GestorBD gbd = new GestorBD();

	public MenuPausa(GamePanel gp) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Menu pausa");
		setSize(300, 400);
		setResizable(false);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setLocationRelativeTo(null);
		JLabel informacion = new JLabel(gp.getJugador().getNombreJugador());
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
		
		// Dinero del jugador (nuevo JLabel)
				JLabel dineroJugador = new JLabel("Dinero: " + gp.getJugador().getDineroJugador() + " oro");
				dineroJugador.setFont(new Font(Font.DIALOG, Font.PLAIN, 16));
				dineroJugador.setForeground(Color.black);
				dineroJugador.setAlignmentX(CENTER_ALIGNMENT);
		
		
		
		
		// Placeholder podría ser un menú de instrucciones, un menú de opciones o
		// incluso un diario de objetivos
		JButton botonConfiguracion = new JButton("Configuracion");
		botonConfiguracion.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
		JButton salir = new JButton("Salir del juego");
		salir.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
		salir.addActionListener(e -> salir());
		salir.setForeground(Color.RED);
		int segundos = gp.getJugador().getTiempoJugado();
		int horas = segundos/3600;
		int minutos = segundos/60;
		System.out.println(gp.getJugador().getTiempoJugado());
		if (minutos> 60) minutos=minutos%60;
		if (segundos> 60) segundos=segundos%60;
		JLabel tiempo = new JLabel("Tiempo de juego: "+horas+":"+minutos+":"+segundos);
		add(Box.createVerticalGlue());
		add(informacion);
		informacion.setAlignmentX(CENTER_ALIGNMENT);
		add(Box.createVerticalGlue());
		add(reanudar);
		reanudar.setAlignmentX(CENTER_ALIGNMENT);
		reanudar.setPreferredSize(new Dimension(100, 50));
		add(Box.createVerticalGlue());
		
		JButton botonGuardar = new JButton("Guardar Partida");
		botonGuardar.setAlignmentX(CENTER_ALIGNMENT);
		botonGuardar.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
		botonGuardar.setPreferredSize(new Dimension(100,50));
		botonGuardar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int choice = JOptionPane.showConfirmDialog(botonGuardar, "Seguro que quieres guardar? Reescribirás tu guardado anterior", "Confirmar", JOptionPane.YES_NO_OPTION);
				if (choice ==0) {
					gbd.actualizarUsuario(new Usuario(gp.getJugador().getNombreJugador(), gp.getJugador().getNumMuertes(), gp.getJugador().getEnemigosDerrotados(), gp.getJugador().getTiempoJugado()));
					gbd.actualizarPosicionUsuarioPos(gp.getJugador().getNombreJugador(), gp.getJugador().getX(), gp.getJugador().getY(), gp.getMapa().getNumcelda(), gp.getMapa().getNumeroMapa(), gp.getJugador().getArchivoACargar());
					gbd.resetearInventario(gp.getJugador().getNombreJugador());
					HashMap<String, Integer> inventario = gp.getJugador().getInventario();
					for (HashMap.Entry<String, Integer> entry : inventario.entrySet()) {
						String key = entry.getKey();
						Integer val = entry.getValue();
						gbd.guardarItemEnInventario(gp.getJugador().getNombreJugador(), key, val);
						
					}
					if(!gp.getJugador().objetoEnMano.equals("")) {
					gbd.guardarItemEnInventario(gp.getJugador().getNombreJugador(), gp.getJugador().objetoEnMano, 1);
					}
					JOptionPane.showMessageDialog(botonGuardar, "Partida guardada, ya puedes seguir jugando o cerrar el juego!");
				}
			
				
			}
		});
		
		
		add(botonGuardar);
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
		
		// Añadir el JLabel del dinero en la parte inferior
				add(dineroJugador);
				dineroJugador.setAlignmentX(CENTER_ALIGNMENT);

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
