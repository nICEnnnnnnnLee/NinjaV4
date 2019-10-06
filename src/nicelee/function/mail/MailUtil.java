package nicelee.function.mail;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import nicelee.global.GlobalConfig;

public class MailUtil {
	// 发件人地址
	public static String senderAddress = null;
	// 收件人地址
	public static String recipientAddress = null;
	// 发件人账户名
	public static String senderAccount = null;
	// 发件人账户密码
	public static String senderPassword = null;

	public static void main(String[] args) throws Exception {
		send("test", "haha");
	}

	public static void init() {
		if(senderAddress == null) {
			senderAddress = GlobalConfig.mail_senderAddress;
			recipientAddress = GlobalConfig.mail_recipientAddress;
			senderAccount = GlobalConfig.mail_senderAccount;
			senderPassword = GlobalConfig.mail_senderPassword;
		}
	}
	/**
	 * 
	 * @param topic
	 * @param content
	 * @throws Exception
	 */
	public static boolean send(String topic, String content) throws Exception {
		init();
		Transport transport = null;
		try {
			System.out.println("邮件发送开始");
			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.transport.protocol", "smtp");
			props.put("mail.smtp.host", "smtp.163.com");
//			props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//			props.setProperty("mail.smtp.socketFactory.fallback", "false");
//			props.setProperty("mail.smtp.port", "465");
//			props.setProperty("mail.smtp.socketFactory.port", "465");

			Session session = Session.getInstance(props);
//			session.setDebug(true);
			Message msg = getMimeMessage(session, topic, content);
			transport = session.getTransport();
			transport.connect(senderAccount, senderPassword);
			// transport.sendMessage(msg, msg.getAllRecipients());
			transport.sendMessage(msg, new InternetAddress[] { new InternetAddress(recipientAddress) });
			System.out.println("邮件发送成功");
			return true;
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("邮件发送失败");
			return false;
		}finally {
			
			if(transport != null)
				transport.close();
		}
	}

	/**
	 * 获得创建一封邮件的实例对象
	 * 
	 * @param session
	 * @return
	 * @throws MessagingException
	 * @throws AddressException
	 */
	public static MimeMessage getMimeMessage(Session session, String topic, String content) throws Exception {
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(senderAddress));
		msg.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(recipientAddress));
		msg.setSubject(topic, "UTF-8");
		msg.setContent(content, "text/html;charset=UTF-8");
		msg.setSentDate(new Date());
		return msg;
	}
}
