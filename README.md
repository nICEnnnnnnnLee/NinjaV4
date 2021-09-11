
## 前言  
+ 功能是搭建本地api web服务器。  
+ 初衷是PC端、Android Temux两用，但已经很久没有维护这些功能了，年久失修。  
    现在精简为骨架，方便DIY拓展功能。  
+ 目前主要的想法是通过自己的Android应用，开一个Service热加载外部dex包。  



## 如何扩展  
参考`nicelee.server.controller.ControllerSystemInfo.java`，在该目录下添加实现其它类即可

## 如何配置  
`app.config`举例：
```
httpServerPort = 8880
```

## :smile:如何使用  
+ 作为独立应用，UTF-8编码方式运行jar/dex包即可，举例：  
```
// 会从当前目录下的`app.config`读取配置
java -Dfile.encoding=utf-8 -jar ninjaV4.jar  
dalvikvm -cp ninjaV4.dex nicelee.server.MainServer  
// 或者nohup ...
nohup dalvikvm -cp ninjaV4.dex nicelee.server.MainServer >/dev/null 2>&1 &
```

+ 作为外部dex包加载时:
    + 必须设置环境变量`ninja.dex.path`，即该dex包的本地路径。  
    + 会从同级目录下的`app.config`读取配置
    + 入口类`nicelee.server.MainServer`
    + 在service内开启、关闭服务接口：  
        +   `MainServer.start()`
        +   `MainServer.stop()`


## :smile:LICENSE
```
Copyright (C) 2021 NiceLee. All Rights Reserved.

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
