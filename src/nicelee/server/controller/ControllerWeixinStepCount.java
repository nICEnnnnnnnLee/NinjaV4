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

@Controller(path = "/weixin", note = "修改微信计步（春雨医生）")
public class ControllerWeixinStepCount {

	public static String postStepUrl = "http://steps.chunyuyisheng.com/robot/p/upload_steps_data/?";
	public static String refreshCookieUrl = "http://steps.chunyuyisheng.com/api/pedometer/contest_start/?";
	
	@Controller(path = "/cookieRefresh", note = "延长cookie有效时长")
	public String refreshCookie(BufferedWriter out) throws IOException {
		HttpRequestUtil util = new HttpRequestUtil();
		HashMap<String, String> header = new HashMap<>();
		header.put("Content-Type", "application/x-www-form-urlencoded");
		header.put("Accept-Encoding", "gzip");
		//header.put("Cookie", "sessionid=");
		header.put("User-Agent", "okhttp/2.2.0");
//		String result = util.getContent("http://steps.chunyuyisheng.com/api/pedometer/contest_start/?union_id=oONgnuBH8uIkp19qMYitJBM_byxU&nickname=%EE%84%BC+%D2%88l%D2%88i%D2%88i%D2%88i%D2%88i%D2%88i%D2%88j%D2%88i%D2%88a%D2%88&figure=http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83erKNlEn95C4Zke63ib3SiaPoKqzD8DN8aPyqyIaG797wAwLCnQeeKe1Yga7TIKUdAwaZPHhXvCkKCLA/132&app=7&platform=android&systemVer=8.0.0&version=2.5.4&app_ver=2.5.4&imei=865968031285267&device_id=865968031285267&mac=50%3A04%3Ab8%3A2e%3A1c%3A00&secureId=43c30a49b7bdaac9&installId=1553228055224&phoneType=DUK-AL20_by_HUAWEI&vendor=zhihuiyun&u=fbc67f3e9ac9159aa9dd66a739635e47", null);
		String result = util.getContent(refreshCookieUrl, null);
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
		
//		String result = util.postContent("http://steps.chunyuyisheng.com/robot/p/upload_steps_data/?app=7&platform=android&systemVer=8.0.0&version=2.5.4&app_ver=2.5.4&imei=865968031285267&device_id=865968031285267&mac=50%3A04%3Ab8%3A2e%3A1c%3A00&secureId=43c30a49b7bdaac9&installId=1553228055224&phoneType=DUK-AL20_by_HUAWEI&vendor=zhihuiyun&u=fbc67f3e9ac9159aa9dd66a739635e47", 
		String result = util.postContent(postStepUrl, 
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
			refreshCookieUrl = "http://steps.chunyuyisheng.com/api/pedometer/contest_start/?" + param;
			param = param.replaceFirst("union_id=[^&]*&?", "")
					.replaceFirst("nickname=[^&]*&?", "")
					.replaceFirst("figure=[^&]*&?", "");
			postStepUrl = "http://steps.chunyuyisheng.com/robot/p/upload_steps_data/?" + param;
		}
		System.out.println(refreshCookieUrl);
		System.out.println(postStepUrl);
		return refreshCookieUrl; 
	}
	
	@Controller(path = "/print", note = "打印相关url")
	public String printUrls(BufferedWriter out, @Value(key = "paramData") String param) throws IOException {
		System.out.println(refreshCookieUrl);
		System.out.println(postStepUrl);
		out.write("refreshCookieUrl: ");
		out.write("\r\n<br/>");
		out.write(refreshCookieUrl);
		out.write("\r\n<br/>");
		out.write("\r\n<br/>");
		out.write("postStepUrl: ");
		out.write("\r\n<br/>");
		out.write(postStepUrl);
		out.write("\r\n<br/>");
		return null; 
	}
}
