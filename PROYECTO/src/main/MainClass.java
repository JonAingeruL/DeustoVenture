package main;

import gui.MenuInicio;

public class MainClass {

	public static void main(String[] args) {
		GestorBD bd = new GestorBD();
		bd.CrearBBDD();
        MenuInicio programa = new MenuInicio();
        programa.setVisible(true);

	}
}
