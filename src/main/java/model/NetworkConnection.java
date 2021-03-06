package main.java.model;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class NetworkConnection {

	// ## Variablen ############################################################
	private JSONFileHandler jfh;

	// --> Listen für View -----------------------------------------------------
	private ArrayList<String> portList = new ArrayList<>();
	private ArrayList<String> portNameList = new ArrayList<>();
	private ArrayList<String> serverNameList = new ArrayList<>();
	private ArrayList<String> ipList = new ArrayList<>();
	private ArrayList<String> hostList = new ArrayList<>();
	private ArrayList<ArrayList<String>> connectArray;
	private ArrayList<String> portConnArray;
	private String serverName = "";
	private String portName = "";
	private String ip = "";
	private String host = "";
	private int portAdr = 0;

	// --> Abfrage Prozess -----------------------------------------------------
	private Socket socket;
	private boolean connected;
	private boolean stoped;
	private boolean running;

	// --> Fortschritt Ausgabe -------------------------------------------------
	private String statusOutput;
	private String serverOutput;
	private String portOutput;
	private String serverNameOutput;
	private String portNameOutput;
	private double progress;
	private double progressIndicator;
	private double progress100;
	private double progressPort;
	private double progressIndicatorPort;
	private double progress100Port;

	// --> Thread --------------------------------------------------------------
	private Service<Object> ncService;
	
	// #########################################################################
	// ## Initialisieren #######################################################
	// #########################################################################
	public NetworkConnection() {
		jfh = new JSONFileHandler();
		setPortServerValuesInAList();
	}

	// #########################################################################
	// ## Daten als für View
	// ###################################################
	// #########################################################################

	/**
	 * Holt Werte eines Arrays über den Key und speichert diese in eine neue
	 * ArrayList.
	 * 
	 * @param array
	 * @param key
	 * @param list
	 */
	private void saveValuesInArray(JsonArray array, String key,
			ArrayList<String> list) {
		for (int i = 0; i < array.size(); i++) {
			JsonObject temp = array.get(i).getAsJsonObject();
			JsonElement tempE = temp.get(key);
			String tempS = tempE.getAsString();
			list.add(tempS);
		}
	}
	/**
	 * Speichert folgende Werte aus den Server- und Port-Arrays in neue Listen.
	 * 
	 * <p>
	 * <b>Werte:</b>
	 * <ul>
	 * <li><b>Port als Integer</b></li>
	 * <li><b>Servername als String</b></li>
	 * <li><b>ServerIP als String</b></li>
	 * </ul>
	 * </p>
	 */
	private void setPortServerValuesInAList() {
		// die Port-Werte in neue Liste speichern
		saveValuesInArray(jfh.getPortsArray(), "port", portList);
		// die Port-Werte in neue Liste speichern
		saveValuesInArray(jfh.getPortsArray(), "name", portNameList);
		// Servername in neue List speichern
		saveValuesInArray(jfh.getServerArray(), "name", serverNameList);
		// IP-Adressen aus Server-Array in neue List speichern
		saveValuesInArray(jfh.getServerArray(), "ip", ipList);
		// Host-Adressen aus Server-Array in neue List speichern
		saveValuesInArray(jfh.getServerArray(), "host", hostList);
	}
	/**
	 * Setzt die Werte zum Ausführen der calcProgress-Methode.
	 */
	public void setDefaultProgressInfo() {
		connectArray = new ArrayList<ArrayList<String>>();
		setProgress100(serverNameList.size() * portNameList.size());
		setProgress100Port(portNameList.size());
		setProgress(0);
		setProgressIndicator(0.0);
	}
	/**
	 * Rechnet den Gesamtfortschritt aus
	 */
	private void calcProgress(int j) {
		progressIndicator = progress / progress100;
		progressIndicatorPort = j / progress100Port;
	}
	/**
	 * Setzt verschiedene Status auf Ausgangswert zurück.
	 */
	private void setStatus() {
		setRunning(true);
		setStoped(false);
	}

	/**
	 * In einem eigenen Thread werden die Verbindungen getestet und in ein Array
	 * geschrieben.
	 * 
	 * Zuerst wird ein Service und dann ein Task -Objekt angelegt. In dem
	 * Task-Objekt werden aus den Arrays die Server auf allen Ports angepingt
	 * und das Ergebnis wird in ein neue Array gespeichert. Nach jedem Durchlauf
	 * wird ein Fortschritt des Gesamtdurchlaufes gespeichert.
	 */
	public void saveConnectionRequest() {
		ncService = new Service<>() {
			@Override
			protected Task<Object> createTask() {
				return new Task<>() {
					@Override
					protected Object call() throws InterruptedException {
						setDefaultProgressInfo();
						for (int i = 0; i < ipList.size(); i++) {
							if (!isStoped()) {
								setStatus();
								ip = ipList.get(i);
								host = hostList.get(i);
								setServerName(serverNameList.get(i));
								// Zeit zum drücken der Button
								if (!isStoped()) {
									portConnArray = new ArrayList<String>();
									portConnArray.add(serverName);
									for (int j = 0; j < portList.size(); j++) {
										if (!isStoped()) {
											setStatus();
											setPortName(portNameList.get(j));
											portAdr = Integer
													.parseInt(portList.get(j));
											if (!isStoped()) {
												connected = openSocket(ip,
														portAdr, serverName,
														portName);
												portConnArray.add(
														""+connected);
												// Fortschritt +1
												progress++;
												calcProgress(j);
												setRunning(true);
											} else {
												break;
											}
										}
									}
								}
								connectArray.add(portConnArray);
							} else {
								break;
							}
						}
						// TODO löschen
						System.out.println(connectArray);
						if (getSocket() != null) {
							closeSocket();
						}
						setServerOutput(" "); 
						setPortOutput(" "); 
						setServerNameOutput(" "); 
						setPortNameOutput(" "); 
						setRunning(false);
						return null;
					}
				};
			}
		};
		ncService.start();
	}
	/**
	 * Startet die Verbindung von Server und Port.
	 * 
	 * @throws InterruptedException
	 */
	public boolean openSocket(String server, int port, String serverName,
			String portName) {
		setServerOutput(server); 
		setServerNameOutput(serverName);
		setPortOutput(""+port); 
		setPortNameOutput(portName);
		try {
			setSocket(new Socket(server, port));
			Thread.sleep(50);
			return true;
		} catch (IOException | InterruptedException e) {
			return false;
		}
	}
	/**
	 * Schließt die Verbindung zum Server.
	 */
	public void closeSocket() {
		try {
			getSocket().close();
		} catch (IOException e) {
		}
	}
	// #########################################################################
	// ## Getter und Setter ####################################################
	// #########################################################################
	// --> Daten für die View --------------------------------------------------
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public ArrayList<String> getHostList() {
		return hostList;
	}
	public void setHostList(ArrayList<String> hostList) {
		this.hostList = hostList;
	}
	public ArrayList<String> getIpList() {
		return ipList;
	}
	public ArrayList<String> getServerNameList() {
		return serverNameList;
	}
	public ArrayList<String> getPortList() {
		return portList;
	}
	public ArrayList<String> getPortNameList() {
		return portNameList;
	}
	public ArrayList<ArrayList<String>> getConnectArray() {
		return connectArray;
	}
	public boolean isConnected() {
		return connected;
	}
	public void setConnected(boolean connected) {
		this.connected = connected;
	}
	public ArrayList<String> getPortConnArray() {
		return portConnArray;
	}
	public void setPortConnArray(ArrayList<String> portConnArray) {
		this.portConnArray = portConnArray;
	}
	// --> Array Iteration -----------------------------------------------------
	public boolean isStoped() {
		return stoped;
	}
	public void setStoped(boolean stoped) {
		this.stoped = stoped;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getPortName() {
		return portName;
	}

	public void setPortName(String portName) {
		this.portName = portName;
	}

	// --> Socket --------------------------------------------------------------
	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	// --> Progress & Indicator -----------------------------------------------
	public double getProgressIndicator() {
		return progressIndicator;
	}

	public double getProgress() {
		return progress;
	}

	public void setProgress(double progress) {
		this.progress = progress;
	}

	public double getProgress100() {
		return progress100;
	}

	public void setProgress100(double progress100) {
		this.progress100 = progress100;
	}

	public void setProgressIndicator(double progressIndicator) {
		this.progressIndicator = progressIndicator;
	}
	
	public double getProgressPort() {
		return progressPort;
	}

	public void setProgressPort(double progressPort) {
		this.progressPort = progressPort;
	}

	public double getProgressIndicatorPort() {
		return progressIndicatorPort;
	}

	public void setProgressIndicatorPort(double progressIndicatorPort) {
		this.progressIndicatorPort = progressIndicatorPort;
	}

	public double getProgress100Port() {
		return progress100Port;
	}

	public void setProgress100Port(double progress100Port) {
		this.progress100Port = progress100Port;
	}

	// --> Message -------------------------------------------------------------
	public String getStatusOutput() {
		return statusOutput;
	}

	public void setStatusOutput(String statusOutput) {
		this.statusOutput = statusOutput;
	}

	public String getServerOutput() {
		return serverOutput;
	}

	public void setServerOutput(String serverOutput) {
		this.serverOutput = serverOutput;
	}

	public String getPortOutput() {
		return portOutput;
	}

	public void setPortOutput(String portOutput) {
		this.portOutput = portOutput;
	}

	public String getServerNameOutput() {
		return serverNameOutput;
	}

	public void setServerNameOutput(String serverNameOutput) {
		this.serverNameOutput = serverNameOutput;
	}

	public String getPortNameOutput() {
		return portNameOutput;
	}

	public void setPortNameOutput(String portNameOutput) {
		this.portNameOutput = portNameOutput;
	}


	

}
