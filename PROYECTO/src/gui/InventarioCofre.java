package gui;

import main.ManejoTeclado;

//TODO Crear una clase InventarioCofre diferente de Inventario que lea de su propio fichero
public class InventarioCofre extends Inventario{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InventarioCofre(ManejoTeclado tecladoM) {
		super(tecladoM);
		setTitle("Cofre");
	}

}
