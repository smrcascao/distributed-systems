/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Monitores;

import Interfaces.*;
import States.*;
import genclass.GenericIO;
import genclass.TextFile;
//import java.util.Random;
/**
 *
 * @author rofler
 */
public class monGeral implements intDonaGeral, intArtesaoGeral, intClienteGeral {
    
    //variaveis
   /**
   *  Estado presente dos artesaos
   *
   *    @serialField estadosArtesao
   */
    private SArtesao[] estadosArtesao;
    
    /**
   *  Estado presente dos Clientes
   *
   *    @serialField estadosCliente
   */
    private SCliente[] estadosCliente;
    
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
   *  numero total de produtos no sistema.
   *
   *    @serialField totalProdutos
   */
    private int totalProdutos;
    
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
   *  Nome do ficheiro de logging
   *
   *    @serialField fileName
   */

   private String fileName = "log.txt";
    
    
    
    public monGeral(int numArtesaos, int numClientes, int totalPM, String fileName) {
        if(numArtesaos>0)
        {
            this.nCArtesaos=numArtesaos;
            this.estadosArtesao=new SArtesao[numArtesaos];
        }
        if(numClientes>0)
        {
            this.nClientes=numClientes;
            this.estadosCliente=new SCliente[numClientes];
        }
        this.totalPM=totalPM;
        this.totalProdutos=0;
       
        this.estadosDona = SDona.OPENING_THE_SHOP;
        
        for (int i = 0; i < this.nCArtesaos; i++)
        { 
            this.estadosArtesao[i] = SArtesao.FETCHING_PRIME_MATERIALS; 
        }
        for (int i = 0; i < this.nClientes; i++)
            this.estadosCliente[i] = SCliente.CARRYING_OUT_DAILY_CHORES;
                  
        if ((fileName != null) && !("".equals (fileName))) this.fileName = fileName;
            reportInitialStatus ();
    }
   
 
    /*
    @Override
    public void livingNormalLife(int idCliente)
    {
        
    }*/
    
    /*@Override
    public boolean tryAgainLater()
    {
        Random espera = new Random();
        return espera.nextInt(100) >= 50;   
    }*/
    
    
    /**
    *
    *  Altera o estado da Dona.
    *
    *  @param estado novo estado da dona
    */
    @Override
    public void setEstadoDona(SDona estado)
    {
        this.estadosDona=estado;
    }
    
    /**
    *
    *  Altera o estado do cliente.
    *
    *  @param id id do cliente 
    *  @param estado novo estado do cliente
    */
    @Override
    public void setEstadoCliente(int id, SCliente estado)
    {
        this.estadosCliente[id]=estado;
    }
    
    /**
    *
    *  Altera o estado do artesao.
    *
    *  @param id id do artesao 
    *  @param estado novo estado do artesao
    */
    @Override
    public void setEstadoArtesao(int id, SArtesao estado)
    {
        this.estadosArtesao[id]=estado;
    }
    
    /**
    *
    *  funcao usada pela dona para depositar na loja os produtos que traz da oficina.
    *
    */
    @Override 
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
    */
    @Override 
    public void donaRecebeProdutos(int p)
    {
        this.produtosComDona=p;
    }
    
    /**
    *
    *  funcao usada para calcular se a thread dona deve terminar.
    *
    */
    @Override 
    public boolean endOpDona()
    {
        return (this.totalPM==0 && this.totalProdutos==0);
    }
    
    /**
    *
    *  funcao usada para calcular se a thread cliente deve terminar.
    *
    */
    @Override 
    public boolean endOpCliente(int idCliente)
    {
        return (this.totalPM==0 && this.totalProdutos==0);
    }
    
    /**
    *
    *  funcao usada para calcular se a thread artesao deve terminar.
    *
    */
    @Override
    public boolean enOpArtesao()
    {
        return(this.totalPM==0);
    }

    /**
    *
    *  funcao usada pela dona para saber se foi chamada para fornecer mais PM a oficina.
    *
    */
    @Override
    public boolean isDonaChamadaPM() {
        return donaChamadaPM;
    }

    /**
    *
    *  funcao usada pela dona para saber se foi chamada para recolher produtos da oficina.
    *
    */
    @Override
    public boolean isDonaChamadaProdutos() {
        return donaChamadaProdutos;
    }

    /**
    *
    *  funcao usada pelos artesaos para chamar a dona quando ha falta de PM.
    *
    */    
    @Override
    public void setDonaChamadaPM(boolean donaChamadaPM) {
        this.donaChamadaPM = donaChamadaPM;
    }

    /**
    *
    *  funcao usada pelos artesaos para chamar a dona quando a quantidade de produtos na oficina passa o nivel de alarme.
    *
    */  
    @Override
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
      log.writelnString ("                Problema Obrigatorio 1");
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
          case OPENING_THE_SHOP: lineStatus += " OTS ";
                         break;
          case WAITING_FOR_NEXT_TASK: lineStatus += " WFNT ";
                         break;
          case ATTENDIND_A_CUSTOMER: lineStatus += " AAC ";
                         break;
          case CLOSING_THE_SHOP: lineStatus += " CTS ";
                         break;
          case BUYING_PRIME_MATERIALS: lineStatus += " BPM ";
                         break;
          case DELIVERING_PRIME_MATERIALS: lineStatus += " DPM ";
                         break;
          case COLLECTING_A_BATCH_OF_PRODUCTS: lineStatus += " CABOP ";
                         break;
          
      }
      
      for (int i = 0; i < this.nCArtesaos; i++)
        switch (this.estadosArtesao[i])
        { case FETCHING_PRIME_MATERIALS: lineStatus += " FPM ";
                         break;
          case PRODUCING_A_NEW_PIECE:  lineStatus += " PANP ";
                         break;
          case STORING_IT_FOR_TRANSFER:  lineStatus += " SIFT ";
                         break;
          case CONTACTING_THE_ENTREPRENEUR:  lineStatus += " CTE ";
                         break;
        }
      for (int i = 0; i < nClientes; i++)
        switch (this.estadosCliente[i])
        { case CARRYING_OUT_DAILY_CHORES: lineStatus += " CODC ";
                         break;
          case CHECKING_DOOR_OPEN: lineStatus += " CDO ";
                         break;
          case APPRAISING_OFFER_IN_DISPLAY: lineStatus += " AOID ";
                         break;
          case BUYING_SOME_GOODS:  lineStatus += " BSG ";
                         break;
        }
      log.writelnString (lineStatus);
      if (!log.close ())
         { GenericIO.writelnString ("A operação de fecho do ficheiro " + fileName + " falhou!");
           System.exit (1);
         }
   }
    
    
}
