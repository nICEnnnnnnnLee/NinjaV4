package nicelee.server;

import nicelee.global.GlobalConfig;
import nicelee.server.core.SocketServer;

public class MainServer {

	final static String version = "V1.0.10";
	public static void main(String[] args) {
		try {
			System.out.println(GlobalConfig.baseDirectory());
		}catch (Exception e) {
		}
		System.out.println("当前版本为 NinjaV4-" + version);
		// 初始化配置
		GlobalConfig.init();
		
//		// 初始化MAC地址的备注
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				MacsRemark.refreshRemarks(GlobalConfig.url_markOfMacs, GlobalConfig.token);
//			}
//		}).start();
		
//		
//		// 开启schedule，周期性上报邻居状态
//		TaskOnlinerFinder task = new TaskOnlinerFinder();
//		task.start(GlobalConfig.onlineStatusReportPeriod);
		
		// 简易的http服务器，用于接收局域网命令
		SocketServer server = new SocketServer(GlobalConfig.httpServerPort);
		server.startServer();
	}
}
