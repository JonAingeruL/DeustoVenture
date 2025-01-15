package entidades;

import main.GamePanel;

public class BabosaFuego extends Enemigo{
	
	public BabosaFuego(int x, int y, GamePanel gp) {
		
		super(30,gp);
		this.setNombre("Babosa ignea");
		this.velocidad = 1;
		this.x = x;
		this.y = y;
		setPatronMovimiento(1);
	}
	

}
