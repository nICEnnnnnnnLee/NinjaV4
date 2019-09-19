package nicelee.global;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GlobalConfig {

	// http服务器监听端口
	public static int httpServerPort = 8888;
	
	// dex包路径
	public static String dexPath = "ninjaV4.dex";

	// 命令获取周期(min)
	public static int taskPeriod = 5;
	
	// 配置文件存储位置
	final public static File configDir = new File("config/");
	// 临时文件存储位置
	final public static File tmpDir = new File("tmp/");
	// 下载文件存储位置
	public static File downloadDir = new File("download/");

	// url-在线设备存放位置  上传
	public static String url_onlineDevices = null;
	// url-Mac地址备注存放位置 下载
	public static String url_markOfMacs = null;
	// url-云端命令存放位置  下载
	public static String url_taskToDo = null;
	// url-云端命令运行结果存放位置 上传
	public static String url_taskReport = null;
	// 用于鉴权
	public static String token = null;
	// 用于鉴权
	public static boolean deleteOnchecked  = false;

	// 扫描前缀
	public static String[] ipPrefixs = { "192.168.0.", "192.168.1." };

	final static Pattern patternConfig = Pattern.compile("^[ ]*([0-9|a-z|A-Z|.|_]+)[ ]*=[ ]*([^ ]+.*$)");

	public static void init() {
		// 从配置文件读取
		BufferedReader buReader = null;
		File file = new File(configDir, "app.config");
		System.out.println("----Read config from " + file.getAbsolutePath());
		System.out.println("----Config init begin...----");
		try {
			buReader = new BufferedReader(new FileReader(file));
			String config;
			while ((config = buReader.readLine()) != null) {
				Matcher matcher = patternConfig.matcher(config);
				if (matcher.find()) {
					switch (matcher.group(1)) {
					case "httpServerPort":
						httpServerPort = Integer.parseInt(matcher.group(2).trim());
						break;
					case "taskPeriod":
						taskPeriod = Integer.parseInt(matcher.group(2).trim());
						break;
					case "downloadDir":
						downloadDir = new File(matcher.group(2).trim());
						break;
					case "url_onlineDevices":
						url_onlineDevices = matcher.group(2).trim();
						break;
					case "url_markOfMacs":
						url_markOfMacs = matcher.group(2).trim();
						break;
					case "url_taskToDo":
						url_taskToDo = matcher.group(2).trim();
						break;
					case "url_taskReport":
						url_taskReport = matcher.group(2).trim();
						break;
					case "token":
						token = matcher.group(2).trim();
						break;
					case "deleteOnchecked":
						deleteOnchecked = matcher.group(2).trim().equals("true");
						break;
					case "ipPrefixs":
						ipPrefixs = matcher.group(2).trim().split(",");
						break;
					case "dexPath":
						dexPath = matcher.group(2).trim();
						break;
					default:
						break;
					}
					System.out.printf("  key-->value:  %s --> %s\r\n", matcher.group(1), matcher.group(2));
				}
			}
		} catch (IOException e) {
			// e.printStackTrace();
		} finally {
			try {
				buReader.close();
			} catch (Exception e) {
			}
		}
		System.out.println("----Config ini end...----");
		checkNprint();
	}
	
	public static void checkNprint() {
		for(Field field: GlobalConfig.class.getFields()) {
			try {
				Object obj = field.get(null);
				if(obj instanceof File) {
					File file = (File)obj;
					System.out.printf("%s : %s\n", field.getName(), file.getAbsolutePath());
				}else if(obj instanceof String[]) {
					
				}else {
					if(obj == null) {
						System.out.printf("%s 必须配置！！！",field.getName());
						System.exit(1);
					}
					System.out.printf("%s : ",field.getName());
					System.out.println(obj);
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
}
