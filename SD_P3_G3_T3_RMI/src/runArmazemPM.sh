#!/bin/bash
fuser -k 22324/tcp
cd src/
./build_and_deploy.sh
clear
cd dir_ArmazemPMSide/
ArmazemPMSide_com.sh
fuser -k 22324/tcp