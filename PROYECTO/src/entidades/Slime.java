package entidades;
import main.GamePanel;

public class Slime extends Enemigo{

	public Slime(int x, int y, GamePanel gp) {
		//vida, panel que recibe
		super(3, gp);
		this.velocidad =2;
		this.x = x;
		this.y =y;
		
		
	}

}