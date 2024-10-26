package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Book;

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

}
