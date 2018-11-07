package sd_p3_g3_t1;

import Com.ServerCom;
import Monitores.monOficina;
import Proxies.ClientProxyOficina;
import ServerInterface.OficinaInterface;
import genclass.GenericIO;

/**
 *Programa de execuçao do servidor Oficina.
 * @author rofler
 */
public class RunServerOficina {
    /**
   *  Número do port de escuta do serviço a ser prestado na Oficina (4001, por defeito)
   *
   *    @serial portNumbOficina
   */

   private static final int portNumbOficina = 22321;
   
   public static void main(String[] args)
   {
               //Oficina
        monOficina oficina;                                    // barbearia (representa o serviço a ser prestado)
        OficinaInterface oficinaInter;                      // interface à barbearia
        ServerCom sconOficina, sconiOficina;                               // canais de comunicação
        ClientProxyOficina cliProxyOficina;                                // thread agente prestador do serviço
        
        //Oficina
        sconOficina = new ServerCom(portNumbOficina);                     // criação do canal de escuta e sua associação
        sconOficina.start();                                       // com o endereço público
        oficina = new monOficina(25, 100, 50, 2, 25, "l040101-ws01.ua.pt", 22320);// (int alarmeProd, int limiteProd, int alarmePM, int prodPM, int prodDona, String hostNameGeral, int portGeral)
        oficinaInter = new OficinaInterface(oficina);        // activação do interface com o serviço
        GenericIO.writelnString("O serviço foi estabelecido!");
        GenericIO.writelnString("O servidor Oficina esta em escuta.");
        
        while(true)
        {
            sconiOficina = sconOficina.accept();                            // entrada em processo de escuta
            cliProxyOficina = new ClientProxyOficina(sconiOficina, oficinaInter);    // lançamento do agente prestador do serviço
            cliProxyOficina.start();
            
        }
   }
}
