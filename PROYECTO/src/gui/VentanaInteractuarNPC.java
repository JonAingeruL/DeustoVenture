package gui;


import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


import main.GamePanel;
import main.ManejoTeclado;

public class VentanaInteractuarNPC extends JFrame{
	private static final long serialVersionUID = 1L;
	private ManejoTeclado tecladoM;
	
    public VentanaInteractuarNPC(ManejoTeclado tecladoM, GamePanel gp, String marcador) {
		
    	this.tecladoM =tecladoM;
        setTitle("NPC");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setIconImage(new ImageIcon("resources/texturas/extra/LogoDeustoVenture.png").getImage());
        int x = 275; 
		int y = (gp.maxPantallaFila - gp.maxPantallaFila/3) * gp.tamañoBaldosa - 15; //hay q * para pasarlo a la unidad correcta
		int ancho = gp.maxPantallaColu * (gp.tamañoBaldosa -2); 
		int largo = gp.maxPantallaFila/3 * gp.tamañoBaldosa; 
		
		setBounds(x, y, ancho, largo); 

		// Esto hace que que se pueda escuchar el evento de tecla esc añadido abajo
	    setFocusable(true);
	    setResizable(false);

	    // Crear el panel principal
	    JPanel panel = new JPanel();
	    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Diseño vertical
	    panel.setBorder(BorderFactory.createCompoundBorder(
	        BorderFactory.createLineBorder(Color.YELLOW, 5), // Borde amarillo grueso
	        BorderFactory.createEmptyBorder(10, 10, 10, 10) // Espacio interno
	    ));
	    panel.setBackground(Color.BLACK); // Fondo negro del panel

	    // Agregar un JLabel con la frase del NPC
	    JLabel fraseLabel = new JLabel(marcador != null ? marcador : "No hay frase disponible");
	    fraseLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrar el texto
	    fraseLabel.setFont(new Font("Georgia", Font.BOLD, 18)); // Fuente bonita (Georgia)
	    fraseLabel.setForeground(Color.YELLOW); // Texto amarillo
	    fraseLabel.setOpaque(true);
	    fraseLabel.setBackground(Color.BLACK); // Fondo negro para el texto
	    fraseLabel.setBorder(BorderFactory.createCompoundBorder(
	        BorderFactory.createLineBorder(Color.YELLOW, 2), // Borde amarillo para el texto
	        BorderFactory.createEmptyBorder(5, 5, 5, 5) // Espaciado interno del texto
	    ));
	    panel.add(fraseLabel);

	    // Agregar instrucción para cerrar
	    JLabel instruccion = new JLabel("Pulse ESC para cerrar");
	    instruccion.setAlignmentX(Component.CENTER_ALIGNMENT);
	    instruccion.setFont(new Font("Arial", Font.ITALIC, 14));
	    instruccion.setForeground(Color.LIGHT_GRAY);
	    panel.add(instruccion);

	    // Agregar el panel a la ventana
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



