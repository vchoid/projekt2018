package test.java;

import main.java.utils.GSONFileHandler;

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
				GSONFileHandler jFile = new GSONFileHandler();
//				jFile.addServerViaHost("Auslieferungsserver","lk101sw0395.ew1intra.de");
				jFile.addPort("tom", "1234");
				jFile.addPort("Chris", "5678");
				jFile.editPort("port", "5678", "1234");
				jFile.editPort("name", "tom", "jens");
				// jFile.addPort("DB3", "1527");
				jFile.addServerViaIP("Local-Serve", "1.1.40.117");
				jFile.editServer("name", "Local-Serve", "neuer Server");
				jFile.editServer("name", "neuer Server", "Local-Server");
				jFile.deletePort("jens");
				jFile.deletePort("Chris");
				jFile.deleteServer("neuer Server");
				jFile.editPort("name", "DB2", "Tomca");
				jFile.editPort("name", "Tomca", "DB2");
				// jFile.deleteServer("Local-Server");
//				jFile.deleteServer("Local-Serve");
//				jFile.deletePort("test");
				// jFile.addPort("DB2", "1527");

				// #####################################################################
			}

}

