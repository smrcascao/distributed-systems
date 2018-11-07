package Entidades;

import Com.ClientCom;
import Message.Message;
import States.SCliente;
import genclass.GenericIO;

/**
 *Entidade activa Cliente.
 * O Cliente apenas interage com a loja e o repositorio geral.
 * Compra produtos e morre depois do artesao e antes da dona
 * @author rofler
 **/
public class Cliente extends Thread{
    
    /**
   *  Identificação do cliente
   *
   *    @serial customerId
   */
    private int idCliente;
   
    /**
   *  estado actual do cliente
   *
   *    @serial estadoCliente
   */
    private SCliente estadoCliente;
    
   /**
   *  Numero de compras efectuadas pelo cliente
   *
   *    @serial cumCompras
   */
   private int numCompras;
   
   /**
   *  Nome do sistema computacional onde está localizado o servidor do REpositorio Geral
   *    @serial serverHostNameGeral
   */

   private String serverHostNameGeral = null;

   /**
   *  Nome do sistema computacional onde está localizado o servidor da Loja
   *    @serial serverHostNameLoja
   */
   private String serverHostNameLoja = null;
   
  /**
   *  Número do porto de escuta do servidor (repositorio geral)
   *    @serial serverPortNumbGeral
   */

   private int serverPortNumbGeral;
   
   /**
   *  Número do porto de escuta do servidor (Loja)
   *    @serial serverPortNumbLoja
   */

   private int serverPortNumbloja;
   
    
    /**
     *
     *  Construtor
     * 
     * @param idCliente id do cliente
     * @param hostNameGeral nome do sistema computacional onde está localizado o servidor (regiao partilhada - repositorio geral)
     * @param hostNameLoja nome do sistema computacional onde está localizado o servidor (regiao partilhada - Loja)
     * @param portGeral número do porto de escuta do servidor do repositorio geral
     * @param portLoja número do porto de escuta do servidor da loja
     *
     */
    public Cliente(int idCliente, String hostNameGeral, int portGeral, String hostNameLoja, int portLoja) {
        
        this.numCompras=0;
        this.idCliente=idCliente;
        this.estadoCliente=SCliente.CARRYING_OUT_DAILY_CHORES;
        this.serverHostNameGeral =hostNameGeral;
        this.serverPortNumbGeral=portGeral;
        this.serverHostNameLoja =hostNameLoja;
        this.serverPortNumbloja=portLoja;
    }
    
    
    
    /**
   *  Ciclo de vida do thread cliente.
   */

   @Override
   public void run ()
   {
      boolean portaAberta;
      
      do{
            livingNormalLife();
            
            goShopping();
            
            portaAberta=isDoorOpen();


            if(portaAberta)
            {
                enterShop();

                if(perusingAround()){
                    iWantThis();
                }

                exitShop();
            }
            else{
                tryAgainLater();
            }

        }while(!endOpCliente());
      
   }
    

    /**
    *  devolve o numero de produtos que o cliente comprou
    * 
    * @return numero de produtos que o cliente comprou
    * 
    */
    public int getNumCompras() {
        return numCompras;
    }

    /**
    *  devolve o numero de identificacao do cliente
    * 
    * @return numero de identificacao do cliente
    * 
    */
    public int getIdCliente() {
        return idCliente;
    }

    /**
    *  o cliente fica em espera um tempo aleatorio(operação interna).
    */
    public void livingNormalLife()
    {
      try
      { sleep ((long) (1 + 40 * Math.random ()));
      }
      catch (InterruptedException e) {}
    }
    
    
    /*interaccao cliente - loja*/
    /**
    *  O cliente decide visitar a loja.
    */
    public void goShopping()
    {
        this.estadoCliente=SCliente.CHECKING_DOOR_OPEN;
        ClientCom con = new ClientCom(serverHostNameLoja, serverPortNumbloja);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        
        outMessage = new Message(Message.GOSHOP, idCliente);   // o cliente visita a loja
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK) {
            GenericIO.writelnString("Cliente " + this.idCliente + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close ();
        
    }
    
    /**
    *  O cliente verifica se a porta esta aberta.
    * 
    * @return true se a porta da loja estiver aberta
    *         false em caso contrario
    */
    public boolean isDoorOpen()
    {
        ClientCom con = new ClientCom(serverHostNameLoja, serverPortNumbloja);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.ISDOORO, idCliente);        // pede a realização do serviço
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if ((inMessage.getType() != Message.SHOPCL) && (inMessage.getType() != Message.SHOPOP)) {
            GenericIO.writelnString("Cliente " + this.idCliente + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close();

        if (inMessage.getType() == Message.SHOPOP)
            return true;                                                // porta aberta, pode entrar
        else
            return false;                                          // porta fechada, nao pode entrar
  
    }
    
   
    /**
     * 
     * O cliente entra na loja.
     * (comunica ao server a mensagem REQENTS)
     * 
     */
    private void enterShop() {
        this.estadoCliente = SCliente.APPRAISING_OFFER_IN_DISPLAY;
        
        ClientCom con = new ClientCom(serverHostNameLoja, serverPortNumbloja);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.REQENTS, idCliente);   // o cliente entra na loja
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK) {
            GenericIO.writelnString("Cliente " + this.idCliente + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close ();
    }

    
    /**
    *  O cliente avalia os produtos em exposicao na loja.
    * 
    * @return true se o cliente decide comprar produtos
    *         false em caso contrario
    */
    private boolean perusingAround() {
        ClientCom con = new ClientCom(serverHostNameLoja, serverPortNumbloja);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.PERUSE, idCliente);        // pede a realização do serviço
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if ((inMessage.getType() != Message.WILLBUY) && (inMessage.getType() != Message.WONTBUY)) {
            GenericIO.writelnString("Cliente " + this.idCliente + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close();

        if (inMessage.getType() == Message.WILLBUY)
            return true;                                                // o cliente decide comprar produtos
        else
            return false;                                          // o cliente nao compra produtos
    }

    /**
     * 
     * O cliente compra produtos.
     * (comunica ao server a mensagem IWANT)
     * 
     */
    private void iWantThis() {
        this.estadoCliente=SCliente.BUYING_SOME_GOODS;
        
        ClientCom con = new ClientCom(serverHostNameLoja, serverPortNumbloja);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.IWANT, idCliente);   // o cliente compra produtos
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK) {
            GenericIO.writelnString("Cliente " + this.idCliente + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close ();       
        
    }

    /**
     * 
     * O cliente sai da loja.
     * (comunica ao server a mensagem REQEXTS)
     * 
     */
    private void exitShop() {
        this.estadoCliente=SCliente.CARRYING_OUT_DAILY_CHORES;
        
        ClientCom con = new ClientCom(serverHostNameLoja, serverPortNumbloja);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.REQEXTS, idCliente);   // o cliente compra produtos
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK) {
            GenericIO.writelnString("Cliente " + this.idCliente + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close (); 
    }

    
    /**
     * 
     * O cliente desiste de tentar entrar na loja.
     * (comunica ao server a mensagem TRYLAT)
     * 
     */
    private void tryAgainLater() {
        this.estadoCliente=SCliente.CARRYING_OUT_DAILY_CHORES;
        
        ClientCom con = new ClientCom(serverHostNameLoja, serverPortNumbloja);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.TRYLAT, idCliente);   // o cliente compra produtos
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK) {
            GenericIO.writelnString("Cliente " + this.idCliente + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close ();
    }

    
    /*interaccao cliente - geral*/
    /**
    *  O cliente verifica se deve terminar o seu ciclo de vida.
    * 
    * @return true se a thread deve morrer
    *         false em caso contrario
    */
    private boolean endOpCliente() {
        ClientCom con = new ClientCom(serverHostNameGeral, serverPortNumbGeral);
        Message inMessage, outMessage;

        if (!con.open()) {
            return false;                                // nao foi possivel entrar em comunicacao
        }
        outMessage = new Message(Message.ENDOPCL, idCliente);        // pede a realização do serviço
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if ((inMessage.getType() != Message.END) && (inMessage.getType() != Message.CONT)) {
            GenericIO.writelnString("Cliente " + this.idCliente + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close();

        if (inMessage.getType() == Message.END)
            return true;                                                // o cliente deve terminar
        else
            return false;                                          // o cliente nao deve morrer
    }
    
    
    @Override
    public String toString() {
        return "Cliente{" + ", numCompras=" + numCompras + ", idCliente=" + idCliente + '}';
    }
    
}
