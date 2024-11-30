package entidades;
import java.awt.Graphics2D;
import main.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;


public class Jugador extends Personaje {
	GamePanel gp;
	ManejoTeclado maneT;
	private boolean estaDentroDeMazmorra = true;
	private String archivoACargar = "Resources/mapas/tutorial.txt";
	private BufferedImage corazonVida, corazonSinVida, corazonAMedias;
	private boolean[] vidas = { true, true, true, false, false, false  };
	// en caso de que se quieran añadir más números que tengan colision, se añaden a
	// esta lista
	private List<Integer> zonasConColision = List.of(1, 4, 6, 7, 9, 10, 11, 12, 13, 15, 16, 17, 30, 31, 32, 33, 35, 36,
			37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 60);

	public Jugador(GamePanel gp, ManejoTeclado maneT) {

		this.gp = gp;
		this.maneT = maneT;

		valoresDefault();
		conseguirImagenJugador();
	}

	public void valoresDefault() {
		x = 475;
		y = 400;
		velocidad = 4;
		direccion = "abajo";
		hablarNPC = false;
		
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
			corazonVida = ImageIO.read(getClass().getResourceAsStream("/texturas/vida/corazonConVida.png")); // el corazon con
																									// vida y su ruta
			corazonSinVida = ImageIO.read(getClass().getResourceAsStream("/texturas/vida/CorazonSinVida.png")); // el corazon sin
																										// vida y su
																										// ruta
			corazonAMedias = ImageIO.read(getClass().getResourceAsStream("/texturas/vida/CorazonAMedias.png")); //El corazón a medias

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
	public void movimiento(ManejoTeclado tecladoM, Mapa mapa, int tamanoBaldosa) {
		// Dependiendo de la tecla pulsada, muevo al jugador en la dirección
		// correspondiente.
		// Después compruebo si hay colisión con el mapa, y en ese caso revierto el
		// movimiento lo que haga falta
		// para corregirla.
		// También compruebo si se aprieta shift para aplicar un sprint

		if (tecladoM.arribaPulsado == true || tecladoM.abajoPulsado == true || tecladoM.izquierdaPulsado == true
				|| tecladoM.derechaPulsado == true) {
			if (tecladoM.shiftPulsado == true) {
				velocidad += 2;
			}
			if (tecladoM.arribaPulsado == true) {
				direccion = "arriba";
				y -= velocidad;
				while (detectaColision(mapa, tamanoBaldosa)) {
					y += 1;
				}
			} else if (tecladoM.abajoPulsado == true) {
				direccion = "abajo";
				y += velocidad;
				while (detectaColision(mapa, tamanoBaldosa)) {
					y -= 1;
				}
			} else if (tecladoM.izquierdaPulsado == true) {
				direccion = "izquierda";
				x -= velocidad;
				while (detectaColision(mapa, tamanoBaldosa)) {
					x += 1;
				}
			} else if (tecladoM.derechaPulsado == true) {
				direccion = "derecha";
				x += velocidad;
				while (detectaColision(mapa, tamanoBaldosa)) {
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
			break;
		case "abajo":
			if (numSprite == 1) {
				imagen = abajo1;
			} else if (numSprite == 2) {
				imagen = abajo2;

			} else if (numSprite == 3) {
				imagen = abajo3;
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
			break;
		case "derecha":
			if (numSprite == 1) {
				imagen = derecha1;
			} else if (numSprite == 2) {
				imagen = derecha2;

			} else if (numSprite == 3) {
				imagen = derecha3;
			}
			break;
		}
		g2.drawImage(imagen, x, y, gp.tamañoBaldosa, gp.tamañoBaldosa, null);
	}
	/**
	 * Dibuja las vidas como corazones en pantalla
	 * @param g2 El objeto graphics 2d a utilizar
	 */
	public void dibujarVidas(Graphics2D g2) {
		for (int i = 0; i < vidas.length; i+=2) {
			if (vidas[i] && vidas[i+1]) {
				g2.drawImage(corazonVida, 80 + 35 * i/2, 60, 40, 40, null);
			} else if(vidas[i] && !vidas[i+1]) {
				g2.drawImage(corazonAMedias, 80 + 35 * i/2, 60, 40, 40, null);
			}else {
				// TODO: Aparentemente, el sprite de corazon sin vida no está bien alineado
				// (está más
				// a la derecha que el sprite de corazón con vida). Estaría bien alinearlo
				// correctamente
				g2.drawImage(corazonSinVida, 80 + 38 * i/2, 60, 40, 40, null);
			}
		}
	}

	public boolean[] getVidas() {
		return vidas;
	}
/**
 * Establece el número de vidas y su estado.
 * AVISO: Es preferible utilizar el método cambiarVidas para asegurar
 * que el dibujado sea correcto.
 * Utilizar este método sólo para alterar vidas máximas
 * @param vidas Un array booleano que representa la cantidad de corazones y su estado
 */
	public void setVidas(boolean[] vidas) {
		if (!((vidas.length)%2==0)){
			boolean[] vidasCorregido = new boolean[vidas.length+1];
			vidasCorregido[vidasCorregido.length-1] = false;
			System.arraycopy(vidas, 0, vidasCorregido, 0, vidas.length);
			this.vidas = vidasCorregido;
		}else {
		this.vidas = vidas;
		}
	}
	/**
	 * Este método aumenta o reduce el número de vidas según el entero que recibe 
	 * (Positivo aumenta negativo disminuye)
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
		System.out.println("Posicionjugador:" + celdaX + "," + celdaY + ", Posicion coords: " + x + "," + y);
		// Recorro el array celda completo

		for (int i = 0; i < mapa.getCelda().length; i++) {
			for (int j = 0; j < mapa.getCelda()[i].length; j++) {
				// Por cada baldosa no vacía, calcula si está colisionando de algún modo con el
				// personaje, USANDO LA LISTA QUE TENEMOS DE NUMEROS COLISIONABLES
				if (zonasConColision.contains(mapa.getCelda()[i][j])) {
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
								archivoACargar = "Resources/mapas/mapa.txt";
								mapa.cargarCelda(archivoACargar, mapa.getNumcelda());
								mapa.updateMapa(tamanobaldosa);
								this.estaDentroDeMazmorra = false;
								// POSICIONES NUEVAS PARA DESPUES CARGAR EL MAPA
								x = 450;
								y = 300;
							} else {
								mapa.setNumcelda(4);
								archivoACargar = "Resources/mapas/tutorial.txt";
								mapa.cargarCelda(archivoACargar, mapa.getNumcelda());
								mapa.updateMapa(tamanobaldosa);
								x = 705;
								y = 241;
								this.estaDentroDeMazmorra = true;

							}
							break;

						case 21:
							if (!estaDentroDeMazmorra) {

								archivoACargar = "Resources/dungeon1.txt";
								mapa.cargarCelda(archivoACargar, 1);

								estaDentroDeMazmorra = true;
							} else {
								archivoACargar = "Resources/mapas/mapa.txt";
								mapa.cargarCelda(archivoACargar, 1);

								estaDentroDeMazmorra = false;
							}
							break;

						case 22:
							if (!estaDentroDeMazmorra) {
								archivoACargar = "Resources/mapas/dungeon2.txt";
								mapa.cargarCelda(archivoACargar, 1);

								estaDentroDeMazmorra = true;
							} else {
								archivoACargar = "Resources/mapas/mapa.txt";
								mapa.cargarCelda(archivoACargar, 1);

								estaDentroDeMazmorra = false;
							}
							break;
						case 23:
							if (!estaDentroDeMazmorra) {
								archivoACargar = "Resources/mapas/casa.txt";
								mapa.cargarCelda(archivoACargar, 1);

								estaDentroDeMazmorra = true;
							} else {
								archivoACargar = "Resources/mapas/mapa.txt";
								mapa.cargarCelda(archivoACargar, 1);

								estaDentroDeMazmorra = false;
							}
							break;
						case 24:
							if (!estaDentroDeMazmorra) {
								archivoACargar = "Resources/mapas/dungeon3.txt";
								mapa.cargarCelda(archivoACargar, 1);

								estaDentroDeMazmorra = true;
							} else {
								archivoACargar = "Resources/mapas/mapa.txt";
								mapa.cargarCelda(archivoACargar, 1);

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
	
	public void InteracctuarNPC(Mapa mapa, int tamanobaldosa, ManejoTeclado tecladoM, GamePanel gamePanel) {
		int celdaX = (x + 32) / tamanobaldosa;
		if (x < -32) {
			celdaX = mapa.getCelda().length - 1;
		}
		int celdaY = (y + 32) / tamanobaldosa;
		if (y < -32) {
			celdaY = mapa.getCelda()[0].length - 1;
		}
		
		for(NPC npc: mapa.getNpcs()) {
			if(Math.abs(celdaX - npc.getX()) < 1 && Math.abs(celdaY - npc.getY()) <1 ) {
				if(tecladoM.hablarNPCPulsado == true) { //interactua con la tecla E
					String textoDialogo =  npc.hablar(); // Imprime en consola y obtiene el texto.
					gamePanel.mostrarDialogo(textoDialogo);// Enseña el texto en el JLabel.
					break;
				}else {
					gamePanel.ocultarDialogo(); //se oculta el JLabel
					break;
				}
			}
		}
		
	}

	public String getArchivoACargar() {
		return archivoACargar;
	}

	public void setArchivoACargar(String archivoACargar) {
		this.archivoACargar = archivoACargar;
	}
	

//	public JPanel vidaJugador(Boolean vida1,Boolean vida2, Boolean vida3) {
//		panelVida = new JPanel(new BorderLayout());
//		boolean[] vidas = {vida1, vida2, vida3}; //para poder hacer un for y que el codigo sea mas optimo
//        JPanel panelDerecho = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Uso FlowLayout para poder alinear los corazones en la derecha
//        panelDerecho.setPreferredSize(new Dimension(150, 50)); // Tamaño del panel
//
//        // Cargar imágenes de los corazones
//        ImageIcon corazonIconoConVida = new ImageIcon("/texturas/vida/corazonConVida.png"); // el corazon con vida y su ruta
//        ImageIcon corazonIconoSinVida = new ImageIcon("/texturas/vida/CorazonSinVida.png"); // el corazon sin vida y su ruta
//
//        JLabel corazonVida = new JLabel(corazonIconoConVida); //creo los Jlabel para meter las imagenes en el panel
//        JLabel corazonSinVida = new JLabel(corazonIconoSinVida);
//        
//        for (boolean vida : vidas) {
//            if (vida) {
//                panelDerecho.add(corazonVida);
//            } else {
//                panelDerecho.add(corazonSinVida);
//            }
//        }
//
//        // Agrega el panelDerecho al panel principal en la posición derecha
//        panelVida.add(panelDerecho, BorderLayout.EAST);
//        
//        return panelVida;
//	}
//	
//	public JPanel getPanelVidas(Graphics2D g2) {
//		return panelVida;
//	}
}
