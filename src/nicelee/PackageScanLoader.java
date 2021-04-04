package nicelee;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import nicelee.common.annotation.Controller;
import nicelee.global.GlobalConfig;

public abstract class PackageScanLoader {

	public static HashMap<String, java.lang.Class<?>> dealerClazz;
	public static List<java.lang.Class<?>> controllerClazzes;
	
	public static boolean isJVM = true;
	static {
		String vmName = System.getProperty("java.vm.name").toLowerCase();
		//System.out.println(vmName);
		if(vmName.startsWith("dalvik")) {
			isJVM = false;
			//System.out.println("Dex程序所在位置: ");
			//System.out.println(GlobalConfig.dexPath);
			// dex 包扫描（需要确保该包加载在配置读取之后）
			byte[] dexBytes = com.mazaiting.ParseDexMain
					.getDexFromDisk(GlobalConfig.dexPath);
			com.mazaiting.ParseDexUtil.parseDexHeader(dexBytes);
			com.mazaiting.ParseDexUtil.parseStringIds(dexBytes);
			com.mazaiting.ParseDexUtil.parseStringList(dexBytes);
			com.mazaiting.ParseDexUtil.parseTypeIds(dexBytes);
		}else {
			//System.out.println("非Android平台");
			isJVM = true;
		}
		
		// 扫描包，加载 parser 类
		dealerClazz = new HashMap<>();
		PackageScanLoader pLoader = new PackageScanLoader() {
			@Override
			public boolean isValid(java.lang.Class<?> clazz) {
				try {
					String liver = (String) clazz.getField("liver").get(null);
					dealerClazz.put(liver, clazz);
				} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
						| SecurityException e) {
					e.printStackTrace();
				}
				return false;
			}
		};
		pLoader.scanRoot("nicelee.function.live_recorder.live.impl");

		// 扫描包，加载 controller 类
		controllerClazzes = new ArrayList<java.lang.Class<?>>();
		pLoader = new PackageScanLoader() {
			@Override
			public boolean isValid(java.lang.Class<?> clazz) {
				if (clazz.getAnnotation(Controller.class) != null) {
					// System.out.println(clazz.getName());
					controllerClazzes.add(clazz);
				}
				return true;
			}
		};
		pLoader.scanRoot("nicelee.server.controller");
	}

	/**
	 * java.lang.Class 类型是否符合预期，如果是，则加入列表
	 * 
	 * @param klass
	 * @return
	 */
	public abstract boolean isValid(java.lang.Class<?> clazz);

	public void scanRoot(String packName) {
		if(isJVM) {
			scanRootWithJVM(packName);
		}else {
			scanRootWithDalvik(packName);
		}
	}
	
	private void scanRootWithDalvik(String packName) {
		// 这里的descriptor_idx就是解析之后的字符串中的索引值
		for (com.mazaiting.struct.TypeIdsItem item : com.mazaiting.ParseDexUtil.typeIdsList) {
			String classNameWithoutDot = com.mazaiting.ParseDexUtil.stringList.get(item.descriptorIdx);
			String pNameWithoutDot = "L" + packName.replace(".", "/");
			if(classNameWithoutDot.startsWith(pNameWithoutDot) && !classNameWithoutDot.contains("$")) {
				String className = classNameWithoutDot.substring(1, classNameWithoutDot.length() -1).replace("/", ".");
				System.out.println(className);
				try {
					isValid(Class.forName(className));
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	private void scanRootWithJVM(String packNameWithDot) {
		String packNameWithFileSep = packNameWithDot.replace("\\", "/").replace(".", "/");
		packNameWithDot = packNameWithDot.replace("/", ".");
		try {
			Enumeration<URL> url = Thread.currentThread().getContextClassLoader().getResources(packNameWithFileSep);
			while (url.hasMoreElements()) {
				URL currentUrl = url.nextElement();
				String type = currentUrl.getProtocol();
				if (type.equals("jar")) { // jar 包
					dealWithJar(currentUrl, packNameWithFileSep);
				} else if (type.equals("file")) { // file
					File file = new File(currentUrl.toURI());
					if (file.isDirectory()) { // 目录
						dealWithFolder(packNameWithDot, file);
					} else if (file.getName().endsWith(".class")) {
						deaWithJavaClazzFile(packNameWithDot, file);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	// JVM - 处理目录文件
	private void dealWithFolder(String packNameWithDot, File file) {
		if (file.exists()) {
			// file一定是目录型文件所以得到该目录下所有文件遍历它们
			File[] files = file.listFiles();
			for (File childfile : files) {
				// 如果子文件是目录，则递归处理，调用本方法递归。
				if (childfile.isDirectory()) {
					// 注意递归时候包名字要加上".文件名"后为新的包名
					// 因为后面反射时需要类名，也就是com.mec.***
					dealWithFolder(packNameWithDot + "." + childfile.getName(), childfile);
				} else {
					// 如果该文件不是目录。
					String name = childfile.getName();
					// 该文件是class类型
					if (name.contains(".class")) {
						deaWithJavaClazzFile(packNameWithDot, childfile);
					} else {
						continue;
					}
				}
			}
		} else {
			return;
		}
	}

	// JVM - 处理class类型
	private void deaWithJavaClazzFile(String packNameWithDot, File file) {
		int index = file.getName().lastIndexOf(".class");
		String filename = file.getName().substring(0, index);
		java.lang.Class<?> klass = null;
		try {
			klass = Class.forName(packNameWithDot + "." + filename);
			isValid(klass);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	// JVM - 处理jar包类型
	private void dealWithJar(URL url, String packNameWithFileSep) {
		JarURLConnection jarURLConnection;
		try {
			jarURLConnection = (JarURLConnection) url.openConnection();
			JarFile jarFile = jarURLConnection.getJarFile();
			Enumeration<JarEntry> jarEntries = jarFile.entries();

			while (jarEntries.hasMoreElements()) {
				JarEntry jar = jarEntries.nextElement();
				if (jar.isDirectory() || !jar.getName().endsWith(".class") || !jar.getName().startsWith(packNameWithFileSep)) {
					continue;
				}
				// 处理class类型
				String jarName = jar.getName();
				int dotIndex = jarName.indexOf(".class");
				String className = jarName.substring(0, dotIndex).replace("/", ".");
				java.lang.Class<?> klass = Class.forName(className);
				isValid(klass);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
