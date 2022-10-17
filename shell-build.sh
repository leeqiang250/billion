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

gateway_aptos_kiko=gateway-aptos-kiko

mkdir -p /home/ubuntu/${gateway_aptos_kiko}/config
mkdir -p /home/ubuntu/${gateway_aptos_kiko}/logs
mkdir -p /home/ubuntu/${gateway_aptos_kiko}/shell
mkdir -p /home/ubuntu/${gateway_aptos_kiko}/target

rm -rf /home/ubuntu/${gateway_aptos_kiko}/target/${gateway_aptos_kiko}.jar
cp /home/ubuntu/source-code/billion/${gateway_aptos_kiko}/target/${gateway_aptos_kiko}-0.0.1-SNAPSHOT.jar /home/ubuntu/${gateway_aptos_kiko}/target/${gateway_aptos_kiko}.jar
cp /home/ubuntu/source-code/billion/shell-${gateway_aptos_kiko}.sh /home/ubuntu/${gateway_aptos_kiko}/shell/restart.sh