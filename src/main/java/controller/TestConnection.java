package main.java.controller;

import java.io.IOException;
import java.net.Socket;

import main.java.model.Port;
import main.java.model.Server;

public class TestConnection {

	
	
	/**
	 * Verbindung von Server mit Port testen.
	 */
	public boolean testServerPortConnection(String server, int port) {
		try {
			Socket s = new Socket(server, port);
			s.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	
}
