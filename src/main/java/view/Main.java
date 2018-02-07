package main.java.view;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Main extends Application {
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/main/java/view/view.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/main/resources/application.css").toExternalForm());
			primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/main/resources/Icons/appIcon.png")));
			primaryStage.setScene(scene);
			primaryStage.setTitle("Netzwerk Monitor");
			primaryStage.setMinWidth(600.00);
			primaryStage.setMinHeight(500.00);
			primaryStage.setAlwaysOnTop(true);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
}
