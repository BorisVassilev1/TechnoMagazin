package edu.school.technomagazin.util;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ScrollableTable extends JPanel {
	private static final long serialVersionUID = 1L;

	private JTable table;

	public ScrollableTable(String[] columnNames, int rowCount) {
		super();
		setLayout(new BorderLayout());

		table = new JTable();
		table.setModel(new DefaultTableModel(columnNames, rowCount) {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
		table.getTableHeader().setReorderingAllowed(false);
		add(table);

		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane);
	}

	public JTable getTable() {
		return table;
	}

	public DefaultTableModel getModel() {
		return (DefaultTableModel) table.getModel();
	}

	public int getSelectedRow() {
		return table.getSelectedRow();
	}

	public int getSelectedRowCount() {
		return table.getSelectedRowCount();
	}

	public int[] getSelectedRows() {
		return table.getSelectedRows();
	}
}
