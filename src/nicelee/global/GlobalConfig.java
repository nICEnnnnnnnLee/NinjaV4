package nicelee.global;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nicelee.common.util.geoip.IPSeeker;
import nicelee.server.MainServer;

public class GlobalConfig {

	// 谁在窥屏专用
	public static String whoIsOnlinePicFile = "";
	public static byte[] whoIsOnlinePic;
	public static byte[] whoIsOnlineIndex;
	public static String geoIpDir = "D:\\Work\\WinMTR-CN-IP\\";
	
	// douyu 车队id
	public static String douyuMotorcadeId = "";
	
	// 免翻域名
	public static String magnetDomain = "";
	
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

	// QQ 机器人配置
	public static String coolQ_httpApi_Addr = "http://127.0.0.1:5701";
	public static String QQToInform = "xxxxxxxx";
	
	// 邮件发送配置
	public static String mail_senderAddress = "sender@sina.com";
	public static String mail_recipientAddress = "receiver@sina.com";
	public static String mail_senderAccount = "sender@sina.com";
	public static String mail_senderPassword = "xxx";
	
	
	// url-在线设备存放位置  上传
	public static String url_onlineDevices = "";
	// url-Mac地址备注存放位置 下载
	public static String url_markOfMacs = "";
	// url-云端命令存放位置  下载
	public static String url_taskToDo = "";
	// url-云端命令运行结果存放位置 上传
	public static String url_taskReport = "";
	// 用于鉴权
	public static String token = null;
	// 录制完毕校正时间戳后是否删除源文件
	public static boolean deleteOnchecked  = false;
	// 录制完毕是否自动校正时间戳后
	public static boolean autoCheck  = false;


	// 春雨计步postStepUrl
	public static String weixin_step_postStepUrl  = "";
	// 春雨计步refreshCookieUrl
	public static String weixin_step_refreshCookieUrl  = "";

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
					try {
						Field f = GlobalConfig.class.getField(matcher.group(1));
						if(f != null) {
							System.out.printf("  key-->value:  %s --> %s\r\n", matcher.group(1), matcher.group(2));
							if(f.getType().equals(String.class)) {
								f.set(null, matcher.group(2).trim());
							}else if(f.getType().equals(File.class)) {
								f.set(null, new File(matcher.group(2).trim()));
							}else if(f.getType().equals(int.class)) {
								f.set(null, Integer.parseInt(matcher.group(2).trim()));
							}else if(f.getType().equals(boolean.class)) {
								f.set(null, matcher.group(2).trim().equals("true"));
							}else if(f.getType().equals(String[].class)) {
								f.set(null, matcher.group(2).trim().split(","));
							}else {
								System.err.printf("当前尚未配置%s - %s\n", f.getType().toString(), matcher.group(1));
							}
						}
					} catch (Exception e) {
					} 
				}
			}
			// 初始化 谁在窥屏
			IPSeeker.getInstance().init("qqwry.dat", geoIpDir);
			try {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				InputStream in = null;
				try{
					in = GlobalConfig.class.getResource("/resources/favicon.jpg").openStream();
				}catch (Exception e) {
					in = new FileInputStream(whoIsOnlinePicFile);
				}
				byte[] buffer = new byte[2048];
				int len = in.read(buffer);
				while(len >= 0) {
					out.write(buffer, 0, len);
					len = in.read(buffer);
				}
				in.close();
				whoIsOnlinePic = out.toByteArray();
			}catch (Exception e) {
			}
			try {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				InputStream in = null;
				try{
					in = GlobalConfig.class.getResource("/resources/index.html").openStream();
				}catch (Exception e) {
					in = new FileInputStream(new File(configDir, "index.html"));
				}
				byte[] buffer = new byte[2048];
				int len = in.read(buffer);
				while(len >= 0) {
					out.write(buffer, 0, len);
					len = in.read(buffer);
				}
				in.close();
				whoIsOnlineIndex = out.toByteArray();
			}catch (Exception e) {
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
	
	public static String baseDirectory() {
        try {
            String path = ClassLoader.getSystemResource("").getPath();
            if (path == null || "".equals(path))
                return getProjectPath();
            return path;
        } catch (Exception ignored) {
        	return getProjectPath();
        }
    }

	private static String getProjectPath() {
        java.net.URL url = MainServer.class.getProtectionDomain().getCodeSource()
                .getLocation();
        String filePath = null;
        try {
            filePath = java.net.URLDecoder.decode(url.getPath(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (filePath.endsWith(".jar")) {
        	int lastIndex = filePath.lastIndexOf("/");
        	System.out.println(lastIndex);
        	if(lastIndex > -1) {
        		filePath = filePath.substring(0, lastIndex + 1);
        	}else {
        		filePath = filePath.substring(0, filePath.lastIndexOf("\\") + 1);
        	}
        }
        File file = new File(filePath);
        filePath = file.getAbsolutePath();
        return filePath;
    }
}
