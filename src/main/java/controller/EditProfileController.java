package controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Model;
import model.User;

public class EditProfileController {
	private Model model;
    private Stage dialogStage;

    @FXML
    private TextField usernameField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private PasswordField passwordField;

    public EditProfileController(Model model) {
        this.model = model;
    }

    @FXML
    private void initialize() {
        // Load current user data into fields
        User currentUser = model.getCurrentUser();
        usernameField.setText(currentUser.getUsername());
        firstNameField.setText(currentUser.getFirstName());
        lastNameField.setText(currentUser.getLastName());
        passwordField.setText(currentUser.getPassword());
    }

    @FXML
    private void handleSave() {
        // Update the user's profile with new data
        User currentUser = model.getCurrentUser();
        currentUser.setFirstName(firstNameField.getText());
        currentUser.setLastName(lastNameField.getText());
        currentUser.setPassword(passwordField.getText());

        // Save changes and close the dialog
        model.updateUser(currentUser);
        dialogStage.close();
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

}
