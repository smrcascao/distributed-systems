/*
 * Oficina
 * 
 * 
 */
package Monitores;

import Interfaces.*;
import States.SArtesao;
import States.SDona;
import java.util.Random;

/**
 *
 * @author rofler
 */
public class monOficina implements intArtesaoOficina, intDonaOficina {
    
    //PM
    //quantidade de PM em stock 
    private int stockPM;
    
    //quantidade total de PM encomendada
    private int totalPM;
            
    //num total de fornecimentos de PM
    private int fornecimentosPM;
    
    //PM para construir um produto
    private int produtoPM;        
    
    //limite minimo de PM
    private int limiteMinPM;
            
    //PM recebida por fornecimento
    private int PMPorFornecimento;
    
    //Produto
    //Produtos em stock
    private int stockProdutos;
    
    //quantidade total de produtos produzidos
    private int totalProdutos;
    
    //Limite de Alarme da quantidade de produtos
    private int alarmeProdutos;
    
    //Limite maximo de produtos
    private int limiteMaxProdutos;
    
    //quantidade de produtos que a dona leva para a loja
    private int produtosDona;
    
    //dona chamada (por falta de PM ou por existirem demasiados produtos em stock)
    private boolean donaChamadaPM, donaChamadaProdutos;
    
    
    //construtor
    public monOficina(int alarmeProd, int limiteProd, int alarmePM, int prodPM, int fornPM)
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
                
    }
    
    
    /* funcoes dos artesaos*/
    @Override
    public synchronized int checkForMaterials(int id)
    {
        if(stockPM < produtoPM)
            return 0;
        else if (stockPM - produtoPM < limiteMinPM)
            return 2;
        else 
            return 1;
    }
    
    @Override
    public synchronized void collectMaterials(int id)
    {
        stockPM-=produtoPM;
        
    }
    
    @Override
    public synchronized void primeMaterialsNeeded(int id, intArtesaoGeral artesao)
    {
        artesao.setEstadoArtesao(id, SArtesao.CONTACTING_THE_ENTREPRENEUR);
        this.donaChamadaPM=true;
        artesao.setDonaChamadaPM(true);
        
    }
    
    @Override
    public synchronized void backToWork(int id, intArtesaoGeral artesao)
    {
        artesao.setEstadoArtesao(id, SArtesao.FETCHING_PRIME_MATERIALS);
    }
    
    @Override
    public synchronized int goToStore(int id, intArtesaoGeral artesao)
    {
        artesao.setEstadoArtesao(id, SArtesao.STORING_IT_FOR_TRANSFER);
        this.stockProdutos++;
        this.totalProdutos++;
        if(stockProdutos >= limiteMaxProdutos )
            return 0;
        else if(stockProdutos >= alarmeProdutos)
            return 2;
        else 
            return 1;        
    }
    
    /*@Override
    public synchronized boolean ShapingItUp()
    {
        Random espera = new Random();
        return espera.nextInt(100) >= 50;
    }*/
    
    @Override
    public synchronized void prepareToProduce(int id, intArtesaoGeral artesao)
    {
        artesao.setEstadoArtesao(id, SArtesao.PRODUCING_A_NEW_PIECE);
    }
    
    @Override
    public synchronized void batchReadyForTransfer(int id, intArtesaoGeral artesao)
    {
        artesao.setEstadoArtesao(id, SArtesao.CONTACTING_THE_ENTREPRENEUR);
        this.donaChamadaProdutos=true;
        artesao.setDonaChamadaProdutos(true);
        
    }
    
    
    /*funcoes da dona*/
    
    @Override
    public synchronized void goToWorkShop(intDonaGeral dona)
    {
        dona.setEstadoDona(SDona.COLLECTING_A_BATCH_OF_PRODUCTS);
        this.stockProdutos-=this.produtosDona;
        dona.donaRecebeProdutos(this.produtosDona);
        this.donaChamadaProdutos=false;
        
        
    }
    
    
    
    @Override
    public synchronized void replenishStock(intDonaGeral dona)
    {
        this.fornecimentosPM++;
        this.totalPM+=this.PMPorFornecimento;
        this.stockPM+=this.PMPorFornecimento;
        dona.setEstadoDona(SDona.DELIVERING_PRIME_MATERIALS);
    }
    
}
