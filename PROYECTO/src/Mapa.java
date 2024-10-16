import java.awt.Color;
import java.awt.Graphics2D;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Mapa {
	private int[][] celda;
	private int numCelda;

	public Mapa(int[][] celda, int numCelda) {
		super();
		this.celda = celda;
		this.numCelda = numCelda;
	}

	public Mapa(int[][] celda) {
		super();
		this.celda = celda;

	}

	public Mapa(int numCelda) {
		super();
		this.numCelda = numCelda;
		this.celda = cargarCelda("src\\mapa.txt", numCelda);
	}
/**
 * Este metodo devuelve el array que representa la celda actual
 * @return array int[][] de la celda actual
 */
	public int[][] getCelda() {
		return celda;
	}
/**
 * Este metodo establece el array que representa a la celda actual
 * @param celda La celda que se quiere establecer como actual
 */
	public void setCelda(int[][] celda) {
		this.celda = celda;
	}
/**
 * Este metodo devuelve el numero de la celda actual
 * @return Numero de la celda actual
 */
	public int getNumcelda() {
		return numCelda;
	}
/**
 * Este metodo establece el numero de la celda actual
 * @param numCelda El numero de celda que se quiere establecer
 */
	public void setNumcelda(int numCelda) {
		this.numCelda = numCelda;
	}
	/**
	 * Este método carga la celda indicada en el parámetro num
	 * @param celda La dirección de el fichero que contiene toda la celda
	 * @param num La celda a cargar
	 * @return Una celda int[][] sacada del indice correspondiente de un fichero
	 */
	public int[][] cargarCelda(String celda, int num) {
		//Creo un array donde se guardarán los números
		int[][] map = new int[16][12];
		try {
			//Leo el fichero
			Scanner sc = new Scanner(new FileInputStream(celda));
			while (sc.hasNextLine()) {
				//Si la linea actual contiene el indice que busco, la escaneo
				if (sc.nextLine().contains("-" + num)) {
					for (int i = 0; i < 12; i++) {
						String linea = new String(sc.nextLine());
						//Divido cada linea en sus números y los voy metiendo al array
						try {
						StringTokenizer st = new StringTokenizer(linea, ";");
						for (int j = 0; j < 16; j++) {
							map[j][i] = Integer.parseInt(st.nextToken());
						}
					}catch (NumberFormatException e){
						e.printStackTrace();}
					}
					break;
				}
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * Este metodo se ocupa de dibujar la celda actual del mapa en pantalla.
	 * 
	 * @param g             El objeto Graphics2D que se va a utilizar.
	 * @param tamanoBaldosa El tamaño de cada baldosa del juego.
	 */
	public void dibujarCelda(Graphics2D g, int tamanoBaldosa) {
		// Recorro todo el array que contienen la celda
		for (int i = 0; i < 12; i++) {
			for (int j = 0; j < 16; j++) {
				switch (celda[j][i]) {
				// Por cada celda, dependiendo de su número, voy dibujando bloques donde
				// corresponde
				case 1:
					g.setColor(Color.GREEN);
					g.fillRect(j * tamanoBaldosa, i * tamanoBaldosa, tamanoBaldosa, tamanoBaldosa);
					break;
				case 2:
					g.setColor(Color.GREEN.darker());
					g.fillRect(j * tamanoBaldosa, i * tamanoBaldosa, tamanoBaldosa, tamanoBaldosa);
					break;
				default:
					g.setColor(Color.GRAY);
					g.fillRect(j * tamanoBaldosa, i * tamanoBaldosa, tamanoBaldosa, tamanoBaldosa);
					break;
				}
			}
		}
	}

}
