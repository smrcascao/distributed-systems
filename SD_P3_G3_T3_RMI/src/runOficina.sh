#!/bin/bash
fuser -k 22323/tcp
cd src/
./build_and_deploy.sh
clear
cd dir_OficinaSide/
OficinaSide_com.sh
fuser -k 22323/tcp