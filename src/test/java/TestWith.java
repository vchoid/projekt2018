package test.java;

import main.java.model.JSONFileHandler;
import main.java.model.Port;
import main.java.model.Server;

public class TestWith {

	public static void main(String[] args) {

		// ## JSON SIMPLE #####################################################
				// JSONSimpleFileHandler port = new JSONSimpleFileHandler("PortsDB");
				// port.addPort("RemoteServer", "8084");
				// port.addPort("Tomcat", "8080");
				// port.addPort("FirstSpirit", "8000");
				// port.addPort("SE", "2409");
				// port.addPort("SQl", "1234");
				// port.addPort("DB", "1520");
				// port.editValue("port", "1520", "1500");
				// port.addPort("DB2", "1527");
				// port.deleteJSONObject("SQL");
				// port.deleteJSONObject("SQl");
				// port.editValue("port", "15", "1527");
				// port.deleteJSONObject("DB");
				//
				// JSONSimpleFileHandler server = new JSONSimpleFileHandler("ServerDB");
				// server.addServerViaIP("PreProd-Server", "10.33.246.213");
				// server.addServerViaHost("Staging-Server", "lk101sw0396.ew1intra.de");
				// server.addServerViaIP("Auslieferungsserver", "10.33.246.211");
				// server.addServerViaIP("Local-Server", "10.1.40.117");
				// server.addServerViaIP("Test-Server", "1.1.1.1");
				// server.editValue("ip", "1.1.1.1", "1.2.3.4");
				// server.deleteJSONObject("Test-Server");
				// #####################################################################

				// ## JSON GSON2
				// ########################################################
				// jUnit TEST ----------------------------------------------------------
				// jUnit TEST ----------------------------------------------------------
				// jUnit TEST ----------------------------------------------------------
				JSONFileHandler jFile = new JSONFileHandler();
				Port portDB = new Port("tes");
				jFile.addPort(portDB.createPort("1234"));
				
				jFile.editPortName("tes", "test");
				jFile.editPort("1234", "5678");
				jFile.editPort("4567", "9870");
				
				
				Server stage = new Server("Locla-Server");
				stage.createServerViaIP("10.1.40.117");
				jFile.addServer(stage);
				
				jFile.editServer("name", "Locla-Server", "Local-Server");
				
				jFile.deletePort(portDB);
				

				// #####################################################################
			}

}

