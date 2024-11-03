package controller;

import java.io.IOException;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Model;
import model.User;

public class LoginController {
	@FXML
	private TextField name;
	@FXML
	private PasswordField password;
	@FXML
	private Label message;
	@FXML
	private Button login;
	@FXML
	private Button signup;

	private Model model;
	private Stage stage;

	public LoginController(Stage stage, Model model) {
		this.stage = stage;
		this.model = model;
	}

	@FXML
	public void initialize() {		
		login.setOnAction(event -> {
			if (!name.getText().isEmpty() && !password.getText().isEmpty()) {
				User user;
				try {
					user = model.getUserDao().getUser(name.getText(), password.getText());
					if (user != null) {
						model.setCurrentUser(user);
						try {
							FXMLLoader loader;
							if (user.isAdmin()) {
								// Redirect to Admin Dashboard if user is an admin
								loader = new FXMLLoader(getClass().getResource("/view/AdminDashboard.fxml"));
								AdminController adminController = new AdminController(stage, model);
								loader.setController(adminController);
							} else {
								// Redirect to HomeView for regular user
								loader = new FXMLLoader(getClass().getResource("/view/HomeView.fxml"));
								HomeController homeController = new HomeController(stage, model);
								loader.setController(homeController);
							}
							AnchorPane root = loader.load();
							Scene scene = new Scene(root);
							stage.setScene(scene);
							stage.setTitle(user.isAdmin() ? "Admin Dashboard" : "Home");
							stage.show();
						} catch (IOException e) {
							message.setText("Error loading view: " + e.getMessage());
							message.setTextFill(Color.RED);
						}
					} else {
						message.setText("Wrong username or password");
						message.setTextFill(Color.RED);
					}
				} catch (SQLException e) {
					message.setText("Database error: " + e.getMessage());
					message.setTextFill(Color.RED);
				}
			} else {
				message.setText("Empty username or password");
				message.setTextFill(Color.RED);
			}
			name.clear();
			password.clear();
		});
		
		signup.setOnAction(event -> {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SignupView.fxml"));
				SignupController signupController = new SignupController(stage, model);
				loader.setController(signupController);
				VBox root = loader.load();
				signupController.showStage(root);
				message.setText("");
				name.clear();
				password.clear();
				stage.close();
			} catch (IOException e) {
				message.setText("Error loading signup view: " + e.getMessage());
				message.setTextFill(Color.RED);
			}
		});
	}

	public void showStage(Pane root) {
		Scene scene = new Scene(root, 500, 300);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle("Welcome");
		stage.show();
	}
}
