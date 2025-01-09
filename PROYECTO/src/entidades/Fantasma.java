package entidades;

import main.GamePanel;

public class Fantasma extends Enemigo {
	
	public Fantasma(int x, int y, GamePanel gp) {
		super(4,gp);
		this.setNombre("Fantasma del bosque");
		this.velocidad = 4;
		this.x = x;
		this.y = y;
		setContadorTiempoMovimiento(60);
		setPatronMovimiento(2);
		
	}
	

}
