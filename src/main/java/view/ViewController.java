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
	private ArrayList<String> serverArr = new ArrayList<>();
	private JSONFileHandler jfh = new JSONFileHandler();
	
	// --> Detail <-------------------------------------------------------------
	@FXML private TableView<String> detailTable;
	@FXML private TableColumn<String, String> hostDetail;
	@FXML private TableColumn<String, String> ipDetail;
	@FXML private TableColumn<String, String> localAdressDetail;
	@FXML private TableColumn<String, String> connectedDetail; 

	// ## init Methode #########################################################
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		addPorts();
		addServer();
	}
	
	// ## Server-Port Tabelle ##################################################
	/**
	 * Holt die Name der Ports aus der Liste und legt für jeden Namen eine
	 * Spalte an und fügt sie der Tabelle Ports hinzu.
	 */
	private void addPorts() {
		for (int i = 0; i < jfh.getPortNameList().size(); i++) {
			TableColumn<String, String> col = new TableColumn<String, String>(
					jfh.getPortNameList().get(i));
			portServerTable.getColumns().add(col);
		}
	}
	/**
	 * Fügt der Server Tabelle in der Spalte Server die Namen der der
	 * ObserbableList hinzu.
	 */
	private void addServer() {
		server.setCellValueFactory(
				new Callback<CellDataFeatures<String, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(
							CellDataFeatures<String, String> p) {
						return new SimpleStringProperty(p.getValue());
					}
				});
		portServerTable.setItems(createList());
	}
	/**
	 * Holt die Namen der Server aus der Liste und speichert jeden Namen in das
	 * ServerArray und gibt das Array als eine {@link ObservableList}
	 * zurück.
	 * 
	 * @return
	 */
	private ObservableList<String> createList() {
		for (int i = 0; i < jfh.getServerNameList().size(); i++) {
			serverArr.add(jfh.getServerNameList().get(i));
		}
		return FXCollections.observableArrayList(serverArr);
	}
	// ## Detail Tabelle ##########################################################
	
}
