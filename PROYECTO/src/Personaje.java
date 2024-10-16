
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
	public void movimiento(ManejoTeclado tecladoM) {
		if (tecladoM.arribaPulsado == true) {
			jugadorY -= velocidadJugador; // Basicamente cuando el jugador mantenga pulado W el personaje se movera 4
											// hacia arriba, 4 porque hemos puesto esa su velocidad
		} else if (tecladoM.abajoPulsado == true) {
			jugadorY += velocidadJugador; // Basicamente cuando el jugador mantenga pulado S el personaje se movera 4
											// hacia abajo, 4 porque hemos puesto esa su velocidad
		} else if (tecladoM.izquierdaPulsado == true) {
			jugadorX -= velocidadJugador; // Basicamente cuando el jugador mantenga pulado A el personaje se movera 4
											// hacia la izq, 4 porque hemos puesto esa su velocidad
		} else if (tecladoM.derechaPulsado == true) {
			jugadorX += velocidadJugador; // Basicamente cuando el jugador mantenga pulado D el personaje se movera 4
											// hacia la der, 4 porque hemos puesto esa su velocidad
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
}
