package nicelee.function.magnet.magnet.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nicelee.common.util.HttpRequestUtil;
import nicelee.function.live_recorder.util.HttpHeaders;
import nicelee.function.magnet.domain.Magnet;
import nicelee.function.magnet.domain.MediaInfo;
import nicelee.function.magnet.magnet.IMagnetUtil;
import nicelee.global.GlobalConfig;


public class JAVBusMagnet implements IMagnetUtil {

	String host;

	public JAVBusMagnet() {
		this.host = GlobalConfig.magnetDomain;
	}

	public JAVBusMagnet(String host) {
		this.host = host;
	}

	public List<Magnet> getMagnetsById(String avId) {
		String url = String.format("https://%s/%s", host, avId);
		return getMagnetsByUrl(url);
	}

	public List<Magnet> getMagnetsByUrl(String url) {
		List<Magnet> magnets = new ArrayList<Magnet>();

		// 访问url，获取参数
		HttpRequestUtil util = new HttpRequestUtil();
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:73.0) Gecko/20100101 Firefox/73.0");
		headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		headers.put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
		headers.put("Accept-Encoding", "gzip, deflate");
		String html = util.getContent(url, headers);
		//System.out.println(html);
		Pattern pattern = Pattern.compile(
				"<script>[ \t\r\n]+var[ \t\r\n]+gid[ \t\r\n]+=[ \t\r\n]+([0-9]+);[ \t\r\n]+var[ \t\r\n]+uc[ \t\r\n]+=[ \t\r\n]+([0-9])+;[ \t\r\n]+var[ \t\r\n]+img[ \t\r\n]+=[ \t\r\n]+'([^']+)';");
		Matcher matcher = pattern.matcher(html);
		if (matcher.find()) {
			String host = HttpHeaders.getHost(url);
			String gid = matcher.group(1);
			String uc = matcher.group(2);
			String img = matcher.group(3);
			int floor = (int) (Math.random() * 1000 + 1);

			// 根据参数构造请求
			String ajaxUrl = String.format(
					"https://%s/ajax/uncledatoolsbyajax.php?gid=%s&lang=zh&img=%s&uc=%s&floor=%d", host, gid, img, uc,
					floor);
			HashMap<String, String> ajaxHeaders = new HashMap<String, String>();
			ajaxHeaders.put("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:73.0) Gecko/20100101 Firefox/73.0");
			ajaxHeaders.put("Accept", "*/*");
			ajaxHeaders.put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
			ajaxHeaders.put("Accept-Encoding", "gzip, deflate");
			ajaxHeaders.put("X-Requested-With", "XMLHttpRequest");
			ajaxHeaders.put("TE", "Trailers");
			ajaxHeaders.put("Referer", url);
			// System.out.println(ajaxUrl);
			String result = util.getContent(ajaxUrl, ajaxHeaders);
			// System.out.println(result);
			MediaInfo media = new MediaInfo();
			media.setImg(img);
			
			Pattern pMediaTitle = Pattern.compile("<h3>(.*)</h3>");
			matcher = pMediaTitle.matcher(html);
			matcher.find();
			media.setTitle(matcher.group(1));
			
			Pattern pMediaActors = Pattern.compile("star-name[\\s\\S]*?<a[\\s\\S]*?>([\\s\\S]*?)</a>");
			matcher = pMediaActors.matcher(html);
			StringBuilder sb = new StringBuilder();
			while(matcher.find()) {
				sb.append(matcher.group(1)).append(" ");
			}
			media.setActors(sb.toString().trim());
			/*
			 * <tr> <td><a ...> </a></td> <td><a ...> </a></td> <td><a ...> </a></td> </tr>
			 */
			Pattern pMagnet = Pattern.compile(
					"<tr[\\s\\S]*?<td [\\s\\S]*?'(magnet:[^']+)'[\\s\\S]*?<a[^<>]*>([^<>]+)[\\s\\S]*?<td [\\s\\S]*?<a[^<>]*>([^<>]+)[\\s\\S]*?<td [\\s\\S]*?<a[^<>]*>([^<>]+)");// [^>]*>
			Matcher mMagnet = pMagnet.matcher(result);
			while (mMagnet.find()) {
				// System.out.println(mMagnet.group(2).trim());
				Magnet mag = new Magnet(media, mMagnet.group(2).trim(), mMagnet.group(1), mMagnet.group(3).trim(),
						mMagnet.group(4).trim());
				magnets.add(mag);
			}

		}
		return magnets;
	}

}
