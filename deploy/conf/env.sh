MAILBOT_ROOT_PATH=e:/project/mailbot/deploy
MAILBOT_ROOT_PATH=$(dirname $( dirname "${BASH_SOURCE[0]}"))
MAILBOT_LIB_PATH=$MAILBOT_ROOT_PATH/lib
MAILBOT_CONF_PATH=$MAILBOT_ROOT_PATH/conf



unameOut="$(uname -s)"
case "${unameOut}" in		
	MINGW*)    
		export MAILBOT_PHANTOMJS_PATH=$MAILBOT_ROOT_PATH/bin/phantomjs/bin/phantomjs.exe
		log_path=$MAILBOT_ROOT_PATH/log
		;;
	*)
		export MAILBOT_PHANTOMJS_PATH=$MAILBOT_ROOT_PATH/bin/phantomjs/bin/phantomjs
		log_path=/var/log			
		;;
esac


export MAILBOT_HTJS_PATH=$MAILBOT_ROOT_PATH/bin/highcharts/highcharts.js
export MAILBOT_HTCOVJS_PATH=$MAILBOT_ROOT_PATH/bin/highcharts/highcharts-convert.js
export MAILBOT_TEMPLATE_PATH=$MAILBOT_ROOT_PATH/template


log4j_config=file:$MAILBOT_CONF_PATH/log4j.xml
BOT_JAR=$MAILBOT_LIB_PATH/mailbot-1.0-SNAPSHOT.jar

log_level=info
export MAILBOT_WORKING_PATH=`pwd`


cmd="java -Dlog4j.debug -Dlog4j.configuration=$log4j_config -Dlog_path=$log_path -Dfile.encoding=utf-8 -jar $BOT_JAR -m console --deliver --silent "
#mvn clean jetty:run
#$cmd -Dprop=$MAILBOT_CONF_PATH/ctx.properties -Dlog4j.debug -Dlog4j.configuration=$log4j_config -Dfile.encoding=utf-8 -jar $BOT_JAR -o $output 2>&1 1>/dev/null
#$cmd -Dlog4j.debug -Dlog4j.configuration=$log4j_config -Dfile.encoding=utf-8 -jar $BOT_JAR -m console -o a.json 
#java -jar something.jar

run_client(){

	java -Dlog4j.debug -Dlog4j.configuration=$log4j_config -Dlog_path=$log_path -Dfile.encoding=utf-8 -jar $BOT_JAR -m client -o a.json 
}

run_console(){
	java -Dlog4j.debug -Dlog_level=$log_level -Dlog4j.configuration=$log4j_config -Dlog_path=$log_path -Dfile.encoding=utf-8 -jar $BOT_JAR -m console -x "$1" -o a.json 
}

run_server(){

	java -Dlog4j.info -Dlog_level=$log_level -Dlog4j.configuration=$log4j_config -Dlog_path=$log_path -Dfile.encoding=utf-8 -jar $BOT_JAR -m server -o a.json
}

mailbot(){
	[ $# -le 0 ] && return
	run_console $@
}



















