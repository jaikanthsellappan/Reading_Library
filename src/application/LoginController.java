package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TitledPane;

public class LoginController {

    @FXML
    private Hyperlink registerLink;

    @FXML 	//If the user is clicking the register icon, then he is directed to register page
    private void handleRegisterClick() {
        try {
            // Load the register.fxml when the "Register" hyperlink is clicked
            FXMLLoader loader = new FXMLLoader(getClass().getResource("register.fxml"));
            TitledPane registerPane = loader.load();

            // Set the scene to the register page
            Scene scene = new Scene(registerPane);
            Stage stage = (Stage) registerLink.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Register Page");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML		////If the user is clicking the login icon, then he is directed to register page
    private void handleLoginClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("shopping.fxml"));
            TitledPane shoppingPane = loader.load();
            Scene scene = new Scene(shoppingPane);
            Stage stage = (Stage) registerLink.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Shopping Page");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
