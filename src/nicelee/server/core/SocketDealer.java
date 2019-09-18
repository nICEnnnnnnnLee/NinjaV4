package nicelee.server.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SocketDealer extends PathDealer implements Runnable {

	Socket socketClient;

	// 与客户端之间的联系
	BufferedReader in;
	BufferedWriter out;

	public SocketDealer(Socket socketClient) {
		this.socketClient = socketClient;
	}

	final static Pattern urlPattern = Pattern.compile("^GET ([^ \\?]+)\\??([^ \\?]*) HTTP.*$");
	@Override
	public void run() {
		String path = null, param = null;
		try {
			in = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()));
			
			// 读取url请求
			String line = null;
			while ((line = in.readLine()) != null) {
				Matcher matcher = urlPattern.matcher(line);
				if(path == null && matcher.find()) {
					System.out.println("正在处理请求: " + line);
					path = matcher.group(1);
					param = matcher.group(2);
				}
				if(line.length() == 0)
					break;
			}
			
			// 返回结果
			out.write("HTTP/1.1 200 OK\r\n");
			out.write("Content-Type: text/html; charset=UTF-8\r\n");
//			out.write("Content-Length: "+ html.length()+ "\r\n");
			out.write("\r\n");
			// 处理请求并返回内容
			dealRequest(out, path, param);
			out.write("\r\n");
			out.flush();
			
		} catch (SocketException e) {
		} catch (IOException e) {
		} catch (IndexOutOfBoundsException e) {
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//System.out.println(path + " -线程结束...");
			try {
				in.close();
			} catch (Exception e) {
			}
			try {
				out.close();
			} catch (Exception e) {
			}
			try {
				socketClient.close();
			} catch (Exception e) {
			}
		}
	}
	
}
