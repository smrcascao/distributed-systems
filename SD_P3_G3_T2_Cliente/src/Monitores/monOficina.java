
package Monitores;

import Com.ClientCom;
import Message.Message;
import States.SArtesao;
import States.SDona;
import genclass.GenericIO;
import static java.lang.Thread.sleep;

/**
 * Oficina
 *
 * Repositorio oficina
 * As entidades que interage com este repositorio de dados sao:
 *  artesaos
 *  dona
 * @author rofler
*
 */
public class monOficina{
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
    
    //PM
    
    /**
   *  quantidade de PM em stock 
   *
   *    @serial stockPM
   */
    private int stockPM;
    

   /**
   *  quantidade total de PM encomendada 
   *
   *    @serial totalPM
   */
    private int totalPM;
            
    
   /**
   *  num total de fornecimentos de PM
   *
   *    @serial fornecimentosPM
   */
    private int fornecimentosPM;
    

    /**
   *  PM para construir um produto
   *
   *    @serial produtoPM
   */
    private int produtoPM;        
    

    /**
   *  limite minimo de PM
   *
   *    @serial limiteMinPM
   */
    private int limiteMinPM;
                
    //Produto
    /**
   * Produtos em stock
   *
   *    @serial stockProdutos
   */
    private int stockProdutos;
    
 
    /**
   *  quantidade total de produtos produzidos
   *
   *    @serial totalProdutos
   */
    private int totalProdutos;
    

    /**
   *  Limite de Alarme da quantidade de produtos
   *
   *    @serial alarmeProdutos
   */
    private int alarmeProdutos;
    

    /**
   *  Limite maximo de produtos
   *
   *    @serial limiteMaxProdutos
   */
    private int limiteMaxProdutos;
    

    /**
   *  quantidade de produtos que a dona leva para a loja
   *
   *    @serial produtosDona
   */
    private int produtosDona;
    
 
    /**
   *  dona chamada (por falta de PM)
   *
   *    @serial donaChamadaPM
   */
    private boolean donaChamadaPM;
    
    
    /**
   *  dona chamada (por existirem demasiados produtos em stock)
   *
   *    @serial donaChamadaProdutos
   */
    private boolean donaChamadaProdutos;
    
    
    /**
     *
     *  Construtor
     *
     * @param alarmeProd Limite de Alarme da quantidade de produtos
     * @param limiteProd Limite maximo de produtos
     * @param alarmePM limite minimo de PM
     * @param prodPM PM para construir um produto
     * @param prodDona quantidade de produtos que a dona leva para a loja
     * @param hostNameGeral nome do sistema computacional onde está localizado o servidor (regiao partilhada - repositorio geral)
     * @param portGeral número do porto de escuta do servidor do repositorio geral
     *
     */
    public monOficina(int alarmeProd, int limiteProd, int alarmePM, int prodPM, int prodDona, String hostNameGeral, int portGeral)
    {
        this.alarmeProdutos=alarmeProd;
        this.donaChamadaPM=false;
        this.donaChamadaProdutos=false;
        this.fornecimentosPM=0;
        this.limiteMaxProdutos=limiteProd;
        this.limiteMinPM=alarmePM;
        this.produtoPM=prodPM;
        this.stockPM=0;
        this.stockProdutos=0;
        this.totalPM=0;
        this.totalProdutos=0;
        this.produtosDona=prodDona;
        this.serverHostNameGeral=hostNameGeral;
        this.serverPortNumbGeral=portGeral;
                
    }
    
    
    /* funcoes dos artesaos*/
    
    /**
     *
     *  funcao usada pelo artesao para ver se ha PM para poder trabalhar
     *
     * @param id id do artesao
     * 
     * @return 0 se a quantidade de PM nao for suficiente para fazer um produto
     *         2 se for necessario chamar a dona para repor o stock de PM
     *         3 se nao houver espaco para mais produtos na oficina
     *         1 para outros casos
     * 
     */
    public synchronized int checkForMaterials(int id)
    {
        if(endOpArtesao())
            return 4;
        else if (stockProdutos==limiteMaxProdutos)
            return 3;
        else if(stockPM < produtoPM)
            return 0;
        else if (stockPM - produtoPM < limiteMinPM)
        {
            stockPM-=produtoPM;         //para o artesao reservar a PM que vai usar (se nao outro artesao pode pensar que a pode usar)
            return 2;   
        }
        else 
        {
            stockPM-=produtoPM;         //para o artesao reservar a PM que vai usar (se nao outro artesao pode pensar que a pode usar)
            return 1;
        }
    }
    
    
    /**
     *
     *  funcao usada pelo artesao para recolher PM para produzir um produto
     *
     * @param id id do artesao
     * 
     */
    public synchronized void collectMaterials(int id)
    {
        setnPMNaOficina(this.stockPM);
        pMGasta(produtoPM);
    }
    
    
    /**
     *
     *  funcao usada pelo artesao para chamar a dona por falta de PM
     *
     * @param id id do artesao
     * 
     */
    public synchronized void primeMaterialsNeeded(int id)
    {
        if(this.donaChamadaPM==false){
            setEstadoArtesao(id, SArtesao.CONTACTING_THE_ENTREPRENEUR);
            this.donaChamadaPM=true;
            setDonaChamadaPM(true);
            notifyAll();
        }
        else
        {
            try {
                this.wait();
            } catch (InterruptedException ex) {

            }
        }
        
    }
    
    
    /**
     *
     *  funcao usada pelo artesao para chamar a dona quando a quantidade de produtos excede o nivel de alarme
     *
     * @param id id do artesao
     * 
     */
    public synchronized void batchReadyForTransfer(int id)
    {
        if(this.donaChamadaProdutos==false)
        {
            setEstadoArtesao(id, SArtesao.CONTACTING_THE_ENTREPRENEUR);
            this.donaChamadaProdutos=true;
            setDonaChamadaProdutos(true);
            notifyAll();
        }
        else
        {
            try {
                this.wait();
            } catch (InterruptedException ex) {

            }
        }
                
    }
    
    
    /**
     *
     *  funcao usada pelo artesao para voltar a trabalhar apos ter terminado um produto ou contactado a dona
     *
     * @param id id do artesao
     * 
     */
    public synchronized void backToWork(int id)
    {
        setEstadoArtesao(id, SArtesao.FETCHING_PRIME_MATERIALS);
    }
    
    
    
    /**
     *
     *  funcao usada pelo artesao para colocar o produto que acabou de produzir no armazem da oficina
     *
     * @param id id do artesao
     * 
     * @return 0 se o armazem estiver cheio
     *         2 se a quantidade de produtos no armazem da oficina exceder o nivel de alarme
     *         1 para outros casos
     * 
     */
    public synchronized int goToStore(int id)
    {
        setEstadoArtesao(id, SArtesao.STORING_IT_FOR_TRANSFER);
        this.stockProdutos++;
        this.totalProdutos++;
        artesaoFezProduto(id);
        setnProdutosNaOficina(this.stockProdutos);
        if(stockProdutos >= limiteMaxProdutos )
            return 0;
        else if(stockProdutos >= alarmeProdutos)
            return 2;
        else 
            return 1;   
    }
        
    /**
     *
     *  funcao usada pelo artesao para comecar a fazer um produto
     *
     * @param id id do artesao
     * 
     */
    public synchronized void prepareToProduce(int id)
    {
        setEstadoArtesao(id, SArtesao.PRODUCING_A_NEW_PIECE);
    }
    
    
    
    
    
    /*funcoes da dona*/
    
    /**
     *
     *  funcao usada pela dona para receber produtos do armazem da oficina.
     *
     * 
     */
    public synchronized void goToWorkShop()
    {
        if(!endOpArtesao())
        {
            this.stockProdutos-=this.produtosDona;
            donaRecebeProdutos(this.produtosDona);
            setnProdutosNaOficina(this.stockProdutos);
            setEstadoDona(SDona.COLLECTING_A_BATCH_OF_PRODUCTS);
            this.donaChamadaProdutos=false;
            setDonaChamadaProdutos(false);
            System.out.println("\nA dona recolheu produtos.\n Quantidade de produtos que a dona leva: "+this.produtosDona+"\n Stock restante: "+this.stockProdutos);
            notifyAll();
        }
        else
        {
            System.out.println("\nA dona recolheu produtos.\n Quantidade de produtos que a dona leva: "+this.stockProdutos+"\n Stock restante: 0");
            donaRecebeProdutos(this.stockProdutos);
            this.stockProdutos=0;
            setnProdutosNaOficina(this.stockProdutos);
            setEstadoDona(SDona.COLLECTING_A_BATCH_OF_PRODUCTS);
            this.donaChamadaProdutos=false;
            setDonaChamadaProdutos(false);
            notifyAll();
        }  
        
    }
    
    
    
    /**
     *
     *  funcao usada pela dona para repor PM na oficina
     *
     * @param pm quantidade de pm fornecida pela dona
     * 
     */
    public synchronized void replenishStock( int pm)
    {
        this.fornecimentosPM++;
        novoFornecimentoPM(pm);
        this.totalPM+=pm;
        this.stockPM+=pm;
        this.donaChamadaPM=false;
        setDonaChamadaPM(false);
        setnPMNaOficina(this.stockPM);
        setEstadoDona(SDona.DELIVERING_PRIME_MATERIALS);
        
        System.out.println("\nA dona repos o stock de PM. \n quantidade de PM reposta: "+pm+"\n Stock de PM na Oficina: "+this.stockPM);
        
        notifyAll();
    }
    
    
    
    /*interaccao com o repositorio geral*/
    /**
     *
     * Funcao usada para saber se o artesao deve terminar o seu ciclo de vida. (comunica ao server a mensagem ENDOPA)
     *
    * @return true se o artesao deve terminar o seu ciclo de vida
    *         false caso contrario
     */
    private boolean endOpArtesao() {
        ClientCom con = new ClientCom(serverHostNameGeral, serverPortNumbGeral);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.ENDOPA);        // pede a realização do serviço
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if ((inMessage.getType() != Message.END) && (inMessage.getType() != Message.CONT)) {
            GenericIO.writelnString("Oficina: Tipo inválido!");
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
     *
     * Altera o estado dos Artesaos no repositorio geral.
     * (comunica ao server a mensagem START)
     * 
     * @param idArtesao   id do artesao que executa a operacao
     * @param estado estado para o qual o cliente vai mudar
     * 
     */
    private void setEstadoArtesao(int idArtesao, SArtesao estado) {

        ClientCom con = new ClientCom(serverHostNameGeral, serverPortNumbGeral);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.START, idArtesao, estado);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK) {
            GenericIO.writelnString("Oficina: Tipo inválido!");
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
     * a quantidade de pm na oficina (representada no repositorio geral).
     * (comunica ao server a mensagem SETPMW)
     *
     * @param stockPM quantidade de pm na oficina
     * 
     */
    private void setnPMNaOficina(int stockPM) {

        ClientCom con = new ClientCom(serverHostNameGeral, serverPortNumbGeral);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.SETPMW, stockPM);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK) {
            GenericIO.writelnString("Oficina: Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }
    
    /**
     *
     * um artesao gastou pm para fazer um produto (altera a quantidade total de pm representada no repositorio geral).
     * (comunica ao server a mensagem PMGAST)
     *
     * @param pm quantidade de PM que o artesao gastou
     * 
     */
    private void pMGasta(int pm) {

        ClientCom con = new ClientCom(serverHostNameGeral, serverPortNumbGeral);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.PMGAST, pm);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK) {
            GenericIO.writelnString("Oficina: Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }
    
    /**
     *
     * um artesao chama a dona para repor o stock de pm (altera o campo isDonaChamadaPM no repositorio geral).
     * (comunica ao server a mensagem SETDCPM)
     *
     * @param chamarDona novo estado do campo isDonaChamadaPM no repositorio geral
     * 
     */
    private void setDonaChamadaPM(boolean chamarDona) {

        ClientCom con = new ClientCom(serverHostNameGeral, serverPortNumbGeral);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.SETDCPM, chamarDona);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK) {
            GenericIO.writelnString("Oficina: Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }
    
    /**
     *
     * um artesao chama a dona para recolher produtos da oficina (altera o campo isDonaChamadaProdutos no repositorio geral).
     * (comunica ao server a mensagem SETDCPM)
     *
     * @param chamarDona novo estado do campo isDonaChamadaProdutos no repositorio geral
     * 
     */
    private void setDonaChamadaProdutos(boolean chamarDona) {

        ClientCom con = new ClientCom(serverHostNameGeral, serverPortNumbGeral);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.SETDCPR, chamarDona);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK) {
            GenericIO.writelnString("Oficina: Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }
    
    /**
     *
     * um artesao fez um produto incrementando a quantidade total de produtos feitos pelo mesmo (no repositorio geral).
     * (comunica ao server a mensagem ARTFP)
     *
     * @param idArtesao id do artesao que fez o produto
     * 
     */
    private void artesaoFezProduto(int idArtesao) {

        ClientCom con = new ClientCom(serverHostNameGeral, serverPortNumbGeral);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.ARTFP, idArtesao);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK) {
            GenericIO.writelnString("Oficina: Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }
    
    /**
     *
     * quantidade de produtos na oficina (representada no repositorio geral).
     * (comunica ao server a mensagem SETPRW)
     *
     * @param stockProd qunatidade de produtos na oficina
     * 
     */
    private void setnProdutosNaOficina(int stockProd) {

        ClientCom con = new ClientCom(serverHostNameGeral, serverPortNumbGeral);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.SETPRW, stockProd);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK) {
            GenericIO.writelnString("Oficina: Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }
    
    /**
     *
     * a dona recebe produtos que leva para a loja (no repositorio geral).
     * (comunica ao server a mensagem DONRPR)
     *
     * @param prodDona qunatidade de produtos que a dona leva para a loja
     * 
     */
    private void donaRecebeProdutos(int prodDona) {

        ClientCom con = new ClientCom(serverHostNameGeral, serverPortNumbGeral);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.DONRPR, prodDona);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK) {
            GenericIO.writelnString("Oficina: Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }
    
    /**
     *
     * a dona traz pm do armazem de PM para a oficina (no repositorio geral).
     * (comunica ao server a mensagem NEWPMF)
     *
     * @param pm pm que a dona traz do armazem de PM
     * 
     */
    private void novoFornecimentoPM(int pm) {

        ClientCom con = new ClientCom(serverHostNameGeral, serverPortNumbGeral);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.NEWPMF, pm);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK) {
            GenericIO.writelnString("Oficina: Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close();
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
