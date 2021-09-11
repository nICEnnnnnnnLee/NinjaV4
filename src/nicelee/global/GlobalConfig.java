package nicelee.global;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nicelee.server.MainServer;

public class GlobalConfig {

	// http服务器监听端口
	public static int httpServerPort = 8888;

	// dex包路径
	public static String dexPath = "./ninjaV4.dex";

	final static Pattern patternConfig = Pattern.compile("^[ ]*([0-9|a-z|A-Z|.|_]+)[ ]*=[ ]*([^ ]+.*$)");

	public static void init() {
		// 从配置文件读取
		BufferedReader buReader = null;
		File file = new File(new File(dexPath).getParent(), "app.config");
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
						if (f != null) {
							System.out.printf("  key-->value:  %s --> %s\r\n", matcher.group(1), matcher.group(2));
							if (f.getType().equals(String.class)) {
								f.set(null, matcher.group(2).trim());
							} else if (f.getType().equals(File.class)) {
								f.set(null, new File(matcher.group(2).trim()));
							} else if (f.getType().equals(int.class)) {
								f.set(null, Integer.parseInt(matcher.group(2).trim()));
							} else if (f.getType().equals(boolean.class)) {
								f.set(null, matcher.group(2).trim().equals("true"));
							} else if (f.getType().equals(String[].class)) {
								f.set(null, matcher.group(2).trim().split(","));
							} else {
								System.err.printf("当前尚未配置%s - %s\n", f.getType().toString(), matcher.group(1));
							}
						}
					} catch (Exception e) {
					}
				}
			}
//			IPSeeker.getInstance().init("qqwry.dat", geoIpDir);
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
		for (Field field : GlobalConfig.class.getFields()) {
			try {
				Object obj = field.get(null);
				if (obj instanceof File) {
					File file = (File) obj;
					System.out.printf("%s : %s\n", field.getName(), file.getAbsolutePath());
				} else if (obj instanceof String[]) {

				} else {
					if (obj == null) {
						System.out.printf("%s 必须配置！！！", field.getName());
//						System.exit(1);
					}
					System.out.printf("%s : ", field.getName());
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
		java.net.URL url = MainServer.class.getProtectionDomain().getCodeSource().getLocation();
		String filePath = null;
		try {
			filePath = java.net.URLDecoder.decode(url.getPath(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (filePath.endsWith(".jar")) {
			int lastIndex = filePath.lastIndexOf("/");
			System.out.println(lastIndex);
			if (lastIndex > -1) {
				filePath = filePath.substring(0, lastIndex + 1);
			} else {
				filePath = filePath.substring(0, filePath.lastIndexOf("\\") + 1);
			}
		}
		File file = new File(filePath);
		filePath = file.getAbsolutePath();
		return filePath;
	}
}
