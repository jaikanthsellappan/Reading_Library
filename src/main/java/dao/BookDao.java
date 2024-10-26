package dao;

import java.awt.print.Book;
import java.sql.SQLException;
import java.util.List;

public interface BookDao {
	List<model.Book> getAllBooks() throws SQLException;

}
