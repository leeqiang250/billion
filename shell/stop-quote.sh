#! /bin/sh

app_name=quote

kill `ps -ef | grep ${app_name} | grep -v grep | grep -v restart | awk 'BEGIN{ORS=" "}{print$2}'`

sleep 5