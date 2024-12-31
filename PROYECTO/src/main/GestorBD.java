package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
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
			DATABASE_FILE = p.getProperty("DATABASE_FILE");
			CONNECTION_STRING = p.getProperty("CONNECTION_STRING") + DATABASE_FILE;
			
			
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
	
	public void CrearBBDD() {
		//Se abre la conexion y se obtiene el Statement
		//Al abrir la conexion, si no existia el fichero, se crea la base de datos
		
		try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
				Statement stmt = con.createStatement()) {
			 // Define la estructura de la tabla
			String sql = "CREATE TABLE IF NOT EXISTS USUARIO (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nomUsuario TEXT NOT NULL, " +
                    "numMuertes INTEGER DEFAULT 0, " +
                    "numAsesinatos INTEGER DEFAULT 0, " +
                    "tiempoJugado INTEGER DEFAULT 0" +
                    ");";
			
			 // Ejecuta la consulta para crear la tabla
            stmt.executeUpdate(sql);
            System.out.println("Tabla USUARIO creada o ya existía.");
			
		} catch (Exception e) {
			System.err.format("\n* Error al crear la tabla en la base de datos: %s", e.getMessage());
            e.printStackTrace();
		}
		
	}
	
	public int guardarUsuario(String nomUsuario, int numMuertes, int numAsesinatos, int tiempoJugado) {
        String sql = "INSERT INTO USUARIO (nomUsuario, numMuertes, numAsesinatos, tiempoJugado) VALUES (?, ?, ?, ?)";
        int generatedId = -1;

        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            // Configura los valores para la consulta
            pstmt.setString(1, nomUsuario);
            pstmt.setInt(2, numMuertes);
            pstmt.setInt(3, numAsesinatos);
            pstmt.setInt(4, tiempoJugado);

            // Ejecuta la consulta
            int affectedRows = pstmt.executeUpdate();

            // Comprueba si se ha insertado correctamente
            if (affectedRows > 0) {
                // Obtiene el ID generado automáticamente
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1);
                    }
                }
            }
        } catch (Exception e) {
            System.err.format("\n* Error al guardar el usuario: %s", e.getMessage());
            e.printStackTrace();
        }

        return generatedId;
    }
	
	public boolean actualizarUsuario(int id, String nomUsuario, int numMuertes, int numAsesinatos, int tiempoJugado) {
	    String sql = "UPDATE USUARIO SET nomUsuario = ?, numMuertes = ?, numAsesinatos = ?, tiempoJugado = ? WHERE id = ?";
	    boolean actualizado = false;

	    try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
	         PreparedStatement pstmt = con.prepareStatement(sql)) {

	        // Configura los valores para la consulta
	        pstmt.setString(1, nomUsuario);
	        pstmt.setInt(2, numMuertes);
	        pstmt.setInt(3, numAsesinatos);
	        pstmt.setInt(4, tiempoJugado);
	        pstmt.setInt(5, id);

	        // Ejecuta la consulta y verifica si se actualizó alguna fila
	        int affectedRows = pstmt.executeUpdate();
	        actualizado = affectedRows > 0;

	    } catch (Exception e) {
	        System.err.format("\n* Error al actualizar el usuario: %s", e.getMessage());
	        e.printStackTrace();
	    }

	    return actualizado;
	}
}
