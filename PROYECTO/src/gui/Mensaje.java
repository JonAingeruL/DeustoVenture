package gui;

import java.awt.Color;

public class Mensaje {
	
	private String texto;
	private int tiempodeVida;
	private Color colorMensaje;
	
	
	
	public Mensaje(String texto, int tiempodeVida, Color colorMensaje) {
		super();
		this.texto = texto;
		this.tiempodeVida = tiempodeVida;
		this.colorMensaje = colorMensaje;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public int getTiempodeVida() {
		return tiempodeVida;
	}
	public void setTiempodeVida(int tiempodeVida) {
		this.tiempodeVida = tiempodeVida;
	}
	public Color getColorMensaje() {
		return colorMensaje;
	}
	public void setColorMensaje(Color colorMensaje) {
		this.colorMensaje = colorMensaje;
	}
	
	
	
	
}
