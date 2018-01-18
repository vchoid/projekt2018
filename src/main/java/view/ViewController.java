package main.java.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import main.java.model.JSONFileHandler;

public class ViewController implements Initializable {

	@FXML
	private TableView<String> portTable;
	@FXML
	private TableView<String> serverTable;
	@FXML
	private TableColumn<String, String> serverName = new TableColumn<>("Server");

	ArrayList<String> serverArr = new ArrayList<>();
	ObservableList<?> list = FXCollections.observableArrayList();
	
	private JSONFileHandler jfh = new JSONFileHandler();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		addPortsAsColoumn();
		addServerAsRow();
	}

	private void addPortsAsColoumn() {
		for (int i = 0; i < jfh.getPortNameList().size(); i++) {
			TableColumn<String, String> col = new TableColumn<String, String>(
					jfh.getPortNameList().get(i));
			portTable.getColumns().add(col);
		}
	}

	private void addServerAsRow() {
		for (int i = 0; i < jfh.getServerNameList().size(); i++) {
			serverArr.add(jfh.getServerNameList().get(i));
		}
		System.out.println(serverArr);
	}

	
}
