package edu.school.technomagazin;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import edu.school.technomagazin.menus.ProductsMenu;
import edu.school.technomagazin.menus.TransactionsMenu;
import javax.swing.JComboBox;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;

import edu.school.technomagazin.dataobjects.Transaction.Type;
import javax.swing.JCheckBox;


public class Main {

	private JFrame frame;
	private ProductsMenu queryProductsMenu;
	private TransactionsMenu queryTransactionsMenu;

	private JPanel currentMenu;
	
	public static final int WINDOW_WIDTH = 800;
	public static final int WINDOW_HEIGHT = 600;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, WINDOW_WIDTH, WINDOW_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        try {
					DatabaseManager.closeConnection();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		    }
		});
		frame.getContentPane().setLayout(null);
		frame.setTitle("TehnoMagazin");

		JButton btnQueryProducts = new JButton("Products");
		btnQueryProducts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCurrentMenu(queryProductsMenu);
				queryProductsMenu.refreshTable();
			}
		});
		btnQueryProducts.setBounds(12, 12, 180, 27);
		frame.getContentPane().add(btnQueryProducts);

		JButton btnQueryTransactions = new JButton("Transactions");
		btnQueryTransactions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCurrentMenu(queryTransactionsMenu);
				queryTransactionsMenu.refreshTable();
			}
		});
		btnQueryTransactions.setBounds(202, 12, 180, 27);
		frame.getContentPane().add(btnQueryTransactions);

		frame.setResizable(false);
		
		queryProductsMenu = new ProductsMenu();
		frame.getContentPane().add(queryProductsMenu);

		
		queryTransactionsMenu = new TransactionsMenu();
		queryTransactionsMenu.setBounds(0, 40, WINDOW_WIDTH, WINDOW_HEIGHT - 80);
		queryTransactionsMenu.setVisible(false);
		frame.getContentPane().add(queryTransactionsMenu);
		queryTransactionsMenu.setLayout(null);
		
		Image logo = null;
		try {
			logo = ImageIO.read(new File("./technomagazin.png")).getScaledInstance(210, 52, Image.SCALE_DEFAULT);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		JLabel lblLogo = new JLabel(new ImageIcon(logo));
		lblLogo.setBounds(542, 12, 210, 52);
		frame.getContentPane().add(lblLogo);
		
		try {
			DatabaseManager.initConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			closeWindow();
		}
	}

	void closeWindow() {
		try {
			DatabaseManager.closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}

	void setCurrentMenu(JPanel menu) {
		if (currentMenu != null)
			currentMenu.setVisible(false);
		if (menu != null)
			menu.setVisible(true);
		currentMenu = menu;
		frame.repaint();
	}
}

