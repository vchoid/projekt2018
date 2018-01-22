package main.java.model;

/**
 * 
 * @author Christoph Kiank
 * @version 0.0.1
 */
public class Port {

	private String name;
	private int port;

	public Port(String name) {
		super();
		this.name = name;
	}

	/**
	 * Ein Port erzeugen.
	 * 
	 * @param name
	 * @param port
	 * @return
	 */
	public Port createPort(int port) {
		this.port = port;
		return this;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPort() {
		return port;
	}
	public String getPortAsString() {
		return "" + getPort();
	}
	public void setPort(int port) {
		this.port = port;
	}

}
