package main.java.model;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javafx.concurrent.Service;
import javafx.concurrent.Task;



public class NetworkConnection  {
	
	// hier weiter machen -> Code aus startConnectioRequest
	// --> Extends Service
	

	// ## Variablen ############################################################
	private JSONFileHandler jfh;

	// --> Listen für View -----------------------------------------------------
	private ArrayList<String> portList = new ArrayList<>();
	private ArrayList<String> portNameList = new ArrayList<>();
	private ArrayList<String> serverNameList = new ArrayList<>();
	private ArrayList<String> ipList = new ArrayList<>();

	private ArrayList<ArrayList<String>> connectArray = new ArrayList<ArrayList<String>>();
	private ArrayList<String> temp;
	private String isConnected = "";

	private double progress = 0;
	private double progressIndicator = 0;
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
						String serverName = "";
						String ip = "";
						String portName = "";
						int portAdr = 0;
						progress100 = serverNameList.size()
								* portNameList.size();
						progress = 0;
						progressIndicator = 0.0;
						for (int i = 0; i < ipList.size(); i++) {
							ip = ipList.get(i);
							serverName = serverNameList.get(i);
							temp = new ArrayList<String>();
							temp.add(serverName);
							// TODO löschen
							System.out.println(serverName + "[" + ip + "]");
							for (int j = 0; j < portList.size(); j++) {
								portAdr = Integer.parseInt(portList.get(j));
								portName = portNameList.get(j);
								isConnected = startSocket(ip,
										portAdr);
								temp.add(isConnected.toString());
								// TODO löschen
								System.out.println(portName + "[" + portAdr
										+ "]" + ":" + isConnected);
								// setzt den Fortschritt nach jedem Durchlauf
								// eins höher
								progress++;
								// rechnet den Gesamtfortschritt aus
								progressIndicator = progress / progress100;
							}
							connectArray.add(temp);
						}
						progressIndicator = 0.0;
						// TODO löschen
						System.out.println(connectArray);
						return null;
					}
				};
			}
		};
		ncService.start();
	}

	// /**
	// * Array für die Verbindungen.
	// */
	// public void saveAllPortServerConnectionInArray() {
	// int portAdr = 0;
	// String ip = "";
	// connectArray.add(serverNameList);
	// for (int i = 0; i < getPortList().size(); i++) {
	// portAdr = Integer.parseInt(getPortList().get(i));
	// System.out.println(portAdr);
	// temp = new ArrayList<String>();
	// for (int j = 0; j < serverNameList.size(); j++) {
	// ip = getIpList().get(j);
	// System.out.print(" -" + ip);
	// try {
	// Socket s = new Socket(ip, portAdr);
	// System.out.println(" -> " + s.isConnected());
	// setIsConnected(true);
	//// temp.add(getIsConnected().toString());
	// temp.add(ip + ":" + portAdr);
	// setTemp(temp);
	// s.close();
	// } catch (UnknownHostException e) {
	// System.out.println("Unbekanner Host");
	// } catch (IOException e) {
	// System.out.println(" XXX ");
	// setIsConnected(false);
	// temp.add("X " + ip + ":" + portAdr + " X");
	//// temp.add(getIsConnected().toString());
	// setTemp(temp);
	// }
	// }
	// connectArray.add(temp);
	// }
	// System.out.println(connectArray);
	// }

	/**
	 * Verbindung von Server mit Port testen.
	 */
	public String startSocket(String server, int port) {
		try {
			Socket s = new Socket(server, port);
			s.close();
		} catch (IOException e) {
			return "-";
		}
		return "O";
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

	public double getProgress100() {
		return progress100;
	}

	public void setProgress100(double progress100) {
		this.progress100 = progress100;
	}

	public double getProgress() {
		return progress;
	}

	public void setProgress(double progress) {
		this.progress = progress;
	}
	public double getProgressIndicator() {
		return progressIndicator;
	}

	public void setProgressIndicator(double progressIndicator) {
		this.progressIndicator = progressIndicator;
	}

}
