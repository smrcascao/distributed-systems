package sd_p3_g3_t1;

import Com.ServerCom;
import Monitores.monArmazemPM;
import Proxies.ClientProxyArmazemPM;
import ServerInterface.ArmazemPMInterface;
import genclass.GenericIO;

/**
 *Programa de execuçao do servidor Armazem PM.
 * @author rofler
 */
public class RunServerRegPart {
    

   
    /**
   *  Número do port de escuta do serviço a ser prestado no Armazem de PM (4003, por defeito)
   *
   *    @serial portNumbArmazemPM
   */

   private static final int portNumbArmazemPM = 22322;

  /**
   *  Programa principal.
   * @param args parametros de entrada predefinidos para a funçao main
   */

    public static void main(String[] args) {



        

        //ArmazemPM
        monArmazemPM armazemPM;                                   // barbearia (representa o serviço a ser prestado)
        ArmazemPMInterface armazePMInter;                      // interface à barbearia
        ServerCom sconArmazemPM, sconiArmazemPM;                               // canais de comunicação
        ClientProxyArmazemPM cliProxyArmazemPM;                                // thread agente prestador do serviço
        
        /* estabelecimento do servico */
        
        
        
        
        //Armazem PM
        sconArmazemPM = new ServerCom(portNumbArmazemPM);                     // criação do canal de escuta e sua associação
        sconArmazemPM.start();                                       // com o endereço público
        armazemPM =new monArmazemPM(300, "l040101-ws01.ua.pt", 22320, 2);                           // (int stockInicialPM, String hostNameGeral, int portGeral, int prodPM)
        armazePMInter = new ArmazemPMInterface(armazemPM);        // activação do interface com o serviço
        GenericIO.writelnString("O serviço foi estabelecido!");
        GenericIO.writelnString("O servidor ArmazemPM esta em escuta.");
        
        
        /* processamento de pedidos */
        while (true) {

            
            
            sconiArmazemPM = sconArmazemPM.accept();                            // entrada em processo de escuta
            cliProxyArmazemPM = new ClientProxyArmazemPM(sconiArmazemPM, armazePMInter);    // lançamento do agente prestador do serviço
            cliProxyArmazemPM.start();
        }
        
        

        
    }
}
