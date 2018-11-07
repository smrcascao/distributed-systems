/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import States.SDona;

/**
 *
 * @author rofler
 */
public interface intDonaGeral {
    
     public void setEstadoDona(SDona state);   
     public int getProdutosComDona();
     public void donaRecebeProdutos(int p);
     public boolean endOpDona();
     public boolean isDonaChamadaPM();
     public boolean isDonaChamadaProdutos();
     
}
