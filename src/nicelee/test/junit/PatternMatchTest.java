package nicelee.test.junit;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class PatternMatchTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	// @Test
	public void test() {
		String test = "﻿1 2019-09-16 14:14 /test";
		Pattern pattern = Pattern.compile("([0-9]+) ([0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2} +.*)$");
		Matcher matcher = pattern.matcher(test);
		if (matcher.find()) {
			System.out.println(matcher.group(1));
			System.out.println(matcher.group(2));
		} else {
			System.out.println("不匹配");
			;
		}
	}

	@Test
	public void testThread() {
		final ConcurrentHashMap<String, String> obj = new ConcurrentHashMap<String, String>();
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					System.out.println("Thread-1 ---");
					for(int i=0; i<100; i++) {
						obj.put("" + i, "" + i);
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					System.out.println("Thread-1 ---");
					for(int i=0; i<100; i++) {
						obj.remove("" + i, "" + i);
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		while (true) {
			System.out.println("Thread-Main ---");
			Entry<String, String>[] entries = new Entry[obj.size()];
			entries = obj.entrySet().toArray(entries) ;
			for (Entry<String, String> entry : obj.entrySet()) {
				System.out.println("Thread-Main ---" + entry.getKey());
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
