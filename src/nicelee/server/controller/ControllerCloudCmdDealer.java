package nicelee.server.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.Map.Entry;

import nicelee.common.annotation.Controller;
import nicelee.function.cloud.task.CommandDealer;
import nicelee.function.cloud.upload.FileUploader;
import nicelee.global.Global;
import nicelee.global.GlobalConfig;

@Controller(path = "/cloud", note = "云端命令相关")
public class ControllerCloudCmdDealer {

	
	@Controller(path = "/run", note = "从云端获取命令，并执行")
	public String getCmdFromCloudNRun() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				CommandDealer cmdDealer = new CommandDealer();
				// 从云端获取命令集, 只增加，不减少。确保taskList最新
				System.out.println("从云端获取命令集");
				cmdDealer.refreshTaskList(GlobalConfig.url_taskToDo, GlobalConfig.token, Global.taskList);
				// 处理taskList
				System.out.println("处理taskList");
				boolean hasTaskRemoved = cmdDealer.runTask(Global.taskList);
				if(hasTaskRemoved) {
					// 上传结果
					System.out.println("上传taskList处理结果");
					File file = new File(GlobalConfig.tmpDir, "task-result.txt");
					FileUploader.update(GlobalConfig.url_taskReport, file, GlobalConfig.token);
				}
			}
		}).start();
		return "正在尝试从云端获取命令并执行";
	}
	
	@Controller(path = "/history/delete", note = "删除本地task运行结果记录")
	public String deleteHistory() {
		File file = new File(GlobalConfig.tmpDir, "task-result.txt");
		file.delete();
		return "正在删除本地task运行结果记录";
	}
	
	@Controller(path = "/history/upload", note = "上传本地task运行结果记录")
	public String uploadHistory() {
		File file = new File(GlobalConfig.tmpDir, "task-result.txt");
		FileUploader.update(GlobalConfig.url_taskReport, file, GlobalConfig.token);
		return "正在尝试上传本地task运行结果记录";
	}
	
	@Controller(path = "/latestNo", note = "获取最近的任务标号")
	public String latestNo() {
		return "" + Global.lastCmdNo;
	}
	
	@Controller(path = "/list", note = "列出本地的计划任务")
	public String listTask(BufferedWriter out) {
		for (Entry<Integer, String> entry : Global.taskList.entrySet()) {
			try {
				out.write(entry.getKey() + " ");
				out.write(entry.getValue());
				out.write("<br/>\r\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
}
