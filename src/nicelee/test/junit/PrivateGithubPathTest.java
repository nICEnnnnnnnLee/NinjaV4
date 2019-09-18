package nicelee.test.junit;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import nicelee.global.Global;


public class PrivateGithubPathTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		String url = "https://raw.githubusercontent.com/nICEnnnnnnnLee/FileDownloader/master/src/nicelee/util/Logger.java";
		BufferedReader in = null;
		HttpURLConnection conn = null;
		try {
			URL realUrl = new URL(url);
			conn = (HttpURLConnection) realUrl.openConnection();
			conn.setConnectTimeout(120000);
			conn.setReadTimeout(120000);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("User-Agent", "Github File Uploader App");
			//conn.setRequestProperty("Authorization", "token xxx");
			conn.connect();
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			String line;
			while ((line = in.readLine()) != null) {
					System.out.printf(line);
			}
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
