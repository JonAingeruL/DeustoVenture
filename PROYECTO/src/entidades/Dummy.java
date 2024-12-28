package entidades;

import main.GamePanel;
import main.Mapa;

public class Dummy extends Enemigo{

	public Dummy(int x, int y, GamePanel gp) {
		super(1000, gp);
		this.velocidad=0;
		this.x = x;
		this.y = y;
	}

	@Override
	public void movimiento(Mapa mapa, int tamanoBaldosa, Jugador jugador) {
		return;
	}


	
	
	
	
}
