package main.java.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import main.java.model.NetworkConnection;

public class ViewController implements Initializable {

	// ## Variablen ############################################################
	// --> Port/Server <--------------------------------------------------------
	@FXML
	private TableView<ServerPortConnection> portServerTable;
	@FXML
	private TableColumn<ServerPortConnection, String> server;
	@FXML
	private TableColumn<ServerPortConnection, String> portA;
	@FXML
	private TableColumn<ServerPortConnection, String> portB;
	
	;
	// --> NetworkTable
	// <-------------------------------------------------------------
	NetworkConnection nc = new NetworkConnection();

	// ## init Methode #########################################################
	/**
	 * Hier werden alle Methoden aufgeführt, die direkt nach dem Laden der
	 * Anwendung gestartet werden sollen.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setColumn();
		portServerTable.setEditable(true);
		portServerTable.setItems(getConnection());
		portServerTable.getColumns().addAll(server, portA, portB);
	}
	/**
	 * Starte den Ping per Knopfdruck.
	 * 
	 * @param e
	 */
	public void startPing(ActionEvent e) {
		// nc.testServerPortConnection();
	}

	
	private void setColumn() {
		server  = new TableColumn<>();
		server.setId("server");
		server.setCellFactory(TextFieldTableCell.forTableColumn());
		server.setCellValueFactory(new PropertyValueFactory<>("server"));
		portA = new TableColumn<>("Port A");
		portA.setCellValueFactory(new PropertyValueFactory<>("portA"));
		portB = new TableColumn<>("Port B");
		portB.setCellValueFactory(new PropertyValueFactory<>("portB"));
	}

	private ObservableList<ServerPortConnection> getConnection() {
		ObservableList<ServerPortConnection> con = FXCollections.observableArrayList();
		con.add(new ServerPortConnection("Stage", "true", "true"));
		con.add(new ServerPortConnection("Production", "false", "true"));
		con.add(new ServerPortConnection("FirstSpirit", "false", "true"));
		con.add(new ServerPortConnection("SE", "false", "false"));

		return con;
	}

	// |||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
	// |||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
	// ## Server-Port Tabelle ##################################################
	/**
	 * Holt die Name der Ports aus der Liste und legt für jeden Namen eine
	 * Spalte an und fügt sie der Tabelle Ports hinzu.
	 */
	// private void addPortsColumn() {
	// TableColumn<String, String> col = null;
	// server = new TableColumn("Server");
	// portServerTable.getColumns().add(server);
	//
	// for (int i = 0; i < nc.getServerNameList().size(); i++) {
	// col = new TableColumn<String, String>(nc.getPortList().get(i));
	//
	// portServerTable.getColumns().add(col);
	// }
	// }

	// /**
	// * Fügt der Server Tabelle in der Spalte Server die Namen der der
	// * ObserbableList hinzu.
	// */
	// private void addServerValues() {
	// ObservableList<String> serverList =
	// FXCollections.observableArrayList(nc.getServerNameList());
	// server.setCellValueFactory(new Callback<CellDataFeatures<String, String>,
	// ObservableValue<String>>() {
	// public ObservableValue<String> call(CellDataFeatures<String, String> p) {
	// return new SimpleStringProperty(p.getValue());
	// }
	// });
	// portServerTable.setItems(serverList);
	// }

	// // TODO Werte in die Spalten füllen. ConnectionArray aufspalten für jeden
	// Port
	// private void addPortValues() {
	// System.out.println(nc.getConnectArray().size());
	// System.out.println(nc.getPortList().size());
	// for (int i = 0; i < nc.getConnectArray().size(); i++) {
	//
	// }
	// }

}
