package edu.school.technomagazin.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

import edu.school.technomagazin.DatabaseManager;
import edu.school.technomagazin.Main;
import edu.school.technomagazin.dataobjects.Transaction;
import edu.school.technomagazin.dataobjects.Transaction.Type;
import edu.school.technomagazin.util.DateSpinner;
import edu.school.technomagazin.util.HintTextField;
import edu.school.technomagazin.util.NumberSpinner;
import edu.school.technomagazin.util.ScrollableTable;

public class TransactionsMenu extends JPanel {
	private static final long serialVersionUID = 1L;
	private JButton btnDeleteRow;
	private JButton btnAddProduct;
	private ScrollableTable transactionsTable;
	private List<Transaction> tableContent;
	
	private JComboBox<Transaction.Type> typeComboBox;
	private HintTextField descriptionField;
	private DateSpinner dateField;
	private JCheckBox chckbxUseCurrentDate;
	private NumberSpinner productIdField;
	private NumberSpinner quantityField;
	private DateSpinner minDateField;
	private DateSpinner maxDateField;
	private JButton btnQueryTransactions;
	private JButton btnAllTransactions;
	
	public TransactionsMenu() {
		super();
		setBounds(0, 40, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT - 80);
		setVisible(false);
		setLayout(null);
		setOpaque(false);
		
		btnDeleteRow = new JButton("Delete Rows");
		btnDeleteRow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int count = transactionsTable.getSelectedRowCount();
				if (count == 0)
					return;
				else if(count > 1) {
					JOptionPane.showMessageDialog(null, "Error: select only one row to delete.");
				}

				int confirmed = JOptionPane.showConfirmDialog(null,
						"Are you sure you want to delete that transaction?", "Deletion Warning",
						JOptionPane.YES_NO_OPTION);

				if (confirmed == JOptionPane.NO_OPTION) {
					return;
				}

				int row = transactionsTable.getSelectedRow();
				DatabaseManager.deleteTransaction(tableContent.get(row));
				refreshTable();
			}
		});
		btnDeleteRow.setBounds(12, 12, 140, 27);
		add(btnDeleteRow);

		btnAddProduct = new JButton("Add Transaction");
		btnAddProduct.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Type transactionType = (Type) typeComboBox.getSelectedItem();
				if (transactionType.equals(Type.SELECT_TYPE)) {
					JOptionPane.showMessageDialog(null, "Invalid field: 'type' cannot be the default option.");
					return;
				}

				String description = descriptionField.getText();
				if (description.length() == 0) {
					JOptionPane.showMessageDialog(null, "Invalid field: 'description' cannot be empty.");
					return;
				} else if (description.length() > 70) {
					JOptionPane.showMessageDialog(null,
							"Invalid field: 'description' cannot be more than 70 symbols long.");
					return;
				}

				Date date = null;
				if(chckbxUseCurrentDate.isSelected()) {
					date = new Date();
				} else {
					date = dateField.getDate();
				}
				
				int productId = (int) productIdField.getValue();
				int quantity = (int) quantityField.getValue();
				if (quantity == 0) {
					JOptionPane.showMessageDialog(null, "Invalid field: 'quantity' cannot be 0.");
					return;
				}
				
				if(transactionType.equals(Type.out)) {
					int currentQuantity = DatabaseManager.getProductQuantity(productId);
					if(currentQuantity < quantity) {
						JOptionPane.showMessageDialog(null, "Invalid transaction: cannot sell more than is present in the shop");
						return;
					}
				}
				
				DatabaseManager.addTransaction(new Transaction(transactionType, description, date, productId, quantity));
				refreshTable();
				descriptionField.reset();
				typeComboBox.setSelectedIndex(0);
				quantityField.setValue(0);
			}
		});
		btnAddProduct.setBounds(162, 12, 150, 27);
		add(btnAddProduct);

		typeComboBox = new JComboBox<Type>();
		typeComboBox.setModel(new DefaultComboBoxModel<Type>(Type.values()));
		typeComboBox.setBounds(12, 51, 140, 27);
		add(typeComboBox);

		descriptionField = new HintTextField("description");
		descriptionField.setBounds(162, 51, 200, 27);
		add(descriptionField);

		dateField = new DateSpinner();
		dateField.setBounds(372, 51, 150, 27);
		add(dateField);

		chckbxUseCurrentDate = new JCheckBox("Use Current Date");
		chckbxUseCurrentDate.setBounds(372, 12, 150, 27);
		chckbxUseCurrentDate.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				dateField.setEnabled(!dateField.isEnabled());
			}
		});
		add(chckbxUseCurrentDate);
		chckbxUseCurrentDate.setSelected(true);

		productIdField = new NumberSpinner(1, 1, Integer.MAX_VALUE);
		productIdField.setBounds(532, 51, 70, 27);
		add(productIdField);

		quantityField = new NumberSpinner(0, 0, Integer.MAX_VALUE);
		quantityField.setBounds(612, 51, 70, 27);
		add(quantityField);

		minDateField = new DateSpinner();
		minDateField.setBounds(12, 90, 140, 27);
		add(minDateField);
		
		maxDateField = new DateSpinner();
		maxDateField.setBounds(162, 90, 140, 27);
		add(maxDateField);
		
		btnQueryTransactions = new JButton("Filter");
		btnQueryTransactions.setBounds(312, 90, 120, 27);
		btnQueryTransactions.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				Date min = minDateField.getDate();
				Date max = maxDateField.getDate();
				refreshTable(DatabaseManager.queryTransactions(min, max));
			}
		});
		add(btnQueryTransactions);
		
		btnAllTransactions = new JButton("All");
		btnAllTransactions.setBounds(442, 90, 120, 27);
		btnAllTransactions.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refreshTable();
			}
		});
		add(btnAllTransactions);
		
		transactionsTable = new ScrollableTable(new String[] { "Id", "Type", "Description", "Date", "ProductId", "Quantity" }, 0);
		transactionsTable.setBounds(12, 129, Main.WINDOW_WIDTH - 30, Main.WINDOW_HEIGHT - 212);
		add(transactionsTable);
	}

	public void refreshTable() {
		refreshTable(DatabaseManager.getAllTransactions());
	}
	
	public void refreshTable(List<Transaction> transactions) {
		tableContent = transactions;
		DefaultTableModel model = (DefaultTableModel) transactionsTable.getModel();
		model.setRowCount(tableContent.size());
		for (int i = 0; i < tableContent.size(); ++i) {
			Transaction p = tableContent.get(i);
			model.setValueAt(p.getTransactionId(), i, 0);
			model.setValueAt(p.getType(), i, 1);
			model.setValueAt(p.getDescription(), i, 2);
			model.setValueAt(p.getDate(), i, 3);
			model.setValueAt(p.getProductId(), i, 4);
			model.setValueAt(p.getQuantity(), i, 5);
		}
	}
}
