package main.java.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import main.java.utils.TableHandler;

public class ViewController implements Initializable{

	@FXML private TableView tab1;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		TableHandler port1 = new TableHandler(tab1, "SE");
		TableHandler port1 = new TableHandler(tab1, "SE");
	}
	
	
	
}
