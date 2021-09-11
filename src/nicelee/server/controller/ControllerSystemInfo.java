package nicelee.server.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

import nicelee.common.annotation.Controller;
import nicelee.global.GlobalConfig;

@Controller(path = "/info", note = "相关信息")
public class ControllerSystemInfo {
	
	@Controller(path = "/space/root", note = "获取根目录可用空间信息")
	public String rootSpaceInfo(BufferedWriter out) {
		try {
//			String path = new File("").getCanonicalPath();
//			int end = path.indexOf(File.separatorChar);
//			File file = new File(path.substring(0, end));
			File file = new File("/");
			
			out.write(file.getCanonicalPath());
			out.write("<br/>\n");
			String totalSpace = String.format("总空间：%.2f GB<br/>\n", ((double)file.getTotalSpace())/1024/1024/1024);
			String freeSpace = String.format("可用空间：%.2f GB<br/>\n", ((double)file.getFreeSpace())/1024/1024/1024);
			out.write(totalSpace);
			out.write(freeSpace);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
