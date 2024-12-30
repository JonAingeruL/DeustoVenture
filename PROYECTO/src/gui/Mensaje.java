package gui;

public class Mensaje {
	
	private String texto;
	private int tiempodeVida;
	
	
	public Mensaje(String texto, int tiempodeVida) {
		super();
		this.texto = texto;
		this.tiempodeVida = tiempodeVida;
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
	
	
	
}
