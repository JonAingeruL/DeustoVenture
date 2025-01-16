package entidades;

import java.awt.image.BufferedImage;

public class Personaje {
	// POSICION
	public int x, y;
	// velocidad
	public int velocidad;
	
	public BufferedImage arriba1, arriba2, arriba3,  abajo1, abajo2, abajo3, derecha1, derecha2, derecha3, izquierda1, izquierda2, izquierda3;
	public String direccion;
	
	public int contadorSprites = 0;
	public int numSprite = 1;
	
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
	public String getDireccion() {
		return direccion;
	}

}
