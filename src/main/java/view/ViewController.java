package main.java.view;


import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import main.java.model.JSONFileHandler;
import main.java.model.NetworkConnection;

public class ViewController implements Initializable {

	// ## Variablen ############################################################
	// --> Port/Server <--------------------------------------------------------
	@FXML private TableView<String> portServerTable;
	@FXML private TableColumn<String, String> server;
	
	// --> Thread <-------------------------------------------------------------
	NetworkConnection nc = new NetworkConnection();
	

	// ## init Methode #########################################################
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		addServerValues();
		addPortsColumn();
	}
	
	
	
	public void startPing(ActionEvent e) {
//		jfh.testServerPortConnection();
	}
	
	// ## Server-Port Tabelle ##################################################
	/**
	 * Holt die Name der Ports aus der Liste und legt für jeden Namen eine
	 * Spalte an und fügt sie der Tabelle Ports hinzu.
	 */
	private void addPortsColumn() {
		TableColumn<String, String> col = null;
		for (int i = 0; i < nc.getPortList().size(); i++) {
			col = new TableColumn<String, String>(nc.getPortList().get(i));
			//TODO Einfügen der isConnected-Werte
			portServerTable.getColumns().add(col);
//			//TODO -> umarbeiten
//			ObservableList<String> serverList = FXCollections.observableArrayList(jfh.getConnectArray().get(i));
//			col.setCellValueFactory(new Callback<CellDataFeatures<String, String>, ObservableValue<String>>() {
//				public ObservableValue<String> call(CellDataFeatures<String, String> p) {
//					return new SimpleStringProperty(p.getValue());
//				}
//			});
//			portServerTable.setItems(serverList);
		}
	}
	/**
	 * Fügt der Server Tabelle in der Spalte Server die Namen der der
	 * ObserbableList hinzu.
	 */
	private void addServerValues() {
		ObservableList<String> serverList = FXCollections.observableArrayList(nc.getServerNameList());
		server.setCellValueFactory(new Callback<CellDataFeatures<String, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<String, String> p) {
				return new SimpleStringProperty(p.getValue());
			}
		});
		portServerTable.setItems(serverList);
	}
	
	
	// TODO Werte in die Spalten füllen. ConnectionArray aufspalten für jeden Port
	private void addPortValues() {
		System.out.println(nc.getConnectArray().size());
		System.out.println(nc.getPortList().size());
		for (int i = 0; i < nc.getConnectArray().size(); i++) {
			
		}
	}
	
	
	
	
	
}
