#!/bin/bash

if [ "$#" -ne 8 ]; then
	echo "numero de argumentos errado. template:"
	echo "./EnviarGenerico Registry Geral Loja Oficina ArmazemPM Dona Artesaos Clientes"
    exit 0
fi

zip -r src.zip src 

echo "A Enviar os ficheiros para os computadores"
echo "Registry - Maquina $1" 
sshpass -p qwerty scp src.zip sd0303@l040101-ws$1.ua.pt:src.zip
echo "Geral - Maquina $2"
sshpass -p qwerty scp src.zip sd0303@l040101-ws$2.ua.pt:src.zip
echo "Loja - Maquina $3"
sshpass -p qwerty scp src.zip sd0303@l040101-ws$3.ua.pt:src.zip
echo "Oficina - Maquina $4"
sshpass -p qwerty scp src.zip sd0303@l040101-ws$4.ua.pt:src.zip
echo "ArmazemPM - Maquina $5"
sshpass -p qwerty scp src.zip sd0303@l040101-ws$5.ua.pt:src.zip
echo "Dona - Maquina $6"
sshpass -p qwerty scp src.zip sd0303@l040101-ws$6.ua.pt:src.zip
echo "Artesaos - Maquina $7"
sshpass -p qwerty scp src.zip sd0303@l040101-ws$7.ua.pt:src.zip
echo "Clientes - Maquina $8"
sshpass -p qwerty scp src.zip sd0303@l040101-ws$8.ua.pt:src.zip
echo "envio dos ficheiros terminado"

echo "Registry"
sshpass -p qwerty ssh sd0303@l040101-ws$1.ua.pt 'unzip -o src.zip'
#sshpass -p qwerty ssh sd0303@l040101-ws11.ua.pt 'fuser -k 22329/tcp'
xfce4-terminal -T "Registry" --geometry 80x20+0+0 --hold -e "sshpass -p qwerty ssh sd0303@l040101-ws$1.ua.pt " &
sleep 1

echo "Remote Object"
#sshpass -p qwerty ssh sd0303@l040101-ws11.ua.pt 'unzip -o src.zip'
#sshpass -p qwerty ssh sd0303@l040101-ws11.ua.pt 'fuser -k 22320/tcp'
xfce4-terminal -T "Remote Object" --geometry 80x20+655+0 --hold -e "sshpass -p qwerty ssh sd0303@l040101-ws$1.ua.pt" &
sleep 1

echo "Geral"
sshpass -p qwerty ssh sd0303@l040101-ws$2.ua.pt 'unzip -o src.zip'
#sshpass -p qwerty ssh sd0303@l040101-ws11.ua.pt 'fuser -k 22321/tcp'
xfce4-terminal -T "Geral" --geometry 80x20+1300+0 --hold -e "sshpass -p qwerty ssh sd0303@l040101-ws$2.ua.pt" &
sleep 1

echo "Loja"
sshpass -p qwerty ssh sd0303@l040101-ws$3.ua.pt 'unzip -o src.zip'
#sshpass -p qwerty ssh sd0303@l040101-ws11.ua.pt 'fuser -k 22322/tcp'
xfce4-terminal -T "Loja" --geometry 80x20+0+340  --hold -e "sshpass -p qwerty ssh sd0303@l040101-ws$3.ua.pt" &
sleep 1

echo "Oficina"
sshpass -p qwerty ssh sd0303@l040101-ws$4.ua.pt 'unzip -o src.zip'
#sshpass -p qwerty ssh sd0303@l040101-ws11.ua.pt 'fuser -k 22323/tcp'
xfce4-terminal -T "Oficina" --geometry 80x20+655+340 --hold -e "sshpass -p qwerty ssh sd0303@l040101-ws$4.ua.pt" &
sleep 1

echo "ArmazemPM"
sshpass -p qwerty ssh sd0303@l040101-ws$5.ua.pt 'unzip -o src.zip'
#sshpass -p qwerty ssh sd0303@l040101-ws11.ua.pt 'fuser -k 22324/tcp'
xfce4-terminal -T "ArmazemPM" --geometry 80x20+1300+340 --hold -e "sshpass -p qwerty ssh sd0303@l040101-ws$5.ua.pt" &
sleep 1

echo "Dona"
sshpass -p qwerty ssh sd0303@l040101-ws$6.ua.pt 'unzip -o src.zip'
xfce4-terminal -T "Dona" --geometry 80x20+0+700 --hold -e "sshpass -p qwerty ssh sd0303@l040101-ws$6.ua.pt" &
#xfce4-terminal -T "Dona" --geometry 80x20+1300+700 --hold -e "sshpass -p qwerty ssh sd0303@l040101-ws$6.ua.pt './src/runDona.sh'" 
sleep 1

echo "Artesaos"
sshpass -p qwerty ssh sd0303@l040101-ws$7.ua.pt 'unzip -o src.zip'
xfce4-terminal -T "Artesaos" --geometry 80x20+655+700 --hold -e "sshpass -p qwerty ssh sd0303@l040101-ws$7.ua.pt" &
sleep 1



echo "Clientes"
sshpass -p qwerty ssh sd0303@l040101-ws$8.ua.pt 'unzip -o src.zip'
xfce4-terminal -T "Clientes" --geometry 80x20+1300+700 --hold -e "sshpass -p qwerty ssh sd0303@l040101-ws$8.ua.pt" &
sleep 1

echo -n "ler ficheiro de logging? (1 - sim, outro valor - nao)"
echo ""
read first_num

if [ $first_num -eq 1 ]; then
	sshpass -p qwerty scp sd0303@l040101-ws$2.ua.pt:/home/sd0303/src/dir_GeralSide/log log
fi
