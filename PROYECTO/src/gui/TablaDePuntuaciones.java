package gui;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import main.Usuario;

public class TablaDePuntuaciones extends JFrame {

	private static final long serialVersionUID = 1L;
	private String[] headers = {"Nombre","Enemigos asesinados","Muertes","Tiempo Jugado"};
	
	public TablaDePuntuaciones(ArrayList<Usuario> usuarios) {
		setTitle("Tabla de Puntuaciones");
		setSize(600,400);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		
		
		DefaultTableModel modelo = new DefaultTableModel(headers,0) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		for (Usuario usuario : usuarios) {
			Object[] filausuario = {usuario.getNomUsuario(),usuario.getNumAsesinatos(),usuario.getNumMuertes(),usuario.getTiempoJugado()};
			modelo.addRow(filausuario);
		}
		
		JTable tablaPuntuaciones = new JTable(modelo);
		tablaPuntuaciones.getTableHeader().setDefaultRenderer(new TablaDePuntuacionesHeadRenderer());
		
		tablaPuntuaciones.setDefaultRenderer(Object.class, new TablaDePuntuacionesBodyRenderer());
		JScrollPane panelTablaPuntuaciones = new JScrollPane(tablaPuntuaciones);
		tablaPuntuaciones.setRowHeight(32);
		
		add(panelTablaPuntuaciones);
		
		setVisible(true);
		
		
	}
	
	public static void main(String[] args) {
		ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
		usuarios.add(new Usuario("Primero", 1, 1, 1));
		usuarios.add(new Usuario("Segundo", 1, 1, 1));
		usuarios.add(new Usuario("Tercero", 1, 1, 1));
		usuarios.add(new Usuario("Primero", 1, 1, 1));
		usuarios.add(new Usuario("Primero", 1, 1, 1));
		usuarios.add(new Usuario("Primero", 1, 1, 1));
		usuarios.add(new Usuario("Primero", 1, 1, 1));
		usuarios.add(new Usuario("Primero", 1, 1, 1));
		new TablaDePuntuaciones(usuarios);
	}
	
	
		
	

}
