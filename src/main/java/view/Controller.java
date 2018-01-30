package main.java.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import main.java.model.NetworkConnection;

public class Controller implements Initializable {

	// #########################################################################
	// ## Variablen ############################################################
	// #########################################################################
	// --> Port/Server <--------------------------------------------------------
	@FXML
	private TableView<String> portServerTable;

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
		buildData();
	}
	// #########################################################################
	// ## Daten verarbeiten ####################################################
	// #########################################################################

	public void buildData() {
		
	}
	
	
	
}

