package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import main.GamePanel;
import main.ManejoTeclado;

public class NPC2 extends JFrame{
	private static final long serialVersionUID = 1L;
	private ManejoTeclado tecladoM;
	
    public NPC2(ManejoTeclado tecladoM, GamePanel gp, String marcador) {
    	this.tecladoM =tecladoM;
        setTitle("NPC");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        int x = 275; 
		int y = (gp.maxPantallaFila - gp.maxPantallaFila/3) * gp.tamañoBaldosa - 15; //hay q * para pasarlo a la unidad correcta
		int ancho = gp.maxPantallaColu * (gp.tamañoBaldosa -2); 
		int largo = gp.maxPantallaFila/3 * gp.tamañoBaldosa; 
		
		setBounds(x, y, ancho, largo); 

    	//Esto hace que que se pueda escuchar el evento de tecla esc añadido abajo
    	setFocusable(true);
    	setResizable(false);
        
    	JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Diseño vertical
        		
        add(panel);

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				cerrarConversacion();
			}
		});
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
		
//		leerFichero(marcador);
		JLabel instruccion = new JLabel("Pulse esc para cerrar");
		add(instruccion, BorderLayout.NORTH);
		setResizable(false);
		setVisible(true);

    }
    
    /*
     * PRUEBAS
     
    
    public void leerFichero(String marcador) {
        try (Scanner sc = new Scanner(new FileInputStream("src/NPCs.txt"))) {
            boolean encontrado = false;

            // Leer línea por línea
            while (sc.hasNextLine()) {
                String linea = sc.nextLine().strip();

                // mira si tienen el marcador q le hemos pasado
                if (linea.equals(marcador)) {
                    encontrado = true;

                    // busca otro marcador o el final del archivo
                    while (sc.hasNextLine()) {
                        String texto = sc.nextLine().strip();
                        if (texto.startsWith("-")) { // Detecta otro marcador, detener lectura
                            break;
                        }
                        // Crear JLabel con el texto y añadirlo al panel
                        JLabel etiqueta = new JLabel("--"+texto);
                        etiqueta.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrar etiqueta
                        etiqueta.setFont(new Font("PAPYRUS", Font.PLAIN, 20));// Dar fuente a la etiqueta
                        etiqueta.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.WHITE, 5)
                        		, BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.LIGHT_GRAY, Color.GRAY))); //Dar borde a la etiqueta
                        etiqueta.setBackground(new Color(0,0,0)); //Color a la etiqueta
                        etiqueta.setForeground(Color.YELLOW.brighter());
                        etiqueta.setOpaque(true);
                        add(etiqueta); // Añadir al contenedor
                    }
                    break; // Salir del bucle principal
                }
            }

            if (!encontrado) {
                // Si no se encuentra el marcador, mostrar un mensaje
                JLabel etiquetaError = new JLabel("Marcador no encontrado: " + marcador);
                etiquetaError.setForeground(Color.RED);
                add(etiquetaError);
            }

            // Refrescar la ventana
            revalidate();
            repaint();

        } catch (FileNotFoundException e) {
            JLabel errorLabel = new JLabel("Error: Archivo no encontrado.");
            errorLabel.setForeground(Color.RED);
            add(errorLabel);
            revalidate();
            repaint();
        }
    }
    
    */

	public void cerrarConversacion() {
    	tecladoM.empezarConversacion =false;
		tecladoM.hablarNPCPulsado = false;
		dispose();
    }
	

}


