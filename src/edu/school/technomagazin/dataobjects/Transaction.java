package edu.school.technomagazin.dataobjects;

import java.util.Date;

public class Transaction {
	int transactionId;
	Type type;
	String description;
	Date date;
	int productId;
	int quantity; // THIS IS ALWAYS POSITIVE
	
	public enum Type {
		SELECT_TYPE, in, out
	}

	public Transaction(Type type, String description, Date date, int productId, int quantity) {
		this(-1, type, description, date, productId, quantity);
	}
	
	public Transaction(int transactionId, Type type, String description, Date date, int productId, int quantity) {
		this.transactionId = transactionId;
		this.type = type;
		this.description = description;
		this.date = date;
		this.productId = productId;
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "Transaction [transactionId=" + transactionId + ", type=" + type + ", description=" + description
				+ ", date=" + date + ", productId=" + productId + ", quantity=" + quantity + "]";
	}

	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
}
