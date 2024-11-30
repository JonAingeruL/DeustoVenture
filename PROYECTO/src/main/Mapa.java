package main;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import javax.imageio.ImageIO;
import entidades.Jugador;
import entidades.NPC;

public class Mapa {
	private int[][] celda;
	private int numCelda;
	private String archivoACargar="Resources/mapas/tutorial.txt";
	private List<NPC> npcs;
	private BufferedImage i = new BufferedImage(1024,768, BufferedImage.TYPE_INT_ARGB);

	public Mapa(int[][] celda, int numCelda) {
		super();
		this.celda = celda;
		this.numCelda = numCelda;
	}

	public Mapa(int[][] celda) {
		super();
		this.celda = celda;

	}

	public Mapa(int numCelda) {
		super();
		this.numCelda = numCelda;
		this.npcs = new ArrayList<NPC>();
		cargarCelda(archivoACargar, numCelda);
	}

	/**
	 * Este metodo devuelve el array que representa la celda actual
	 * 
	 * @return array int[][] de la celda actual
	 */
	public int[][] getCelda() {
		return celda;
	}

	/**
	 * Este metodo establece el array que representa a la celda actual
	 * 
	 * @param celda La celda que se quiere establecer como actual
	 */
	public void setCelda(int[][] celda) {
		this.celda = celda;
	}

	/**
	 * Este metodo devuelve el numero de la celda actual
	 * 
	 * @return Numero de la celda actual
	 */
	public int getNumcelda() {
		return numCelda;
	}

	/**
	 * Este metodo establece el numero de la celda actual
	 * 
	 * @param numCelda El numero de celda que se quiere establecer
	 */
	public void setNumcelda(int numCelda) {
		this.numCelda = numCelda;
	}

	/**
	 * Este método carga la celda indicada en el parámetro num
	 * 
	 * @param celda La dirección de el fichero que contiene toda la celda
	 * @param num   La celda a cargar
	 * @return Una celda int[][] sacada del indice correspondiente de un fichero
	 */
	public void cargarCelda(String celda, int num) {
		// Creo un array donde se guardarán los números
		int[][] map = new int[16][12];

		try {
			// Leo el fichero
			Scanner sc = new Scanner(new FileInputStream(celda));
			while (sc.hasNextLine()) {
				// Si la linea actual equivale al indice que busco, la escaneo
				if (sc.nextLine().strip().equals("-"+num+"-")) {
					for (int i = 0; i < 12; i++) {
						String linea = new String(sc.nextLine());
						// Divido cada linea en sus números y los voy metiendo al array
						try {
							StringTokenizer st = new StringTokenizer(linea, ";");
							for (int j = 0; j < 16; j++) {
								map[j][i] = Integer.parseInt(st.nextToken());
							}
						} catch (NumberFormatException e) {
							e.printStackTrace();
						}
					}
					break;
				}
			}
			sc.close();
			System.out.println(celda);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		setCelda(map);
	}

	/**
	 * Este metodo se ocupa de dibujar la celda actual del mapa en pantalla.
	 * 
	 * @param g             El objeto Graphics2D que se va a utilizar.
	 * @param tamanoBaldosa El tamaño de cada baldosa del juego.
	 */
	public void dibujarCelda(Graphics2D g, int tamanoBaldosa) {
		// Recorro todo el array que contienen la celda
		
			for (int i = 0; i < 12; i++) {
				for (int j = 0; j < 16; j++) {
					if (celda[j][i]<=15) {
						try {
							BufferedImage texturaCasilla = ImageIO.read(getClass().getResourceAsStream("/texturas/texMapa/"+celda[j][i]+".png"));
							g.drawImage(texturaCasilla,j*tamanoBaldosa,i*tamanoBaldosa, tamanoBaldosa,tamanoBaldosa,null);
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						switch (celda[j][i]) {
						// Por cada celda, dependiendo de su número, voy dibujando bloques donde
						// corresponde
						
						case 20,21,22,23,24:
							g.setColor(Color.MAGENTA);
							break;
						case 30,31,32,33,35,36,37,38,39,40,41,42,43,44,45,46:
							g.setColor(Color.ORANGE.darker());
							break;
						case 60:
							g.setColor(Color.white);
							npcs.add(new NPC(i, j, new String[] {
									"hola soy un NPC",
									"bienvenido al juego "
							}));
							g.fillRect(j * tamanoBaldosa, i * tamanoBaldosa, tamanoBaldosa, tamanoBaldosa);
							g.setColor(Color.WHITE);
							
							break;
						}
					}
					
					
					
				}
			}
	}
		

	/**
	 * Este método detecta cuando un jugador se sale de la celda actual y lo
	 * transporta a la siguiente (vertical +10/-10, horizontal +1/-1) Alterando las
	 * coordenadas del eje que sea necesario para que la posición del jugador sea
	 * coherente con la anterior
	 * 
	 * @param personaje El personaje para el que se va a detectar el cambio de celda
	 */
	public void detectarCambio(Jugador jugador, int tamanoBaldosa) {
		boolean hayCambio = false;
		// Se intenta que cuando el jugador sale de una celda siempre quede
		// la mitad del personaje fuera y la mitad dentro (IMPORTANTE tener en cuenta
		// que ni las coordenadas del mapa ni las del jugador tienen el 0,0 en el centro)
		// Si el jugador se sale por el eje X se suma/resta al numCelda 1, y en el caso del eje Y,
		// 10.
		if (jugador.getX() > 992) {
			numCelda = numCelda + 10;
			hayCambio = true;
			jugador.setX(-32);
		} else if (jugador.getX() < -32) {
			numCelda = numCelda - 10;
			hayCambio = true;
			jugador.setX(992);
		} else if (jugador.getY() > 736) {
			numCelda = numCelda - 1;
			hayCambio = true;
			jugador.setY(-32);
		} else if (jugador.getY() < -32) {
			numCelda = numCelda + 1;
			hayCambio = true;
			jugador.setY(736);
		}
		// Si se ha detectado algún cambio, se cambia la celda a la que corresponda
		if (hayCambio) {
			cargarCelda(jugador.getArchivoACargar(), numCelda);
			updateMapa(tamanoBaldosa);
		}
	}
	
	public void updateMapa(int tamanoBaldosa) {
		Graphics2D g = i.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		dibujarCelda(g, tamanoBaldosa);
		g.drawString(""+numCelda, 40, 40);
	}
	public void dibujarImagen(Graphics2D g, int tamanoBaldosa){
		//g.drawImage(i,0,0, tamanoBaldosa*16,tamanoBaldosa*12,null);
		g.drawImage(i, 0, 0, null);
	}
	
	public List<NPC> getNpcs(){
		return npcs;
	}
	
	

}
