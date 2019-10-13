package nicelee.function.daily.douyu;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import nicelee.common.util.HttpRequestUtil;

public class DouyuDailyTask {

	HttpRequestUtil util;

	public DouyuDailyTask() {
		util = new HttpRequestUtil();
	}

	/**
	 * 在指定房间签到
	 * 
	 * @param cookie
	 * @param roomId
	 */
	static Pattern signToken = Pattern.compile("acf_ccn=(.*)&?");

	public void sign(String cookie, String roomId) {
		// TODO
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Accept", "application/json, text/plain, */*");
		headers.put("Referer", "https://www.douyu.com/" + roomId);
		headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		headers.put("Cookie", cookie);
		headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0");

		Matcher matcher = signToken.matcher(cookie);
		matcher.find();
		String param = String.format("rid=%s&ctn=%s", roomId, matcher.group(1));
		String result = util.postContent("https://www.douyu.com/japi/roomuserlevel/apinc/checkIn", headers, param);
		System.out.println(result);
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
				String info = String.format("领取任务 %d - %s奖励\n", taskNo, obj.getString("desc"));
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
		// System.out.println(result);
		JSONArray array = new JSONObject(result).getJSONObject("data").getJSONArray("list");
		for (int i = 0; i < array.length(); i++) {
			JSONObject obj = array.optJSONObject(i);
			// 如果该任务已经完成
			if (obj != null && obj.optInt("status", 0) == 2) {
				int taskNo = obj.getInt("id");
				getTaskGift(cookie, taskNo);
				try {
					String info = String.format("领取任务 %d - %s奖励</br>\n", taskNo, obj.getString("desc"));
					sb.append(info);
				} catch (Exception e) {
				}
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

}
