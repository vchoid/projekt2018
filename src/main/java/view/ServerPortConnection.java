package main.java.view;

public class ServerPortConnection {

	private String server;
	private String portA;
	private String portB;
	
	public ServerPortConnection(String server, String portA, String portB) {
		super();
		this.server = server;
		this.portA = portA;
		this.portB = portB;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String isPortA() {
		return portA;
	}

	public void setPortA(String portA) {
		this.portA = portA;
	}

	public String isPortB() {
		return portB;
	}

	public void setPortB(String portB) {
		this.portB = portB;
	}
	
	
	
	
}
