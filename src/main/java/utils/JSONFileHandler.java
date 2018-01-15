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
import com.google.gson.JsonObject;

import main.java.model.EmptyPortServerTemplate;

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
	private JsonArray serverArray;

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
			setServerArray(getJsonObj().getAsJsonArray("server"));
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
	
	
	// ## Getter und Setter ####################################################
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
	public JsonArray getServerArray() {
		return serverArray;
	}
	public void setServerArray(JsonArray serverArray) {
		this.serverArray = serverArray;
	}

}
