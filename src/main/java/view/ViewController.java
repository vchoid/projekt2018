package main.java.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.table.TableCellEditor;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.model.JSONFileHandler;

public class ViewController implements Initializable {

	@FXML
	private TableView<String> portTable;
	@FXML
	private TableView<String> serverTable;
	@FXML
	private TableColumn serverName;

	ArrayList<TableObject> serverArr = new ArrayList<TableObject>();

	private String filePath = System.getProperty("user.dir")
			+ "/src/main/resources/Server_Ports.JSON";
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
			TableObject entry = new TableObject(jfh.getServerNameList().get(i));
			serverArr.add(entry);
		}
		serverTable.set(
				FXCollections.observableArrayList().add(serverArr));
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
