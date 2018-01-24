package main.java.model;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class NetworkConnection implements Runnable {

	// ## Variablen ############################################################
	private JSONFileHandler jfh;

	// --> Listen für View -----------------------------------------------------
	private ArrayList<String> portList = new ArrayList<>();
	private ArrayList<String> serverNameList = new ArrayList<>();
	private ArrayList<String> ipList = new ArrayList<>();
	private ArrayList<ArrayList<String>> connectArray = new ArrayList<ArrayList<String>>();
	private ArrayList<String> temp;

	private Boolean isConnected = false;

	// #########################################################################
	// ## Initialisieren #######################################################
	// #########################################################################
	public NetworkConnection() {
		jfh = new JSONFileHandler();
		setPortServerValuesInAList();
		testServerPortConnection();
	}
	@Override
	public void run() {

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
		// Ports-Array aus JSONFile speichern
		jfh.setPortsArray(jfh.getJsonObj().getAsJsonArray("ports"));
		// die Port-Werte in neue Liste speichern
		saveValuesInArray(jfh.getPortsArray(), "port", portList);
		// Server-Array aus JSONFile speichern
		jfh.setServerArray(jfh.getJsonObj().getAsJsonArray("server"));
		// Servername in neue List speichern
		saveValuesInArray(jfh.getServerArray(), "name", serverNameList);
		// IP-Adressen aus Server-Array in neue List speichern
		saveValuesInArray(jfh.getServerArray(), "ip", ipList);

	}

	/**
	 * Array für die Verbindungen.
	 */
	public void testServerPortConnection() {
		int portAdr = 0;
		String ip = "";
		for (int i = 0; i < getPortList().size(); i++) {
			portAdr = Integer.parseInt(getPortList().get(i));
			System.out.println(portAdr);
			temp = new ArrayList<String>();
			for (int j = 0; j < getIpList().size(); j++) {
				ip = getIpList().get(j);
				System.out.print(" -" + ip);
				try {
					Socket s = new Socket(ip, portAdr);
					System.out.println(" -> " + s.isConnected());
					setIsConnected(true);
					temp.add(getIsConnected().toString());
					setTemp(temp);
					s.close();
				} catch (UnknownHostException e) {
					System.out.println("Unbekanner Host");
				} catch (IOException e) {
					System.out.println(" XXX ");
					setIsConnected(false);
					temp.add(getIsConnected().toString());
					setTemp(temp);
				}
			}
			connectArray.add(temp);
		}
		System.out.println(connectArray);
	}
	// #########################################################################
	// ## Getter und Setter ####################################################
	// #########################################################################
	// --> Daten für die View --------------------------------------------------
	public ArrayList<String> getIpList() {
		return ipList;
	}
	public ArrayList<String> getPortList() {
		return portList;
	}
	public ArrayList<String> getServerNameList() {
		return serverNameList;
	}
	public ArrayList<ArrayList<String>> getConnectArray() {
		return connectArray;
	}
	public Boolean getIsConnected() {
		return isConnected;
	}
	public void setIsConnected(Boolean isConnected) {
		this.isConnected = isConnected;
	}
	public ArrayList<String> getTemp() {
		return temp;
	}
	public void setTemp(ArrayList<String> temp) {
		this.temp = temp;
	}

}
