package entidades;

import main.GamePanel;
import main.Mapa;

public class Dummy extends Enemigo{

	public Dummy(int x, int y, GamePanel gp) {
		super(5, gp);
		this.setNombre("Dummy");
		this.velocidad=0;
		this.x = x;
		this.y = y;
	}

	@Override
	public void movimiento(Mapa mapa, int tamanoBaldosa, Jugador jugador) {
		return;
	}

	@Override
	public void setVida(int vida) {
	}
	


	
	
	
	
}
