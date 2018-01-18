package test.java;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Test extends Application {
	public static void main(String[] args) throws Exception {
		launch(args);
	}

	@Override
	public void start(Stage primarystage) throws Exception {
		// Create layout
		VBox root = new VBox();

		TableView<TableObject> table = new TableView<TableObject>();

		TableColumn<TableObject, String> col1 = new TableColumn<TableObject, String>(
				"Column 1");
		
		table.getColumns().add(col1);

		col1.setCellValueFactory(
				new PropertyValueFactory<TableObject, String>("column1"));
	
//		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		root.getChildren().add(table);

		ScrollPane scrollpane = new ScrollPane();
		scrollpane.setFitToWidth(true);
		scrollpane.setFitToHeight(true);
		scrollpane.setPrefSize(500, 200);
		scrollpane.setContent(root);

		// Create and show scene
		Scene scene = new Scene(scrollpane);
		primarystage.setScene(scene);
		primarystage.show();

		// Populate table
		ArrayList<TableObject> data = new ArrayList<TableObject>();
		for (int i = 0; i < 20;) {
			TableObject entry = new TableObject(String.valueOf(i++));
			data.add(entry);
		}

		table.setItems(FXCollections.observableArrayList(data));
	}

	public class TableObject {
		private StringProperty column1;

		public TableObject(String col1) {
			column1 = new SimpleStringProperty(col1);
			
		}

		public StringProperty column1Property() {
			return column1;
		}

		
	}
}