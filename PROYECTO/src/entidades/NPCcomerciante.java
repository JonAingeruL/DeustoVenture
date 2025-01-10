package entidades;

import java.util.ArrayList;

import gui.Item;
import main.GamePanel;

public class NPCcomerciante extends NPC{
	
	public NPCcomerciante(int posX, int posY, String frase, GamePanel gp) {
		super(posX, posY, frase, gp);
		productos = new ArrayList<>();
	}

	private ArrayList<Item> productos;


    public void agregarItem(Item item) {
        productos.add(item);
    }

    public ArrayList<Item> getProductos() {
        return productos;
    }

}
