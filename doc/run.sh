#!/bin/bash
#会搜索当前sh文件同级目录内的jar文件
#stop：停止该jar的运行
#start：停止并运行jar
#logstart：停止并运行jar，同时打印日志文件
prog=$(basename $0)
JAR_HOME=$(cd `dirname $0`; pwd)
port=10001

if [ $(ls $JAR_HOME | grep .jar |wc -l) -gt 1 ];
then
	echo "find more then one .jar file in dir $JAR_HOME"
	exit 1
fi

JAR_NAME=$(ls $JAR_HOME | grep .jar | awk '{print $1}')
if [ -z "$JAR_NAME" ]
then
    echo "can not found .jar file in dir $JAR_HOME"
    exit 1
fi

#echo $"prog=$prog"
#echo $"JAR_HOME= $JAR_HOME"
#echo $"JAR_NAME=$JAR_NAME"


#stop function
stop() {
        #PID=$(ps -ef | grep $JAR_HOME/$JAR_NAME | grep -v grep | awk '{ print $2 }')
        PID=$(ss -n -t -l -p | grep $port | column -t | awk -F ',' '{print $(NF-1)}')
        [[ $PID =~ "pid" ]] && PID=$(echo $PID | awk -F '=' '{print $NF}')
	if [ -z "$PID" ]
	then
	    echo Application  $JAR_HOME/$JAR_NAME is already stopped
	else
	    echo kill -9 $PID
	    kill -9 $PID
	fi
}
#start function
start() {
        PID=$(ss -n -t -l -p | grep $port | column -t | awk -F ',' '{print $(NF-1)}')
        [[ $PID =~ "pid" ]] && PID=$(echo $PID | awk -F '=' '{print $NF}')
        if [ -n "$PID" ]
	then
		echo Application  $JAR_HOME/$JAR_NAME is already running...
	else
		cd $JAR_HOME

		echo Application $JAR_HOME/$JAR_NAME starting...
		#nohup java -Dfile.encoding=UTF-8 -jar $JAR_NAME >/dev/null 2>&1 &
		BUILD_ID=dontKillMe nohup java -javaagent:/home/pinpoint-agent-2.1.0/pinpoint-bootstrap-2.1.0.jar -Dpinpoint.agentId=old -Dpinpoint.applicationName=oldService -jar -Xms512m -Xmx512m -Dappname=old-service -Dspring.config.location=config/app.properties,config/application.properties,config/security-mapping.properties /txb3.0/old-service/OLD-SERVICE.jar>out.log 2>&1&
	fi
}
#status function
status() {
	ps -ef | grep $JAR_NAME | grep -v grep
}


case "$1" in
	stop)
		stop
		tail -f out.log
	;;
	logstart)
		start
		tail -f out.log
	;;
	logrestart)
		stop
		sleep 2
		start
                tail -f out.log
		#tail -f out.log|sed -e "/-Stopping service/q"|sed -e "/NioSelectorPool/q"
	;;
	start)
		start
	;;
	restart)
		stop
		sleep 2
		start
	;;
	status)
		status
	;;
	*)
		echo $"unknow func $1,Usage: $prog {start|stop|logstart|restart|logrestart|status}"
	;;

esac
