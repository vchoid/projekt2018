package main.java.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
	private Button fastForwardButton;
	// --> Ausgabe <------------------------------------------------------------
	@FXML
	private Label readOutLabel;
	@FXML
	private Label serverReadOutLabel;
	@FXML
	private Label portReadOutLabel;
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
	 * Verbindungsanfrage starten. Den Progress-Indicator auf sichtbar machen.
	 * Den Stop-Wert auf false setzen, der aussagt, dass der Prozess noch nicht
	 * gestoppt wurde.
	 */
	@FXML
	public void startBuild() {
		nc.startConnectionRequest();
		nc.setStoped(false);
		setStartButton(false);
	}
	// #########################################################################
	// ## Steuerelemente #######################################################
	// #########################################################################
	/**
	 * Deaktiviert alle Skip-Button und den FastForward-Button, wenn der Parameter auf true ist.
	 * 
	 * @param val
	 */
	public void setSkipButtonDisable(boolean val) {
		skipServerButton.setDisable(val);
		skipPortButton.setDisable(val);
	}
	/**
	 * Setzt Start-Button bei true auf Sichtbar und der Stop-Button
	 * verschwindet.
	 * 
	 * @param val
	 */
	public void setStartButton(boolean val) {
		startButton.setVisible(val);
		stopButton.setVisible(!val);
		fastForwardButton.setDisable(val);
	}
	/**
	 * Schnellvorlauf für die Serveranfragen. Deaktiviert die Skip-Button
	 * Deaktiviert den Start-Button
	 */
	public void fastForwardQuery() {
		if (!fastForwardButton.isPressed()) {
			fastForwardButton.setDisable(true);
			nc.setAusgabeText(">>");
			nc.setThreadTime(0);
			
		}
	}
	/**
	 * Nur wenn die Serveranfrage geschlossen ist soll einer Versuche gestartet
	 * werden.
	 */
	@FXML
	public void restartBuild() {
		if (nc.isRunning() == false) {
			fastForwardButton.setDisable(false);
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
		nc.setAusgabeText(nc.getPortName());
		nc.setSkipPort(true);
	}
	/**
	 * Überspringt die Abfrage eines Servers und den dazu gehörigen Ports.
	 */
	@FXML
	public void skipServer() {
		nc.setAusgabeText(nc.getServerName());
		nc.setSkipServer(true);
	}

	// #########################################################################
	// ## Fortschrittsanzeige ##################################################
	// #########################################################################
	/**
	 * Gibt den Status der Verarbeitung aus.
	 */
	private void setProgressStatus() {
		// ständige Abfrage des Fortschritts
		sc = new ScheduledService<>() {
			@Override
			protected Task<ProgressBar> createTask() {
				// dieser Thread läuft etwas später, damit sie nicht kollidieren und abstürzen
				Platform.runLater(new Runnable(){
					@Override
					public void run() {
						readOutLabel.textProperty().set(nc.getAusgabeText());
						serverReadOutLabel.textProperty().set(nc.getServerName());
						portReadOutLabel.textProperty().set(nc.getPortName());
					}
				});
				return new Task<ProgressBar>() {
					@Override
					protected ProgressBar call() {
						// wenn Anfrage läuft
						if(nc.isRunning()) {
							skipServerButton.setDisable(!nc.isServerSkippable());
							skipPortButton.setDisable(!nc.isPortSkippable());
							progressInd.setProgress(0);
							setVisibleOfProgressAndReadOut(true);
						}
						pbBar.setProgress(nc.getProgressIndicator());
						progressInd.setProgress(nc.getProgressIndicator());
						// wenn Anfrage nicht mehr läuft
						while (nc.isRunning() == false) {
							setSkipButtonDisable(false);
							setStartButton(true);
							nc.setDefaultProgressInfo();
							progressInd.setProgress(1);
							try {
								Thread.sleep(1 * 100);
								Platform.runLater(new Runnable(){
									@Override
									public void run() {
										setVisibleOfProgressAndReadOut(false);
									}
								});
							} catch (InterruptedException e) {
								System.out.println("Fehler im ScheduleService");
							}
						}
						return null;
					}
				};
			}
		};
		sc.start();
	}

	private void setVisibleOfProgressAndReadOut(boolean visible) {
		progressInd.setVisible(visible);
		pbBar.setVisible(visible);
		readOutLabel.setVisible(visible);
		serverReadOutLabel.setVisible(visible);
		portReadOutLabel.setVisible(visible);
	}
	
}
