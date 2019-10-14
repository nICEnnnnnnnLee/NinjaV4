package nicelee.server.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

import nicelee.common.annotation.Controller;
import nicelee.common.annotation.Value;
import nicelee.function.cloud.mac_remarks.MacsRemark;
import nicelee.function.cloud.upload.FileUploader;
import nicelee.function.ipscan.IpScanner;
import nicelee.global.Global;
import nicelee.global.GlobalConfig;

@Controller(path = "/onliner", note = "在线设备状态相关")
public class ControllerOnlinerFinder {

	@Controller(path = "/status/upload",
			note="[动]刷新在线设备状态并上传")
	public String uploadStatus() {
		IpScanner sanner = new IpScanner() {
			@Override
			public void dealResult(Map<String, String> resultMap) {
				System.out.println("--------");
				File macResult = new File(GlobalConfig.tmpDir, "online-devices.txt");
				if (!macResult.exists()) {
					macResult.getParentFile().mkdir();
				}
				try {
					BufferedWriter writer = new BufferedWriter(new FileWriter(macResult, false));

					// 写入最后更新时间
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					sdf.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
					writer.write(sdf.format(new Date()));
					writer.newLine();

					for (Map.Entry<String, String>  entry : resultMap.entrySet()) {
						writer.write(entry.getKey());
						writer.write(" ");
						String note = Global.macRemark.get(entry.getKey());
						if (note != null) {
							writer.write(note);
						}
						writer.newLine();
					}
					writer.flush();
					writer.close();
					Global.macIP = (ConcurrentHashMap<String, String>) resultMap;
					new Thread(new Runnable() {
						@Override
						public void run() {
							FileUploader.update(GlobalConfig.url_onlineDevices, macResult,
									GlobalConfig.token);
						}
					}).start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		sanner.startScan();
		return "online状态尝试上传";
	}
	
	@Controller(path = "/status/refresh", note="[动]刷新在线设备状态")
	public String refreshStatus() {
		IpScanner sanner = new IpScanner() {
			@Override
			public void dealResult(Map<String, String> resultMap) {
				System.out.println("--------");
				Global.macIP = (ConcurrentHashMap<String, String>) resultMap;
			}
		};
		sanner.startScan();
		return "尝试刷新在线设备状态";
	}

	@Controller(path = "/remarks/refresh",note="[动]刷新Mac备注")
	public String refreshMacRemarks() {
		// 初始化MAC地址的备注
		MacsRemark.refreshRemarks(GlobalConfig.url_markOfMacs, GlobalConfig.token);
		return "尝试获取最新Mac地址";
	}
	
	@Controller(path = "/remarks",note="Mac备注")
	public String reportMacRemarks(BufferedWriter out) {
		try {
			for (Map.Entry<String, String>  entry : Global.macRemark.entrySet()) {
				out.write(entry.getKey());
				out.write(" ");
				String note = Global.macRemark.get(entry.getKey());
				if (note != null) {
					out.write(note);
				}
				out.write("<br/>\r\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Controller(path = "/status",note="在线设备状态")
	public String reportStatus(BufferedWriter out) {
		try {
			out.write("当前在线数： " + Global.macIP.size());
			out.write("<br/>");
			for (Map.Entry<String, String> entry : Global.macIP.entrySet()) {
				out.write(entry.getKey());
				out.write(" ");
				String note = Global.macRemark.get(entry.getKey());
				if (note != null) {
					out.write(note);
				}
				out.write("<br/>\r\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Controller(path = "/test", note="测试")
	public String test(@Value(key = "param") String param, @Value(key = "_t") String time) {
		System.out.println("执行controller - /test?param=" + param + "&_t=" + System.currentTimeMillis() / 1000);
		return "test:" + System.currentTimeMillis() / 1000;
	}
}
