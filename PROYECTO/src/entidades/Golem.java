package entidades;

import main.GamePanel;

public class Golem extends Enemigo {
	
	public Golem(int x, int y, GamePanel gp) {
		
		super(8,gp);	
		this.setNombre("Golem de piedra");
		this.velocidad = 2;
		this.x = x;
		this.y = y;
		
	}

}
