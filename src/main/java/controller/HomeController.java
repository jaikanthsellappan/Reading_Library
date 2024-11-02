package controller;

import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Book;
import model.Model;
import model.Order;
import model.User;

public class HomeController {
    private Model model;
    private Stage stage;
    private Stage parentStage;

    @FXML
    private Label welcomeLabel;  // Label for welcome message
    @FXML
    private MenuItem viewProfile;
    @FXML
    private MenuItem updateProfile;
    @FXML
    private MenuItem signOut; 
    @FXML
    private TableView<Book> bookTable;
    @FXML
    private Button exportOrdersButton;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> authorsColumn;
    @FXML
    private TableColumn<Book, Integer> priceColumn;
    @FXML
    private TableColumn<Book, Integer> copiesColumn;
    @FXML
    private TableColumn<Book, Void> actionColumn;
    @FXML
    private Button cartButton;
    @FXML
    private Button ordersButton;

    private ObservableList<Book> booksData;

    public HomeController(Stage parentStage, Model model) {
        this.stage = new Stage();
		this.parentStage = parentStage;
        this.model = model;
    }

    @FXML
    public void initialize() throws SQLException {
    	// Set column resize policy to prevent extra columns
    	bookTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        User currentUser = model.getCurrentUser();
        welcomeLabel.setText("Welcome, " + currentUser.getUsername() + "!");  // Set personalized welcome message

        viewProfile.setOnAction(event -> viewUserProfile());
        updateProfile.setOnAction(event -> updateUserProfile());
        signOut.setOnAction(event -> {
            stage.close();
            parentStage.show();
        });

        cartButton.setOnAction(event -> openCart());
        ordersButton.setOnAction(event -> openOrders());

        // Configure table columns
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorsColumn.setCellValueFactory(new PropertyValueFactory<>("authors"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        copiesColumn.setCellValueFactory(new PropertyValueFactory<>("physicalCopies"));

        // Add button to each row in action column
        addButtonToTable();

        // Load data into the table
        loadBooksData();
    }

    private void openCart() {
    	System.out.println("Cart opened"); 
        try {
            // Load the FXML for the Cart dialog
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CartView.fxml"));
            
            // Set the controller for the CartView
            CartController cartController = new CartController(model);
            loader.setController(cartController);
            
            VBox cartPane = loader.load();
            
            // Create a new dialog stage for the Cart window
            Stage cartStage = new Stage();
            cartStage.setTitle("My Cart");
            cartStage.initModality(Modality.WINDOW_MODAL);
            cartStage.initOwner(stage);
            
            // Set the scene and show the dialog
            Scene scene = new Scene(cartPane);
            cartStage.setScene(scene);
            
            // Show the cart stage and wait for it to close
            cartStage.showAndWait();
            
            // After closing the cart, reload the book data in the home page
            loadBooksData();

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void openOrders() {
        System.out.println("Orders opened"); // Placeholder for orders logic
        try {
            List<Order> orders = model.getOrders(model.getCurrentUser());
            if (orders.isEmpty()) {
                showAlert("Orders", "You have no orders.");
            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/OrderView.fxml"));
                loader.setController(new OrderController(orders));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("Order History");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(this.stage);
                stage.showAndWait();
            }
        } catch (SQLException | IOException e) {
            showAlert("Error", "Could not load orders. Please try again.");
            e.printStackTrace();
        }
    }
    
    @FXML
    private void exportOrders() {
        try {
            List<Order> orders = model.getOrders(model.getCurrentUser());
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Orders");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                model.exportOrdersToCSV(orders, file);
                showAlert("Export Successful", "Orders exported successfully to " + file.getAbsolutePath());
            }
        } catch (IOException | SQLException e) {
            showAlert("Export Error", "Failed to export orders. Please try again.");
            e.printStackTrace();
        }
    }

    private void loadBooksData() throws SQLException {
        booksData = FXCollections.observableArrayList(model.books());
        bookTable.setItems(booksData);
    }

    private void viewUserProfile() {
        User user = model.getCurrentUser();
        System.out.println("Viewing profile for: " + user.getUsername());
    }

    private void updateUserProfile() {
    	try {
            // Load the FXML for the Edit Profile dialog
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EditProfile.fxml"));
            loader.setController(new EditProfileController(model));
            
            VBox editProfilePane = loader.load();
            
            // Create a new dialog stage for the Edit Profile window
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Profile");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(stage);
            
            // Set the scene and show the dialog
            Scene scene = new Scene(editProfilePane);
            dialogStage.setScene(scene);
            
            // Set dialog stage to the controller
            EditProfileController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            
            dialogStage.showAndWait();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    	
    }

    private void addButtonToTable() {
        Callback<TableColumn<Book, Void>, TableCell<Book, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Book, Void> call(final TableColumn<Book, Void> param) {
                return new TableCell<>() {
                    private final Button addButton = new Button("Add to Cart");

                    {
                        addButton.setOnAction(event -> {
                            Book book = getTableView().getItems().get(getIndex());
                            addToCart(book);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(addButton);
                        }
                    }
                };
            }
        };
        actionColumn.setCellFactory(cellFactory);
    }
    
    private void addToCart(Book book) {
        // Assume that the quantity for adding to the cart is 1 by default.
        int quantity = 1; 
        String message = model.addBookToCart(book, quantity, model.getCurrentUser());

        // Show feedback to the user. In a real application, you would use a JavaFX Alert or Label for this.
        System.out.println(message);

        if (message.startsWith("Warning")) {
            // Handle the warning message by displaying it to the user.
            // For example, use a dialog or alert:
            showAlert("Warning", message);
        } else {
            System.out.println("Added to cart: " + book.getTitle());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    
    public void showStage(AnchorPane root) {
        Scene scene = new Scene(root, 600, 400);  // Adjust the scene size
        stage.setScene(scene);
        stage.setResizable(true);  // Allow resizing
        stage.setTitle("Home");
        stage.show();
    }
}
