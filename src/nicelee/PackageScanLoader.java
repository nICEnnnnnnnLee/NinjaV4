package nicelee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mazaiting.ParseDexMain;
import com.mazaiting.ParseDexUtil;
import com.mazaiting.struct.TypeIdsItem;

import nicelee.common.annotation.Controller;
import nicelee.global.GlobalConfig;

public abstract class PackageScanLoader {

	public static HashMap<String, Class<?>> dealerClazz;
	public static List<Class<?>> controllerClazzes;
	static {
		System.out.println("Dex程序所在位置: ");
		System.out.println(GlobalConfig.dexPath);
		// dex 包扫描（需要确保该包加载在配置读取之后）
		byte[] dexBytes = ParseDexMain
				.getDexFromDisk(GlobalConfig.dexPath);
		ParseDexUtil.parseDexHeader(dexBytes);
		ParseDexUtil.parseStringIds(dexBytes);
		ParseDexUtil.parseStringList(dexBytes);
		ParseDexUtil.parseTypeIds(dexBytes);

		// 扫描包，加载 parser 类
		dealerClazz = new HashMap<>();
		PackageScanLoader pLoader = new PackageScanLoader() {
			@Override
			public boolean isValid(Class<?> clazz) {
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
		controllerClazzes = new ArrayList<Class<?>>();
		pLoader = new PackageScanLoader() {
			@Override
			public boolean isValid(Class<?> clazz) {
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
	 * Class 类型是否符合预期，如果是，则加入列表
	 * 
	 * @param klass
	 * @return
	 */
	public abstract boolean isValid(Class<?> clazz);

	public void scanRoot(String packName) {
		// 这里的descriptor_idx就是解析之后的字符串中的索引值
		for (TypeIdsItem item : ParseDexUtil.typeIdsList) {
			String classNameWithoutDot = ParseDexUtil.stringList.get(item.descriptorIdx);
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
}
