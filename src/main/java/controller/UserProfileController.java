package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.User;

public class UserProfileController {
    @FXML
    private Label usernameLabel;
    @FXML
    private Label firstnameLabel;
    @FXML
    private Label lastnameLabel;
    @FXML
    private Label emailLabel;  // Assuming email is part of the User class
    
    private Stage stage;
	private User user;

    public UserProfileController(User user) {
        this.user = user;
    }

    @FXML
    public void initialize() {
        // Set user data in labels
        usernameLabel.setText(user.getUsername());
        firstnameLabel.setText(user.getFirstName());
        lastnameLabel.setText(user.getLastName());
        //emailLabel.setText(user.getEmail()); // Update accordingly
    }

    @FXML
    private void closeWindow() {
        stage.close();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
