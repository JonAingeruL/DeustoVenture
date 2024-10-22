
public class Jugador extends Personaje{

	public Jugador(int personajeX, int personajeY, int velocidadPersonaje) {
		super(personajeX, personajeY, velocidadPersonaje);
		
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
		if (tecladoM.arribaPulsado == true) {
			personajeY -= velocidadPersonaje;
			while (detectaColision(mapa, tamanoBaldosa)) {
				personajeY += 1;
			}
		} else if (tecladoM.abajoPulsado == true) {
			personajeY += velocidadPersonaje;
			while (detectaColision(mapa, tamanoBaldosa)) {
				personajeY -= 1;
			}
		} else if (tecladoM.izquierdaPulsado == true) {
			personajeX -= velocidadPersonaje;
			while (detectaColision(mapa, tamanoBaldosa)) {
				personajeX += 1;
			}
		} else if (tecladoM.derechaPulsado == true) {
			personajeX += velocidadPersonaje;
			while (detectaColision(mapa, tamanoBaldosa)) {
				personajeX -= 1;
			}
		}
	}
	
	/**
	 * Devuelve la posición en X del jugador
	 * 
	 * @return Posición en X jugador (int)
	 */
	public int getJugadorX() {
		return personajeX;
	}

	/**
	 * Establece la posición del jugador en X
	 * 
	 * @param jugadorX Posición en x del jugador
	 */
	public void setJugadorX(int personajeX) {
		this.personajeX = personajeX;
	}

	/**
	 * Devuelve la posición en Y del jugador
	 * 
	 * @return Posición en Y jugador (int)
	 */
	public int getJugadorY() {
		return personajeY;
	}

	/**
	 * Establece la posición en Y del jugador
	 * 
	 * @param jugadorY Posición en Y del jugador
	 */
	public void setJugadorY(int personajeY) {
		this.personajeY = personajeY;
	}

	/**
	 * Devuelve la velocidad del jugador
	 * 
	 * @return velocidad del jugador
	 */
	public int getVelocidadJugador() {
		return velocidadPersonaje;
	}

	/**
	 * Establece la velocidad del jugador
	 * 
	 * @param velocidadJugador La velocidad con la que se moverá el jugador
	 */
	public void setVelocidadJugador(int velocidadPersonaje) {
		this.velocidadPersonaje = velocidadPersonaje;
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
		// Recorro el array celda completo
		for (int i = 0; i < mapa.getCelda().length; i++) {
			for (int j = 0; j < mapa.getCelda()[i].length; j++) {
				// Por cada baldosa no vacía, calcula si está colisionando de algún modo con el
				// personaje
				if (mapa.getCelda()[i][j] != 0) {
					// Compruebo si hay colisión en X con la esquina izquierda
					if (((((personajeX <= (i * tamanobaldosa) + tamanobaldosa) && (personajeX >= i * tamanobaldosa))
							// Compruebo si hay colisión en X con la esquina derecha
							|| ((personajeX + tamanobaldosa <= (i * tamanobaldosa) + tamanobaldosa)
									&& (personajeX + tamanobaldosa >= i * tamanobaldosa)))
							// Compruebo si hay colisión en Y por arriba
							&& (((personajeY <= (j * tamanobaldosa) + tamanobaldosa) && (personajeY >= j * tamanobaldosa))
									// Compruebo si hay colisión en Y por abajo
									|| ((personajeY + tamanobaldosa <= (j * tamanobaldosa) + tamanobaldosa)
											&& (personajeY + tamanobaldosa >= j * tamanobaldosa))))) {
						// Debe detectarse por lo menos una colisión en el eje X y una colisión en el
						// eje Y
						System.out.println("Hay colision");
						return true;
					}
				}

			}
		}
		return false;
	}


}