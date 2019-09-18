package nicelee.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.regex.Matcher;

import nicelee.function.live_recorder.Recorder;
import nicelee.function.live_recorder.util.HttpRequestUtil;
import nicelee.function.live_recorder.util.RhinoEngine;
import nicelee.global.Global;
import nicelee.global.GlobalConfig;

public class Test {

	public static void main(String[] args) {

		String realPath = Test.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		System.out.println(realPath);
//		Recorder recorder = new Recorder();
//		recorder.start("douyu", "6296895", "0");
		System.out.println("---begin---");
		try {
			BufferedReader buReader = null;
			buReader = new BufferedReader(new InputStreamReader(
					Test.class.getResource("resources/crypto-js.min.js").openStream()));
			String line = null;
			while ((line = buReader.readLine()) != null) {
				System.out.println(line);
			}
			buReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("---end---");
	}
}
