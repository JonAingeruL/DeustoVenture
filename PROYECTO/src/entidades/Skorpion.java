package entidades;

import main.GamePanel;

public class Skorpion extends Enemigo {
	
	public Skorpion(int x, int y, GamePanel gp) {
		
		super(8,gp);
		this.setNombre("Skorpion");
		this.velocidad = 5;
		this.x = x;
		this.y = y;
		setPatronMovimiento(1);
	}

}
