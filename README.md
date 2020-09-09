
## :smile:前言  
收录了一些日常用的功能，Android旧手机服务器+Linux小鸡两用。  
yoooooooooooo~  

## :smile:能做什么  
| 功能  | 支持情况 |
| ------------- | ------------- |
| 查看WiFi邻居  | jar/dex |
| 录制B站/斗鱼/虎牙/快手直播  | jar/dex |
| 通过Github中转推拉信息  | jar/dex |
| QQ信息收发(配合酷Q)  | 基于酷Q |
| 邮件发送  | jar/dex |
| 斗鱼养粉丝牌  | jar/dex |
| 斗鱼鱼塘任务奖品领取  | jar/dex |
| 斗鱼鱼吧每日签到  | jar/dex |
| 斗鱼客户端每日签到  | jar/dex |
| 斗鱼车队每日签到  | jar/dex |
| 斗鱼登录(扫二维码获取cookie)  | jar/dex |
| 微信计步作弊  | jar/dex |
| 通过id查询磁力  | jar/dex |
    
## :smile:如何使用  
+ UTF-8编码方式运行jar/dex包即可，举例：  
```
java -Dfile.encoding=utf-8 -jar ninjaV4.jar  
dalvikvm -cp ninjaV4.dex nicelee.server.MainServer  
// 或者nohup ...
nohup dalvikvm -cp ninjaV4.dex nicelee.server.MainServer >/dev/null 2>&1 &
```
+ 请务必注意**配置文件**的相应配置  
[此处](docs/0_configs.md)列有相应配置  

+ 关于dex包  
    + Android的虚拟Linux环境内dalvikvm与常见jvm存在差异(除非你再套一层，使用jvm)  
    + Android熄屏后定时操作存在问题  
    + [此处-Android手机当服务器](docs/1_android_dex.md)有所补充
    
## :smile:功能举例  
<details>
<summary>同一局域网内</summary>


假设手机IP为:**192.168.0.101**  
浏览器访问`http://192.168.0.101:8888/`,可以得到如下结果：  
![](https://nICEnnnnnnnLee.github.io/sources/archive/2019/09/index-page.png)   


根据提示操作即可。  
例如直播录制测试状态查询：    
![](https://nICEnnnnnnnLee.github.io/sources/archive/2019/09/live-recorder.png)  
</details>

<details>
<summary>远程操作</summary>


+ 查看家内的WiFi连接状态  
也就是终端收集Wifi连接信息，然后上传，在Github对应的Repo路径查看即可  
![](https://nICEnnnnnnnLee.github.io/sources/archive/2019/09/onliner.png)  

+ 让家内的Android设备执行某些特定操作  
    + 因为是每5分钟取一次，有一定延迟。  
    + 编辑配置指定好的位置的文件，如`task/todo.txt`

+ 任务格式说明  
    + 任务编号： 需要严格递增，编号超过99999以后从0开始
    + 计划时间： 假设`18:15`,`18:20`各取一次任务，企划在`18:18`,`18:19`的任务都将在`18:20`左右执行，且执行顺序没法确定
    + 命令内容： 与局域网内各命令的网址URL一致，例如：  
        + 录制b站某主播： /live/start?liver=bili&id=6&qn=10000  
        + 录制斗鱼某主播： /live/start?liver=douyu&id=233223&qn=0  
        + 停止所有录制： /live/stopAll
        + 更新WiFi设备连接状态： /onliner/status/upload
```
任务编号 计划时间 命令内容
1 2019-09-16 14:14 /test
2 2019-09-16 14:14 /cloud/history/delete
3 2019-09-17 18:18 /onliner/status/upload
4 2019-09-17 18:19 /onliner/status/upload
5 2019-09-17 18:45 /onliner/status/upload
6 2019-09-17 20:46 /onliner/status/upload
```
</details> 

## :smile:如何扩展DIY  
+ 在`nicelee.server.controller`下建立方法解决类，并加上注解`@Controller`  
+ 在处理访问请求时，将会自动调用与URL一致的方法，并自动传入URL里面的参数  
+ 更详细请见 [处理都在Controller的弱鸡服务器](https://nICEnnnnnnnLee.github.io/blog/2019/09/09/android-linux-6/?from=NinjaV4.github)  

<details>
<summary>举例</summary>


```java
package nicelee.server.controller;

import java.io.BufferedWriter;
import java.io.IOException;

import nicelee.common.annotation.Controller;
import nicelee.common.annotation.Value;

@Controller(path = "/test", note = "测试类")
public class ControllerTester {

	@Controller(path = "/helloWorld", note = "Hello World处理方法1")
	public String test1(BufferedWriter out, @Value(key = "param1") String param1) throws IOException {		
		out.write("调用的方法为：helloWorld<br/>\r\n");
		out.write("传入的参数param1 :" + param1);
		return null;
	}
    
	@Controller(path = "/helloWorld2", note = "Hello World处理方法2")
	public String test2(@Value(key = "param1") String param1) throws IOException {
		StringBuffer sb = new StringBuffer();
		sb.append("调用的方法为：helloWorld<br/>\r\n");
		sb.append("传入的参数param1 :");
		sb.append(param1);
		return  sb.toString();
	}
}
```
</details>


## :smile:实现功能的参考引用  
+ [获取邻居MAC地址](https://github.com/nICEnnnnnnnLee/NeighborFinder)  
+ [直播录制](https://github.com/nICEnnnnnnnLee/BilibiliLiveRecorder)  
+ [简单的文件服务器](https://github.com/nICEnnnnnnnLee/FileHttpServer)  
+ [通过HTTP请求上传/更新Github文件](https://github.com/nICEnnnnnnnLee/FileUploader4Github)  
+ [Dex包解析扫描](https://gitee.com/mazaiting/app_protection/tree/master/ParseDEX)  
+ [微信计步作弊原理](https://nICEnnnnnnnLee.github.io/blog/2019/12/07/weixin-step-counter-cheater-2/?from=NinjaV4.github)
+ [斗鱼客户端+车队签到](https://github.com/qianjiachun/douyuEx)

## :smile:第三方使用声明   
+ 使用[JSON.org](https://github.com/stleary/JSON-java)库做简单的Json解析[![](https://img.shields.io/badge/license-MIT-green.svg)](https://github.com/stleary/JSON-java/blob/master/LICENSE) 
+ 使用[Crypto-js](https://github.com/brix/crypto-js)仿浏览器生成斗鱼直播录制token[![](https://img.shields.io/badge/license-MIT-green.svg)](https://github.com/brix/crypto-js/blob/develop/LICENSE) 
+ 使用[Rhino](https://github.com/brix/crypto-js)库在斗鱼直播录制生成token时调用了js[![](https://img.shields.io/badge/license-MPL%202.0-green.svg)](https://github.com/mozilla/rhino/blob/master/LICENSE.txt) 
+ 使用[JavaMail](https://github.com/javaee/javamail)发送邮件[![](https://img.shields.io/badge/license-CDDL-green.svg)](https://javaee.github.io/javamail/LICENSE) 
* 使用[qrcodejs](https://github.com/davidshimjs/qrcodejs)库生成链接二维码图片[![](https://img.shields.io/badge/license-MIT-green.svg)](https://github.com/davidshimjs/qrcodejs/blob/master/LICENSE)  


## :smile:更新日志
[UPDATE.md](https://github.com/nICEnnnnnnnLee/NinjaV4/blob/master/UPDATE.md)

## :smile:LICENSE
```
Copyright (C) 2019 NiceLee. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
