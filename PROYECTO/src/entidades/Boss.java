package entidades;

import java.util.Random;


import main.GamePanel;
import main.Mapa;

public class Boss extends Enemigo {
	
	public Boss(int x, int y, GamePanel gp) {
		super(350, gp);
		this.setNombre("JavaBoss");
		velocidad = 4;
		this.x = x;
		this.y =y;
		Random r = new Random();
		this.direccion = r.nextInt(1, 9);
	}
	
	//mismo metodo de enemigo, pero con 4 movimientos verticales añadidos y el doble de velocidad si solo se mueve por el eje x
	public void movimientoBoss(Mapa mapa, int tamanoBaldosa, Jugador jugador,int contador) {
		
		switch(this.direccion) {
		case 1: x+=velocidad;
		if(detectaColisionJugador(mapa, jugador, tamanoBaldosa)) {
			jugador.cambiarVidas(-1);
		}
		while(detectaColisionJugador(mapa, jugador, tamanoBaldosa)) {
			x-=1;
		}
		while(detectaColision(mapa, tamanoBaldosa)) {
			x-=1;
		}
		break;
		case 2: x-=velocidad;
		if(detectaColisionJugador(mapa, jugador, tamanoBaldosa)) {
			jugador.cambiarVidas(-1);
		}
		while(detectaColisionJugador(mapa, jugador, tamanoBaldosa)) {
			x+=1;
		}
		break;
		case 3: y+=velocidad;
		if(detectaColisionJugador(mapa, jugador, tamanoBaldosa)) {
			jugador.cambiarVidas(-1);
		}
		while(detectaColisionJugador(mapa, jugador, tamanoBaldosa)) {
			y-=1;
		}
		break;
		case 4: y-=velocidad;
		if(detectaColisionJugador(mapa, jugador, tamanoBaldosa)) {
			jugador.cambiarVidas(-1);
		}
		while(detectaColisionJugador(mapa, jugador, tamanoBaldosa)) {
			y+=1;
		}
		break;
		case 5: x+=velocidad; y+=velocidad;
		if(detectaColisionJugador(mapa, jugador, tamanoBaldosa)) {
			jugador.cambiarVidas(-1);
		}
		while(detectaColisionJugador(mapa, jugador, tamanoBaldosa)) {
			x-=1; y-=1;
			
		}
		break;
		case 6: x-=velocidad; y+=velocidad;
		if(detectaColisionJugador(mapa, jugador, tamanoBaldosa)) {
			jugador.cambiarVidas(-1);
		}
		while(detectaColisionJugador(mapa, jugador, tamanoBaldosa)) {
			x+=1; y-=1;
		}
		break;
		case 7: x+=velocidad; y-=velocidad;
		if(detectaColisionJugador(mapa, jugador, tamanoBaldosa)) {
			jugador.cambiarVidas(-1);
		}
		while(detectaColisionJugador(mapa, jugador, tamanoBaldosa)) {
			x-=1;y+=1;
		}
		break;
		case 8: x-=velocidad; y-=velocidad;
		if(detectaColisionJugador(mapa, jugador, tamanoBaldosa)) {
			jugador.cambiarVidas(-1);
		}
		while(detectaColisionJugador(mapa, jugador, tamanoBaldosa)) {
			x+=1;y+=1;
		}
		break;
		}
		if (detectaColision(mapa,tamanoBaldosa) || x<0 || x>16*tamanoBaldosa || y> 12*tamanoBaldosa || y<0 || detectarContador(contador)) {
			switch(this.direccion) {
			case 1: x-=velocidad;
			break;
			case 2: x+=velocidad;
			break;
			case 3: y-=velocidad;
			break;
			case 4: y+=velocidad;
			break;
			case 5: x-=velocidad; y-=velocidad;
			break;
			case 6: x+=velocidad; y-=velocidad;
			break;
			case 7: x-=velocidad; y+=velocidad;
			break;
			case 8: x+=velocidad; y+=velocidad;
			break;
			}
			Random r = new Random();
			direccion = r.nextInt(1, 9);
		}
		
			

		
	}
	 //método que sirve para hacer que el enemigo pare pasado X tiempo (si el contador es 60, el jugador parará después de 1 segundo)
	public boolean  detectarContador(int contador) {
		if (contador ==0) {
			return true;
		} else {
			return false;
		}
	}


	
	
	

}
