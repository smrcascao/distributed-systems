
package Monitores;

import Com.ClientCom;
import Message.Message;
import States.SCliente;
import States.SDona;
import genclass.GenericIO;
import static java.lang.Thread.sleep;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 *
 * Estrutura de dados Loja.
 * Entidades activas que interagem com a regiao partilhada:
 * Dona
 * Clientes
 * 
 * 
 */
public class monLoja{
    
     //variaveis
    /**
     * Nome do sistema computacional onde está localizado o servidor do
     * REpositorio Geral
     *
     * @serial serverHostNameGeral
     */
    private String serverHostNameGeral = null;
    
    /**
     * Número do porto de escuta do servidor (repositorio geral)
     *
     * @serial serverPortNumbGeral
     */
    private int serverPortNumbGeral;
    
    /**
    *  Numero de produtos em exposicao na loja
    *
    *    @serial nProdutosLoja
    */
    private int nProdutosLoja;
    
    /**
    *  Numero total de produtos vendidos na loja
    *
    *    @serial nTotalProdutosVendidos
    */
    private int nTotalProdutosVendidos;
    
    /**
    *  Numero total de produtos vendidos na loja do dia (por usar)
    *
    *    @serial nTotalProdutosVendidosPorDia
    */
    private int nTotalProdutosVendidosPorDia;
    
    
   /**
   *    Numero maximo de produtos na loja
   *
   *    @serial nMaximoProdutosLoja
   */
    private int nMaximoProdutosLoja; 
    
   /**
   *  Estado da porta da loja (0- porta fechada ; 1- porta Aberta)
   *
   *    @serial portaAberta
   *    
   */
    private boolean portaAberta; 
    
   /**
   *  Numero de clientes dentro da loja
   *
   *    @serial nClientesLoja
   */
    private int nClientesLoja;
    
   /**
   *  fila de clientes a espera para comprar produtos
   *
   *    @serial filaClientes
   */
    private Queue<Integer> filaClientes;
    
    
    /**
     *
     *  Construtor
     *
     * @param nMaximoProdutosLoja Limite maximo de produtos em exposicao na loja
     * @param hostNameGeral nome do sistema computacional onde está localizado o servidor (regiao partilhada - repositorio geral)
     * @param portGeral número do porto de escuta do servidor do repositorio geral
     *
     */
    public monLoja(int nMaximoProdutosLoja, String hostNameGeral, int portGeral) {
        
        this.nClientesLoja=0;
        this.filaClientes= new LinkedList();
        
        if(nMaximoProdutosLoja>0)
            this.nMaximoProdutosLoja=nMaximoProdutosLoja;
        
        this.portaAberta=false;
        this.nProdutosLoja=0;
                
        this.nTotalProdutosVendidos=0;
        this.nTotalProdutosVendidosPorDia=0;
        this.serverHostNameGeral=hostNameGeral;
        this.serverPortNumbGeral=portGeral;
        
    }
    
    /*funcoes do cliente*/
    
    /**
     *
     *  establece a primenira interaccao do cliente com o loja
     *
     * @param idCliente id do cliente
     * 
     */
    public synchronized void goShopping(int idCliente){
        //cliente.setEstadoCliente(idCliente, SCliente.CHECKING_DOOR_OPEN);
        setEstadoCliente(idCliente, SCliente.CHECKING_DOOR_OPEN);
    }
    
    
    /**
     *
     *  funcao usada pelo cliente para ver se a porta esta aberta
     *
     * @return   true, se a porta esta aberta
     *           false, em caso contrário 
     */
    public synchronized boolean isDoorOpen(){
        return this.portaAberta;
    }
    
    
    /**
     *
     *  Ciente entra na loja
     *
     * @param idCliente id do cliente
     * 
     */  
    public synchronized void enterShop(int idCliente){
        nClientesLoja++;
        clienteEntra();
        setEstadoCliente(idCliente, SCliente.APPRAISING_OFFER_IN_DISPLAY);
    }
    
    
    /**
     *
     *  O cliente observa os produtos na loja
     *
     * @return   true, se o cliente quer comprar um ou mais produtos
     *           false, em caso contrário (se nao existirem produtos na loja, o return e sempre false)
     */
    public synchronized boolean perusingAround() {
        if(this.nProdutosLoja==0)
            return false;
        Random espera = new Random();
        return espera.nextInt(100) >= 10;
    }
    
    
    /**
     *
     *  Ciente sai da loja
     *
     * @param idCliente id do cliente
     * 
     */  
    public synchronized void exitShop(int idCliente) {
        this.nClientesLoja--;
        clienteSai();
        setEstadoCliente(idCliente, SCliente.CARRYING_OUT_DAILY_CHORES);
        if(nClientesLoja == 0)
            notifyAll();
    }
    
    
    /**
     *
     *  Ciente quer comprar produtos (o numero de produtos a comprar e calculado aleatoriamente) 
     *  e e inserido na fila de espera e fica a dormir ate ser chamado pela dona
     * 
     * @param idCliente id do cliente
     * 
     */  
    public synchronized void iWantThis(int idCliente) {
        
        Random rand= new Random();
        int produtosAComprar= 0;
        if(this.nProdutosLoja!=0)
            {
            while(produtosAComprar==0)
                produtosAComprar=rand.nextInt(this.nProdutosLoja+1);
            this.nProdutosLoja-=produtosAComprar;

            setnProdutosNaLoja(this.nProdutosLoja);

            setEstadoCliente(idCliente, SCliente.BUYING_SOME_GOODS);
            filaClientes.add(idCliente);

            notifyAll();
            try {
                this.wait();
            } catch (InterruptedException ex) {

            }

            System.out.println("\nO cliente "+idCliente+" comprou "+produtosAComprar+" produtos.\n Produtos em exposiçao na Loja: "+this.nProdutosLoja);
            compraCliente(idCliente, produtosAComprar);
        }
    }
    
    
    /**
     *
     *  Ciente termina a interaccao com a loja sem nunca ter entrado 
     *
     * @param idCliente id do cliente
     * 
     */  
    public synchronized void tryAgainLater(int idCliente)
    {
        setEstadoCliente(idCliente, SCliente.CARRYING_OUT_DAILY_CHORES);
    }
    
    
    /*funcoes da dona*/
    
    /**
     *
     *  A dona atende um cliente (inicio da interaccao)
     * 
     * @return id do cliente a servir  
     *  
     */
    public synchronized int addressACustomer()
    {
        setEstadoDona(SDona.ATTENDIND_A_CUSTOMER);
        return filaClientes.peek();
    }
    
    
    /**
     *
     *  A dona despede-se do cliente (fim da interaccao)
     *
     * @param idCliente id do cliente
     * 
     */
    public synchronized void sayGoodbyeToCustomer( int idCliente)
    {
        filaClientes.poll();
        setEstadoDona(SDona.WAITING_FOR_NEXT_TASK);
        notifyAll();
    }
       
    
    /**
     *
     *  A dona despede-se do cliente (fim da interaccao).
     *
     * 
     */
    public synchronized void prepareToWork()
    {
        this.portaAberta=true;
        setShopDoorStat("open");
        setEstadoDona(SDona.WAITING_FOR_NEXT_TASK);
        
        int aux=getProdutosComDona();
        if(aux!=0)
        {
            this.nProdutosLoja+=aux;
            System.out.println("\n A dona traz "+aux+" produtos para a loja.\n Quantidade de produtos em disposiçao: "+this.nProdutosLoja);
            
        }
        
        setnProdutosNaLoja(this.nProdutosLoja);
        
        
        try {
            this.wait();
        } catch (InterruptedException ex) {
            
        }
    }
    
    /**
     *
     *  A dona verifica se ha tarefas que necessita a sua accao 
     *
     * 
     * @return 1 se o ciclo de vida da dona deve terminar;
     *         2 se a fila de clientes nao esta vazia; 
     *         3 se a dona tiver sido chamada para ir buscar produtos a ofcina;
     *         4 se a dona tiver sido chamada para levar PM a oficina; 
     *         0 para outros casos 
     *  
     */
    public synchronized int appraiseSit()
    {
        if(!this.filaClientes.isEmpty())
            return 2;
        else if(isDonaChamadaPM())
        {   
            return 4;
        }
        else if(isDonaChamadaProdutos())
            return 3;
        else if(endOpDona())
        {   
            return 1;
        }
        else
            return 0;
    }
    
    /**
     *
     *  funcao usada pela dona para ver se ha clientes na loja
     *
     * @return true se houverem clientes na loja
     *         false se nao houverem clientes na loja
     *  
     */
    public synchronized boolean customersInTheShop()
    {
        return(this.nClientesLoja!=0);
    }
    
    
    /**
     *
     *  funcao usada pela dona para fechar a porta da loja
     * 
     */
    public synchronized void closeTheDoor()
    {
        
        this.portaAberta=false;
        setShopDoorStat("opdc");
        
    }
    
    
    /**
     *
     *  funcao usada pela dona para abandonar a Loja
     * 
     */
    public synchronized void prepareToLeave()
    {
        setEstadoDona(SDona.CLOSING_THE_SHOP);
        setShopDoorStat("clos");
    }
    
    
    /**
     *
     *  funcao usada pela dona para regressar a Loja
     * 
     */   
    public synchronized void returnToShop()
    {
        setEstadoDona(SDona.OPENING_THE_SHOP); 
    }
    
    
    /*interaccao com o repositorio geral*/
    /**
     *
     * Altera o estado dos clientes no repositorio geral.
     * (comunica ao server a mensagem STCLI)
     * 
     * @param idCliente id do cliente que executa a operacao
     * @param estado estado para o qual o cliente vai mudar
     *
     */
    private void setEstadoCliente(int idCliente, SCliente estado) {

        ClientCom con = new ClientCom(serverHostNameGeral, serverPortNumbGeral);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.STCLI, idCliente, estado);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK) {
            GenericIO.writelnString("Loja: Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }
    
    /**
     *
     * Altera o estado da dona no repositorio geral.
     * (comunica ao server a mensagem STDON)
     * 
     * @param estado novo estado da dona
     *
     */
    private void setEstadoDona(SDona estado) {

        ClientCom con = new ClientCom(serverHostNameGeral, serverPortNumbGeral);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.STDON, estado);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK) {
            GenericIO.writelnString("Loja: Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }
    
    /**
     *
     * Incrementa o numero de clientes na loja (no repositorio geral).
     * (comunica ao server a mensagem CLIIN)
     *
     */
    private void clienteEntra() {

        ClientCom con = new ClientCom(serverHostNameGeral, serverPortNumbGeral);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.CLIIN);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK) {
            GenericIO.writelnString("Loja: Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }
    
    
    /**
     *
     * Decrementa o numero de clientes na loja (no repositorio geral).
     * (comunica ao server a mensagem CLIOUT)
     *
     */
    private void clienteSai() {

        ClientCom con = new ClientCom(serverHostNameGeral, serverPortNumbGeral);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.CLIOUT);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK) {
            GenericIO.writelnString("Loja: Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }
    
    /**
     *
     * Cliente adquire produtos (no repositorio geral).
     * (comunica ao server a mensagem CLIOUT)
     *
     * @param idCliente id cdo cliente que executa a operacao
     * @param produtosAComprar numero de produtos que o cliente compra
     */
    private void compraCliente(int idCliente, int produtosAComprar) {

        ClientCom con = new ClientCom(serverHostNameGeral, serverPortNumbGeral);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.CLIBUY, idCliente, produtosAComprar);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK) {
            GenericIO.writelnString("Loja: Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }
    
    
    /**
     *
     * altera o numero de produtos na loja (no repositorio geral).
     * (comunica ao server a mensagem PRODINST)
     *
     * @param nProdutosLoja numero de produtos na loja (valor a usar no repositorio geral / ficheiro de logging)
     */
    private void setnProdutosNaLoja(int nProdutosLoja) {

        ClientCom con = new ClientCom(serverHostNameGeral, serverPortNumbGeral);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.PRODINST, nProdutosLoja);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK) {
            GenericIO.writelnString("Loja: Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }
    
    /**
     *
     * altera o estado da porta da loja (no repositorio geral).
     * (comunica ao server a mensagem PRODINST)
     *
     * @param state novo estado para a porta da loja ("open", "opdc" ou "clos")
     * 
     */
    private void setShopDoorStat(String state) {

        ClientCom con = new ClientCom(serverHostNameGeral, serverPortNumbGeral);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.SETDOORSTAT, state);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK) {
            GenericIO.writelnString("Loja: Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }
    
    /**
     *
     * altera o estado da porta da loja (no repositorio geral).
     * (comunica ao server a mensagem GETPRODD)
     *
     * @return quantidade de produtos que a dona traz consigo
     */
    private int getProdutosComDona() {

        ClientCom con = new ClientCom(serverHostNameGeral, serverPortNumbGeral);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.GETPRODD);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.GETPRODDR) {
            GenericIO.writelnString("Loja: Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close();
        
        return (inMessage.getId());
    }
    
    /**
     *
     * consulta se a dona ja foi chamada por um artesao para recolher produtos (no repositorio geral).
     * (comunica ao server a mensagem donaChamadaProdutos)
     *
     * @return true se a dona ja foi chamada
     *         false caso contrario
     */
    private boolean isDonaChamadaProdutos() {
      ClientCom con = new ClientCom(serverHostNameGeral,serverPortNumbGeral);
        
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.isDonaChamadaProdutos);   // o cliente compra produtos
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if ((inMessage.getType() != Message.donaChamadaProdutos) && (inMessage.getType() != Message.donaNaoChamadaProdutos)) {
            GenericIO.writelnString("Loja: Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close ();
        
         if (inMessage.getType() == Message.donaChamadaProdutos)
             return true;
         else
             return false;
    }
    
    /**
     *
     * consulta se a dona ja foi chamada por um artesao para comprar PM (no repositorio geral).
     * (comunica ao server a mensagem isDonaChamadaPM)
     *
     * @return true se a dona ja foi chamada
     *         false caso contrario
     */
    private boolean isDonaChamadaPM() {
      ClientCom con = new ClientCom(serverHostNameGeral,serverPortNumbGeral);
        
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.isDonaChamadaPM);   // o cliente compra produtos
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if ((inMessage.getType() != Message.donaJaFoiAlertada) && (inMessage.getType() != Message.donaNaoFoiAlertada)) {
            GenericIO.writelnString("Loja: Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close ();
        
         if (inMessage.getType() == Message.donaJaFoiAlertada)
             return true;
         else
             return false;
    }
    
    /**
     *
     * Funcao usada para saber se a dona deve terminar o seu ciclo de vida. (comunica ao server a mensagem ENDOPD)
     *
     * @return true se a dona deve terminar o seu ciclo de vida
     *         false caso contrario
     */
    private boolean endOpDona() {
        ClientCom con = new ClientCom(serverHostNameGeral, serverPortNumbGeral);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.ENDOPD);        // pede a realização do serviço
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if ((inMessage.getType() != Message.END) && (inMessage.getType() != Message.CONT)) {
            GenericIO.writelnString("Loja: Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close();

        if (inMessage.getType() == Message.END)
            return true;                                                // o cliente deve terminar
        else
            return false;                                          // o cliente nao deve morrer
    }
    
    /**
   *  Encerra a regiao partilhada.
   *  <p>o Server recebeuma mensagem da dona e encerra.
   */
   public void shutdown ()
   {
       System.out.println("Mensagem recebida.\nO server vai encerrar");
       System.exit (0);
   }
}
