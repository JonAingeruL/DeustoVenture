package entidades;

import main.GamePanel;

public class EspirituFuego extends Enemigo {
	
	public EspirituFuego(int x, int y, GamePanel gp) {
		
		super(6,gp);
		this.setNombre("Espiritu de fuego");
		this.velocidad = 5;
		this.x = x;
		this.y = y;
	}

}
