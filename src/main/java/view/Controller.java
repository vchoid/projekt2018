package main.java.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import main.java.model.NetworkConnection;
import main.java.model.Server;

public class Controller implements Initializable {

	// #########################################################################
	// ## Variablen ############################################################
	// #########################################################################
	// --> Port/Server <--------------------------------------------------------
	@FXML
	private TableView<Node> portServerTable;

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

