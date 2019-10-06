package nicelee.function.cloud.task;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nicelee.global.Global;
import nicelee.global.GlobalConfig;
import nicelee.server.core.PathDealer;

public class CommandDealer {

	final static Pattern patternCmdOnCloud = Pattern
			.compile("([0-9]+) (([0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}) +.*)$");
	final static Pattern patternCommand = Pattern
			.compile("([0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}) +([^\\?]+)\\??(.*)$");
	final static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	final static TimeZone tzChina = TimeZone.getTimeZone("GMT+8:00");

	/**
	 * 执行taskList表中的合适的任务
	 * @param taskList
	 */
	public boolean runTask(ConcurrentHashMap<Integer, String> taskList) {
		long currentTime = System.currentTimeMillis();
		PathDealer dealer = new PathDealer();
		
		boolean hasTaskRemoved = false;

		try {
			// 将结果输出到{tmp}/task-result.txt, 先获取权柄
			File file = new File(GlobalConfig.tmpDir, "task-result.txt");
			if(!file.exists()) {
				file.getParentFile().mkdirs();
			}
			BufferedWriter out = new BufferedWriter(new FileWriter(file,true));
			// 遍历任务表，执行任务
			for (Entry<Integer, String> entry : taskList.entrySet()) {
				Matcher matcher = patternCommand.matcher(entry.getValue());
				matcher.find();
				formatter.setTimeZone(tzChina);
				long planTime = formatter.parse(matcher.group(1)).getTime();
				// 如果当前时间已经过了指定点，执行任务
				if (planTime <= currentTime) {
					System.out.printf("正在处理task： %s\n", entry.getValue());
					String path = matcher.group(2);
					String param = matcher.group(3);
					out.write(String.format("%d %s\r\n", entry.getKey(),entry.getValue()));
					if(path.startsWith("/cloud/history/delete") || path.startsWith("/cloud/run")) {
						out.write("该命令只能本地执行");
					}else {
						dealer.dealRequest(out, path, param, null, true);
					}
					out.newLine();
					// 执行后，在列表中去除任务
					taskList.remove(entry.getKey());
					hasTaskRemoved = true;
				}
			}
			out.flush();
			out.close();
		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
		}
		return hasTaskRemoved;

	}

	/**
	 * 从云端获取待执行的命令，刷新任务列表(只增不减)
	 * 
	 * @param url
	 * @param taskList
	 */
	public void refreshTaskList(String url, String token, ConcurrentHashMap<Integer, String> taskList) {
		long currentTime = System.currentTimeMillis();
		BufferedReader in = null;
		HttpURLConnection conn = null;
		try {
			URL realUrl = new URL(url);
			conn = (HttpURLConnection) realUrl.openConnection();
			conn.setConnectTimeout(120000);
			conn.setReadTimeout(120000);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("User-Agent", "Github File Uploader App");
			conn.setRequestProperty("Authorization", "token " + token);
			conn.connect();
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String line;
			int newNo = -1;
			while ((line = in.readLine()) != null) {
				Matcher matcher = patternCmdOnCloud.matcher(line);
				if (matcher.find()) {
					// 首先，不去管企划在一个周期+一分钟之前的命令
					formatter.setTimeZone(tzChina);
					long planTime = formatter.parse(matcher.group(3)).getTime();
					if(currentTime - planTime > 60000 + GlobalConfig.taskPeriod*60000) {
						continue;
					}
					// 其次，不去管序号太老的命令，因为可能已经获取过了
					int cmdNo = Integer.parseInt(matcher.group(1));
					if (isLatest(cmdNo, Global.lastCmdNo)) {
						String command = matcher.group(2);
						taskList.put(cmdNo, command);
						if (isLatest(cmdNo, newNo)) {
							newNo = cmdNo;
						}
						System.out.printf("新增task： %s\n", command);
					}
				}
			}
			if (newNo != -1) {
				Global.lastCmdNo = newNo;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (Exception e2) {
			}
		}
	}

	/**
	 * 判断当前任务序号是否过时
	 * 
	 * @param currentNum
	 * @param lastNum
	 * @return
	 */
	boolean isLatest(int currentNum, int lastNum) {
		if (lastNum == -1)
			return true;

		// 如果两者接近，大的那个最新; 如果两者差距过大，说明序号重新计算，小的那个最新
		if (currentNum > lastNum && currentNum - lastNum < 100)
			return true;
		if (currentNum < lastNum && currentNum < 10 && lastNum - currentNum > 100)
			return true;
		return false;
	}
}
