package nicelee.function.daily.douyu;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import nicelee.common.Escape;
import nicelee.common.util.HttpRequestUtil;
import nicelee.common.util.Logger;
import nicelee.common.util.RandomUtil;
import nicelee.function.live_recorder.util.TrustAllCertSSLUtil;

public class DouyuDailyTask {

	HttpRequestUtil util;

	public DouyuDailyTask() {
		util = new HttpRequestUtil();
//		System.setProperty("proxyHost", "127.0.0.1");//
//		System.setProperty("proxyPort", "8888");//
//		try {
//			HttpsURLConnection.setDefaultSSLSocketFactory(TrustAllCertSSLUtil.getFactory());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	/**
	 * 在指定房间签到
	 * 
	 * ps: 调用该方法后，实际上已经签到了，经验也增加了。 手机端显示已签，但是web端仍然diaplay签到按钮，虽然点击后不再增加经验
	 * 
	 * @param cookie
	 * @param roomId
	 * @param isFirstTimeToSignLately 短时间内可以一直用同一个
	 */
	static Pattern signToken = Pattern.compile("acf_ccn=([^\\],]*)&?");

	public String signAtRoom(String cookie, String roomId, boolean isFirstTimeToSignLately) {
		StringBuilder sb = new StringBuilder();

		HashMap<String, String> headers = new HashMap<>();
		headers.put("Accept", "application/json, text/plain, */*");
		headers.put("Referer", "https://www.douyu.com/" + roomId);
		headers.put("Cookie", cookie);
		headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0");

		// 获取acf_ccn 这是一个时效较短的cookie，但是短时间内频繁的话可以复用
		if (isFirstTimeToSignLately) {
			String result = util.getContent("https://www.douyu.com/curl/csrfApi/getCsrfCookie?", headers);
			Logger.println(result);
			// System.out.println(util.CurrentCookieManager().getCookieStore().getCookies().toString());
		}
		// 签到
		Matcher matcher = signToken.matcher(util.CurrentCookieManager().getCookieStore().getCookies().toString());
		matcher.find();
		headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		String param = String.format("rid=%s&ctn=%s", roomId, matcher.group(1));
		String result = util.postContent("https://www.douyu.com/japi/roomuserlevel/apinc/checkIn", headers, param);
		Logger.println(result);
		JSONObject obj = new JSONObject(result);
		if (obj.getInt("error") == 0) {
			JSONObject data = obj.getJSONObject("data");
			int exp_add = data.getInt("exp_add");
			int rank = data.getInt("rank");
			if (exp_add == 0)
				sb.append("   今日已经签到过");
			else
				sb.append("   增加经验：").append(exp_add).append(", 排名：").append(rank);
		} else {
			sb.append("   ").append(obj.getString("msg")); // 错误信息
		}
		sb.append("<br/>\n");
		return sb.toString();
	}

	/**
	 * 在关注的正在开播的直播间签到(不超过limit个)
	 * 
	 */
	public String signAtRoomActiveAndFollowed(String cookie, int limit) {
		StringBuilder sb = new StringBuilder();
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Accept", "application/json, text/plain, */*");
		headers.put("Referer", "https://www.douyu.com/directory/myFollow");
		headers.put("X-Requested-With", "XMLHttpRequest");
		headers.put("Cookie", cookie);
		headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0");
		String result = util.getContent("https://www.douyu.com/wgapi/livenc/liveweb/follow/list?sort=1&cid1=0",
				headers);
		Logger.println(result);
		JSONArray array = new JSONObject(result).getJSONObject("data").getJSONArray("list");
		for (int i = 0; i < array.length() && limit > 0; i++) {
			limit--;
			JSONObject obj = array.getJSONObject(i);
			if (obj.getInt("show_status") == 1) { // 在线
				Logger.printf("房间%s %s 签到<br/>\n", obj.getLong("room_id"), obj.getString("nickname"));
				sb.append("房间").append(obj.getLong("room_id")).append(" ").append(obj.getString("nickname"));// .append("
																												// 签到<br/>");
				sb.append(signAtRoom(cookie, "" + obj.getLong("room_id"), i == 0));
			} else { // 不在线
				break;
			}
		}
		return sb.toString();
	}

	/**
	 * 获取关注的鱼吧列表，并签到
	 * 
	 * @param cookie
	 * @param limit  每页大小
	 * @param page   第几页
	 */
	public String signAtBarsFollowed(String cookie, int limit, int page) {
		String testCookieValidUrl = "https://yuba.douyu.com/wbapi/web/user/info";
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Accept", "*/*");
		headers.put("Origin", "https://yuba.douyu.com");
		headers.put("Cookie", cookie);
		headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0");
		String info = util.getContent(testCookieValidUrl, headers);
		Logger.println(info);
		if (info.contains("未登录")) {
			new DouyuLogin().authFishBar();
		}

		String url = String.format("https://yuba.douyu.com/wbapi/web/group/myFollow?page=%d&limit=%d&timestamp=0.%s",
				page, limit, RandomUtil.getRandom("0234567890", 16));
		String result = util.getContent(url, headers);
		Logger.println(result);
		StringBuilder sb = new StringBuilder();
		try {
			JSONArray list = new JSONObject(result).getJSONObject("data").getJSONArray("list");
			for (int i = 0; i < list.length(); i++) {
				String groupId = "" + list.getJSONObject(i).getLong("group_id");
				sb.append(signAtBar(cookie, groupId));// .append("<br/>\n");
			}
			return sb.toString();
		} catch (Exception e) {
			return "鱼吧cookie已失效";
		}
	}

	/**
	 * 在指定鱼吧签到
	 * 
	 * @param cookie
	 * @param groupId 鱼吧id
	 */
	public String signAtBar(String cookie, String groupId) {

		HashMap<String, String> headers = new HashMap<>();
		headers.put("Accept", "*/*");
		headers.put("Origin", "https://yuba.douyu.com");
		headers.put("Referer", "https://yuba.douyu.com/group/" + groupId);
		headers.put("Cookie", cookie);
		headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0");
		// 生成随机timestamp
		String random = RandomUtil.getRandom("0123456789", 16);
		// 获取是否签到、当前经验
		String url = String.format("https://yuba.douyu.com/wbapi/web/group/head?group_id=%s&timestamp=0.%s", groupId,
				random);
		String r1 = util.getContent(url, headers);
		System.out.println(r1);
		JSONObject obj1 = new JSONObject(r1).getJSONObject("data");
		int currentStatus = obj1.getInt("is_signed"); // 0 或者 连续签到多少天
		int group_exp = obj1.getInt("group_exp");
		int group_level = obj1.getInt("group_level");
		String group_name = obj1.getString("group_name");

		StringBuilder sb = new StringBuilder();
//		if (group_level == 0 && group_exp == 0) {
//			System.err.println(groupId);
//			System.err.println("鱼吧Cookie 已失效 !!!");
//			return "鱼吧Cookie 已失效 !!!";
//		}
		if (currentStatus == 0) {// 未签到
			headers.put("Content-Type", "application/x-www-form-urlencoded");
			String param = String.format("group_id=%s&cur_exp=%s", groupId, group_exp);
			String result = util.postContent("https://yuba.douyu.com/ybapi/topic/sign?timestamp=0." + random, headers,
					param);
			Logger.println(result);
			if (result.contains("\\u672a\\u767b\\u5f55")) {// 未登录
				sb.append("cookie失效 <br/>\n");
//				new DouyuLogin().authFishBar();
//				result = util.postContent("https://yuba.douyu.com/ybapi/topic/sign?timestamp=0." + random, headers,
//						param);
//				Logger.println(result);
//				if(result.contains("\\u672a\\u767b\\u5f55")) {
//					sb.append("cookie失效 <br/>\n");
//				}else if(result.contains("\\u4eca\\u5929\\u5df2\\u7ecf\\u7b7e\\u5230\\u8fc7\\u4e86")) {
//					sb.append("鱼吧已经签到过了： " + group_name).append("<br/>\n");
//				}else{
//					sb.append("鱼吧签到中： " + group_name).append("<br/>\n");
//				}
			} else if (result.contains("\\u9c7c\\u4ed4\\u62a5\\u5230")) {// 鱼仔报到
				Logger.println("鱼仔报到" + groupId + group_name);
			} else {
				Logger.println(groupId + group_name);
			}
		} else {
			sb.append("鱼吧已经签到过了： " + group_name).append("<br/>\n");
		}
		return sb.toString();
	}

	/**
	 * 获取已完成的活跃任务，并领奖
	 * 
	 * @param roomNo
	 * @param realId
	 */
	public String gainActiveTask(String cookie) {
		StringBuilder sb = new StringBuilder();
		// 每日任务
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Accept", "application/json, text/plain, */*");
		headers.put("Referer", "https://www.douyu.com/312212");
		headers.put("Cookie", cookie);
		headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0");
		String result = util.getContent("https://www.douyu.com/japi/tasksys/ytxb/userStatusV2?roomId=198859&tagId=1",
				headers);
		// System.out.println(result);
		JSONArray array = new JSONObject(result).getJSONObject("data").getJSONArray("list");
		for (int i = 0; i < array.length(); i++) {
			JSONObject obj = array.optJSONObject(i);
			// 如果该任务已经完成
			if (obj != null && obj.optInt("status", 0) == 2) {
				int taskNo = obj.getInt("id");
				getTaskGift(cookie, taskNo);
				String info = String.format("领取任务 %d - %s奖励</br>\n", taskNo, obj.getString("desc"));
				sb.append(info);
			}
		}
		// 每周任务
		result = util.getContent(
				"https://www.douyu.com/japi/tasksys/ytxb/userStatusV2?cycleType=2&roomId=198859&tagId=1", headers);
		// System.out.println(result);
		array = new JSONObject(result).getJSONObject("data").getJSONArray("list");
		for (int i = 0; i < array.length(); i++) {
			JSONObject obj = array.optJSONObject(i);
			// 如果该任务已经完成
			if (obj != null && obj.optInt("status", 0) == 2) {
				int taskNo = obj.getInt("id");
				getTaskGift(cookie, taskNo);
				String info = String.format("领取任务 %d - %s奖励<br/>\n", taskNo, obj.getString("desc"));
				sb.append(info);
			}
		}
		return sb.toString();
	}

	/**
	 * 获取已完成的鱼塘任务，并领奖
	 * 
	 */
	public String gainSharkTask(String cookie) {
		StringBuilder sb = new StringBuilder();
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Accept", "application/json, text/plain, */*");
		headers.put("Referer", "https://www.douyu.com/312212");
		headers.put("Cookie", cookie);
		headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0");

		String result = util.getContent("https://www.douyu.com/japi/tasksys/ytxb/bubble?", headers);
		System.out.println(result);
		JSONArray array = new JSONObject(result).getJSONObject("data").getJSONArray("list");
		for (int i = 0; i < array.length(); i++) {
			JSONObject obj = array.optJSONObject(i);
			// 如果该任务已经完成
			if (obj != null && obj.optInt("status", 0) == 2) {
				int taskNo = obj.getInt("id");
				getTaskGift(cookie, taskNo);
				String info = String.format("领取任务 %d - %s奖励</br>\n", taskNo, obj.getString("name"));
				sb.append(info);
			}
		}
		return sb.toString();
	}

	/**
	 * 领取指定任务奖励
	 * 
	 * @param cookie
	 * @param id     任务id
	 */
	public void getTaskGift(String cookie, int id) {
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Accept", "application/json, text/plain, */*");
		headers.put("Referer", "https://www.douyu.com/312212");
		headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		headers.put("Cookie", cookie);
		headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0");

		String param = "roomId=312212&id=" + id;
//		String result = 
		util.postContent("https://www.douyu.com/japi/tasksys/ytxb/getPrize", headers, param);
		// System.out.println(result);
	}

	/**
	 * 车队签到(PC web端)
	 * 
	 * @param cookie
	 */
	public String signMotorcade(String cookie, String motorcadeId) {
		// 先获取msg.douyu.com的cookie
		DouyuLogin login = new DouyuLogin();
		login.goAuthMsgDomain(cookie);

		String csrf_token = RandomUtil.getRandom("0123456789abcdefghijklmnopqrstuvwxyz", 10);
		String dy_did = getValue(cookie, "acf_did");
		if (dy_did == null) {
			dy_did = RandomUtil.getRandom("1234567890abcdefghijklmnopqrstuvwxyz", 32);
		}
		StringBuilder sb = new StringBuilder(cookie);
		if (!cookie.endsWith(";"))
			sb.append(";");
		sb.append("dy_did=").append(dy_did).append(";");
		sb.append("post-csrfToken=").append(csrf_token).append(";");
		cookie = sb.toString();

		// 获取车队id(此处写死)
		// 可在页面获取https://msg.douyu.com/motorcade/#/motorcade/list/recommend
		Logger.println(Escape.escape(motorcadeId));
		// 获取车队任务完成信息
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Accept", "application/vnd.msg.douyu.com.v2+json");
		headers.put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
		headers.put("Accept-Encoding", "gzip, deflate");
		headers.put("Referer", "https://msg.douyu.com/motorcade/");
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		headers.put("dy-device-id", "-");
		headers.put("dy-client", "web");
		headers.put("dy-csrf-token", csrf_token);
		headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:73.0) Gecko/20100101 Firefox/73.0");
		headers.put("Cookie", cookie);

//		String url = "https://msg.douyu.com/v3/motorcade/getSetting?timestamp=0.18065999224197338";
//		String content = util.postContent(url, headers, "motorcadeId=" + motorcadeId);
//		System.out.println(content);

//		String url1 = String.format("https://msg.douyu.com/v1.0/motorcade/tasks?timestamp=0.%s&group_id=%s", 
//				RandomUtil.getRandomNumber(16),
//				Escape.escape(motorcadeId));
//		String content1 = util.getContent(url1, headers);
//		System.out.println(content1);
		String url = String.format("https://msg.douyu.com/v3/motorcade/signs/weekly?timestamp=0.%s&mid=%s",
				RandomUtil.getRandomNumber(16), Escape.escape(motorcadeId));
		String result = util.getContent(url, headers);
		Logger.println(result);
		JSONObject obj = new JSONObject(result).optJSONObject("data");
		String tips = null;
		if(obj == null) {
			tips = "cookie 失效<br/>\r\n";
		}else if (obj.getString("is_sign").equals("1")) {
			tips = "车队已经签到<br/>\r\n";
		} else {
			url = String.format("https://msg.douyu.com/v3/msign/add?timestamp=0.%s", RandomUtil.getRandomNumber(16));
			String param = String.format("to_mid=%s&expression=%d", Escape.escape(motorcadeId),
					obj.getInt("total") + 1);
			result = util.postContent(url, headers, param);
			Logger.println(result);
			obj = new JSONObject(result);
			if (obj.getInt("status_code") == 200) {
				Logger.println("签到成功<br/>\r\n");
				tips = "车队签到成功<br/>\r\n";
			}else {
				tips = "车队签到失败<br/>\r\n";
			}
		}
		Logger.print(tips);
		return tips;
	}

	
	/**
	 * 签到(Android客户端)
	 * 
	 * @param cookie
	 */
	public String signDailyPC(String cookie) {
		// Android端cookie和PC Web端不通用，header里不能带，否则报错
		// 参考 https://github.com/qianjiachun/douyuEx
		// 利用PC Web端 cookie生成token参数
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Accept", "application/json, text/javascript, */*; q=0.01");
		headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		headers.put("Origin", "https://apiv2.douyucdn.cn");
		headers.put("X-Requested-With", "XMLHttpRequest");
		headers.put("Referer", "https://apiv2.douyucdn.cn/H5/Sign/info?client_sys=android&ic=0");
		headers.put("Accept-Encoding", "gzip, deflate");
		headers.put("Accept-Language", "zh-CN,en,*");
//		headers.put("User-Agent", "Mozilla/5.0 ..., Douyu_Android");
//		headers.put("Cookie", cookie);

		String param = "token=" + getToken(cookie);
		Logger.println(param);
//		String result = util.postContent("https://apiv2.douyucdn.cn/h5nc/sign/getSign", headers, param);
		String result = util.postContent("https://apiv2.douyucdn.cn/h5nc/sign/sendSign", headers, param);
		Logger.println(result);
		JSONObject obj = new JSONObject(result);
		String tips = null;
		if ("6305".equals(obj.optString("error"))) {
			tips = ("Android客户端已经签到<br/>\n");
		}else {
			tips = ("Android客户端签到成功<br/>\n");
		}
		Logger.println(tips);
		return tips;
	}

	private static String getToken(String cookie) {
		StringBuilder sb = new StringBuilder();
		sb.append(getValue(cookie, "acf_uid")).append("_");
		sb.append(getValue(cookie, "acf_biz")).append("_");
		sb.append(getValue(cookie, "acf_stk")).append("_");
		sb.append(getValue(cookie, "acf_ct")).append("_");
		sb.append(getValue(cookie, "acf_ltkid"));
		return sb.toString();
	}

	private static String getValue(String cookie, String key) {
		Pattern pattern = Pattern.compile(key + "=([^;]*)");
		Matcher matcher = pattern.matcher(cookie);
		if (matcher.find()) {
			return Escape.unescape(matcher.group(1));
		}
		return null;
	}
}
