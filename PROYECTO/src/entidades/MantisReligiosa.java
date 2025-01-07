package entidades;

import main.GamePanel;

public class MantisReligiosa extends Enemigo {
	
	public MantisReligiosa(int x, int y, GamePanel gp) {
		
		super(8,gp);
		this.setNombre("Mantiss religiossa");
		this.velocidad = 5;
		this.x = x;
		this.y = y;
		
	}

}