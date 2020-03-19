
请在相对位置`config/app.config`编辑好配置文件
==============================

## :smile:通用配置  
```
// 服务器监听端口
httpServerPort = 8888
// 下载文件夹
downloadDir = download/
// dex包的路径(用于包扫描，非dalvikvm可以不必配置) 
dexPath = ninjaV4.dex
```

## :smile:Github远程任务下发与信息上传
+ 用于没有小鸡，在github上建个repo当作任务信息中转。  
具体设置可以参照以下repo
```
// 用于鉴权
token = xxxxxx
// url-云端命令存放位置  下载
url_taskToDo = https://raw.githubusercontent.com/nICEnnnnnnnLee/AbcTest/master/task/todo.txt
// url-云端命令运行结果存放位置 上传
url_taskReport = https://api.github.com/repos/nICEnnnnnnnLee/AbcTest/contents/task/report.txt
```

## :smile:WiFi设备状态上传Github
+ 用于没有小鸡，在github上建个repo存储WiFi设备状态  
具体设置可以参照以下repo
```
// 用于鉴权
token = xxxxxx
// url-在线设备存放位置  上传
url_onlineDevices = https://api.github.com/repos/nICEnnnnnnnLee/AbcTest/contents/online-devices/onlines.txt
// url-Mac地址备注存放位置 下载
url_markOfMacs = https://raw.githubusercontent.com/nICEnnnnnnnLee/AbcTest/master/online-devices/remarks.txt
```


## :smile:QQ机器人相关
```
// 酷Q插件httpapi的地址，用于发消息
coolQ_httpApi_Addr = http://192.168.0.103:5701
// 默认通知的QQ信息
QQToInform = 1234567
```

## :smile:邮件相关
```
// 因为酷Q可能无法很好的在Android Termux环境里面运行，将用邮件替代提醒服务
mail_senderAddress = sender@sina.com
mail_recipientAddress = receiver@sina.com
mail_senderAccount = sender@sina.com
mail_senderPassword = xxx
```

## :smile:斗鱼登录相关
```
对于dex，由于没法从dex包里面读取出资源，需要以下操作：
  将qrcode.min.js拷贝至config/qrcode.min.js
```

## :smile:斗鱼签到相关
```
// 斗鱼账号所在车队id
// 打开https://msg.douyu.com/motorcade/#/motorcade/list/recommend, 可以搜索或者查看自己的车队id
// 一般可以从url中提取，此时需注意unescape操作
douyuMotorcadeId = @TGS#4UIILTYFJ
```

## :smile:视频录制相关
```
对于dex的斗鱼视频录制，由于没法从dex包里面读取出资源，需要以下操作：
  将crypto-js.min.js拷贝至config/crypto-js.min.js

// 可以是相对路径，也可以是绝对路径。请确保目录下具有读写权限
downloadDir = download/
// 录制完毕校正时间戳后是否删除源文件
deleteOnchecked = true 
```

## :smile:微信作弊相关  
参数获取请参见：[此处](http://nicennnnnnnlee.github.io/blog/2020/01/18/weixin-step-counter-cheater-3/)
```
weixin_step_refreshCookieUrl = http://steps.chunyuyisheng.com/api/pedometer/contest_start/?union_id=xxx 
weixin_step_postStepUrl = http://steps.chunyuyisheng.com/robot/p/upload_steps_data/?app=7&platform=android&systemVer=xxx
```

## :smile:磁力查询相关  
```
免翻域名， 比如xxxbus
magnetDomain = www.xxx.com
```