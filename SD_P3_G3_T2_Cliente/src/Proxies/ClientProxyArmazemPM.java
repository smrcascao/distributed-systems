/**
 *   Este tipo de dados define o thread agente prestador de serviço para uma solução do Problema Obrigatorio 2
 *   que implementa o modelo cliente-servidor de tipo 2 (replicação do servidor).
 *   A comunicação baseia-se em passagem de mensagens sobre sockets usando o protocolo TCP.
 */
package Proxies;

import Com.ServerCom;
import Message.Message;
import Message.MessageException;
import ServerInterface.ArmazemPMInterface;
import genclass.GenericIO;

/**
 *   Este tipo de dados define o thread agente prestador de serviço para uma solução do Problema Obrigatorio 2
 *   que implementa o modelo cliente-servidor de tipo 2 (replicação do servidor) relativo ao armazem de PM.
 *   A comunicação baseia-se em passagem de mensagens sobre sockets usando o protocolo TCP.
 *
 * @author rofler
 */
public class ClientProxyArmazemPM extends Thread
{
  /**
   *  Contador de threads lançados
   *
   *    @serial nProxy
   */

   private static int nProxy;

  /**
   *  Canal de comunicação
   *
   *    @serial sconi
   */

   private ServerCom sconi;

  /**
   *  Interface ao armazem pm
   *
   *    @serial armazemPM
   */

   private ArmazemPMInterface armazemPM=null;

  /**
   *  Instanciação do interface ao armazem pm.
   *
   *    @param sconi canal de comunicação
   *    @param armazem interface ao armazem pm
   */

   public ClientProxyArmazemPM (ServerCom sconi, ArmazemPMInterface armazem)
   {
      super ("Proxy_" + getProxyId ());

      this.sconi = sconi;
      this.armazemPM=armazem;
   }

  /**
   *  Ciclo de vida do thread agente prestador de serviço.
   */

   @Override
   public void run ()
   {
      Message inMessage = null,                                      // mensagem de entrada
              outMessage = null;                      // mensagem de saída

      inMessage = (Message) sconi.readObject ();                     // ler pedido do cliente
      try
      { outMessage = armazemPM.processAndReply (inMessage);         // processá-lo
      }
      catch (MessageException e)
      { GenericIO.writelnString ("Thread " + getName () + ": " + e.getMessage () + "!");
        GenericIO.writelnString (e.getMessageVal ().toString ());
        System.exit (1);
      }
      sconi.writeObject (outMessage);                                // enviar resposta ao cliente
      sconi.close ();                                                // fechar canal de comunicação
   }

  /**
   *  Geração do identificador da instanciação.
   *
   *    @return identificador da instanciação
   */

   private static int getProxyId ()
   {
      Class<Proxies.ClientProxyArmazemPM> cl = null;             // representação do tipo de dados ClientProxyGeral na máquina
                                                           //   virtual de Java
      int proxyId;                                         // identificador da instanciação

      try
      { cl = (Class<Proxies.ClientProxyArmazemPM>) Class.forName ("Proxies.ClientProxyArmazemPM");
      }
      catch (ClassNotFoundException e)
      { GenericIO.writelnString ("O tipo de dados ClientProxy não foi encontrado!");
        e.printStackTrace ();
        System.exit (1);
      }

      synchronized (cl)
      { proxyId = nProxy;
        nProxy += 1;
      }

      return proxyId;
   }
}
