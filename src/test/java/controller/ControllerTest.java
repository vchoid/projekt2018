package test.java.controller;

import java.net.URL;
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

public class ControllerTest implements Initializable {

	// ## Variablen ############################################################
	// --> Port/Server <--------------------------------------------------------
	@FXML
	private TableView portServerTable;

	// --> Liste <--------------------------------------------------------------
	private ObservableList<ObservableList> data;

	// --> Array <--------------------------------------------------------------
	int[] portArray = {1234, 5678, 987};
	// Anzahl Reihen
	String[] serverArray = {"Prod", "Stage", "Auslieferung"};
	// Anzahl Spalten
	String[] connArray = {"true", "false", "false"};
	String[] connArray1 = {"true", "true", "true"};
	String[] connArray2 = {"false", "false", "false"};

	// ## Initialisieren #######################################################
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		buildData();
	}
	public void buildData() {

		data = FXCollections.observableArrayList();
		try {

			/**********************************
			 * TABLE COLUMN ADDED DYNAMICALLY *
			 **********************************/
			for (int i = 0; i < portArray.length; i++) {
				
				final int j = i;
				TableColumn col = new TableColumn("" + portArray[i]);
				// hier werden die Werte den Spalten zugeordnet
				col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
						return new SimpleStringProperty(param.getValue().get(j).toString());
					}
				});

				portServerTable.getColumns().addAll(col);
				System.out.println("Column [" + i + "] ");
			}

			/********************************
			 * Data added to ObservableList *
			 ********************************/
			for (int k = 0; k < serverArray.length; k++) {
				// Iterate Row
				ObservableList<String> row = FXCollections
						.observableArrayList();
				for (int i = 0; i < connArray.length; i++) {
					// Iterate Column
					row.add(connArray[i]);
				}
				System.out.println("Row [1] added " + row);
				data.add(row);

			}

			// FINALLY ADDED TO TableView
			portServerTable.setItems(data);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error on Building Data");
		}
	}

}
