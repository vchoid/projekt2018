package test.java;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import main.java.model.JSONFileHandler;

public class TestConnection {

	public static void main(String[] args) {

		JSONFileHandler jFile = new JSONFileHandler();
		ArrayList connectArray = new ArrayList();
		int port = 0;
		String ip = " XXX ";

		for (int i = 0; i < jFile.getPortList().size(); i++) {
			port = Integer.parseInt(jFile.getPortList().get(i));
			System.out.println(port);
			ArrayList temp = new ArrayList();
			for (int j = 0; j < jFile.getIpList().size(); j++) {
				ip = jFile.getIpList().get(j);
				System.out.print(" -" + ip);
				try {
					Socket s = new Socket(ip, port);
					System.out.println(" -> " + s.isConnected());
					temp.add(ip + ":" + port);
				} catch (UnknownHostException e) {
					System.out.println("Unbekanner Host");
				} catch (IOException e) {
					System.out.println(" XXX ");
					temp.add(" XXX ");
				}
			}
			connectArray.add(temp);
		}
		System.out.println(connectArray.get(1));
		
	}// ## ENDE MAIN #####################################################

}
