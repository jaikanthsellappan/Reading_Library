package controller;

import javafx.scene.control.Label; // JavaFX Label
import java.sql.SQLException;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Book;
import model.Model;
import model.User;

public class HomeController {
	private Model model;
	private Stage stage;
	private Stage parentStage;
	@FXML
	private MenuItem viewProfile; // Corresponds to the Menu item "viewProfile" in HomeView.fxml
	@FXML
	private MenuItem updateProfile; // // Corresponds to the Menu item "updateProfile" in HomeView.fxml
	@FXML
	private Label popularBooksLabel; // Add this to match the FXML file's Label
	
	public HomeController(Stage parentStage, Model model) {
		this.stage = new Stage();
		this.parentStage = parentStage;
		this.model = new Model();
	}
	
	// Add your code to complete the functionality of the program
	@FXML
    public void initialize() throws SQLException {
        viewProfile.setOnAction(event -> viewUserProfile());
        updateProfile.setOnAction(event -> updateUserProfile());

        // Display popular books
        displayPopularBooks();
    }

    private void viewUserProfile() {
        // Logic to display the user's profile
        User user = model.getCurrentUser();
        // Implement a way to show user information, e.g., open a new dialog
        System.out.println("Viewing profile for: " + user.getUsername());
        // Here you might want to create a separate ProfileController and FXML for viewing the profile
    }

    private void updateUserProfile() {
        // Logic to update the user's profile
        System.out.println("Updating profile for: " + model.getCurrentUser().getUsername());
        // Similar to viewing profile, implement a dialog or new screen for updating user info
    }

    private void displayPopularBooks() throws SQLException {
        List<Book> popularBooks = model.books(); // Implement this method in your Model class
        StringBuilder sb = new StringBuilder("Top 5 Popular Books:\n");
        for (Book book : popularBooks) {
            sb.append(book.getTitle()).append(" by ").append(book.getAuthors()).append("\n");
        }
        popularBooksLabel.setText(sb.toString());
    }
	
	
	
	
	public void showStage(Pane root) {
		Scene scene = new Scene(root, 500, 300);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle("Home");
		stage.show();
	}
}
