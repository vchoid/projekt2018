package main.java.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableView;
import javafx.util.Duration;
import main.java.model.NetworkConnection;

public class Controller implements Initializable {

	// #########################################################################
	// ## Variablen ############################################################
	// #########################################################################
	// --> NetworkTable <-------------------------------------------------------
	@FXML
	private TableView<String> portServerTable;
	private NetworkConnection nc = new NetworkConnection();
	// --> Progress <-----------------------------------------------------------
	@FXML
	private ProgressBar pbBar;
	@FXML
	private ProgressIndicator progressInd;
	private ScheduledService<ProgressBar> sc;

	// #########################################################################
	// ## initialize-Methode ###################################################
	// #########################################################################
	/**
	 * Hier werden alle Methoden aufgef�hrt, die direkt nach dem Laden der
	 * Anwendung gestartet werden sollen.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		startBuild();
		setProgressStatus();

	}
	// #########################################################################
	// ## Daten verarbeiten ####################################################
	// #########################################################################

	/**
	 * Verbindungsabfrage starten. Den Progress-Indicator auf sichtbar machen.
	 * Den Stop-Wert auf false setzen, der aussagt, dass der Prozess noch nicht
	 * gestoppt wurde.
	 */
	@FXML
	public void startBuild() {
		nc.startConnectionRequest();
		progressInd.setVisible(true);
		nc.setStopNC(false);
	}
	/**
	 * Nur wenn die Serverabfrage geschlossen ist soll einer Versuche gestartet
	 * werden.
	 */
	@FXML
	public void restartBuild() {
		if (nc.getSocket().isClosed() == true) {
			startBuild();
		}
	}
	/**
	 * Den Prozess stoppen in dem der Stop-Wert auf true setzen. Den
	 * Progress-Indicator unsichtbar machen.
	 */
	@FXML
	public void stopBuild() {
		nc.setStopNC(true);
		progressInd.setVisible(false);
	}

	// #########################################################################
	// ## Fortschrittsanzeige ##################################################
	// #########################################################################
	/**
	 * Gibt den Status der Verarbeitung an.
	 */
	private void setProgressStatus() {
		// st�ndige Abfrage des Fortschritts
		sc = new ScheduledService<>() {
			@Override
			protected Task<ProgressBar> createTask() {
				// TODO Auto-generated method stub
				return new Task<ProgressBar>() {
					@Override
					protected ProgressBar call() throws Exception {
						pbBar.setProgress(nc.getProgressIndicator());
						progressInd.setProgress(nc.getProgressIndicator());
						if (nc.getProgressIndicator() >= 0.935) {
							progressInd.setVisible(false);
						}
						return null;
					}
				};
			}
		};
		sc.start();
		sc.setPeriod(Duration.seconds(0.25));
	}

}
