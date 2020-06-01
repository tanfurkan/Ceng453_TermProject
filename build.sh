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
mv -f client/target/client_program7.jar executables/client_program7.jar
mv -f server/target/server_program7.war executables/server_program7.war
