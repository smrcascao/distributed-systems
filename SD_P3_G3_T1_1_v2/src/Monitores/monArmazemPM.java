/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Monitores;

import States.SDona;
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
   *  PM disponivel para venda
   *
   *    @serialField stockPM
   */
    private int stockPM;
    

    /**
   *  preco da unidade de PM
   *
   *    @serialField precoPM
   */
    private int precoPM;
    

    /**
   *  quantidade total de PM vendida
   *
   *    @serialField totalVendaPM
   */
    private int totalVendaPM;
    

    /**
   *  Numero de vendas
   *
   *    @serialField vendasPM
   */
    private int vendasPM;
    
    //quantidade de PM por fornecimento
   /**
   *  quantidade de PM por fornecimento
   *
   *    @serialField fornecimentoPM
   */
    private int fornecimentoPM;
    
    
    /**
     *
     *  Construtor
     *
     * @param stockInicialPM PM disponivel para venda
     * @param preco preco da unidade de PM
     * @param PMPorFornecimento quantidade de PM por fornecimento
     *
     */
    public monArmazemPM(int stockInicialPM, int preco, int PMPorFornecimento)
    {

      
        
        this.fornecimentoPM=0;
        this.precoPM=preco;
        this.stockPM=stockInicialPM;
        this.totalVendaPM=0;
        this.vendasPM=0;
    }
    
   
    public int visitSuppliers(monGeral dona)
    {
        Random randPercent= new Random();
        Random rand= new Random();
        
        int percent = randPercent.nextInt(101);
        
        if(percent<80)
        {
            this.fornecimentoPM = rand.nextInt((int) ((int)(this.stockPM)*0.3+1));
            this.stockPM-=this.fornecimentoPM;
                           
        }
        else
        {
            this.fornecimentoPM = rand.nextInt((this.stockPM+1));
            this.stockPM-=this.fornecimentoPM;
        }
        
        if(this.stockPM==0)
            dona.setArmPM(false);
        
        /*
        if(this.stockPM >= this.fornecimentoPM)
        {
            dona.setEstadoDona(SDona.BUYING_PRIME_MATERIALS);
            this.stockPM-=this.fornecimentoPM;
            this.totalVendaPM+=this.fornecimentoPM;
            this.vendasPM++;
            return this.fornecimentoPM;
        }
        else
        {
            dona.setEstadoDona(SDona.BUYING_PRIME_MATERIALS);
            int aux=this.stockPM;
            this.stockPM = 0;
            this.totalVendaPM+=aux;
            this.vendasPM++;
            dona.setArmPM(false);
            System.out.println(this.stockPM);        //debufg
            
            return aux;
        }*/
    return fornecimentoPM;
    }
    
}
