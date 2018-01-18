package main.java.view;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("view.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Netzwerk Monitor");
			primaryStage.setMinWidth(600.00);
			primaryStage.setMinHeight(600.00);
			primaryStage.show();
		} catch(Exception e) {
			
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
}
