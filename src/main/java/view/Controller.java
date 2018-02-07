package main.java.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import main.java.model.JSONFileHandler;
import main.java.model.NetworkConnection;

public class Controller implements Initializable {

	// #########################################################################
	// ## Variablen ############################################################
	// #########################################################################
	// --> Data <--------------------------------------------------------------
	private NetworkConnection nc = new NetworkConnection();
	
	// --> Table <--------------------------------------------------------------
	@FXML private TableView<String> portServerTable;
	@FXML private TableColumn<String, String> server;
	private ArrayList<String> serverArr = new ArrayList<>();
	private JSONFileHandler jfh = new JSONFileHandler();

	// --> Progress Output <----------------------------------------------------
	@FXML
	private ProgressBar pgBar;
	@FXML
	private ProgressIndicator progressInd;
	@FXML
	private ImageView openPic;
	@FXML
	private ImageView closePic;
	// --> Message Output <-----------------------------------------------------
	@FXML
	private Label serverOutput;
	@FXML
	private Label portOutput;
	@FXML
	private Label serverNameOutput;
	@FXML
	private Label portNameOutput;
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
		addPorts();
		addServer();
		startBuild();

	}

	// #########################################################################
	// ## Daten verarbeiten ####################################################
	// #########################################################################
	// ## Server-Port Tabelle ##################################################
		/**
		 * Holt die Name der Ports aus der Liste und legt f�r jeden Namen eine
		 * Spalte an und f�gt sie der Tabelle Ports hinzu.
		 */
		private void addPorts() {
			for (int i = 0; i < nc.getPortNameList().size(); i++) {
				TableColumn<String, String> col = new TableColumn<String, String>(
						nc.getPortNameList().get(i));
				portServerTable.getColumns().add(col);
			}
		}
		/**
		 * F�gt der Server Tabelle in der Spalte Server die Namen der der
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
		 * zur�ck.
		 * 
		 * @return
		 */
		private ObservableList<String> createList() {
			for (int i = 0; i < nc.getServerNameList().size(); i++) {
				serverArr.add(nc.getServerNameList().get(i));
			}
			return FXCollections.observableArrayList(serverArr);
		}
	/**
	 * Verbindungsanfrage starten. Den Progress-Indicator auf sichtbar machen.
	 * Den Stop-Wert auf false setzen, der aussagt, dass der Prozess noch nicht
	 * gestoppt wurde.
	 */
	@FXML
	private void startBuild() {
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
	private void setStartButton(boolean val) {
		startButton.setVisible(val);
		stopButton.setVisible(!val);
	}
	/**
	 * Nur wenn die Serveranfrage geschlossen ist soll einer Versuche gestartet
	 * werden.
	 */
	@FXML
	private void restartBuild() {
		if (nc.isRunning() == false) {
			startBuild();
		}
	}
	/**
	 * Den Prozess stoppen in dem der Stop-Wert auf true setzen. Den
	 * Progress-Indicator unsichtbar machen.
	 */
	@FXML
	private void stopBuild() {
		nc.setStoped(true);
	}
	// #########################################################################
	// ## Fortschritt ##########################################################
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
	 * Verändert sich Sichtbarkeit der openPic und closePic Elemente.
	 * @param isOpen
	 */
	private void setOpenPic(boolean isOpen) {
		openPic.setVisible(isOpen);
		closePic.setVisible(!isOpen);
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
					protected Object call() {
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
					protected Object call() throws InterruptedException {
						// während der Abfrage
						while (nc.isRunning()) {
							pgBar.setVisible(true);
							pgBar.setProgress(nc.getProgressIndicator());
							progressInd.setVisible(true);
							progressInd.setProgress(nc.getProgressIndicator());
							setOpenPic(nc.isConnected());
						}

						// nach Beendigung der Abfrage
						Thread.sleep(50);
						openPic.setVisible(false);
						closePic.setVisible(false);
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
					protected Object call() {
						// während der Abfrage
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								serverOutput.textProperty()
										.set(nc.getServerOutput());
								portOutput.textProperty()
										.set(nc.getPortOutput());
								serverNameOutput.textProperty()
										.set(nc.getServerNameOutput());
								portNameOutput.textProperty()
										.set(nc.getPortNameOutput());
								
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
