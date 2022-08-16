#! /bin/sh

mvn clean -DskipTests=true package

#mvn clean -DskipTests=true install

#sudo mvn clean -DskipTests=true package spring-boot:build-image

#docker run -d -p 8888:8888 --name billion-spring-cloud-server-0.0.1-SNAPSHOT billion-spring-cloud-server:0.0.1-SNAPSHOT

#https://start.spring.io