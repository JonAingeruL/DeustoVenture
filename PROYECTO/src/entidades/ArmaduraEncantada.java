package entidades;

import main.GamePanel;

public class ArmaduraEncantada extends Enemigo {
	
	public ArmaduraEncantada(int x, int y, GamePanel gp) {
		
		super(14,gp);
		this.setNombre("Caballero olvidado");
		this.velocidad = 5;
		this.x = x;
		this.y = y;
		setPatronMovimiento(1);
	}
}
