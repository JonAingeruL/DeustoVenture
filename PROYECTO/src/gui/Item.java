package gui;

public class Item {
	private String nombre;
    private int precio;

    public Item(String nombre, int precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPrecio() {
        return precio;
    }

}
