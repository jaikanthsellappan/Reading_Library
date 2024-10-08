package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
//			BorderPane root = new BorderPane();
//			Scene scene = new Scene(root,400,400);
//			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
//			primaryStage.setScene(scene);
			
			
//			FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
//			TitledPane root = loader.load();			
//			Scene scene = new Scene(root);
//			TextField text1 = new TextField();
//			TextField text2 = new TextField();
//			Label label1 = new Label();
//			Label label2 = new Label();
//			Button resetButton = new Button("button");
//			Button submitButton = new Button("button");
////			Scene scene1 = new Scene(root);
//			primaryStage.setScene(scene);
//			primaryStage.show();
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            TitledPane root = loader.load();
            
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Login Page");
            primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
