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

	// hier weiter machen -> Code aus startConnectioRequest
	// --> Extends Service

	// ## Variablen ############################################################
	private JSONFileHandler jfh;

	// --> Listen für View -----------------------------------------------------
	private ArrayList<String> portList = new ArrayList<>();
	private ArrayList<String> portNameList = new ArrayList<>();
	private ArrayList<String> serverNameList = new ArrayList<>();
	private ArrayList<String> ipList = new ArrayList<>();
	private String serverName = "";
	private String ip = "";
	private int portAdr = 0;

	private ArrayList<ArrayList<String>> connectArray = new ArrayList<ArrayList<String>>();
	private ArrayList<String> temp;
	private String isConnected = "";
	private boolean stopNC;
	private boolean isRunning;

	private double progress;
	private double progressIndicator;
	private double progress100;

	private Service<Object> ncService;
	private Socket socket;
	// #########################################################################
	// ## Initialisieren #######################################################
	// #########################################################################
	public NetworkConnection() {
		jfh = new JSONFileHandler();
		setPortServerValuesInAList();
	}

	// #########################################################################
	// ## Daten als für View ###################################################
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
	
	private void setProgressInfo() {
		progress100 = serverNameList.size()* portNameList.size();
		progress = 0;
		progressIndicator = 0.0;
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
							
							if(isStopNC() == true) {
								break;
							} else if(isStopNC() == false) {
								ip = ipList.get(i);
								serverName = serverNameList.get(i);
								temp = new ArrayList<String>();
								temp.add(serverName);
								// TODO löschen
								System.out.print(serverName);
							}
							for (int j = 0; j < portList.size(); j++) {
								if(isStopNC() == true) {
									break;
								} else if(isStopNC() == false) {
									portAdr = Integer.parseInt(portList.get(j));
									isConnected = startSocket(ip, portAdr);
									temp.add(isConnected.toString());
									// TODO löschen
									System.out.print(" | " + isConnected);
									// setzt den Fortschritt nach jedem Durchlauf
									// eins höher
									progress++;
									// rechnet den Gesamtfortschritt aus
									progressIndicator = progress / progress100;
								}
							}
							connectArray.add(temp);
							System.out.println(
									"\n----------------------------------------------");
						}
						progressIndicator = 0.0;
						// TODO löschen
//						System.out.println(connectArray);
						closeSocket();
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
	public String startSocket(String server, int port) {
		try {
			setSocket(new Socket(server, port));
		} catch (IOException e) {
			return "-";
		}
		return "O";
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
	// --> Daten für die View --------------------------------------------------
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
	public String getIsConnected() {
		return isConnected;
	}
	public void setIsConnected(String isConnected) {
		this.isConnected = isConnected;
	}
	public ArrayList<String> getTemp() {
		return temp;
	}
	public void setTemp(ArrayList<String> temp) {
		this.temp = temp;
	}
	// --> Array Iteration -----------------------------------------------------
	public boolean isStopNC() {
		return stopNC;
	}
	public void setStopNC(boolean stopNC) {
		this.stopNC = stopNC;
	}
	
	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
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
	

}
