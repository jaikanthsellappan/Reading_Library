package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Book;
import model.CartItem;
import model.User;

public class BookDaoImpl implements BookDao{
	
	private final String TABLE_NAME = "books"; 

    @Override
    public List<Book> getAllBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME;

        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Book book = new Book();
                book.setTitle(rs.getString("title"));
                book.setAuthors(rs.getString("authors"));
                book.setPhysicalCopies(rs.getInt("physical_copies"));
                book.setPrice(rs.getDouble("price"));
                book.setSoldCopies(rs.getInt("sold_copies"));

                books.add(book);
            }
        }
        return books;
    }
    
    @Override
    public void addBookToCart(Book book, int quantity, User user) {
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                 "INSERT INTO cart (username, book_title, quantity) VALUES (?, ?, ?)")) {
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, book.getTitle());
            stmt.setInt(3, quantity);
            
            stmt.executeUpdate();
            System.out.println("Book added to cart: " + book.getTitle());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<CartItem> getCartItems(User user) {
        List<CartItem> cartItems = new ArrayList<>();
        String sql = "SELECT * FROM cart WHERE username = ?";
        
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setString(1, user.getUsername());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                CartItem item = new CartItem(rs.getString("book_title"), rs.getInt("quantity"));
                cartItems.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartItems;
    }

}
