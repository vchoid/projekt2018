package main.java.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
	// --> Message Output <-----------------------------------------------------
	@FXML
	private Label messageOutput;
	// --> Button <-------------------------------------------------------------
	@FXML
	private Button startButton;
	@FXML
	private Button stopButton;
	// --> Thread <-------------------------------------------------------------
	private Service<Object> startStopButtonService;
	private Service<Object> progressService;
	private ScheduledService<Object> messageOutputService;
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
		nc.saveConnectionRequest();
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
		showMessageOutput();
		showStartStopButton();
		showProgress();
	}
	/**
	 * Verändert die Sichtbarkeit der Start/Stop-Buttons.
	 */
	private void showStartStopButton() {
		startStopButtonService = new Service<Object>() {
			@Override
			protected Task<Object> createTask() {
				return new Task<>() {
					@Override
					protected Object call() throws Exception {
						// während der Abfrage
						while (nc.isRunning()) {
							setStartButton(false);
						}
						// nach Beendigung der Abfrage
						setStartButton(true);
						return null;
					}
				};
			}
		};
		startStopButtonService.start();
	}
	/**
	 * Fragt ständig den Fortschritt ab und gibt ihn als Bar und Zahl aus.
	 */
	private void showProgress() {
		progressService = new Service<Object>() {
			@Override
			protected Task<Object> createTask() {
				return new Task<>() {
					@Override
					protected Object call() throws Exception {
						// während der Abfrage
						while (nc.isRunning()) {
							pgBar.setVisible(true);
							pgBar.setProgress(nc.getProgressIndicator());
							progressInd.setVisible(true);
							progressInd.setProgress(nc.getProgressIndicator());
						}
						// nach Beendigung der Abfrage
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
		progressService.start();
	}
	/**
	 * Fragt ständig den Fortschritt ab und gibt ihn als Bar und Zahl aus.
	 */
	private void showMessageOutput() {
		messageOutputService = new ScheduledService<Object>() {
			@Override
			protected Task<Object> createTask() {
				return new Task<>() {
					@Override
					protected Object call() throws Exception {
						// während der Abfrage
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									messageOutput.textProperty()
											.set(nc.getMessageOutput());
								}
							});
						return null;
					}
				};
			}
		};
		messageOutputService.start();
	}

}
