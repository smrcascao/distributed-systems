#!/bin/bash
zip -r src.zip src 

echo "A Enviar os ficheiros para os computadores"
echo "Registry - Maquina 11"
sshpass -p qwerty scp src.zip sd0303@l040101-ws11.ua.pt:src.zip
echo "Geral - Maquina 01"
sshpass -p qwerty scp src.zip sd0303@l040101-ws01.ua.pt:src.zip
echo "Loja - Maquina 02"
sshpass -p qwerty scp src.zip sd0303@l040101-ws02.ua.pt:src.zip
echo "Oficina - Maquina 03"
sshpass -p qwerty scp src.zip sd0303@l040101-ws03.ua.pt:src.zip
echo "ArmazemPM - Maquina 04"
sshpass -p qwerty scp src.zip sd0303@l040101-ws04.ua.pt:src.zip
echo "Dona - Maquina 05"
sshpass -p qwerty scp src.zip sd0303@l040101-ws05.ua.pt:src.zip
echo "Artesaos - Maquina 07"
sshpass -p qwerty scp src.zip sd0303@l040101-ws07.ua.pt:src.zip
echo "Clientes - Maquina 09"
sshpass -p qwerty scp src.zip sd0303@l040101-ws09.ua.pt:src.zip
echo "envio dos ficheiros terminado"

echo "Registry"
sshpass -p qwerty ssh sd0303@l040101-ws11.ua.pt 'unzip -o src.zip'
#sshpass -p qwerty ssh sd0303@l040101-ws11.ua.pt 'fuser -k 22329/tcp'
xfce4-terminal -T "Registry" --geometry 80x20+0+0 --hold -e "sshpass -p qwerty ssh sd0303@l040101-ws11.ua.pt " &
sleep 1

echo "Remote Object"
#sshpass -p qwerty ssh sd0303@l040101-ws11.ua.pt 'unzip -o src.zip'
#sshpass -p qwerty ssh sd0303@l040101-ws11.ua.pt 'fuser -k 22320/tcp'
xfce4-terminal -T "Remote Object" --geometry 80x20+655+0 --hold -e "sshpass -p qwerty ssh sd0303@l040101-ws11.ua.pt" &
sleep 1

echo "Geral"
sshpass -p qwerty ssh sd0303@l040101-ws01.ua.pt 'unzip -o src.zip'
#sshpass -p qwerty ssh sd0303@l040101-ws11.ua.pt 'fuser -k 22321/tcp'
xfce4-terminal -T "Geral" --geometry 80x20+1300+0 --hold -e "sshpass -p qwerty ssh sd0303@l040101-ws01.ua.pt" &
sleep 1

echo "Loja"
sshpass -p qwerty ssh sd0303@l040101-ws02.ua.pt 'unzip -o src.zip'
#sshpass -p qwerty ssh sd0303@l040101-ws11.ua.pt 'fuser -k 22322/tcp'
xfce4-terminal -T "Loja" --geometry 80x20+0+340  --hold -e "sshpass -p qwerty ssh sd0303@l040101-ws02.ua.pt" &
sleep 1

echo "Oficina"
sshpass -p qwerty ssh sd0303@l040101-ws03.ua.pt 'unzip -o src.zip'
#sshpass -p qwerty ssh sd0303@l040101-ws11.ua.pt 'fuser -k 22323/tcp'
xfce4-terminal -T "Oficina" --geometry 80x20+655+340 --hold -e "sshpass -p qwerty ssh sd0303@l040101-ws03.ua.pt" &
sleep 1

echo "ArmazemPM"
sshpass -p qwerty ssh sd0303@l040101-ws04.ua.pt 'unzip -o src.zip'
#sshpass -p qwerty ssh sd0303@l040101-ws11.ua.pt 'fuser -k 22324/tcp'
xfce4-terminal -T "ArmazemPM" --geometry 80x20+1300+340 --hold -e "sshpass -p qwerty ssh sd0303@l040101-ws04.ua.pt" &
sleep 1

echo "Artesaos"
sshpass -p qwerty ssh sd0303@l040101-ws07.ua.pt 'unzip -o src.zip'
xfce4-terminal -T "Artesaos" --geometry 80x20+0+700 --hold -e "sshpass -p qwerty ssh sd0303@l040101-ws07.ua.pt" &
sleep 1

echo "Clientes"
sshpass -p qwerty ssh sd0303@l040101-ws09.ua.pt 'unzip -o src.zip'
xfce4-terminal -T "Clientes" --geometry 80x20+655+700 --hold -e "sshpass -p qwerty ssh sd0303@l040101-ws09.ua.pt" &
sleep 1

echo "Dona"
sshpass -p qwerty ssh sd0303@l040101-ws05.ua.pt 'unzip -o src.zip'
xfce4-terminal -T "Dona" --geometry 80x20+1300+700 --hold -e "sshpass -p qwerty ssh sd0303@l040101-ws05.ua.pt" 
sleep 1

sshpass -p qwerty scp sd0303@l040101-ws01.ua.pt:/home/sd0303/src/dir_GeralSide/log log