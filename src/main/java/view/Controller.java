package main.java.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableView;
import main.java.model.NetworkConnection;

public class Controller implements Initializable {

	// #########################################################################
	// ## Variablen ############################################################
	// #########################################################################
	// --> Data <--------------------------------------------------------------
	private NetworkConnection nc = new NetworkConnection();
	// --> Table <--------------------------------------------------------------
	@FXML
	private TableView<String> portServerTable;
	// --> Progress <-----------------------------------------------------------
	@FXML
	private ProgressBar pgBar;
	@FXML
	private ProgressIndicator progressInd;
	// --> Button <-------------------------------------------------------------
	@FXML
	private Button startButton;
	@FXML
	private Button stopButton;
	// --> Thread <-------------------------------------------------------------
	private Service<Object> startStopButtonVisibilitySService;
	private Service<Object> statusSService;
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
		startServices();
	}
	// #########################################################################
	// ## Steuerelemente #######################################################
	// #########################################################################
	/**
	 * Setzt Start-Button bei true auf Sichtbar und der Stop-Button
	 * verschwindet.
	 * 
	 * @param val
	 */
	public void setStartButton(boolean val) {
		startButton.setVisible(val);
		stopButton.setVisible(!val);
	}
	/**
	 * Nur wenn die Serveranfrage geschlossen ist soll einer Versuche gestartet
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
	// #########################################################################
	// ## Threads ##############################################################
	// #########################################################################
	/**
	 * Startet die Service-Threads.
	 */
	private void startServices() {
		showStartStopButton();
		showProgress();
	}
	/**
	 * Verändert die Sichtbarkeit der Start/Stop-Buttons.
	 */
	private void showStartStopButton() {
		startStopButtonVisibilitySService = new Service<Object>() {
			@Override
			protected Task<Object> createTask() {
				return new Task<>() {
					@Override
					protected Object call() throws Exception {
						while(nc.isRunning()) {
							setStartButton(false);
						}
						setStartButton(true);
						return null;
					}
				};
			}
		};
		startStopButtonVisibilitySService.start();
	}
	/**
	 * Fragt ständig den Fortschritt ab und gibt ihn als Bar und Zahl aus.
	 */
	private void showProgress() {
		statusSService = new Service<Object>() {
			@Override
			protected Task<Object> createTask() {
				return new Task<>() {
					@Override
					protected Object call() throws Exception {
						while(nc.isRunning()) {
							pgBar.setVisible(true);
							pgBar.setProgress(nc.getProgressIndicator());
							progressInd.setVisible(true);
							progressInd.setProgress(nc.getProgressIndicator());
						}
						pgBar.setProgress(1);
						progressInd.setProgress(1);
							Thread.sleep(100);
							pgBar.setVisible(false);
							progressInd.setVisible(false);
						return null;
					}
				};
			}
		};
		statusSService.start();
	}
	
}
