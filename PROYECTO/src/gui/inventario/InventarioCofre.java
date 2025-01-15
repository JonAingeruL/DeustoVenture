package gui.inventario;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import entidades.Jugador;
import main.sonido.*;
import main.GamePanel;
import main.ManejoTeclado;

//TODO Buscar manera de aprovechar código de inventario mediante herencia o interfaces
public class InventarioCofre extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable tabla;
	private ManejoTeclado tecladoM;
	
    public InventarioCofre(ManejoTeclado tecladoM, GamePanel gp, String mapa, int celda,Jugador jugador) {
    	AudioPlayer audio = new AudioPlayer("Resources/audio/chest.wav");
    	audio.playClip(gp.getVolumenAudio());
    	this.tecladoM =tecladoM;
        setTitle("Cofre");
        //Esto no es necesario, ya que está creado un windowListener que hace lo mismo
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        
    	JButton botonUsar = new JButton("Coger todo");
    	JButton botonSalir = new JButton("Salir");
    	
    	JPanel panelBotones = new JPanel();
    	panelBotones.add(botonUsar);
    	panelBotones.add(botonSalir);
    	getContentPane().add(panelBotones, BorderLayout.SOUTH);
    	//Esto hace que que se pueda escuchar el evento de tecla esc añadido abajo
    	setFocusable(true);

        // Nombres de las columnas
        String[] columnaNombres = {"Objeto", "Cantidad"};

        // Crear el modelo de la tabla
        DefaultTableModel model = new DefaultTableModel(columnaNombres,0) {
        	//añadiendo las 4 lineas de abajo, puedo hacer que toda la tabla se vuelva 
        	//no editable
			private static final long serialVersionUID = 1L;
			@Override
        	public boolean isCellEditable(int row, int column) {
        		return false;
        	}
        };
        
        
        leerFichero(mapa,celda, model);

        // Crear la tabla y asignar el modelo
        tabla = new JTable(model);
        getContentPane().add(new JScrollPane(tabla), BorderLayout.CENTER);
        
        //Asignar el renderer de cuerpo y el de Head
        TableCellRenderer bodyRendererInventario = new InventarioBodyRender();
        TableCellRenderer headRendererInventario = new InventarioHeadRenderer();
        tabla.setDefaultRenderer(Object.class, bodyRendererInventario);
        tabla.getTableHeader().setDefaultRenderer(headRendererInventario);
        tabla.setRowHeight(24);
        tabla.getTableHeader().setAlignmentX(CENTER_ALIGNMENT);
        tabla.setCellSelectionEnabled(true);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        
     // Manejador de eventos para el botón "Coger todo"
    	botonUsar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Coloca los objetos del modelo en el inventario. Si ya están, los suma
				for (int i = 0 ; i < model.getRowCount(); ++i) {
					if (jugador.getInventario().containsKey(model.getValueAt(i, 0))) {
						jugador.getInventario().put(model.getValueAt(i, 0).toString(),Integer.parseInt(model.getValueAt(i, 1).toString())+jugador.getInventario().get(model.getValueAt(i, 0)));
					}else {
						jugador.getInventario().put(model.getValueAt(i, 0).toString(),Integer.parseInt(model.getValueAt(i, 1).toString()));
	            }
				}
				//Vacia el cofre para la próxima vez que se abra
				vaciarCofre(mapa, celda);
				audio.closeClip();
				cerrarInventario();
				
			}
        	
    	});
    	
     // Manejador de eventos para el botón "Salir"
    	botonSalir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// reinicia ambas variables a false, para que no se vuelva a abrir hasta que se vuelva a pulsar la tecla I
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
				if (code == KeyEvent.VK_ESCAPE) { //Esto quiere decir que si el usuario pulsa la tecla ESC se cierra el inventario
					cerrarInventario();
				} 
			}
		});
		
		setResizable(false);
		setVisible(true);

    }
    /**
     * Lee datos de fichero para llenar el coffre con los objetos que corresponden
     * @param mapa El mapa en el que está el cofre (dirección)
	 * @param celda La celda en la que está el cofre
     * @param modeloDatos el modelo de datos del cofre
     */
	public void leerFichero(String mapa,int celda, DefaultTableModel modeloDatos) {
		try {
			// Leo el fichero
			Scanner sc = new Scanner(new FileInputStream("src/lootCofre.txt"));
			while (sc.hasNextLine()) {
				// Si la linea actual equivale al indice que busco, la escaneo
				if (sc.nextLine().strip().equals("-" + mapa + ","+ celda +"-")) {
					String linea = sc.nextLine();
					//Escaneo hasta encontrar el siguiente guión
					while(!linea.contains("-")) {
						String[] dato = linea.split(";");
						modeloDatos.addRow(dato);
						linea = sc.nextLine();
						
					}
					break;
				}
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Este método escribe a fichero para que se vacien los cofres ya looteados
	 * @param mapa El mapa en el que está el cofre (dirección)
	 * @param celda La celda en la que está el cofre
	 */
	public void vaciarCofre(String mapa,int celda) {
		//Este método funciona leyendo todo el fichero de cofres y después reescribiendolo omitiendo
		//el cofre a vaciar
		ArrayList<String> fichero = new ArrayList<String>();
		try {
			// Leo el fichero
			Scanner sc = new Scanner(new FileInputStream("src/lootCofre.txt"));
			while (sc.hasNextLine()) {
				String linea = sc.nextLine();
				// Si la linea actual equivale al indice que busco, la elimino
				if (!linea.strip().equals("-" + mapa + ","+ celda +"-")) {
					fichero.add(linea);
				}else {
					fichero.add(linea);
					linea = sc.nextLine();
					//Escaneo hasta encontrar el siguiente guión
					while(!linea.contains("-")) {
						linea = sc.nextLine();
					}
					fichero.add(linea);
					}
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
            PrintStream ps = new PrintStream("src/lootCofre.txt");
            for (String string : fichero) {
            	ps.println(string);
            }
            ps.close();
        } catch (FileNotFoundException e) {
        	e.printStackTrace();
        }
	}
	public void cerrarInventario() {
    	tecladoM.abrirInventario =false;
		tecladoM.iPulsado = false;
		dispose();
    }
	/**
	 * Este método inicializa los cofres con su loot por defecto
	 */
	public static void inicializarLoot() {
		 try {
	            PrintStream ps = new PrintStream("src/lootCofre.txt");
	            ps.println("-resources/mapas/tutorial.txt,1-");
	            ps.println("Manzana;3");
	            ps.println("Espada de Madera;1");
	            ps.println("Pocion de salud;1");
	            ps.println("-resources/mapas/mapa.txt,1-");
	            ps.println("Oro;100");
	            ps.println("Platano;2");
	            ps.println("-resources/mapas/mapa.txt,31-");
	            ps.println("Espada de Piedra;1");
	            ps.println("-resources/mapas/mapa.txt,74-");
	            ps.println("Pocion de salud;1");
	            ps.println("Manzana;2");
	            ps.println("-resources/mapas/mapa.txt,77-");
	            ps.println("Tarta de Tarta;1");
	            ps.println("Platano;1");
	            ps.println("-resources/mapas/mapa.txt,8-");
	            ps.println("Java en vena;3");
	            ps.println("Java Monster;1");
	            ps.println("-resources/mapas/dungeon1.txt,3-");
	            ps.println("Llave del bosque;1");
	            ps.println("Chuleta;1");
	            ps.println("Oro;100");
	            ps.println("-resources/mapas/dungeon1.txt,13-");
	            ps.println("Espada de Hierro;1");
	            ps.println("Cacaolat;1");
	            ps.println("Limon;2");
	            ps.println("-resources/mapas/dungeon2.txt,23-");
	            ps.println("Llave de la montaña;1");
	            ps.println("Espada de Diamante;1");
	            ps.println("Radler Limon;1");
	            ps.println("Durum solo Carne;1");
	            ps.println("-resources/mapas/dungeon3.txt,13-");
	            ps.println("Llave del volcán;1");
	            ps.println("Monster Java;1");
	            ps.println("Fabada asturiana;1");
	            ps.println("Oro;300");
	            ps.println("-resources/mapas/dungeon3.txt,21-");
	            ps.println("Espada de Obsidiana;1");
	            ps.println("Pocion de Salud;1");
	            ps.println("-");
	            ps.close();
	        } catch (FileNotFoundException e) {
	        	e.printStackTrace();
	        	System.out.println("Error inicializando loot cofres prueba");
	        }
	}
	

}
