package nicelee.global;

import java.util.concurrent.ConcurrentHashMap;

public class Global {

	// 用于存放<Mac地址，备注>
	public static ConcurrentHashMap<String, String> macRemark = new ConcurrentHashMap<String, String>();
	
	// 用于存放<Mac地址，IP>
	public static ConcurrentHashMap<String, String> macIP = new ConcurrentHashMap<String, String>();
	
	// 用于存放本地最后执行的命令编号
	public static int lastCmdNo = 0; 
	
	// 用于存放本地待执行的任务列表
	public static ConcurrentHashMap<Integer, String> taskList = new ConcurrentHashMap<Integer, String>();
}
