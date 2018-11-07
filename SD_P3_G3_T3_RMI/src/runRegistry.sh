#!/bin/bash
fuser -k 22329/tcp
cd src/
./build_and_deploy.sh
clear
cd ..
./set_rmiregistry.sh 22329
fuser -k 22329/tcp
