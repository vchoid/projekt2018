package test.java.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import main.java.model.NetworkConnection;
import main.java.model.ServerPortConnection;

public class ControllerTest2 implements Initializable {

	// ## Variablen ############################################################
	// --> Port/Server <--------------------------------------------------------
	@FXML
	private TableView<ServerPortConnection> portServerTable;
	@FXML
	private TableColumn<ServerPortConnection, String> serverTab;
	@FXML
	private TableColumn<ServerPortConnection, String> portTab;

	// --> NetworkTable <-------------------------------------------------------
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
		portServerTable.setItems(getWerte());
		portServerTable.getColumns().addAll(serverTab, portTab);
	}

	/**
	 * Starte den Ping per Knopfdruck.
	 * 
	 * @param e
	 */
	public void startPing(ActionEvent e) {
//		 nc.testAllServerPortConnection();
	}

	private void setColumn() {
		serverTab = new TableColumn<>();
		serverTab.setId("server");
		serverTab.setCellFactory(TextFieldTableCell.forTableColumn());
		// server = Variable aus der ServerPortCooncetion-Klasse
		serverTab.setCellValueFactory(new PropertyValueFactory<>("server"));
		
		portTab = new TableColumn<>("Tomcat");
		portTab.setCellValueFactory(new PropertyValueFactory<>("port"));
		
	}

	private ObservableList<ServerPortConnection> getWerte() {
		boolean[] bool = {true, false, false};
		
		ObservableList<ServerPortConnection> con = FXCollections
				.observableArrayList();
		con.add(new ServerPortConnection("Stage", bool));
//		con.add(new ServerPortConnection("Production", false, true));
//		con.add(new ServerPortConnection("FirstSpirit", false, true));
//		con.add(new ServerPortConnection("SE", false, false));

		return con;
	}

	

	 

	 

}
