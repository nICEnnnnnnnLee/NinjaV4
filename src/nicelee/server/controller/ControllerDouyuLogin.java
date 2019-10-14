package nicelee.server.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import nicelee.common.Base64;
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

	@Controller(path = "/qrcode", note = "扫码图片")
	public String qrCode(BufferedWriter out) {
		try {
			out.write("<html><head><title>扫码图片</title></head><body><div style='margin:0 auto;width: 80%;max-width: 500px; border:1px solid #F00'>");
			out.write("<img style=\"width:100%\" src=\"data:image/jpeg;base64,");//iVBORw0KGgo=...\" />");
			
			byte[] buffer = new byte[1024 * 3]; // 3的倍数
			RandomAccessFile raf = new RandomAccessFile(new File("config/qrcode.jpg"), "r");
			long size = raf.read(buffer);
			while (size > -1) {
				if (size == buffer.length) {
					out.write(Base64.getEncoder().encodeToString(buffer));
				} else {
					byte tmp[] = new byte[(int) size];
					System.arraycopy(buffer, 0, tmp, 0, (int) size);
					out.write(Base64.getEncoder().encodeToString(tmp));
				}
				size = raf.read(buffer);
			}
			raf.close();
			out.write("\" />");
			out.write("</div></h1></body></html>");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	@Controller(path = "/genBarCookies", note = "根据已有cookie，获取鱼吧cookie")
	public String genBarCookies(BufferedWriter out) {
		DouyuLogin login = new DouyuLogin();
		return login.authFishBar();
	}
}
