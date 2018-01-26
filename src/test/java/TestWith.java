package test.java;

import main.java.model.JSONFileHandler;
import main.java.model.Port;

public class TestWith {

	public static void main(String[] args) {
				
		// ## JSON GSON2 #######################################################
		// jUnit TEST ----------------------------------------------------------
		 JSONFileHandler jFile = new JSONFileHandler();
		 Port portDB = new Port("tes");
		 jFile.addPort(portDB.createPort(1234));
		//
		// jFile.editPortName("tes", "test");
		// jFile.editPort(1234, 5678);
		// jFile.editPort(4567, 9870);

		// Server stage = new Server("Locla-Server");
		// stage.createServerViaIP("10.1.40.117");
		// jFile.addServer(stage);
		////
		// jFile.editServer("name", "Locla-Server", "Local-Server");
		//
		// jFile.deletePort("tes");
		// jFile.deletePort("test");
		//
		// Server test = new Server("test");
		// jFile.addServer(test.createServerViaIP("127.0.0.2"));
		//
		// jFile.deleteServer("test");
		
		// #####################################################################
		// ############################## testen ###############################
		// #####################################################################
		
		
		
	}// ########################### main - Ende #############################

}
