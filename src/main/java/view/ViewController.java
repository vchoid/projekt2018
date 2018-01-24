package main.java.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import main.java.model.JSONFileHandler;

public class ViewController implements Initializable {

	// ## Variablen ############################################################
	// --> Port/Server <--------------------------------------------------------
	@FXML private TableView<String> portServerTable;
	@FXML private TableColumn<String, String> server;
	
	private JSONFileHandler jfh = new JSONFileHandler();
	
	

	// ## init Methode #########################################################
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		addPortsColumn();
		addServerValues();
	}
	
	// ## Server-Port Tabelle ##################################################
	/**
	 * Holt die Name der Ports aus der Liste und legt für jeden Namen eine
	 * Spalte an und fügt sie der Tabelle Ports hinzu.
	 */
	private void addPortsColumn() {
		TableColumn<String, String> col = null;
		for (int i = 0; i < jfh.getPortList().size(); i++) {
			col = new TableColumn<String, String>(jfh.getPortList().get(i));
			//TODO Einfügen der isConnected-Werte
			portServerTable.getColumns().add(col);
		}
		addPortValues();
	}
	/**
	 * Fügt der Server Tabelle in der Spalte Server die Namen der der
	 * ObserbableList hinzu.
	 */
	private void addServerValues() {
		ObservableList<String> serverList = FXCollections.observableArrayList(jfh.getServerNameList());
		server.setCellValueFactory(new Callback<CellDataFeatures<String, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<String, String> p) {
						return new SimpleStringProperty(p.getValue());
					}
				});
		portServerTable.setItems(serverList);
	}
	
	private void addPortValues() {
		System.out.println(jfh.getConnectArray().size());
		System.out.println(jfh.getPortList().size());
		for (int i = 0; i < jfh.getConnectArray().size(); i++) {
			
		}
	}
	
	
	
	
	
}
