package nicelee.function.cloud.mac_remarks;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nicelee.global.Global;

public class MacsRemark {

	static Pattern pattern = Pattern.compile("^([^ ]+) +(.*)$");
	public static void refreshRemarks(String url, String token) {
		ConcurrentHashMap<String, String> result = new ConcurrentHashMap<String, String>();;
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
			while ((line = in.readLine()) != null) {
				Matcher matcher = pattern.matcher(line);
				if(matcher.find()) {
					result.put(matcher.group(1), matcher.group(2).trim());
					System.out.printf("MAC为：%s, 备注： %s\n", matcher.group(1), matcher.group(2).trim());
				}
			}
			Global.macRemark = result;
		} catch (Exception e) {
			System.out.println("刷新Mac备注出现异常！");
		} finally {
			try {
				in.close();
			} catch (Exception e2) {
			}
		}
	}
}
