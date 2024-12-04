package gui;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class InventarioBodyRender implements TableCellRenderer{

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		JLabel l = new JLabel();
		if (value instanceof String) {
			String texto = (String) value;
			l.setText(texto);
		}
		if (row%2 ==0) {
			l.setBackground(new Color(202, 243, 242 ));
		} else {
			l.setBackground(new Color(255, 255, 255 ));
		}
		
		int filaSeleccionada = table.getSelectedRow();
		if (row == filaSeleccionada) {
			l.setBackground(new Color(248, 156, 156));
		}
		l.setOpaque(true);
		return l;
	}

}
