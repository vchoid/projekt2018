package test.java;

import main.java.utils.JSONFileHandler;

public class Test {

	private static String filePath = System.getProperty("user.dir")
			+ "/src/main/resources/Server_Ports.JSON";
	
	
	public static void main(String[] args) {
		
		JSONFileHandler jFile = new JSONFileHandler(filePath);
		System.out.println(jFile.getPortsArray());
		
	}
	
}
