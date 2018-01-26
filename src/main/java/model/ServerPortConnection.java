package main.java.model;

import java.io.IOException;
import java.net.Socket;

public class ServerPortConnection {

	private String server;
	private boolean[] port;
	
	public ServerPortConnection(String server, boolean[] port) {
		super();
		this.server = server;
		this.port = port;
	}

	/**
	 * Verbindung von Server mit Port testen.
	 */
	public boolean testServerPortConnection(Server server, Port port) {
		try {
			Socket s = new Socket(server.getIp(), port.getPort());
			s.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	
	/**
	 * @return the server
	 */
	public String getServer() {
		return server;
	}

	/**
	 * @return the portA
	 */
	public boolean[] getPort() {
		return port;
	}

	
	

	

	
	
	
	
	
}
