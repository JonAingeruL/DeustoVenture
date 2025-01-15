package entidades;

import main.GamePanel;

public class EsqueletoConArmadura extends Enemigo {
	
	public EsqueletoConArmadura(int x, int y, GamePanel gp) {
		
		super(20,gp);
		this.setNombre("Teniente Esqueleto");
		this.velocidad = 6;
		this.x = x;
		this.y = y;
		setPatronMovimiento(1);
	}
}
