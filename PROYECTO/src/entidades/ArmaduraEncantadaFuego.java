package entidades;

import main.GamePanel;

public class ArmaduraEncantadaFuego extends Enemigo {
	
	public ArmaduraEncantadaFuego(int x, int y, GamePanel gp) {
		
		super(12,gp);
		this.setNombre("Caballero fundido");
		this.velocidad = 6;
		this.x = x;
		this.y = y;
		setPatronMovimiento(1);
	}

}
