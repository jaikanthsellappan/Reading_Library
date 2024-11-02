package model;

import java.sql.Timestamp;
import java.util.List;

public class Order {
    private String orderNumber;
    private double totalPrice;
    private Timestamp orderDate;
    private List<OrderItem> items; // List of items in this order

    // Constructor to initialize an order with all attributes
    public Order(String orderNumber, double totalPrice, Timestamp orderDate, List<OrderItem> items) {
        this.orderNumber = orderNumber;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
        this.items = items;
    }

    // Getters for each attribute
    public String getOrderNumber() {
        return orderNumber;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    // Optional: toString method for easy logging and debugging
    @Override
    public String toString() {
        return "Order{" +
                "orderNumber='" + orderNumber + '\'' +
                ", totalPrice=" + totalPrice +
                ", orderDate=" + orderDate +
                ", items=" + items +
                '}';
    }

    // Inner or separate class to represent an item within an order
    public static class OrderItem {
        private String bookTitle;
        private int quantity;

        // Constructor
        public OrderItem(String bookTitle, int quantity) {
            this.bookTitle = bookTitle;
            this.quantity = quantity;
        }

        // Getters
        public String getBookTitle() {
            return bookTitle;
        }

        public int getQuantity() {
            return quantity;
        }
        
        

        @Override
        public String toString() {
            return "OrderItem{" +
                    "bookTitle='" + bookTitle + '\'' +
                    ", quantity=" + quantity +
                    '}';
        }
    }

	public String getBookDetails() {
		StringBuilder bookDetails = new StringBuilder();
	    for (OrderItem item : items) {
	        bookDetails.append(item.getBookTitle())
	                   .append(" (Qty: ").append(item.getQuantity()).append("), ");
	    }
	    // Remove the last comma and space
	    if (bookDetails.length() > 0) {
	        bookDetails.setLength(bookDetails.length() - 2);
	    }
	    return bookDetails.toString();
	}
}
