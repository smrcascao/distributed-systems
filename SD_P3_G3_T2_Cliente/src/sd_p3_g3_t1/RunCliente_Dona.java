package sd_p3_g3_t1;

import Entidades.Dona;
import genclass.GenericIO;
import static java.lang.Thread.sleep;

/**
 *Programa para a execuçao dos clientes (dona).
 * @author rofler
 */
public class RunCliente_Dona {
    
    public static void main(String[] args) {
        Dona dona;
        String serverHostNameGeral = "l040101-ws01.ua.pt", serverHostNameOficina = "l040101-ws03.ua.pt", serverHostNameLoja = "l040101-ws04.ua.pt", serverHostNameArmazemPM = "l040101-ws05.ua.pt";                        // nome do sistema computacional onde está o servidor
        int serverPortNumbGeral = 22320, serverPortNumbOficina = 22321, serverPortNumbLoja = 22323, serverPortNumbArmazemPM = 22322;                                  // número do port de escuta do servidor

        /* Criação da thread dona */
        dona = new Dona(serverHostNameGeral, serverPortNumbGeral, serverHostNameLoja, serverPortNumbLoja, serverHostNameOficina, serverPortNumbOficina, serverHostNameArmazemPM, serverPortNumbArmazemPM);
        
        /* Arranque da simulação */
        dona.start();
        System.out.println("dona iniciou");

        try {
            dona.join();
        } catch (InterruptedException e) {
            GenericIO.writelnString("erro");
        }
        GenericIO.writelnString("a thread dona terminou.");
        GenericIO.writelnString();
        
        
        try {
                sleep((long) (3000));
            } catch (InterruptedException e) {
            }
        
        dona.shutdown();
    }
}
