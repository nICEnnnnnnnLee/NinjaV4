package nicelee.function.ipscan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import nicelee.global.GlobalConfig;

/**
 * 
 * 实现对mac地址dealResult(Map<String, String> resultMap)
 */
public abstract class IpScanner {

	/**
	 * 获取局域网中的 存在的ip地址及对应的mac
	 */
	public void startScan() {

		// 获取本机所在的局域网地址
		String hostIP = getHostIP();
		System.out.printf("本机IP为： %s\n", hostIP);
		int lastIndexOf = hostIP.lastIndexOf(".");
		final String substring = hostIP.substring(0, lastIndexOf + 1);
		DatagramPacket dp = new DatagramPacket(new byte[0], 0, 0);
		DatagramSocket socket;
		try {
			boolean inList = false;
			for (String prefix : GlobalConfig.ipPrefixs) {
				if (prefix.equals(substring)) {
					inList = true;
				}
				socket = new DatagramSocket();
				int position = 2;
				while (position < 255) {
					dp.setAddress(InetAddress.getByName(prefix + String.valueOf(position)));
					socket.send(dp);
					position++;
					if (position == 125) {// 分两段掉包，一次性发的话，达到236左右，会耗时3秒左右再往下发
						socket.close();
						socket = new DatagramSocket();
					}
				}
				socket.close();
			}
			if (!inList) {
				socket = new DatagramSocket();
				int position = 2;
				while (position < 255) {
					dp.setAddress(InetAddress.getByName(substring + String.valueOf(position)));
					socket.send(dp);
					position++;
					if (position == 125) {// 分两段掉包，一次性发的话，达到236左右，会耗时3秒左右再往下发
						socket.close();
						socket = new DatagramSocket();
					}
				}
				socket.close();
			}

			execCatForArp();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 执行 cat命令 查找android 设备arp表 arp表 包含ip地址和对应的mac地址
	 */
	private void execCatForArp() {
		try {
			final ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
			// 分系统进行判断
			String os = System.getProperty("os.name").toLowerCase();
			if(os.startsWith("win")) {
				// windows只考虑动态ip对应的MAC, 假设cmd为GBK编码
				Process exec = Runtime.getRuntime().exec("arp -a");
				InputStream is = exec.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "GBK"));
				String line;
				while ((line = reader.readLine()) != null) {
					if (line.contains("动态")) {
						String[] split = line.split("\\s+");
						map.put(split[2].replace("-", ":"), split[1].replace("-", ":"));// mac ip
					}
				}
			}else {
				Process exec = Runtime.getRuntime().exec("cat /proc/net/arp");
				InputStream is = exec.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				String line;
				while ((line = reader.readLine()) != null) {
					if (!line.contains("00:00:00:00:00:00") && !line.contains("IP")) {
						String[] split = line.split("\\s+");
						map.put(split[3], split[0]);// mac ip
					}
				}
			}
			// 处理结果
			dealResult(map);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取本机 ip地址
	 *
	 * @return
	 */
	private String getHostIP() {

		String hostIp = null;
		try {
			Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
			InetAddress ia;
			while (nis.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) nis.nextElement();
				Enumeration<InetAddress> ias = ni.getInetAddresses();
				while (ias.hasMoreElements()) {
					ia = ias.nextElement();
					if (ia instanceof Inet6Address) {
						continue;// skip ipv6
					}
					String ip = ia.getHostAddress();
					if (!"127.0.0.1".equals(ip)) {
						hostIp = ia.getHostAddress();
						break;
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return hostIp;

	}

	/**
	 * 查询的结果在Map中，Mac地址为Key，IP为Value
	 * 
	 * @param resultMap <Mac, IP>
	 */
	public abstract void dealResult(Map<String, String> resultMap);
}