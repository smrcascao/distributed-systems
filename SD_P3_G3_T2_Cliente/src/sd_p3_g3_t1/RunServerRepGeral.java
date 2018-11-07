package sd_p3_g3_t1;

import Com.ServerCom;
import Monitores.monGeral;
import Proxies.ClientProxyGeral;
import ServerInterface.GeralInterface;
import genclass.GenericIO;

/**
 *Programa de execuçao do servidor Repositorio Geral.
 * @author rofler
 */
public class RunServerRepGeral {
    /**
   *  Número do port de escuta do serviço a ser prestado (4000, por defeito)
   *
   *    @serial portNumb
   */

   private static final int portNumb = 22320;
   
   public static void main (String [] args)
   {
       monGeral repGeral;
       GeralInterface repGeralInter;
       ServerCom scon, sconi;                               // canais de comunicação
       ClientProxyGeral cliProxy;                                // thread agente prestador do serviço
       
      scon = new ServerCom(portNumb);                     // criação do canal de escuta e sua associação
      scon.start ();                                       // com o endereço público
      repGeral = new monGeral(3, 3, 300, 2, "log");        // monGeral(int numArtesaos, int numClientes, int totalPM, int produtoPM, String fileName)
      repGeralInter = new GeralInterface(repGeral);        // activação do interface com o serviço
      GenericIO.writelnString ("O serviço foi estabelecido!");
      GenericIO.writelnString ("O servidor Repositorio Geral esta em escuta.");

      /* processamento de pedidos */

      while (true)
      { sconi = scon.accept ();                            // entrada em processo de escuta
        cliProxy = new ClientProxyGeral(sconi, repGeralInter);    // lançamento do agente prestador do serviço
        cliProxy.start ();
      }
   }
   
}
