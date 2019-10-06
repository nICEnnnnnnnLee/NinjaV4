package nicelee.server.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import nicelee.common.annotation.Controller;
import nicelee.common.annotation.Value;
import nicelee.function.mail.MailUtil;

@Controller(path = "/mail", note = "发送163邮件，dex包无法使用")
public class Controller163MailSender {
	
	static ExecutorService mailThreadPool = null;
	@Controller(path = "/send", note = "发送一条消息")
	public String sendMsg(@Value(key = "msg")String msg) {
		try {
			if(msg == null)
				msg = "内容为空";
			msg = URLDecoder.decode(msg, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		synchronized (Controller163MailSender.class) {
			if(mailThreadPool == null)
				mailThreadPool = Executors.newFixedThreadPool(1);
		}
		final String fmsg = msg;
		Runnable run = new Runnable() {
			@Override
			public void run() {
				try {
					MailUtil.send("来自NinjaV4的提示", fmsg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		mailThreadPool.execute(run);
		return "已尝试发送邮件！！";
	}
	
}
