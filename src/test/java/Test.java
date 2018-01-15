package test.java;

import main.java.model.Port;
import main.java.utils.JSONFileHandler;

public class Test {

	private static String filePath = System.getProperty("user.dir")
			+ "/src/main/resources/Server_Ports.JSON";

	public static void main(String[] args) {

		JSONFileHandler jFile = new JSONFileHandler(filePath);

		Port portDB = new Port();
		jFile.addPort(portDB.createPort("Tom", "2408"));
		
		System.out.println("");
		System.out.println(jFile.getPortElementFrom(4).getName());
	}

}
