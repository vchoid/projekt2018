package main.java.model;

import javafx.beans.property.SimpleStringProperty;

/**
 * 
 * @author Christoph Kiank
 * @version 0.0.1
 */
public class Port {

	private String name;
	private String port;

	private SimpleStringProperty sName;
	private SimpleStringProperty sPort;

	public Port() {
		super();
	}

	/**
	 * Ein Port-Element erzeugen, die Elemente zusätzlich als
	 * {@link SimpleStringProperty}-Variablen speichern und den Port zurück
	 * geben.
	 * 
	 * @param name
	 * @param port
	 * @return
	 */
	public Port createPort(String name, String port) {
		this.name = name;
		this.port = port;
		createSimpleStringProperty();
		System.out.println(sName);
		System.out.println(sPort);
		return this;
	}
	/**
	 * Den Name und den Port als {@link SimpleStringProperty}-Variable speichern.
	 * 
	 * @param p
	 */
	private void createSimpleStringProperty() {
		this.sName = new SimpleStringProperty(this.name);
		this.sPort = new SimpleStringProperty(this.port);

	}
	public String getsName() {
		return sName.get();
	}

	public String getsPort() {
		return sPort.get();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}

}
