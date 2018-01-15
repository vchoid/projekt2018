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
		Port p = new Port();
		p.name = name;
		p.port = port;
		createSimpleStringProperty(p);
		return p;
	}


	/**
	 * Den Name und den Port in {@link SimpleStringProperty}-Variable speichern
	 * 
	 * @param p
	 */
	private void createSimpleStringProperty(Port p) {
		this.sName = new SimpleStringProperty(p.getName());
		this.sPort = new SimpleStringProperty(p.getPort());

	}
	public SimpleStringProperty getsName() {
		return sName;
	}

	public void setsName(SimpleStringProperty sName) {
		this.sName = sName;
	}

	public SimpleStringProperty getsPort() {
		return sPort;
	}

	public void setsPort(SimpleStringProperty sPort) {
		this.sPort = sPort;
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
