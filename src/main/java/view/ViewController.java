package main.java.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import main.java.utils.JSONFileHandler;
import main.java.utils.JSONFileHandler;

public class ViewController implements Initializable {

	@FXML
	private TableView<String> tab1;

	private String filePath = System.getProperty("user.dir")
			+ "/src/main/resources/Server_Ports.JSON";
	private JSONFileHandler jfh = new JSONFileHandler();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		addPortsAsColoumn();
	}

	public void addPortsAsColoumn() {
		for (int i = 0; i < jfh.getPortsArray().size(); i++) {
			// We are using non property style for making dynamic table
			TableColumn col = new TableColumn(jfh.getPortsArray().get(i).toString());
			tab1.getColumns().addAll(col);
		}
	}
}
