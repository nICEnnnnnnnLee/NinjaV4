package nicelee.server.controller;

import java.io.BufferedWriter;
import java.io.IOException;

import nicelee.common.annotation.Controller;
import nicelee.function.daily.douyu.DouyuLogin;

@Controller(path = "/douyu/login", note = "登录斗鱼账号，获取cookies")
public class ControllerDouyuLogin {

	static String plan = "";

	@Controller(path = "/", note = "发起登录线程")
	public String login(BufferedWriter out) {
		DouyuLogin thread = new DouyuLogin();
		thread.setDaemon(true);
		thread.start();
		try {
			out.write("<html><head><title>发起登录线程</title></head><body><h1>");
			out.write("<a href='/douyu/login/qrcode'>请等待10秒时间，再点击链接前往扫码</a>");
			out.write("</h1></body></html>");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

//	@Controller(path = "/scan", note = "扫码跳转页面")
//	public String scan(BufferedWriter out) {
//		try {
//			out.write("<html><head><title>扫码跳转页面</title></head><body><h1>");
//			out.write("<a href='/douyu/login/qrcode?file'>请点击链接前往扫码</a>");
//			out.write("</h1></body></html>");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}

	@Controller(path = "/genBarCookies", note = "根据已有cookie，获取鱼吧cookie")
	public String genBarCookies(BufferedWriter out) {
		DouyuLogin login = new DouyuLogin();
		return login.authFishBar();
	}

	@Controller(path = "/qrcode.min.js", note = "获取qrcode.min.js")
	public String qrcodeJs(BufferedWriter out) {
		try {
			out.write(DouyuLogin.qrcodeJs);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Controller(path = "/qrcode", note = "扫码图片")
	public String test(BufferedWriter out) {
		try {
			if(DouyuLogin.getCurrentScanUrl() == null) {
				out.write("扫码url尚未生成！！！");
			}else {
				out.write(
						"<html><head><title>扫码图片</title></head><body><script type=\"text/javascript\" src=\"/douyu/login/qrcode.min.js\"></script>");
				out.write("<div id=\"qrcode\" style='margin:0 auto;width: 256; border:1px solid #F00'></div><script type=\"text/javascript\">\r\n");
				out.write("var qrcode = new QRCode(document.getElementById(\"qrcode\"), {\r\n	text: \"");
				out.write(DouyuLogin.getCurrentScanUrl());
				out.write("\",\r\n	width: 256,height: 256});");
				out.write("</script></body></html>");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
