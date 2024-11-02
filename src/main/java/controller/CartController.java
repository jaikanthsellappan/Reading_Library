package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import model.Book;
import model.CartItem;
import model.Model;

import java.sql.SQLException;
import java.util.Optional;

public class CartController {

    private Model model;

    @FXML
    private TableView<CartItem> cartTable;
    @FXML
    private TableColumn<CartItem, String> bookTitleColumn;
    @FXML
    private TableColumn<CartItem, Integer> quantityColumn;
    @FXML
    private TableColumn<CartItem, Void> actionColumn;
    @FXML
    private Button checkoutButton;

    public CartController(Model model) {
        this.model = model;
    }

    @FXML
    public void initialize() {
        bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        addActionButtonsToCart();
        loadCartData();

        checkoutButton.setOnAction(event -> finalizeCheckout());
    }

    private void loadCartData() {
        ObservableList<CartItem> cartItems = FXCollections.observableArrayList(model.getCartItems(model.getCurrentUser()));
        cartTable.setItems(cartItems);
    }

    private void addActionButtonsToCart() {
        Callback<TableColumn<CartItem, Void>, TableCell<CartItem, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<CartItem, Void> call(final TableColumn<CartItem, Void> param) {
                return new TableCell<>() {
                    private final Button updateButton = new Button("Update");
                    private final Button removeButton = new Button("Remove");

                    {
                        updateButton.setOnAction(event -> {
                            CartItem item = getTableView().getItems().get(getIndex());
                            updateCartItemQuantity(item);
                        });

                        removeButton.setOnAction(event -> {
                            CartItem item = getTableView().getItems().get(getIndex());
                            removeCartItem(item);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox hBox = new HBox(updateButton, removeButton);
                            hBox.setSpacing(10);
                            setGraphic(hBox);
                        }
                    }
                };
            }
        };
        actionColumn.setCellFactory(cellFactory);
    }

    private void updateCartItemQuantity(CartItem cartItem) {
        TextInputDialog dialog = new TextInputDialog(String.valueOf(cartItem.getQuantity()));
        dialog.setTitle("Update Quantity");
        dialog.setHeaderText("Update quantity for " + cartItem.getBookTitle());
        dialog.setContentText("Enter new quantity:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(quantityStr -> {
            try {
                int newQuantity = Integer.parseInt(quantityStr);
                if (newQuantity <= 0) {
                    showAlert("Error", "Quantity must be greater than zero.");
                } else {
                    Book book = model.getBookByTitle(cartItem.getBookTitle());
                    int availableStock = model.getBookStock(book);

                    if (newQuantity > availableStock) {
                        showAlert("Warning", "Only " + availableStock + " copies available for " + book.getTitle());
                    } else {
                        model.updateCartItem(model.getCurrentUser(), book, newQuantity);
                        loadCartData(); // Refresh the cart data
                    }
                }
            } catch (NumberFormatException e) {
                showAlert("Error", "Invalid quantity entered.");
            } catch (SQLException e) {
                showAlert("Error", "An error occurred while fetching book details.");
                e.printStackTrace();
            }
        });
    }

    private void removeCartItem(CartItem cartItem) {
        Book book = null;
        try {
            book = model.getBookByTitle(cartItem.getBookTitle());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        model.removeCartItem(model.getCurrentUser(), book);
        loadCartData();
    }

    private void finalizeCheckout() {
    	try {
            // Check if all items in the cart have sufficient stock
            for (CartItem item : model.getCartItems(model.getCurrentUser())) {
                Book book = model.getBookByTitle(item.getBookTitle());
                int availableStock = model.getBookStock(book);

                if (item.getQuantity() > availableStock) {
                    showAlert("Checkout Error", "Insufficient stock for " + book.getTitle() + ". Only " + availableStock + " copies available.");
                    return;
                }
            }

            // Collect payment information
            TextInputDialog cardDialog = new TextInputDialog();
            cardDialog.setTitle("Payment");
            cardDialog.setHeaderText("Enter your payment details");

            // Collect and validate card number
            cardDialog.setContentText("Card Number (16 digits):");
            Optional<String> cardNumber = cardDialog.showAndWait();
            if (!cardNumber.isPresent() || !cardNumber.get().matches("\\d{16}")) {
                showAlert("Payment Error", "Invalid card number. It must be 16 digits.");
                return;
            }

            // Collect and validate expiry date
            cardDialog.setContentText("Expiry Date (MM/YY):");
            Optional<String> expiryDate = cardDialog.showAndWait();
            if (!expiryDate.isPresent() || !isFutureDate(expiryDate.get())) {
                showAlert("Payment Error", "Invalid expiry date. It must be a future date.");
                return;
            }

            // Collect and validate CVV
            cardDialog.setContentText("CVV (3 digits):");
            Optional<String> cvv = cardDialog.showAndWait();
            if (!cvv.isPresent() || !cvv.get().matches("\\d{3}")) {
                showAlert("Payment Error", "Invalid CVV. It must be 3 digits.");
                return;
            }

            // If payment is valid, proceed with the checkout
            double totalPrice = model.calculateTotalCartPrice(model.getCurrentUser());
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Checkout");
            alert.setHeaderText("Total Price: $" + totalPrice);
            alert.setContentText("Do you want to confirm the order?");
            
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                model.finalizeCheckout(model.getCurrentUser(), totalPrice);
                showAlert("Checkout Successful", "Your order has been placed successfully.");
                loadCartData(); // Refresh the cart after checkout
            }
        } catch (SQLException e) {
            showAlert("Checkout Error", "An error occurred during checkout. Please try again.");
            e.printStackTrace();
        }
    }

    // Helper method to validate future expiry date
    private boolean isFutureDate(String expiryDate) {
        // Implement your date validation logic here
        // Example: Parse and check if the date is in the future
        return true; // Placeholder
    }
    	
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
