package main.java.view;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ViewController implements Initializable{

	@FXML private TableView tab1;
	
	 List intValues = Arrays.asList(1, 2, 3, 4, 5);
	 
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		TableColumn id = new TableColumn("ID");
		id.setCellValueFactory(cellData -> {
            Integer rowIndex = cellData;
            return new ReadOnlyIntegerWrapper(intValues.get(rowIndex));
        });;
		TableColumn name = new TableColumn("Name");
		TableColumn age = new TableColumn("Age");
		TableColumn email = new TableColumn("Email");
		
		tab1.getColumns().addAll(id,name,age,email);
	}

	

	
	
	
	
}
