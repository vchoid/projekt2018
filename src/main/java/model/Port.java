package main.java.model;

/**
 * 
 * @author up40008
 *
 */
public class Port {

	private String name;
	private String port;
	
	public Port(String name, String port) {
		super();
		this.name = name;
		this.port = port;
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
