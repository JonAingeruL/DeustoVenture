package gui;


import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.*;

import entidades.Jugador;
import main.GamePanel;
import main.ManejoTeclado;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class VentanaComerciante extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int dineroJugador;
    private HashMap<String, Integer> productos;

    public VentanaComerciante(int dineroJugador, ManejoTeclado mt, GamePanel gp, HashMap<String, Integer> productos, Jugador jugador) {
        this.dineroJugador = dineroJugador;
        this.productos = productos;

        setTitle("Comercio con el NPC");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Añadir KeyListener para cerrar con ESC
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    dispose(); // Cerrar la ventana
                }
            }
        });

        // Crear tabla personalizada
        String[] columnas = {"Producto", "Precio", "Comprar"};
        Object[][] datos = new Object[productos.size()][3];
        int i = 0;
        for (String producto : productos.keySet()) {
            datos[i][0] = producto;
            datos[i][1] = productos.get(producto) + " monedas";
            datos[i][2] = "Comprar"; // Botón textual
            i++;
        }

        DefaultTableModel modeloTabla = new DefaultTableModel(datos, columnas) {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Solo la columna de "Comprar" es editable
            }
        };

        JTable tabla = new JTable(modeloTabla);
        tabla.setRowHeight(30);

        // Personalizar encabezados
        JTableHeader encabezado = tabla.getTableHeader();
        encabezado.setFont(new Font("Arial", Font.BOLD, 14));
        encabezado.setBackground(Color.DARK_GRAY);
        encabezado.setForeground(Color.WHITE);

        // Renderizar columna de botones
        tabla.getColumnModel().getColumn(2).setCellRenderer(new ButtonRenderer());
        tabla.getColumnModel().getColumn(2).setCellEditor(new ButtonEditor(new JCheckBox(), jugador));

        // Panel con tabla
        JScrollPane scrollPane = new JScrollPane(tabla);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);

        // Solicitar foco para capturar teclas
        this.requestFocus();
    }

    // Renderizador personalizado para la columna de botones
    class ButtonRenderer extends JButton implements TableCellRenderer {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    // Editor personalizado para manejar compras
    class ButtonEditor extends DefaultCellEditor {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private JButton button;
        private String productoSeleccionado;
        private int precioSeleccionado;
        private boolean clicked;

        public ButtonEditor(JCheckBox checkBox, Jugador jugador) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (dineroJugador >= precioSeleccionado) {
                        dineroJugador -= precioSeleccionado;
                        JOptionPane.showMessageDialog(button, "Has comprado: " + productoSeleccionado);
                        jugador.getInventario().put(productoSeleccionado, +1);
                    } else {
                        JOptionPane.showMessageDialog(button, "No tienes suficiente dinero", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            productoSeleccionado = (String) table.getValueAt(row, 0);
            String precioStr = (String) table.getValueAt(row, 1);
            precioSeleccionado = Integer.parseInt(precioStr.split(" ")[0]); // Extraer precio
            button.setText((value == null) ? "" : value.toString());
            clicked = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            clicked = false;
            return button.getText();
        }

        @Override
        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }
}
