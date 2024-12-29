package gui;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import entidades.Jugador;
import main.AudioPlayer;
import main.GamePanel;
import main.ManejoTeclado;
import main.Mapa;

public class NPC2 extends JFrame{
	private static final long serialVersionUID = 1L;
	private ManejoTeclado tecladoM;
	
    public NPC2(ManejoTeclado tecladoM, String mapa, int celda, GamePanel gp) {
    	this.tecladoM =tecladoM;
        setTitle("NPC");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int x = 15; 
		int y = (gp.maxPantallaFila - gp.maxPantallaFila/3) * gp.tamañoBaldosa - 15; //hay q * para pasarlo a la unidad correcta
		int ancho = gp.maxPantallaColu * (gp.tamañoBaldosa -2); 
		int largo = gp.maxPantallaFila/3 * gp.tamañoBaldosa; 
		
		//llama al metodo de abajo y le t¡mete los datos para q haga el rectangulo, lo hago en 2 metodo pq hay varios npcs
		setBounds(x, y, ancho, largo); 

    	//Esto hace que que se pueda escuchar el evento de tecla esc añadido abajo
    	setFocusable(true);
        
    	// Manejador de cerrar la ventana con la X

		
		addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				int code = e.getKeyCode();
				if (code == KeyEvent.VK_ESCAPE) { //Esto quiere decir que si el usuario pulsa la tecla ESC se cierra el inventario
					cerrarConversacion();
				} 
			}
		});
		
		setResizable(false);
		setVisible(true);

    }

    /*
	public void leerFichero(String mapa,int celda, DefaultTableModel modeloDatos) {
		try {
			// Leo el fichero
			Scanner sc = new Scanner(new FileInputStream("src/lootCofre.txt"));
			while (sc.hasNextLine()) {
				// Si la linea actual equivale al indice que busco, la escaneo
				if (sc.nextLine().strip().equals("-" + mapa + ","+ celda +"-")) {
					String linea = sc.nextLine();
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
	*/
	public void cerrarConversacion() {
    	tecladoM.empezarConversacion =false;
		tecladoM.hablarNPCPulsado = false;
		dispose();
    }
	

}


