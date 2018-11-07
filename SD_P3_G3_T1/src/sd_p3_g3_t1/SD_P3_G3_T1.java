/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sd_p3_g3_t1;
import Interfaces.*;
import Entidades.*;
import Monitores.*;
import States.*;
/**
 *
 * @author smrcascao
 */
public class SD_P3_G3_T1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
      monLoja loja;                                        // loja
     int nMaximoProdutosLoja=30;
     int precoProduto=10;
      int nCustomer = 5;                                   // número de clientes
      Cliente [] clientes = new Cliente[nCustomer];        // array de threads cliente
      intClienteGeral clientGeral;
      intClienteLoja clientLoja;
    
    
    loja = new monLoja(nMaximoProdutosLoja, precoProduto);
    
       for (int i = 0; i < nCustomer; i++)
        clientes[i] = new Cliente(i,clientGeral,clientLoja);  
    
    
    }
}
