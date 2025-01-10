package gui;

public class Item { //para saber el precio y nombre de los objertos en el comercio

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
