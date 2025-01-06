package main;

public class Usuario {

	//Registra el nombre de usuario de la persona que juega
	public String nomUsuario;
	
	//Registra las veces que el jugador ha muerto
	public int numMuertes;
	
	//Registra las veces que ha matado a enemigos
	public int numAsesinatos;
	
	//Registra el tiempo jugado
	public int tiempoJugado;
	
	public Usuario() {}

	public Usuario(String nomUsuario, int numMuertes, int numAsesinatos, int tiempoJugado) {
		super();
		this.nomUsuario = nomUsuario;
		this.numMuertes = numMuertes;
		this.numAsesinatos = numAsesinatos;
		this.tiempoJugado = tiempoJugado;
	}

	public String getNomUsuario() {
		return nomUsuario;
	}

	public void setNomUsuario(String nomUsuario) {
		this.nomUsuario = nomUsuario;
	}

	public int getNumMuertes() {
		return numMuertes;
	}

	public void setNumMuertes(int numMuertes) {
		this.numMuertes = numMuertes;
	}

	public int getNumAsesinatos() {
		return numAsesinatos;
	}

	public void setNumAsesinatos(int numAsesinatos) {
		this.numAsesinatos = numAsesinatos;
	}

	public int getTiempoJugado() {
		return tiempoJugado;
	}

	public void setTiempoJugado(int tiempoJugado) {
		this.tiempoJugado = tiempoJugado;
	}
	
	
}
