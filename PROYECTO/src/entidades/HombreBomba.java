package entidades;

import main.GamePanel;

public class HombreBomba extends Enemigo {
	
	public HombreBomba(int x, int y, GamePanel gp) {
		
		super(1,gp);
		this.setNombre("BombaViva");
		this.velocidad = 10;
		this.x = x;
		this.y = y;
		setPatronMovimiento(1);
	}

}
