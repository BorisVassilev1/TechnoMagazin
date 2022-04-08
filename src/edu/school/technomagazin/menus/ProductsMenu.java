package edu.school.technomagazin.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

import edu.school.technomagazin.DatabaseManager;
import edu.school.technomagazin.Main;
import edu.school.technomagazin.dataobjects.Product;
import edu.school.technomagazin.util.HintTextField;
import edu.school.technomagazin.util.ScrollableTable;

public class ProductsMenu extends JPanel {

	private static final long serialVersionUID = 1L;
	private JButton btnDeleteRow;
	private JButton btnAddProduct;
	private ScrollableTable productsTable;
	private List<Product> tableContent;
	
	private HintTextField nameField;
	private HintTextField descriptionField;
	private HintTextField priceField;

	public ProductsMenu() {
		super();
		setBounds(0, 40, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT - 80);
		setVisible(false);
		setLayout(null);
		setOpaque(false);

		btnDeleteRow = new JButton("Delete Rows");
		btnDeleteRow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int count = productsTable.getSelectedRowCount();
				if (count == 0)
					return;

				int confirmed = JOptionPane.showConfirmDialog(null,
						"Are you sure you want to delete all " + count + " selected rows?", "Deletion Warning",
						JOptionPane.YES_NO_OPTION);

				if (confirmed == JOptionPane.NO_OPTION) {
					return;
				}

				int[] rows = productsTable.getSelectedRows();
				int minId = tableContent.get(rows[0]).getProductId();
				int maxId = tableContent.get(rows[rows.length - 1]).getProductId();
				DatabaseManager.deleteProductsBatch(minId, maxId);
				refreshTable();
			}
		});
		btnDeleteRow.setBounds(12, 12, 140, 27);
		add(btnDeleteRow);

		btnAddProduct = new JButton("Add Product");
		btnAddProduct.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = nameField.getText();
				if (name.length() == 0) {
					JOptionPane.showMessageDialog(null, "Invalid field: 'name' cannot be empty.");
					return;
				} else if (name.length() > 40) {
					JOptionPane.showMessageDialog(null, "Invalid field: 'name' cannot be more than 40 symbols long.");
					return;
				}

				String description = descriptionField.getText();
				if (description.length() == 0) {
					JOptionPane.showMessageDialog(null, "Invalid field: 'description' cannot be empty.");
					return;
				} else if (description.length() > 200) {
					JOptionPane.showMessageDialog(null,
							"Invalid field: 'description' cannot be more than 200 symbols long.");
					return;
				}

				float price = -1;
				try {
					price = Float.parseFloat(priceField.getText());
				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(null, "Invalid field: 'price' must be a number.");
					return;
				}
				if (price <= 0) {
					JOptionPane.showMessageDialog(null, "Invalid field: 'price' must be a positive number.");
					return;
				}
				DatabaseManager.addProduct(new Product(name, description, price));
				refreshTable();
			}
		});
		btnAddProduct.setBounds(162, 12, 150, 27);
		add(btnAddProduct);

		nameField = new HintTextField("product name");
		nameField.setBounds(12, 50, 140, 27);
		add(nameField);

		descriptionField = new HintTextField("description");
		descriptionField.setBounds(162, 50, 200, 27);
		add(descriptionField);

		priceField = new HintTextField("price");
		priceField.setBounds(372, 50, 130, 27);
		add(priceField);

		productsTable = new ScrollableTable(new String[] { "Id", "Name", "Description", "Price", "Quantity" }, 0);
		productsTable.setBounds(12, 90, Main.WINDOW_WIDTH - 30, Main.WINDOW_HEIGHT - 172);
		add(productsTable);
	}

	public void refreshTable() {
		tableContent = DatabaseManager.queryProducts();
		DefaultTableModel model = (DefaultTableModel) productsTable.getModel();
		model.setRowCount(tableContent.size());
		for (int i = 0; i < tableContent.size(); ++i) {
			Product p = tableContent.get(i);
			model.setValueAt(p.getProductId(), i, 0);
			model.setValueAt(p.getName(), i, 1);
			model.setValueAt(p.getDescription(), i, 2);
			model.setValueAt(p.getPrice(), i, 3);
			model.setValueAt(p.getCurrentQuantity(), i, 4);
		}
	}
}
