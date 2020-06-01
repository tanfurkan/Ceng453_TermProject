#!/bin/bash
cd client
./mvnw clean package
cd ../server
./mvnw clean package
cd ..
if [ ! -d "executables" ] 
then
	mkdir executables
fi
mv -f client/target/client7.jar executables/client7.jar
mv -f server/target/server7.war executables/server7.war
