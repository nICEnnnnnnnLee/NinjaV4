package nicelee.function.daily.douyu;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import nicelee.common.util.HttpRequestUtil;
import nicelee.common.util.RandomUtil;

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
	 * 获取关注的鱼吧列表，并签到
	 * @param cookie
	 * @param limit 每页大小
	 * @param page 第几页
	 */
	public String signAtBarsFollowed(String cookie, int limit, int page) {
		String url = String.format("https://yuba.douyu.com/wbapi/web/group/myFollow?page=%d&limit=%d&timestamp=0.%s", 
				page, limit, RandomUtil.getRandom("0234567890", 16));
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Accept", "*/*");
		headers.put("Origin", "https://yuba.douyu.com");
		headers.put("Cookie", cookie);
		headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0");
		String result = util.getContent(url, headers);
		System.out.println(result);
		StringBuilder sb = new StringBuilder();
		try {
			JSONArray list = new JSONObject(result).getJSONObject("data").getJSONArray("list");
			for(int i = 0; i < list.length(); i++) {
				String groupId = "" + list.getJSONObject(i).getLong("group_id");
				sb.append(signAtBar(cookie, groupId));//.append("<br/>\n");
			}
			return sb.toString();
		}catch (Exception e) {
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
		String url = String.format("https://yuba.douyu.com/wbapi/web/group/head?group_id=%s&timestamp=0.%s", groupId, random);
		String r1 = util.getContent(url, headers);
		//System.out.println(r1);
		JSONObject obj1 =  new JSONObject(r1).getJSONObject("data");
		int currentStatus = obj1.getInt("is_signed"); // 0 或者 连续签到多少天
		int group_exp = obj1.getInt("group_exp");
		int group_level = obj1.getInt("group_level");
		String group_name = obj1.getString("group_name");
		
		StringBuilder sb = new StringBuilder();
		if(group_level == 0 && group_exp == 0) {
			System.err.println("鱼吧Cookie 已失效 !!!");
			return "鱼吧Cookie 已失效 !!!";
		}
		// TODO 推测，尚未验证
		if(currentStatus == 0) {// 未签到
			sb.append("鱼吧签到中： "+ group_name).append("<br/>\n");
			headers.put("Content-Type", "application/x-www-form-urlencoded");
			String param = String.format("group_id=%s&cur_exp=%s", groupId, group_exp);
			String result = util.postContent("https://yuba.douyu.com/ybapi/topic/sign?timestamp=0."+ random, headers, param);
			System.out.println(result);
		}else {
			sb.append("鱼吧已经签到过了： "+ group_name).append("<br/>\n");
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

}
