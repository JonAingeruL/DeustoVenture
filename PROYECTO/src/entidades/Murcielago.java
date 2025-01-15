package entidades;

import main.GamePanel;

public class Murcielago extends Enemigo{
	
	public Murcielago(int x, int y, GamePanel gp) {
		
		super(2,gp);
		this.setNombre("Murcielago");
		this.velocidad = 7;
		this.x = x;
		this.y = y;
		setContadorTiempoMovimiento(60);
		setPatronMovimiento(2);
	}

}
