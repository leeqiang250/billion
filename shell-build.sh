#! /bin/sh

rm -rf /home/ubuntu/source-code
mkdir -p /home/ubuntu/source-code

cd /home/ubuntu/source-code
git clone https://github.com/leeqiang250/aptos-sdk-java.git
cd aptos-sdk-java
mvn clean -DskipTests=true install

cd /home/ubuntu/source-code
git clone https://github.com/leeqiang250/billion.git
cd billion
mvn clean -DskipTests=true package


app_name='gateway-aptos-kiko'

mkdir -p /home/ubuntu/$app_name/config
mkdir -p /home/ubuntu/$app_name/logs
mkdir -p /home/ubuntu/$app_name/shell
mkdir -p /home/ubuntu/$app_name/target

rm -rf /home/ubuntu/$app_name/target/$app_name.jar
cp /home/ubuntu/source-code/billion/gateway-aptos-kiko/target/gateway-aptos-kiko-0.0.1-SNAPSHOT.jar /home/ubuntu/$app_name/target/$app_name.jar