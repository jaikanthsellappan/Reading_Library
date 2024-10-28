package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.CartItem;
import model.Model;

public class CartController {
	
	private Model model;

    @FXML
    private TableView<CartItem> cartTable;
    @FXML
    private TableColumn<CartItem, String> bookTitleColumn;
    @FXML
    private TableColumn<CartItem, Integer> quantityColumn;

    public CartController(Model model) {
        this.model = model;
    }

    @FXML
    public void initialize() {
        bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        loadCartData();
    }

    private void loadCartData() {
        ObservableList<CartItem> cartItems = FXCollections.observableArrayList(model.getCartItems(model.getCurrentUser()));
        cartTable.setItems(cartItems);
    }

}
