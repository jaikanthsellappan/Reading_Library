package model;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import dao.BookDao;
import dao.BookDaoImpl;
import dao.UserDao;
import dao.UserDaoImpl;

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
	
	public void addBookToCart(Book book) {
        // Implement the logic to add the book to the user's shopping cart
        System.out.println("Book added to cart: " + book.getTitle());
    }
	
	public User getCurrentUser() {
		return this.currentUser;
	}
	
	public void setCurrentUser(User user) {
		currentUser = user;
	}
}
