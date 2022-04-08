package edu.school.technomagazin.dataobjects;

public class Product {
	int productId;
	String name;
	String description;
	float price;
	int currentQuantity;
	
	@Override
	public String toString() {
		return "Product [productId=" + productId + ", name=" + name + ", description=" + description + ", price="
				+ price + ", currentQuantity=" + currentQuantity + "]";
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getCurrentQuantity() {
		return currentQuantity;
	}

	public void setCurrentQuantity(int currentQuantity) {
		this.currentQuantity = currentQuantity;
	}

	public Product(String name, String description, float price) {
		this(-1, name, description, price, -1);
	}
	
	public Product(int productId, String name, String description, float price, int currentQuantity) {
		this.productId = productId;
		this.name = name;
		this.description = description;
		this.price = price;
		this.currentQuantity = currentQuantity;
	}
}
