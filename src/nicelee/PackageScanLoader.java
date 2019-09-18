package nicelee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nicelee.common.annotation.Controller;

public abstract class PackageScanLoader {

	private static List<Class<?>> clazzList;
	
	public static HashMap<String, Class<?>> dealerClazz;
	public static List<Class<?>> controllerClazzes;
	static {
		clazzList = new ArrayList<>();
		String classes[] = {"nicelee.function.live_recorder.live.impl.RoomDealerBilibili", 
				"nicelee.function.live_recorder.live.impl.RoomDealerDouyu", 
				"nicelee.function.live_recorder.live.impl.RoomDealerHuya", 
				"nicelee.function.live_recorder.live.impl.RoomDealerKuaishou", 
				"nicelee.server.controller.ControllerOnlinerFinder", 
				"nicelee.server.controller.ControllerCloudCmdDealer", 
				"nicelee.server.controller.ControllerLiveRecorder" 
		};
		for(String path: classes) {
			try {
				Class<?> klass = Class.forName(path);
				clazzList.add(klass);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		// 扫描包，加载 parser 类
		dealerClazz = new HashMap<>();
		PackageScanLoader pLoader = new PackageScanLoader() {
			@Override
			public boolean isValid(Class<?> clazz) {
				try {
					String liver = (String)clazz.getField("liver").get(null);
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
				if(clazz.getAnnotation(Controller.class) != null) {
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
	
	public void scanRoot(String packName){
		for(Class<?> clazz: clazzList) {
			if(clazz.getName().startsWith(packName)) {
				isValid(clazz);
			}
		}
	}
}
