/*
 * Oficina

 * Repositorio oficina
 * As entidades que interage com este repositorio de dados sao:
 *  artesaos
 *  dona

 */
package Monitores;

import ServerInterface.GeralInterface;
import ServerInterface.OficinaInterface;
import States.SArtesao;
import States.SDona;
import java.rmi.RemoteException;

/**
 *
 * @author rofler
*
 */
public class monOficina implements OficinaInterface{
    //variaveis
    //PM
    
    /**
   *  quantidade de PM em stock 
   *
   *    @serialField stockPM
   */
    private int stockPM;
    

   /**
   *  quantidade total de PM encomendada 
   *
   *    @serialField totalPM
   */
    private int totalPM;
            
    
   /**
   *  num total de fornecimentos de PM
   *
   *    @serialField fornecimentosPM
   */
    private int fornecimentosPM;
    

    /**
   *  PM para construir um produto
   *
   *    @serialField produtoPM
   */
    private int produtoPM;        
    

    /**
   *  limite minimo de PM
   *
   *    @serialField limiteMinPM
   */
    private int limiteMinPM;
            
    
    /**
   *  PM recebida por fornecimento
   *
   *    @serialField PMPorFornecimento
   */
    private int PMPorFornecimento;
    
    
    //Produto
    /**
   * Produtos em stock
   *
   *    @serialField stockProdutos
   */
    private int stockProdutos;
    
 
    /**
   *  quantidade total de produtos produzidos
   *
   *    @serialField totalProdutos
   */
    private int totalProdutos;
    

    /**
   *  Limite de Alarme da quantidade de produtos
   *
   *    @serialField alarmeProdutos
   */
    private int alarmeProdutos;
    

    /**
   *  Limite maximo de produtos
   *
   *    @serialField limiteMaxProdutos
   */
    private int limiteMaxProdutos;
    

    /**
   *  quantidade de produtos que a dona leva para a loja
   *
   *    @serialField produtosDona
   */
    private int produtosDona;
    
 
    /**
   *  dona chamada (por falta de PM)
   *
   *    @serialField donaChamadaPM
   */
    private boolean donaChamadaPM;
    
    
    /**
   *  dona chamada (por existirem demasiados produtos em stock)
   *
   *    @serialField donaChamadaProdutos
   */
    private boolean donaChamadaProdutos;
    
    /**
   *  interface com o rep geral
   *
   *    @serialField geralInter
   */
    private GeralInterface geralInter;
    
    
    
    
    /**
     *
     *  Construtor
     *
     * @param alarmeProd Limite de Alarme da quantidade de produtos
     * @param limiteProd Limite maximo de produtos
     * @param alarmePM limite minimo de PM
     * @param prodPM PM para construir um produto
     * @param fornPM PM recebida por fornecimento
     * @param prodDona quantidade de produtos que a dona leva para a loja
     *
     */
    public monOficina(int alarmeProd, int limiteProd, int alarmePM, int prodPM, int fornPM, int prodDona, GeralInterface geralInter)
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
        this.PMPorFornecimento=fornPM;
        this.produtosDona=prodDona;
        this.geralInter=geralInter;
                
    }
    
    
    /* funcoes dos artesaos*/
    
    /**
     *
     *  funcao usada pelo artesao para ver se ha PM para poder trabalhar
     *
     * @param id id do artesao
     * @param geral interface com o rpositorio geral
     * 
     * @return 0 se a quantidade de PM nao for suficiente para fazer um produto
     *         2 se for necessario chamar a dona para repor o stock de PM
     *         3 se nao houver espaco para mais produtos na oficina
     *         1 para outros casos
     * 
     */
    public synchronized int checkForMaterials(int id) throws RemoteException
    {
        if (geralInter.endOpArtesao())
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
     * @param artesao usado para alterar o repositorio geral
     * 
     */
    public synchronized void collectMaterials(int id) throws RemoteException
    {
        //stockPM-=produtoPM;
        geralInter.setnPMNaOficina(this.stockPM);
        geralInter.pMGasta(produtoPM);
        
    }
    
    
    /**
     *
     *  funcao usada pelo artesao para chamar a dona por falta de PM
     *
     * @param id id do artesao
     * @param artesao usado para alterar o repositorio geral
     * 
     */
    public synchronized void primeMaterialsNeeded(int id) throws RemoteException
    {
        if(this.donaChamadaPM==false){
            geralInter.setEstadoArtesao(id, SArtesao.CONTACTING_THE_ENTREPRENEUR);
            this.donaChamadaPM=true;
            geralInter.setDonaChamadaPM(true);
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
     * @param artesao usado para alterar o repositorio geral
     * 
     */
    public synchronized void batchReadyForTransfer(int id) throws RemoteException
    {
        if(this.donaChamadaProdutos==false)
        {
            geralInter.setEstadoArtesao(id, SArtesao.CONTACTING_THE_ENTREPRENEUR);
            this.donaChamadaProdutos=true;
            geralInter.setDonaChamadaProdutos(true);
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
     * @param artesao usado para alterar o estado do artesao no repositorio geral
     * 
     */
    public synchronized void backToWork(int id) throws RemoteException
    {
        geralInter.setEstadoArtesao(id, SArtesao.FETCHING_PRIME_MATERIALS);
    }
    
    
    
    /**
     *
     *  funcao usada pelo artesao para colocar o produto que acabou de produzir no armazem da oficina
     *
     * @param id id do artesao
     * @param artesao usado para alterar o repositorio geral
     * 
     * @return 0 se o armazem estiver cheio
     *         2 se a quantidade de produtos no armazem da oficina exceder o nivel de alarme
     *         1 para outros casos
     * 
     */
    public synchronized int goToStore(int id) throws RemoteException
    {
        geralInter.setEstadoArtesao(id, SArtesao.STORING_IT_FOR_TRANSFER);
        this.stockProdutos++;
        this.totalProdutos++;
        geralInter.artesaoFezProduto(id);
        geralInter.setnProdutosNaOficina(this.stockProdutos);
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
     * @param artesao usado para alterar o estado do artesao no repositorio geral
     * 
     */
    public synchronized void prepareToProduce(int id) throws RemoteException
    {
        geralInter.setEstadoArtesao(id, SArtesao.PRODUCING_A_NEW_PIECE);
    }
    
    
    
    
    
    /*funcoes da dona*/
    
    /**
     *
     *  funcao usada pela dona para receber produtos do armazem da oficina
     *
     * @param dona usado para alterar o repositorio geral
     * 
     */
    public synchronized void goToWorkShop() throws RemoteException
    {
        if(!geralInter.endOpArtesao())
        {
            this.stockProdutos-=this.produtosDona;
            geralInter.donaRecebeProdutos(this.produtosDona);
            geralInter.setnProdutosNaOficina(this.stockProdutos);
            geralInter.setEstadoDona(SDona.COLLECTING_A_BATCH_OF_PRODUCTS);
            this.donaChamadaProdutos=false;
            geralInter.setDonaChamadaProdutos(false);
        }
        else
        {
            geralInter.donaRecebeProdutos(this.stockProdutos);
            this.stockProdutos=0;
            geralInter.setnProdutosNaOficina(this.stockProdutos);
            geralInter.setEstadoDona(SDona.COLLECTING_A_BATCH_OF_PRODUCTS);
            this.donaChamadaProdutos=false;
            geralInter.setDonaChamadaProdutos(false);
        }  
        
    }
    
    
    
    /**
     *
     *  funcao usada pela dona para repor PM na oficina
     *
     * @param dona usado para alterar o repositorio geral
     * @param pm quantidade de pm fornecida pela dona
     * 
     */
    public synchronized void replenishStock(int pm) throws RemoteException
    {
        this.fornecimentosPM++;
        geralInter.novoFornecimentoPM(pm);
        this.totalPM+=pm;
        this.stockPM+=pm;
        geralInter.setnPMNaOficina(this.stockPM);
        geralInter.setEstadoDona(SDona.DELIVERING_PRIME_MATERIALS);
        this.donaChamadaPM=false;
        geralInter.setDonaChamadaPM(false);
        notifyAll();
    }
    
}
