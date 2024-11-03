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
import javafx.scene.layout.GridPane;
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
        this.stage = new Stage(); // Initialize new stage for HomeController
        this.parentStage = parentStage; // Set parent stage reference for sign-out action
        this.model = model; // Reference to model for data access
    }

    @FXML
    public void initialize() throws SQLException {
        // Adjust column resizing policy to prevent empty columns from appearing
        bookTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        // Retrieve current user and set personalized welcome message
        User currentUser = model.getCurrentUser();
        welcomeLabel.setText("Welcome, " + currentUser.getUsername() + "!");

        // Assign event handlers for menu items
        viewProfile.setOnAction(event -> viewUserProfile());
        updateProfile.setOnAction(event -> updateUserProfile());
        signOut.setOnAction(event -> {
            try {
                // Load Login View and set it on the parent stage
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
                LoginController loginController = new LoginController(parentStage, model);
                loader.setController(loginController);

                // Load the LoginView and set it on the parent stage
                GridPane loginRoot = loader.load();
                Scene loginScene = new Scene(loginRoot);

                // Set the Login scene and show the parent stage
                parentStage.setScene(loginScene);
                parentStage.setTitle("Login");
                parentStage.show();

                // Close the current HomeController stage
                stage.close();

            } catch (IOException e) {
                e.printStackTrace(); // Print error if loading Login view fails
            }
        });

        // Set up event handlers for the cart and orders buttons
        cartButton.setOnAction(event -> openCart());
        ordersButton.setOnAction(event -> openOrders());

        // Set up cell value factories for table columns
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorsColumn.setCellValueFactory(new PropertyValueFactory<>("authors"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        copiesColumn.setCellValueFactory(new PropertyValueFactory<>("physicalCopies"));

        // Configure action column with add-to-cart button for each row
        addButtonToTable();

        // Load book data into the table
        loadBooksData();
    }

    private void openCart() {
        System.out.println("Cart opened");
        try {
            // Load Cart View and set controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CartView.fxml"));
            CartController cartController = new CartController(model);
            loader.setController(cartController);

            // Load cart pane and set scene
            VBox cartPane = loader.load();
            Stage cartStage = new Stage();
            cartStage.setTitle("My Cart");
            cartStage.initModality(Modality.WINDOW_MODAL);
            cartStage.initOwner(stage);

            Scene scene = new Scene(cartPane);
            cartStage.setScene(scene);

            // Show cart stage as a dialog and wait for it to close
            cartStage.showAndWait();

            // Reload book data after cart interaction
            loadBooksData();

        } catch (IOException | SQLException e) {
            e.printStackTrace(); // Print error if loading cart view fails
        }
    }

    private void openOrders() {
        System.out.println("Orders opened");
        try {
            // Retrieve current user's orders
            List<Order> orders = model.getOrders(model.getCurrentUser());
            if (orders.isEmpty()) {
                // Show alert if no orders found
                showAlert("Orders", "You have no orders.");
            } else {
                // Load Order View and set controller
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/OrderView.fxml"));
                loader.setController(new OrderController(orders));
                Parent root = loader.load();

                // Create new stage for order history
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
            // Retrieve orders and show file chooser for export destination
            List<Order> orders = model.getOrders(model.getCurrentUser());
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Orders");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                // Export orders to chosen CSV file
                model.exportOrdersToCSV(orders, file);
                showAlert("Export Successful", "Orders exported successfully to " + file.getAbsolutePath());
            }
        } catch (IOException | SQLException e) {
            showAlert("Export Error", "Failed to export orders. Please try again.");
            e.printStackTrace();
        }
    }

    private void loadBooksData() throws SQLException {
        // Fetch book data from model and display in TableView
        booksData = FXCollections.observableArrayList(model.books());
        bookTable.setItems(booksData);
    }

    private void viewUserProfile() {
        try {
            // Load User Profile View and set controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UserProfileView.fxml"));
            UserProfileController userProfileController = new UserProfileController(model.getCurrentUser());
            loader.setController(userProfileController);

            // Show user profile in a new modal stage
            Parent root = loader.load();
            Stage profileStage = new Stage();
            userProfileController.setStage(profileStage);
            profileStage.setTitle("User Profile");
            profileStage.setScene(new Scene(root));
            profileStage.initModality(Modality.WINDOW_MODAL);
            profileStage.initOwner(stage);
            profileStage.showAndWait();

        } catch (IOException e) {
            showAlert("Error", "Unable to load profile view.");
            e.printStackTrace();
        }
    }

    private void updateUserProfile() {
        try {
            // Load Edit Profile View and set controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EditProfile.fxml"));
            loader.setController(new EditProfileController(model));

            // Show edit profile pane in a new dialog stage
            VBox editProfilePane = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Profile");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(stage);

            Scene scene = new Scene(editProfilePane);
            dialogStage.setScene(scene);

            EditProfileController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addButtonToTable() {
        // Define a button cell for the action column in TableView
        Callback<TableColumn<Book, Void>, TableCell<Book, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Book, Void> call(final TableColumn<Book, Void> param) {
                return new TableCell<>() {
                    private final Button addButton = new Button("Add to Cart");

                    {
                        addButton.setOnAction(event -> {
                            // Get the selected book and add to cart
                            Book book = getTableView().getItems().get(getIndex());
                            addToCart(book);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : addButton); // Set button if row is not empty
                    }
                };
            }
        };
        actionColumn.setCellFactory(cellFactory); // Set cell factory for action column
    }

    private void addToCart(Book book) {
        int quantity = 1; // Default quantity for cart addition
        String message = model.addBookToCart(book, quantity, model.getCurrentUser());

        if (message.startsWith("Warning")) {
            // Show warning message if addition is restricted
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
        Scene scene = new Scene(root, 600, 400);  // Set scene size
        stage.setScene(scene);
        stage.setResizable(true);  // Allow stage resizing
        stage.setTitle("Home");
        stage.show();
    }
}