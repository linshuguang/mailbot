export MAILBOT_ROOT_PATH=/home/kenya/bot
MAILBOT_LIB_PATH=$MAILBOT_ROOT_PATH/lib
MAILBOT_CONF_PATH=$MAILBOT_ROOT_PATH/conf

export MAILBOT_HTJS_PATH=$MAILBOT_ROOT_PATH/bin/highcharts/highcharts.js
export MAILBOT_HTCOVJS_PATH=$MAILBOT_ROOT_PATH/bin/highcharts/highcharts-convert.js
export MAILBOT_TEMPLATE_PATH=$MAILBOT_ROOT_PATH/template

export MAILBOT_WORKING_PATH=`pwd`

cmd=java


[ $# -le 0 ] && exit -1

input=$1

[ ! -f "$input" ] && exit -2


log4j_config=file:$MAILBOT_CONF_PATH/log4j.xml
BOT_JAR=$MAILBOT_JAVA_LIB_PATH/mailbot-1.0-SNAPSHOT.jar


exit
#$cmd -Dprop=$MAILBOT_CONF_PATH/ctx.properties -Dlog4j.debug -Dlog4j.configuration=$log4j_config -Dfile.encoding=utf-8 -jar $BOT_JAR -o $output 2>&1 1>/dev/null
$cmd -Dlog4j.debug -Dlog4j.configuration=$log4j_config -Dfile.encoding=utf-8 -jar $BOT_JAR -m console -o a.json

