package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	
	//Metodo que crea la base de datos
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
	
	//Metodo para guardar los usuarios en la base de datos
	public int guardarUsuario(String nomUsuario, int numMuertes, int numAsesinatos, int tiempoJugado) {
        String sql = "INSERT INTO USUARIO (nomUsuario, numMuertes, numAsesinatos, tiempoJugado) VALUES (?, ?, ?, ?)";
        int generatedId = -1;

        try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
             PreparedStatement pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            // configura los valores para la consulta
            pstmt.setString(1, nomUsuario);
            pstmt.setInt(2, numMuertes);
            pstmt.setInt(3, numAsesinatos);
            pstmt.setInt(4, tiempoJugado);

            // ejecuta la consulta
            int affectedRows = pstmt.executeUpdate();

            // comprueba si se ha insertado correctamente
            if (affectedRows > 0) {
                // obtiene el ID generado automáticamente
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
	
	//Metodo para actulizar la base de datos
	public boolean actualizarUsuario(int id, String nomUsuario, int numMuertes, int numAsesinatos, int tiempoJugado) {
	    String sql = "UPDATE USUARIO SET nomUsuario = ?, numMuertes = ?, numAsesinatos = ?, tiempoJugado = ? WHERE id = ?";
	    boolean actualizado = false;

	    try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
	         PreparedStatement pstmt = con.prepareStatement(sql)) {

	        // configura los valores para la consulta
	        pstmt.setString(1, nomUsuario);
	        pstmt.setInt(2, numMuertes);
	        pstmt.setInt(3, numAsesinatos);
	        pstmt.setInt(4, tiempoJugado);
	        pstmt.setInt(5, id);

	        // ejecuta la consulta y verifica si se actualizó alguna fila
	        int affectedRows = pstmt.executeUpdate();
	        actualizado = affectedRows > 0;

	    } catch (Exception e) {
	        System.err.format("\n* Error al actualizar el usuario: %s", e.getMessage());
	        e.printStackTrace();
	    }

	    return actualizado;
	}
	
	//Metodo para eliminar un usuario de la base de datos
	public boolean eliminarUsuario(int id) {
	    String sql = "DELETE FROM USUARIO WHERE id = ?";
	    boolean eliminado = false;

	    try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
	         PreparedStatement pstmt = con.prepareStatement(sql)) {

	        // configura el parámetro para la consulta
	        pstmt.setInt(1, id);

	        // ejecuta la consulta y verifica si se eliminó alguna fila
	        int affectedRows = pstmt.executeUpdate();
	        eliminado = affectedRows > 0;

	    } catch (Exception e) {
	        System.err.format("\n* Error al eliminar el usuario: %s", e.getMessage());
	        e.printStackTrace();
	    }

	    return eliminado;
	}
	
	//Metodo que obtiene un usuario de la base de datos por su ID
	public Usuario obtenerUsuarioPorId(int id) {
	    String sql = "SELECT * FROM USUARIO WHERE id = ?";
	    Usuario usuario = null;

	    try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
	         PreparedStatement pstmt = con.prepareStatement(sql)) {

	        // configura el ID como parámetro de la consulta
	        pstmt.setInt(1, id);

	        // ejecuta la consulta y procesa el resultado
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                // crea un objeto Usuario con los datos obtenidos
	                usuario = new Usuario(
	                    rs.getString("nomUsuario"),
	                    rs.getInt("numMuertes"),
	                    rs.getInt("numAsesinatos"),
	                    rs.getInt("tiempoJugado")
	                );
	            }
	        }
	    } catch (Exception e) {
	        System.err.format("\n* Error al obtener el usuario: %s", e.getMessage());
	        e.printStackTrace();
	    }

	    return usuario; // Retorna el usuario o null si no se encontró
	}
	
	//Metodo que recupera todos los usuarios almacenados en la base de datos.
	public List<Usuario> listarUsuarios() {
	    String sql = "SELECT * FROM USUARIO";
	    List<Usuario> usuarios = new ArrayList<>();

	    try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
	         Statement stmt = con.createStatement();
	         ResultSet rs = stmt.executeQuery(sql)) {

	        // recorre todos los resultados y crea objetos Usuario
	        while (rs.next()) {
	            Usuario usuario = new Usuario(
	                rs.getString("nomUsuario"),
	                rs.getInt("numMuertes"),
	                rs.getInt("numAsesinatos"),
	                rs.getInt("tiempoJugado")
	            );
	            usuarios.add(usuario); // agrega el usuario a la lista
	        }
	    } catch (Exception e) {
	        System.err.format("\n* Error al listar usuarios: %s", e.getMessage());
	        e.printStackTrace();
	    }

	    return usuarios; // Retorna la lista de usuarios
	}
	
	//Metodo que busca usuarios por nombre
	public List<Usuario> buscarUsuariosPorNombre(String nombre) {
	    String sql = "SELECT * FROM USUARIO WHERE nomUsuario LIKE ?";
	    List<Usuario> usuarios = new ArrayList<>();

	    try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
	         PreparedStatement pstmt = con.prepareStatement(sql)) {

	        // configura el parámetro con el texto de búsqueda
	        pstmt.setString(1, "%" + nombre + "%");

	        // ejecuta la consulta y procesa los resultados
	        try (ResultSet rs = pstmt.executeQuery()) {
	            while (rs.next()) {
	                Usuario usuario = new Usuario(
	                    rs.getString("nomUsuario"),
	                    rs.getInt("numMuertes"),
	                    rs.getInt("numAsesinatos"),
	                    rs.getInt("tiempoJugado")
	                );
	                usuarios.add(usuario); // agrega el usuario a la lista
	            }
	        }
	    } catch (Exception e) {
	        System.err.format("\n* Error al buscar usuarios: %s", e.getMessage());
	        e.printStackTrace();
	    }

	    return usuarios; // Retorna la lista de usuarios encontrados
	}
	
	//Metodo que obtiene estadísticas de todos los usuarios
	public Map<String, Integer> obtenerEstadisticasGlobales() {
	    String sql = "SELECT SUM(numMuertes) AS totalMuertes, " +
	                 "SUM(numAsesinatos) AS totalAsesinatos, " +
	                 "SUM(tiempoJugado) AS totalTiempoJugado FROM USUARIO";
	    Map<String, Integer> estadisticas = new HashMap<>();

	    try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
	         Statement stmt = con.createStatement();
	         ResultSet rs = stmt.executeQuery(sql)) {

	        if (rs.next()) {
	            // obtiene los totales y los almacena en el mapa
	            estadisticas.put("totalMuertes", rs.getInt("totalMuertes"));
	            estadisticas.put("totalAsesinatos", rs.getInt("totalAsesinatos"));
	            estadisticas.put("totalTiempoJugado", rs.getInt("totalTiempoJugado"));
	        }
	    } catch (Exception e) {
	        System.err.format("\n* Error al obtener estadísticas: %s", e.getMessage());
	        e.printStackTrace();
	    }

	    return estadisticas; // Retorna el mapa con las estadísticas
	}
	
	//Metodo que elimina usuarios cuyo tiempo jugado sea menor que un valor dado
	public boolean eliminarUsuariosInactivos(int tiempoJugadoMaximo) {
	    String sql = "DELETE FROM USUARIO WHERE tiempoJugado < ?";
	    boolean eliminado = false;

	    try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
	         PreparedStatement pstmt = con.prepareStatement(sql)) {

	        // configura el tiempo jugado máximo como parámetro
	        pstmt.setInt(1, tiempoJugadoMaximo);

	        // ejecuta la consulta y verifica si se eliminaron filas
	        int affectedRows = pstmt.executeUpdate();
	        eliminado = affectedRows > 0;

	    } catch (Exception e) {
	        System.err.format("\n* Error al eliminar usuarios inactivos: %s", e.getMessage());
	        e.printStackTrace();
	    }

	    return eliminado; // Retorna true si se eliminaron usuarios
	}
	
	//Metodo que cuenta el numero total de usuarios
	public int contarUsuarios() {
	    String sql = "SELECT COUNT(*) AS totalUsuarios FROM USUARIO";
	    int totalUsuarios = 0;

	    try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
	         Statement stmt = con.createStatement();
	         ResultSet rs = stmt.executeQuery(sql)) {

	        // Si hay un resultado, obtiene el número total de usuarios
	        if (rs.next()) {
	            totalUsuarios = rs.getInt("totalUsuarios");
	        }
	    } catch (Exception e) {
	        System.err.format("\n* Error al contar usuarios: %s", e.getMessage());
	        e.printStackTrace();
	    }

	    return totalUsuarios; // Retorna el total de usuarios encontrados
	}
	
	//Metodo que obtiene el usuario con más asesinatos
	public Usuario obtenerUsuarioConMasAsesinatos() {
	    String sql = "SELECT * FROM USUARIO ORDER BY numAsesinatos DESC LIMIT 1";
	    Usuario usuario = null;

	    try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
	         Statement stmt = con.createStatement();
	         ResultSet rs = stmt.executeQuery(sql)) {

	        // Si hay resultados, crea un objeto Usuario con los datos obtenidos
	        if (rs.next()) {
	            usuario = new Usuario(
	                rs.getString("nomUsuario"),
	                rs.getInt("numMuertes"),
	                rs.getInt("numAsesinatos"),
	                rs.getInt("tiempoJugado")
	            );
	        }
	    } catch (Exception e) {
	        System.err.format("\n* Error al obtener usuario con más asesinatos: %s", e.getMessage());
	        e.printStackTrace();
	    }

	    return usuario; // Retorna el usuario con más asesinatos o null si no hay usuarios
	}
	
	//Metodo que actualiza el tiempo de un jugador por su ID
	public boolean actualizarTiempoJugado(int id, int tiempoNuevo) {
	    String sql = "UPDATE USUARIO SET tiempoJugado = ? WHERE id = ?";
	    boolean actualizado = false;

	    try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
	         PreparedStatement pstmt = con.prepareStatement(sql)) {

	        // Configura los parámetros de la consulta
	        pstmt.setInt(1, tiempoNuevo);
	        pstmt.setInt(2, id);

	        // Ejecuta la actualización y verifica si se afectaron filas
	        int affectedRows = pstmt.executeUpdate();
	        actualizado = affectedRows > 0;

	    } catch (Exception e) {
	        System.err.format("\n* Error al actualizar el tiempo jugado: %s", e.getMessage());
	        e.printStackTrace();
	    }

	    return actualizado; // Retorna true si la actualización fue bien
	}
	
	//Metodo que resetea todas las estadisticas
	public boolean reiniciarEstadisticas() {
	    String sql = "UPDATE USUARIO SET numMuertes = 0, numAsesinatos = 0, tiempoJugado = 0";
	    boolean reiniciado = false;

	    try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
	         Statement stmt = con.createStatement()) {

	        // Ejecuta la consulta de actualización
	        int affectedRows = stmt.executeUpdate(sql);
	        reiniciado = affectedRows > 0; // Se reiniciaron estadísticas si hay filas afectadas

	    } catch (Exception e) {
	        System.err.format("\n* Error al reiniciar estadísticas: %s", e.getMessage());
	        e.printStackTrace();
	    }

	    return reiniciado; // Retorna true si se reiniciaron estadísticas
	}
	
	//Metodo que verifica si existe un usuario por ID
	public boolean existeUsuario(int id) {
	    String sql = "SELECT COUNT(*) AS total FROM USUARIO WHERE id = ?";
	    boolean existe = false;

	    try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
	         PreparedStatement pstmt = con.prepareStatement(sql)) {

	        // Configura el parámetro de la consulta
	        pstmt.setInt(1, id);

	        // Ejecuta la consulta y verifica el resultado
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next() && rs.getInt("total") > 0) {
	                existe = true; // El usuario existe si el total es mayor que 0
	            }
	        }
	    } catch (Exception e) {
	        System.err.format("\n* Error al verificar existencia del usuario: %s", e.getMessage());
	        e.printStackTrace();
	    }

	    return existe; // Retorna true si el usuario existe
	}
}
