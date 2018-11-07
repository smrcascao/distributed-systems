/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Monitores;

import States.*;
import genclass.GenericIO;
import genclass.TextFile;

/**
 *
 * @author rofler
 */
public class monGeral{
    
    //variaveis
   /**
   *  Estado presente dos artesaos
   *
   *    @serialField estadosArtesao
   */
    private SArtesao[] estadosArtesao;
    
      /**
   *  produtos Produzidos pelos artesaos
   *
   *    @serialField produtosArtesao
   */
    private int[] produtosArtesao;
    
    /**
   *  Estado presente dos Clientes
   *
   *    @serialField estadosCliente
   */
    private SCliente[] estadosCliente;
    
     /**
   *  Compras dos clientes
   *
   *    @serialField comprasCliente
   */
    private int[] comprasCliente;
    
   /**
   *  Estado presente da Dona
   *
   *    @serialField estadosDona
   */
    private SDona estadosDona;
    
    /**
   *  Produtos que a dona esta a transportar da oficina para a loja
   *
   *    @serialField produtosComDona
   */
    private int produtosComDona;
    
   /**
   *  PM total no sistema.
   *
   *    @serialField totalPM
   */
    private int totalPM; 
    
    /**
   *  numero total de produtos na oficina e na loja.
   *
   *    @serialField totalProdutos
   */
    private int totalProdutos;
    
     /**
   *  numero total de produtos produzidos na oficina.
   *
   *    @serialField totalProdutosOficina
   */
    private int totalProdutosOficina;
    
      /**
   *  quantidade total de PM fornecida a oficina.
   *
   *    @serialField totalPMOficina
   */
    private int totalPMOficina;
    
   /**
   *  dona chamada por falta de PM na oficina.
   *
   *    @serialField donaChamadaPM
   */
    private boolean donaChamadaPM; 
    
    /**
   *  dona chamada por excesso de produtos na oficina.
   *
   *    @serialField donaChamadaProdutos
   */
    private boolean donaChamadaProdutos;
    
   /**
   *  Número de artesaos que frequentam a Oficina
   *
   *    @serialField nArtesaos
   */

   private int nCArtesaos = 0;
    
   /**
   *  Número de clientes que frequentam a Loja
   *
   *    @serialField nClientes
   */

   private int nClientes = 0;
   
   /**
   *  Número de clientes que frequentam a Loja no instante actual
   *
   *    @serialField nClientesNaLoja
   */

   private int nClientesNaLoja = 0;
   
   /**
   *  Número de produtos na loja
   *
   *    @serialField nProdutosNaLoja
   */

   private int nProdutosNaLoja = 0;
   
   /**
   *  Número de produtos na Oficina
   *
   *    @serialField nProdutosNaOficina
   */

   private int nProdutosNaOficina = 0;
   
   /**
   *  quantidade de PM na oficina
   *
   *    @serialField nPMNaOficina
   */

   private int nPMNaOficina = 0;
   
   /**
   *  fornecimentos de PM a oficina
   *
   *    @serialField nFornOficina
   */

   private int nFornOficina = 0;
   
   /**
   *  Nome do ficheiro de logging
   *
   *    @serialField fileName
   */
   private String fileName = "log.txt";
    
   /**
   *  Estado da porta da loja (open || opdc || clos)
   *
   *    @serialField shopDoorStat
   */
   private String shopDoorStat = "";
   
   /**
    * 
    * Estado do armazem de PM (true se houver PM, false se estiver vazio)
    * 
    * @serialField armPM
    *  
    */
    private boolean armPM;
    
    /**
   *  PM para construir um produto
   *
   *    @serialField produtoPM
   */
    private int produtoPM; 
   
   /**
     *
     *  Construtor
     *
     * @param numArtesaos Número de artesaos que frequentam a Oficina
     * @param numClientes Número de clientes que frequentam a Loja
     * @param totalPM total de PM no sistema no instante inicial
     * @param fileName ficheiro de logging
     *
     */
    public monGeral(int numArtesaos, int numClientes, int totalPM, int produtoPM, String fileName) {
        if(numArtesaos>0)
        {
            this.nCArtesaos=numArtesaos;
            this.estadosArtesao=new SArtesao[numArtesaos];
            this.produtosArtesao=new int[numArtesaos];
        }
        if(numClientes>0)
        {
            this.nClientes=numClientes;
            this.estadosCliente=new SCliente[numClientes];
            this.comprasCliente = new int[numClientes];
        }
        this.totalPM=totalPM;
        this.totalProdutos=0;
        this.shopDoorStat="clos";
        this.nClientesNaLoja=0;
        this.nProdutosNaLoja=0;
        this.nProdutosNaOficina=0;
        this.nPMNaOficina=0;
        this.nFornOficina=0;
        this.totalPMOficina=0;
        this.armPM=true;
        this.produtoPM=produtoPM;
       
        this.estadosDona = SDona.OPENING_THE_SHOP;
        
        for (int i = 0; i < this.nCArtesaos; i++)
        { 
            this.estadosArtesao[i] = SArtesao.FETCHING_PRIME_MATERIALS; 
            this.produtosArtesao[i]=0;
        }
        for (int i = 0; i < this.nClientes; i++)
        {
            this.estadosCliente[i] = SCliente.CARRYING_OUT_DAILY_CHORES;
            this.comprasCliente[i]=0;
        }
        
        if ((fileName != null) && !("".equals (fileName))) this.fileName = fileName;
            reportInitialStatus ();
    }

    
    /**
     *
     *  altera o estado do armazemPM no repositorio geral
     * 
     * @param armPM novo estado do armazemPM
     *
     */    
    public void setArmPM(boolean armPM) {
        this.armPM = armPM;
    }

    
    /**
    *
    *  devolve o estado do armazem PM
    *
    * @return true - tem PM
    *         false - esta vazio
    * 
    */
    public boolean isArmPM() {
        return armPM;
    }
       
    
    /**
     *
     *  reduz o nivel total de pm no sistema
     * 
     * @param quantPM quantidade de pm usada
     *
     */
    public void pMGasta(int quantPM)
    {
        this.totalPM-=quantPM;
        
        System.err.println(this.totalPM);       //debug
        
    }
    
    /**
     *
     *  Incrementa o numero de fornecimentos de pm a oficina
     *
     * @param quantPM quantidade de PM fornecida
     */
    public void novoFornecimentoPM(int quantPM)
    {
        this.nFornOficina++;
        this.totalPMOficina+=quantPM;
        
    }
    
    /**
     *
     *  Altera estado da porta da loja
     *
     *  @param shopDoorStat novo estado da porta da loja
     */
    public void setShopDoorStat(String shopDoorStat)
    {
        this.shopDoorStat = shopDoorStat;
        
    }

    /**
     *
     *  Altera o numero de produtos na loja dentro do repositorio geral
     *
     *  @param nProdutosNaLoja numero de produtos na loja
     */
    public void setnProdutosNaLoja(int nProdutosNaLoja)
    {
        this.nProdutosNaLoja = nProdutosNaLoja;
        
    }
    
    /**
     *
     *  Altera o numero de produtos na Oficina dentro do repositorio geral
     *
     *  @param nProdutosNaOficina numero de produtos na Oficina
     */
    public void setnProdutosNaOficina(int nProdutosNaOficina)
    {
        this.nProdutosNaOficina = nProdutosNaOficina;
        
    }
    
    /**
     *
     *  Altera a quantidade de PM na Oficina dentro do repositorio geral
     *
     *  @param nPMNaOficina quantidade de pm na oficina
     */
    public void setnPMNaOficina(int nPMNaOficina)
    {
        this.nPMNaOficina = nPMNaOficina;
        
    }
    
    /**
     *
     *  Acrescenta produtos a quantidade previamente na posse do cliente.
     *
     *  @param idCliente id do cliente
     *  @param numProdutos numero de produtos que o cliente comprou
     * 
     */
    public void compraCliente(int idCliente, int numProdutos) {
        this.comprasCliente[idCliente]+=numProdutos;
        this.totalProdutos-=numProdutos;
        
        
        //System.out.println(totalProdutos);
        
        
    }
    
    /**
     *
     *  Acrescenta um produto a quantidade de produtos feitos pelo artesao.
     *
     *  @param idArtesao id do artesao que fez o produto
     */
    public void artesaoFezProduto(int idArtesao) {
        this.produtosArtesao[idArtesao]++;
        this.totalProdutos++;
        this.totalProdutosOficina++;
    }
    
    /**
    *
    *  Altera o estado da Dona.
    *
    *  @param estado novo estado da dona
    */
    public void setEstadoDona(SDona estado)
    {
        this.estadosDona=estado;
        reportStatus ();
    }
    
    /**
    *
    *  Altera o estado do cliente.
    *
    *  @param id id do cliente 
    *  @param estado novo estado do cliente
    */
    public void setEstadoCliente(int id, SCliente estado)
    {
        this.estadosCliente[id]=estado;
        reportStatus ();
    }
    
    /**
    *
    *  Cliente entrou na loja.
    *
    */
    public void clienteEntra()
    {
        this.nClientesNaLoja++;
    }
    
    /**
    *
    *  Cliente saiu da loja.
    *
    */
    public void clienteSai()
    {
        this.nClientesNaLoja--;
    }
    
    /**
    *
    *  Altera o estado do artesao.
    *
    *  @param id id do artesao 
    *  @param estado novo estado do artesao
    */
    public void setEstadoArtesao(int id, SArtesao estado)
    {
        this.estadosArtesao[id]=estado;
        reportStatus ();
    }
    
    /**
    *
    *  funcao usada pela dona para depositar na loja os produtos que traz da oficina.
    *
    * @return numero de produtos depositados na loja
    * 
    */
    public int getProdutosComDona()
    {
        int aux=this.produtosComDona;
        this.produtosComDona=0;
        return aux;
    }
    
    
    /**
    *
    *  funcao usada pela receber produtos.
    *
    *  @param p quantidade de produtos que a dona recebe
    */
    public void donaRecebeProdutos(int p)
    {
        this.produtosComDona=p;
    }
    
    /**
    *
    *  funcao usada para calcular se a thread dona deve terminar.
    *
    * @return   true, se a thread deve acabar
   *            false, em caso contrário
    */
    public boolean endOpDona()
    {
        return (this.totalPM<this.produtoPM && this.totalProdutos==0);
    }
    
    /**
    *
    *  funcao usada para calcular se a thread cliente deve terminar.
    * 
    * @return   true, se a thread deve acabar
   *            false, em caso contrário
    */
    public synchronized boolean endOpCliente()
    {
        if(this.totalPM<this.produtoPM && this.totalProdutos==0)
        {
            notifyAll();
            return true;
        }
        return false;
        
    }
    
    
    /**
    *
    *  funcao usada para calcular se a thread artesao deve terminar.
    *
    * @return   true, se a thread deve acabar
   *            false, em caso contrário
    */
    public boolean endOpArtesao()
    {
        return(this.totalPM<this.produtoPM);
    }

    
    /**
    *
    *  funcao usada pela dona para saber se foi chamada para fornecer mais PM a oficina.
    *
    * @return   true, se a dona foi chamada
   *            false, em caso contrário
    */
    public boolean isDonaChamadaPM() {
        return donaChamadaPM;
    }

    
    /**
    *
    *  funcao usada pela dona para saber se foi chamada para recolher produtos da oficina.
    *
    * @return   true, se a dona foi chamada
    *           false, em caso contrário
    */
    public boolean isDonaChamadaProdutos() {
        return donaChamadaProdutos;
    }

    
    /**
    *
    *  funcao usada pelos artesaos para chamar a dona quando ha falta de PM.
    *
    * @param donaChamadaPM variavel booleana (true - dona chamada; false - cancelar chamamento da dona)
    * 
    */   
    public void setDonaChamadaPM(boolean donaChamadaPM) {
        this.donaChamadaPM = donaChamadaPM;
    }

    
    /**
    *
    *  funcao usada pelos artesaos para chamar a dona quando a quantidade de produtos na oficina passa o nivel de alarme.
    *
    * @param donaChamadaProdutos variavel booleana (true - dona chamada; false - cancelar chamamento da dona)
    * 
    */  
    public void setDonaChamadaProdutos(boolean donaChamadaProdutos) {
        this.donaChamadaProdutos = donaChamadaProdutos;
    }
    
    
    /**
   *  Escrever o estado inicial (operação interna).
   *  <p>A dona vai abrir a loja, os artesaos iniciam o trabalho e os clientes a realizar as tarefas do dia a dia.
   */
   private void reportInitialStatus ()
   {
      TextFile log = new TextFile ();                      // instanciação de uma variável de tipo ficheiro de texto

      if (!log.openForWriting (".", fileName))
         { GenericIO.writelnString ("A operação de criação do ficheiro " + fileName + " falhou!");
           System.exit (1);
         }
      log.writelnString ("                Aveiro Handicraft SARL - Description of the internal state\n\n"
             // + "\tENTREPRE\tCUST_0\t\tCRAFT_0\t\t\t\t\tSHOP\t\t\t\t\t\tWORKSHOP\n");
              + "\tENTREPRE\tCUST_0\t\tCUST_1\t\tCUST_2\t\tCRAFT_0\t\t\tCRAFT_1\t\t\tCRAFT_2\t\t\t\t\tSHOP\t\t\t\t\t\t\tWORKSHOP");
      //log.writelnString ("\nNúmero de iterações = " + nIter + "\n");
      if (!log.close ())
         { GenericIO.writelnString ("A operação de fecho do ficheiro " + fileName + " falhou!");
           System.exit (1);
         }
      reportStatus ();
   }
   
   
   /**
   *  Escrever o estado actual (operação interna).
   *  <p>Uma linha de texto com o estado de actividade da Dona, dos artesaos e dos clientes é escrito no ficheiro.
   */
   private void reportStatus ()
   {
      TextFile log = new TextFile ();                      // instanciação de uma variável de tipo ficheiro de texto

      String lineStatus = "";                              // linha a imprimir

      if (!log.openForAppending (".", fileName))
         { GenericIO.writelnString ("A operação de criação do ficheiro " + fileName + " falhou!");
           System.exit (1);
         }
      
      switch(this.estadosDona)
      {
          case OPENING_THE_SHOP: lineStatus += "\tOPTS\t\t";
                         break;
          case WAITING_FOR_NEXT_TASK: lineStatus += "\tWFNT\t\t";
                         break;
          case ATTENDIND_A_CUSTOMER: lineStatus += "\tATAC\t\t";
                         break;
          case CLOSING_THE_SHOP: lineStatus += "\tCLTS\t\t";
                         break;
          case BUYING_PRIME_MATERIALS: lineStatus += "\tBUPM\t\t";
                         break;
          case DELIVERING_PRIME_MATERIALS: lineStatus += "\tDEPM\t\t";
                         break;
          case COLLECTING_A_BATCH_OF_PRODUCTS: lineStatus += "\tCABOP\t\t";
                         break;
          
      }
      for (int i = 0; i < nClientes; i++)
        switch (this.estadosCliente[i])
        { case CARRYING_OUT_DAILY_CHORES: lineStatus += "CODC\t" + this.comprasCliente[i] + "\t";
                         break;
          case CHECKING_DOOR_OPEN: lineStatus += "CDOP\t" + this.comprasCliente[i] + "\t";
                         break;
          case APPRAISING_OFFER_IN_DISPLAY: lineStatus += "AOID\t" + this.comprasCliente[i] + "\t";
                         break;
          case BUYING_SOME_GOODS:  lineStatus += "BUSG\t" + this.comprasCliente[i] + "\t";
                         break;
        }
      
      for (int i = 0; i < this.nCArtesaos; i++)
        switch (this.estadosArtesao[i])
        { case FETCHING_PRIME_MATERIALS: lineStatus += "FEPM\t" + this.produtosArtesao[i] + "\t\t";
                         break;
          case PRODUCING_A_NEW_PIECE:  lineStatus += "PANP\t" + this.produtosArtesao[i] + "\t\t";
                         break;
          case STORING_IT_FOR_TRANSFER:  lineStatus += "SIFT\t" + this.produtosArtesao[i] + "\t\t";
                         break;
          case CONTACTING_THE_ENTREPRENEUR:  lineStatus += "CTEP\t" + this.produtosArtesao[i] + "\t\t";
                         break;
        }
      
      lineStatus += this.shopDoorStat + "\t";
      lineStatus += this.nClientesNaLoja + "\t";      
      lineStatus += this.nProdutosNaLoja + "\t";       
      lineStatus += this.donaChamadaProdutos + "\t";
      lineStatus += this.donaChamadaPM + "\t\t";   
      
      lineStatus += this.nPMNaOficina+ "\t";     
      lineStatus += this.nProdutosNaOficina + "\t";  
      lineStatus += this.nFornOficina + "\t";   
      lineStatus += this.totalPMOficina + "\t";  
      lineStatus += this.totalProdutosOficina + "\t\t";
      lineStatus += this.totalPM;
      
      
      log.writelnString (lineStatus);
      if (!log.close ())
         { GenericIO.writelnString ("A operação de fecho do ficheiro " + fileName + " falhou!");
           System.exit (1);
         }
   }
    
    
}
