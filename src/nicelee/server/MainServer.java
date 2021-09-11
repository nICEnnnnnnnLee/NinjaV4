package nicelee.server;

import nicelee.global.GlobalConfig;
import nicelee.server.core.SocketServer;

public class MainServer {

	final static String version = "V2.0.0";
	static SocketServer server;

	public static void main(String[] args) {
		start();
	}

	public static void start() {
		GlobalConfig.dexPath = System.getProperty("ninja.dex.path", GlobalConfig.dexPath);
		System.out.println("-- NinjaV4 --" + version);
		// 初始化配置
		GlobalConfig.init();

		// 简易的http服务器，用于接收局域网命令
		server = new SocketServer(GlobalConfig.httpServerPort);
		server.startServer();
	}

	public static void stop() {
		server.stopServer();
		server = null;
		System.out.println("- NinjaV4 Dex Service Stoped -");
	}
}
