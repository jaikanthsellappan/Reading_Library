package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Order;

import java.util.List;

public class OrderController {

    @FXML
    private TableView<Order> orderTable;
    @FXML
    private TableColumn<Order, String> orderNumberColumn;
    @FXML
    private TableColumn<Order, String> dateColumn;
    @FXML
    private TableColumn<Order, Double> totalPriceColumn;
    @FXML
    private TableColumn<Order, String> bookDetailsColumn;

    private ObservableList<Order> ordersData;

    public OrderController(List<Order> orders) {
        ordersData = FXCollections.observableArrayList(orders);
    }

    @FXML
    public void initialize() {
        orderNumberColumn.setCellValueFactory(new PropertyValueFactory<>("orderNumber"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        bookDetailsColumn.setCellValueFactory(new PropertyValueFactory<>("bookDetails"));

        orderTable.setItems(ordersData);
    }
}
