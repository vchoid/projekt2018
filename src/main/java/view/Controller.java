package main.java.view;

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

public class Controller implements Initializable {

	// #########################################################################
	// ## Variablen ############################################################
	// #########################################################################
	// --> Port/Server <--------------------------------------------------------
	@FXML
	private TableView portServerTable;

	// --> NetworkTable <-------------------------------------------------------
	NetworkConnection nc = new NetworkConnection();

	// #########################################################################
	// ## init Methode #########################################################
	// #########################################################################
	/**
	 * Hier werden alle Methoden aufgeführt, die direkt nach dem Laden der
	 * Anwendung gestartet werden sollen.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}
	// #########################################################################
	// ## Daten verarbeiten ####################################################
	// #########################################################################

	
	
}

