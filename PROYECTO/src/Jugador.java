
import java.awt.Color;
import java.awt.Graphics2D;

import entidades.Personaje;

public class Jugador extends Personaje{
	
	GamePanel gp;
	ManejoTeclado maneT;

	public Jugador(GamePanel gp, ManejoTeclado maneT) {
		
		this.gp = gp;
		this.maneT = maneT;
		
		valoresDefault();
	}
	
	public void valoresDefault() {
		x = 350;
		y = 300;
		velocidad = 4;
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
			y -= velocidad;
			while (detectaColision(mapa, tamanoBaldosa)) {
				y += 1;
			}
		} else if (tecladoM.abajoPulsado == true) {
			y += velocidad;
			while (detectaColision(mapa, tamanoBaldosa)) {
				y -= 1;
			}
		} else if (tecladoM.izquierdaPulsado == true) {
			x -= velocidad;
			while (detectaColision(mapa, tamanoBaldosa)) {
				x += 1;
			}
		} else if (tecladoM.derechaPulsado == true) {
			x += velocidad;
			while (detectaColision(mapa, tamanoBaldosa)) {
				x -= 1;
			}
		}
	}
	
	public void dibujarPer(Graphics2D g2) {
		g2.setColor(Color.white);
		
		g2.fillRect(x, y, gp.tamañoBaldosa, gp.tamañoBaldosa); 
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
					if (((((x <= (i * tamanobaldosa) + tamanobaldosa) && (x >= i * tamanobaldosa))
							// Compruebo si hay colisión en X con la esquina derecha
							|| ((x + tamanobaldosa <= (i * tamanobaldosa) + tamanobaldosa)
									&& (x + tamanobaldosa >= i * tamanobaldosa)))
							// Compruebo si hay colisión en Y por arriba
							&& (((y <= (j * tamanobaldosa) + tamanobaldosa) && (y >= j * tamanobaldosa))
									// Compruebo si hay colisión en Y por abajo
									|| ((y + tamanobaldosa <= (j * tamanobaldosa) + tamanobaldosa)
											&& (y + tamanobaldosa >= j * tamanobaldosa))))) {
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