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
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import main.AudioPlayer;
import main.ManejoTeclado;

public class Inventario extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable tabla;
	private ManejoTeclado tecladoM;
	
    public Inventario(ManejoTeclado tecladoM) {
    	AudioPlayer audio = new AudioPlayer("Resources/audio/InvSound.wav");
    	audio.playClip();
    	this.tecladoM =tecladoM;
        setTitle("Tu inventario");
        //Esto no es necesario, ya que está creado un windowListener que hace lo mismo
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        
    	JButton botonUsar = new JButton("Usar");
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
        
        
        String nombreFich = "src/inventario.txt";
        leerFichero(nombreFich, model);

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
        
        
     // Manejador de eventos para el botón "Usar"
    	botonUsar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
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
  // Lee el fichero y carga los datos en el inventario
    public void leerFichero(String nombreFich, DefaultTableModel modeloDatos) {
    	HashMap<String,String> mObjeto = new HashMap<>();
    	try(BufferedReader br = new BufferedReader(new FileReader(nombreFich))){
    		String linea;
    		br.readLine();
    		while ((linea = br.readLine())!=null) {
    			String[] datos = linea.split(";");
    			mObjeto.put(datos[1], datos[0]);
    			modeloDatos.addRow(datos);
    		}
    	} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
 
    public void cerrarInventario() {
    	tecladoM.abrirInventario =false;
		tecladoM.iPulsado = false;
		dispose();
    }
    
     
}



