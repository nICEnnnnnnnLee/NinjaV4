package nicelee.test.junit.daily.douyu;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import nicelee.function.daily.douyu.DouyuGift;
import nicelee.function.daily.douyu.DouyuLogin;

public class DouyuGiftTest {

	//@Test
	public void testInfo() {
		String cookie = DouyuLogin.getCookie();
		new DouyuGift().info(cookie);
	}

	@Test
	public void testSendGift() {
		String cookie = DouyuLogin.getCookie();
		new DouyuGift().send(cookie, "198859", "198859", 180, 1);
	}
	
	//@Test
	public void testIdols() {
		String cookie = DouyuLogin.getCookie();
		List<String> list = new DouyuGift().idols(cookie);
		assertNotNull(list);
		assertTrue(list.size() > 0);
	}

}
