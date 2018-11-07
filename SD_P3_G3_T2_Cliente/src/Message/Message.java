package Message;

import States.SArtesao;
import States.SCliente;
import States.SDona;
import java.io.*;

/**
 *   Este tipo de dados define as mensagens que são trocadas entre os clientes e o servidor numa solução do Problema
 *   Obrigatorio 2 que implementa o modelo cliente-servidor de tipo 2 (replicação do servidor).
 *   A comunicação propriamente dita baseia-se na troca de objectos de tipo Message num canal TCP.
 */

public class Message implements Serializable
{
    /**
   *  Chave de serialização
   *  @serial serialVersionUID
   */

   private static final long serialVersionUID = 1000L;

  /* Tipos das mensagens */

   /**
   *  Encerra o servidor (operação pedida pelo cliente)
   *  @serial SHUT
   */
   public static final int SHUT  = 999;
   
  /**
   *  Inicialização do ficheiro de logging (operação pedida pelo cliente)
   *  @serial SETNFIC
   */
   public static final int SETNFIC  = 1;
   
   /**
   *  Ficheiro de logging foi inicializado (resposta enviada pelo servidor)
   *  @serial NFICDONE
   */
   public static final int NFICDONE = 2;
   
   /**
   *  Operação realizada com sucesso (resposta enviada pelo servidor)
   *    @serial ACK
   */
   public static final int ACK      =  7;
   
   /**
   *  Continuação do ciclo de vida do cliente (resposta enviada pelo servidor)
   *    @serial CONT
   */
   public static final int CONT     =  9;

  /**
   *  Terminação do ciclo de vida do cliente (resposta enviada pelo servidor)
   *    @serial END
   */
   public static final int END      = 10;
   
   /*interaccao cliente - loja*/
   
   /**
   *  entrar na loja (operação pedida pelo cliente)
   *    @serial REQENTS
   */
   public static final int REQENTS  =  3;

  /**
   *  ver se a porta da loja esta aberta (pergunta enviada pelo cliente)
   *    @serial ISDOORO
   */
   public static final int ISDOORO =  4;
   
   /**
   *  porta fechada (resposta enviada pelo servidor)
   *    @serial SHOPCL
   */
   public static final int SHOPCL   =  5;
   
   /**
   *  porta aberta (resposta enviada pelo servidor)
   *    @serial SHOPOP
   */
   public static final int SHOPOP   =  11;
   
   /**
   *  O thread cliente deve terminar o seu ciclo de vida? (pergunta enviada pelo cliente)
   *    @serial ENDOPCL
   */
   public static final int ENDOPCL    =  6;
   
   /**
   *  Mandar o cliente dormir (operação pedida pelo cliente)
   *    @serial GOTOSLP
   */
   public static final int GOTOSLP  =  8;

  
   
   /**
   *  O cliente decide visitar a loja (operação pedida pelo cliente)
   *    @serial GOSHOP
   */
   public static final int GOSHOP      = 12;
   
   /**
   *  o cliente decide se vai ou nao comprar produtos (pergunta enviada pelo cliente)
   *    @serial PERUSE
   */
   public static final int PERUSE =  13;
   
   /**
   *  O cliente quer comprar produtos (resposta enviada pelo servidor)
   *    @serial WILLBUY
   */
   public static final int WILLBUY      = 14;
   
   /**
   *  O cliente nao quer comprar produtos (resposta enviada pelo servidor)
   *    @serial WONTBUY
   */
   public static final int WONTBUY      = 15;
   
   /**
   *  O cliente compra produtos (operação pedida pelo cliente)
   *    @serial IWANT
   */
   public static final int IWANT      = 16;
   
   /**
   *  O cliente sai da loja (operação pedida pelo cliente)
   *    @serial REQEXTS
   */
   public static final int REQEXTS      = 17;
   
   
   /**
   *  O cliente desiste de tentar entrar na loja (operação pedida pelo cliente)
   *    @serial TRYLAT
   */
   public static final int TRYLAT      = 18;
   

   
   /*interaccao dona loja*/
   /**
   *  a dona abre a loja e prepara se para trabalhar (operação pedida pelo cliente)
   *    @serial PREPTW
   */
   public static final int PREPTW      = 52;
   
   
   /**
   *  A dona verifica se ha tarefas que necessita a sua accao (pergunta feita pelo cliente)
   *    @serial APRSIT
   */
   public static final int APRSIT      = 53;
   
   /**
   *  A dona atende um cliente (pergunta feita pelo cliente)
   *    @serial ADDCUST
   */
   public static final int ADDCUST      = 55;
   
   
   /**
   *  A dona despede se um cliente (operacao pedida pelo cliente)
   *    @serial SAYGBCUST
   */
   public static final int SAYGBCUST      = 56;
   
   
   /**
   *  a dona fecha a loja para sair (operação pedida pelo cliente)
   *    @serial PREPTL
   */
   public static final int PREPTL      = 57;
   
   /**
   *  a dona fecha a porta da loja (operação pedida pelo cliente)
   *    @serial CLOSSHDOOR
   */
   public static final int CLOSSHDOOR      = 58;
   
   /**
   *  a dona pergunta ao server se ainda ha cliebntes na loja (pergunta feita pelo cliente)
   *    @serial CUSTINSH
   */
   public static final int CUSTINSH      = 59;
   
   /**
   *  a dona volta a loja (operacao pedida pelo cliente)
   *    @serial RETSHP
   */
   public static final int RETSHP      = 61;
   
   /**
   *  resposta do server a pergunta APRSIT do cliente(dona) 
   *    @serial appraiseReply
   */
   public static final int appraiseReply      = 54;
   
   /**
   *  resposta do server a pergunta CUSTINSH do cliente(dona) 
   *    @serial CUSTINSHR
   */
   public static final int CUSTINSHR      = 60;
   
   
   /* interaccao dona - oficina*/
   /**
   *  A dona vai a oficina buscar produtos (operação pedida pelo cliente)
   *    @serial GTWKSHP
   */
   public static final int GTWKSHP      = 63;
   
    /**
   *  A dona vai a oficina levar pm (operação pedida pelo cliente)
   *    @serial REPSTCK
   */
   public static final int REPSTCK      = 64;
   
   
   
   
   /* interaccao dona - armazemPM*/
   /**
   *  a dona visita o armazem PM (operação pedida pelo cliente)
   *    @serial DONAVPM
   */
   public static final int DONAVPM      = 50;
   
   
   /**
   *  a dona volta do armazem PM (resposta dada pelo server)
   *    @serial DONVOPM
   */
   public static final int DONVOPM      = 51;
   
   
   /* interaccao armazem PM - geral*/
   /**
   *  Altera o estado do armazem PM no epositorio geral (operação pedida pelo cliente)
   *    @serial SETARMPM
   */
   public static final int SETARMPM      = 65;
   
   
   /*interaccao loja - geral*/
   /**
   *  Incrementa o numero de clientes na loja (operação pedida pelo cliente)
   *    @serial CLIIN
   */
   public static final int CLIIN      = 70;
   
   /**
   *  Decrementa o numero de clientes na loja (operação pedida pelo cliente)
   *    @serial CLIOUT
   */
   public static final int CLIOUT      = 71;
   
   /**
   *  o cliente compra produtos (operação pedida pelo cliente)
   *    @serial CLIBUY
   */
   public static final int CLIBUY      = 72;
   
   /**
   *  altera o numero de produtos na loja (no repositorio geral) (operação pedida pelo cliente)
   *    @serial PRODINST
   */
   public static final int PRODINST      = 73;
   
   /**
   *  altera o estado da porta da loja (no repositorio geral) (operação pedida pelo cliente)
   *    @serial SETDOORSTAT
   */
   public static final int SETDOORSTAT      = 74;
   
   /**
   *  a dona entrega os produtos que traz do armazem de PM (pergunta feita pelo cliente)
   *    @serial GETPRODD
   */
   public static final int GETPRODD      = 75;
   
   /**
   *  resposta do server a mensagem GETPRODD (resposta enviada pelo server)
   *    @serial GETPRODDR
   */
   public static final int GETPRODDR      = 76;
   
   
   /*interaccao oficina - geral*/
   /**
   *  altera a quantidade de PM na oficina representada no repositorio geral (operação pedida pelo cliente)
   *    @serial SETPMW
   */
   public static final int SETPMW      = 80;
   
   /**
   *  altera a quantidade de PM total no repositorio geral apos o gasto de pm por um artesao (operação pedida pelo cliente)
   *    @serial PMGAST
   */
   public static final int PMGAST      = 81;
   
   /**
   *  um artesao chama a dona para repor o stock de pm (operação pedida pelo cliente)
   *    @serial SETDCPR
   */
   public static final int SETDCPR      = 82;
   
   /**
   *  um artesao chama a dona para recolher produtos da oficina (operação pedida pelo cliente)
   *    @serial SETDCPM
   */
   public static final int SETDCPM      = 87;
   
   /**
   *  um artesao acabou de coriarum produto (operação pedida pelo cliente)
   *    @serial ARTFP
   */
   public static final int ARTFP      = 83;
   
   /**
   *  altera a quantidade de Produtos na oficina representada no repositorio geral(operação pedida pelo cliente)
   *    @serial SETPRW
   */
   public static final int SETPRW      = 84;
   
   /**
   *  a dona recebe produtos (operação pedida pelo cliente)
   *    @serial DONRPR
   */
   public static final int DONRPR      = 85;
   
   /**
   *  a dona traz um novo fornecimento de pm para a oficina (operação pedida pelo cliente)
   *    @serial NEWPMF
   */
   public static final int NEWPMF      = 86;
   
   
   /*interaccao dona - geral*/
   
   /**
   *  a dona pegunta se deve terminar o seu ciclo de vida (pergunta feita pelo cliente)
   *    @serial ENDOPD
   */
   public static final int ENDOPD      = 62;
   
   
   
   /*interaccao artesao - geral*/
   
   /**
   *  o artesao pegunta se deve terminar o seu ciclo de vida (pergunta feita pelo cliente)
   *    @serial ENDOPA
   */
   public static final int ENDOPA      = 63;
   
   /**
   *   o artesao verifica se o armazem pm ainda pode fornecer pm a dona  (pergunta feita pelo cliente)
   *    @serial ISARMPM
   */
   public static final int  ISARMPM =38;
   
   
   /**
   *   resposta do server a pergunta ISARMPM  (resposta enviada pelo server)
   *    @serial ISARMPMR
   */
   public static final int  ISARMPMR =39;
   
   
   /*Alteracao dos estados*/
   
   /**
   *  Alteracao do estado do cliente (operação pedida pelo cliente)
   *    @serial STCLI
   */
   public static final int STCLI      = 99;
   
   
   /**
   *  Alteracao do estado do artesao (operação pedida pelo cliente)
   *    @serial START
   */
   public static final int START      = 98;
   
   
   /**
   *  Alteracao do estado da dona (operação pedida pelo cliente)
   *    @serial STDON
   */
   public static final int STDON      = 97;
   
   
   

   /*Artesãos inicio-19*/
   
    /**
   *  o artesao verifica se há materia prima no repositorio geral(operação pedida pelo Artesão)
   *    @serial checkForMaterials
   */
   public static final int checkForMaterials =19;
   
 
   
          /**
   *  verificar fim de vida do Artesão(operação enviada pela Oficina)
   *    @serial endOpArtesao
   */
   public static final int endOpArtesao =20;
   
   
                /**
   *  verifica que foi atingido o maximo de stock produtos na oficina(operação enviada para a oficina)
   *    @serial maxStockProdutos
   */
   public static final int maxStockProdutos =21;
   
   
                   /**
   *  verifica se existe materia prima na oficina para criar um produto (operação enviada para artesao)
   *    @serial PMInsuficiente
   */
   public static final int PMInsuficiente =22;
   
   /**
   *  verifica se existe materia prima na oficina para criar um produto (operação enviada para artesao)
   *    @serial PMInsuficiente
   */
   public static final int PMInfAAlarme=105;
   
   
                      /**
   *  verifica se foi atingido o Min de stock materia prima na oficina(operação enviada para artesao)
   *    @serial minPM
   */
   public static final int minPM =23;
   
                        /**
   *  verifica que existe materia prima suficiente para um produto (operação enviada para artesao)
   *    @serial PMOK
   */
   public static final int PMOK =24;
   
   
                           /**
   *  operaçao de verificação se a dona já foi chamada (operação enviada para o Geral)
   *    @serial isDonaChamadaPM
   */
   public static final int isDonaChamadaPM =25;
      
   /**
   * *  verificação que a dona já foi chamada (operação enviada para artesao)
   *    @serial donaJaFoiAlertada
   */
   public static final int donaJaFoiAlertada =26;
   
     /**
   * *  verificação que a dona nao foi alertada (operação enviada para artesao)
   *    @serial donaNaoFoiAlertada
   */
   public static final int donaNaoFoiAlertada =27;
   
   
   /**
   * *  operaçao de alerta á dona  (operação enviada para oficina)
   *    @serial donaNaoFoiAlertada
   */
   public static final int primeMaterialsNeeded =28;
   
   /**
   * *  operação backToWork  (operação enviada para oficina)
   *    @serial backToWork
   */
   public static final int backToWork =29;
   
      /**
   * *  operação collectMaterials  (operação enviada para Oficina)
   *    @serial collectMaterials
   */
   public static final int collectMaterials =30;
   
   
   /**
   * *  operação prepareToProduce  (operação enviada para Oficina)
   *    @serial prepareToProduce
   */
   public static final int prepareToProduce =31;
   
   
   /**
   * *  operação goToStore  (operação enviada para Oficina)
   *    @serial goToStore
   */
   public static final int goToStore =32;  
   
     /**
   * *  Operaçao alarmeProdutosOficina  (operação enviada para artesao)
   *    @serial alarmeProdutosOficina
   */
   public static final int alarmeProdutosOficina =33;  
   
   
        /**
   * *  operaçao batchReadyForTransfer  (operação enviada para Oficna)
   *    @serial batchReadyForTransfer
   */
   public static final int  batchReadyForTransfer =34;  
   
           /**
   * *  operaçao para verificar se a dona foi chamada ou não (operação enviada para geral)
   *    @serial isDonaChamadaProdutos
   */
   public static final int  isDonaChamadaProdutos =35;  
   
  
              /**
   * *  caso a dona já tenha sido chamada  (operação enviada para artesao)
   *    @serial donaChamadaProdutos
   */
   public static final int  donaChamadaProdutos =36;
   
   
   /**
   *   caso a dona não tenha sido chamada  (operação enviada para artesao)
   *    @serial donaNaoChamadaProdutos
   */
   public static final int  donaNaoChamadaProdutos =37;
   
   
   
   
   
   
   
   
   
   
   

   /* Campos das mensagens */

  /**
   *  Tipo da mensagem
   *  @serial msgType
   */
   private int msgType = -1;

  /**
   *  Identificação do cliente
   *  @serial clientId
   */
   private int clientId = -1;

  /**
   *  valor inteiro de retorno para algumas funcoes
   *  @serial value
   */
   private int value = -1;
   
   /**
   *  valor boolean de retorno para algumas funcoes
   *  @serial answer
   */
   private boolean answer = false;
   
   /**
   *  Numero de compras efectuadas pelo cliente
   *  @serial numCompras
   */
   private int numCompras;
   
   /**
   *  String auxiliar
   *  @serial tempS
   */
   private String tempS;
   
   /**
   *  quantidade de PM obtida pela dona
   *  @serial donaPM
   */
   private int donaPM=-1;
  
   /**
   *  estado da dona (usado como parametro de entrada do construtor)
   *  @serial estadoCliente
   */
   private SCliente estadoCliente= SCliente.CARRYING_OUT_DAILY_CHORES;
   
   /**
   *  quantidade de PM obtida pela dona
   *  @serial estadoDona
   */
   private SDona estadoDona= SDona.OPENING_THE_SHOP;

    
   
   /**
   *  quantidade de PM obtida pela dona
   *  @serial estadoArtesao
   */
   private SArtesao estadoArtesao= SArtesao.FETCHING_PRIME_MATERIALS;
   
   
  /**
   *  Nome do ficheiro de logging
   *  @serial fName
   */
   private String fName = null;
   
   
   /*Metodos*/
   
   /**
   *  Instanciação de uma mensagem (forma 1).
   *
   *    @param type tipo da mensagem
   */

   public Message (int type)
   {
      msgType = type;
   }
   
   /**
   *  Instanciação de uma mensagem (forma 2).
   *
   *    @param type tipo da mensagem
   *    @param id identificação do cliente
   *    @param op resultado da funcao check for materials do artesao
   */

   public Message (int type, int id, int op)
   {
      msgType = type;
      clientId= id;
      this.value = op;
   }
   
   
   /**
   *  Instanciação de uma mensagem (forma 3).
   *
   *    @param type tipo da mensagem
   *    @param id identificação do cliente ou para quantidade de PM que a dona traz do armazem ou resultado da funcao apraiseSit() da dona
   */

   public Message (int type, int id)
   {
      msgType = type;
      clientId= id;
   }
   
   
   /**
   *  Instanciação de uma mensagem (forma 4).
   *
   *    @param type tipo da mensagem
   *    @param ans valor boolean de retorno de algumas funcoes
   */

   public Message (int type, boolean ans)
   {
      msgType = type;
      answer= ans;
   }
   
   
   /**
   *  Instanciação de uma mensagem (forma 5).
   *
   *    @param type tipo da mensagem
   *    @param name nome do ficheiro de logging
   *    @param dPM  quantidade de PM que a dona tira do armazem PM
   */

   public Message (int type, String name, int dPM)
   {
      msgType = type;
      fName= name;
      this.donaPM=dPM;
   }
   
   /**
   *  Instanciação de uma mensagem (forma 6).
   *
   *    @param type tipo da mensagem
   *    @param name nome do ficheiro de logging
   */

   public Message (int type, String name)
   {
      msgType = type;
      this.tempS= name;
   }
   
   /**
   *  Instanciação de uma mensagem (forma 7).
   *
   *    @param type tipo da mensagem
   *    @param id id do cliente
   *    @param est estado para o qual o cliente definido por id ira ser alterado
   */

   public Message (int type, int id, SCliente est)
   {
      msgType = type;
      this.clientId=id;
      this.estadoCliente=est;
   }
   
   
   /**
   *  Instanciação de uma mensagem (forma 8).
   *
   *    @param type tipo da mensagem
   *    @param id id do artesao
   *    @param est estado para o qual o artesao definido por id ira ser alterado
   */

   public Message (int type, int id, SArtesao est)
   {
      msgType = type;
      this.clientId=id;
      this.estadoArtesao=est;
   }
   
   
   /**
   *  Instanciação de uma mensagem (forma 9).
   *
   *    @param type tipo da mensagem
   *    @param est estado para o qual a dona ira ser alterado
   */

   public Message (int type, SDona est)
   {
      msgType = type;
      this.estadoDona=est;
   }
   
   
   /**
   *  Obtenção do estado para qual o cliente deve mudar.
   *
   *    @return estado do cliente
   */
   public SCliente getEstadoCliente() {
        return estadoCliente;
    }

   
   /**
   *  Obtenção do estado para qual a dona deve mudar.
   *
   *    @return estado da dona
   */
    public SDona getEstadoDona() {
        return estadoDona;
    }

   /**
   *  Obtenção do estado para qual o artesao deve mudar.
   *
   *    @return estado do artesao
   */
    public SArtesao getEstadoArtesao() {
        return estadoArtesao;
    }
   
   
   /**
   *  Obtenção do valor do campo tipo da mensagem.
   *
   *    @return tipo da mensagem
   */

   public int getType ()
   {
      return (msgType);
   }

  /**
   *  Obtenção do valor do campo identificador do cliente.
   *
   *    @return identificação do cliente
   */

   public int getId ()
   {
      return (clientId);
   }

  /**
   *  Obtenção do valor do campo número de tentativas efectuadas pelo cliente para conseguir entrar na loja.
   *
   *    @return número de tentativas efectuadas
   */

   public int getNTry ()
   {
      return (value);
   }

  /**
   *  Obtenção do valor do campo nome do ficheiro de logging.
   *
   *    @return nome do ficheiro
   */

   public String getFName ()
   {
      return (fName);
   }

  /**
   *  Obtenção do valor do campo número de iterações do ciclo de vida dos clientes.
   *
   *    @return número de iterações do ciclo de vida dos clientes
   */

   public int getNumCompras ()
   {
      return (numCompras);
   }
   
   /**
   *  Obtenção do valor boolean generico.
   *
   *    @return valor boolean resultado de operaçoes pedidas por clientes a regioes partilhadas
   */
    public boolean isAnswer() {
        return answer;
    }
    
    /**
   *  Obtenção do valor String auxiliar.
   *
   *    @return valor String passado ao server
   */
    public String getTempS() {
        return tempS;
    }
    
    
}
