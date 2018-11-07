#!/bin/bash
fuser -k 22321/tcp
cd src/
./build_and_deploy.sh
clear
cd dir_GeralSide/
GeralSide_com.sh
fuser -k 22321/tcp