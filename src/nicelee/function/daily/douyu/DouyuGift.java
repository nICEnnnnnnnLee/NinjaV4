package nicelee.function.daily.douyu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import nicelee.common.util.HttpRequestUtil;
import nicelee.common.util.Logger;
import nicelee.function.daily.douyu.domain.Gift;

public class DouyuGift {

	HttpRequestUtil util;

	public DouyuGift() {
		util = new HttpRequestUtil();
	}

	/**
	 * 获取拥有的礼物
	 * 
	 * @return 没有礼物为零，异常为null
	 */
	public List<Gift> info(String cookie) {
		try {
			List<Gift> gifts = new ArrayList<Gift>();
			HashMap<String, String> headers = new HashMap<>();
			headers.put("Referer", "https://www.douyu.com/288016");
			headers.put("Cookie", cookie);
			String json = util.getContent("https://www.douyu.com/japi/prop/backpack/web/v1?rid=288016", headers);
			//Logger.println(json);
			JSONArray array = new JSONObject(json).getJSONObject("data").getJSONArray("list");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				Gift gift = new Gift();
				gift.id = obj.getInt("id");
				gift.count = obj.getInt("count");
				gift.name = obj.getString("name");
				gifts.add(gift);
				System.out.printf("礼物id:%d %s 还剩%d\n", gift.id, gift.name, gift.count);
			}
			return gifts;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 给主播送礼物
	 * 
	 * @param cookie
	 * @param roomNo 房间号
	 * @param realId 真实id
	 * @param id     礼物id
	 * @param count  礼物数量
	 * @return 0 成功 1 没有足够的礼物 9需要登录(cookie失效) 403 非法请求 -1 网络或其它原因
	 */
	public int send(String cookie, String roomNo, String realId, int id, int count) {
		try {
			HashMap<String, String> headers = new HashMap<>();
			headers.put("Referer", "https://www.douyu.com/" + roomNo);
			headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			headers.put("Cookie", cookie);

			String param = String.format("propId=%d&propCount=%d&roomId=%s&bizExt={\"yzxq\":{}}", id, count, realId);
			String json = util.postContent("https://www.douyu.com/japi/prop/donate/mainsite/v1", headers, param);
			// System.out.println(json);
			return new JSONObject(json).getInt("error");
		} catch (Exception e) {
			return -1;
		}
	}

	/**
	 * 获取所有的牌子
	 * 
	 * @param cookie
	 * @return
	 */
	static Pattern pattern = Pattern.compile("<a href=\"/([0-9]+)\"[\\s\\S]*?data-rid=\"([0-9]+)\"");

	public List<String> idols(String cookie) {
		try {
			HashMap<String, String> headers = new HashMap<>();
			headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			headers.put("Referer", "https://www.douyu.com/");
			headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			headers.put("Cookie", cookie);
			headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0");

			String html = util.getContent("https://www.douyu.com/member/cp/getFansBadgeList", headers);
			// System.out.println(html);
			// 取出唯一的表格
			int begin = html.indexOf("tbody");
			int end = html.indexOf("tbody", begin + 6);
			String tbody = html.substring(begin, end);
			// System.out.println(tbody);

			Matcher matcher = pattern.matcher(tbody);
			List<String> list = new ArrayList<>();
			while (matcher.find()) {
				list.add(matcher.group(1) + "#" + matcher.group(2));
				System.out.println(matcher.group(1) + "#" + matcher.group(2));
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将全部礼物送给主播
	 * 
	 * @param roomNo
	 * @param realId
	 */
	public void sendAll(String cookie, String roomNo, String realId) {
		DouyuGift dg = new DouyuGift();
		List<Gift> list = dg.info(cookie);
		for (Gift gift : list) {
			if(gift.id ==268) { // 粉丝荧光棒可以一次性全部送出
				dg.send(cookie, roomNo, realId, gift.id, gift.count);
			}else {// 其他礼物一个一个送
				for(int i=0; i<gift.count; i++) {
					dg.send(cookie, roomNo, realId, gift.id, 1);
				}
			}
			
		}
	}
}
