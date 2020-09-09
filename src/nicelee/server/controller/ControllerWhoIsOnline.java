package nicelee.server.controller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import nicelee.common.annotation.Controller;
import nicelee.common.annotation.Value;
import nicelee.common.util.geoip.IPLocation;
import nicelee.common.util.geoip.IPSeeker;
import nicelee.global.GlobalConfig;

/***
 * 
 * 基本上已废弃
 * 记录访问者的ip 简易实现 “谁在窥屏”功能
 *
 *https://connect.qq.com/widget/shareqq/index.html?url=https://www.qq.com/&share_source=qq&share_medium=web&desc=1&title=谁在窥屏&summary=谁在窥屏&pics=http://127.0.0.1:8888/whoisonline/favicon.jpg&flash=&site=&style=201&width=32&height=32
 */
@Controller(path = "/whoisonline", note = "记录谁在窥屏")
public class ControllerWhoIsOnline {

	static ConcurrentHashMap<String, Long> map = new ConcurrentHashMap<>();

	@Controller(path = "/favicon.jpg", note = "返回icon，并记录ip")
	public String whoisonlineFavicon(OutputStream outRaw, @Value(key = "ipData") String ipData,
			@Value(key = "id") String id) {
		IPLocation loc = IPSeeker.getInstance().getIPLocation(ipData);
		String info = String.format("%s : %s - %s", ipData, loc.getCountry(), loc.getArea());
		map.put(info, System.currentTimeMillis());
		try {
			outRaw.write(GlobalConfig.whoIsOnlinePic);
			outRaw.flush();
		} catch (IOException e) {
		}
		return null;
	}

	@Controller(path = "/clear", note = "清空记录")
	public String clear(BufferedWriter out) {
		map.clear();
		return null;
	}

	@Controller(path = "/info", note = "查看访问的ip信息")
	public String query(BufferedWriter out) {
		Enumeration<String> enumeration = map.keys();
		try {
			while (enumeration.hasMoreElements()) {
				out.write(enumeration.nextElement());
				out.write("<br/>\r\n");
			}
		} catch (IOException e) {
		}
		return null;
	}
	
	@Controller(path = "/index", note = "待分享的页面")
	public String index(OutputStream outRaw, @Value(key = "ipData") String ipData,
			@Value(key = "id") String id) {
		IPLocation loc = IPSeeker.getInstance().getIPLocation(ipData);
		String info = String.format("%s : %s - %s", ipData, loc.getCountry(), loc.getArea());
		map.put(info, System.currentTimeMillis());
		try {
			outRaw.write(GlobalConfig.whoIsOnlineIndex);
			outRaw.flush();
		} catch (IOException e) {
		}
		return null;
	}

}
