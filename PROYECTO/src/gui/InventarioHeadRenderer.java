package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class InventarioHeadRenderer implements TableCellRenderer{

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		JLabel l = new JLabel();
		l.setText((String) value);
		l.setFont(new Font("Papyrus", Font.BOLD, 16));
		l.setBackground(new Color(249, 209, 119));
		l.setHorizontalAlignment(JLabel.CENTER);
		if (column ==0) {
			l.setIcon(new ImageIcon("Resources/texturas/texInventario/espada.png"));
		} else {
			l.setIcon(new ImageIcon("Resources/texturas/texInventario/caja.png"));
		}
		
		l.setOpaque(true);
		return l;
	}
	
}
