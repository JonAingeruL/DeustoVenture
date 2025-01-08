package main;
import java.awt.Color;
import entidades.*;
import gui.*;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;


import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;
	// AJUSTES DE PANTALLA
	final int tamañoOriginalBaldosa = 16; // Una baldosa de 16x16 (una baldosa en la medida en pixeles para crear un
											// personaje, ncp...)
	// Como el tamaño es muy pequeño para las pantallas actuales hay que escalarlo
	final int escala = 4; // De esta manera escalara en x3 los 16 pixeles, para que se vea mas grande

	public final int tamañoBaldosa = tamañoOriginalBaldosa * escala; // Con esto escalamos el tamaño original a 64x64 y es lo
																// que aparecera en pantalla
	

	// Para ajustar el tamaño de la pantalla tenemos quen saber cuantas baldosas
	// pueden entrar horizontal y verticalmente
	public final int maxPantallaColu = 16; // 16 baldosas en la parte de arriba cada una con un numero igual de pixeles
	public final int maxPantallaFila = 12; // 12 baldosas en cada fila, lo que hara una pantalla de 16x12 con cada baldosa
									// de 64 pixeles
	// Ahora creamos dos variables para ponerle la altura y la anchura de la
	// pantalla
	final int pantallaAnchuta = maxPantallaColu * tamañoBaldosa; // 1024 pixeles de ancho
	final int pantallaAltura = maxPantallaFila * tamañoBaldosa; // 768 pixeles de alto
	private Inventario inventarioJugador;
	private long tiempoPausa = 0;
	private float volumenAudio = 1;
	private float volumenMusica = 1f;
	private MusicPlayer musicPlayer = new MusicPlayer("Resources/musica/1.mp3");
	// establecemos los fps como variable
	// FPS
	int FPS = 60;
	int contadorTiempo =60;

	// Ya tenemos la pantalla creada, pero todos los juegos 2d de aventuras tienen
	// tiempo, hay enemigos que se mueven etc... por eso hay que hacer un reloj (los
	// fps) para establecer como se van a mover las cosas en el jeugo
	// Para eso vamos a utilzar una clase que se llama Thread (hilos)
	// Thread es algo que se puede iniciar y parar, y una vez lo inicias, no parara
	// hasta que lo pares tu
	// Es necesario ya que necesitamos que el juego este continuamente a 60fps todo
	// el rato, tiene que dibujar la pantalla 60 veces cada segundo todo el rato
	Thread gameThread;
	// Para correr esto necesitamos implementar Runnable la clase

	// Una vez creada la clase ManejoTeclado y programar el movimineto AWSD hay que
	// instanciar la clase
	ManejoTeclado tecladoM = new ManejoTeclado();
	
	

	public Jugador getJugador() {
		return jugador;
	}

	public float getVolumenAudio() {
		return volumenAudio;
	}

	public void setVolumenAudio(float volumen) {
		this.volumenAudio = volumen;
	}
	public float getVolumenMusica() {
		return volumenMusica;
	}

	public void setVolumenMusica(float volumen) {
		this.volumenMusica = volumen;
	}
	

	public MusicPlayer getMusicPlayer() {
		return musicPlayer;
	}

	public void setMusicPlayer(MusicPlayer musicPlayer) {
		this.musicPlayer = musicPlayer;
	}

	// Despues de crear el ManejoTeclado y los controles de movimiento del juego y
	// hacer que lo pueda leer el GamePanel, hay que crear el mapa y el jugador
	
	// Creo el jugador
	// TODO Pensar el esquema de herencias de la clase personaje y crear una clase
	// personajeJugable (PJ) que sea la que controla el jugador
	Jugador jugador = new Jugador(this, tecladoM);
	//Creo el mapa
		Mapa mapa = new Mapa(1,this);
	HashMap<String,ArrayList<Enemigo>> enemigos;
	int contadorBoss = 180;
	
	HashMap<String,ArrayList<NPC>> npcs;
	
	
	
	
	public Mapa getMapa() {
		return mapa;
	}

	public void setMapa(Mapa mapa) {
		this.mapa = mapa;
	}

	private ArrayList<Mensaje> mensajes = new ArrayList<Mensaje>();
	
	
	
	
	// Creamos un constructor de este GamePanel
	public GamePanel(String usuarioActual, Boolean esnuevoUsuario, GestorBD gbd) {
		//creo un gestor nuevo en gamePanel
		
		//hago que el jugador tenga este nombre (se utiliza ya que el gameOverScreen utiliza a un jugador
		jugador.setNombreJugador(usuarioActual);
		jugador.setInventario(Jugador.leerBD(gbd, usuarioActual));
		if (esnuevoUsuario) {
			//en caso de ser un usuarioNuevo (se verifica antes), se ponen todos los valores en 0
			jugador.setEnemigosDerrotados(0);
			jugador.setNumMuertes(0);
			jugador.setTiempoJugado(0);
			//si está el jugador ya en la base de datos, se le cambian los valores a 0 (ya que es una nueva partida)
			if (gbd.existeUsuario(usuarioActual)) {
				gbd.actualizarUsuario(new Usuario(usuarioActual,0,0,0));
				


				//en caso de que no, se añade un nuevo usuario a la BD
			} else {
				gbd.guardarUsuarioConValidacion(new Usuario(usuarioActual,0,0,0));	
				
				
				
			}
			if (gbd.existeUsuarioPos(usuarioActual)) {
				gbd.resetearPosicionUsuario(usuarioActual, "resources/mapas/tutorial.txt");
			} else {
				gbd.insertarPosicionUsuario(usuarioActual, 475, 400, 1, 0, "resources/mapas/tutorial.txt");
			}
		} else { //en caso de que le demos a continuar
			Usuario u = gbd.obtenerEstadisticasUsuario(usuarioActual);
			jugador.setEnemigosDerrotados(u.getNumAsesinatos());
			jugador.setNumMuertes(u.getNumMuertes());
			jugador.setTiempoJugado(u.getTiempoJugado());
			
		}
		
		this.setPreferredSize(new Dimension(pantallaAnchuta, pantallaAltura)); // Esto establece el tamaño de esta clase
																				// (JPanel)
		this.setBackground(Color.black); // Para que el fondo de la pantalla sea negro (esto no importa mucho porque no
											// se va a ver)
		this.setDoubleBuffered(true); // si se establece en true, todo el dibujo de este componente se realizará en un
										// búfer de pintura fuera de la pantalla, basicamente lo que hara en mejorar la
										// renderizacion del juego
		// Una vez instaciada la clase ManejoTeclado la añadimos al GamePanel
		this.addKeyListener(tecladoM); // De esta maenera el GamePanel podra reconocer la entrada de teclas
		// Y vamos a añadir esto para que el GamePanel este "centrado" en recibir
		// entrada de teclado
		this.setFocusable(true);
		

	}

	// vamos a crear un nuevo metodo para iniciar el juego
	public void iniciarJuegoHilo(String usuarioActual, Boolean esNuevoUsuario, GestorBD gbd) {
		
		InventarioCofre.inicializarLoot();
		//Cargamos un txt de enemigos ya hecho
		cargarEmemigos();
		//Cargamos un txt de npcs ya hecho 
		cargarNPCs();
		
		musicPlayer.playMusic();
		//cargamos el primer mapa XD
		if (esNuevoUsuario) {
			mapa.updateMapa(tamañoBaldosa);
		} else {
			 //datosACargar esta ordenado así: {usuario, x,y,numCelda,numMapa, direccionMapa}
			List<Object> datosACargar = gbd.buscarDatosUsuarioPOS(usuarioActual);
			boolean empiezaDentroDeMazmorra;
			String archivo = (String) datosACargar.get(5);
			if (archivo.equals("resources/mapas/mapa.txt")) {
				empiezaDentroDeMazmorra = false;
			} else {
				empiezaDentroDeMazmorra= true;
			}
			jugador.cambioMapa((String) datosACargar.get(5), (Integer) datosACargar.get(3), (Integer) datosACargar.get(4), tamañoBaldosa, (Integer) datosACargar.get(1), (Integer) datosACargar.get(2), empiezaDentroDeMazmorra, mapa);
		}
		
		// Vamos a instanciar el gameThread
		gameThread = new Thread(this); // Cuando ponemos this nos referimos a esta clase (GamePanel), basicamente
										// estamos pasando la clase GamePanel a este constructor de hilo para
										// instanciarlo.
		
		// Ahora vamos a iniciar el hilo
		gameThread.start(); // Esto automaticamente llamara al metodo run() que nos puso auto el Runnable
	}

	// Despues de implementar Runnable automaticamente nos metera este metodo
	// Nada mas iniciemos el gameThread, llamara directamente a este metodo
	@Override
	public void run() {
		// Primero creamos varibales fuera del bucle
		double dibujarIntervalo = 1000000000 / FPS; // Ese numero esta en nanosegundos por lo que es 1seg, esto
													// significa que vamos a dibujar la pantalla cada 0,016seg, por lo
													// que podemos dibujar en la pantalla 60 veces por segundo

		double tiempoDibujadoSiguiente = System.nanoTime() + dibujarIntervalo; // Con esto hacemos que despues del
																				// primer dibujado, haga el siguientte
																				// 0,016 segundos mas tarde

		// En este metodo vamos a crear un gameLoop que va a ser el nucleo de nuestro
		// juego
		while (!gameThread.isInterrupted()) { // Esto significa que mientras el hilo exista se va a repetir el proceso que
										// este escrito en este bucle
//			System.out.println("El bucle se esta ejecurando"); //esto lo usamos para comprobar si estamos en el bucle

			// Una vez tenemos el movimiento de la caja y todo, cuando movemos el objeto con
			// la WASD el objeto desaparecera de la pantalla nada mas darle, esto se debe a
			// que los ordenadores modernos son demasiado rapidos y hacen el proceso de
			// moverlo miles de veces por segundo
			// Por eso hay que ponerle una limitacion para que no lo haga tan rapido, como
			// pueden ser 60fps
			// Para esto necesitamos saber la hora que es y tambien el tiempo que pasa desde
			// que hace el metodo update() hasta el repaint()
			// Para saber que hora es utilizamos esto
			// long horaActual = System.nanoTime(); //vamos a utilizar los nanosegundos para
			// comprobar la hora del sistema con exactitud
			// Ahora toca establecer los fps, asi que hacemos una variable
			// Despues de las variables hay que crear otras variables fuera del bucle
			
			
			// Ahora vamos a hacer dos cosas en el bucle

			// 1 ACTUALIZACION: actualizar informacion como la posicion del personaje (Si el
			// jugador esta en una cordenada y mueve el personaje hacia abajo ajustar la
			// cordenada)
			update();

			// 2 DIBUJAR: dibuajar la pantalla con la informacion actualizada (Como el
			// jugador ha movido el personaje hacia abajo hay que dibujarlo en la nueva
			// posicion)
			repaint(); // Es confuso, pero es la unica manera de llamar al metodo paintComponent

			// Ahora mientras el bucle se este ejecutando va a actualizar y repintar el
			// juego

			try {
				// Necesitamos saber cuanto tiempo nos queda despues de que se actualize y
				// repinte el juego, por lo que vamos a comprobarlo
				double tiempoRestante = tiempoDibujadoSiguiente - System.nanoTime()+ tiempoPausa;
				// Una putada es que hemos calculado todo en nanosegundos para mas precision,
				// pero sleep solo detecta milisegundos por lo que hay que transformarlo
				tiempoRestante = tiempoRestante / 1000000;

				// Creamos un if para que si la actu y el repin tardaron mas que el Intervalo de
				// dibujado no quede tiempo, el hilo no necesita dormir, por eso le ponemos 0
				if (tiempoRestante < 0) {
					tiempoRestante = 0;
				}

				// Como sobra tiempo, hay que hacer que el hilo "descanse", basicamente que no
				// haga nada hasta que pasen los 0,016 segundos
				Thread.sleep((long) tiempoRestante); // Sleep pausa el GameLoop, por lo que no hara nada hasta que el
														// tiempo restante haya terminado

				// Cuando se despierta el hilo, vamos a establecer un nuevo
				// tiempoDibujadoSiguiente
				tiempoDibujadoSiguiente += dibujarIntervalo+tiempoPausa; // 0.016 segundos despues del dibujado hara el siguiente
				tiempoPausa=0;
			} catch (InterruptedException e) {
				
				Thread.currentThread().interrupt();
			}

		}
		System.out.println("Se acabo");
		musicPlayer.stopMusic();
	}

	// Para hacer las dos cosas dentro del bucle tenemos que crear dos metodos
	public void update() {
		
		contadorTiempo--;
		if(contadorTiempo==0) {
			jugador.setTiempoJugado(jugador.getTiempoJugado()+1);
			contadorTiempo=60;
		}
		musicPlayer.cambiarVolumen(volumenMusica);
		// Recomiendo comentar jugador.muerte para debugging
		jugador.muerte(this, gameThread);
		// El personaje tiene una función movimiento a la que llamamos ahora
		jugador.movimiento(tecladoM, mapa, tamañoBaldosa, enemigos, npcs);
		jugador.interaccion(tecladoM,mapa);
		jugador.AccionAtacar(enemigos, mapa, tamañoBaldosa, tecladoM, null,inventarioJugador,mensajes);
		
		//detectamos el cambio de movimiento de cada Enemigo
		if (enemigos.containsKey(mapa.getNumeroMapa()+","+mapa.getNumcelda())) {
			for (Enemigo enemigo : enemigos.get(mapa.getNumeroMapa()+","+mapa.getNumcelda())) {
				if (enemigo instanceof Boss) {
					Boss finalBoss = (Boss) enemigo;
					//mediante el contadorBoss, se controla que el boss cuando este sea 0, se cambie el patrón de movimiento a otro aleatorio, pa que el boss sea mas dificil de dar.
					if (contadorBoss ==0) {
						finalBoss.movimientoBoss(mapa, tamañoBaldosa, jugador, contadorBoss);
						contadorBoss=180;
					} else {
						contadorBoss--;
						finalBoss.movimientoBoss(mapa, tamañoBaldosa, jugador, contadorBoss);
					}
					
					
				} else {
					enemigo.movimiento(mapa,tamañoBaldosa, jugador);
				}
				
			}
		}
		//controla si el inventario se puede abrir o no, para que no se abran mas de un inventario (controla que también se le haya dado a la I para abrirlo)
		if(tecladoM.iPulsado && !tecladoM.abrirInventario) {
			tecladoM.abrirInventario = true;
			pararMovimiento();
			inventarioJugador = new Inventario(tecladoM, this, jugador);
			//Se para el update mientras el inventario está abierto, el cerrar el inventario con la tecla ESC está dentro de Inventario.java
		}
		//La idea con este código es que el botón esc sea una pausa real en el juego.
		//De esta manera, cuando implementemos enemigos también se pararán
		if(tecladoM.escPulsado && !tecladoM.abrirPausa) {
			pararMovimiento();
			tecladoM.escPulsado  = false;
			tecladoM.abrirPausa = true;
			this.tiempoPausa = System.nanoTime();

			MenuPausa pausa = new MenuPausa(this);
			while (pausa.isOpen()==true) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Thread.currentThread().interrupt();
				}
				
			}
			tecladoM.abrirPausa = false;
			this.tiempoPausa = System.nanoTime()-tiempoPausa;
		}
		
		//Después comprobamos si el personaje ha cambiado de celda
		mapa.detectarCambio(jugador, tamañoBaldosa);
		
//		if (jugador.detectaColisionNpcs(npcs, tamañoBaldosa, mapa)) {
//	        jugador.hablarConNPC = true; // Activar la bandera si hay un NPC cerca
//	    } else {
//	        jugador.hablarConNPC = false; // Desactivarla si no hay colisión
//	    }

	}

	// Esto es un metodo ya establecido en java en el JPanel
	public void paintComponent(Graphics g) { // Graphics es una clase que tiene muchas funciones para pintar objetos en
										// pantalla
		

		super.paintComponent(g); // Esto es un formato que tiene java, cada vez que usemos el painComponent hay
									// que poner esto
		// Vamos a usar este metodo para dibujar y la clase grafico
		// La clase de Graphics va a ser como nuestro lapiz para dibujar
		// Pero antes de usarlo hay que covertirlo en 2D

		Graphics2D g2 = (Graphics2D) g; // La clase Graphics2D extiende de la clase Graphics para proporcionar un
										// control mas sofisticado sobre la geometria, transformacion de coordenadas,
										// manejo del color y el layout de los textos
		//Antes de dibujar al personaje, dibujamos el mapa para que el personaje siempre se pinte encima
		
		mapa.dibujarImagen(g2);
		jugador.dibujarPer(g2);
		jugador.dibujarInterfaz(g2);
		jugador.dibujarInteraccion(g2);
		//vamos a pintar todos los enemigos
		if (enemigos.containsKey(mapa.getNumeroMapa()+","+mapa.getNumcelda())) {
			for (Enemigo enemigo : enemigos.get(mapa.getNumeroMapa()+","+mapa.getNumcelda())) {
				enemigo.dibujarEnemigo(g2);
			}
		}
		
		//muestra tiempo de juego TOTAL
		g2.setColor(Color.BLACK);
		//g2.drawString(""+jugador.getTiempoJugado()+"", 60, 60);
		//dibuja los mensajes de looteo
		int alturaDibujadoTexto =700;

		for (Mensaje mensaje : mensajes) {
			g2.setColor(mensaje.getColorMensaje());
				g2.drawString(mensaje.getTexto(), 750, alturaDibujadoTexto);
				alturaDibujadoTexto+=20;
		
			
			mensaje.setTiempodeVida(mensaje.getTiempodeVida()-1);
			
		}
		
		for (int i=0; i<mensajes.size();) {
			if (mensajes.get(i).getTiempodeVida()==0){
				mensajes.remove(i);
			} else {
				i++;
			}
		}
		if (npcs.containsKey(mapa.getNumeroMapa()+","+mapa.getNumcelda())) {
			for (NPC npc : npcs.get(mapa.getNumeroMapa()+","+mapa.getNumcelda())) {
				npc.dibujarNpc(g2);
			}
		}


		g2.dispose(); // Esto sirve para ahorrar memoria en el dibujado
	}
	
	//metodo que para todo el movimiento, de esta manera "pausando" el juego
	public void pararMovimiento() {
		tecladoM.abajoPulsado = false;
		tecladoM.derechaPulsado = false;
		tecladoM.izquierdaPulsado = false;
		tecladoM.arribaPulsado = false;
		
	}
	
	private void cargarEmemigos() {
		enemigos = new HashMap<String, ArrayList<Enemigo>>();
		try (Scanner sc = new Scanner(new FileInputStream("Resources/mapas/mapaEnemigos.txt"))) {
			while (sc.hasNextLine()) {
				String linea = sc.nextLine();
				if (!linea.trim().startsWith("//")) {
					String[] campos = linea.split(";");
					String numMapa = campos[0];
					String numCelda = campos[1];
					String clave = numMapa+","+numCelda;
					int posXEnemigo = Integer.parseInt(campos[3])*tamañoBaldosa;
					int posYEnemigo = Integer.parseInt(campos[4])*tamañoBaldosa;
					Enemigo e;
					switch (campos[2]) {
					//al añadir un enemigo nuevo, se tiene que meter a este dentro del case
					case "Slime": e = new Slime(posXEnemigo,posYEnemigo,this);
					break;
					case "Dummy": e = new Dummy(posXEnemigo, posYEnemigo, this);
					break;
					case "Boss": e = new Boss(posXEnemigo,posYEnemigo,this);
					break;
					default: e= new Slime(posXEnemigo, posYEnemigo, this);
					}
					if (!enemigos.containsKey(clave)) {
						enemigos.put(clave, new ArrayList<Enemigo>());
					}
					enemigos.get(clave).add(e);
					
			
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	
	private void cargarNPCs() {
		npcs = new HashMap<String, ArrayList<NPC>>();
		try (Scanner sc = new Scanner(new FileInputStream("Resources/mapas/NPCs.txt"))) {
			while (sc.hasNextLine()) {
				String linea = sc.nextLine();
				if (!linea.trim().startsWith("//")) {
					String[] campos = linea.split(";");
					String numMapa = campos[0];
					String numCelda = campos[1];
					String clave = numMapa+","+numCelda;
					int posXNpc = Integer.parseInt(campos[2])*tamañoBaldosa;
					int posYNpc = Integer.parseInt(campos[3])*tamañoBaldosa;
					String frase = campos[4];
					NPC n = new NPC(posXNpc, posYNpc, frase, this);
					
					if (!npcs.containsKey(clave)) {
						npcs.put(clave, new ArrayList<NPC>());
					}
					npcs.get(clave).add(n);
					
			
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	
	public void anadirMensaje(Mensaje mensaje) {
		mensajes.add(mensaje);
	}

}
