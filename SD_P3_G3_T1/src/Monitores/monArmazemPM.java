/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Monitores;

import Interfaces.*;
import States.SDona;

/**
 *
 * @author rofler
 */
public class monArmazemPM implements intDonaArmazemPM{
    
    //variaveis
    
    //PM disponivel para venda
    private int stockPM;
    
    //preco da unidade de PM
    private int precoPM;
    
    //quantidade total de PM vendida
    private int totalVendaPM;
    
    //Numero de vendas
    private int vendasPM;
    
    //quantidade de PM por fornecimento
    private int fornecimentoPM;
    
    public monArmazemPM(int stockInicialPM, int preco, int PMPorFornecimento)
    {
        this.fornecimentoPM=PMPorFornecimento;
        this.precoPM=preco;
        this.stockPM=stockInicialPM;
        this.totalVendaPM=0;
        this.vendasPM=0;
    }
    
    @Override
    public void visitSuppliers(intDonaGeral dona)
    {
        dona.setEstadoDona(SDona.BUYING_PRIME_MATERIALS);
        this.stockPM-=this.fornecimentoPM;
        this.totalVendaPM+=this.fornecimentoPM;
        this.vendasPM++;
    }
    
}
