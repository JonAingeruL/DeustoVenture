
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import entidades.Personaje;

public class Jugador extends Personaje{
	GamePanel gp;
	ManejoTeclado maneT;
	private boolean estaDentroDeMazmorra=true;
	//en caso de que se quieran añadir más números que tengan colision, se añaden a esta lista
	private List<Integer> zonasConColision = List.of(1);

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
	}
	
	public void conseguirImagenJugador() {
		
		try {
			
			arriba1 = ImageIO.read(getClass().getResourceAsStream("/texJugador/arribaPers1.png"));
			arriba2 = ImageIO.read(getClass().getResourceAsStream("/texJugador/arribaPers2.png"));
			arriba3 = ImageIO.read(getClass().getResourceAsStream("/texJugador/arribaPers3.png"));
			abajo1 = ImageIO.read(getClass().getResourceAsStream("/texJugador/abajoPers1.png"));
			abajo2 = ImageIO.read(getClass().getResourceAsStream("/texJugador/abajoPers2.png"));
			abajo3 = ImageIO.read(getClass().getResourceAsStream("/texJugador/abajoPers3.png"));
			derecha1 = ImageIO.read(getClass().getResourceAsStream("/texJugador/derechaPers1.png"));
			derecha2 = ImageIO.read(getClass().getResourceAsStream("/texJugador/derechaPers2.png"));
			derecha3 = ImageIO.read(getClass().getResourceAsStream("/texJugador/derechaPers3.png"));
			izquierda1 = ImageIO.read(getClass().getResourceAsStream("/texJugador/izquierdaPers1.png"));
			izquierda2 = ImageIO.read(getClass().getResourceAsStream("/texJugador/izquierdaPers2.png"));
			izquierda3 = ImageIO.read(getClass().getResourceAsStream("/texJugador/izquierdaPers3.png"));
			
		}catch(IOException e) {
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
		
		if(tecladoM.arribaPulsado == true || tecladoM.abajoPulsado == true || tecladoM.izquierdaPulsado == true || tecladoM.derechaPulsado == true) {
			
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
			
			contadorSprites++;
			if(contadorSprites > 12) {
				if(numSprite == 1) {
					numSprite = 2;
				}
				else if(numSprite == 2) {
					numSprite = 1;
				}
				contadorSprites = 0;
			}
		}
			
	}
	
	public void dibujarPer(Graphics2D g2) {
		//g2.setColor(Color.white);
		
		//g2.fillRect(x, y, gp.tamañoBaldosa, gp.tamañoBaldosa); 
		
		BufferedImage imagen = null;
		
		switch(direccion) {
		case "arriba":
			if (numSprite == 1) {
				imagen = arriba1;
			}
			if (numSprite == 2) {
				imagen = arriba2;
				imagen = arriba3;
			}
			break;
		case "abajo":
			if (numSprite == 1) {
				imagen = abajo1;
			}
			if(numSprite == 2) {
				imagen = abajo2;
				imagen = abajo3;
			}
			break;
		case "izquierda":
			if (numSprite == 1) {
				imagen = izquierda1;
			}
			if(numSprite == 2) {
				imagen = izquierda2;
				imagen = izquierda3;
			}
			break;
		case "derecha":
			if (numSprite == 1) {
				imagen = derecha1;
			}
			if (numSprite == 2) {
				imagen = derecha2;
				imagen = derecha3;
			}
			break;
		}
		g2.drawImage(imagen, x, y, gp.tamañoBaldosa, gp.tamañoBaldosa, null);
	}

	/**
	 * Este método detecta colisiones entre el personaje y el mapa. Devuelve un
	 * booleano.
	 * 
	 * @param mapa          El mapa que puede colisionar con el jugador
	 * @param tamanobaldosa El tamaño de cada baldosa del mapa
	 * @return
	 */
	public boolean detectaColision(Mapa mapa, int tamanobaldosa) {
		//se usa para saber en que celda esta el jugador
		int celdaX = (x+32)/tamanobaldosa;
		if (x<-32) {
			celdaX=mapa.getCelda().length-1;	
		}
		int celdaY = (y+32)/tamanobaldosa;
		if (y<-32) {
			celdaY=mapa.getCelda()[0].length-1;
		}
		System.out.println("Posicionjugador:"+celdaX+","+celdaY+", Posicion coords: "+x+","+y);
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
					//este switch analiza cada uno de los numeros que se han asignado para cada portal 
					//puesto en guia.txt NO ESTÁ COMPLETO, SOLO EL DE DEL TUTORIAL AL MAPA PRINCIPAL

					switch (mapa.getCelda()[i][j]) {
					
					case 0:
						
						break;
						
					case 20:
						System.out.println("20");
						if(celdaX==i && celdaY==j) {
							System.out.println("dentro");
							if (this.estaDentroDeMazmorra) {
								
								mapa.cargarCelda("src/mapa.txt", 1);
								
								this.estaDentroDeMazmorra = false;
								//POSICIONES NUEVAS PARA DESPUES CARGAR EL MAPA
								x=450;
								y=300;
							}	else {
								mapa.cargarCelda("src/tutorial.txt", 4);
								x=705;
								y=241;
								this.estaDentroDeMazmorra = true;
								
							}
							
						}
						break;

					case 21:
						if (!estaDentroDeMazmorra) {
							mapa.cargarCelda("src/dungeon1.txt", 1);
							
							estaDentroDeMazmorra = true;
						}	else {
							mapa.cargarCelda("src/tutorial.txt", 1);
							
							estaDentroDeMazmorra = false;
						}
						break;
					
					case 22: 
						if (!estaDentroDeMazmorra) {
							mapa.cargarCelda("src/dungeon2.txt", 1);
							
							estaDentroDeMazmorra = true;
						}	else {
							mapa.cargarCelda("src/tutorial.txt", 1);
							
							estaDentroDeMazmorra = false;
						}
						break;
					case 23:
						if (!estaDentroDeMazmorra) {
							mapa.cargarCelda("src/casa.txt", 1);
						
							estaDentroDeMazmorra = true;
						}	else {
							mapa.cargarCelda("src/tutorial.txt", 1);
							
							estaDentroDeMazmorra = false;
						}
						break;
					case 24:
						if (!estaDentroDeMazmorra) {
							mapa.cargarCelda("src/dungeon3.txt", 1);
							
						  estaDentroDeMazmorra= true;
						}	else {
							mapa.cargarCelda("src/tutorial.txt", 1);
							
							estaDentroDeMazmorra = false;
							
						}
						break;
						}
					}

				}
			}


	
		return false;
	}
}
	
