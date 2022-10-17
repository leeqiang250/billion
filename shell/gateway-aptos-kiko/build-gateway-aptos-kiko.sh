#! /bin/sh

rm -rf /home/ubuntu/source-code
mkdir -p /home/ubuntu/source-code

cd /home/ubuntu/source-code
git clone https://github.com/leeqiang250/aptos-sdk-java.git
cd aptos-sdk-java
mvn clean -DskipTests=true install

app_name=gateway-aptos-kiko

cd /home/ubuntu/source-code
git clone https://github.com/leeqiang250/billion.git
cd billion
mvn clean -DskipTests=true package

mkdir -p /home/ubuntu/${app_name}/logs

rm -rf /home/ubuntu/${app_name}/${app_name}.jar
cp /home/ubuntu/source-code/billion/${app_name}/target/${app_name}-0.0.1-SNAPSHOT.jar /home/ubuntu/${app_name}/${app_name}.jar
cp /home/ubuntu/source-code/billion/shell/${app_name}/restart.sh /home/ubuntu/${app_name}/restart.sh

sh /home/ubuntu/${app_name}/restart.sh