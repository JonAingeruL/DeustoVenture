package main;

public class BasesMain {

	public static void main(String[] args) {
		GestorBD bd = new GestorBD();
		bd.CrearBBDD();
		bd.CrearBBDD_POS();
		bd.CrearBBDD_INV();
		bd.guardarUsuarioConValidacion(new Usuario("Default", 0, 0, 0));
		bd.guardarPosicionUsuario("Default", 0, 0, 0, 0);
		bd.guardarItemEnInventario("Default", "Espada de madera", 1);
		bd.guardarItemEnInventario("Default", "Manzana", 1);
		bd.guardarItemEnInventario("Default", "Pocion de salud", 1);
	}
}
