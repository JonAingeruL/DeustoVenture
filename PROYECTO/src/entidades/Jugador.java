package entidades;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import main.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;

import gui.GameOverScreen;
import gui.Inventario;
import gui.InventarioCofre;
import gui.Mensaje;
import gui.NPC2;

public class Jugador extends Personaje {
	GamePanel gp;
	ManejoTeclado maneT;
	private boolean estaDentroDeMazmorra = true;
	private String archivoACargar = "resources/mapas/tutorial.txt";
	private BufferedImage corazonVida, corazonSinVida, corazonAMedias, espada;
	private boolean[] vidas = { true, true, true, true, true, true };
	private boolean atacando = false;
	boolean interaccionDisponible = false;
	public boolean hablarConNPC = false;
	private HashMap<String, Integer> inventario = new HashMap<String, Integer>();


	public String objetoEnMano = "";
	public int danoJugador = 0;
	
	//guia numMapa
	//mapa tutorial =0 ; mapa principal =1 ;mapa dungeon1 = 2;  mapa dungeon2 = 3;  mapa dungeon3 =4 ; mapa casa =5;
	
	//estadísticas jugador
	private String nombreJugador;
	private int enemigosDerrotados;
	private int numMuertes;
	private int tiempoJugado;
	
	
	
	public int getNumMuertes() {
		return numMuertes;
	}

	public void setNumMuertes(int numMuertes) {
		this.numMuertes = numMuertes;
	}

	public int getTiempoJugado() {
		return tiempoJugado;
	}

	public void setTiempoJugado(int tiempoJugado) {
		this.tiempoJugado = tiempoJugado;
	}

	public String getNombreJugador() {
		return nombreJugador;
	}

	public void setNombreJugador(String nombreJugador) {
		this.nombreJugador = nombreJugador;
	}

	public int getEnemigosDerrotados() {
		return enemigosDerrotados;
	}

	public HashMap<String, Integer> getInventario() {
		return inventario;
	}

	public void setInventario(HashMap<String, Integer> inventario) {
		this.inventario = inventario;
	}

	public void setEnemigosDerrotados(int enemigosDerrotados) {
		this.enemigosDerrotados = enemigosDerrotados;
	}
	long cooldownAtaque = 0; // De momento, cooldown servirá para almacenar en qué momento
	// e ataca. Con esto se puede calcular cuanto tiempo ha pasado
	// TODO Método que cierre todo lo que se abra al cerrar el juego, tanto en
	// esta clase como en las demás.
	private AudioPlayer sword = new AudioPlayer("resources/audio/Sword.wav");
	private AudioPlayer damage = new AudioPlayer("resources/audio/PlayerDmg.wav");
	private AudioPlayer heal = new AudioPlayer("resources/audio/Heal.wav");

	public Jugador(GamePanel gp, ManejoTeclado maneT) {

		this.gp = gp;
		this.maneT = maneT;

		valoresDefault();
		conseguirImagenJugador();
		String nombreFich = "src/inventario.txt";
		Inventario.inicializarInventarioPrueba();
        setInventario(leerFichero(nombreFich));
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
			HashMap<String, ArrayList<Enemigo>> enemigos, HashMap<String, ArrayList<NPC>> npcs) {
		// Dependiendo de la tecla pulsada, muevo al jugador en la dirección
		// correspondiente.
		// Después compruebo si hay colisión con el mapa, y en ese caso revierto el
		// movimiento lo que haga falta
		// para corregirla.
		// También compruebo si se aprieta shift para aplicar un sprint
		

		if (tecladoM.arribaPulsado == true || tecladoM.abajoPulsado == true || tecladoM.izquierdaPulsado == true
				|| tecladoM.derechaPulsado == true) {
			interaccionDisponible = false;
			hablarConNPC = false;
			if (tecladoM.shiftPulsado == true) {
				velocidad += 2;
			}
			if (tecladoM.arribaPulsado == true) {
				direccion = "arriba";
				y -= velocidad;
				while (detectaColision(mapa, tamanoBaldosa) || detectaColisionEnemigos(enemigos, tamanoBaldosa, mapa)|| detectaColisionNpcs(npcs, tamanoBaldosa, mapa)) {
					y += 1;
				}
			} else if (tecladoM.abajoPulsado == true) {
				direccion = "abajo";
				y += velocidad;
				while (detectaColision(mapa, tamanoBaldosa) || detectaColisionEnemigos(enemigos, tamanoBaldosa, mapa)|| detectaColisionNpcs(npcs, tamanoBaldosa, mapa)) {
					y -= 1;
				}
			} else if (tecladoM.izquierdaPulsado == true) {
				direccion = "izquierda";
				x -= velocidad;
				while (detectaColision(mapa, tamanoBaldosa) || detectaColisionEnemigos(enemigos, tamanoBaldosa, mapa)|| detectaColisionNpcs(npcs, tamanoBaldosa, mapa)) {
					x += 1;
				}
			} else if (tecladoM.derechaPulsado == true) {
				direccion = "derecha";
				x += velocidad;
				while (detectaColision(mapa, tamanoBaldosa) || detectaColisionEnemigos(enemigos, tamanoBaldosa, mapa)|| detectaColisionNpcs(npcs, tamanoBaldosa, mapa)) {
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
		if (tecladoM.unopulsado) {
			archivoACargar="resources/mapas/tutorial.txt";
			cambioMapa(archivoACargar, 1, 0, tamanoBaldosa, 475, 400, true, mapa);
		}
		if (tecladoM.dospulsado) {
			archivoACargar="resources/mapas/mapa.txt";
			cambioMapa(archivoACargar, 1, 1, tamanoBaldosa, 320, 310, false, mapa);
		}
		if (tecladoM.trespulsado) {
			archivoACargar="resources/mapas/dungeon1.txt";
			cambioMapa(archivoACargar,1,2,tamanoBaldosa,212,370,true,mapa);
		}
		if(tecladoM.cuatropulsado) {
			archivoACargar="resources/mapas/mapa.txt";
			cambioMapa(archivoACargar, 31, 1, tamanoBaldosa, 50, 380, false, mapa);
		}
		if(tecladoM.cincopulsado) {
			archivoACargar="resources/mapas/dungeon2.txt";
			cambioMapa(archivoACargar, 1, 3, tamanoBaldosa, 180, 370, true, mapa);
		}
		if(tecladoM.seispulsado) {
			archivoACargar="resources/mapas/mapa.txt";
			cambioMapa(archivoACargar,85,1,tamanoBaldosa,460,360,false,mapa);
		}
		if(tecladoM.sietepulsado) {
			archivoACargar="resources/mapas/dungeon3.txt";
			cambioMapa(archivoACargar, 1, 4, tamanoBaldosa, 200, 380, true, mapa);
		}
		if (tecladoM.ochopulsado) {
			archivoACargar="resources/mapas/casa.txt";
			cambioMapa(archivoACargar, 1, 5, tamanoBaldosa, 180, 370, true, mapa);
		}
		if (tecladoM.nuevepulsado) {
			archivoACargar="resources/mapas/mapa.txt";
			cambioMapa(archivoACargar, 5, 1, tamanoBaldosa, 440, 40, false, mapa);
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
	public void dibujarInterfaz(Graphics2D g2) {
		//Primero dibujo las vidas
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
		//Después el arma equipada actualmente
		g2.setColor(new Color(0, 0, 0, 100));
		g2.fillRoundRect(760, 40, 240, 60, 5, 5);
		g2.setFont(new Font("TIMES NEW ROMAN",Font.BOLD,25));
		g2.setColor(Color.WHITE);
		g2.drawString(objetoEnMano, 780, 80);
	}
	
	public void dibujarInteraccion(Graphics2D g2) {
		if(interaccionDisponible) {
			g2.setColor(Color.DARK_GRAY);
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
	 * Dependiendo de si la vida aumenta o disminuye, reproduce un sonido u otro
	 * 
	 * @param value El entero que indica si aumentar o reducir las vidas
	 */
	public void cambiarVidas(int value) {
		if (value < 0) {
			damage.playClip(gp.getVolumenAudio());
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
			heal.playClip(gp.getVolumenAudio()*0.1f);
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
						if(mapa.getCelda()[i][j] == 50  || mapa.getCelda()[i][j] == 60) {
							interaccionDisponible = true;
						if (mapa.getCelda()[i][j] == 60) {
							hablarConNPC = true;
							}
						}else {
							interaccionDisponible = false;
							hablarConNPC = false;
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
							//entrada y salida tutorial
							System.out.println("20");
							if (this.estaDentroDeMazmorra) {
								archivoACargar = "resources/mapas/mapa.txt";		
								cambioMapa(archivoACargar, 1, 1, tamanobaldosa, 280, 300, false, mapa);
							} else {
								archivoACargar = "resources/mapas/tutorial.txt";
								cambioMapa(archivoACargar, 4, 0, tamanobaldosa, 705, 241, true, mapa);
							}
							break;

						case 21:
							//entrada y salida mazmorra 1
							if (!estaDentroDeMazmorra) {

								archivoACargar = "resources/mapas/dungeon1.txt";
								cambioMapa(archivoACargar, 1, 2, tamanobaldosa, 200, 380, true, mapa);
							} else {
								archivoACargar = "resources/mapas/mapa.txt";
								cambioMapa(archivoACargar, 34, 1, tamanobaldosa, 450, 160, false, mapa);
							}
							break;

						case 22:
							//entrada y salida mazmorra 2
							if (!estaDentroDeMazmorra) {
								archivoACargar = "resources/mapas/dungeon2.txt";
								cambioMapa(archivoACargar, 1, 3, tamanobaldosa, 200, 380, true, mapa);
							} else {
								archivoACargar = "resources/mapas/mapa.txt";
								cambioMapa(archivoACargar,94,1,tamanobaldosa,500,380,false,mapa);
							}
							break;
						case 23:
							//entrada y salida mazmorra 3
							if (!estaDentroDeMazmorra) {

								archivoACargar = "resources/mapas/dungeon3.txt";
								cambioMapa(archivoACargar,1,4,tamanobaldosa,200,380,true,mapa);
							} else {
								archivoACargar = "resources/mapas/mapa.txt";
								cambioMapa(archivoACargar,56,1,tamanobaldosa,450,270,false,mapa);
							}
							break;
						case 24:
							//entrada y salida casa
							if (!estaDentroDeMazmorra) {
								archivoACargar = "resources/mapas/casa.txt";
								cambioMapa(archivoACargar,1,5,tamanobaldosa,200,380,true,mapa);
							} else {
								archivoACargar = "resources/mapas/mapa.txt";
								cambioMapa(archivoACargar,87,1,tamanobaldosa,780,370,false,mapa);
							}
							break;
						}
					}
				}
			}
		}

		return false;
	}
	public void cambioMapa(String archivoACargar, int numeroCelda, int numeroMapa, int tamanoBaldosa, int posx, int posy, boolean estaDentro, Mapa mapa) {
		mapa.setNumcelda(numeroCelda);
		mapa.setNumeroMapa(numeroMapa);
		mapa.cargarCelda(archivoACargar, mapa.getNumcelda());
		mapa.updateMapa(tamanoBaldosa);
		x = posx;
		y=posy;
		estaDentroDeMazmorra = estaDentro;
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



	public String getArchivoACargar() {
		return archivoACargar;
	}

	public void setArchivoACargar(String archivoACargar) {
		this.archivoACargar = archivoACargar;
	}

	public void AccionAtacar(HashMap<String, ArrayList<Enemigo>> enemigos, Mapa mapa, int tamanobaldosa, ManejoTeclado tecladoM, GamePanel gamePanel, Inventario inventario, ArrayList<Mensaje> mensajes) {
		//TODO que el enemigo reciba daño una sola vez
		if ((tecladoM.fPulsado == true) && (atacando == false) && (objetoEnMano != "")) {
			// Establecemos en qué mopmento hemos atacado
			cooldownAtaque = System.currentTimeMillis();
			sword.playClip(gp.getVolumenAudio());
			System.out.println("Sword");
			atacando = true;
			DanoAtaque(enemigos, tamanobaldosa, mapa, inventario, mensajes);
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
	
	public void DanoAtaque(HashMap<String, ArrayList<Enemigo>> enemigos, int tamanobaldosa, Mapa mapa, Inventario inventario, ArrayList<Mensaje> mensajes) {
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
					//sacamos por el juego el daño hecho mediante un texto
					Mensaje m = new Mensaje("-"+danoJugador+" hp a "+enemigo.getNombre()+"!", 90,Color.BLUE);
					mensajes.add(m);
					enemigoAElim=enemigo;
					enemigosAElim.add(enemigoAElim);
				}
			}
			if(null!=enemigoAElim) {
				for (Enemigo enemigo : enemigosAElim) {
					enemigo.setVida(enemigoAElim.getVida()-danoJugador);
					if(enemigo.getVida()<=0) {
						mensajes.add(new Mensaje("Has matado a "+enemigo.getNombre()+"!", tamanobaldosa, Color.BLUE));
						this.setEnemigosDerrotados(this.getEnemigosDerrotados()+1);
						enemigo.looteoEnemigo(inventario, this, gp);
						enemigos.get(mapa.getNumeroMapa() + "," + mapa.getNumcelda()).remove(enemigo);
					}
				}
			}else {
				System.out.println("No hay daño");
			}
		}

	}
	
	public static  HashMap<String, Integer> leerFichero(String nombreFich) {
    	HashMap<String,Integer> inventario = new HashMap<>();
  
    	try(BufferedReader br = new BufferedReader(new FileReader(nombreFich))){
    		String linea;
    		while ((linea = br.readLine())!=null) {
    			String[] datos = linea.split(";");
    			inventario.put(datos[0], Integer.parseInt(datos[1]));
    		}
    	} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return inventario;
    }
	
	public boolean detectaColisionNpcs(HashMap<String, ArrayList<NPC>> npcs, int tamanobaldosa, Mapa mapa) {
		if (npcs.containsKey(mapa.getNumeroMapa() + "," + mapa.getNumcelda())) {
			for (NPC npc : npcs.get(mapa.getNumeroMapa() + "," + mapa.getNumcelda())) {
				if (((((npc.getPosX() <= (this.getX()) + tamanobaldosa) && (npc.getPosX() >= this.getX()))
						// Colisión en X 
						|| ((npc.getPosX() + tamanobaldosa <= this.getX() + tamanobaldosa)
								&& (npc.getPosX() + tamanobaldosa >= this.getX())))
						// Colision en Y
						&& (((npc.getPosY() <= this.getY() + tamanobaldosa) && (npc.getPosY() >= this.getY()))
								|| ((npc.getPosY() + tamanobaldosa <= this.getY() + tamanobaldosa)
										&& (npc.getPosY() + tamanobaldosa >= this.getY()))))) {
					return true;
					
					
				}
			}
			return false;
		}
		return false;

	}

	
	public void interaccion(ManejoTeclado mt, Mapa mapa) {
		if(this.interaccionDisponible && mt.hablarNPCPulsado) {
			if(!this.hablarConNPC) {
			new InventarioCofre(mt,gp,archivoACargar,mapa.getNumcelda(), this);
			}else{
			mt.empezarConversacion = true;
			new NPC2(mt, gp, "-1-");
			
		}
			mt.hablarNPCPulsado = false;
		}
	}
	public void muerte(GamePanel gp, Thread t) {
		if(vidas[0] == false) {
			t.interrupt();
			numMuertes++;
			new GameOverScreen(enemigosDerrotados,gp,nombreJugador);
		}
	}
	
}
