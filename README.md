
## :smile:能做什么  
+ 远程查看家里WiFi的设备连接情况  
+ 远程控制手机录制B站/斗鱼/虎牙/快手直播  
    
    
## :smile:需要什么  
+ 一台Android手机(可以不必Root)   
    + 安装Termux
    + 安装Termux：Tasker
    + 安装Tasker
    + 不咋会的可以看看前面的[博文](https://nICEnnnnnnnLee.github.io/blog/2019/09/07/android-linux-3/)  
+ 一个Github账号  
    + 创建Repo，用于状态获取与任务发布  
+ 一个编译好的dex程序 [ninjaV4.dex](https://github.com/nICEnnnnnnnLee/NinjaV4/releases)


## :smile:如何部署  
<details>
<summary>Github</summary>


+ 新建一个用于鉴权的token  [配置Token](https://github.com/settings/tokens)  
+ 新建一个Repo(可以是私有),例如`AbcTest`
+ 为了方便识别在线设备，添加对MAC地址的备注，如`online-devices/remarks.txt`  
```
2b:2b:2b:2b:2b:2b 我的PC
2b:2b:2b:2b:2b:2c 我的手机
```
</details>


<details>
<summary>Termux</summary>


+ 安装  
更详细的请访问[博客](https://nICEnnnnnnnLee.github.io/blog/2019/09/17/android-linux-8/)
```
wget install.sh https://gitee.com/NiceLeee/NinjaV4/raw/master/auto-install/install.sh
bash install.sh
```

+ 配置
```
cd ~/workspace/ninjaV4/config
vi app.config
```
以下列出必不可少的关键配置，其它可参考`app.config.sample`
```
# dex包的路径(用于包扫描)
dexPath = ninjaV4.dex
# token 用于访问github
token = xxxxxxxx
# 在线设备上传的位置
#例如访问{nICEnnnnnnnLee}的{AbcTest}仓库的{online-devices/onlines.txt}可以看到我的WiFi设备状态
url_onlineDevices = https://api.github.com/repos/nICEnnnnnnnLee/AbcTest/contents/online-devices/onlines.txt
# 对MAC地址的备注，例如访问{nICEnnnnnnnLee}的{AbcTest}仓库的{online-devices/remarks.txt}
url_markOfMacs = https://raw.githubusercontent.com/nICEnnnnnnnLee/AbcTest/master/online-devices/remarks.txt
# 用于云端下发命令
url_taskToDo = https://raw.githubusercontent.com/nICEnnnnnnnLee/AbcTest/master/task/todo.txt
# 用于命令执行情况上报
url_taskReport = https://api.github.com/repos/nICEnnnnnnnLee/AbcTest/contents/task/report.txt
```

+ 运行  
```
bash ~/run_ninjaV4.sh
```
</details>
 
<details>
<summary>Tasker</summary>


+ 周期性触发WiFi状态上传
    + Tasker添加任务
    + 弹出对话框，选择插件
    + 弹出对话框，选择termux:task
    + 弹出对话框，选择脚本wifi_status_upload.sh
    + 建议每30分钟触发一次
    
+ 周期性触发云端命令获取并执行
    + Tasker添加任务
    + 弹出对话框，选择插件
    + 弹出对话框，选择termux:task
    + 弹出对话框，选择脚本get_command_and_run.sh
    + 建议每5分钟触发一次
</details>

    
## :smile:如何使用  
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
在Github对应的Repo路径查看即可  
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
+ 更详细请见 [处理都在Controller的弱鸡服务器](https://nICEnnnnnnnLee.github.io/blog/2019/09/09/android-linux-6/)  

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

## :smile:第三方使用声明   
+ 使用[JSON.org](https://github.com/stleary/JSON-java)库做简单的Json解析[![](https://img.shields.io/badge/license-MIT-green.svg)](https://github.com/stleary/JSON-java/blob/master/LICENSE) 
+ 使用[Crypto-js](https://github.com/brix/crypto-js)仿浏览器生成斗鱼直播录制token[![](https://img.shields.io/badge/license-MIT-green.svg)](https://github.com/brix/crypto-js/blob/develop/LICENSE) 
+ 使用[Rhino](https://github.com/brix/crypto-js)库在斗鱼直播录制生成token时调用了js[![](https://img.shields.io/badge/license-MPL%202.0-green.svg)](https://github.com/mozilla/rhino/blob/master/LICENSE.txt) 

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
