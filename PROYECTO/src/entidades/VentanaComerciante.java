package entidades;

	import java.awt.BorderLayout;
	import java.awt.Component;
	import java.awt.FlowLayout;
	import java.awt.Font;
	import java.awt.event.ActionEvent;
	import java.awt.event.ActionListener;
	import java.util.ArrayList;

	import javax.swing.DefaultCellEditor;
	import javax.swing.JButton;
	import javax.swing.JCheckBox;
	import javax.swing.JFrame;
	import javax.swing.JLabel;
	import javax.swing.JOptionPane;
	import javax.swing.JPanel;
	import javax.swing.JScrollPane;
	import javax.swing.JTable;
	import javax.swing.table.DefaultTableModel;
	import javax.swing.table.TableCellRenderer;

import gui.Item;

	public class VentanaComerciante extends JFrame {
	    private JTable tabla;
	    private DefaultTableModel modeloTabla;
	    private JLabel lblDinero;
	    private int dineroJugador;
	    private ArrayList<Item> inventarioNPC;

	    public VentanaComerciante(int dineroJugador, ArrayList<Item> inventarioNPC) {
	        this.dineroJugador = dineroJugador;
	        this.inventarioNPC = inventarioNPC;

	        setTitle("Comercio con el NPC");
	        setSize(500, 300);
	        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        setLayout(new BorderLayout());

	        // Panel de dinero del jugador
	        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
	        lblDinero = new JLabel("Dinero: " + dineroJugador);
	        lblDinero.setFont(new Font("Arial", Font.BOLD, 16));
	        panelSuperior.add(lblDinero);

	        // Crear tabla
	        String[] columnas = {"Nombre", "Precio", "Comprar"};
	        modeloTabla = new DefaultTableModel(columnas, 0);
	        tabla = new JTable(modeloTabla) {
	            @Override
	            public boolean isCellEditable(int row, int column) {
	                return column == 2; // Solo la columna de "Comprar" es editable
	            }
	        };

	        // Renderizar botón en la columna "Comprar"
	        tabla.getColumnModel().getColumn(2).setCellRenderer(new ButtonRenderer());
	        tabla.getColumnModel().getColumn(2).setCellEditor(new ButtonEditor(new JCheckBox()));

	        // Agregar ítems a la tabla
	        for (Item item : inventarioNPC) {
	            modeloTabla.addRow(new Object[]{item.getNombre(), item.getPrecio(), "Comprar"});
	        }

	        // Scroll para la tabla
	        JScrollPane scroll = new JScrollPane(tabla);

	        // Agregar componentes al frame
	        add(panelSuperior, BorderLayout.NORTH);
	        add(scroll, BorderLayout.CENTER);
	        setVisible(true);
	    }

	    // Actualizar dinero del jugador
	    private void actualizarDinero(int nuevoDinero) {
	        dineroJugador = nuevoDinero;
	        lblDinero.setText("Dinero: " + dineroJugador);
	    }

	    // Clase para renderizar botones en la tabla
	    class ButtonRenderer extends JButton implements TableCellRenderer {
	        public ButtonRenderer() {
	            setOpaque(true);
	        }

	        @Override
	        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	            setText((value == null) ? "" : value.toString());
	            return this;
	        }
	    }

	    // Clase para manejar clics en los botones
	    class ButtonEditor extends DefaultCellEditor {
	        private JButton button;
	        private String label;
	        private boolean isPushed;

	        public ButtonEditor(JCheckBox checkBox) {
	            super(checkBox);
	            button = new JButton();
	            button.setOpaque(true);
	            button.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						int fila = tabla.getSelectedRow();
	                    int precio = (int) modeloTabla.getValueAt(fila, 1);

	                    if (dineroJugador >= precio) {
	                        // Comprar ítem
	                        actualizarDinero(dineroJugador - precio);
	                        JOptionPane.showMessageDialog(null, "¡Has comprado " + modeloTabla.getValueAt(fila, 0) + "!");
	                    } else {
	                        // Dinero insuficiente
	                        JOptionPane.showMessageDialog(null, "No tienes suficiente dinero.", "Error", JOptionPane.ERROR_MESSAGE);
	                    }
						
					}
				}); 
	            
	        }
	              

	        @Override
	        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
	            label = (value == null) ? "" : value.toString();
	            button.setText(label);
	            isPushed = true;
	            return button;
	        }

	        @Override
	        public Object getCellEditorValue() {
	            isPushed = false;
	            return label;
	        }

	        @Override
	        public boolean stopCellEditing() {
	            isPushed = false;
	            return super.stopCellEditing();
	        }

	        @Override
	        protected void fireEditingStopped() {
	            super.fireEditingStopped();
	        }
	    }
	}





