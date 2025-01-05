package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import entidades.Jugador;
import main.AudioPlayer;
import main.GamePanel;
import main.ManejoTeclado;

public class Inventario extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable tabla;
	private ManejoTeclado tecladoM;
	// HashMap de Espadas y su daño
	private HashMap<String, Integer> espadasDisponibles = new HashMap<String, Integer>() {
		private static final long serialVersionUID = 1L;
		{
			put("Espada de Madera", 1);
			put("Espada de Piedra", 2);
			put("Espada de Hielo", 1);
			put("Espada Debug", 200);
		}
	};
	// Hashmap de Pociones y su curación
	private HashMap<String, Integer> objetosCurativos = new HashMap<String, Integer>() {
		private static final long serialVersionUID = 1L;
		{
			put("Manzana", 1);
			put("Pocion de salud", 6);
		}
	};

	public Inventario(ManejoTeclado tecladoM, GamePanel gp, Jugador jugador) {
		AudioPlayer audio = new AudioPlayer("Resources/audio/InvSound.wav");
		audio.playClip(gp.getVolumenAudio());
		this.tecladoM = tecladoM;
		setTitle("Tu inventario");
		// Esto no es necesario, ya que está creado un windowListener que hace lo mismo
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 300);
		setLocationRelativeTo(null);

		JButton botonUsar = new JButton("Usar");
		JButton botonSalir = new JButton("Salir");

		JPanel panelBotones = new JPanel();
		panelBotones.add(botonUsar);
		panelBotones.add(botonSalir);
		getContentPane().add(panelBotones, BorderLayout.SOUTH);
		// Esto hace que que se pueda escuchar el evento de tecla esc añadido abajo
		setFocusable(true);

		// Nombres de las columnas
		String[] columnaNombres = { "Objeto", "Cantidad" };

		// Crear el modelo de la tabla
		DefaultTableModel model = createTableModel(jugador);

		// Crear la tabla y asignar el modelo
		tabla = new JTable(model);
		getContentPane().add(new JScrollPane(tabla), BorderLayout.CENTER);

		// Asignar el renderer de cuerpo y el de Head
		TableCellRenderer bodyRendererInventario = new InventarioBodyRender();
		TableCellRenderer headRendererInventario = new InventarioHeadRenderer();
		tabla.setDefaultRenderer(Object.class, bodyRendererInventario);
		tabla.getTableHeader().setDefaultRenderer(headRendererInventario);
		tabla.setRowHeight(24);
		tabla.getTableHeader().setAlignmentX(CENTER_ALIGNMENT);
		tabla.setRowSelectionAllowed(true);
		tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Manejador de eventos para el botón "Usar"
		botonUsar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String objetoSeleccionado = (String) tabla.getValueAt(tabla.getSelectedRow(), 0);
				//Si el objeto está en algún hashmap de objetos utilizables, se activa su uso.
				if (espadasDisponibles.containsKey(objetoSeleccionado)
						|| objetosCurativos.containsKey(objetoSeleccionado)) {

					String numObjetosSeleccionados = (String) tabla.getValueAt(tabla.getSelectedRow(), 1);
					//Si el objeto es el último, se elimina
					if (Integer.parseInt(numObjetosSeleccionados) - 1 == 0) {
						jugador.getInventario().remove(objetoSeleccionado);
					} else {
						//Si hay más de uno, se reduce la cantidad
						jugador.getInventario().put(objetoSeleccionado,
								jugador.getInventario().get(objetoSeleccionado) - 1);
					}
					//Si el jugador tenía algún objeto en mano, se sustituye por el nuevo y el viejo
					//vuelve al inventario
					if (!jugador.objetoEnMano.equals("")) {
						jugador.getInventario().put(jugador.objetoEnMano, 1);
					}
					//Si el objeto es una espada, se equipa al jugador
					if (espadasDisponibles.containsKey(objetoSeleccionado)) {
						jugador.objetoEnMano = objetoSeleccionado;
						jugador.danoJugador = espadasDisponibles.get(objetoSeleccionado);
						//Si el objeto es curativo, el jugador se cura el valor que corresponda al objeto
					} else if (objetosCurativos.containsKey(objetoSeleccionado)) {
						jugador.cambiarVidas(objetosCurativos.get(objetoSeleccionado));
					} else {
						jugador.objetoEnMano = "";
					}
					audio.closeClip();
					cerrarInventario();
				}else {
					//Si el objeto no es utilizable, se avisa al jugador con un mensaje
					gp.anadirMensaje(new Mensaje("Este objeto no es utilizable", 60));
			}
				
			}
			
		});

		// Manejador de eventos para el botón "Salir"
		botonSalir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// reinicia ambas variables a false, para que no se vuelva a abrir hasta que se
				// vuelva a pulsar la tecla I
				cerrarInventario();
			}
		});
		// Manejador de cerrar la ventana con la X
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				audio.closeClip();
				cerrarInventario();
			}
		});

		addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {
				int code = e.getKeyCode();
				if (code == KeyEvent.VK_ESCAPE) { // Esto quiere decir que si el usuario pulsa la tecla ESC se cierra el
													// inventario
					cerrarInventario();
				}
			}
		});

		setResizable(false);
		setVisible(true);

	}

	// Lee el fichero y carga los datos en el inventario
	public static DefaultTableModel crearModeloFichero(String nombreFich) {
		String[] columnaNombres = { "Objeto", "Cantidad" };
		DefaultTableModel modeloDatos = new DefaultTableModel(columnaNombres, 0) {
			// añadiendo las 4 lineas de abajo, puedo hacer que toda la tabla se vuelva
			// no editable
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		try (BufferedReader br = new BufferedReader(new FileReader(nombreFich))) {
			String linea;
			while ((linea = br.readLine()) != null) {
				String[] datos = linea.split(";");
				modeloDatos.addRow(datos);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return modeloDatos;
	}
	/**
	 * Crea el modelo de tabla de la ventana a partir del hashmap inventario del jugador
	 * @param jugador El jugador
	 * @return DefaultTableModel
	 */
	public DefaultTableModel createTableModel(Jugador jugador) {
		HashMap<String, Integer> mapa = jugador.getInventario();
		String[] columnaNombres = { "Objeto", "Cantidad" };
		DefaultTableModel modeloDatos = new DefaultTableModel(columnaNombres, 0) {
			// añadiendo las 4 lineas de abajo, puedo hacer que toda la tabla se vuelva
			// no editable
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		for (HashMap.Entry<String, Integer> entry : mapa.entrySet()) {
			String key = entry.getKey();
			Integer val = entry.getValue();
			modeloDatos.addRow(new String[] { key, val.toString() });
		}
		return modeloDatos;
	}

	public static void cargarFichero(DefaultTableModel model) {
		try {
			PrintStream ps = new PrintStream("src/inventario.txt");
			for (int i = 0; i < model.getRowCount(); ++i) {
				ps.println(model.getValueAt(i, 0) + ";" + model.getValueAt(i, 1));
			}
			ps.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void cerrarInventario() {
		tecladoM.abrirInventario = false;
		tecladoM.iPulsado = false;
		dispose();
	}

	public HashMap<String, Integer> getEspadasDisponibles() {
		return espadasDisponibles;
	}

	public void setEspadasDisponibles(HashMap<String, Integer> espadasDisponibles) {
		this.espadasDisponibles = espadasDisponibles;
	}
/**
 * Inicializa un inventario de prueba
 */
	public static void inicializarInventarioPrueba() {
		try {
			PrintStream ps = new PrintStream("src/inventario.txt");
			ps.println("Espada de Madera;1");
			ps.println("Espada de Piedra;1");
			ps.println("Pocion de salud;1");
			ps.println("Manzana;1");
			ps.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Error inicializando inventario prueba");
		}
	}

}
