package main.java.model;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javafx.beans.property.SimpleStringProperty;

/**
 * Ein Server-Objekt anlegen. Entweder mit der IP-Adresse oder der Host-Adresse.
 * Der jeweils fehlende Wert wird automatisch ergänzt.
 * 
 * @author Christoph Kiank
 * @version 1.0.0
 */
public class Server {

	private String name;
	private String host;
	private String ip;
	private InetAddress inet;

	public Server(String name) {
		super();
		this.name = name;

	}
	/**
	 * Zum Server-Objekt eine IP-Adresse hinzufügen. Der Host wird automatisch
	 * ermittelt und hinzugefügt.
	 * 
	 * @param ip
	 * @return
	 */
	public Server createServerViaIP(String ip) {
		try {
			inet = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			// TODO löschen
			System.out.print(" -> Unbekannte IP-Adresse");
		}
		this.ip = ip;
		this.host = inet.getHostName();
		return this;
	}
	/**
	 * Zum Server-Objekt ein Host-Adresse hinzufügen. Die IP wird automatisch
	 * ermittelt und hinzugefügt.
	 * 
	 * @param host
	 * @return
	 */
	public Server createServerViaHost(String host) {
		try {
			inet = InetAddress.getByName(host);
		} catch (UnknownHostException e) {
			// TODO löschen
			System.out.print(" -> Unbekannter Host");
		}
		this.host = host;
		this.ip = inet.getHostAddress();
		return this;
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
