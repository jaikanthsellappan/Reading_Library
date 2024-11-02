package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import dao.BookDao;
import dao.BookDaoImpl;
import dao.UserDao;
import dao.UserDaoImpl;
import model.Order.OrderItem;

public class Model {
    private UserDao userDao;
    private BookDao bookdao;
    private User currentUser; 
    
    public Model() {
        userDao = new UserDaoImpl();
        bookdao = new BookDaoImpl();
    }
    
    public void setup() throws SQLException {
        userDao.setup();
    }

    public UserDao getUserDao() {
        return userDao;
    }
    
    public List<Book> books() throws SQLException {
        List<Book> books = bookdao.getAllBooks();
        Collections.sort(books, Comparator.comparingInt(Book::getSoldCopies).reversed());
        return books.subList(0, Math.min(5, books.size())); // Return top 5 or fewer if not enough
    }
    
    public String addBookToCart(Book book, int quantity, User user) {
        int availableStock = bookdao.getBookStock(book);
        int currentCartQuantity = bookdao.getCartQuantity(user, book);
        
        if (currentCartQuantity + quantity > availableStock) {
            return "Warning: Only " + (availableStock - currentCartQuantity) + " copies available.";
        } else {
            bookdao.addBookToCart(book, quantity, user);
            return "Book added to cart: " + book.getTitle();
        }
    }
    
    public List<CartItem> getCartItems(User user) {
        return bookdao.getCartItems(user);
    }
    
    public void updateCartItem(User user, Book book, int newQuantity) {
        int availableStock = bookdao.getBookStock(book);
        if (newQuantity > availableStock) {
            System.out.println("Warning: Only " + availableStock + " copies available for " + book.getTitle());
        } else {
            bookdao.updateCartItem(user, book, newQuantity);
        }
    }

    public void removeCartItem(User user, Book book) {
        bookdao.removeCartItem(user, book);
    }

    public double calculateTotalCartPrice(User user) {
        List<CartItem> cartItems = bookdao.getCartItems(user);
        double totalPrice = 0;
        for (CartItem item : cartItems) {
            Book book = null;
			try {
				book = bookdao.getBookByTitle(item.getBookTitle());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            totalPrice += book.getPrice() * item.getQuantity();
        }
        return totalPrice;
    }
    
    public void finalizeCheckout(User user) {
    	bookdao.finalizeCheckout(user);
    }
    
    public void finalizeCheckout(User user, double totalPrice) throws SQLException {
        String orderNumber = generateOrderNumber();  // Generate a unique order number
        bookdao.saveOrder(user, orderNumber, totalPrice, getCartItems(user)); // Save order details to database
        bookdao.finalizeCheckout(user);  // Deduct stock and clear the cart
    }

    // Helper method to generate unique order number
    private String generateOrderNumber() {
        return "ORD" + System.currentTimeMillis();
    }
    
    public List<Order> getOrders(User user) throws SQLException {
        return bookdao.getOrders(user);
    }

    public void exportOrdersToCSV(List<Order> orders, File file) throws IOException {
    	try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("Order Number,Date,Total Price,Books");

            for (Order order : orders) {
                writer.printf("%s,%s,%.2f,%s\n",
                    order.getOrderNumber(),
                    order.getOrderDate(),
                    order.getTotalPrice(),
                    order.getBookDetails());
            }
        }
    }

    
    public Book getBookByTitle(String title) throws SQLException {
		return bookdao.getBookByTitle(title);
    	
    }
    
    public void updateUser(User user) throws SQLException {
    	userDao.updateUser(user);
    }

    public User getCurrentUser() {
        return this.currentUser;
    }
    
    public void setCurrentUser(User user) {
        currentUser = user;
    }

	public int getBookStock(Book book) {
		return bookdao.getBookStock(book);
	}
}
