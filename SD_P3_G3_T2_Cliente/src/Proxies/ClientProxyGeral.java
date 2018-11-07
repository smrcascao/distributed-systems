
package Proxies;

import Com.ServerCom;
import Message.Message;
import Message.MessageException;
import ServerInterface.GeralInterface;
import genclass.GenericIO;

/**
 *   Este tipo de dados define o thread agente prestador de serviço para uma solução do Problema Obrigatorio 2
 *   que implementa o modelo cliente-servidor de tipo 2 (replicação do servidor) relativo ao repositorio geral.
 *   A comunicação baseia-se em passagem de mensagens sobre sockets usando o protocolo TCP.
 *
 * @author rofler
 */
public class ClientProxyGeral extends Thread
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
   *  Interface ao repositorio geral
   *
   *    @serial geral
   */

   private GeralInterface geral=null;

  /**
   *  Instanciação do interface ao repositorio geral.
   *
   *    @param sconi canal de comunicação
   *    @param geral interface ao geral
   */

   public ClientProxyGeral (ServerCom sconi, GeralInterface geral)
   {
      super ("Proxy_" + getProxyId ());

      this.sconi = sconi;
      this.geral = geral;
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
      { outMessage = geral.processAndReply (inMessage);         // processá-lo
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
      Class<Proxies.ClientProxyGeral> cl = null;             // representação do tipo de dados ClientProxyGeral na máquina
                                                           //   virtual de Java
      int proxyId;                                         // identificador da instanciação

      try
      { cl = (Class<Proxies.ClientProxyGeral>) Class.forName ("Proxies.ClientProxyGeral");
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
