/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import States.SCliente;
import Interfaces.*;
import Monitores.monGeral;

/**
 *
 * @author smrcascao
 */
public class Cliente extends Thread{
    
    /**
   *  Identificação do cliente
   *
   *    @serialField customerId
   */
    private int idCliente;
    
     
   /**
   *  Repositorio Geral
   *
   *    @serialField clientGeral
   */
   private intClienteGeral clientGeral;
   
   /**
   *  Repositorio Geral
   *
   *    @serialField clientLoja
   */
   private intClienteLoja clientLoja;
   
   /**
   *  Numero de compras efectuadas pelo cliente
   *
   *    @serialField cumCompras
   */
   private int numCompras;
   
    //private SCliente estadoCliente;
    //private int produtosComprados;
    //private int vezesEntrouLoja;
    
    //private int vezesPortaFechada;
    //private Boolean portaAberta=false;
    
    
    
    public Cliente(int idCliente, intClienteGeral clientGeral, intClienteLoja clientLoja) {
        //estadoCliente=SCliente.CARRYING_OUT_DAILY_CHORES;
        //produtosComprados=0;
        //vezesEntrouLoja=0;
        numCompras=0;
        //vezesPortaFechada=0;
        this.idCliente=idCliente;
        this.clientGeral=clientGeral;
        this.clientLoja=clientLoja;
    }
    
    @Override
    public void run() {

        boolean portaAberta;
        
        do{
            livingNormalLife();
            clientLoja.goShopping(idCliente, clientGeral);
            portaAberta=clientLoja.isDoorOpen(idCliente, clientGeral);


            if(portaAberta)
            {
                clientLoja.enterShop(idCliente, clientGeral);

                if(clientLoja.perusingAround())
                    clientLoja.iWantThis(idCliente, clientGeral);

                clientLoja.exitShop(idCliente, clientGeral);
            }
            else
                tryAgainLater();

        }while(!clientGeral.endOpCliente(idCliente));

    }

    /*public int getProdutosComprados() {
        return produtosComprados;
    }

    public int getVezesEntrouLoja() {
        return vezesEntrouLoja;
    }
*/
    public int getNumCompras() {
        return numCompras;
    }
/*
    public int getVezesPortaFechada() {
        return vezesPortaFechada;
    }
*/
    public int getIdCliente() {
        return idCliente;
    }


/*
    public void setProdutosComprados(int produtosComprados) {
        this.produtosComprados = produtosComprados;
    }

    public void setVezesEntrouLoja(int vezesEntrouLoja) {
        this.vezesEntrouLoja = vezesEntrouLoja;
    }

    public void setVezesPortaFechada(int vezesPortaFechada) {
        this.vezesPortaFechada = vezesPortaFechada;
    }
    */
    
    public void setNumCompras(int numCompras) {
        this.numCompras = numCompras;
    }

    
    
    public void livingNormalLife()
    {
      try
      { sleep ((long) (1 + 40 * Math.random ()));
      }
      catch (InterruptedException e) {}
    }

    public void tryAgainLater()
    {
      try
      { sleep ((long) (1 + 10 * Math.random ()));
      }
      catch (InterruptedException e) {}
    }
    
    @Override
    public String toString() {
        return "Cliente{" + ", numCompras=" + numCompras + ", idCliente=" + idCliente + '}';
    }
    
}
