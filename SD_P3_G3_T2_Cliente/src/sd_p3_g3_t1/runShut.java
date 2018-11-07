package sd_p3_g3_t1;

import Entidades.Dona;

/**
 *Programa usado para encerrar todos os servidores ( para evitar conflitos )
 * 
 * @author rofler
 */
public class runShut {
    
    public static void main(String[] args)
    {
        Dona dona;
        String serverHostNameGeral = "l040101-ws01.ua.pt", serverHostNameOficina = "l040101-ws03.ua.pt", serverHostNameLoja = "l040101-ws04.ua.pt", serverHostNameArmazemPM = "l040101-ws05.ua.pt";                        // nome do sistema computacional onde está o servidor
        int serverPortNumbGeral = 22320, serverPortNumbOficina = 22321, serverPortNumbLoja = 22323, serverPortNumbArmazemPM = 22322;                                  // número do port de escuta do servidor

        /* Criação da thread dona */
        dona = new Dona(serverHostNameGeral, serverPortNumbGeral, serverHostNameLoja, serverPortNumbLoja, serverHostNameOficina, serverPortNumbOficina, serverHostNameArmazemPM, serverPortNumbArmazemPM);
    
        dona.shutdownFirst();
    }   
}
