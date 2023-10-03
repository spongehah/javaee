#! /bin/bash
case $1 in
"start"){
 for i in redis100 redis101 redis102
 do
 echo " --------���� $i Kafka-------"
 ssh $i "/opt/module/kafka/bin/kafka-server-start.sh -
daemon /opt/module/kafka/config/server.properties"
 done
};;
"stop"){
 for i in redis100 redis101 redis102
 do
 echo " --------ֹͣ $i Kafka-------"
 ssh $i "/opt/module/kafka/bin/kafka-server-stop.sh "
 done
};;
esac