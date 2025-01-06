package main;

import gui.MenuInicio;

public class MainClass {

	public static void main(String[] args) {
		GestorBD bd = new GestorBD();
		bd.CrearBBDD();
		Usuario u = new Usuario("Paco", 0, 0, 0);
		bd.guardarUsuarioConValidacion(u);
		bd.existeUsuario("Paco");
        MenuInicio programa = new MenuInicio();
        programa.setVisible(true);

	}
}
