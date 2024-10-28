package dao;

import java.sql.SQLException;
import java.util.List;

import model.Book;
import model.CartItem;
import model.User;

public interface BookDao {
	List<model.Book> getAllBooks() throws SQLException;
	public void addBookToCart(Book book, int quantity, User user);
	public List<CartItem> getCartItems(User user);

}
