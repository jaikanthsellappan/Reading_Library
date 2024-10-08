package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TitledPane;

public class RegisterController {

    @FXML
    private Hyperlink loginLink;

    // Handle when the "Existing user login here" hyperlink is clicked
    @FXML
    private void handleLoginClick() {
        try {
            // Load the login FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            TitledPane loginPane = loader.load();

            // Create a new scene with the login page
            Scene scene = new Scene(loginPane);

            // Get the current stage (window)
            Stage stage = (Stage) loginLink.getScene().getWindow();

            // Set the new scene to the current stage
            stage.setScene(scene);
            stage.setTitle("Login Page");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
