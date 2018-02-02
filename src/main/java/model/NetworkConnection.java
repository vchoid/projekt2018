package main.java.model;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import javax.print.attribute.SetOfIntegerSyntax;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class NetworkConnection {

	// hier weiter machen -> Code aus startConnectioRequest
	// --> Extends Service

	// ## Variablen ############################################################
	private JSONFileHandler jfh;

	// --> Listen für View
	// -----------------------------------------------------
	private ArrayList<String> portList = new ArrayList<>();
	private ArrayList<String> portNameList = new ArrayList<>();
	private ArrayList<String> serverNameList = new ArrayList<>();
	private ArrayList<String> ipList = new ArrayList<>();
	private String serverName = "";
	private String ip = "";
	private int portAdr = 0;

	private ArrayList<ArrayList<String>> connectArray = new ArrayList<ArrayList<String>>();
	private ArrayList<String> portConnArray;
	private Socket socket;
	private String connected = "";
	private boolean stoped;
	private boolean running;
	private boolean skipPort;
	private boolean skipServer;
	private int threadTime;
	
	private double progress;
	private double progressIndicator;
	private double progress100;

	private Service<Object> ncService;
	// #########################################################################
	// ## Initialisieren #######################################################
	// #########################################################################
	public NetworkConnection() {
		jfh = new JSONFileHandler();
		setPortServerValuesInAList();
	}

	// #########################################################################
	// ## Daten als f�r View
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
	}
	/**
	 * Setzt die Werte zum Ausführen der calcProgress-Methode.
	 */
	private void setProgressInfo() {
		setProgress100(serverNameList.size() * portNameList.size());
		setProgress(0);
		setProgressIndicator(0.0);
		setThreadTime(2*1000);
	}
	/**
	 * Setzt verschiedene Status auf Ausgangswert zurück.
	 */
	private void setStatus() {
		setRunning(true);
		setSkipPort(false);
		setSkipServer(false);
		setStoped(false);
	}
	/**
	 * Rechnet den Gesamtfortschritt aus
	 */
	private void calcProgress() {
		progressIndicator = progress / progress100;
	}
	/**
	 * Einen Server auslassen. Die Fortschrittsanzeige um die Anzahl der
	 * ausgelassenen Ports erhöhen. Im Array den Eintrag skipped hinzufügen.
	 * 
	 * @param size
	 */
	private void skipServer(int size) {
		setProgress((1 * size) + getProgress());
		ArrayList<String> temp = new ArrayList<>();
		temp.add(">skipped");
		connectArray.add(temp);
		System.out.println(" >skipped");
	}
	/**
	 * Einen Port auslassen. Die Fortschrittsanzeige um eins erhöhen und ins
	 * Array anstelle der Verbindungsanfrage ein -S- schreiben.
	 */
	private void skipPort() {
		setProgress(getProgress() + 1);
		portConnArray.add("-s-");
		System.out.print("-s-");
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
	public void startConnectionRequest() {
		ncService = new Service<>() {
			@Override
			protected Task<Object> createTask() {
				return new Task<>() {
					@Override
					protected Object call() throws Exception {
						setProgressInfo();
						for (int i = 0; i < ipList.size(); i++) {
							setStatus();
							ip = ipList.get(i);
							serverName = serverNameList.get(i);
							System.out.print(serverName);
							// Zeit zum drücken der Button
							Thread.sleep(getThreadTime());
							if (isSkipServer()) {
								skipServer(portList.size());
								continue;
							} else if (isStoped()) {
								break;
							} else if (!isStoped()) {
								portConnArray = new ArrayList<String>();
								portConnArray.add(serverName);
								// TODO löschen
								for (int j = 0; j < portList.size(); j++) {
									setStatus();
									portAdr = Integer.parseInt(portList.get(j));
									System.out.print(" | ");
									// Zeit zum drücken der Button
									Thread.sleep(getThreadTime());
									if (isSkipPort()) {
										skipPort();
										continue;
									} else if (isStoped()) {
										break;
									} else if (!isStoped()) {
										connected = openSocket(ip, portAdr);
										portConnArray.add(connected.toString());
										// TODO löschen
										System.out.print(connected);
										// Fortschritt +1
										progress++;
										calcProgress();
										setRunning(true);
									}
								}
								connectArray.add(portConnArray);
								System.out.println(
										"\n----------------------------------------------");
							}
						}
						setRunning(false);
						if (getSocket() != null) {
							closeSocket();
						}
						// progressIndicator = 0.0;
						// TODO löschen
						System.out.println(connectArray);
						return null;
					}
				};
			}
		};
		ncService.start();
	}
	/**
	 * Startet die Verbindung von Server und Port.
	 */
	public String openSocket(String server, int port) {
		try {
			setSocket(new Socket(server, port));
		} catch (IOException e) {
			return " -- ";
		}
		return " -O- ";
	}
	/**
	 * Schließt die Verbindung zum Server.
	 */
	public void closeSocket() {
		try {
			getSocket().close();
		} catch (IOException e) {
			System.out.println("Konnte nicht geschlossen werden");
		}
	}
	// #########################################################################
	// ## Getter und Setter ####################################################
	// #########################################################################
	// --> Daten für die View
	// --------------------------------------------------
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
	public String isConnected() {
		return connected;
	}
	public void setConnected(String connected) {
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

	public boolean isSkipPort() {
		return skipPort;
	}

	public void setSkipPort(boolean skipPort) {
		this.skipPort = skipPort;
	}

	public boolean isSkipServer() {
		return skipServer;
	}

	public void setSkipServer(boolean skipServer) {
		this.skipServer = skipServer;
	}
	
	public int getThreadTime() {
		return threadTime;
	}

	public void setThreadTime(int threadTime) {
		this.threadTime = threadTime;
	}

	// --> Socket --------------------------------------------------------------
	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	// --> Progress Indicator --------------------------------------------------
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
	
	
}
