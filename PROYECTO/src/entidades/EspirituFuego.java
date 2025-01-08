package entidades;

import java.util.Random;

import main.GamePanel;
import main.Mapa;

public class EspirituFuego extends Enemigo {
	
	public EspirituFuego(int x, int y, GamePanel gp) {
		
		super(6,gp);
		this.setNombre("Espiritu de fuego");
		this.velocidad = 5;
		this.x = x;
		this.y = y;
		Random r = new Random();
		this.direccion = r.nextInt(1,5);
	}
	
	public void movimiento(Mapa mapa, int tamanoBaldosa, Jugador jugador) {
		switch(this.direccion) {
		case 1,3: y+=velocidad;
		if(detectaColisionJugador(mapa, jugador, tamanoBaldosa)) {
			jugador.cambiarVidas(-1);
		}
		while(detectaColisionJugador(mapa, jugador, tamanoBaldosa)) {
			y-=1;
		}
		break;
		case 2,4: y-=velocidad;
		if(detectaColisionJugador(mapa, jugador, tamanoBaldosa)) {
			jugador.cambiarVidas(-1);
		}
		while(detectaColisionJugador(mapa, jugador, tamanoBaldosa)) {
			y+=1;
		}
		break;
		}
		if(detectaColision(mapa, tamanoBaldosa) || x<0 || x>16*tamanoBaldosa || y>12*tamanoBaldosa || y<0 ) {
			switch(this.direccion) {
				case 1,3: y-=velocidad;
				break;
				case 2,4: y+=velocidad;
				break;
			}
			Random r = new Random();
			direccion = r.nextInt(1,5);
		}
	}

}
