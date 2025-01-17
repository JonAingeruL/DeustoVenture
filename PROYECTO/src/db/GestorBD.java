package db;

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
	    String sql = "SELECT * FROM USUARIO ORDER BY numAsesinatos DESC";
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
	
	//BASE DE DATOS NUMERO 2
	
	//Metodo para crear las base de datos de posicion
	public void CrearBBDD_POS() {
		//Se abre la conexion y se obtiene el Statement
		//Al abrir la conexion, si no existia el fichero, se crea la base de datos
		
		try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
				Statement stmt = con.createStatement()) {
			 // Define la estructura de la tabla
			String sql = "CREATE TABLE IF NOT EXISTS POSICION_USUARIO ("+
					 "usuario TEXT PRIMARY KEY, "+
					 "x INTEGER NOT NULL, "+
					 "y INTEGER NOT NULL, "+
					 "numCelda INTEGER NOT NULL, "+
					 "numMapa INTEGER NOT NULL, "+
					 "mapaCargar TEXT NOT NULL "+
					");";
			
			 // Ejecuta la consulta para crear la tabla
            stmt.executeUpdate(sql);
            System.out.println("Tabla POSICION_USUARIO creada o ya existía.");
			
		} catch (Exception e) {
			System.err.format("\n* Error al crear la tabla en la base de datos: %s", e.getMessage());
            e.printStackTrace();
		}
		
	}
	
	//Metodo para para buscar datos por nombre de usuario
	public List<Object> buscarDatosUsuarioPOS(String usuario) {
	    String sql = "SELECT usuario, x, y, numCelda, numMapa, mapaCargar FROM POSICION_USUARIO WHERE usuario = ?";
	    List<Object> datosUsuario = new ArrayList<>();

	    try (
	        Connection con = DriverManager.getConnection(CONNECTION_STRING);
	        PreparedStatement pstmt = con.prepareStatement(sql)
	    ) {
	        pstmt.setString(1, usuario);
	        ResultSet rs = pstmt.executeQuery();

	        // Si hay resultados, los agregamos a la lista
	        if (rs.next()) {
	            datosUsuario.add(rs.getString("usuario"));  // Nombre del usuario
	            datosUsuario.add(rs.getInt("x"));          // Coordenada X
	            datosUsuario.add(rs.getInt("y"));          // Coordenada Y
	            datosUsuario.add(rs.getInt("numCelda"));   // Número de celda
	            datosUsuario.add(rs.getInt("numMapa"));    // Número de mapa
	            datosUsuario.add(rs.getString("mapaCargar"));  // Mapa
	        } else {
	            System.out.println("Usuario no encontrado: " + usuario);
	        }
	    } catch (SQLException e) {
	        System.err.format("\n* Error al buscar datos del usuario: %s", e.getMessage());
	    }

	    return datosUsuario; // Devolverá una lista vacía si no se encuentra nada
	}
	
	public boolean existeUsuarioPos(String nomUsuario) {
	    String sql = "SELECT COUNT(*) AS total FROM POSICION_USUARIO WHERE usuario = ?";
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
	
	//Metodo para resetear posiciones
	public boolean resetearPosicionUsuario(String usuario,String mapaCargar) {
	    String sql = """
	        UPDATE POSICION_USUARIO
	        SET x = 475, y = 400, numCelda = 1, numMapa = 0, mapaCargar = ?
	        WHERE usuario = ?;
	        """;
	    
		    try (
			        Connection con = DriverManager.getConnection(CONNECTION_STRING);
			        PreparedStatement pstmt = con.prepareStatement(sql)
			    ) {
			    	pstmt.setString(1, mapaCargar);
			        pstmt.setString(2, usuario);

			        int filasAfectadas = pstmt.executeUpdate();
			        if (filasAfectadas > 0) {
			            System.out.println("Posición reseteada para el usuario: " + usuario);
			            return true;
			        } else {
			            System.out.println("El usuario no existe: " + usuario);
			        }
			    } catch (SQLException e) {
			        System.err.format("\n* Error al resetear los datos del usuario: %s", e.getMessage());
			    }

	    
	    return false;	
	    
	}
	//metodo para actualizar posicion usuario
	public boolean actualizarPosicionUsuarioPos(String usuario,int x, int y, int numCelda, int numMapa, String mapaCargar) {
	    String sql = """
	        UPDATE POSICION_USUARIO
	        SET x = ?, y = ?, numCelda = ?, numMapa = ?, mapaCargar = ?
	        WHERE usuario = ?;
	        """;
	    
		    try (
			        Connection con = DriverManager.getConnection(CONNECTION_STRING);
			        PreparedStatement pstmt = con.prepareStatement(sql)
			    ) {
		    		pstmt.setInt(1, x);
		    		pstmt.setInt(2, y);
		    		pstmt.setInt(3, numCelda);
		    		pstmt.setInt(4, numMapa);
			    	pstmt.setString(5, mapaCargar);
			        pstmt.setString(6, usuario);

			        int filasAfectadas = pstmt.executeUpdate();
			        if (filasAfectadas > 0) {
			            System.out.println("Posición modificada para el usuario: " + usuario);
			            return true;
			        } else {
			            System.out.println("El usuario no existe: " + usuario);
			        }
			    } catch (SQLException e) {
			        System.err.format("\n* Error al resetear los datos del usuario: %s", e.getMessage());
			    }

	    
	    return false;	
	    
	}
	
	//Metodo para verificar si la el nombre de usuario en la tabla 2 
	public boolean verificarUsuarioPorNombrePOS(String usuario) {
	    String sql = "SELECT 1 FROM POSICION_USUARIO WHERE usuario = ?";
	    try (
	        Connection con = DriverManager.getConnection(CONNECTION_STRING);
	        PreparedStatement pstmt = con.prepareStatement(sql)
	    ) {
	        pstmt.setString(1, usuario);
	        ResultSet rs = pstmt.executeQuery();
	        return rs.next(); // Si hay un resultado, el usuario ya existe
	    } catch (SQLException e) {
	        System.err.format("\n* Error al verificar el usuario: %s", e.getMessage());
	    }
	    return false;
	}
	
	//Metodo para eliminar posiciones
	public boolean eliminarPosicionUsuario(String usuario) {
	    String sql = "DELETE FROM POSICION_USUARIO WHERE usuario = ?";

	    try (
	        Connection con = DriverManager.getConnection(CONNECTION_STRING);
	        PreparedStatement pstmt = con.prepareStatement(sql)
	    ) {
	        pstmt.setString(1, usuario);

	        int filasAfectadas = pstmt.executeUpdate();
	        if (filasAfectadas > 0) {
	            System.out.println("Usuario eliminado de la tabla POSICION_USUARIO: " + usuario);
	            return true;
	        } else {
	            System.out.println("El usuario no existe: " + usuario);
	        }
	    } catch (SQLException e) {
	        System.err.format("\n* Error al eliminar los datos del usuario: %s", e.getMessage());
	    }

	    return false;
	}
	
	//Metodo para guardar datos de la tabla
	public boolean guardarPosicionUsuario(String usuario, int x, int y, int numCelda, int numMapa, String mapaCargar) {
	    // Verificar si el usuario ya existe
	    if (verificarUsuarioPorNombrePOS(usuario)) {
	        System.out.println("El nombre de usuario ya existe: " + usuario);
	        return false;
	    }

	    String sql = """
	        INSERT INTO POSICION_USUARIO (usuario, x, y, numCelda, numMapa, mapaCargar)
	        VALUES (?, ?, ?, ?, ?, ?);
	        """;

	    try (
	        Connection con = DriverManager.getConnection(CONNECTION_STRING);
	        PreparedStatement pstmt = con.prepareStatement(sql)
	    ) {
	        pstmt.setString(1, usuario);
	        pstmt.setInt(2, x);
	        pstmt.setInt(3, y);
	        pstmt.setInt(4, numCelda);
	        pstmt.setInt(5, numMapa);
	        pstmt.setString(6, mapaCargar);

	        pstmt.executeUpdate();
	        System.out.println("Datos guardados correctamente para el usuario: " + usuario);
	        return true;
	    } catch (SQLException e) {
	        System.err.format("\n* Error al guardar los datos del usuario: %s", e.getMessage());
	    }

	    return false;
	}
	
	//Metodo para insertar datos en la tabla de pos
	public void insertarPosicionUsuario(String usuario, int x, int y, int numCelda, int numMapa, String mapaCargar) {
	    // SQL para insertar los datos en la tabla POSICION_USUARIO
	    String insertarPosicionSQL = """
	        INSERT INTO POSICION_USUARIO (usuario, x, y, numCelda, numMapa, mapaCargar)
	        VALUES (?, ?, ?, ?, ?, ?);
	    """;

	    try (
	        Connection con = DriverManager.getConnection(CONNECTION_STRING);
	        PreparedStatement pstmt = con.prepareStatement(insertarPosicionSQL)
	    ) {
	        // Establecer los valores de los parámetros en el PreparedStatement
	        pstmt.setString(1, usuario);  // Usuario
	        pstmt.setInt(2, x);  // Posición X
	        pstmt.setInt(3, y);  // Posición Y
	        pstmt.setInt(4, numCelda);  // Número de celda
	        pstmt.setInt(5, numMapa);  // Número de mapa
	        pstmt.setString(6, mapaCargar);  // Mapa que el usuario debe cargar (String)

	        // Ejecutar la inserción
	        pstmt.executeUpdate();
	        System.out.println("Posición de usuario '" + usuario + "' insertada correctamente.");
	    } catch (SQLException e) {
	        System.err.format("\n* Error al insertar la posición del usuario: %s", e.getMessage());
	    }
	}
	
	//BASE DE DATOS NUMERO 3
	
	//Metodo para crear las base de datos de posicion
	public void CrearBBDD_INV() {
		//Se abre la conexion y se obtiene el Statement
		//Al abrir la conexion, si no existia el fichero, se crea la base de datos
		
		try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
				Statement stmt = con.createStatement()) {
			 // Define la estructura de la tabla
			String sql = "CREATE TABLE IF NOT EXISTS INVENTARIO_JUGADORES ("+
					 "id INTEGER PRIMARY KEY AUTOINCREMENT, "+
					 "usuario TEXT NOT NULL, "+
					 "nombreObjeto TEXT NOT NULL, "+
					 "cantidad INTEGER NOT NULL, "+
					 "FOREIGN KEY (usuario) REFERENCES POSICION_USUARIO(usuario) ON DELETE CASCADE "+
					");";
			
			 // Ejecuta la consulta para crear la tabla
            stmt.executeUpdate(sql);
            System.out.println("Tabla INVENTARIO_JUGADORES creada o ya existía.");
			
		} catch (Exception e) {
			System.err.format("\n* Error al crear la tabla en la base de datos: %s", e.getMessage());
            e.printStackTrace();
		}
	}
	
	//Metodo para guardar item en el inventario
	public void guardarItemEnInventario(String usuario, String nombreObjeto, int cantidad) {
	    String insertarItemSQL = """
	        INSERT INTO INVENTARIO_JUGADORES (usuario, nombreObjeto, cantidad)
	        VALUES (?, ?, ?);
	    """;

	    try (
	        Connection con = DriverManager.getConnection(CONNECTION_STRING);
	        PreparedStatement pstmt = con.prepareStatement(insertarItemSQL)
	    ) {
	        // Establece los valores en el PreparedStatement
	        pstmt.setString(1, usuario);  // usuario
	        pstmt.setString(2, nombreObjeto);  // nombreObjeto
	        pstmt.setInt(3, cantidad);  // cantidad

	        // Ejecuta la inserción
	        pstmt.executeUpdate();
	        System.out.println("Ítem '" + nombreObjeto + "' guardado correctamente para el usuario" + usuario);
	    } catch (SQLException e) {
	        System.err.format("\n* Error al guardar el ítem: %s", e.getMessage());
	    }
	}
	
	//Metodo para eliminar item de inventario
	public void eliminarItemDelInventario(String usuario, String nombreObjeto) {
	    String eliminarItemSQL = """
	        DELETE FROM INVENTARIO_JUGADORES
	        WHERE usuario = ? AND nombreObjeto = ?;
	    """;

	    try (
	        Connection con = DriverManager.getConnection(CONNECTION_STRING);
	        PreparedStatement pstmt = con.prepareStatement(eliminarItemSQL)
	    ) {
	        // Establece los valores en el PreparedStatement
	        pstmt.setString(1, usuario);  // id_posicion
	        pstmt.setString(2, nombreObjeto);  // nombreObjeto

	        // Ejecuta la eliminación
	        int rowsAffected = pstmt.executeUpdate();
	        if (rowsAffected > 0) {
	            System.out.println("Ítem '" + nombreObjeto + "' eliminado correctamente para el usuario" + usuario);
	        } else {
	            System.out.println("No se encontró el ítem '" + nombreObjeto + "' para el usuario " + usuario);
	        }
	    } catch (SQLException e) {
	        System.err.format("\n* Error al eliminar el ítem: %s", e.getMessage());
	    }
	}
	public void resetearInventario(String usuario) {
	    String eliminarItemSQL = """
	        DELETE FROM INVENTARIO_JUGADORES
	        WHERE usuario = ?;
	    """;

	    try (
	        Connection con = DriverManager.getConnection(CONNECTION_STRING);
	        PreparedStatement pstmt = con.prepareStatement(eliminarItemSQL)
	    ) {
	        // Establece los valores en el PreparedStatement
	        pstmt.setString(1, usuario);  // id_posicion

	        // Ejecuta la eliminación
	        int rowsAffected = pstmt.executeUpdate();
	        if (rowsAffected > 0) {
	            System.out.println("Ítems eliminados correctamente para el usuario " + usuario);
	        } else {
	            System.out.println("No se encontraron los items para el usuario " + usuario);
	        }
	    } catch (SQLException e) {
	        System.err.format("\n* Error al eliminar ítems: %s", e.getMessage());
	    }
	}
	
	//Metodo para actulizar los items de la BD
	public void actualizarItemInventario(int id, String usuario, String nombreObjeto, int cantidad) {
	    // SQL para actualizar los datos del ítem en la tabla INVENTARIO
	    String actualizarItemSQL = """
	        UPDATE INVENTARIO
	        SET usuario = ?, nombreObjeto = ?, cantidad = ?
	        WHERE id = ?;
	    	""";

	    try (
	        Connection con = DriverManager.getConnection(CONNECTION_STRING);
	        PreparedStatement pstmt = con.prepareStatement(actualizarItemSQL)
	    ) {
	        // Asignar valores al PreparedStatement
	        pstmt.setString(1, usuario);       // Nombre del usuario
	        pstmt.setString(2, nombreObjeto); // Nombre del objeto
	        pstmt.setInt(3, cantidad);        // Cantidad del objeto
	        pstmt.setInt(4, id);              // ID del ítem a actualizar

	        // Ejecutar la actualización
	        int filasActualizadas = pstmt.executeUpdate();
	        if (filasActualizadas > 0) {
	            System.out.println("Ítem del inventario con ID " + id + " actualizado correctamente.");
	        } else {
	            System.out.println("No se encontró ningún ítem con el ID " + id + ".");
	        }
	    } catch (SQLException e) {
	        System.err.format("\n* Error al actualizar el ítem del inventario: %s", e.getMessage());
	    }
	}
	
	//Metodo qeu verifica si un usuario tiene objetos en el inventario
	public boolean usuarioTieneInventario(String usuario) {
	    // SQL para contar el número de objetos del usuario en la tabla INVENTARIO
	    String contarItemsSQL = """
	        SELECT COUNT(*) AS total
	        FROM INVENTARIO
	        WHERE usuario = ?;
	    	""";

	    try (
	        Connection con = DriverManager.getConnection(CONNECTION_STRING);
	        PreparedStatement pstmt = con.prepareStatement(contarItemsSQL)
	    ) {
	        // Establecer el usuario como parámetro en el PreparedStatement
	        pstmt.setString(1, usuario);

	        // Ejecutar la consulta
	        ResultSet rs = pstmt.executeQuery();

	        if (rs.next()) {
	            int total = rs.getInt("total"); // Obtener el conteo de filas
	            return total > 0; // Si hay al menos un ítem, devuelve true
	        }
	    } catch (SQLException e) {
	        System.err.format("\n* Error al verificar el inventario del usuario '%s': %s", usuario, e.getMessage());
	    }
	    return false; // En caso de error o si no se encuentra nada, devuelve false
	}
	/**
	 * Saca de la base de datos el inventario guardado de un usuario concreto.
	 * @param usuario El nombre del usuario dueño del inventario
	 * @return HashMap inventario.
	 */
	public HashMap<String, Integer> obtenerInventarioUsuario(String usuario) {
		HashMap<String, Integer> inv = new HashMap<String, Integer>();
		String select = "Select nombreObjeto, cantidad"
				+ " From Inventario_Jugadores"
				+ " Where usuario = ?";
		try(Connection con = DriverManager.getConnection(CONNECTION_STRING);
			PreparedStatement pstmt = con.prepareStatement(select)) {
			pstmt.setString(1, usuario);
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String nombreObj = rs.getString("nombreObjeto");
				Integer cant = rs.getInt("cantidad");
				inv.put(nombreObj, cant);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.print("Error cargando el inventario del usuario");
		}
		return inv;
	}
}
