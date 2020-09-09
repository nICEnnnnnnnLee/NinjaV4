package nicelee.test.junit;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.TimeZone;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import nicelee.common.util.HttpRequestUtil;

public class StepModifyTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAll() {
		String userName = ""; // 手机号
		String userPwd = ""; // 密码MD5
		String userIMEI = "M_NULL"; // M_ + 手机imei号
		String clientId = "xxx"; // 盲猜可以随机生成，但生成后不要变动，建议使用userName的MD5值
		int step = 10087;
		
		String url = "https://sports.lifesense.com/sessions_service/login?city=&province=&devicemodel=PLK-TL01H&roleType=0&areaCode=&osversion=6.0&screenHeight=1812&provinceCode=&version=4.6.7&channel=huawei&systemType=2&promotion_channel=huawei&screenWidth=1080&rnd=6287cadf&requestId=&longitude=&requestToken=&screenheight=1812&os_country=CN&timezone=Asia%2FShanghai&cityCode=&os_langs=zh&platform=android&openudid=&countryCode=&country=&screenwidth=1080&network_type=wifi&appType=6&area=CN&latitude=&language=zh&ts="
				+ System.currentTimeMillis() / 1000;
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json; charset=utf-8");
		headers.put("User-Agent", "Dalvik/2.1.0 (Linux; U; Android 6.0; PLK-TL01H Build/HONORPLK-TL01H)");
		headers.put("Connection", "Keep-Alive");
		headers.put("Accept-Encoding", "gzip");
		String params = String.format(
				"{\"loginName\":\"%s\",\"password\":\"%s\",\"clientId\":\"%s\",\"roleType\":0,\"appType\":6}", userName,
				userPwd, clientId);

		HttpRequestUtil util = new HttpRequestUtil();
		String content = util.postContent(url, headers, params);
		System.out.println(content);
		System.out.println();
		System.out.println(util.CurrentCookieManager().getCookieStore().getCookies().toString());
		JSONObject json = new JSONObject(content).getJSONObject("data");
		String userId = json.getString("userId");
		{
			// 从登录数据得来
			// String cookie = "";
			// String userId = "";
			// 当前时间
			long updated = System.currentTimeMillis();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sdf.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
			String createdTime = sdf.format(updated);
			String measurementTime = createdTime;
			String dayMeasurementTime = createdTime.split(" ")[0];
			// 想要的数据
			int calories = step / 4000;
			int distance = step / 3;

			url = "https://sports.lifesense.com/sport_service/sport/sport/uploadMobileStepV2?city=&province=&devicemodel=PLK-TL01H&areaCode=&osversion=6.0&provinceCode=&version=4.6.7&channel=huawei&systemType=2&promotion_channel=huawei&rnd=&requestId=&longitude=&requestToken=&screenheight=1812&os_country=CN&timezone=Asia%2FShanghai&cityCode=&os_langs=zh&platform=android&openudid=&countryCode=&country=&screenwidth=1080&network_type=wifi&appType=6&area=CN&latitude=&language=zh&ts="
					+ System.currentTimeMillis() / 1000;
			headers = new HashMap<>();
			headers.put("Content-Type", "application/json; charset=utf-8");
			headers.put("User-Agent", "Dalvik/2.1.0 (Linux; U; Android 6.0; PLK-TL01H Build/HONORPLK-TL01H)");
			headers.put("Connection", "Keep-Alive");
			headers.put("Accept-Encoding", "gzip");

			JSONObject obj = new JSONObject();
			obj.put("active", 1);
			obj.put("calories", calories);
			obj.put("created", createdTime);
			obj.put("dataSource", 2);
			obj.put("dayMeasurementTime", dayMeasurementTime);
			obj.put("deviceId", userIMEI);
			obj.put("distance", distance);
			obj.put("isUpload", 0);
			obj.put("measurementTime", measurementTime);
			obj.put("priority", 0);
			obj.put("step", step);
			obj.put("type", 2);
			obj.put("updated", updated);
			obj.put("userId", userId);
			obj.put("DataSource", 2);
			obj.put("exerciseTime", 0);
			params = String.format("{\"list\":[%s]}", obj.toString());

//			System.out.println(params);
//			HttpRequestUtil util = new HttpRequestUtil();
//			String content = util.postContent(url, headers, params);
//			System.out.println(content);

		}
	}

	// @Test
	public void testLogin() {
		String userName = "手机号";
		String userPwd = "密码MD5"; // MD5
		String clientId = "xxx"; // 盲猜可以随机生成，生成后但不要变动，建议使用userName的MD5值

		String url = "https://sports.lifesense.com/sessions_service/login?city=&province=&devicemodel=PLK-TL01H&roleType=0&areaCode=&osversion=6.0&screenHeight=1812&provinceCode=&version=4.6.7&channel=huawei&systemType=2&promotion_channel=huawei&screenWidth=1080&rnd=6287cadf&requestId=&longitude=&requestToken=&screenheight=1812&os_country=CN&timezone=Asia%2FShanghai&cityCode=&os_langs=zh&platform=android&openudid=&countryCode=&country=&screenwidth=1080&network_type=wifi&appType=6&area=CN&latitude=&language=zh&ts="
				+ System.currentTimeMillis() / 1000;
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json; charset=utf-8");
		headers.put("User-Agent", "Dalvik/2.1.0 (Linux; U; Android 6.0; PLK-TL01H Build/HONORPLK-TL01H)");
		headers.put("Connection", "Keep-Alive");
		headers.put("Accept-Encoding", "gzip");
		String params = String.format(
				"{\"loginName\":\"%s\",\"password\":\"%s\",\"clientId\":\"%s\",\"roleType\":0,\"appType\":6}", userName,
				userPwd, clientId);
		
		HttpRequestUtil util = new HttpRequestUtil();
		String content = util.postContent(url, headers, params);
		System.out.println(content);
		System.out.println();
		System.out.println(util.CurrentCookieManager().getCookieStore().getCookies().toString());
	}

	// @Test
	public void testModifyStep() {
		String userIMEI = "M_NULL"; // M_ + 手机imei号
		// 从登录数据得来
		 String cookie = "";
		 String userId = "";
		 
		// 当前时间
		long updated = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		String createdTime = sdf.format(updated);
		String measurementTime = createdTime;
		String dayMeasurementTime = createdTime.split(" ")[0];
		// 想要的数据
		int step = 10087;
		int calories = step / 4000;
		int distance = step / 3;

		String url = "https://sports.lifesense.com/sport_service/sport/sport/uploadMobileStepV2?city=&province=&devicemodel=PLK-TL01H&areaCode=&osversion=6.0&provinceCode=&version=4.6.7&channel=huawei&systemType=2&promotion_channel=huawei&rnd=&requestId=&longitude=&requestToken=&screenheight=1812&os_country=CN&timezone=Asia%2FShanghai&cityCode=&os_langs=zh&platform=android&openudid=&countryCode=&country=&screenwidth=1080&network_type=wifi&appType=6&area=CN&latitude=&language=zh&ts="
				+ System.currentTimeMillis() / 1000;
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json; charset=utf-8");
		headers.put("User-Agent", "Dalvik/2.1.0 (Linux; U; Android 6.0; PLK-TL01H Build/HONORPLK-TL01H)");
		headers.put("Connection", "Keep-Alive");
		headers.put("Accept-Encoding", "gzip");
		headers.put("Cookie", cookie);

		JSONObject obj = new JSONObject();
		obj.put("active", 1);
		obj.put("calories", calories);
		obj.put("created", createdTime);
		obj.put("dataSource", 2);
		obj.put("dayMeasurementTime", dayMeasurementTime);
		obj.put("deviceId", userIMEI);
		obj.put("distance", distance);
		obj.put("isUpload", 0);
		obj.put("measurementTime", measurementTime);
		obj.put("priority", 0);
		obj.put("step", step);
		obj.put("type", 2);
		obj.put("updated", updated);
		obj.put("userId", userId);
		obj.put("DataSource", 2);
		obj.put("exerciseTime", 0);
		String params = String.format("{\"list\":[%s]}", obj.toString());

		System.out.println(params);
		HttpRequestUtil util = new HttpRequestUtil();
		String content = util.postContent(url, headers, params);
		System.out.println(content);
	}

}
