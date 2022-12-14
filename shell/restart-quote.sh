#! /bin/sh

app_name=quote
enviroment=test
app_mem=512m

base_home=/home/ubuntu/${app_name}
errorlogs_dir=${base_home}/logs
gclog_file=${base_home}/logs/${app_name}-gc.log
dump_dir=${base_home}/heapdump

cd ${base_home}

./stop.sh

java -Dspring.profiles.active=${enviroment} -jar ./${app_name}.jar &

#java -Xmx${app_mem} -Xms${app_mem} -XX:+UseParallelGC -XX:+UseParallelOldGC -XX:+PrintGCDetails -Xloggc:${gclog_file} -XX:HeapDumpPath=${dump_dir} -XX:+HeapDumpOnOutOfMemoryError  -XX:ErrorFile=${errorlogs_dir}/hs_err_%p.log  -Dfile.encoding=UTF-8 -Duser.timezone="Asia/Singapore" -jar ${base_home}/target/${app_name}.jar >${base_home}/logs/${app_name}-info.log 2>&1 spring.profiles.active=${enviroment} &