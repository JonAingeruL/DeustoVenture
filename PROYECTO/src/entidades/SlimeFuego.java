package entidades;
import main.GamePanel;

public class SlimeFuego extends Enemigo {
	
	public SlimeFuego(int x, int y, GamePanel gp) {
		
		super(45,gp);
		this.setNombre("Slime de fuego");
		this.velocidad = 4;
		this.x = x;
		this.y = y;
		setPatronMovimiento(1);
	}

}
