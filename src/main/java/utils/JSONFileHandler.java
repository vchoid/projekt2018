package main.java.utils;

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

import javafx.beans.property.SimpleStringProperty;
import main.java.model.EmptyPortServerTemplate;
import main.java.model.Port;

public class JSONFileHandler {

	// ## Variablen ############################################################

	// --> Datei-Handling ------------------------------------------------------
	private Gson gson;
	private String filePath = System.getProperty("user.dir")
			+ "/src/main/resources/Server_Ports.JSON";
	private BufferedReader reader;
	private FileInputStream input;
	private BufferedWriter writer;
	private FileOutputStream out;
	private EmptyPortServerTemplate emptyPSTemplate = new EmptyPortServerTemplate();
	// --> json-Handling -------------------------------------------------------
	private JsonObject jsonObj;
	private JsonArray portsArray;
	private JsonElement searchElement;
	private int positionInArray;

	
	// --> add Methoden --------------------------------------------------------
	private JsonObject newPort = new JsonObject();

	// --> Exception-Handling --------------------------------------------------

	// ## Konstruktor ##########################################################
	/**
	 * Holt den Dateipfad und legt ein neues GSON-Objekt an.
	 * 
	 * @param path
	 */
	public JSONFileHandler(String path) {
		filePath = path;
		gson = new Gson();
		readAndParseJSONFileToJsonArray();
	}

	// ## JSON-Datei Methoden ##################################################
	/**
	 * Parst Datei als JSON-Objekt und referenziert ein Array für Ports und
	 * eines für Server. Wenn Datei nicht vorhanden wird eine neue leere
	 * json-Datei angelegt und diese wird geparst.
	 */
	private void readAndParseJSONFileToJsonArray() {
		try {
			// Datei über einen Stream einlesen
			input = new FileInputStream(filePath);
			reader = new BufferedReader(new InputStreamReader(input));
			// Inhalt als JSONObject referenzieren
			setJsonObj(gson.fromJson(reader, JsonObject.class));
			// PortsArray und ServerArray setzen
			setPortsArray(getJsonObj().getAsJsonArray("ports"));
			// setServerArray(getJsonObj().getAsJsonArray("server"));
		} catch (FileNotFoundException e) {
			addEmptyJsonFileTemplate();
			readAndParseJSONFileToJsonArray();
		}
	}
	/**
	 * Schreibe Inhalt(content) in der JSON-Datei und schließe den Writer.
	 * 
	 * @param content
	 */
	private void writeInFile(String content) {
		try {
			out = new FileOutputStream(filePath);
			writer = new BufferedWriter(new OutputStreamWriter(out));
			writer.write(content);
			// TODO löschen!
			System.out.print(" -> gespeichert in Datei.");
			writer.close();
		} catch (IOException e) {

		}
	}
	/**
	 * Vorlageninhalt für leere JSON-Datei.
	 */
	private void addEmptyJsonFileTemplate() {
		writeInFile(emptyPSTemplate.getPortServerTemplate());
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
	// --> Port ----------------------------------------------------------------
	private Boolean isPortAvailable(Port port) {
		if (isValueInArray(getPortsArray(), "port", port.getPort())
				|| isValueInArray(getPortsArray(), "name", port.getName())) {
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
	// --> add Port ------------------------------------------------------------
	/**
	 * Fügt zwei Key-Value Paare dem PortsArray hinzu.
	 * 
	 * @param name
	 * @param port
	 * @return
	 */
	private void addPortValues(Port port) {
		System.out.println("\n++++++++++++++ HINZUFÜGEN ++++++++++++++");
		newPort.addProperty("name", port.getName());
		newPort.addProperty("port", port.getPort());
	}
	/**
	 * Ein Port in die Json-Datei schreiben.
	 *
	 * Erstellt ein neues {@link JsonObject} an und fügt die Parameter hinzu.
	 *
	 * {@link #isValueInArray(JsonArray, String, String)} überprüft ob der Wert
	 * bereits existiert und gibt ein Boolean zurück. Wenn er nicht vorhanden
	 * ist soll er ihn zum Array hinzufügen und dann in die Datei schreiben.
	 *
	 * 
	 * @param name
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

	// ## Getter und Setter ####################################################
	/**
	 * Werte aus dem Port Array an einer bestimmten Position in ein neues Port
	 * Objekt speichern.
	 * 
	 * @param posInArray
	 * @return
	 */
	public Port getValueAndSetAsSimpleProperty(int posInArray) {
		Port p = new Port();
		try {
			JsonObject temp = new JsonObject();
			temp = getPortsArray().get(posInArray).getAsJsonObject();
			p.setName(temp.get("name").getAsString());
			p.setPort(temp.get("port").getAsString());
		} catch (IndexOutOfBoundsException e) {
			// TODO: handle exception

		}
		return p;
	}
	
	// --> Content-Handling ----------------------------------------------------
	public String getFilePath() {
		return filePath;
	}
	public JsonObject getJsonObj() {
		return jsonObj;
	}
	public void setJsonObj(JsonObject jsonObj) {
		this.jsonObj = jsonObj;
	}
	public JsonArray getPortsArray() {
		return portsArray;
	}
	public void setPortsArray(JsonArray portsArray) {
		this.portsArray = portsArray;
	}

	public JsonElement getSearchElement() {
		return searchElement;
	}

	public void setSearchElement(JsonElement searchElement) {
		this.searchElement = searchElement;
	}

	public int getPositionInArray() {
		return positionInArray;
	}

	public void setPositionInArray(int positionInArray) {
		this.positionInArray = positionInArray;
	}

}
