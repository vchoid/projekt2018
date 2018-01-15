package main.java.model;

/**
 * 
 * @author Christoph Kiank
 * @version 0.0.1
 */
public class Port {

	private String name;
	private String port;
	
	public Port() {
		super();
	}
	
	public static Port createPort(String name, String port) {
		Port p = new Port();
		
		p.name = name;
		p.port = port;
		
		return p;
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
