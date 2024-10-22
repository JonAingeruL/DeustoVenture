package entidades;

import java.awt.image.BufferedImage;

public class Personaje {
	// POSICION
	public int x, y;
	// velocidad
	public int velocidad;
	
	public BufferedImage arriba1, arriba2, abajo1, abajo2, derecha1, derecha2, izquierda1, izquierda2;
	public String direccion;
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getVelocidad() {
		return velocidad;
	}
	public void setVelocidad(int velocidad) {
		this.velocidad = velocidad;
	}

}
