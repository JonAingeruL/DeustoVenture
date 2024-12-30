package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;
import javax.imageio.ImageIO;
import entidades.Jugador;

public class Mapa {
	private int[][] celda;
	private int numCelda;
	private String archivoACargar = "resources/mapas/tutorial.txt";
	private BufferedImage i = new BufferedImage(1024, 768, BufferedImage.TYPE_INT_ARGB);
	// en caso de que se quieran añadir más números que tengan colision, se añaden a
	// esta lista
	public static List<Integer> zonasConColision = List.of(1, 4, 6, 7, 9, 10, 11, 12, 13, 15, 16, 17, 30, 31, 32, 33, 35, 36,
			37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 50, 60);
	
	//Esta variable sirve para controlar el nº de mapa AL CARGAR ENEMIGOS
	//valor 0 = tutorial.txt, valor 1 = mapa.txt, valor 2 = dungeon1.txt, valor 3 = dungeon2.txt, valor 4= dungeon3.txt
	//valor 5 = casa.txt
	private int numeroMapa;

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
				if (sc.nextLine().strip().equals("-" + num + "-")) {
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
	 * Este metodo se ocupa de dibujar la celda actual del mapa.
	 * También gira de forma aleatoria las texturas que se pueden girar para evitar homogeneidad
	 * @param g             El objeto Graphics2D que se va a utilizar.
	 * @param tamanoBaldosa El tamaño de cada baldosa del juego.
	 */
	public void dibujarCelda(Graphics2D g, int tamanoBaldosa) {
		//En estas listas metemos las texturas que se puedan rotar en el eje x o en el x e y.
		List<Integer> flipeableX = Arrays.asList(0,1,2,4,5,6,7,8,14,15);
		List<Integer> flipeableY = Arrays.asList(0,4,5,7,8,14,15);
		// Recorro todo el array que contienen la celda
			for (int i = 0; i < 12; i++) {
				for (int j = 0; j < 16; j++) {
					//Dibujamos las texturas que ya tenemos
					if (celda[j][i]<=15 && !(celda[j][i]==3)) {
						try {
							//Creo un randomizador
							Random rand = new Random();
							BufferedImage texturaCasilla = ImageIO.read(getClass().getResourceAsStream("/texturas/texMapa/"+celda[j][i]+".png"));
							//Dibujo la imagen girada, dependiendo de en qué ejes hemos establecido que se puede girar
							//Si el random da = 0, no se gira, y si da = 1 se gira.
							//hago esto moviendo la imagen una baldosa en el eje que se va a girar e invirtiendo el dibujado (tamaño del eje en negativo)
							if (flipeableX.contains(celda[j][i])) {
								int flipX = rand.nextInt(0, 2);
								if (flipeableY.contains(celda[j][i])) {
									int flipY = rand.nextInt(0, 2);
									g.drawImage(texturaCasilla,j*tamanoBaldosa+tamanoBaldosa*flipX,i*tamanoBaldosa+tamanoBaldosa*flipY, tamanoBaldosa-tamanoBaldosa*2*flipX,tamanoBaldosa-tamanoBaldosa*2*flipY,null);
								}else {
									g.drawImage(texturaCasilla,j*tamanoBaldosa+tamanoBaldosa*flipX,i*tamanoBaldosa, tamanoBaldosa-tamanoBaldosa*2*flipX,tamanoBaldosa,null);
									}
								}else {
									//Si la textura no se puede girar en ningún eje, la dibujo sin transformaciones de posición o tamaño.
									g.drawImage(texturaCasilla,j*tamanoBaldosa,i*tamanoBaldosa, tamanoBaldosa,tamanoBaldosa,null);
								}
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						switch (celda[j][i]) {
						// Por cada celda, dependiendo de su número, voy dibujando bloques donde
						// corresponde
						//Estos dibujos son la representación temporal de los materiales que todavía no tienen textura
						case 3, 16,17,18,19:
							g.setColor(Color.GREEN);
							break;
						
						case 20,21,22,23,24:
							g.setColor(Color.MAGENTA);
							break;
						case 30,31,32,33,35,36,37,38,39,40,41,42,43,44,45,46:
							g.setColor(Color.ORANGE.darker());
							break;
						case 50:
							g.setColor(Color.WHITE.darker());
							break;
						case 60:
							g.setColor(Color.white);
							break;

						}
						g.fillRect(j * tamanoBaldosa, i * tamanoBaldosa, tamanoBaldosa, tamanoBaldosa);
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
		// que ni las coordenadas del mapa ni las del jugador tienen el 0,0 en el
		// centro)
		// Si el jugador se sale por el eje X se suma/resta al numCelda 1, y en el caso
		// del eje Y,
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
			//Se actualiza también la imagen a dibujar del mapa para que coincida con la celda actual
			updateMapa(tamanoBaldosa);
		}
	}

	/**
	 * Este método se ocupa de actualizar la imágen del mapa que se dibuja en
	 * pantalla según la celda actual.
	 * 
	 * @param tamanoBaldosa El tamaño que tendrá cada baldosa del mapa.
	 */
	public void updateMapa(int tamanoBaldosa) {
		Graphics2D g = i.createGraphics();
		dibujarCelda(g, tamanoBaldosa);
		g.setColor(Color.WHITE);
		g.drawString("" + numCelda, 40, 40);
	}

	/**
	 * Este método dibuja la imagen del mapa en pantalla.
	 * 
	 * @param g Graphics2d a utilizar.
	 */
	public void dibujarImagen(Graphics2D g) {
		g.drawImage(i, 0, 0, null);
	}

	
	public int getNumeroMapa() {
		return numeroMapa;
	}

	public void setNumeroMapa(int numeroMapa) {
		this.numeroMapa = numeroMapa;
	}
	
}
