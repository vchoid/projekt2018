package main.java.model;

/**
 * 
 * @author Christoph Kiank
 * @version 0.0.1
 */
public class Port {

	private String name;
	private String port;

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
	public Port createPort(String port) {
		this.port = port;
		return this;
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
