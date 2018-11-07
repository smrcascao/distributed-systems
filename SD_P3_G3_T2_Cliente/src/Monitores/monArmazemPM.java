package Monitores;

import Com.ClientCom;
import Message.Message;
import States.SDona;
import genclass.GenericIO;
import static java.lang.Thread.sleep;
import java.util.Random;
/**
 *  Estrutura de dados Armazem de PM
 *  Armazem ao qual a dona vai buscar PM para levar para a Oficina
 * 
 * Entidades Acitvas que interagem com a regiao partilhada:
 *  Dona
 * 
 * 
 * 
 */
public class monArmazemPM{
    
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
   *  PM disponivel para venda
   *
   *    @serial stockPM
   */
    private int stockPM;
    

    /**
   *  quantidade total de PM vendida
   *
   *    @serial totalVendaPM
   */
    private int totalVendaPM;
    

    /**
   *  Numero de vendas
   *
   *    @serial vendasPM
   */
    private int vendasPM;
    
    //quantidade de PM por fornecimento
   /**
   *  quantidade de PM por fornecimento
   *
   *    @serial fornecimentoPM
   */
    private int fornecimentoPM;
    
    /**
   *  PM para fazer u produto
   *
   *    @serial prodPM
   */
    private int prodPM;
    
    
    /**
     *
     *  Construtor
     *
     * @param stockInicialPM PM disponivel para venda
     * @param hostNameGeral nome do sistema computacional onde está localizado o servidor (regiao partilhada - repositorio geral)
     * @param portGeral número do porto de escuta do servidor do repositorio geral
     * @param prodPM pm para produzir um produto
     *
     */
    public monArmazemPM(int stockInicialPM, String hostNameGeral, int portGeral, int prodPM)
    {

        this.fornecimentoPM=0;
        this.stockPM=stockInicialPM;
        this.totalVendaPM=0;
        this.vendasPM=0;
        this.serverHostNameGeral=hostNameGeral;
        this.serverPortNumbGeral=portGeral;
        this.prodPM=prodPM;
        
        
    }
    
    /**
     *
     * A dona visita o armazem PM para recolher a materia prima que vai leva para a Oficina.
     * 
     * @return quantidade de PM que a dona recolheu (gerada aleatoriamente).
     *
     */
    public int visitSuppliers()
    {
        Random randPercent= new Random();
        Random rand= new Random();
        this.fornecimentoPM=0;
        
        if(this.stockPM > this.prodPM*2){
            int percent = randPercent.nextInt(101);

            if(percent<80)
            {
                while(this.fornecimentoPM==0)
                    this.fornecimentoPM = rand.nextInt((int) ((int)(this.stockPM)*0.3+1));
                this.stockPM-=this.fornecimentoPM;

            }
            else
            {
                while(this.fornecimentoPM==0)
                    this.fornecimentoPM = rand.nextInt((this.stockPM+1));
                this.stockPM-=this.fornecimentoPM;
            }
        }
        else
        {
            this.fornecimentoPM=this.stockPM;
            this.stockPM=0;
        }
        
        setEstadoDona(SDona.BUYING_PRIME_MATERIALS);
        
        System.out.println("A dona visitou o armazem. \n Quantidade de PM recolhida: "+ this.fornecimentoPM + "\n Stock restante: "+this.stockPM+"\n");
        if(this.stockPM==0)
        {
            System.out.println("armazem pm esta vazio.");
            setArmPM(false);
        }
        
        return fornecimentoPM;
    }
    
    
    /*interaccao com o repositorio geral*/
    /**
     *
     * Altera o estado do armazem PM no repositorio geral.
     * (comunica ao server a mensagem SETARMPM)
     *
     * @param temp situacao do armazem. True - ainda ha PM no armazem.
     *                                  False - caso contrario.
     * 
     */
    private void setArmPM(boolean temp) {

        ClientCom con = new ClientCom(serverHostNameGeral, serverPortNumbGeral);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.SETARMPM, temp);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK) {
            GenericIO.writelnString("Armazem PM: Tipo inválido!");
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
   *  Encerra a regiao partilhada.
   *  <p>o Server recebeuma mensagem da dona e encerra.
   */
   public void shutdown ()
   {
       System.out.println("Mensagem recebida.\nO server vai encerrar");
       System.exit (0);
   }
}
