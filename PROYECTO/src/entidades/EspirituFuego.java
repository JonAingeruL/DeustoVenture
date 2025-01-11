package entidades;

import java.util.Random;

import main.GamePanel;
import main.Mapa;

public class EspirituFuego extends Enemigo {
	
	public EspirituFuego(int x, int y, GamePanel gp) {
		
		super(6,gp);
		this.setNombre("Espiritu de fuego");
		this.velocidad = 5;
		this.x = x;
		this.y = y;
		setPatronMovimiento(3);
		Random r = new Random();
		this.direccion = r.nextInt(1,5);
	}
	
	

}
