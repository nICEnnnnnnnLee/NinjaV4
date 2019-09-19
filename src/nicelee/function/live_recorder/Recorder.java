package nicelee.function.live_recorder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nicelee.PackageScanLoader;
import nicelee.function.live_recorder.enums.StatusEnum;
import nicelee.function.live_recorder.live.FlvChecker;
import nicelee.function.live_recorder.live.RoomDealer;
import nicelee.function.live_recorder.live.domain.RoomInfo;
import nicelee.function.live_recorder.live.impl.RoomDealerBilibili;
import nicelee.function.live_recorder.util.Logger;
import nicelee.global.GlobalConfig;

public class Recorder {

	final static String version = "v1.8";
	boolean autoCheck;
	boolean deleteOnchecked;
	int maxFailCnt;

	RoomDealer roomDealer; // 主要功能实现类
	List<String> fileList; // 用于存放下载的文件
	long beginTime; // 记录开始下载的时间
	public String title; // 简要描述

	public Recorder() {
		autoCheck = true;
		deleteOnchecked = GlobalConfig.deleteOnchecked;
		maxFailCnt = 5;
		Logger.debug = true;
		fileList = new ArrayList<String>();
		beginTime = System.currentTimeMillis();
	}

	/**
	 * 开始录制直播
	 * 
	 * @param liver
	 * @param shortId
	 * @param qn
	 */
	public void start(String liver, String shortId, String qn) {
		try {
			System.out.println(liver + " 直播录制 version " + version);
			// 加载cookies
			String cookie = null;
			try {
				File cookieFile = new File(GlobalConfig.configDir, liver + "-cookie.txt");
				BufferedReader buReader = new BufferedReader(new FileReader(cookieFile));
				cookie = buReader.readLine();
				buReader.close();
				Logger.println(cookie);
			} catch (Exception e) {
				// e.printStackTrace();
			}
			final String fcookie = cookie;

			roomDealer = getRoomDealer(liver);
			// 获取房间信息
			RoomInfo roomInfo = roomDealer.getRoomInfo(shortId);
			roomInfo.setLiver(liver);
			// 查看是否在线
			if (roomInfo != null && roomInfo.getLiveStatus() != 1) {
				System.out.println("当前没有在直播");
				RecorderManager.liveRecorders.remove(liver + "-" + shortId);
				return;
			}
			// 获取下载地址
			String url = roomDealer.getLiveUrl((roomInfo.getRoomId()), "" + qn, roomInfo.getRemark(), cookie);
			Logger.println(url);

			// 开始下载
			System.out.println("开始录制，输入stop停止录制");
			record(roomDealer, roomInfo, url, fileList); // 此处阻塞

			// 判断当前状态 如果异常连接导致失败，那么重命名后重新录制
			while (roomDealer.util.getStatus() == StatusEnum.FAIL && maxFailCnt >= fileList.size()) {
				System.out.println("连接异常，重新尝试录制");
				url = roomDealer.getLiveUrl((roomInfo.getRoomId()), "" + qn, roomInfo.getRemark(), fcookie);
				Logger.println(url);
				record(roomDealer, roomInfo, url, fileList); // 此处阻塞
			}

			System.out.println("下载停止");
			if (".flv".equals(roomDealer.getType())) {
				if (autoCheck) {
					try {
						for (String path : fileList) {
							System.out.println("校对时间戳开始...");
							synchronized (FlvChecker.buffer) {
								new FlvChecker().check(path, deleteOnchecked);
							}
							System.out.println("校对时间戳完毕。");
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		RecorderManager.liveRecorders.remove(liver + "-" + shortId);
	}

	/**
	 * 停止录制
	 */
	public void stop() {
		roomDealer.stopRecord();
	}

	/**
	 * 获取当前直播状态
	 * 
	 * @return
	 */
	public String status() {
		StringBuffer sb = new StringBuffer();
		if (roomDealer.util.getStatus() == StatusEnum.DOWNLOADING) {
			int period = (int) ((System.currentTimeMillis() - beginTime) / 1000);
			int hour = period / 3600;
			int minute = period / 60 - hour * 60;
			int second = period - minute * 60 - hour * 3600;
			if (hour == 0) {
				sb.append(String.format("已经录制了%dm%ds, ", minute, second));
			} else {
				sb.append(String.format("已经录制了%dh%dm%ds, ", hour, minute, second));
			}
			sb.append("当前进度： " + RoomDealer.transToSizeStr(roomDealer.util.getDownloadedFileSize()));
		} else if (roomDealer.util.getStatus() == StatusEnum.NONE) {
			sb.append("尚未开始 ");
		} else if (roomDealer.util.getStatus() == StatusEnum.FAIL) {
			sb.append("下载失败 ");
		} else {
			sb.append("正在处理时间戳，请稍等 ");
		}
		return sb.toString();
	}

	private void record(RoomDealer roomDealer, RoomInfo roomInfo, String url, List<String> fileList) {
		title = roomInfo.getUserName();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH.mm");
		String filename = String.format("%s-%s 的%s直播 %s-%d",
				roomInfo.getUserName().replaceAll("[\\\\|\\/|:\\*\\?|<|>|\\||\\\"$]", "."), roomInfo.getShortId(),
				roomInfo.getLiver(), sdf.format(new Date()), fileList.size());
		roomDealer.startRecord(url, filename, roomInfo.getShortId());// 此处一直堵塞， 直至停止
		File file = roomDealer.util.getFileDownload();
		File partFile = new File(file.getParent(), filename + roomDealer.getType() + ".part");
		File dstFile = new File(file.getParent(), filename + roomDealer.getType());
		// 将可能的.flv.part文件重命名为.flv
		partFile.renameTo(dstFile);
		// 加入已下载列表
		fileList.add(dstFile.getAbsolutePath());
	}

	/**
	 * 获取正确的视频录制器
	 * 
	 * @param liver
	 * @return
	 */
	private RoomDealer getRoomDealer(String liver) {
		Class<?> clazz = PackageScanLoader.dealerClazz.get(liver);
		try {
			return (RoomDealer) clazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("当前没有发现合适的视频录制器： " + liver);
			RoomDealer dealer = new RoomDealerBilibili();
			return dealer;
		}
	}

}
