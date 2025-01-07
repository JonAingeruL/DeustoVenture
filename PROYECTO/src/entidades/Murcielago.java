package entidades;

import main.GamePanel;

public class Murcielago extends Enemigo{
	
	public Murcielago(int x, int y, GamePanel gp) {
		
		super(6,gp);
		this.setNombre("Mucielago");
		this.velocidad = 7;
		this.x = x;
		this.y = y;
	}

}
