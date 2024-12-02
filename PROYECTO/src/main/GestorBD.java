package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class GestorBD {

	protected static String DRIVER_NAME;
	protected static String DATABASE_FILE;
	protected static String CONNECTION_STRING;
	
	public GestorBD() {
		
		//Carga la configuracion de base de datos del fichero de propiedades
		try (FileInputStream f = new FileInputStream("config/database.properties")){
			//Carga el archivo
			Properties p = new Properties();
			p.load(f);
			
			//Tenemos los valores en p
			DRIVER_NAME = p.getProperty("DRIVER_NAME");
			DRIVER_NAME = p.getProperty("DATABASE_FILE");
			DRIVER_NAME = p.getProperty("CONNECTION_STRING") + DATABASE_FILE;
			
			
		} catch (IOException e) {
			System.out.println("Error, no se pudo leer el fichero de la configuracion");
		}
		
		try {
			//Caragar el driver SQLite
			Class.forName(DRIVER_NAME);
		} catch (ClassNotFoundException ex) {
			System.err.format("\n* Error al cargar el driver de BBDD: %s", ex.getMessage());
			ex.printStackTrace();
		}
		
		
	}
}
