package edu.school.technomagazin;

import java.io.FileReader;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.mariadb.jdbc.Connection;
import org.mariadb.jdbc.Statement;

import edu.school.technomagazin.dataobjects.Product;
import edu.school.technomagazin.dataobjects.Transaction;

public class DatabaseManager {

	static Connection connection = null;

	public static void initConnection() throws SQLException {
		JSONParser parser = new JSONParser();
		JSONObject data = null;
		try {
			data = (JSONObject) parser.parse(new FileReader("./env.json"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject db = (JSONObject) data.get("database");
		String url = (String) db.get("url");
		String user = (String) db.get("username");
		String password = (String) db.get("password");

		connection = (Connection) DriverManager.getConnection("jdbc:mariadb://" + url, user, password);
		System.out.println("Connected successfully");
	}

	public static void closeConnection() throws SQLException {
		if (connection != null) {
			connection.close();
			System.out.println("Closed connection successfully");
		}
	}

	public static List<Product> queryProducts() {
		Statement st = connection.createStatement();
		ResultSet res = null;
		try {
			res = st.executeQuery("SELECT * FROM shop.products");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return asProductList(res);
	}

	public static void deleteProductsBatch(int minId, int maxId) {
		try {
			PreparedStatement st = connection.prepareStatement(
					"DELETE FROM shop.products "
					+ "WHERE ProductId BETWEEN ? and ?");
			st.setInt(1, minId);
			st.setInt(2, maxId);
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void addProduct(Product p) {
		try {
			PreparedStatement st = connection.prepareStatement(
					"INSERT INTO shop.products "
					+ "(Name, Description, Price) "
					+ "VALUES (? , ?, ?) RETURNING ProductID, CurrentQuantity");
			st.setString(1, p.getName());
			st.setString(2, p.getDescription());
			st.setFloat(3, p.getPrice());
			ResultSet res = st.executeQuery();
			res.next();
			p.setProductId(res.getInt(1));
			p.setCurrentQuantity(res.getInt(2));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static int getProductQuantity(int productId) {
		PreparedStatement checkQuantitySt;
		try {
			checkQuantitySt = connection.prepareStatement("SELECT CurrentQuantity FROM shop.products WHERE ProductID = ?");
			checkQuantitySt.setInt(1, productId);
			ResultSet res = checkQuantitySt.executeQuery();
			res.next();
			return res.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public static List<Transaction> getAllTransactions() {
		Statement st = connection.createStatement();
		ResultSet res = null;
		try {
			res = st.executeQuery("SELECT * FROM shop.transactions");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return asTransactionList(res);
	}
	
	public static List<Transaction> queryTransactions(Date min, Date max) {
		ResultSet res = null;
		try {
			PreparedStatement st = connection.prepareStatement("SELECT * FROM shop.transactions WHERE _Date > ? AND _Date < ?");
			st.setTimestamp(1, new Timestamp(min.getTime()));
			st.setTimestamp(2, new Timestamp(max.getTime()));
			res = st.executeQuery();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return asTransactionList(res);
	}

	public static void addTransaction(Transaction t) {
		try {
			PreparedStatement st = connection.prepareStatement(
					"INSERT INTO shop.transactions "
					+ "(Type, Description, _Date, ProductID, Quantity) "
					+ "VALUES (?, ?, ?, ?, ?) "
					+ "RETURNING TransactionID");
			st.setString(1, t.getType().name());
			st.setString(2, t.getDescription());
			Timestamp ts = new Timestamp(t.getDate().getTime());
			st.setTimestamp(3, ts);
			st.setInt(4, t.getProductId());
			st.setInt(5, t.getQuantity());

			ResultSet res = st.executeQuery();
			res.next();
			t.setTransactionId(res.getInt(1));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			processTransaction(t);
		}
	}

	public static void processTransaction(Transaction t) {
		try {
			PreparedStatement st = connection.prepareStatement(
					"UPDATE shop.products SET CurrentQuantity = CurrentQuantity + ? WHERE ProductID = ? AND CurrentQuantity + ? >= 0");
			st.setInt(2, t.getProductId());
			if (t.getType().equals(Transaction.Type.in)) {
				st.setInt(1, t.getQuantity());
				st.setInt(3, t.getQuantity());
			} else if (t.getType().equals(Transaction.Type.out)) {
				st.setInt(1, -t.getQuantity());
				st.setInt(3, -t.getQuantity());
			} else {
				throw new RuntimeException("Invalid transaction type: " + t.getType().name());
			}
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void deleteTransaction(Transaction t) {
		try {
			PreparedStatement st = connection.prepareStatement("DELETE FROM shop.transactions WHERE TransactionID = ?");
			st.setInt(1, t.getTransactionId());
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			t.setQuantity(-t.getQuantity());
			processTransaction(t);
			t.setTransactionId(-1);
		}
	}
	
	public static List<Transaction> asTransactionList(ResultSet res) {
		ArrayList<Transaction> transactions = new ArrayList<>();
		try {
			while (res.next()) {
				Transaction t = new Transaction(res.getInt(1), Transaction.Type.valueOf(res.getString(2)),
						res.getString(3), new java.util.Date(res.getTimestamp(4).getTime()), res.getInt(5),
						res.getInt(6));
				transactions.add(t);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return transactions;
	}
	
	public static List<Product> asProductList(ResultSet res) {
		ArrayList<Product> products = new ArrayList<>();
		try {
			while (res.next()) {
				Product p = new Product(res.getInt(1), res.getString(2), res.getString(3), res.getFloat(4),
						res.getInt(5));
				products.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return products;
	}
}
