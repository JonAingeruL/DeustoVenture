package entidades;

import java.util.Random;

import main.GamePanel;
import main.Mapa;

public class CangrejoFuego extends Enemigo {
	
	public CangrejoFuego(int x, int y, GamePanel gp) {
		
		super(6, gp);
		this.setNombre("Cangrejo igneo");
		this.velocidad = 5;
		this.x = x;
		this.y = y;
		setPatronMovimiento(4);
		Random r = new Random();
		this.direccion = r.nextInt(1,5);
	}
	
	
}

