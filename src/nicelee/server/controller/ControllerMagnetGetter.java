package nicelee.server.controller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;

import nicelee.common.annotation.Controller;
import nicelee.common.annotation.Value;
import nicelee.function.magnet.domain.Magnet;
import nicelee.function.magnet.domain.MediaInfo;
import nicelee.function.magnet.magnet.IMagnetUtil;
import nicelee.function.magnet.magnet.impl.JAVBusMagnet;

@Controller(path = "/magnet", note = "获取媒体信息")
public class ControllerMagnetGetter {

	@Controller(path = "/", note = "---")
	public String login(BufferedWriter out) {
		
		try {
			out.write("<html><head><title>媒体信息获取</title></head><body>");
			out.write("<h4 style=\"text-align: center;\">请输入id或者链接</h4>");
			out.write("<div style=\"width: 50%; height: 40px;margin: 10 auto\"><input id=\"key\" type=\"text\" style=\"width: 100%; height: 40px;\"></input></div>");
			out.write("<div style=\"width: 80px;height: 40px;margin: 5 auto;\"><button  type=\"button\" onClick='window.open(\"av?key=\" + document.getElementById(\"key\").value,\"_blank\"); '>Start!</button></div>");
			out.write("</body></html>");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Controller(path = "/av", note = "获取详细信息")
	public String qrcodeJs(BufferedWriter out, @Value(key = "key") String key) {
		try {
			IMagnetUtil util = new JAVBusMagnet();
			List<Magnet> magnets = null;
			key = URLDecoder.decode(key, "UTF-8");
			if(key.startsWith("http")) {
				magnets = util.getMagnetsByUrl(key);
			}else {
				magnets = util.getMagnetsById(key);
			}
			if (magnets.size() == 0) {
				out.write("<html><head><title>网络错误或未找到</title></head><body><h1>");
				out.write("请刷新重试");
				out.write("</h1></body></html>");
			} else {
				out.write("<html><head><title>" + magnets.get(0).getInfo().getTitle() +"</title></head><body>");
				
				for (int i = 0; i < magnets.size(); i++) {
					Magnet magnet = magnets.get(i);
					if (i == 0) {
						MediaInfo info = magnet.getInfo();
						out.write("<h3>");
						out.write(info.getTitle());
						out.write("</h3>");
						
						out.write("<div><a href='");
						out.write(info.getImg());
						out.write("' target='_blank'>预览图</a></div>");
						
						out.write("<div><span>");
						out.write(info.getActors());
						out.write("</span></div><hr/><div><ul>");
					}
					out.write("<li>");
					out.write("<div style=\"margin: 5 25;\">" +magnet.getTitle() +"</div>");
					out.write("<span style=\"margin: 5 25;\">" +magnet.getTime() +"</span>");
					out.write("<span style=\"margin: 5 25;\">" +magnet.getSize() +"</span>");
					
					out.write("<div style=\"margin: 5 25 20 25;\"><a href=\"");
					out.write(magnet.getLink());
					out.write("\">");
					out.write(magnet.getLink());
					out.write("</a></div>");
					out.write("</li>");
				}
				out.write("</ul></div></body></html>");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
