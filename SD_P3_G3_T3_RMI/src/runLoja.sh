#!/bin/bash
fuser -k 22322/tcp
cd src/
./build_and_deploy.sh
clear
cd dir_LojaSide/
LojaSide_com.sh
fuser -k 22322/tcp