package dao;

import java.sql.SQLException;
import java.util.List;

import model.Book;
import model.CartItem;
import model.Order;
import model.User;

public interface BookDao {
	List<model.Book> getAllBooks() throws SQLException;
	public void addBookToCart(Book book, int quantity, User user);
	public List<CartItem> getCartItems(User user);
	public int getBookStock(Book book);
	public int getCartQuantity(User user, Book book);
	public void updateCartItem(User user, Book book, int newQuantity);
	public void removeCartItem(User user, Book book);
	public void finalizeCheckout(User user);
	public void clearUserCart(User user);
	Book getBookByTitle(String title) throws SQLException;
    void updateBookStock(Book book, int newStock) throws SQLException;
    void saveOrder(User user, String orderNumber, double totalPrice, List<CartItem> cartItems) throws SQLException;
    List<Order> getOrders(User user) throws SQLException;

}
