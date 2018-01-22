package test.java;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerSockets {

	public static void main(String[] args)
			throws UnknownHostException, IOException {

		Socket s = new Socket("lk101sw0395.ew1intra.de", 8080);
		// --> verbunden Funktion ----------------------
		System.out.println("isConnected: "); // true
		System.out.println(" -> " + s.isConnected());
		// ---------------------------------------------
		System.out.println("getRemoteSocketAddress: "); // lk101sw0396.ew1intra.de/10.33.246.212:8080
		System.out.println(" -> " + s.getRemoteSocketAddress());
		System.out.println("getLocalSocketAddress: "); // /10.1.40.117:63666
		System.out.println(" -> " + s.getLocalSocketAddress());
		System.out.println("getLocalPort: "); // 63666
		System.out.println(" -> " + s.getLocalPort());
		System.out.println("getLocalAddress: "); // /10.1.40.117
		System.out.println(" -> " + s.getLocalAddress());
		System.out.println("getPort: "); // 8080
		System.out.println(" -> " + s.getPort());
		System.out.println("getKeepAlive: "); // false
		System.out.println(" -> " + s.getKeepAlive());
		System.out.println("getOOBInline: "); // false
		System.out.println(" -> " + s.getOOBInline());
		System.out.println("getReuseAddress: "); // false
		System.out.println(" -> " + s.getReuseAddress());
		System.out.println("getReceiveBufferSize: "); // 8192
		System.out.println(" -> " + s.getReceiveBufferSize());
		System.out.println("getSendBufferSize: "); // 64512
		System.out.println(" -> " + s.getSendBufferSize());
		System.out.println("getSoLinger: "); // –1
		System.out.println(" -> " + s.getSoLinger());
		System.out.println("getTcpNoDelay: "); // false
		System.out.println(" -> " + s.getTcpNoDelay());
		System.out.println("getTrafficClass: "); // 0
		System.out.println(" -> " + s.getTrafficClass());

		System.out.println(
				"-------------------------------------------------------------");

		InetAddress inet = InetAddress.getByName("lk101sw0397.ew1intra.de");
		// InetAddress inet = InetAddress.getByName("10.33.246.212");
//		InetAddress inet = InetAddress.getByName("mwg7.prodsrz.srzintra.de");
		System.out.println("isReachable");
		System.out.println("  -> " + inet.isReachable(1));
		System.out.println("getHostName");
		System.out.println("  -> " + inet.getHostName());
		System.out.println("getHostAddress");
		System.out.println("  -> " + inet.getHostAddress());
	}

}
