package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellRenderer;

public class TablaDePuntuacionesHeadRenderer implements TableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		JLabel l = new JLabel();
		if(column ==0) {
			l.setIcon(new ImageIcon("resources/texturas/extra/user.png"));
		} else if (column==1) {
			l.setIcon(new ImageIcon("resources/texturas/extra/monster.png"));
		} else if (column ==2) {
			l.setIcon(new ImageIcon("resources/texturas/extra/dead.png"));
		} else if (column ==3) {
			l.setIcon(new ImageIcon("resources/texturas/extra/tiempo.png"));
		}
		l.setText(""+value+"");
		l.setFont(new Font(Font.DIALOG, Font.BOLD, 13));
		l.setBackground(new Color(173,216,230));
		l.setBorder(new LineBorder(Color.BLACK));
		l.setOpaque(true);
		return l;
	}

}
