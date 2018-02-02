package main.java.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
	// --> Button <-------------------------------------------------------------
	@FXML
	private Button startButton;
	@FXML
	private Button stopButton;
	@FXML
	private Button skipServerButton;
	@FXML
	private Button skipPortButton;
	@FXML
	private CheckBox disCheckBox;

	// #########################################################################
	// ## initialize-Methode ###################################################
	// #########################################################################
	/**
	 * Hier werden alle Methoden aufgeführt, die direkt nach dem Laden der
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
		nc.setStoped(false);
	}
	// #########################################################################
	// ## Steuerelemente ######################################################
	// #########################################################################
	public void disableAllButtons() {
		if (disCheckBox.isSelected()) {
			nc.setThreadTime(0);
			startButton.setDisable(true);
			stopButton.setDisable(true);
			skipServerButton.setDisable(true);
			skipPortButton.setDisable(true);
		} else if (!disCheckBox.isSelected()) {
			nc.setThreadTime(2*1000);
			startButton.setDisable(false);
			stopButton.setDisable(false);
			skipServerButton.setDisable(false);
			skipPortButton.setDisable(false);
		}
	}
	/**
	 * Nur wenn die Serverabfrage geschlossen ist soll einer Versuche gestartet
	 * werden.
	 */
	@FXML
	public void restartBuild() {
		if (nc.isRunning() == false) {
			startBuild();
		}
	}
	/**
	 * Den Prozess stoppen in dem der Stop-Wert auf true setzen. Den
	 * Progress-Indicator unsichtbar machen.
	 */
	@FXML
	public void stopBuild() {
		nc.setStoped(true);
	}
	/**
	 * Überspringt die Abfrage eines Ports
	 */
	@FXML
	public void skipPort() {
		nc.setSkipPort(true);
	}
	/**
	 * Überspringt die Abfrage eines Servers und den dazu gehörigen Ports.
	 */
	@FXML
	public void skipServer() {
		nc.setSkipServer(true);
	}

	// #########################################################################
	// ## Fortschrittsanzeige ##################################################
	// #########################################################################
	/**
	 * Gibt den Status der Verarbeitung aus.
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
						if (nc.isRunning()) {
							progressInd.setProgress(0);
							pbBar.setVisible(true);
							progressInd.setVisible(true);
						}
						pbBar.setProgress(nc.getProgressIndicator());
						progressInd.setProgress(nc.getProgressIndicator());
						while (nc.isRunning() == false) {
							progressInd.setProgress(1);
							Thread.sleep(1 * 200);
							progressInd.setVisible(false);
							pbBar.setVisible(false);
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
