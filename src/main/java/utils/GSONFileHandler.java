package main.java.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import main.java.model.EmptyPortServerTemplate;
import main.java.model.Port;



//TODO syso -> l�schen								
//TODO regEx -> bei add und edit Funktionen machen	�
//TODO Javadoc -> �berpr�fen						�
//TODO Funktionsnamen ggf. anpassen					
//TODO jUnit Tests schreiben 						!
//TODO Ecxeption Handling 							!
//TODO Port/Server Klassen -> einbinden

/**
 * 
 * Werte in die Server_Ports.JSON schreiben, �ndern und l�schen.</br>
 * 
 * <p>
 * <b>Methoden:</b>
 * <ul>
 * <li>Einen Porteintrag hinzuf�gen: <b>{@link #addPort(String, String)}</b></li>
 * <li>Einen Porteintrag bearbeiten: <b>{@link #editPort(String, String, String)}</b></li>
 * <li>Einen Porteintrag l�schen: <b>{@link #deletePort(String)}</b></li>
 * <li>Einen Servereintrag via Host hinzuf�gen: <b>{@link #addServerViaHost(String, String)}</b></li>
 * <li>Einen Servereintrag via IP hinzuf�gen: <b>{@link #addServerViaIP(String, String)}</b></li>
 * <li>Einen Servereintrag bearbeiten: <b>{@link #editServer(String, String, String)}</b></li>
 * <li>Einen Servereintrag l�schen: <b>{@link #deleteServer(String)}</b></li>
 * </ul>
 * </p>
 * 
 * @author Christoph Kiank
 * @version 1.0.0
 */

public class GSONFileHandler {

	// ## Variablen ############################################################
	
	// --> Datei-Handling ------------------------------------------------------
	private Gson gson;
	private final static String FILE_PATH = System.getProperty("user.dir")
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
	private InetAddress inet;
	private Boolean success = false;
	// --> Exception-Handling --------------------------------------------------
	private Exception e;

	// ## Konstruktor ##########################################################

	public GSONFileHandler(String path) {
		System.out.println("~~~~~~~~~~~~~~~~ Start ~~~~~~~~~~~~~~~~~");
		
		gson = new Gson();
		readAndParseFileToJsonArrays();
	}
	
	// ## JSON-Datei Methoden ##################################################
	/**
	 * Parst Datei als JSON-Objekt und referenziert ein Array f�r Ports und
	 * eines f�r Server. Wenn Datei nicht vorhanden wird eine neue leere
	 * json-Datei angelegt und diese wird geparst.
	 */
	private void readAndParseFileToJsonArrays() {
		try {
			// Datei �ber einen Stream einlesen
			input = new FileInputStream(getFilePath());
			reader = new BufferedReader(new InputStreamReader(input));
			// Datei als JSON-Objekt einlesen
			setJsonObj(gson.fromJson(reader, JsonObject.class));
			// PortsArray und ServerArray setzen
			setPortsArray(getJsonObj().getAsJsonArray("ports"));
			setServerArray(getJsonObj().getAsJsonArray("server"));
		} catch (FileNotFoundException e) {
			addEmptyJsonFileTemplate();
			readAndParseFileToJsonArrays();
			setE(e);
		}
	}
	/**
	 * Vorlageninhalt f�r leere JSON-Datei.
	 */
	private void addEmptyJsonFileTemplate() {
		writeInFile(emptyPSTemplate.getPortServerTemplate());
	}
	/**
	 * Schreibe Inhalt(Parameter content) in der JSON-Datei und schlie�e den
	 * Writer.
	 * 
	 * @param content
	 */
	private void writeInFile(String content) {
		try {
			out = new FileOutputStream(getFilePath());
			writer = new BufferedWriter(new OutputStreamWriter(out));
			writer.write(content);
			// TODO l�schen!
			System.out.println(" -> gespeichert in Datei.");
			writer.close();
		} catch (IOException e) {
			setE(e);
		}
	}

	// ## Pr�fen auf validen Inhalt ############################################
	/**
	 * Sucht einen Wert anhand des gesetzten Parameters im Array.
	 * 
	 * JSONArray wird durchlaufen, jeder Eintrag wird tempor�r gespeichert und
	 * �berpr�ft ob der gesuchte Parameter sich im Array befindet. Wenn ja,
	 * speichere den Eintrag selbst und die Position, wo sich der Eintrag im
	 * Array befindet und gibt true zur�ck. Ansonsten false.
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
				// TODO l�schen!
				System.out.print(
						"#" + getPositionInArray() + " " + getSearchElement());
				return true;
			}
		}
		return false;
	}
	/**
	 * Pr�ft ob der Port bereits im Array vorhanden ist und gibt ein true
	 * zur�ck.
	 * 
	 * @param name
	 * @param port
	 * @return
	 */
	private Boolean isPortAvailable(Port port) {
		if (isValueInArray(getPortsArray(), "port", port.getPort())
				|| isValueInArray(getPortsArray(), "name", port.getName())) {
			// TODO l�schen !
			System.out.print("\n  -> vorhanden");
			return true;
		}
		return false;

	}
	/**
	 * Pr�ft ob der Server bereits im Array vorhanden ist und gibt eib true
	 * zur�ck.
	 * 
	 * @param name
	 * @param ipOrHost
	 * @return
	 */
	private Boolean isServerAvailable(String name, String ip, String host) {
		if (isValueInArray(getServerArray(), "ip", ip)
				|| isValueInArray(getServerArray(), "name", name)
				|| isValueInArray(getServerArray(), "host", host)) {
			// TODO l�schen !
			System.out.print("\n  -> vorhanden");
			return true;
		}
		return false;
	}

	private void addNewObjectInArray(JsonArray array, JsonObject newObject) {
		// Objekt in Array anf�gen
		array.add(newObject);
	}
	private void addNewArrayInJSONFile(JsonArray array, String key) {
		// Werte in Object anf�gen
		getJsonObj().add(key, array);
		// ver�ndertes Objekt als String in Datei schreiben
		writeInFile(getJsonObj().toString());

	}
	// ## add-Methode ##########################################################
	/**
	 * F�gt das Objekt in das Array ein und schreibt es in die Datei
	 * 
	 * @param array
	 * @param newObject
	 * @param key
	 */
	private void addObjectInArrayAndWriteInFile(JsonArray array,
			JsonObject newObject, String key) {
		System.out.print(newObject);
		addNewObjectInArray(array, newObject);
		// TODO l�schen!
		System.out.print(" -> hinzugef�gt zum Array");
		addNewArrayInJSONFile(array, key);
	}
	/**
	 * F�gt zwei Key-Value Paare f�r das PortsArray hinzu.
	 * 
	 * @param name
	 * @param port
	 * @return
	 */
	private void addPortValues(String name, String port) {
		System.out.println("\n++++++++++++++ HINZUF�GEN ++++++++++++++");
		newPort.addProperty("name", name);
		newPort.addProperty("port", port);
	}
	/**
	 * F�gt drei Key-Value Paare f�r das Server-Array hinzu.
	 * 
	 * @param name
	 * @param host
	 * @param ip
	 * @return
	 */
	private void addServerValues(String name, String host, String ip) {
		System.out.println("\n++++++++++++++ HINZUF�GEN ++++++++++++++");
		newServer.addProperty("name", name);
		newServer.addProperty("host", host);
		newServer.addProperty("ip", ip);
	}
	/**
	 * Ein Port in die Json-Datei schreiben.
	 *
	 * Erstellt ein neues {@link JsonObject} an und f�gt die Parameter hinzu.
	 *
	 * {@link #isValueInArray(JsonArray, String, String)} �berpr�ft ob der Wert
	 * bereits existiert und gibt ein Boolean zur�ck. Wenn er nicht vorhanden
	 * ist soll er ihn zum Array hinzuf�gen und dann in die Datei schreiben.
	 *
	 * 
	 * @param name
	 * @param port
	 */
	public void addPort(Port port) {
		// neues Objekt mit zwei Key-Value-Paare anlegen
		addPortValues(port.getName(), port.getPort());
		// pr�fen ob bereits vorhanden
		if (!isPortAvailable(port)){
			// neuen validen Wert schreiben
			addObjectInArrayAndWriteInFile(getPortsArray(), newPort, "ports");
		}
	}
	/**
	 * * {@link #isValueInArray(JsonArray, String, String)} �berpr�ft ob der
	 * Wert bereits existiert und gibt ein Boolean zur�ck. Wenn er nicht
	 * vorhanden ist soll er ihn zum Array hinzuf�gen und dann in die Datei
	 * schreiben. Der fehlenden Host wird automatisch ermittelt und ebenfalls
	 * hinzugef�gt.
	 * 
	 * @param name
	 * @param port
	 * @throws UnknownHostException
	 */
	public void addServerViaIP(String name, String ip) {
		try {
			inet = InetAddress.getByName(ip);
			addServerValues(name, inet.getHostName(), ip);
			if (!isServerAvailable(name, ip, inet.getHostName())) {
				addObjectInArrayAndWriteInFile(getServerArray(), newServer,
						"server");
			}
		} catch (UnknownHostException e) {
			setE(e);
		}
	}
	/**
	 * 
	 * F�gt ein Server in die ServerDB.JSON mittels des Hosts hinzu. Die IP wird
	 * automatisch erg�nzt,
	 * 
	 * {@link #isValueInJSONArray(String)} �berpr�ft ob der Wert bereits
	 * existiert und gibt ein Boolean zur�ck. Wenn er nicht vorhanden ist soll
	 * er ihn zum Array hinzuf�gen und dann in die Datei schreiben. Der
	 * fehlenden Host wird automatisch ermittelt und ebenfalls hinzugef�gt.
	 * 
	 * 
	 * @param name
	 * @param port
	 * @throws UnknownHostException
	 */
	public void addServerViaHost(String name, String host) {
		try {
			inet = InetAddress.getByName(host);
			addServerValues(name, host, inet.getHostAddress());
			if (!isServerAvailable(name, inet.getHostAddress(), host)) {
				addObjectInArrayAndWriteInFile(getServerArray(), newServer,
						"server");
			}
		} catch (UnknownHostException e) {
			setE(e);
		}
	}
	// ## delete Methoden ######################################################
	private void removeValueFromArray(String name, JsonArray array) {
		System.out.println("\n+++++++++++++++ L�SCHEN ++++++++++++++++");
		if (isValueInArray(array, "name", name)) {
			try {
				array.remove(getPositionInArray());
				// TODO l�schen!
				System.out.print("  -> wurde aus Array gel�scht");
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
	 * L�scht ein Port anhand des Parameters.
	 * 
	 * @param name
	 */
	public void deletePort(String name) {
		removeValueFromArray(name, getPortsArray());
		if (getSuccess()) {
			addNewArrayInJSONFile(getPortsArray(), "ports");
		}
	}
	/**
	 * L�scht ein Server anhand des Parameters.
	 * 
	 * @param name
	 */
	public void deleteServer(String name) {
		removeValueFromArray(name, getServerArray());
		if (getSuccess()) {
			addNewArrayInJSONFile(getServerArray(), "server");
		}
	}

	// ## edit Methoden ########################################################
	/**
	 * 
	 * Ver�ndert einzelne Wertepaare aus dem Portsarray.
	 * 
	 * �berpr�ft zuerst ob der alte Wert existiert. Speichert tempor�r das
	 * Array. �berpr�ft als n�chstes, ob der neue Wert nicht schon vorhanden
	 * ist. Wenn er nicht existiert, dann f�ge den neuen Wert dem Array hinzu
	 * und schreibe das neue Array in die Datei.
	 * 
	 * @param key
	 * @param oldVal
	 * @param newVal
	 */
	public void editPort(String key, String oldVal, String newVal) {
		System.out.println("\n////////////// BEARBEITEN //////////////");
		// �berpr�fen ob der alter Wert �berhaupt existiert
		if (isValueInArray(getPortsArray(), key, oldVal)) {
			JsonObject temp = (JsonObject) getPortsArray()
					.get(getPositionInArray());
			// �berpr�fen ob der neue Wert bereits existiert
			if (!isValueInArray(getPortsArray(), key, newVal)) {
				temp.addProperty(key, newVal);
				addNewArrayInJSONFile(getPortsArray(), "ports");
			} else {
				// TODO l�schen!
				System.out.println("  -> keine doppelten Werte erlaubt");
			}
		} else {
			System.out.print(oldVal);
			System.out.println(" -> nicht gefunden");
		}
	}
	/**
	 * 
	 * Ver�ndert einzelne Wertepaare aus dem Serverarray.
	 * 
	 * �berpr�ft zuerst ob der alte Wert existiert. Speichert tempor�r das
	 * Array. �berpr�ft als n�chstes, ob der neue Wert nicht schon vorhanden
	 * ist. Wenn er nicht existiert, dann f�ge den neuen Wert dem Array hinzu
	 * und schreibe das neue Array in die Datei.
	 * 
	 * @param key
	 * @param oldVal
	 * @param newVal
	 */
	public void editServer(String key, String oldVal, String newVal) {
		System.out.println("\n////////////// BEARBEITEN //////////////");
		// �berpr�fen ob der alter Wert �berhaupt existiert
		if (isValueInArray(getServerArray(), key, oldVal)) {
			JsonObject temp = (JsonObject) getServerArray()
					.get(getPositionInArray());
			// �berpr�fen ob der neue Wert bereits existiert
			if (!isValueInArray(getServerArray(), key, newVal)) {
				temp.addProperty(key, newVal);
				addNewArrayInJSONFile(getServerArray(), "server");
			} else {
				// TODO l�schen!
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

	// --> Datei-Handling ------------------------------------------------------
	public static String getFilePath() {
		return FILE_PATH;
	}
	// --> Exception Handling --------------------------------------------------
	public Exception getE() {
		return e;
	}
	public void setE(Exception e) {
		this.e = e;
	}

}
