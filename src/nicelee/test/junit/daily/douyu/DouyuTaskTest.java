package nicelee.test.junit.daily.douyu;

import org.junit.Test;

import nicelee.function.daily.douyu.DouyuDailyTask;
import nicelee.function.daily.douyu.DouyuLogin;
import nicelee.global.GlobalConfig;

public class DouyuTaskTest {

	// @Test
	public void testSpecificTask() {
		String cookie = DouyuLogin.getCookie();
		new DouyuDailyTask().getTaskGift(cookie, 212);
	}

	// @Test
	public void testOkTask() {
		String cookie = DouyuLogin.getCookie();
		new DouyuDailyTask().gainSharkTask(cookie);
	}

	// @Test
	public void testDailyTask() {
		String cookie = DouyuLogin.getCookie();
		new DouyuDailyTask().gainActiveTask(cookie);
	}

	// 测试房间签到
	// @Test
	public void testSignAtRoom() {
		String cookie = DouyuLogin.getCookie();
		System.out.println("房间签到:  ");
		DouyuDailyTask dyTask = new DouyuDailyTask();
//		dyTask.dotFisherSign(cookie, "739226", "/739226");
		dyTask.signAtRoom(cookie, "122402", true);
	}

	// 测试活动的房间签到
	// @Test
	public void signAtRoomActiveAndFollowed() {
		String cookie = DouyuLogin.getCookie();
		System.out.println("在关注过的正在开播的房间签到:  ");
		String result = new DouyuDailyTask().signAtRoomActiveAndFollowed(cookie, 7);
		System.out.println(result);
	}

	// 测试鱼吧签到
	//@Test
	public void testSignAtBar() {
		String cookie = DouyuLogin.getCookie();
		new DouyuDailyTask().signAtBar(cookie, "4760370");
	}

	// 测试根据已有cookie登录鱼吧
	// @Test
	public void testLoginAtBar() {
//			String cookie = DouyuLogin.getCookie();
		String cookie = new DouyuLogin().authFishBar();
		new DouyuDailyTask().signAtBar(cookie, "16775");
	}

	// 测试根据在所有鱼吧签到
	// @Test
	public void testSignAtBarsFollowed() {
		String cookie = DouyuLogin.getCookie();
		new DouyuDailyTask().signAtBarsFollowed(cookie, 6, 1);
	}

	// 测试客户端签到
	@Test
	public void testSignDailyPC() {
		String cookie = DouyuLogin.getCookie();
		new DouyuDailyTask().signDailyPC(cookie);
	}
	
	// 测试车队签到
	//@Test
	public void testSignMotorcade() {
		String cookie = DouyuLogin.getCookie();
		new DouyuDailyTask().signMotorcade(cookie, "@TGS#4YJCIUZFH");
	}
}
