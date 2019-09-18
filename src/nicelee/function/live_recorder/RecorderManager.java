package nicelee.function.live_recorder;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class RecorderManager {

	final static HashMap<String, Recorder> liveRecorders = new HashMap<String, Recorder>();

	/**
	 * 查询当前所有录制状态
	 */
	public static void currentStatus(BufferedWriter out) {
		for (Entry<String, Recorder> entry : liveRecorders.entrySet()) {
			String liver_id = entry.getKey();
			Recorder rec = entry.getValue();
			try {
				out.write(liver_id + " : ");
				if(rec.title != null) {
					out.write(rec.title);
				}
				out.write(rec.status());
				out.write("<br/>\r\n");
			} catch (IOException e) {
			}
		}
	}

	/**
	 * 查询特定录制状态
	 */
	public static void currentStatus(BufferedWriter out, String liver, String id) {
		String liver_id = liver + "-" + id;
		Recorder rec = liveRecorders.get(liver_id);
		try {
			if (rec == null) {
				out.write(liver_id + "没有在录制");
				out.write("<br/>\r\n");
			} else {
				out.write(liver_id + " : " + rec.title);
				out.write(rec.status());
				out.write("<br/>\r\n");
			}
		} catch (IOException e) {
		}
	}

	/**
	 * 开始录制
	 */
	public static void startRecord(BufferedWriter out, String liver, String id, String qn) {
		String liver_id = liver + "-" + id;
		Recorder rec = liveRecorders.get(liver_id);
		try {
			if (rec != null) {
				out.write(liver_id + " 已经在录制");
				out.write("<br/>\r\n");
			} else {
				final Recorder recorder = new Recorder();
				liveRecorders.put(liver_id, recorder);
				new Thread(new Runnable() {
					@Override
					public void run() {
						recorder.start(liver, id, qn);
					}
				}).start();
				
				out.write(liver_id + " 开始录制");
				out.write("<br/>\r\n");
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 停止录制
	 */
	public static void stopRecord(BufferedWriter out, String liver, String id) {
		String liver_id = liver + "-" + id;
		Recorder rec = liveRecorders.get(liver_id);
		try {
			if (rec != null) {
				rec.stop();
				out.write(liver_id + " 正在停止录制");
				out.write("<br/>\r\n");
			} else {
				out.write(liver_id + " 未在录制当中");
				out.write("<br/>\r\n");
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 停止所有录制
	 */
	public static void stopRecord(BufferedWriter out) {
		Set<Entry<String, Recorder>> set = liveRecorders.entrySet();
		for (Entry<String, Recorder> entry : set) {
			String liver_id = entry.getKey();
			Recorder rec = entry.getValue();
			rec.stop();
		}
		try {
			out.write("正在停止所有录制");
			out.write("<br/>\r\n");
		} catch (IOException e) {
		}
	}
}
