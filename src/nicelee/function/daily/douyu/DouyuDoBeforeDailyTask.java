package nicelee.function.daily.douyu;

import java.net.URLEncoder;
import java.util.HashMap;


import nicelee.common.util.HttpRequestUtil;
import nicelee.common.util.Logger;

// 为了获得奖励，做的一些前置小任务
public class DouyuDoBeforeDailyTask {

	HttpRequestUtil util;

	public DouyuDoBeforeDailyTask() {
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
	 * 鱼塘寻宝
	* POST https://www.douyu.com/japi/activepointnc/api/dolottery
	* rid	1738776
	* 
	* Referer	https://www.douyu.com/1738776
	* Content-Type	application/x-www-form-urlencoded; charset=UTF-8
	* X-Requested-With	XMLHttpRequest
	* 
	* {"error":0,"msg":"操作成功","data":{"result":0,"giftLevel":7,"giftId":387,"giftUnit":5,"freeCountRemain":0,"prizeType":2,"giftName":"赞","msg":"恭喜您获得奖品赞 ×5","httpUrl":null,"yuchiRemain":-1.0,"yuwanRemain":-1,"iegRecordId":null},"redirectUrl":"1254,3","enMsg":null}
	* {"error":410004,"msg":"今日次数不足","data":null,"redirectUrl":"1254,3","enMsg":null}
	*/
	
	// https://www.douyu.com/lapi/interact/anchorTask/getIntimateOpenStatus?room_id={}&cate2_id={}

	/**
	 * 用于房间签到的一个补丁
	 */
	public void dotFisherSign(String cookie, String roomId, String url) {
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Accept", "application/json, text/plain, */*");
		headers.put("Referer", "https://www.douyu.com/" + roomId);
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		headers.put("Cookie", cookie);
		headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0");

		String format = "[{\"d\":\"aptl50f9qejx5ar9x52eoypuwwojnkyc\",\"i\":\"224961119\",\"rid\":%s,\"u\":\"%s\",\"ru\":\"/directory/myFollow\",\"ac\":\"click_giftbar_activity\",\"rpc\":\"page_studio_normal\",\"pc\":\"page_studio_normal\",\"pt\":%d,\"oct\":%d,\"dur\":0,\"pro\":\"host_site\",\"ct\":\"web\",\"e\":{\"active_id\":\"223\",\"is_fold\":0,\"rac\":\"msg_server_con\",\"fps\":-1,\"ver\":\"0471f363778cc342f2829a3a44d7573b3a430ae6\",\"dv\":2},\"av\":\"\",\"up\":\"\"}]";
		long tt = System.currentTimeMillis();
		String param = String.format(format, roomId, url, tt - 30000, tt);
		param = String.format("multi=%s&v=1.5", URLEncoder.encode(param));
		String result = util.postContent("https://dotcounter.douyucdn.cn/deliver/fish2", headers, param);
		Logger.println(result);
	}
	
	/**
	 * 用于领取当天荧光棒的一个补丁
	 */
	public void dotFisherClickBag(String cookie, String roomId, String url) {
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Accept", "application/json, text/plain, */*");
		headers.put("Referer", "https://www.douyu.com/" + roomId);
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		headers.put("Cookie", cookie);
		headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0");
		
		String format = "multi=[{\"d\":\"1474ee6ad3fff7616c76b88200011501\",\"i\":\"224961119\",\"rid\":%s,\"u\":\"%s\",\"ru\":\"/directory/myFollow\",\"ac\":\"click_bag\",\"rpc\":\"page_follow\",\"pc\":\"page_studio_normal\",\"pt\":%d,\"oct\":%d,\"dur\":0,\"pro\":\"host_site\",\"ct\":\"web\",\"e\":{\"rac\":\"click_btop_chead_room\",\"fps\":-1,\"ver\":\"0471f363778cc342f2829a3a44d7573b3a430ae6\",\"dv\":2},\"av\":\"\",\"up\":\"\"}]&v=1.5";
		long tt = System.currentTimeMillis();
		String param = String.format(format, roomId, url, tt - 30000, tt);
		String result = util.postContent("https://dotcounter.douyucdn.cn/deliver/fish2", headers, param);
		Logger.println(result);
	}
}
