package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Book;
import model.Model;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class AdminController {
    private Model model;
    private Stage stage;

    @FXML
    private Label welcomeLabel;
    @FXML
    private MenuItem viewProfile;
    @FXML
    private MenuItem updateProfile;
    @FXML
    private MenuItem signOut;
    @FXML
    private TableView<Book> bookTable;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> authorsColumn;
    @FXML
    private TableColumn<Book, Double> priceColumn;
    @FXML
    private TableColumn<Book, Integer> soldCopiesColumn;
    @FXML
    private TableColumn<Book, Integer> stockColumn;
    @FXML
    private Button increaseStockButton;
    @FXML
    private Button decreaseStockButton;

    private ObservableList<Book> booksData;

    public AdminController(Stage stage, Model model) {
        this.stage = stage;
        this.model = model;
    }

    @FXML
    public void initialize() throws SQLException {
        // Display a welcome message to the admin user
        welcomeLabel.setText("Welcome, " + model.getCurrentUser().getUsername() + " (Admin)");

        // Initialize menu actions
        viewProfile.setOnAction(event -> viewUserProfile());
        updateProfile.setOnAction(event -> updateUserProfile());
        signOut.setOnAction(event -> handleSignOut());

        // Configure table columns
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorsColumn.setCellValueFactory(new PropertyValueFactory<>("authors"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        soldCopiesColumn.setCellValueFactory(new PropertyValueFactory<>("soldCopies"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("physicalCopies"));

        // Load the data into the table
        loadBooksData();

        // Add actions for stock update buttons
        increaseStockButton.setOnAction(event -> adjustBookStock(true));
        decreaseStockButton.setOnAction(event -> adjustBookStock(false));
    }

    private void handleSignOut() {
        try {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
            LoginController loginController = new LoginController(stage, model);
            loader.setController(loginController);

            // Load LoginView as GridPane (or change to the correct root type)
            GridPane loginRoot = loader.load();  // Adjusted to match GridPane root
            Scene scene = new Scene(loginRoot);

            // Set the scene to the main stage and update title
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadBooksData() {
        try {
            booksData = FXCollections.observableArrayList(model.adminBooks());
            bookTable.setItems(booksData);
        } catch (SQLException e) {
            showAlert("Error", "Failed to load books data.");
        }
    }

    private void adjustBookStock(boolean isIncrease) {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            TextInputDialog dialog = new TextInputDialog("0");
            dialog.setTitle(isIncrease ? "Increase Stock" : "Decrease Stock");
            dialog.setHeaderText((isIncrease ? "Increase" : "Decrease") + " Stock for " + selectedBook.getTitle());
            dialog.setContentText("Enter quantity to " + (isIncrease ? "increase" : "decrease") + ":");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(quantityStr -> {
                try {
                    int quantity = Integer.parseInt(quantityStr);
                    int newStock = isIncrease ? selectedBook.getPhysicalCopies() + quantity : selectedBook.getPhysicalCopies() - quantity;

                    if (newStock < 0) {
                        showAlert("Error", "Stock cannot be negative.");
                        return;
                    }

                    model.updateBookStock(selectedBook, newStock);
                    selectedBook.setPhysicalCopies(newStock); // Update locally for immediate feedback
                    bookTable.refresh();
                    showAlert("Success", "Stock updated successfully.");
                } catch (NumberFormatException | SQLException e) {
                    showAlert("Error", "Invalid quantity or failed to update stock.");
                }
            });
        } else {
            showAlert("Error", "Please select a book to update.");
        }
    }

    private void viewUserProfile() {
        // no action required
    }

    private void updateUserProfile() {
        // no action required
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showStage(AnchorPane root) {
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setTitle("Admin Dashboard");
        stage.show();
    }
}
