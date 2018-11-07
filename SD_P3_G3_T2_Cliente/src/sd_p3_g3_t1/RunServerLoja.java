package sd_p3_g3_t1;

import Com.ClientCom;
import Com.ServerCom;
import Message.Message;
import Monitores.monLoja;
import Proxies.ClientProxyLoja;
import ServerInterface.LojaInterface;
import genclass.GenericIO;

/**
 *Programa de execuçao do servidor Loja.
 * @author rofler
 */
public class RunServerLoja {
        /**
   *  Número do port de escuta do serviço a ser prestado na Loja(4002, por defeito)
   *
   *    @serial portNumbLoja
   */

   private static final int portNumbLoja = 22323;
   
   public static void main(String[] args) {
       //Loja
        monLoja loja;                                   // barbearia (representa o serviço a ser prestado)
        LojaInterface lojaInter;                      // interface à barbearia
        ServerCom sconLoja, sconiLoja;                               // canais de comunicação
        ClientProxyLoja cliProxyLoja;                                // thread agente prestador do serviço
        
        //Loja
        sconLoja = new ServerCom(portNumbLoja);                     // criação do canal de escuta e sua associação
        sconLoja.start();                                       // com o endereço público
        loja = new monLoja(500, "l040101-ws01.ua.pt", 22320);                           // (int nMaximoProdutosLoja, String hostNameGeral, int portGeral)
        lojaInter = new LojaInterface(loja);        // activação do interface com o serviço
        GenericIO.writelnString("O serviço foi estabelecido!");
        GenericIO.writelnString("O servidor Loja esta em escuta.");
        
        ClientCom con;                                       // canal de comunicação
        Message inMessage, outMessage;                       // mensagens trocadas
/*
        con = new ClientCom("geral", 4000);
        while (!con.open()) {
            try {
                Thread.sleep((long) (1000));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.SETDOORSTAT, "clos");
        con.writeObject(outMessage);
        GenericIO.writelnString("prestes a falhar!");
        inMessage = (Message) con.readObject();
        GenericIO.writelnString("uff!");
        if (inMessage.getType() != Message.ACK) {
            GenericIO.writelnString("Arranque da simulação: Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close();*/
        
        
        while (true) {           
            sconiLoja = sconLoja.accept();                            // entrada em processo de escuta
            cliProxyLoja = new ClientProxyLoja(sconiLoja, lojaInter);    // lançamento do agente prestador do serviço
            cliProxyLoja.start();
            

       }
   }
}
