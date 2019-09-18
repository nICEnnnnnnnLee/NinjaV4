package nicelee.server.controller;

import java.io.BufferedWriter;
import java.io.IOException;

import nicelee.common.annotation.Controller;
import nicelee.common.annotation.Value;
import nicelee.function.live_recorder.RecorderManager;

@Controller(path = "/live", note = "直播录制相关")
public class ControllerLiveRecorder {

	@Controller(path = "/test", note = "测试")
	public String test(@Value(key = "param") String param, @Value(key = "_t") String time) {
		System.out.println("执行controller - /test?param=" + param + "&_t=" + System.currentTimeMillis() / 1000);
		return "test:" + System.currentTimeMillis() / 1000;
	}
	
	@Controller(path = "/test_bili", note = "测试bilibili视频录制")
	public String test_bili(BufferedWriter out) {
		RecorderManager.startRecord(out, "bili", "903363", "10000");
		return null;
	}
	
	@Controller(path = "/test_douyu", note = "测试douyu视频录制")
	public String test_douyu(BufferedWriter out) {
		RecorderManager.startRecord(out, "douyu", "4090043", "0");
		return null;
	}

	@Controller(path = "/status", note = "查询当前录制状态")
	public String status(BufferedWriter out) {
		try {
			out.write("查询当前录制状态<br/>");
		} catch (IOException e) {
		}
		RecorderManager.currentStatus(out);
		return null;
	}

	@Controller(path = "/stopAll", note = "停止所有录制")
	public String stop(BufferedWriter out) {
		RecorderManager.stopRecord(out);
		return null;
	}

	@Controller(path = "/stop", note = "停止录制, 需要传入liver/id")
	public String stop(BufferedWriter out, @Value(key = "liver") String liver, @Value(key = "id") String id) throws IOException {
		if (liver != null && id != null) {
			RecorderManager.stopRecord(out, liver, id);
		}else {
			out.write("需传入liver/id");
		}
		return null;
	}

	@Controller(path = "/start", note = "开启录制，需传入liver/id/qn")
	public String start(BufferedWriter out, @Value(key = "liver") String liver, @Value(key = "id") String id,
			@Value(key = "qn") String qn) throws IOException {
		if (liver != null && id != null && qn != null) {
			RecorderManager.startRecord(out, liver, id, qn);
		} else {
			out.write("需传入liver/id/qn");
		}
		return null;
	}

	@Controller(path = "/qn", note = "关于各网站常见qn")
	public String start(BufferedWriter out) throws IOException {
		out.append("bili:<br/>\r\n");
		out.append("10000:原画<br/>\r\n");
		out.append("150 :高清(可能没有)<br/>\r\n");
		out.append("<hr/>\r\n");
		out.append("douyu:<br/>\r\n");
		out.append("模糊到清晰 1，2，... 0     流畅  高清 超清 蓝光4M<br/>\r\n");
		out.append("<hr/>\r\n");
		out.append("huya:<br/>\r\n");
		out.append("0 : 蓝光4M<br/>\r\n");
		out.append("2000 : 超清<br/>\r\n");
		out.append("500 : 流畅<br/>\r\n");
		out.append("<hr/>\r\n");
		out.append("kuaishou:<br/>\r\n");
		out.append("从小到大，从清晰到模糊 0,1,2... 0最清晰，取个较大的数字，会优先选择最模糊的那个M<br/>\r\n");
		return null;
	}
}
