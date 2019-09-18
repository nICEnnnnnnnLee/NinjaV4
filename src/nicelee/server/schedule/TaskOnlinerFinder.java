package nicelee.server.schedule;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.TimerTask;
import java.util.Timer;

import nicelee.function.cloud.upload.FileUploader;
import nicelee.function.ipscan.IpScanner;
import nicelee.global.Global;
import nicelee.global.GlobalConfig;

public class TaskOnlinerFinder extends TimerTask {

	Timer timer = new Timer(true);
	IpScanner sanner;
	public void init() {
		sanner = new IpScanner() {
			@Override
			public void dealResult(Map<String, String> resultMap) {
				System.out.println("--------");
				File macResult = new File(GlobalConfig.tmpDir, "online-devices.txt");
				if(!macResult.exists()) {
					macResult.getParentFile().mkdir();
				}
				try {
					BufferedWriter writer = new BufferedWriter(new FileWriter(macResult, false));
					
					// 写入最后更新时间
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					sdf.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
					writer.write(sdf.format(new Date())) ;
					writer.newLine();
					
					for (Map.Entry<String, String>  entry : resultMap.entrySet()) {
						writer.write(entry.getKey());
						writer.write(" ");
						String note = Global.macRemark.get(entry.getKey());
						if (note != null) {
							writer.write(note);
						}
						writer.newLine();
					}
					writer.flush();
					writer.close();
					
					FileUploader.update(GlobalConfig.url_onlineDevices, macResult, GlobalConfig.token);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
	}

	public void start(long minutes) {
		init();
		System.out.println(minutes*60*1000);
		timer.scheduleAtFixedRate(this, 0, minutes*60*1000);
	}
	
	
	public void stop() {
		timer.cancel();
	}

	/**
	 * 查询一次Mac地址，然后保存临时结果，最后上传
	 */
	@Override
	public void run() {
		sanner.startScan();
	}

}
