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
	
	private SimpleStringProperty sName;
	
	public Server(String name) {
		super();
		this.sName = new SimpleStringProperty(name);
		this.name = name;
		
	}
	public Server createServerViaIP(String ip) {
		try {
			inet = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			//TODO löschen
			System.out.print(" -> Unbekannter Host");
		}
		this.ip = ip;
		this.host =  inet.getHostName();
		return this;
	}
	public Server createServerViaHost(String host) {
		try {
			inet = InetAddress.getByName(host);
		} catch (UnknownHostException e) {
			//TODO löschen
			System.out.print(" -> Unbekannter Host");
		}
		this.host = host;
		this.ip = inet.getHostAddress();
		return this;
	}
	
	public String getSName() {
		return sName.get();
	}

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
