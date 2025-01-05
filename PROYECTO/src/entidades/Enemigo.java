package entidades;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Random;
import javax.swing.table.DefaultTableModel;

import gui.Inventario;
import gui.Mensaje;
import main.AudioPlayer;
import main.GamePanel;
import main.Mapa;

public abstract class Enemigo extends Personaje{
	GamePanel gp;
	private int vida;
	private int direccion;
	private HashMap<String,Integer> objetosLooteados;
	AudioPlayer loot = new AudioPlayer("Resources/audio/lootCoin.wav");
	
	public Enemigo(int vida, GamePanel gp) {
		super();
		this.vida = vida;
		this.gp =gp;
		Random r = new Random();
		this.direccion = r.nextInt(1, 5);//num aleatorio del 1 al 4	
		}	

	
	public int getVida() {
		return vida;
	}

	public void setVida(int vida) {
		if(vida<0) {
			vida=0;
		}
		this.vida = vida;
	}
	
	
	public void dibujarEnemigo(Graphics2D g2) {
		g2.setColor(new Color(50*vida,0,0));

		g2.fillRect(x, y, gp.tamañoBaldosa, gp.tamañoBaldosa);

		//BufferedImage imagen = null;
		
		
		//g2.drawImage(imagen, x, y, gp.tamañoBaldosa, gp.tamañoBaldosa, null);
	}
	//generador aleatorio de movimiento, mediante una Random que genera un numero del 1 al 4
	//Dependiendo del numero, se moverá en un eje, y en caso de que haya colisión, el jugador se moverá al lado contrario
	public void movimiento(Mapa mapa, int tamanoBaldosa, Jugador jugador) {
		
		switch(this.direccion) {
		case 1: x+=velocidad;
		if(detectaColisionJugador(mapa, jugador, tamanoBaldosa)) {
			jugador.cambiarVidas(-1);
		}
		while(detectaColisionJugador(mapa, jugador, tamanoBaldosa)) {
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
		}
		if (detectaColision(mapa,tamanoBaldosa) || x<0 || x>16*tamanoBaldosa || y> 12*tamanoBaldosa || y<0) {
			switch(this.direccion) {
			case 1: x-=velocidad;
			break;
			case 2: x+=velocidad;
			break;
			case 3: y-=velocidad;
			break;
			case 4: y+=velocidad;
			break;
			}
			Random r = new Random();
			direccion = r.nextInt(1, 5);
		}
	
	}
	//colisiones iguales a las del jugador (no detecta ni al jugador ni a la espada todavía, por lo cual la variable de vida no es utilizada)
	public boolean detectaColision(Mapa mapa, int tamanobaldosa) {
		// se usa para saber en que celda esta el jugador
		int celdaX = (x + 32) / tamanobaldosa;
		if (x < -32) {
			celdaX = mapa.getCelda().length - 1;
		}
		int celdaY = (y + 32) / tamanobaldosa;
		if (y < -32) {
			celdaY = mapa.getCelda()[0].length - 1;
		}
		//System.out.println("Posicionjugador:" + celdaX + "," + celdaY + ", Posicion coords: " + x + "," + y);
		// Recorro el array celda completo

		for (int i = 0; i < mapa.getCelda().length; i++) {
			for (int j = 0; j < mapa.getCelda()[i].length; j++) {
				// Por cada baldosa no vacía, calcula si está colisionando de algún modo con el
				// personaje, USANDO LA LISTA QUE TENEMOS DE NUMEROS COLISIONABLES
				if (Mapa.zonasConColision.contains(mapa.getCelda()[i][j])) {
					if (((((x <= (i * tamanobaldosa) + tamanobaldosa) && (x >= i * tamanobaldosa))
							// Compruebo si hay colisión en X con la esquina derecha
							|| ((x + tamanobaldosa <= (i * tamanobaldosa) + tamanobaldosa)
									&& (x + tamanobaldosa >= i * tamanobaldosa)))
							// Compruebo si hay colisión en Y por arriba
							&& (((y <= (j * tamanobaldosa) + tamanobaldosa) && (y >= j * tamanobaldosa))
									// Compruebo si hay colisión en Y por abajo
									|| ((y + tamanobaldosa <= (j * tamanobaldosa) + tamanobaldosa)
											&& (y + tamanobaldosa >= j * tamanobaldosa))))) {
						//System.out.println("Hay colision");
						return true;
					}
				}
			}
		}
		return false;
	}
	public boolean detectaColisionJugador(Mapa mapa,Jugador jugador, int tamanobaldosa) {
		if (((((x <= (jugador.getX()) + tamanobaldosa) && (x >= jugador.getX()))
				// Compruebo si hay colisión en X con la esquina derecha
				|| ((x + tamanobaldosa <= jugador.getX() + tamanobaldosa)
						&& (x + tamanobaldosa >= jugador.getX())))
				// Compruebo si hay colisión en Y por arriba
				&& (((y <= jugador.getY() + tamanobaldosa) && (y >= jugador.getY()))
						// Compruebo si hay colisión en Y por abajo
						|| ((y + tamanobaldosa <= jugador.getY() + tamanobaldosa)
								&& (y + tamanobaldosa >= jugador.getY()))))) {
				Random r = new Random();
				int randomD = r.nextInt(1, 5);
				//Evito que al cambiar la dirección escoja la misma en la que ya estaba yendo
				while (randomD == direccion) {
					randomD = r.nextInt(1, 5);
				}
				direccion = randomD;
			return true;
		}
		return false;
		
	}
	//no se llama a este método, se llama al de el Slime, Esqueleto, etc
	public void looteoEnemigo(Inventario inventario, Jugador jugador, GamePanel gp) {
		//este hashMap vacío se va a llenar con {Nombre de item, Cantidad de item}
		 objetosLooteados= new HashMap<String,Integer>();
		Random r = new Random();
		int oroRecibido = 0;
		
		//LOOT SLIME
		if (this instanceof Slime) {
			//el jugador recibirá entre 1 y 10 de oro por matar un Slime
			oroRecibido+=r.nextInt(1, 11);
			if (r.nextInt(1, 11)==1) {
				//con un 10% de probabilidad suelta una manzana (la cantidad es 1)
				objetosLooteados.put("Manzana", 1);
				
			}
		}
		//se añade el oro del enemigo eliminado
		loot.playClip(gp.getVolumenAudio());
		objetosLooteados.put("Oro",oroRecibido);
		//actualizamos el inventario
		actualizarInventario(inventario, objetosLooteados);
		anadirMensajesLoot(objetosLooteados, gp);
		
	}
	

	
	//se añaden los items al inventario
	private void actualizarInventario(Inventario inventario,  HashMap<String, Integer> objetosLooteados) {
//		DefaultTableModel nuevoInventario = Inventario.crearModeloFichero("src/inventario.txt");
//		for (String clave : objetosLooteados.keySet()) {
//			boolean elementoEncontrado = false;
//				for (int row = 0; row < nuevoInventario.getRowCount(); row++) {
//					String elemento = (String) nuevoInventario.getValueAt(row, 1);
//					if (nuevoInventario.getValueAt(row, 0).equals(clave)) {
//						elementoEncontrado = true;
//						int elementoASumar = Integer.parseInt(elemento);
//						elementoASumar += objetosLooteados.get(clave);
//						nuevoInventario.setValueAt(String.valueOf(elementoASumar), row, 1);		
//					} 
//				
//				}	
//				if (!elementoEncontrado) {
//					String[] nuevaFila = {clave, objetosLooteados.get(clave).toString()};
//					nuevoInventario.addRow(nuevaFila);
//				}
//				
//				
//		}
//		Inventario.cargarFichero(nuevoInventario);
		for (String clave : objetosLooteados.keySet()) {
		if (gp.getJugador().getInventario().containsKey(clave)) {
			gp.getJugador().getInventario().put(clave, gp.getJugador().getInventario().get(clave)+objetosLooteados.get(clave));
		}else {
			gp.getJugador().getInventario().put(clave, objetosLooteados.get(clave));
		}
	} 
	}
	
	private void anadirMensajesLoot(HashMap<String, Integer> objetosLooteados, GamePanel gp) {
		for (String nombreObjeto : objetosLooteados.keySet()) {
			String mensaje = "Has obtenido "+nombreObjeto+ " x"+objetosLooteados.get(nombreObjeto)+"!";
			
			gp.anadirMensaje(new Mensaje(mensaje, 180));
		}
	}
}
	

	

