package main.java.model;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javafx.beans.property.SimpleStringProperty;

/**
 * 
 * @author Christoph Kiank
 * @version 0.0.1
 */
public class Server {

	private String name;
	private String host;
	private String ip;
	private InetAddress inet;

	public Server() {
		super();
	}

	public Server createServerViaIP(String name, String ip) {
		try {
			inet = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			//TODO löschen
			System.out.print(" -> Unbekannter Host");
		}
		this.name = name;
		this.ip = ip;
		this.host =  inet.getHostName();
		return this;
	}
	public Server createServerViaHost(String name, String host) {
		try {
			inet = InetAddress.getByName(host);
		} catch (UnknownHostException e) {
			//TODO löschen
			System.out.print(" -> Unbekannter Host");
		}
		this.name = name;
		this.host = host;
		this.ip = inet.getHostAddress();
		return this;
	}
	
	/**
	 * Den Name und den Port als {@link SimpleStringProperty}-Variable speichern.
	 * 
	 * @param p
	 */
//	private void createSimpleStringProperty() {
//		this.sName = new SimpleStringProperty(this.name);
//		this.sPort = new SimpleStringProperty(this.port);
//
//	}
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	

}
