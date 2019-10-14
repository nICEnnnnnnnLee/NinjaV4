package nicelee.function.daily.douyu;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpCookie;
import java.util.HashMap;

import org.json.JSONObject;

import nicelee.common.util.HttpRequestUtil;
import nicelee.common.util.QrCodeUtil;
import nicelee.common.util.RandomUtil;

public class DouyuLogin extends Thread {

	static String loginCookie = "";
	HttpRequestUtil util;
	int loginStatus;
	String loginUrl;
	File qrCodeFile;

	static {
		readCookieFromFile();
	}
	public DouyuLogin() {
		util = new HttpRequestUtil();
		qrCodeFile = new File("config/qrcode.jpg");
	}

	/**
	 * 从配置文件初始化cookie
	 * 
	 * @return
	 */
	public static String readCookieFromFile() {
		try {
			File f = new File("config/douyu-cookie.txt");
			if (!f.exists())
				f = new File("douyu-cookie.txt");
			BufferedReader bu = new BufferedReader(new FileReader(f));
			loginCookie = bu.readLine();
			bu.close();
		} catch (IOException e) {
		}
		System.out.println(loginCookie);
		return loginCookie;
	}
	
	public static String getCookie() {
		return loginCookie;
	}

	public static void main(String[] a) {
		try {
			DouyuLogin login = new DouyuLogin();
//			String code = login.generateCode();
//			login.queryStatus(code);
			login.start();
			// Thread.sleep(10000*600);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		System.out.println("login - start");
		try {
			loginStatus = 0;
			String code = generateCode();
			String url = "https://passport.douyu.com/scan/checkLogin?scan_code=" + code;

			loginStatus = 1;
			if (qrCodeFile.exists()) {
				qrCodeFile.delete();
			} else {
				qrCodeFile.getParentFile().mkdirs();
			}
			QrCodeUtil.createQrCode(new FileOutputStream(qrCodeFile), url, 900, "JPEG");

			loginStatus = 2;
			int count = 0, status = -1;
			while (count < 60) {
				status = queryStatus(code);
				if (status == 1) {
					System.out.println("客户端已扫码");
					loginStatus = 3;
				} else if (status == -2) {
					System.out.println("客户端还未扫码");
				} else if (status == 0) {
					System.out.println("客户端已确认");
					loginStatus = 4;
					break;
				} else if (status == -1) {
					System.out.println("二维码不存在或者是已经过期");
					loginStatus = 7;
					break;
				}
				Thread.sleep(2000);
			}

			if (loginStatus == 4) {
				// 扫码成功
				if (getLoginCookie()) {
					loginStatus = 7;
					// 去获取.yuba.douyu.com的cookie
					goAuthFishBar();
					// 保存cookie
					StringBuilder sb = new StringBuilder();
					for (HttpCookie cookie : util.CurrentCookieManager().getCookieStore().getCookies()) {
						sb.append(cookie.getName()).append("=").append(cookie.getValue()).append("; ");
					}
					String cookie = sb.toString();
					if (cookie.endsWith("; ")) {
						cookie = cookie.substring(0, cookie.length() - 2);
					}
					System.out.println(cookie);
					loginCookie = cookie;
					saveCookie();
				} else
					loginStatus = 8;
			} else {
				loginStatus = 5;
			}
		} catch (Exception e) {
			e.printStackTrace();
			loginStatus = 6;
		}

	}

	private String generateCode() throws Exception {
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Accept", "application/json, text/javascript, */*; q=0.01");
		headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		// headers.put("Cookie", cookie);
		headers.put("X-Requested-With", "XMLHttpRequest");
		headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0");
		headers.put("Referer",
				"https://passport.douyu.com/index/login?passport_reg_callback=PASSPORT_REG_SUCCESS_CALLBACK&passport_login_callback=PASSPORT_LOGIN_SUCCESS_CALLBACK&passport_close_callback=PASSPORT_CLOSE_CALLBACK&passport_dp_callback=PASSPORT_DP_CALLBACK&type=login&client_id=1&state=https%3A%2F%2Fwww.douyu.com%2Fdirectory%2FmyFollow&source=click_topnavi_login");

		String result = util.postContent("https://passport.douyu.com/scan/generateCode", headers, "client_id=1");
		JSONObject obj = new JSONObject(result);
		if (obj.getInt("error") == 0)
			return obj.getJSONObject("data").getString("code");
		else
			throw new Exception("二维码请求出现错误");
	}

	private int queryStatus(String code) {
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Accept", "application/json, text/javascript, */*; q=0.01");
		headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		headers.put("X-Requested-With", "XMLHttpRequest");
		headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0");
		headers.put("Referer",
				"https://passport.douyu.com/index/login?passport_reg_callback=PASSPORT_REG_SUCCESS_CALLBACK&passport_login_callback=PASSPORT_LOGIN_SUCCESS_CALLBACK&passport_close_callback=PASSPORT_CLOSE_CALLBACK&passport_dp_callback=PASSPORT_DP_CALLBACK&type=login&client_id=1&state=https%3A%2F%2Fwww.douyu.com%2Fdirectory%2FmyFollow&source=click_topnavi_login");

		String url = String.format("https://passport.douyu.com/lapi/passport/qrcode/check?time=%d&code=%s",
				System.currentTimeMillis(), code);
		String result = util.getContent(url, headers);
		// System.out.println(result);
		JSONObject obj = new JSONObject(result);
		int status = obj.getInt("error");
		if (status == 0) {
			loginUrl = "https:" + obj.getJSONObject("data").getString("url");
		}
		return status;
	}

	private boolean getLoginCookie() {
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Accept", "*/*");
		headers.put("X-Requested-With", "XMLHttpRequest");
		headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0");
		headers.put("Referer", "https://passport.douyu.com/");

		String url = String.format("%s&callback=appClient_json_callback&_=%d", loginUrl, System.currentTimeMillis());
		String result = util.getContent(url, headers);
		//System.out.println(result);
		return result.contains("\"msg\":\"ok\"");
	}

	private static void saveCookie() {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("config/douyu-cookie.txt", false));
			out.write(loginCookie);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据已有Cookie，登录鱼吧
	 * @return
	 */
	public String authFishBar() {
		if(loginCookie.contains("acf_yb_auth"))
			return loginCookie;
		goAuthFishBar();
		
		StringBuilder sb = new StringBuilder();
		for (HttpCookie cookie : util.CurrentCookieManager().getCookieStore().getCookies()) {
			sb.append(cookie.getName()).append("=").append(cookie.getValue()).append("; ");
		}
		if(sb.indexOf(loginCookie) == -1) {
			sb.append(loginCookie);
		}
		String cookie = sb.toString();
		if (cookie.endsWith("; ")) {
			cookie = cookie.substring(0, cookie.length() - 2);
		}
		System.out.println(cookie);
		loginCookie = cookie;
		saveCookie();
		return loginCookie;
	}
	
	private void goAuthFishBar() {
		long currtime = System.currentTimeMillis();
		String callback = RandomUtil.getRandom("1234567890", 16);
		String url = String.format("https://passport.douyu.com/lapi/passport/iframe/safeAuth?callback=jQuery%s_%d&client_id=5&did=&t=1571020930757&_=1571020930392", 
				callback, currtime, currtime +59, currtime +1);
		
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Accept", "*/*");
		headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0");
		headers.put("Referer", "https://yuba.douyu.com/group/16775");
		headers.put("Cookie", loginCookie);
		
//		util.getContent(url, headers);
		String content = util.getContent(url, headers);
		System.out.println(content);
	}
}
