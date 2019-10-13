
<https://nicennnnnnnlee.github.io/blog/2019/09/05/android-linux-1-adb-shell/>  

## :smile:需要什么  
+ 一台Android手机(可不必root)   
    + 安装Termux
    + 安装Termux：Tasker
    + 安装Tasker
    + 不咋会的可以看看前面的[博文](https://nICEnnnnnnnLee.github.io/blog/2019/09/07/android-linux-3/)  
+ 一个Github账号  
    + 创建Repo，用于状态获取与任务发布  
+ 一个编译好的dex程序 [ninjaV4.dex](https://github.com/nICEnnnnnnnLee/NinjaV4/releases)

## :smile:如何部署  
<details>
<summary>Github(仅局域网使用可跳过)</summary>


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
主要的问题是，熄屏以后，定时器不能很好地工作。  
因此，引入了Tasker，目的是定时运行curl一些路径。  
更详细的请访问[博客](https://nICEnnnnnnnLee.github.io/blog/2019/09/17/android-linux-8/)
```
wget -O install.sh https://gitee.com/NiceLeee/NinjaV4/raw/master/auto-install/install.sh
bash install.sh
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

