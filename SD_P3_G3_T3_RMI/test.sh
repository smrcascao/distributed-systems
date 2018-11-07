echo -n "ler ficheiro de logging? (1 - sim, outro valor - nao)"
echo ""
read first_num

if [ $first_num -eq 1 ]; then
	sshpass -p qwerty scp sd0303@l040101-ws01.ua.pt:/home/sd0303/src/dir_GeralSide/log log
fi