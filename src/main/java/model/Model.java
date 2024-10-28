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
	
	public void addBookToCart(Book book, int quantity, User user) {
        // Implement the logic to add the book to the user's shopping cart
		bookdao.addBookToCart(book, quantity, user);
        System.out.println("Book added to cart: " + book.getTitle());
    }
	
	public List<CartItem> getCartItems(User user){
		 return bookdao.getCartItems(user);
	}
	
	public User getCurrentUser() {
		return this.currentUser;
	}
	
	public void setCurrentUser(User user) {
		currentUser = user;
	}
	
	public void updateUser(User user) {
		try {
			userDao.updateUser(user);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    // Implement the logic to update the user's profile in the database
	    System.out.println("User profile updated: " + user.getUsername());
	}
	
}
