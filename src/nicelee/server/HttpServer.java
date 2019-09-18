package nicelee.server;

import nicelee.global.GlobalConfig;
import nicelee.server.core.SocketServer;

public class HttpServer {

	
	public static void main(String[] args) {
		
		SocketServer server = new SocketServer(GlobalConfig.httpServerPort);
		server.startServer();
	}
}
