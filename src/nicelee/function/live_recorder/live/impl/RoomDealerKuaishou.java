package nicelee.function.live_recorder.live.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import nicelee.function.live_recorder.live.RoomDealer;
import nicelee.function.live_recorder.live.domain.RoomInfo;
import nicelee.function.live_recorder.util.HttpCookies;


public class RoomDealerKuaishou extends RoomDealer {

	final public static String liver = "kuaishou";

	public RoomDealerKuaishou() {
		cookie = "kuaishou.live.bfb1s=9b8f70844293bed778aade6e0a8f9942; didv=1568782105000; clientid=3; did=web_7ea498bd37fe43f7b47b8de1b871405e; client_key=65890b29";
	}
	
	@Override
	public String getType() {
		return ".flv";
	}

	/**
	 * https://live.kuaishou.com/u/shortId
	 * 根据url的shortId获取房间信息(从网页里面爬)
	 * 
	 * @param shortId
	 * @return
	 */
	@Override
	public RoomInfo getRoomInfo(String shortId) {
		RoomInfo roomInfo = new RoomInfo();
		roomInfo.setShortId(shortId);
		try {
			// 获取基础信息
			String basicInfoUrl = String.format("https://live.kuaishou.com/profile/%s",
					shortId);
			String html = util.getContent(basicInfoUrl, headers.getCommonHeaders("live.kuaishou.com"), HttpCookies.convertCookies(cookie));
			Pattern pat = Pattern.compile("window.__APOLLO_STATE__ ?= ?(.*?});");
			Matcher matcher = pat.matcher(html);
			matcher.find();
			
			JSONObject user = new JSONObject(matcher.group(1)).getJSONObject("defaultClient").getJSONObject("User:" + shortId);
			
			// 直播状态信息
			int begin = html.indexOf("<div class=\"live-card\""), end;
			String roomStatus = null;
			if(begin > -1) {
				end = html.indexOf("</div>", begin);
				roomStatus = html.substring(begin, end);
				if(roomStatus.contains("直播中"))
					roomInfo.setLiveStatus(1);
				else
					roomInfo.setLiveStatus(0);
			}else {
				roomInfo.setLiveStatus(0);
			}
			
			// 真实房间id
			roomInfo.setRoomId(shortId);
			// 房间主id
			roomInfo.setUserId(user.getLong("userId"));
			// 房间主名称
			roomInfo.setUserName(user.getString("name"));
			
			// 直播描述
			roomInfo.setDescription(user.getString("description"));
			
			if(roomInfo.getLiveStatus() == 1) {
				JSONObject live = getLiveInfoObj(shortId);
				roomInfo.setTitle(live.getString("caption"));
				System.out.println("title: " + roomInfo.getTitle());
				// 清晰度
				JSONArray jArray = live.getJSONArray("playUrls");
				String[] qn = new String[jArray.length()];
				String[] qnDesc = new String[jArray.length()];
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject objTemp = jArray.getJSONObject(i);
					qn[i] = "" + i;
					qnDesc[i] = objTemp.getString("quality");
				}
				roomInfo.setAcceptQuality(qn);
				roomInfo.setAcceptQualityDesc(qnDesc);
			}else {
			}

			roomInfo.print();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return roomInfo;
	}

	/**
	 * 获取直播地址的下载链接
	 * 
	 * @param roomId
	 * @param qn
	 * @return
	 */
	@Override
	public String getLiveUrl(String roomId, String qn, Object... params) {
		try {
			JSONObject obj = getLiveInfoObj(roomId);
			JSONArray array = obj.getJSONArray("playUrls");
			int order = 0;
			try {
				order = Integer.parseInt(qn);
				if(order > array.length() -1) {
					order = array.length() -1;
					System.out.println("没有匹配的清晰度，选取最模糊值：" + array.getJSONObject(0).getString("quality"));
				}
			}catch (Exception e) {
				System.out.println("没有匹配的清晰度，选取最清晰值：" + array.getJSONObject(0).getString("quality"));
			}
			return array.getJSONObject(order).getString("url");
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @param roomId
	 * @return
	 */
	private JSONObject getLiveInfoObj(String roomId) {
		String graphSqlUrl = String.format("https://live.kuaishou.com/graphql");

		StringBuffer param = new StringBuffer();
		param.append("{\"operationName\":\"LiveDetail\",\"variables\":{\"principalId\":\"");
		param.append(roomId);
		param.append(
				"\"},\"query\":\"query LiveDetail($principalId: String) {\\n  liveDetail(principalId: $principalId) {\\n    liveStream\\n    feedInfo {\\n      pullCycleMillis\\n      __typename\\n    }\\n    watchingInfo {\\n      likeCount\\n      watchingCount\\n      __typename\\n    }\\n    noticeList {\\n      feed\\n      options\\n      __typename\\n    }\\n    fastComments\\n    commentColors\\n    moreRecommendList {\\n      user {\\n        id\\n        profile\\n        name\\n        __typename\\n      }\\n      watchingCount\\n      src\\n      title\\n      gameId\\n      gameName\\n      categoryId\\n      liveStreamId\\n      playUrls {\\n        quality\\n        url\\n        __typename\\n      }\\n      quality\\n      gameInfo {\\n        category\\n        name\\n        pubgSurvival\\n        type\\n        kingHero\\n        __typename\\n      }\\n      redPack\\n      liveGuess\\n      expTag\\n      __typename\\n    }\\n    __typename\\n  }\\n}\\n\"}\r\n");

		String json = util.postContent(graphSqlUrl, headers.getKuaishouHeaders("https://live.kuaishou.com/u/" + roomId),
				param.toString(), HttpCookies.convertCookies(cookie));
		System.out.println(json);
		JSONObject obj = new JSONObject(json).getJSONObject("data").getJSONObject("liveDetail").getJSONObject("liveStream");
		return obj;
	}

	@Override
	public void startRecord(String url, String fileName, String shortId) {
		util.download(url, fileName + ".flv", headers.getKuaishouLiveRecordHeaders(url, shortId));
	}

	@Override
	public String getLiver() {
		return liver;
	}
}
