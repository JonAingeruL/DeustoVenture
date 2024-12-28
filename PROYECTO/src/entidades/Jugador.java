package entidades;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import main.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;

import gui.GameOverScreen;
import gui.InventarioCofre;

public class Jugador extends Personaje {
	GamePanel gp;
	ManejoTeclado maneT;
	private boolean estaDentroDeMazmorra = true;
	private String archivoACargar = "resources/mapas/tutorial.txt";
	private BufferedImage corazonVida, corazonSinVida, corazonAMedias, espada;
	private boolean[] vidas = { true, true, true, true, true, true };
	boolean teclaProcesadaNPC = false;
	boolean atacando = false;
	boolean interaccionDisponible = false;
	private int enemigosDerrotados = 0;

	public int getEnemigosDerrotados() {
		return enemigosDerrotados;
	}

	public void setEnemigosDerrotados(int enemigosDerrotados) {
		this.enemigosDerrotados = enemigosDerrotados;
	}
	long cooldownAtaque = 0; // De momento, cooldown servirá para almacenar en qué momento
	// e ataca. Con esto se puede calcular cuanto tiempo ha pasado
	// TODO Método que cierre todo lo que se abra al cerrar el juego, tanto en
	// esta clase como en las demás.
	AudioPlayer sword = new AudioPlayer("resources/audio/Sword.wav");

	public Jugador(GamePanel gp, ManejoTeclado maneT) {

		this.gp = gp;
		this.maneT = maneT;

		valoresDefault();
		conseguirImagenJugador();
	}

	public void valoresDefault() {
		x = 475;
		y = 400;
		velocidad = 5;
		direccion = "abajo";

	}

	public void conseguirImagenJugador() {

		try {

			arriba1 = ImageIO.read(getClass().getResourceAsStream("/texturas/texJugador/arribaPers1.png"));
			arriba2 = ImageIO.read(getClass().getResourceAsStream("/texturas/texJugador/arribaPers2.png"));
			arriba3 = ImageIO.read(getClass().getResourceAsStream("/texturas/texJugador/arribaPers3.png"));
			abajo1 = ImageIO.read(getClass().getResourceAsStream("/texturas/texJugador/abajoPers1.png"));
			abajo2 = ImageIO.read(getClass().getResourceAsStream("/texturas/texJugador/abajoPers2.png"));
			abajo3 = ImageIO.read(getClass().getResourceAsStream("/texturas/texJugador/abajoPers3.png"));
			derecha1 = ImageIO.read(getClass().getResourceAsStream("/texturas/texJugador/derechaPers1.png"));
			derecha2 = ImageIO.read(getClass().getResourceAsStream("/texturas/texJugador/derechaPers2.png"));
			derecha3 = ImageIO.read(getClass().getResourceAsStream("/texturas/texJugador/derechaPers3.png"));
			izquierda1 = ImageIO.read(getClass().getResourceAsStream("/texturas/texJugador/izquierdaPers1.png"));
			izquierda2 = ImageIO.read(getClass().getResourceAsStream("/texturas/texJugador/izquierdaPers2.png"));
			izquierda3 = ImageIO.read(getClass().getResourceAsStream("/texturas/texJugador/izquierdaPers3.png"));
			corazonVida = ImageIO.read(getClass().getResourceAsStream("/texturas/vida/corazonConVida.png")); // el
																												// corazon
																												// con
			// vida y su ruta
			corazonSinVida = ImageIO.read(getClass().getResourceAsStream("/texturas/vida/CorazonSinVida.png")); // el
																												// corazon
																												// sin
			// vida y su
			// ruta
			corazonAMedias = ImageIO.read(getClass().getResourceAsStream("/texturas/vida/CorazonAMedias.png")); // El
																												// corazón
																												// a
																												// medias
			espada = ImageIO.read(getClass().getResourceAsStream("/texturas/texInventario/espada.png"));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Aqui dentro vamos a cambiar la posicion del personaje
	// Hay que recordar que en java la esquina superior izq tiene la cordenada 0,0
	/**
	 * Esta funcion controla el movimiento del personaje jugable según la
	 * interacción con el teclado
	 * 
	 * @param tecladoM El objeto manejoTeclado a utilizar
	 */
	public void movimiento(ManejoTeclado tecladoM, Mapa mapa, int tamanoBaldosa,
			HashMap<String, ArrayList<Enemigo>> enemigos) {
		// Dependiendo de la tecla pulsada, muevo al jugador en la dirección
		// correspondiente.
		// Después compruebo si hay colisión con el mapa, y en ese caso revierto el
		// movimiento lo que haga falta
		// para corregirla.
		// También compruebo si se aprieta shift para aplicar un sprint
		

		if (tecladoM.arribaPulsado == true || tecladoM.abajoPulsado == true || tecladoM.izquierdaPulsado == true
				|| tecladoM.derechaPulsado == true) {
			interaccionDisponible = false;
			if (tecladoM.shiftPulsado == true) {
				velocidad += 2;
			}
			if (tecladoM.arribaPulsado == true) {
				direccion = "arriba";
				y -= velocidad;
				while (detectaColision(mapa, tamanoBaldosa) || detectaColisionEnemigos(enemigos, tamanoBaldosa, mapa)) {
					y += 1;
				}
			} else if (tecladoM.abajoPulsado == true) {
				direccion = "abajo";
				y += velocidad;
				while (detectaColision(mapa, tamanoBaldosa) || detectaColisionEnemigos(enemigos, tamanoBaldosa, mapa)) {
					y -= 1;
				}
			} else if (tecladoM.izquierdaPulsado == true) {
				direccion = "izquierda";
				x -= velocidad;
				while (detectaColision(mapa, tamanoBaldosa) || detectaColisionEnemigos(enemigos, tamanoBaldosa, mapa)) {
					x += 1;
				}
			} else if (tecladoM.derechaPulsado == true) {
				direccion = "derecha";
				x += velocidad;
				while (detectaColision(mapa, tamanoBaldosa) || detectaColisionEnemigos(enemigos, tamanoBaldosa, mapa)) {
					x -= 1;
				}
			}
			if (tecladoM.shiftPulsado == true) {
				velocidad -= 2;
			}

			contadorSprites++;
			if (contadorSprites > 12) {
				if (numSprite == 1) {
					numSprite = 2;
				} else if (numSprite == 2) {
					numSprite = 3;
				} else if (numSprite == 3) {
					numSprite = 1;
				}
				contadorSprites = 0;
			}
		}

	}

	public void dibujarPer(Graphics2D g2) {
		// g2.setColor(Color.white);

		// g2.fillRect(x, y, gp.tamañoBaldosa, gp.tamañoBaldosa);

		BufferedImage imagen = null;

		switch (direccion) {
		case "arriba":
			if (numSprite == 1) {
				imagen = arriba1;
			} else if (numSprite == 2) {
				imagen = arriba2;

			} else if (numSprite == 3) {
				imagen = arriba3;

			}
			//Descomentar para ver colisiones ataque
			//g2.fillRect(x, y - 20, 20, 60);
			if (atacando) {
				g2.drawImage(espada, x, y - 20, 20, 60, null);
			}
			break;
		case "abajo":
			if (numSprite == 1) {
				imagen = abajo1;
			} else if (numSprite == 2) {
				imagen = abajo2;

			} else if (numSprite == 3) {
				imagen = abajo3;
			}
			if (atacando) {
				g2.drawImage(espada, x + 40, y + 100, 20, -60, null);
			}
			break;
		case "izquierda":
			if (numSprite == 1) {
				imagen = izquierda1;
			} else if (numSprite == 2) {
				imagen = izquierda2;

			} else if (numSprite == 3) {
				imagen = izquierda3;
			}
			//g2.fillRect(x + -50, y + 25, 60, 20);
			if (atacando) {
				g2.drawImage(espada, x + 10, y + 25, -60, 20, null);
			}
			break;
		case "derecha":
			if (numSprite == 1) {
				imagen = derecha1;
			} else if (numSprite == 2) {
				imagen = derecha2;

			} else if (numSprite == 3) {
				imagen = derecha3;
			}
			//g2.fillRect(x + 40, y + 25, 60, 20);
			if (atacando) {
				g2.drawImage(espada, x + 40, y + 25, 60, 20, null);
			}
			break;
		}
		g2.drawImage(imagen, x, y, gp.tamañoBaldosa, gp.tamañoBaldosa, null);
	}

	/**
	 * Dibuja las vidas como corazones en pantalla
	 * 
	 * @param g2 El objeto graphics 2d a utilizar
	 */
	public void dibujarVidas(Graphics2D g2) {
		for (int i = 0; i < vidas.length; i += 2) {
			if (vidas[i] && vidas[i + 1]) {
				g2.drawImage(corazonVida, 80 + 35 * i / 2, 60, 40, 40, null);
			} else if (vidas[i] && !vidas[i + 1]) {
				g2.drawImage(corazonAMedias, 80 + 35 * i / 2, 60, 40, 40, null);
			} else {
				// TODO: Aparentemente, el sprite de corazon sin vida no está bien alineado
				// (está más
				// a la derecha que el sprite de corazón con vida). Estaría bien alinearlo
				// correctamente
				g2.drawImage(corazonSinVida, 80 + 38 * i / 2, 60, 40, 40, null);
			}
		}
	}
	
	public void dibujarInteraccion(Graphics2D g2) {
		if(interaccionDisponible) {
			g2.fillRoundRect(150, 650, 75, 60, 10, 10);
			g2.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
			g2.setColor(Color.WHITE);
			g2.drawString("E", 175, 690);
		}
	}

	public boolean[] getVidas() {
		return vidas;
	}

	/**
	 * Establece el número de vidas y su estado. AVISO: Es preferible utilizar el
	 * método cambiarVidas para asegurar que el dibujado sea correcto. Utilizar este
	 * método sólo para alterar vidas máximas
	 * 
	 * @param vidas Un array booleano que representa la cantidad de corazones y su
	 *              estado
	 */
	public void setVidas(boolean[] vidas) {
		if (!((vidas.length) % 2 == 0)) {
			boolean[] vidasCorregido = new boolean[vidas.length + 1];
			vidasCorregido[vidasCorregido.length - 1] = false;
			System.arraycopy(vidas, 0, vidasCorregido, 0, vidas.length);
			this.vidas = vidasCorregido;
		} else {
			this.vidas = vidas;
		}
	}

	/**
	 * Este método aumenta o reduce el número de vidas según el entero que recibe
	 * (Positivo aumenta negativo disminuye)
	 * 
	 * @param value El entero que indica si aumentar o reducir las vidas
	 */
	public void cambiarVidas(int value) {
		if (value < 0) {
			for (int i = vidas.length - 1; i >= 0; i--) {
				if (vidas[i]) {
					vidas[i] = false;
					value += 1;
				}
				if (value == 0) {
					break;
				}
			}
		} else if (value > 0) {
			for (int i = 0; i < vidas.length; i++) {
				if (!vidas[i]) {
					vidas[i] = true;
					value -= 1;
				}
				if (value == 0) {
					break;
				}
			}
		}

	}

	/**
	 * Este método detecta colisiones entre el personaje y el mapa. También se ocupa
	 * de manejar el cambio de mapa por teletransporte. Devuelve un booleano.
	 * 
	 * @param mapa          El mapa que puede colisionar con el jugador
	 * @param tamanobaldosa El tamaño de cada baldosa del mapa
	 * @return
	 */
	public boolean detectaColision(Mapa mapa, int tamanobaldosa) {
		// se usa para saber en que celda esta el jugador
		int celdaX = (x + 32) / tamanobaldosa;
		if (x < -32) {
			celdaX = mapa.getCelda().length - 1;
		}
		int celdaY = (y + 32) / tamanobaldosa;
		if (y < -32) {
			celdaY = mapa.getCelda()[0].length - 1;
		}
		//System.out.println("Posicionjugador:" + celdaX + "," + celdaY + ", Posicion coords: " + x + "," + y);
		// Recorro el array celda completo

		for (int i = 0; i < mapa.getCelda().length; i++) {
			for (int j = 0; j < mapa.getCelda()[i].length; j++) {
				// Por cada baldosa no vacía, calcula si está colisionando de algún modo con el
				// personaje, USANDO LA LISTA QUE TENEMOS DE NUMEROS COLISIONABLES
				if (Mapa.zonasConColision.contains(mapa.getCelda()[i][j])) {
					if (((((x <= (i * tamanobaldosa) + tamanobaldosa) && (x >= i * tamanobaldosa))
							// Compruebo si hay colisión en X con la esquina derecha
							|| ((x + tamanobaldosa <= (i * tamanobaldosa) + tamanobaldosa)
									&& (x + tamanobaldosa >= i * tamanobaldosa)))
							// Compruebo si hay colisión en Y por arriba
							&& (((y <= (j * tamanobaldosa) + tamanobaldosa) && (y >= j * tamanobaldosa))
									// Compruebo si hay colisión en Y por abajo
									|| ((y + tamanobaldosa <= (j * tamanobaldosa) + tamanobaldosa)
											&& (y + tamanobaldosa >= j * tamanobaldosa))))) {
						System.out.println("Hay colision");
						if(mapa.getCelda()[i][j] == 50) {
							interaccionDisponible = true;
						}else {
							interaccionDisponible = false;
						}
						return true;
					}
				} else {
					// este switch analiza cada uno de los numeros que se han asignado para cada
					// portal
					// puesto en guia.txt NO ESTÁ COMPLETO, SOLO EL DE DEL TUTORIAL AL MAPA
					// PRINCIPAL
					// TODO Completar este código
					if (celdaX == i && celdaY == j) {
						switch (mapa.getCelda()[i][j]) {

						case 0:

							break;

						case 20:
							System.out.println("20");
							if (this.estaDentroDeMazmorra) {
								mapa.setNumcelda(1);
								mapa.setNumeroMapa(1);
								archivoACargar = "resources/mapas/mapa.txt";
								mapa.cargarCelda(archivoACargar, mapa.getNumcelda());
								mapa.updateMapa(tamanobaldosa);
								this.estaDentroDeMazmorra = false;
								// POSICIONES NUEVAS PARA DESPUES CARGAR EL MAPA
								x = 450;
								y = 300;
							} else {
								mapa.setNumcelda(4);
								mapa.setNumeroMapa(0);
								archivoACargar = "resources/mapas/tutorial.txt";
								mapa.cargarCelda(archivoACargar, mapa.getNumcelda());
								mapa.updateMapa(tamanobaldosa);
								x = 705;
								y = 241;
								this.estaDentroDeMazmorra = true;

							}
							break;

						case 21:
							if (!estaDentroDeMazmorra) {

								archivoACargar = "resources/mapas/dungeon1.txt";
								mapa.setNumcelda(1);
								mapa.setNumeroMapa(2);
								mapa.cargarCelda(archivoACargar, mapa.getNumcelda());
								mapa.updateMapa(tamanobaldosa);
								x = 450;
								y = 300;
								estaDentroDeMazmorra = true;
							} else {
								mapa.setNumcelda(34);
								mapa.setNumeroMapa(1);
								archivoACargar = "resources/mapas/mapa.txt";
								mapa.cargarCelda(archivoACargar, mapa.getNumcelda());
								mapa.updateMapa(tamanobaldosa);
								x = 450;
								y = 160;
								estaDentroDeMazmorra = false;
							}
							break;

						case 22:
							if (!estaDentroDeMazmorra) {
								archivoACargar = "resources/mapas/dungeon2.txt";
								mapa.setNumcelda(1);
								mapa.setNumeroMapa(3);
								mapa.cargarCelda(archivoACargar, mapa.getNumcelda());
								mapa.updateMapa(tamanobaldosa);
								x = 450;
								y = 300;
								estaDentroDeMazmorra = true;
							} else {
								archivoACargar = "resources/mapas/mapa.txt";
								mapa.setNumcelda(94);
								mapa.setNumeroMapa(1);
								mapa.cargarCelda(archivoACargar, mapa.getNumcelda());
								mapa.updateMapa(tamanobaldosa);
								x = 500;
								y = 380;
								estaDentroDeMazmorra = false;
							}
							break;
						case 23:
							if (!estaDentroDeMazmorra) {

								archivoACargar = "resources/mapas/dungeon3.txt";
								mapa.setNumcelda(1);
								mapa.setNumeroMapa(4);
								mapa.cargarCelda(archivoACargar, mapa.getNumcelda());
								mapa.updateMapa(tamanobaldosa);
								x = 450;
								y = 300;
								estaDentroDeMazmorra = true;
							} else {
								archivoACargar = "resources/mapas/mapa.txt";
								mapa.setNumcelda(56);
								mapa.setNumeroMapa(1);
								mapa.cargarCelda(archivoACargar, mapa.getNumcelda());
								mapa.updateMapa(tamanobaldosa);
								x = 450;
								y = 270;
								estaDentroDeMazmorra = false;
							}
							break;
						case 24:
							if (!estaDentroDeMazmorra) {
								archivoACargar = "resources/mapas/casa.txt";
								mapa.setNumcelda(1);
								mapa.setNumeroMapa(5);
								mapa.cargarCelda(archivoACargar, mapa.getNumcelda());
								mapa.updateMapa(tamanobaldosa);
								x = 450;
								y = 300;
								estaDentroDeMazmorra = true;
							} else {
								archivoACargar = "resources/mapas/mapa.txt";
								mapa.setNumcelda(87);
								mapa.setNumeroMapa(1);
								mapa.cargarCelda(archivoACargar, mapa.getNumcelda());
								mapa.updateMapa(tamanobaldosa);
								x = 780;
								y = 370;
								estaDentroDeMazmorra = false;

							}
							break;
						}
					}
				}
			}
		}

		return false;
	}

	public boolean detectaColisionEnemigos(HashMap<String, ArrayList<Enemigo>> enemigos, int tamanobaldosa, Mapa mapa) {
		if (enemigos.containsKey(mapa.getNumeroMapa() + "," + mapa.getNumcelda())) {
			for (Enemigo enemigo : enemigos.get(mapa.getNumeroMapa() + "," + mapa.getNumcelda())) {
				if (((((enemigo.getX() <= (this.getX()) + tamanobaldosa) && (enemigo.getX() >= this.getX()))
						// Compruebo si hay colisión en X con la esquina derecha
						|| ((enemigo.getX() + tamanobaldosa <= this.getX() + tamanobaldosa)
								&& (enemigo.getX() + tamanobaldosa >= this.getX())))
						// Compruebo si hay colisión en Y por arriba
						&& (((enemigo.getY() <= this.getY() + tamanobaldosa) && (enemigo.getY() >= this.getY()))
								// Compruebo si hay colisión en Y por abajo
								|| ((enemigo.getY() + tamanobaldosa <= this.getY() + tamanobaldosa)
										&& (enemigo.getY() + tamanobaldosa >= this.getY()))))) {
					return true;
				}
			}
			return false;
		}
		return false;

	}

	public void dibujarDialogoPantalla(Graphics2D g2, Mapa mapa) {// hago 2 metodos pq hay diferentes npcs
//      dialogoJL.setBounds(0, maxPantallaFila - maxPantallaFila/3, maxPantallaColu, maxPantallaFila/3); // Ajustar posición y tamaño.

//		el panel de texto lo coloco abajo en el centro

//		int x = 15; 
//		int y = (gp.maxPantallaFila - gp.maxPantallaFila/3) * gp.tamañoBaldosa - 15; //hay q * para pasarlo a la unidad correcta
//		int ancho = gp.maxPantallaColu * (gp.tamañoBaldosa -2); 
//		int largo = gp.maxPantallaFila/3 * gp.tamañoBaldosa; 
//		
//		//llama al metodo de abajo y le t¡mete los datos para q haga el rectangulo, lo hago en 2 metodo pq hay varios npcs
//		dibujarSubPantalla(x, y, ancho, largo, g2); 
//		
//		x+= gp.maxPantallaFila;
//		y+= gp.maxPantallaColu + 20; //para que el texto encaje bien
//		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32F)); //poner la fuente de la letra
//		// esto lo uso para hacer pruebas, no es correcto ya que hace un buble infito pero es una forma de ver que el texto se muestra
//		for(NPC npc: mapa.getNpcs()) { 
//			g2.drawString(npc.hablar(), x, y);
//			
//		//hay q hecer un for q interprete el salto de linea pq en este caso no lo hace automatico		
//			for(String linea: npc.hablar().split("\n")) { 
//				g2.drawString(npc.hablar(), x, y);
//				y += 40;
//			}
//			
//		}
	}

	public void dibujarSubPantalla(int x, int y, int ancho, int largo, Graphics2D g2) {
		Color c = new Color(0, 0, 0, 200); // el 200 le aporta transparencia, cuanto mas bajo mas transparencia, va del
											// 0 al 255
		g2.setColor(c);
		g2.fillRoundRect(x, y, ancho, largo, 20, 20);// dibujamos un recatangulo el 20 es para cambiar la redondez del
														// rectangulo

		c = new Color(255, 255, 255, 200); // el 200 le aporta transparencia
		g2.setStroke(new BasicStroke(5)); // para hecerle un reborde 5 es su anchura
		g2.setColor(c);
		g2.drawRoundRect(x + 5, y + 5, ancho - 10, largo - 10, 10, 10); // saca el reboerde

	}

	public void InteractuarNPC(Mapa mapa, int tamanobaldosa, ManejoTeclado tecladoM) {

//		int celdaX = (x + 32) / tamanobaldosa;
//		if (x < -32) {
//			celdaX = mapa.getCelda().length - 1;
//		}
//		int celdaY = (y + 32) / tamanobaldosa;
//		if (y < -32) {
//			celdaY = mapa.getCelda()[0].length - 1;
//		}
		for (NPC npc : mapa.getNpcs()) {
//			if(Math.abs(celdaX - npc.getX()) <= 1 && Math.abs(celdaY - npc.getY()) <= 1 ) { //no funciona por que hay algo que lo pisa, pero el calculo esta bien
//				System.out.println("deberia funcionar");

			if (tecladoM.hablarNPCPulsado) { // interactua con la tecla E
				if (!teclaProcesadaNPC) {
					teclaProcesadaNPC = true;
					npc.hablar();
				}
			} else {
				teclaProcesadaNPC = false;
				break;
			}
		}
//			}else {
//				System.out.println("coordenada x " + npc.getX() +" coordenada y "+ npc.getY());
//			}
	}

	public String getArchivoACargar() {
		return archivoACargar;
	}

	public void setArchivoACargar(String archivoACargar) {
		this.archivoACargar = archivoACargar;
	}

	public void AccionAtacar(HashMap<String, ArrayList<Enemigo>> enemigos, Mapa mapa, int tamanobaldosa, ManejoTeclado tecladoM, GamePanel gamePanel) {
		//TODO que el enemigo reciba daño una sola vez
		if ((tecladoM.fPulsado == true) && (atacando == false)) {
			// Establecemos en qué mopmento hemos atacado
			cooldownAtaque = System.currentTimeMillis();
			sword.playClip(gp.getVolumenAudio());
			System.out.println("Sword");
			atacando = true;
			DanoAtaque(enemigos, tamanobaldosa, mapa);
		} else {
			// Si se estaba esperando al cooldown y ya ha pasado. Se pasa el cooldown (1) de
			// segundos a milis
			if ((cooldownAtaque != 0) && (System.currentTimeMillis() - cooldownAtaque > 1 * 1000)) {
				System.out.println("Fin ataque");
				// Dejamos de atacar
				cooldownAtaque = 0;
				atacando = false;
			}
		}
	}
	
	public void DanoAtaque(HashMap<String, ArrayList<Enemigo>> enemigos, int tamanobaldosa, Mapa mapa) {
		//Creo un array con los datos de la caja de colisiones de la espada.
		//Dependiendo de la dirección en la que mira el jugador, la caja es diferente
		int[] posicionEspada = null;
		switch (direccion) {
		case "arriba":
			posicionEspada = new int[]{x, y - 20, 20, 60};
			break;
		case "derecha":
			posicionEspada = new int[]{x + 40, y + 25, 60, 20};
			break;
		case "izquierda":
			posicionEspada = new int[]{x + -50, y + 25, 60, 20};
			break;
		case "abajo":
			posicionEspada = new int[]{ x + 40, y +40, 20, 60,};
			break;
			
		}
		if (enemigos.containsKey(mapa.getNumeroMapa() + "," + mapa.getNumcelda())) {
			//TODO Que más de un enemigo pueda recibir daño
			Enemigo enemigoAElim=null;
			ArrayList<Enemigo> enemigosAElim = new ArrayList<Enemigo>();
			for (Enemigo enemigo : enemigos.get(mapa.getNumeroMapa() + "," + mapa.getNumcelda())) {
				if (((((posicionEspada[0] <= enemigo.getX() + tamanobaldosa) && (posicionEspada[0] >= enemigo.getX()))
						// Compruebo si hay colisión en X con la esquina derecha
						|| ((posicionEspada[0] + posicionEspada[2] <= enemigo.getX() + tamanobaldosa)
								&& (posicionEspada[0] + posicionEspada[2] >= enemigo.getX())))
						// Compruebo si hay colisión en Y por arriba
						&& (((posicionEspada[1] <= enemigo.getY() + tamanobaldosa) && (posicionEspada[1] >= enemigo.getY()))
								// Compruebo si hay colisión en Y por abajo
								|| ((posicionEspada[1] + posicionEspada[3] <= enemigo.getY() + tamanobaldosa)
										&& (posicionEspada[1] + posicionEspada[3] >= enemigo.getY() + tamanobaldosa))))) {
					System.out.println("Daño");
					enemigoAElim=enemigo;
					enemigosAElim.add(enemigoAElim);
				}
			}
			if(null!=enemigoAElim) {
				for (Enemigo enemigo : enemigosAElim) {
					enemigo.setVida(enemigoAElim.getVida()-1);
					if(enemigo.getVida()<=0) {
						this.setEnemigosDerrotados(this.getEnemigosDerrotados()+1);
						enemigos.get(mapa.getNumeroMapa() + "," + mapa.getNumcelda()).remove(enemigo);
					}
				}
			}else {
				System.out.println("No hay daño");
			}
		}

	}
	public void interaccion(ManejoTeclado mt, Mapa mapa) {
		if(this.interaccionDisponible && mt.hablarNPCPulsado) {
			new InventarioCofre(mt,archivoACargar,mapa.getNumcelda());
			mt.hablarNPCPulsado = false;

		}
	}
	public void muerte(GamePanel gp, Thread t) {
		if(vidas[0] == false) {
			MusicPlayer.stopMusic();
			t.interrupt();
			new GameOverScreen(getEnemigosDerrotados());
		}
	}

}
