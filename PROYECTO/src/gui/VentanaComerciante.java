package gui;


import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import entidades.Jugador;
import main.GamePanel;
import main.ManejoTeclado;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;

public class VentanaComerciante extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int dineroJugador;
    private HashMap<String, Integer> productos; //es necesario

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

        JTable tabla = new JTable(modeloTabla) {
            private static final long serialVersionUID = 1L;

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE);
                } else {
                    c.setBackground(Color.CYAN);
                }
                return c;
            }
        };
        tabla.setRowHeight(30);
        tabla.setFont(new Font("Arial", Font.PLAIN, 14));
        tabla.setSelectionBackground(new Color(184, 207, 229));
        tabla.setSelectionForeground(Color.BLACK);

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
            setBackground(new Color(30, 144, 255));
            setForeground(Color.BLACK); // Texto en negro
            setFont(new Font("Arial", Font.BOLD, 12));
            setBorder(BorderFactory.createRaisedBevelBorder()); // Borde elevado
            setFocusPainted(false); // Evita el borde de enfoque
            setContentAreaFilled(true); // El área del botón es rellenable
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            if (isSelected) {
                setBackground(new Color(70, 130, 180)); // Color más oscuro cuando está seleccionado
            } else {
                setBackground(new Color(30, 144, 255)); // Azul cuando no está seleccionado
            }
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
            button.setBackground(new Color(34, 139, 34)); // Verde
            button.setForeground(Color.BLACK); // Texto en negro
            button.setFont(new Font("Arial", Font.BOLD, 12));
            button.setBorder(BorderFactory.createRaisedBevelBorder()); // Borde elevado
            button.setFocusPainted(false); // Evitar borde de enfoque
            button.setContentAreaFilled(true);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (dineroJugador >= precioSeleccionado && jugador.getInventario().containsKey(productoSeleccionado)) {
                        dineroJugador -= precioSeleccionado;
                        if(jugador.getInventario().containsKey("Oro")) {
                        	int cantidadOro = jugador.getInventario().get("Oro") - precioSeleccionado;
                        	jugador.getInventario().put("Oro", cantidadOro);
                        }
                        JOptionPane.showMessageDialog(button, "Has comprado: " + productoSeleccionado);
                        jugador.getInventario().put(productoSeleccionado, +1);
                    } else {
                        JOptionPane.showMessageDialog(button, "Eres pobre o ya tienes este objeto", "Error", JOptionPane.ERROR_MESSAGE);
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
