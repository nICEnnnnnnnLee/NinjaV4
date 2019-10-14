package nicelee.test.junit.daily.douyu;

import org.junit.Test;

import nicelee.function.daily.douyu.DouyuDailyTask;
import nicelee.function.daily.douyu.DouyuLogin;

public class DouyuTaskTest {

	
	//@Test
	public void testSpecificTask() {
		String cookie = DouyuLogin.getCookie();
		new DouyuDailyTask().getTaskGift(cookie, 212);
	}
	
	//@Test
	public void testOkTask() {
		String cookie = DouyuLogin.getCookie();
		new DouyuDailyTask().gainSharkTask(cookie);
	}
	
	//@Test
	public void testDailyTask() {
		String cookie = DouyuLogin.getCookie();
		new DouyuDailyTask().gainActiveTask(cookie);
	}
	
	//测试签到
	//@Test
	public void testSign() {
		String cookie = DouyuLogin.getCookie();
		new DouyuDailyTask().sign(cookie, "198859");
	}
	
	//测试鱼吧签到
	//@Test
	public void testSignAtBar() {
		String cookie = DouyuLogin.getCookie();
		new DouyuDailyTask().signAtBar(cookie, "443360");
	}
	
	//测试根据已有cookie登录鱼吧
	//@Test
	public void testLoginAtBar() {
//		String cookie = DouyuLogin.getCookie();
		String cookie = new DouyuLogin().authFishBar();
		new DouyuDailyTask().signAtBar(cookie, "16775");
	}
	
	//测试根据在所有鱼吧签到
	@Test
	public void testSignAtBarsFollowed() {
		String cookie = DouyuLogin.getCookie();
		new DouyuDailyTask().signAtBarsFollowed(cookie, 6, 1);
	}

}
