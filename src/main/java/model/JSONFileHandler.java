package main.java.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

//TODO syso -> löschen								
//TODO regEx -> bei add und edit Funktionen machen	°
//TODO Funktionsnamen ggf. anpassen					
//TODO jUnit Tests schreiben 						!
//TODO Ecxeption Handling 							!

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

	// --> Exception-Handling --------------------------------------------------
	private Exception e;

	// #########################################################################
	// ## Initialisieren ######################################################
	// #########################################################################
	public JSONFileHandler() {
		init();
	}
	/**
	 * Initialisiert die Datei mit der {@link #parseFileAsJSONObject()}-Methode.
	 * Bereitet den Inhalt zur Weiterverabeitung mit folgenden Methoden auf.
	 * <p>
	 * <b>Methoden:</b>
	 * <ul>
	 * <li>Speichert Port und Server in jeweils ein JSONArray:
	 * <b>{@link #setPortServerValuesInAList()}</b></li>
	 * </ul>
	 * </p>
	 */
	private void init() {
		parseFileAsJSONObject();
		setPortServerValuesInAList();
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
			writer.flush();
		} catch (IOException e) {
			setE(e);
		}
	}

	/**
	 * Speichert Werte aus den Server- und Port-Arrays aus der JSON-Datei in
	 * Arrays.
	 * 
	 */
	private void setPortServerValuesInAList() {
		// Ports-Array aus JSONFile speichern
		setPortsArray(getJsonObj().getAsJsonArray("ports"));
		// Server-Array aus JSONFile speichern
		setServerArray(getJsonObj().getAsJsonArray("server"));

	}
	// #########################################################################
	// ## Prüfen auf validen Inhalt ############################################
	// #########################################################################
	/**
	 * Sucht einen Wert anhand des gesetzten Parameters im Array.
	 * 
	 * JSONArray wird durchlaufen, jeder Eintrag wird temporär gespeichert und
	 * überprüft ob der gesuchte Parameter sich im Array befindet. Wenn ja,
	 * speichere den Eintrag selbst mit der
	 * {@link #setSearchElement(JsonElement)}-Methode und die Position mit der
	 * {@link #setPositionInArray(int)}-Methode, wo sich der Eintrag im Array
	 * befindet. Wenn ja dann, gib true zurück. Ansonsten false.
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
	 * Prüft mit der {@link #isValueInArray(JsonArray, String, String)}-Methode,
	 * ob der Port bereits im Array vorhanden ist. Wenn ja dann gib ein true
	 * zurück.
	 * 
	 * @param name
	 * @param port
	 * @return
	 */
	private Boolean isPortAvailable(Port port) {
		if (isValueInArray(getPortsArray(), "port", "" + port.getPort())
				|| isValueInArray(getPortsArray(), "name", port.getName())) {
			// TODO löschen !
			System.out.print("\n  -> vorhanden");
			return true;
		}
		return false;

	}
	/**
	 * Prüft mit der {@link #isValueInArray(JsonArray, String, String)}-Methode,
	 * ob der Server bereits im Array vorhanden ist. Wenn ja dann gib ein true
	 * zurück.
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

	// #########################################################################
	// ## add-Methode ##########################################################
	// #########################################################################
	/**
	 * Fügt ein neue Objekt in ein Array.
	 * 
	 * @param array
	 * @param newObject
	 */
	private void addNewObjectInArray(JsonArray array, JsonObject newObject) {
		// Objekt in Array anfügen
		array.add(newObject);
	}
	/**
	 * Holt das JSONObjekt mit der {@link #getJsonObj()}-Methode und schreibt
	 * mit der {@link #writeInFile(String)}-Methode das neue Objekt.
	 * 
	 * @param array
	 * @param key
	 */
	private void addNewArrayInJSONFile(JsonArray array, String key) {
		// Werte in Object anfügen
		getJsonObj().add(key, array);
		// verändertes Objekt als String in Datei schreiben
		writeInFile(getJsonObj().toString());
	}
	/**
	 * Fügt mit der {@link #addNewObjectInArray(JsonArray, JsonObject)}-Methode
	 * das Objekt in das Array ein und schreibt mit
	 * der{@link #addNewArrayInJSONFile(JsonArray, String)}-Methode das Objekt
	 * in die Datei.
	 * 
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
	 * Ein Port in die Server_Ports.JSON-Datei schreiben
	 * 
	 * {@link #addPortValues(Port)} fügt Werte dem Port-Objekt hinzu.
	 * 
	 * Überprüft mit der {@link #isPortAvailable(Port)}-Methode, ob die Werte
	 * bereits in dem Port-Objekt vorhanden sind. Wenn nicht, werden die neue
	 * Werte mit der
	 * {@link #addObjectInArrayAndWriteInFile(JsonArray, JsonObject, String)}-Methode
	 * dem Array hinzugefügt und in die Datei geschrieben.
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
	 * {@link #addServerValues(Server)} fügt Werte dem Server-Objekt hinzu.
	 * 
	 * Überprüft mit der {@link #isServerAvailable(Server)}-Methode, ob die
	 * Werte bereits in dem Server-Objekt vorhanden sind. Wenn nicht, werden die
	 * neue Werte mit der
	 * {@link #addObjectInArrayAndWriteInFile(JsonArray, JsonObject, String)}-Methode
	 * dem Array hinzugefügt und in die Datei geschrieben.
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
	// #########################################################################
	// ## delete Methoden ######################################################
	// #########################################################################
	/**
	 * Löscht einen Wert aus einem Array. Überprüft mit der
	 * {@link #isValueInArray(JsonArray, String, String)}-Methode, ob der Wert
	 * im Array ist, wenn ja wird der Eintrag über die
	 * {@link #getPositionInArray()}-Methode ermittelt und aus dem Array
	 * entfernt. Der Wert Success wird auf true gesetzt.
	 * 
	 * @param value
	 * @param array
	 */
	private void removeValueFromArray(String value, JsonArray array) {
		System.out.println("\n+++++++++++++++ LÖSCHEN ++++++++++++++++");
		if (isValueInArray(array, "name", value)) {
			try {
				array.remove(getPositionInArray());
				// TODO löschen!
				System.out.print("  -> wurde aus Array gelöscht");
				setSuccess(true);
			} catch (Exception e) {
				setE(e);
			}
		} else {
			System.out.print(value);
			System.out.println("  -> nicht im Array vorhanden");
			setSuccess(false);
		}
	}
	/**
	 * Entfernt Werte aus einem Array mit der
	 * {@link #removeValueFromArray(String, JsonArray)}-Methode. Mit der
	 * {@link #getSuccess()}-Methode, wird überprüft, ob die
	 * {@link #removeValueFromArray(String, JsonArray)}-Methode erfolgreich war.
	 * Wenn ja wird das neue Array mit der
	 * {@link #addNewArrayInJSONFile(JsonArray, String)}-Methode in die Datei
	 * geschrieben.
	 * 
	 * @param array
	 * @param arrayInFile
	 * @param value
	 */
	private void deleteValuesFromArray(JsonArray array, String arrayInFile,
			String value) {
		removeValueFromArray(value, array);
		if (getSuccess()) {
			addNewArrayInJSONFile(array, arrayInFile);
		}
	}
	/**
	 * Löscht ein Port anhand des Parameters mit der
	 * {@link #deleteValuesFromArray(JsonArray, String, String)}-Methode.
	 * 
	 * @param name
	 */
	public void deletePort(String portName) {
		deleteValuesFromArray(getPortsArray(), "ports", portName);
	}
	/**
	 * Löscht ein Server anhand des Parameters mit der
	 * {@link #deleteValuesFromArray(JsonArray, String, String)}-Methode..
	 * 
	 * @param name
	 */
	public void deleteServer(String serverName) {
		deleteValuesFromArray(getServerArray(), "server", serverName);
	}

	// #########################################################################
	// ## edit Methoden ########################################################
	// #########################################################################
	/**
	 * 
	 * Verändert einzelne Wertepaare aus dem Array.
	 * 
	 * Überprüft zuerst ob der alte Wert existiert. Speichert temporär das
	 * Array. Überprüft als nächstes, ob der neue Wert nicht schon vorhanden
	 * ist. Wenn er nicht existiert, dann füge den neuen Wert dem Array hinzu
	 * und schreibe das neue Array in die Datei.
	 * 
	 * @param jArray
	 * @param arrayInFile
	 * @param key
	 * @param oldVal
	 * @param newVal
	 */
	private void editValuesFromArray(JsonArray jArray, String arrayInFile,
			String key, String oldVal, String newVal) {

		System.out.println();

		System.out.println("\n////////////// BEARBEITEN //////////////");
		// überprüfen ob der alter Wert überhaupt existiert
		if (isValueInArray(jArray, key, oldVal)) {
			JsonObject temp = (JsonObject) jArray.get(getPositionInArray());
			// überprüfen ob der neue Wert bereits existiert
			if (!isValueInArray(jArray, key, newVal)) {
				// überprüfen ob Ports vom Typ Integer verändert werden sollen
				if (jArray == getPortsArray() && key == "port") {
					// wenn ja den String in Integer parsen
					temp.addProperty(key, Integer.parseInt(newVal));
					addNewArrayInJSONFile(jArray, arrayInFile);
				} else {
					temp.addProperty(key, newVal);
					addNewArrayInJSONFile(jArray, arrayInFile);
				}
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
	 * Verändert den Port aus dem Port-Array mit der
	 * {@link #editValuesFromArray(JsonArray, String, String, String, String)}-Methode.
	 * 
	 * @param oldVal
	 * @param newVal
	 */
	public void editPort(Integer oldVal, Integer newVal) {
		editValuesFromArray(getPortsArray(), "ports", "port", "" + oldVal,
				"" + newVal);
	}

	/**
	 * Verändert den Namen aus dem Port-Array mit der
	 * {@link #editValuesFromArray(JsonArray, String, String, String, String)}-Methode..
	 * 
	 * @param oldVal
	 * @param newVal
	 */
	public void editPortName(String oldVal, String newVal) {
		editValuesFromArray(getPortsArray(), "ports", "name", oldVal, newVal);
	}
	/**
	 * Verändert einzelne Wertepaare aus dem Server-Array mit der
	 * {@link #editValuesFromArray(JsonArray, String, String, String, String)}-Methode..
	 * 
	 * @param key
	 * @param oldVal
	 * @param newVal
	 */
	public void editServer(String key, String oldVal, String newVal) {
		editValuesFromArray(getServerArray(), "server", key, oldVal, newVal);
	}

	// #########################################################################
	// ## Getter und Setter ####################################################
	// #########################################################################
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
