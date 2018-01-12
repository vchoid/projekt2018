package main.java.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.InetAddress;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import main.java.model.EmptyPortServerTemplate;

/**
 * 
 * @author Christoph Kiank
 * @version 0.0.1
 */
public class JSONFileHandler {

	// ## Variablen ############################################################

	// ->> Datei-Handling <<----------------------------------------------------
	private Gson gson;
	private final static String FILE = System.getProperty("user.dir")
			+ "/src/main/resources/Server_Ports.JSON";
	private BufferedReader reader;
	private FileInputStream input;
	private BufferedWriter writer;
	private FileOutputStream out;

	// ->> JSON-Handling <<-----------------------------------------------------
	private EmptyPortServerTemplate emptyPSTemplate = new EmptyPortServerTemplate();
	private JsonObject jsonFileObject;
	private JsonObject portObject = new JsonObject();
	private JsonObject serverObject = new JsonObject();
	private JsonArray portsArray;
	private JsonArray serverArray;
	private int positionInArray;
	private JsonElement searchElement;
	// ->> Server-Handling <<---------------------------------------------------
	private InetAddress inet;
	// ->> Boolean-handling <<--------------------------------------------------
	private Boolean success = false;
	
	// ->> Exception-Handling <<------------------------------------------------

}
