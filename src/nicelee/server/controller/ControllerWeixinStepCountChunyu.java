package nicelee.server.controller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import nicelee.common.annotation.Controller;
import nicelee.common.annotation.Value;
import nicelee.common.util.HttpRequestUtil;
import nicelee.global.GlobalConfig;

@Controller(path = "/chunyu", note = "修改微信计步（春雨医生）")
public class ControllerWeixinStepCountChunyu {

//	public static String postStepUrl = "http://steps.chunyuyisheng.com/robot/p/upload_steps_data/?";
//	public static String refreshCookieUrl = "http://steps.chunyuyisheng.com/api/pedometer/contest_start/?";

	@Controller(path = "/cookieRefresh", note = "延长cookie有效时长")
	public String refreshCookie(BufferedWriter out) throws IOException {
		HttpRequestUtil util = new HttpRequestUtil();
		HashMap<String, String> header = new HashMap<>();
		header.put("Content-Type", "application/x-www-form-urlencoded");
		header.put("Accept-Encoding", "gzip");
		//header.put("Cookie", "sessionid=");
		header.put("User-Agent", "okhttp/2.2.0");
		String result = util.getContent(GlobalConfig.weixin_step_refreshCookieUrl, null);
		System.out.println(result);
		return result; 
	}

	@Controller(path = "/stepcount", note = "修改春雨医生的步数(url参数直传)")
	public String postStep(BufferedWriter out, @Value(key = "step") String step) throws IOException {
		if (step == null)
			return "请传入step参数";

		HttpRequestUtil util = new HttpRequestUtil();

		HashMap<String, String> header = new HashMap<>();
		header.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		header.put("Accept-Encoding", "gzip");
		//header.put("Cookie", "sessionid=");
		header.put("User-Agent", "okhttp/2.2.0");

		//2019-12-09+10:13:06
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd+HH:mm:ss");
		TimeZone tzChina = TimeZone.getTimeZone("GMT+8:00");
		formatter.setTimeZone(tzChina);
		String steps_data = String.format("[{\"time\":\"%s\",\"steps\":%s}]", formatter.format(new Date()), step);
		System.out.println(steps_data);
		String param = "steps_data=" + URLEncoder.encode(steps_data, "UTF-8").replace("%2B", "+");
		System.out.println(param);

		String result = util.postContent(GlobalConfig.weixin_step_postStepUrl, 
				header, param);
		System.out.println(result);
		return result;
	}

//	@Controller(path = "/postStep", note = "通过代理拦截获取postStepUrl", specificPath="http://steps.chunyuyisheng.com/robot/p/upload_steps_data/")
//	public String setPostStepUrl(BufferedWriter out, @Value(key = "paramData") String param) throws IOException {
//		if(param != null) {
//			postStepUrl = "http://steps.chunyuyisheng.com/robot/p/upload_steps_data/?" + param;
//		}
//		System.out.println(postStepUrl);
//		return postStepUrl; 
//	}

	@Controller(path = "/setParam", note = "通过代理拦截获取相关Url", specificPath="http://steps.chunyuyisheng.com/api/pedometer/contest_start/")
	public String setRefreshCookieUrlUrl(BufferedWriter out, @Value(key = "paramData") String param) throws IOException {
		if(param != null && !param.trim().isEmpty()) {
			GlobalConfig.weixin_step_refreshCookieUrl = "http://steps.chunyuyisheng.com/api/pedometer/contest_start/?" + param;
			param = param.replaceFirst("union_id=[^&]*&?", "")
					.replaceFirst("nickname=[^&]*&?", "")
					.replaceFirst("figure=[^&]*&?", "");
			GlobalConfig.weixin_step_postStepUrl = "http://steps.chunyuyisheng.com/robot/p/upload_steps_data/?" + param;
		}
		System.out.println(GlobalConfig.weixin_step_refreshCookieUrl);
		System.out.println(GlobalConfig.weixin_step_postStepUrl);
		return GlobalConfig.weixin_step_refreshCookieUrl; 
	}

	@Controller(path = "/print", note = "打印相关url")
	public String printUrls(BufferedWriter out, @Value(key = "paramData") String param) throws IOException {
		System.out.println(GlobalConfig.weixin_step_refreshCookieUrl);
		System.out.println(GlobalConfig.weixin_step_postStepUrl);
		out.write("refreshCookieUrl: ");
		out.write("\r\n<br/>");
		out.write(GlobalConfig.weixin_step_refreshCookieUrl);
		out.write("\r\n<br/>");
		out.write("\r\n<br/>");
		out.write("postStepUrl: ");
		out.write("\r\n<br/>");
		out.write(GlobalConfig.weixin_step_postStepUrl);
		out.write("\r\n<br/>");
		return null; 
	}
}