package main.java.model;


/**
 * 
 * @author Christoph Kiank
 * @version 0.0.1
 */
public class Server {

	private String name;
	private String hostOrIP;

	public Server(String name, String hostOrIP) {
		super();
		this.name = name;
		this.hostOrIP = hostOrIP;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHostOrIP() {
		return hostOrIP;
	}

	public void setHostOrIP(String hostOrIP) {
		this.hostOrIP = hostOrIP;
	}

}
