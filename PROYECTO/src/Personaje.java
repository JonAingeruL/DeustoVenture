
public class Personaje {
	// POSICION DEL JUGADOR
	private int jugadorX;
	private int jugadorY;
	//velocidad del jugador
	private int velocidadJugador;

	public Personaje(int jugadorX, int jugadorY, int velocidadJugador) {
		super();
		this.jugadorX = jugadorX;
		this.jugadorY = jugadorY;
		this.velocidadJugador = velocidadJugador;
	}

	// Aqui dentro vamos a cambiar la posicion del personaje
	// Hay que recordar que en java la esquina superior izq tiene la cordenada 0,0
	/**
	 * Esta funcion controla el movimiento del personaje jugable según la interacción con el teclado
	 * @param tecladoM El objeto manejoTeclado a utilizar
	 */
	public void movimiento(ManejoTeclado tecladoM, Mapa mapa, int tamanoBaldosa) {
		//Dependiendo de la tecla pulsada, muevo al jugador en la dirección correspondiente.
		//Después compruebo si hay colisión con el mapa, y en ese caso revierto el movimiento lo que haga falta
		//para corregirla.
		if (tecladoM.arribaPulsado == true) {
			jugadorY -= velocidadJugador;
			while(detectaColision(mapa, tamanoBaldosa)) {
				jugadorY += 1;
			}
		} else if (tecladoM.abajoPulsado == true) {
			jugadorY += velocidadJugador;
			while(detectaColision(mapa, tamanoBaldosa)) {
				jugadorY -= 1;
			}
		} else if (tecladoM.izquierdaPulsado == true) {
			jugadorX -= velocidadJugador;
			while(detectaColision(mapa, tamanoBaldosa)) {
				jugadorX += 1;
			}
		} else if (tecladoM.derechaPulsado == true) {
			jugadorX += velocidadJugador;
			while(detectaColision(mapa, tamanoBaldosa)) {
				jugadorX -= 1;
			}
		}
	}

	/**
	 * Devuelve la posición en X del jugador
	 * 
	 * @return Posición en X jugador (int)
	 */
	public int getJugadorX() {
		return jugadorX;
	}

	/**
	 * Establece la posición del jugador en X
	 * 
	 * @param jugadorX Posición en x del jugador
	 */
	public void setJugadorX(int jugadorX) {
		this.jugadorX = jugadorX;
	}

	/**
	 * Devuelve la posición en Y del jugador
	 * 
	 * @return Posición en Y jugador (int)
	 */
	public int getJugadorY() {
		return jugadorY;
	}

	/**
	 * Establece la posición en Y del jugador
	 * 
	 * @param jugadorY Posición en Y del jugador
	 */
	public void setJugadorY(int jugadorY) {
		this.jugadorY = jugadorY;
	}

	/**
	 * Devuelve la velocidad del jugador
	 * 
	 * @return velocidad del jugador
	 */
	public int getVelocidadJugador() {
		return velocidadJugador;
	}

	/**
	 * Establece la velocidad del jugador
	 * 
	 * @param velocidadJugador La velocidad con la que se moverá el jugador
	 */
	public void setVelocidadJugador(int velocidadJugador) {
		this.velocidadJugador = velocidadJugador;
	}
	/**
	 * Este método detecta colisiones entre el personaje y el mapa. Devuelve un booleano.
	 * @param mapa El mapa que puede colisionar con el jugador
	 * @param tamanobaldosa El tamaño de cada baldosa del mapa
	 * @return
	 */
	public boolean detectaColision(Mapa mapa, int tamanobaldosa) {
		//Recorro el array celda completo
		for (int i = 0; i < mapa.getCelda().length; i++) {
			for (int j = 0; j < mapa.getCelda()[i].length; j++) {
				//Por cada baldosa no vacía, calcula si está colisionando de algún modo con el personaje
				if (mapa.getCelda()[i][j] != 0) {
					if (((((jugadorX <= (i*tamanobaldosa)+tamanobaldosa) && (jugadorX >= i*tamanobaldosa))||((jugadorX+ tamanobaldosa <= (i*tamanobaldosa)+tamanobaldosa) && (jugadorX+tamanobaldosa >= i*tamanobaldosa)))&& (((jugadorY <= (j*tamanobaldosa)+tamanobaldosa) && (jugadorY >= j*tamanobaldosa)) || ((jugadorY+ tamanobaldosa <= (j*tamanobaldosa)+tamanobaldosa) && (jugadorY+tamanobaldosa >= j*tamanobaldosa))))) {
					System.out.println("Hay colision");
					return true;
					}
				}
				
			}
		}
		return false;
	}
	

}
