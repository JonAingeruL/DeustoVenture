package entidades;

import java.util.ArrayList;
import java.util.HashMap;

import main.GamePanel;

public class NPCcomerciante extends NPC{
	
	private HashMap<String, Integer> productos; // Mapa con nombre del producto como clave y precio como valor

	public NPCcomerciante(int posX, int posY, String frase, boolean esComerciante, GamePanel gp) {
		super(posX, posY, frase, esComerciante, gp);
		productos = new HashMap<>(); // Inicializar el mapa
	}

    // Agregar un producto al comerciante
    public void agregarProducto(String nombre, int precio) {
        productos.put(nombre, precio);
    }

    // Obtener todos los productos y precios como un mapa
    public HashMap<String, Integer> getProductos() {
        return productos;
    }

    // Mostrar productos del comerciante
    public void mostrarProductos() {
        System.out.println("Productos disponibles:");
        for (String producto : productos.keySet()) {
            System.out.println(producto + " - Precio: " + productos.get(producto));
        }
    }

}
