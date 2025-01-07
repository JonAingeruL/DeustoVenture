package entidades;

import main.GamePanel;

public class CangrejoFuego extends Enemigo {
	
	public CangrejoFuego(int x, int y, GamePanel gp) {
		
		super(6, gp);
		this.setNombre("Cangrejo igneo");
		this.velocidad = 5;
		this.x = x;
		this.y = y;
	}
	

}
