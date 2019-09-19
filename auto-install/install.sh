
pkg update
pkg install vi curl wget grep
# 下载js - 用于斗鱼直播录制解析
mkdir -p ~/workspace/ninjaV4/config
cd ~/workspace/ninjaV4/config
wget -O crypto-js.min.js https://gitee.com/NiceLeee/NinjaV4/raw/master/src/resources/crypto-js.min.js
wget -O app.config.sample https://gitee.com/NiceLeee/NinjaV4/raw/master/auto-install/app.config.sample

# 定时脚本
mkdir -p ~/.termux/tasker
cd ~/.termux/tasker
echo "curl http://127.0.0.1:8888/onliner/status/upload" > wifi_status_upload.sh
echo "exit"  >> wifi_status_upload.sh
echo "curl http://127.0.0.1:8888/cloud/run" > get_command_and_run.sh
echo "exit"  >> get_command_and_run.sh

# 程序开始/结束脚本
cd ~
echo "cd ~/workspace/ninjaV4" > run_ninjaV4.sh
echo "nohup dalvikvm -cp ninjaV4.dex nicelee.server.MainServer >/dev/null 2>&1 &"  >> run_ninjaV4.sh
echo "kill -9 \`ps -ef|grep java|grep -v grep|awk '{print \$1}'\`" > stop_ninjaV4.sh

# 下载主程序 
# 因为国内Github经常抽风，放在最后，不行的话去码云下载
cd ~/workspace/ninjaV4
wget -4 -O ninjaV4.dex  --user-agent="NinjaV4 App" https://github.com/nICEnnnnnnnLee/NinjaV4/releases/download/1.0.0/NinjaV4-1.0.0.dex --no-check-certificate
