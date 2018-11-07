/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import Com.ClientCom;
import Message.Message;
import States.SArtesao;
import genclass.GenericIO;
import static java.lang.Thread.sleep;
/**
 *Entidade activa Artesao.
 * O artesao apenas interage com a oficina e o repositorio geral.
 * Faz produtos e alerta a Dona para a falta de PM ou excesso de produtos no armazem
 * @author rofler
 **/
public class Artesao extends Thread{
    
    /**
    *  id do artesao
    *
    *    @serial idArtesao
    */
    private int idArtesao;
    
    /**
    *  estado actual do artesao
    *
    *    @serial estadoArtesao
    */
    private SArtesao estadoArtesao;
       
        
    /**
    *  numero de produtos feitos pelo artesao
    *
    *    @serial produtosFeitos
    */
    private int produtosFeitos;
    
    
    /**
   *  Nome do sistema computacional onde está localizado o servidor do REpositorio Geral
   *    @serial serverHostNameGeral
   */

   private String serverHostNameGeral = null;

   /**
   *  Nome do sistema computacional onde está localizado o servidor da Loja
   *    @serial serverHostNameLoja
   */
   private String serverHostNameOficina = null;
   
  /**
   *  Número do porto de escuta do servidor (repositorio geral)
   *    @serial serverPortNumbGeral
   */

   private int serverPortNumbGeral;
   
   /**
   *  Número do porto de escuta do servidor (Oficina)
   *    @serial serverPortNumbLoja
   */
   
      private int serverPortNumbOficina;
   
   
    /**
     *
     *  Construtor
     * 
     * @param idArtesao id do artesao
     * @param hostNameGeral nome do sistema computacional onde está localizado o servidor (regiao partilhada - repositorio geral)
     * @param hostNameOficina nome do sistema computacional onde está localizado o servidor (regiao partilhada - Oficina)
     * @param portGeral número do porto de escuta do servidor do repositorio geral
     * @param portOficina número do porto de escuta do servidor da Oficina
     *
     */
    public Artesao(int idArtesao ,String hostNameGeral, int portGeral,String hostNameOficina,int portOficina) {
        
        this.idArtesao=idArtesao;
        this.estadoArtesao=SArtesao.FETCHING_PRIME_MATERIALS;
        this.produtosFeitos=0;
        this.serverHostNameGeral =hostNameGeral;
        this.serverPortNumbGeral=portGeral;
        this.serverHostNameOficina =hostNameOficina;
        this.serverPortNumbOficina=portOficina;
        
  
    }
    
    
    /**
    *  funcao que executa o ciclo de vida do artesao
    */
    public void run() {
        
        boolean canWork;
        int m=1, ligaDona;
        
        do{
           canWork= false;

           while(!canWork)
           {               
               m= checkForMaterials(idArtesao);
               estadoArtesao=SArtesao.FETCHING_PRIME_MATERIALS;

               switch(m)
               {
               case(1):
                        canWork=true;
                        break;
               case(2):
                        if (isDonaChamadaPM()==false && isArmPM())
                        {
                            primeMaterialsNeeded(idArtesao);
                            estadoArtesao=SArtesao.CONTACTING_THE_ENTREPRENEUR;
                            backToWork(idArtesao);
                            estadoArtesao=SArtesao.FETCHING_PRIME_MATERIALS;
                        }
                        canWork=true;
                        break;
               case(3):
                        break;
               case(0):
                        if (isDonaChamadaPM()==false && isArmPM())
                        {
                            primeMaterialsNeeded(idArtesao);
                            estadoArtesao=SArtesao.CONTACTING_THE_ENTREPRENEUR;
                            backToWork(idArtesao);
                            estadoArtesao=SArtesao.FETCHING_PRIME_MATERIALS;
                        }
                        break;
                   
                case(4):
                        canWork=true;
                        break;


               }
           }    
            
           if(m!=4)
           {
                collectMaterials(idArtesao);
                prepareToProduce(idArtesao);
                estadoArtesao=SArtesao.PRODUCING_A_NEW_PIECE;

                shapingItUp();
                produtosFeitos++;

                ligaDona =goToStore(idArtesao);
                estadoArtesao=SArtesao.STORING_IT_FOR_TRANSFER;

                if((ligaDona==2 || ligaDona==0) && isDonaChamadaProdutos()==false)
                {
                    batchReadyForTransfer(idArtesao);
                    estadoArtesao=SArtesao.CONTACTING_THE_ENTREPRENEUR;
                }
                backToWork(idArtesao);
           }

        }while(!endOpArtesao());
        
        if(isDonaChamadaProdutos()==false && m!= 4)
        {
            batchReadyForTransfer(idArtesao);
            estadoArtesao=SArtesao.CONTACTING_THE_ENTREPRENEUR;
        }

    }

    /**
    *  fabrica o produto (operação interna).
    */
    public void shapingItUp()
    {
     try
      { sleep ((long) (1 + 10 * Math.random ()));
      }
      catch (InterruptedException e) {}
    }

    /**
    *  devolve o numero de produtos que o artesao fez
    * 
    * @return numero de produtos que o artesao fez
    * 
    */
    public int getProdutosFeitos() {
        return produtosFeitos;
    }



    @Override
    public String toString() {
        return "Artesao{" + "id=" + idArtesao + ", produtosFeitos=" + produtosFeitos + '}';
    }

    /*interaccao artesao - oficina*/
    
    /**
    *  funcao usada pelo artesao para ver se ha PM para poder trabalhar
    * 
    * @param idArtesao id do artesao que executa a operacao
    * 
    * @return retorna os casos possiveis
    *         0 se a quantidade de PM nao for suficiente para fazer um produto
     *        2 se for necessario chamar a dona para repor o stock de PM
     *        3 se nao houver espaco para mais produtos na oficina
     *        4 se o artesao deve terminar o seu ciclo de vida
     *        1 para outros casos
    */
    private int checkForMaterials(int idArtesao) {
           
  
        ClientCom con = new ClientCom(serverHostNameOficina,serverPortNumbOficina);
        
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.checkForMaterials, idArtesao);   // o cliente compra produtos
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if ((inMessage.getType() != Message.endOpArtesao) && (inMessage.getType() != Message.maxStockProdutos)&& (inMessage.getType() != Message.PMInsuficiente)&& (inMessage.getType() != Message.PMOK) && (inMessage.getType() != Message.PMInfAAlarme)) {
            GenericIO.writelnString("Thread artesao" + this.idArtesao + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close ();
        
        if (inMessage.getType() == Message.endOpArtesao)
           return 4;
        else
         if (inMessage.getType() == Message.maxStockProdutos)       
           return 3;
         else
         if (inMessage.getType() == Message.PMInsuficiente)       
           return 0;
         else
         if (inMessage.getType() == Message.PMOK)       
           return 1;
         else
         if (inMessage.getType() == Message.PMInfAAlarme)       
           return 2;
        
        return -1;
    }
    
    /**
    *  funcao usada pelo artesao para contactar a dona se houver falta de PM na oficina
    * 
    * @param idArtesao id do artesao que executa a operacao
    * 
    */
    private void primeMaterialsNeeded(int idArtesao) {
          ClientCom con = new ClientCom(serverHostNameOficina,serverPortNumbOficina);
        
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.primeMaterialsNeeded, idArtesao);   // A dona foi alertada
       con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK) {
            GenericIO.writelnString("Thread artesao" + this.idArtesao + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close ();

    }

    /**
    *  funcao usada pelo artesao para voltar ao trabalho depois de fazer um produto ou contactar a dona
    * 
    * @param idArtesao id do artesao que executa a operacao
    * 
    */
    private void backToWork(int idArtesao) {
                 ClientCom con = new ClientCom(serverHostNameOficina,serverPortNumbOficina);
        
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.backToWork, idArtesao);   // A dona foi alertada
       con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK) {
            GenericIO.writelnString("Thread artesao" + this.idArtesao + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close ();
    }

    /**
    *  funcao usada pelo artesao para recolher a PM que vai usar para fazer um produto
    * 
    * @param idArtesao id do artesao que executa a operacao
    * 
    */
    private void collectMaterials(int idArtesao) {
 
        ClientCom con = new ClientCom(serverHostNameOficina,serverPortNumbOficina);
        
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.collectMaterials, idArtesao);   // A dona foi alertada
       con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK) {
            GenericIO.writelnString("Thread artesao" + this.idArtesao + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close ();
    
    }

    /**
    *  funcao em que o artesao se prepara para produzir um produto (altera o seu estado para PRODUCING_A_NEW_PIECE)
    * 
    * @param idArtesao id do artesao que executa a operacao
    * 
    */
    private void prepareToProduce(int idArtesao) {

            ClientCom con = new ClientCom(serverHostNameOficina,serverPortNumbOficina);
        
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.prepareToProduce, idArtesao);   // A dona foi alertada
       con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK) {
            GenericIO.writelnString("Thread artesao" + this.idArtesao + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close ();
    
    
    }

    
    /**
    *  funcao em que o artesao leva o produto que acabou de produzir para o armazem da oficina
    * 
    * @param idArtesao id do artesao que executa a operacao
    * 
    * @return 0 se o armazem estiver cheio
     *         2 se a quantidade de produtos no armazem da oficina exceder o nivel de alarme
     *         1 para outros casos
     *         -1 para casos de erro
    * 
    */
    private int goToStore(int idArtesao) {
                  ClientCom con = new ClientCom(serverHostNameOficina,serverPortNumbOficina);
        
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        
        outMessage = new Message(Message.goToStore, idArtesao);   // o cliente compra produtos
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if ((inMessage.getType() != Message.maxStockProdutos) && (inMessage.getType() != Message.alarmeProdutosOficina)&&(inMessage.getType() != Message.ACK) ) {
            GenericIO.writelnString("Thread artesao" + this.idArtesao + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close ();
        
         if (inMessage.getType() == Message.maxStockProdutos)
             return 0;
         else
               if (inMessage.getType() == Message.alarmeProdutosOficina)
             return 2;
         else
               if (inMessage.getType() == Message.ACK)
                  return 1;
         else
                  return -1;
    }

    
    /**
    *  funcao em que o artesao contacta a dona para alertar o excesso de produtos na Oficina
    * 
    * @param idArtesao id do artesao que executa a operacao
    * 
    */
    private void batchReadyForTransfer(int idArtesao) {
        ClientCom con = new ClientCom(serverHostNameOficina, serverPortNumbOficina);

        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.batchReadyForTransfer, idArtesao);   
       con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK) {
            GenericIO.writelnString("Thread artesao" + this.idArtesao + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close ();

    }
    
    /* interaccao artesao - repositorio geral*/
    
    /**
    *  o artesao consulta se a dona ja foi alertada para o excesso de produtos na oficina
    * 
    * @return true se a dona ja foi chamada
    *         false caso contrario
    * 
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
        outMessage = new Message(Message.isDonaChamadaProdutos, idArtesao);   // o cliente compra produtos
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if ((inMessage.getType() != Message.donaChamadaProdutos) && (inMessage.getType() != Message.donaNaoChamadaProdutos)) {
            GenericIO.writelnString("Thread artesao" + this.idArtesao + ": Tipo inválido!");
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
    *  o artesao consulta se a dona ja foi alertada para a falta de PM na oficina
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
        outMessage = new Message(Message.isDonaChamadaPM, idArtesao);   // o cliente compra produtos
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if ((inMessage.getType() != Message.donaJaFoiAlertada) && (inMessage.getType() != Message.donaNaoFoiAlertada)) {
            GenericIO.writelnString("Thread artesao" + this.idArtesao + ": Tipo inválido!");
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
     * o artesao verifica se o armazem pm ainda pode fornecer pm a dona. (comunica ao server a mensagem ISARMPM)
     *
    * @return true se ainda ha pm no armazem pm
    *         false caso contrario
     */
    private boolean isArmPM() {
        ClientCom con = new ClientCom(serverHostNameGeral, serverPortNumbGeral);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.ISARMPM);        // pede a realização do serviço
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ISARMPMR) {
            GenericIO.writelnString("Thread artesao" + this.idArtesao + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close();

        if (inMessage.isAnswer())
            return true;                                                // o cliente deve terminar
        else
            return false;                                          // o cliente nao deve morrer
    }
    
    
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
            GenericIO.writelnString("Thread artesao" + this.idArtesao + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close();

        if (inMessage.getType() == Message.END)
            return true;                                                // o cliente deve terminar
        else
            return false;                                          // o cliente nao deve morrer
    }
}
