package nicelee.server.controller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import nicelee.common.annotation.Controller;
import nicelee.function.daily.douyu.DouyuDailyTask;
import nicelee.function.daily.douyu.DouyuGift;
import nicelee.function.daily.douyu.DouyuLogin;
import nicelee.function.daily.douyu.domain.Gift;
import nicelee.global.GlobalConfig;

@Controller(path = "/daily", note = "斗鱼日常任务")
public class ControllerDouyuGiftSender {

	@Controller(path = "/douyu/sendGifts", note = "按照A计划每天养牌子")
	public String sendByPlanA() {
		// 获取所有勋章，然后每个勋章送一个礼物，然后剩下的全给佩戴的(也就是第一个)
		String cookie = DouyuLogin.getCookie();
		DouyuGift dg = new DouyuGift();
		List<String> idols = dg.idols(cookie);
		if (idols == null || idols.isEmpty()) {
			// 发送QQ消息通知
			new ControllerQQRobot().sendMsg("斗鱼cookie似乎失效啦~~~", GlobalConfig.QQToInform);
			return "cookie 失效";
		} else {
			List<Gift> giftAll = dg.info(cookie);
			int currentGiftType = 0;
			for (String roomInfo : idols) {
				String[] ids = roomInfo.split("#");
				if (currentGiftType == giftAll.size()) {
					// 发送QQ消息通知
					new ControllerQQRobot().sendMsg("斗鱼礼物似乎不足呢~", GlobalConfig.QQToInform);
					return "斗鱼礼物似乎不足呢~";
				}
				// 如果当前礼物还有剩下的，那就继续送
				if (giftAll.get(currentGiftType).count > 0) {
					dg.send(cookie, ids[0], ids[1], giftAll.get(currentGiftType).id, 1);
					giftAll.get(currentGiftType).count--;
				} else {
					// 如果当前礼物没了，换下一种礼物送
					currentGiftType++;
					if (currentGiftType == giftAll.size()) {
						// 发送QQ消息通知
						new ControllerQQRobot().sendMsg("斗鱼礼物送到一半就不够了呢~", GlobalConfig.QQToInform);
						return "斗鱼礼物送到一半就不够了呢~";
					}
					dg.send(cookie, ids[0], ids[1], giftAll.get(currentGiftType).id, 1);
					giftAll.get(currentGiftType).count--;
				}
			}
			// 剩下的给当前佩戴徽章的
			String[] ids = idols.get(0).split("#");
			dg.sendAll(cookie, ids[0], ids[1]);
			return "已全部发送";
		}
	}

	@Controller(path = "/douyu/getGifts", note = "获取每天可以领取的礼物")
	public String getGifts(BufferedWriter out) {
		DouyuDailyTask daily = new DouyuDailyTask();
		try {
			out.write("以下为具体情况：<br/>\n");
			// 有些任务，必须做完领取完前置任务奖励，才能显示
			int maxCnt = 3;
			String result = daily.gainActiveTask(DouyuLogin.getCookie());
			while(maxCnt > 0 && !result.isEmpty()) {
				out.write(result);
				result = daily.gainActiveTask(DouyuLogin.getCookie());
				maxCnt--;
			}
			
			maxCnt = 3;
			result = daily.gainSharkTask(DouyuLogin.getCookie());
			while(maxCnt > 0 && !result.isEmpty()) {
				out.write(result);
				result = daily.gainSharkTask(DouyuLogin.getCookie());
				maxCnt--;
			}
		} catch (IOException e) {
		}
		return null;
	}
	
	@Controller(path = "/douyu/signAtBars", note = "鱼吧签到")
	public String signAtBars(BufferedWriter out) {
		DouyuDailyTask daily = new DouyuDailyTask();
		return daily.signAtBarsFollowed(DouyuLogin.getCookie(), 20, 1);
	}

}
