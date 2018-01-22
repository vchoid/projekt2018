package main.java.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

//TODO syso -> löschen								
//TODO regEx -> bei add und edit Funktionen machen	°
//TODO Javadoc -> überprüfen						°
//TODO Funktionsnamen ggf. anpassen					
//TODO jUnit Tests schreiben 						!
//TODO Ecxeption Handling 							!
//TODO Port/Server Klassen -> einbinden

/**
 * 
 * Werte in die Server_Ports.JSON schreiben, ändern und löschen.</br>
 * 
 * <p>
 * <b>Methoden:</b>
 * <ul>
 * <li>Einen Porteintrag hinzufügen:
 * <b>{@link #addPort(String, String)}</b></li>
 * <li>Einen Porteintrag bearbeiten:
 * <b>{@link #editPort(String, String, String)}</b></li>
 * <li>Einen Porteintrag löschen: <b>{@link #deletePort(String)}</b></li>
 * <li>Einen Servereintrag via Host hinzufügen:
 * <b>{@link #addServerViaHost(String, String)}</b></li>
 * <li>Einen Servereintrag via IP hinzufügen:
 * <b>{@link #addServerViaIP(String, String)}</b></li>
 * <li>Einen Servereintrag bearbeiten:
 * <b>{@link #editServer(String, String, String)}</b></li>
 * <li>Einen Servereintrag löschen: <b>{@link #deleteServer(String)}</b></li>
 * </ul>
 * </p>
 * 
 * @author Christoph Kiank
 * @version 1.0.0
 */

public class JSONFileHandler {

	// ## Variablen ############################################################

	// --> Datei-Handling ------------------------------------------------------
	private Gson gson = new Gson();
	private final static String FILE = System.getProperty("user.dir")
			+ "/src/main/resources/Server_Ports.JSON";
	private BufferedReader reader;
	private FileInputStream input;
	private BufferedWriter writer;
	private FileOutputStream out;
	// --> json-Handling -------------------------------------------------------
	private EmptyPortServerTemplate emptyPSTemplate = new EmptyPortServerTemplate();
	private JsonObject newPort = new JsonObject();
	private JsonObject newServer = new JsonObject();
	private JsonObject jsonObj;
	private JsonArray portsArray;
	private JsonArray serverArray;
	private int positionInArray;
	private JsonElement searchElement;
	private Boolean success = false;

	private List<String> portNameList = new ArrayList<>();
	private List<String> serverNameList = new ArrayList<>();
	private List<Integer> portList = new ArrayList<>();
	private List<String> ipList= new ArrayList<>();
	
	private List connectionList = new ArrayList();
	// --> Exception-Handling --------------------------------------------------
	private Exception e;

	// ## Konstruktor ##########################################################

	public JSONFileHandler() {
		//TODO löschen
//		System.out.println("~~~~~~~~~~~~~~~~ Start ~~~~~~~~~~~~~~~~~");
		init();
	}
	/**
	 * Initialisiert die Datei und bereitet den Inhalt zur Weiterverabeitung
	 * auf.
	 */
	private void init() {
		parseFileAsJSONObject();
		setPortServerValuesInAList("ports", "port", portNameList, "server", "name", serverNameList);
		setIPAndPortInAList();
	}
	/**
	 * Vorlageninhalt für leere JSON-Datei.
	 */
	private void addEmptyJsonFileTemplate() {
		writeInFile(emptyPSTemplate.getPortServerTemplate());
	}
	/**
	 * Ließt Datei ein und speichern den Inhalt in ein {@link JsonObject}}.
	 */
	private void parseFileAsJSONObject() {
		// Datei über einen Stream einlesen
		try {
			input = new FileInputStream(getFile());
			reader = new BufferedReader(new InputStreamReader(input));
			// Datei als JSON-Objekt einlesen
			setJsonObj(gson.fromJson(reader, JsonObject.class));
		} catch (FileNotFoundException e) {
			addEmptyJsonFileTemplate();
			init();
		}
	}

	
	/**
	 * Schreibe Inhalt(Parameter content) in der JSON-Datei und schließe den
	 * Writer.
	 * 
	 * @param content
	 */
	private void writeInFile(String content) {
		try {
			out = new FileOutputStream(getFile());
			writer = new BufferedWriter(new OutputStreamWriter(out));
			writer.write(content);
			// TODO löschen!
			System.out.println(" -> gespeichert in Datei.");
			writer.close();
		} catch (IOException e) {
			setE(e);
		}
	}
	// ## Daten als Array für View #############################################
	/**
	 * Holt Werte eines Arrays und speichert diese in eine neue ArrayList.
	 * 
	 * @param array
	 * @param key
	 * @param list
	 */
	private void saveValuesInArray(JsonArray array, String key, List<String> list) {
		for (int i = 0; i < array.size(); i++) {
			JsonObject temp = array.get(i).getAsJsonObject();
			JsonElement tempE = temp.get(key);
			String tempS = tempE.getAsString();
			list.add(tempS);
		}
	}
	/**
	 * Key Value Paare aus dem Port und Server Array in eine neue List speichern.
	 * 
	 * @param arr1
	 * @param key1
	 * @param newList
	 * @param arr2
	 * @param key2
	 * @param newList2
	 */
	private void setPortServerValuesInAList(String arr1, String key1, List<String> newList, String arr2, String key2, List<String> newList2) {
		setPortsArray(getJsonObj().getAsJsonArray(arr1));
		saveValuesInArray(getPortsArray(),key1, newList);
		setServerArray(getJsonObj().getAsJsonArray(arr2));
		saveValuesInArray(getServerArray(),key2, newList2);
	}
	private void setIPAndPortInAList() {
		for(int i = 0; i < getServerArray().size(); i++) {
			JsonObject servTempObj = getServerArray().get(i).getAsJsonObject();
			JsonElement servElement = servTempObj.get("ip");
			ipList.add(servElement.getAsString());
		}
		for(int j = 0; j < getPortsArray().size();j++) {
			JsonObject portTempObj = getPortsArray().get(j).getAsJsonObject();
			JsonElement portElement = portTempObj.get("port");
			portList.add(portElement.getAsInt());
		}
		System.out.println(ipList);
		System.out.println(portList);
	}
	// ## Prüfen auf validen Inhalt ############################################
	/**
	 * Sucht einen Wert anhand des gesetzten Parameters im Array.
	 * 
	 * JSONArray wird durchlaufen, jeder Eintrag wird temporär gespeichert und
	 * überprüft ob der gesuchte Parameter sich im Array befindet. Wenn ja,
	 * speichere den Eintrag selbst und die Position, wo sich der Eintrag im
	 * Array befindet und gibt true zurück. Ansonsten false.
	 * 
	 * 
	 * @param value
	 * @return
	 */
	private Boolean isValueInArray(JsonArray array, String key, String value) {
		JsonObject temp = new JsonObject();
		for (int i = 0; i < array.size(); i++) {
			temp = array.get(i).getAsJsonObject();
			JsonElement keyTemp = temp.get(key);
			if (keyTemp.getAsString().equalsIgnoreCase(value)) {
				// das ganze Element gesetzt
				setSearchElement(array.get(i));
				// die Position des Elements im Array gesetzt
				setPositionInArray(i);
				// TODO löschen!
				System.out.print(
						"#" + getPositionInArray() + " " + getSearchElement());
				return true;
			}
		}
		return false;
	}
	/**
	 * Prüft ob der Port bereits im Array vorhanden ist und gibt ein true
	 * zurück.
	 * 
	 * @param name
	 * @param port
	 * @return
	 */
	private Boolean isPortAvailable(Port port) {
		if (isValueInArray(getPortsArray(), "port", port.getPortAsString())
				|| isValueInArray(getPortsArray(), "name", port.getName())) {
			// TODO löschen !
			System.out.print("\n  -> vorhanden");
			return true;
		}
		return false;

	}
	/**
	 * Prüft ob der Server bereits im Array vorhanden ist und gibt true zurück.
	 * 
	 * @param name
	 * @param ipOrHost
	 * @return
	 */
	private Boolean isServerAvailable(Server server) {
		if (isValueInArray(getServerArray(), "ip", server.getIp())
				|| isValueInArray(getServerArray(), "name", server.getName())
				|| isValueInArray(getServerArray(), "host", server.getHost())) {
			// TODO löschen !
			System.out.print("\n  -> vorhanden");
			return true;
		}
		return false;
	}

	// ## add-Methode ##########################################################
	private void addNewObjectInArray(JsonArray array, JsonObject newObject) {
		// Objekt in Array anfügen
		array.add(newObject);
	}
	private void addNewArrayInJSONFile(JsonArray array, String key) {
		// Werte in Object anfügen
		getJsonObj().add(key, array);
		// verändertes Objekt als String in Datei schreiben
		writeInFile(getJsonObj().toString());
	}
	/**
	 * Fügt das Objekt in das Array ein und schreibt es in die Datei
	 * 
	 * @param array
	 * @param newObject
	 * @param key
	 */
	private void addObjectInArrayAndWriteInFile(JsonArray array,
			JsonObject newObject, String key) {
		System.out.print(newObject);
		addNewObjectInArray(array, newObject);
		// TODO löschen!
		System.out.print(" -> hinzugefügt zum Array");
		addNewArrayInJSONFile(array, key);
	}

	// --> Port ------------------------------------------------------------
	/**
	 * Fügt zwei Key-Value Paare für das PortsArray hinzu.
	 * 
	 * @param port
	 */
	private void addPortValues(Port port) {
		System.out.println("\n++++++++++++++ HINZUFÜGEN ++++++++++++++");
		newPort.addProperty("name", port.getName());
		newPort.addProperty("port", port.getPort());
	}
	/**
	 * Ein Port in die Server_Ports.JSON-Datei schreiben.
	 *
	 * Erstellt ein neues {@link JsonObject} an und fügt die Parameter hinzu.
	 *
	 * {@link #isValueInArray(JsonArray, String, String)} überprüft ob der Wert
	 * bereits existiert und gibt ein Boolean zurück. Wenn er nicht vorhanden
	 * ist soll er ihn zum Array hinzufügen und dann in die Datei schreiben.
	 *
	 * 
	 * @param port
	 */
	public void addPort(Port port) {
		// neues Objekt mit zwei Key-Value-Paare anlegen
		addPortValues(port);
		// prüfen ob bereits vorhanden
		if (!isPortAvailable(port)) {
			// neuen validen Wert schreiben
			addObjectInArrayAndWriteInFile(getPortsArray(), newPort, "ports");
		}
	}
	// --> Server ----------------------------------------------------------
	/**
	 * Fügt drei Key-Value Paare für das Server-Array hinzu.
	 * 
	 * @param server
	 */
	private void addServerValues(Server server) {
		System.out.println("\n++++++++++++++ HINZUFÜGEN ++++++++++++++");
		newServer.addProperty("name", server.getName());
		newServer.addProperty("host", server.getHost());
		newServer.addProperty("ip", server.getIp());
	}

	/**
	 * Ein Server in die Server_Ports.JSON-Datei schrieben
	 * 
	 * {@link #addServerValues(Server)}} fügt Werte dem Server-Objekt hinzu.
	 * Überprüft ob die Werte bereits in dem Server-Objekt vorhanden sind. Wenn
	 * nicht, werden diese dem Array hinzugefügt und in die Datei geschrieben.
	 * 
	 * @param server
	 */
	public void addServer(Server server) {
		// neues Objekt mit drei Key-Value-Paare anlegen
		addServerValues(server);
		// prüfen ob bereits vorhanden
		if (!isServerAvailable(server)) {
			// neuen validen Wert schreiben
			addObjectInArrayAndWriteInFile(getServerArray(), newServer,
					"server");
		}
	}
	// ## delete Methoden ######################################################
	private void removeValueFromArray(String name, JsonArray array) {
		System.out.println("\n+++++++++++++++ LÖSCHEN ++++++++++++++++");
		if (isValueInArray(array, "name", name)) {
			try {
				array.remove(getPositionInArray());
				// TODO löschen!
				System.out.print("  -> wurde aus Array gelöscht");
				setSuccess(true);
			} catch (Exception e) {
				setE(e);
			}
		} else {
			System.out.print(name);
			System.err.println("  -> nicht im Array vorhanden");
			setSuccess(false);
		}
	}
	/**
	 * Löscht ein Port anhand des Parameters.
	 * 
	 * @param name
	 */
	public void deletePort(Port port) {
		removeValueFromArray(port.getName(), getPortsArray());
		if (getSuccess()) {
			addNewArrayInJSONFile(getPortsArray(), "ports");
		}
	}
	/**
	 * Löscht ein Server anhand des Parameters.
	 * 
	 * @param name
	 */
	public void deleteServer(Server server) {
		removeValueFromArray(server.getName(), getServerArray());
		if (getSuccess()) {
			addNewArrayInJSONFile(getServerArray(), "server");
		}
	}

	// ## edit Methoden ########################################################
	/**
	 * 
	 * Verändert einzelne Wertepaare aus dem Port-Array.
	 * 
	 * Überprüft zuerst ob der alte Wert existiert. Speichert temporär das
	 * Array. Überprüft als nächstes, ob der neue Wert nicht schon vorhanden
	 * ist. Wenn er nicht existiert, dann füge den neuen Wert dem Array hinzu
	 * und schreibe das neue Array in die Datei.
	 * 
	 * @param key
	 * @param oldVal
	 * @param newVal
	 */
	public void editPort(int oldVal, int newVal) {
		System.out.println("\n////////////// BEARBEITEN //////////////");
		// überprüfen ob der alter Wert überhaupt existiert
		if (isValueInArray(getPortsArray(), "port", ""+oldVal)) {
			JsonObject temp = (JsonObject) getPortsArray()
					.get(getPositionInArray());
			// überprüfen ob der neue Wert bereits existiert
			if (!isValueInArray(getPortsArray(), "port",""+ newVal)) {
				temp.addProperty("port", newVal);
				addNewArrayInJSONFile(getPortsArray(), "ports");
			} else {
				// TODO löschen!
				System.out.println("  -> keine doppelten Werte erlaubt");
			}
		} else {
			System.out.print(oldVal);
			System.out.println(" -> nicht gefunden");
		}
	}
	/**
	 * 
	 * Verändert einzelne Wertepaare aus dem Port-Array.
	 * 
	 * Überprüft zuerst ob der alte Wert existiert. Speichert temporär das
	 * Array. Überprüft als nächstes, ob der neue Wert nicht schon vorhanden
	 * ist. Wenn er nicht existiert, dann füge den neuen Wert dem Array hinzu
	 * und schreibe das neue Array in die Datei.
	 * 
	 * @param key
	 * @param oldVal
	 * @param newVal
	 */
	public void editPortName(String oldVal, String newVal) {
		System.out.println("\n////////////// BEARBEITEN //////////////");
		// überprüfen ob der alter Wert überhaupt existiert
		if (isValueInArray(getPortsArray(), "name", oldVal)) {
			JsonObject temp = (JsonObject) getPortsArray()
					.get(getPositionInArray());
			// überprüfen ob der neue Wert bereits existiert
			if (!isValueInArray(getPortsArray(), "name", newVal)) {
				temp.addProperty("name", newVal);
				addNewArrayInJSONFile(getPortsArray(), "ports");
			} else {
				// TODO löschen!
				System.out.println("  -> keine doppelten Werte erlaubt");
			}
		} else {
			System.out.print(oldVal);
			System.out.println(" -> nicht gefunden");
		}
	}
	/**
	 * 
	 * Verändert einzelne Wertepaare aus dem Server-Array.
	 * 
	 * Überprüft zuerst ob der alte Wert existiert. Speichert temporär das
	 * Array. Überprüft als nächstes, ob der neue Wert nicht schon vorhanden
	 * ist. Wenn er nicht existiert, dann füge den neuen Wert dem Array hinzu
	 * und schreibe das neue Array in die Datei.
	 * 
	 * @param key
	 * @param oldVal
	 * @param newVal
	 */
	public void editServer(String key, String oldVal, String newVal) {
		System.out.println("\n////////////// BEARBEITEN //////////////");
		// überprüfen ob der alter Wert überhaupt existiert
		if (isValueInArray(getServerArray(), key, oldVal)) {
			JsonObject temp = (JsonObject) getServerArray()
					.get(getPositionInArray());
			// überprüfen ob der neue Wert bereits existiert
			if (!isValueInArray(getServerArray(), key, newVal)) {
				temp.addProperty(key, newVal);
				addNewArrayInJSONFile(getServerArray(), "server");
			} else {
				// TODO löschen!
				System.out.println("  -> keine doppelten Werte erlaubt");
			}
		} else {
			System.out.print(oldVal);
			System.out.println(" -> nicht gefunden");
		}
	}

	// ## Getter und Setter ####################################################
	// --> Array-Handling ------------------------------------------------------

	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public JsonElement getSearchElement() {
		return searchElement;
	}
	public void setSearchElement(JsonElement searchElement) {
		this.searchElement = searchElement;
	}
	public JsonObject getJsonObj() {
		return jsonObj;
	}
	public void setJsonObj(JsonObject json) {
		this.jsonObj = json;
	}
	public JsonArray getPortsArray() {
		return portsArray;
	}
	public void setPortsArray(JsonArray portsArray) {
		this.portsArray = portsArray;
	}
	public JsonArray getServerArray() {
		return serverArray;
	}
	public void setServerArray(JsonArray serverArray) {
		this.serverArray = serverArray;
	}
	public int getPositionInArray() {
		return positionInArray;
	}
	public void setPositionInArray(int positionInArray) {
		this.positionInArray = positionInArray;
	}

	public List<String> getPortNameList() {
		return portNameList;
	}
	public List<String> getServerNameList() {
		return serverNameList;
	}
	// --> Datei-Handling ------------------------------------------------------
	public static String getFile() {
		return FILE;
	}
	// --> Exception Handling --------------------------------------------------
	public Exception getE() {
		return e;
	}
	public void setE(Exception e) {
		this.e = e;
	}

}
