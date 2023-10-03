#!/bin/bash

case $1 in
"start"){
	for i in redis100 redis101 redis102
	do
		echo  ------------- zookeeper $i 启动 ------------
		ssh $i "/opt/module/zookeeper-3.5.7/bin/zkServer.sh start"
	done
}
;;
"stop"){
	for i in redis100 redis101 redis102
	do
		echo  ------------- zookeeper $i 停止 ------------
		ssh $i "/opt/module/zookeeper-3.5.7/bin/zkServer.sh stop"
	done
}
;;
"status"){
	for i in redis100 redis101 redis102
	do
		echo  ------------- zookeeper $i 状态 ------------
		ssh $i "/opt/module/zookeeper-3.5.7/bin/zkServer.sh status"
	done
}
;;
esac
