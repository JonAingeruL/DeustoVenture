package gui.menus;

import java.awt.Color;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class TablaDePuntuacionesBodyRenderer implements TableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		JLabel l = new JLabel();
		if (column ==0) {
			if (row ==0 ) {
				l.setIcon(new ImageIcon("resources/texturas/extra/usertop1.png"));
				
			} else if (row==1) {
				l.setIcon(new ImageIcon("resources/texturas/extra/usertop2.png"));
			} else if (row==2) {
				l.setIcon(new ImageIcon("resources/texturas/extra/usertop3.png"));
			} else if (row >= table.getRowCount()-3) {
				l.setIcon(new ImageIcon("resources/texturas/extra/userLoser.png"));
			} else {
				l.setIcon(new ImageIcon("resources/texturas/extra/user.png"));
			}
		}
		
		if (row ==0) {
			l.setBackground(new Color(239,184,16));
		}	if (row ==1) {
			l.setBackground(new Color(190,190,190));
		}	else if (row ==2) {
			l.setBackground(new Color(213,128,90));
		} else if (row >=table.getRowCount()-3) {
			l.setBackground(new Color(184,218,186));
		} else if (!(row==0 || row==1 || row==2 || row>=table.getRowCount()-3) ) {
			l.setBackground(new Color(173,216,230));
		}
			
			 
			
		if (column ==0 && row>=table.getRowCount()-3 && table.getRowCount()>3) {
			l.setForeground(Color.red);
		}
		l.setFocusable(false);
			l.setText(""+value+"");
		
		
		l.setOpaque(true);
		return l;
	}

}
