package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                    "nomUsuario TEXT PRIMARY KEY, " +
                    "numMuertes INTEGER DEFAULT 0, " +
                    "numAsesinatos INTEGER DEFAULT 0, " +
                    "tiempoJugado INTEGER DEFAULT 0 " +
                    ");";
			
			 // Ejecuta la consulta para crear la tabla
            stmt.executeUpdate(sql);
            System.out.println("Tabla USUARIO creada o ya existía.");
			
		} catch (Exception e) {
			System.err.format("\n* Error al crear la tabla en la base de datos: %s", e.getMessage());
            e.printStackTrace();
		}
		
	}
	
	//Metodo que sirve para ver si el nombre que alguien se pone ya esta cogido
	public boolean verificarNombreDisponible(String nomUsuario) {
	    String sql = "SELECT COUNT(*) AS total FROM USUARIO WHERE nomUsuario = ?";
	    try (
	        Connection con = DriverManager.getConnection(CONNECTION_STRING);
	        PreparedStatement pstmt = con.prepareStatement(sql)
	    ) {
	        pstmt.setString(1, nomUsuario);

	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next() && rs.getInt("total") > 0) {
	                return true; // El nombre ya existe
	            }
	        }
	    } catch (Exception e) {
	        System.err.format("\n* Error al verificar nombre: %s", e.getMessage());
	        e.printStackTrace();
	    }
	    return false; // El nombre está disponible
	}
	
	//Metodo para guardar los usuarios en la base de datos una vez validado que ese nombre esta disponible
	public boolean guardarUsuarioConValidacion(Usuario usuario) {
	    // Verificar si el nombre ya existe
	    if (verificarNombreDisponible(usuario.nomUsuario)) {
	        System.out.println("El nombre de usuario \"" + usuario.nomUsuario + "\" no está disponible.");
	        return false;
	    }

	    // Proceder con la inserción si el nombre está disponible
	    String sql = "INSERT INTO USUARIO (nomUsuario, numMuertes, numAsesinatos, tiempoJugado) VALUES (?, ?, ?, ?)";
	    try (
	        Connection con = DriverManager.getConnection(CONNECTION_STRING);
	        PreparedStatement pstmt = con.prepareStatement(sql)
	    ) {
	        pstmt.setString(1, usuario.nomUsuario);
	        pstmt.setInt(2, usuario.numMuertes);
	        pstmt.setInt(3, usuario.numAsesinatos);
	        pstmt.setInt(4, usuario.tiempoJugado);

	        pstmt.executeUpdate();
	        return true;
	    } catch (Exception e) {
	        System.err.format("\n* Error al guardar el usuario: %s", e.getMessage());
	        e.printStackTrace();
	    }

	    return false;
	}
	
	//Metodo para actulizar la base de datos
	public boolean actualizarUsuario(Usuario usuario) {
	    String sql = "UPDATE USUARIO SET numMuertes = ?, numAsesinatos = ?, tiempoJugado = ? WHERE nomUsuario = ?";

	    try (
	        Connection con = DriverManager.getConnection(CONNECTION_STRING);
	        PreparedStatement pstmt = con.prepareStatement(sql)
	    ) {
	        pstmt.setInt(1, usuario.numMuertes);
	        pstmt.setInt(2, usuario.numAsesinatos);
	        pstmt.setInt(3, usuario.tiempoJugado);
	        pstmt.setString(4, usuario.nomUsuario);

	        int filasActualizadas = pstmt.executeUpdate();
	        return filasActualizadas > 0;
	    } catch (Exception e) {
	        System.err.format("\n* Error al actualizar el usuario: %s", e.getMessage());
	        e.printStackTrace();
	    }

	    return false;
	}
	
	//Metodo para eliminar un usuario de la base de datos
	public boolean eliminarUsuario(String nomUsuario) {
	    String sql = "DELETE FROM USUARIO WHERE nomUsuario = ?";

	    try (
	        Connection con = DriverManager.getConnection(CONNECTION_STRING);
	        PreparedStatement pstmt = con.prepareStatement(sql)
	    ) {
	        pstmt.setString(1, nomUsuario);

	        int filasEliminadas = pstmt.executeUpdate();
	        return filasEliminadas > 0;
	    } catch (Exception e) {
	        System.err.format("\n* Error al eliminar el usuario: %s", e.getMessage());
	        e.printStackTrace();
	    }

	    return false;
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
	
	
	//Metodo que verifica si existe un usuario por su nombre
	public boolean existeUsuario(String nomUsuario) {
	    String sql = "SELECT COUNT(*) AS total FROM USUARIO WHERE nomUsuario = ?";
	    boolean existe = false;

	    try (
	        Connection con = DriverManager.getConnection(CONNECTION_STRING);
	        PreparedStatement pstmt = con.prepareStatement(sql)
	    ) {
	        pstmt.setString(1, nomUsuario);

	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next() && rs.getInt("total") > 0) {
	                existe = true;
	            }
	        }
	    } catch (Exception e) {
	        System.err.format("\n* Error al verificar existencia del usuario: %s", e.getMessage());
	        e.printStackTrace();
	    }

	    return existe;
	}
	
	//Método para obtener estadísticas
	public Usuario obtenerEstadisticasUsuario(String nomUsuario) {
	    String sql = "SELECT nomUsuario, numMuertes, numAsesinatos, tiempoJugado FROM USUARIO WHERE nomUsuario = ?";

	    try (
	        Connection con = DriverManager.getConnection(CONNECTION_STRING);
	        PreparedStatement pstmt = con.prepareStatement(sql)
	    ) {
	        pstmt.setString(1, nomUsuario);

	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                // Crear el objeto Usuario con los datos obtenidos
	                Usuario usuario = new Usuario();
	                usuario.nomUsuario = rs.getString("nomUsuario");
	                usuario.numMuertes = rs.getInt("numMuertes");
	                usuario.numAsesinatos = rs.getInt("numAsesinatos");
	                usuario.tiempoJugado = rs.getInt("tiempoJugado");
	                return usuario;
	            } else {
	                // Usuario no encontrado
	                System.out.println("No se encontró el usuario con nombre: " + nomUsuario);
	            }
	        }
	    } catch (SQLException e) {
	        System.err.format("\n* Error al obtener las estadísticas del usuario: %s", e.getMessage());
	        e.printStackTrace();
	    }

	    return null; // Retorna null si ocurre algún error o el usuario no existe
	}
}
