package nicelee.server.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import org.json.JSONObject;

import nicelee.common.annotation.Controller;
import nicelee.common.annotation.Value;
import nicelee.function.live_recorder.util.HttpRequestUtil;
import nicelee.global.GlobalConfig;

/***
 * 
 * 配合酷Q + HTTPAPI插件(https://github.com/richardchien/coolq-http-api)
 * 假设插件配置：
 * port = 5701
 * post_url = http://127.0.0.1:8888/qqrobot
 *
 */
@Controller(path = "/qqrobot", note = "QQ机器人")
public class ControllerQQRobot {
	final static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	final static TimeZone tzChina = TimeZone.getTimeZone("GMT+8:00");
	
	@Controller(path = "/sendMsg", note = "发送一条消息")
	public String sendMsg(@Value(key = "msg")String msg,@Value(key = "id")String id) {
		try {
			if(id == null)
				return "需要输入id";
			if(msg == null)
				return "需要输入msg";
			msg = URLDecoder.decode(msg, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "不支持解码当前msg";
		}
		HttpRequestUtil util = new HttpRequestUtil();
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		JSONObject obj = new JSONObject();
		obj.put("message_type", "private");//private、group、discuss
		obj.put("user_id", id);
//		obj.put("group_id", id);
//		obj.put("discuss_id", id);
		obj.put("message", msg);
		obj.put("auto_escape", false);
		System.out.println(obj.toString());
		String result = util.postContent(GlobalConfig.coolQ_httpApi_Addr +"/send_msg", headers, obj.toString());
//		String result = util.postContent("http://127.0.0.1:8888/qqrobot", headers, obj.toString());
		return result;
	}
	
	
	@Controller(path = "", note = "ai的本质是回复机")
	public String autoReply(@Value(key = "postData")String postData) {
		if(postData == null)
			return "请使用正确的POST方式！！";
		JSONObject json = new JSONObject(postData);
		System.out.println(postData);
		// 只处理message消息
		String postType = json.optString("post_type");
		if(!"message".equals(postType))
			return null;
		
		// 只处理私聊消息和正常群消息
		String msgType = json.optString("message_type");
		if("private".equals(msgType)) {
			formatter.setTimeZone(tzChina);
			String time = formatter.format(new Date(json.optLong("time")));
			String senderQQ = "" + json.optLong("user_id");
			String senderName = json.optJSONObject("sender").optString("nickname");
			String msg = json.optString("message");
			System.out.printf("%s(%s) %s: %s\n", senderName, senderQQ, time, msg);
			JSONObject obj = new JSONObject();
			obj.put("reply", msg);
			obj.put("auto_escape", false);
			return obj.toString();
		}else if("group".equals(msgType) && "normal".equals(json.optString("sub_type"))) {//normal、anonymous、notice
			formatter.setTimeZone(tzChina);
			String time = formatter.format(new Date(json.optLong("time")));
			String groupId = "" + json.optLong("group_id");
			String senderQQ = "" + json.optLong("user_id");
			String senderName = json.optJSONObject("sender").optString("nickname");
			String senderRemark = json.optJSONObject("sender").optString("card");
			if(senderRemark != null && !senderRemark.trim().isEmpty())
				senderName = senderRemark;
			String msg = json.optString("message");
			System.out.printf("来自群%s - %s(%s) %s: %s\n", groupId, senderName, senderQQ, time, msg);
			JSONObject obj = new JSONObject();
			obj.put("reply", msg);
			obj.put("auto_escape", false);
			return obj.toString();
		}
		return null;
	}
	
}
