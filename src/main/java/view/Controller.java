package main.java.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Duration;
import main.java.model.NetworkConnection;

public class Controller implements Initializable {

	// #########################################################################
	// ## Variablen ############################################################
	// #########################################################################
	// --> Port/Server <--------------------------------------------------------
	@FXML
	private TableView<String> portServerTable;
	// --> Buuton <-------------------------------------------------------------
	@FXML
	private Button startBut;
	// --> Progress <-----------------------------------------------------------
	@FXML
	private ProgressBar pbBar;
	private boolean threadFinish = false;
	private Service<NetworkConnection> s;
	private ScheduledService<ProgressBar> sc;
	private NetworkConnection nc = new NetworkConnection();
	// --> NetworkTable <-------------------------------------------------------

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
		// ständige Abfrage des Fortschritts
		sc = new ScheduledService<>() {

			@Override
			protected Task<ProgressBar> createTask() {
				// TODO Auto-generated method stub
				return new Task<ProgressBar>() {

					@Override
					protected ProgressBar call() throws Exception {
						pbBar.setProgress(nc.getProgressIndicator());
						return null;
					}
				};
			}
		};
		sc.start();
	}
	// #########################################################################
	// ## Daten verarbeiten ####################################################
	// #########################################################################

	@FXML
	public void buildData() {
		nc.startConnectionRequest();
	}

	// @FXML
	// public void buildData() {
	// pbBar.setVisible(true);
	// // Thread damit die Anwendung gleich zu sehene ist
	// s = new Service<NetworkConnection>() {
	// @Override
	// protected Task<NetworkConnection> createTask() {
	// return new Task<NetworkConnection>() {
	// @Override
	// protected NetworkConnection call() throws Exception {
	// // ständige Abfrage des Fortschritts
	// sc = new ScheduledService<>() {
	//
	// @Override
	// protected Task<Double> createTask() {
	// // TODO Auto-generated method stub
	// return new Task<Double>() {
	//
	// @Override
	// protected Double call() throws Exception {
	// pbBar.setProgress(nc.getProgressIndicator());
	// return null;
	// }
	// };
	// }
	// };
	// sc.start();
	// nc.startConnectionRequest();
	// pbBar.setVisible(false);
	// return null;
	// }
	// };
	// }
	// };
	// s.start();
	// }

	// #########################################################################
	// ## Getter und Setter ####################################################
	// #########################################################################

	public boolean isThreadFinish() {
		return threadFinish;
	}
	public void setThreadFinish(boolean threadFinish) {
		this.threadFinish = threadFinish;
	}

}
