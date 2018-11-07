#!/bin/bash
fuser -k 22320/tcp
cd src/dir_registry/
./registry_com.sh 
fuser -k 22320/tcp