package entidades;
import main.GamePanel;

public class Esqueleto extends Enemigo {
	
	public Esqueleto(int x, int y, GamePanel gp) {
		
		super(8,gp);
		this.setNombre("Esqueleto");
		this.velocidad = 4;
		this.x = x;
		this.y = y;
		setPatronMovimiento(1);
		
		
	}

}
