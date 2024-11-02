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
    public int getBookStock(Book book) {
        String sql = "SELECT physical_copies FROM books WHERE title = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, book.getTitle());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("physical_copies");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int getCartQuantity(User user, Book book) {
        String sql = "SELECT quantity FROM cart WHERE username = ? AND book_title = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, book.getTitle());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("quantity");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void updateCartItem(User user, Book book, int newQuantity) {
        String sql = "UPDATE cart SET quantity = ? WHERE username = ? AND book_title = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, newQuantity);
            stmt.setString(2, user.getUsername());
            stmt.setString(3, book.getTitle());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeCartItem(User user, Book book) {
        String sql = "DELETE FROM cart WHERE username = ? AND book_title = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, book.getTitle());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void finalizeCheckout(User user) {
    	List<CartItem> cartItems = getCartItems(user);
        for (CartItem item : cartItems) {
            try {
                Book book = getBookByTitle(item.getBookTitle());
                int newStock = book.getPhysicalCopies() - item.getQuantity();
                updateBookStock(book, newStock);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        clearUserCart(user);
    }
    
    @Override
    public Book getBookByTitle(String title) throws SQLException {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE title = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, title);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Book book = new Book();
                book.setTitle(rs.getString("title"));
                book.setAuthors(rs.getString("authors"));
                book.setPhysicalCopies(rs.getInt("physical_copies"));
                book.setPrice(rs.getDouble("price"));
                book.setSoldCopies(rs.getInt("sold_copies"));
                return book;
            }
        }
        return null;
    }

    @Override
    public void updateBookStock(Book book, int newStock) throws SQLException {
    	String sql = "UPDATE books SET physical_copies = ? WHERE title = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, newStock);
            stmt.setString(2, book.getTitle());
            stmt.executeUpdate();
        }
    }

    public void clearUserCart(User user) {
        String sql = "DELETE FROM cart WHERE username = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.executeUpdate();
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