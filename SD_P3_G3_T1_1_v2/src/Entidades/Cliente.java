/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import Monitores.*;
import States.SCliente;

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
   *  estado actual do cliente
   *
   *    @serialField estadoCliente
   */
    private SCliente estadoCliente;
     
   /**
   *  Repositorio Geral
   *
   *    @serialField clientGeral
   */
   private monGeral clientGeral;
   
   /**
   *  Loja
   *
   *    @serialField clientLoja
   */
   private monLoja clientLoja;
   
   /**
   *  Numero de compras efectuadas pelo cliente
   *
   *    @serialField cumCompras
   */
   private int numCompras;
    
    /**
     *
     *  Construtor
     * 
     * @param idCliente id do cliente
     * @param clientGeral repositorio geral
     * @param clientLoja Loja
     *
     */
    public Cliente(int idCliente, monGeral clientGeral, monLoja clientLoja) {
        
        this.numCompras=0;
        this.idCliente=idCliente;
        this.clientGeral=clientGeral;
        this.clientLoja=clientLoja;
        this.estadoCliente=SCliente.CARRYING_OUT_DAILY_CHORES;
    }
    
    /**
    *  funcao que executa o ciclo de vida do artesao
    */
    @Override
    public void run() {

        boolean portaAberta;
        
        do{
            livingNormalLife();
            clientLoja.goShopping(idCliente, clientGeral);
            this.estadoCliente=SCliente.CHECKING_DOOR_OPEN;
            portaAberta=clientLoja.isDoorOpen();


            if(portaAberta)
            {
                clientLoja.enterShop(idCliente, clientGeral);
                this.estadoCliente=SCliente.APPRAISING_OFFER_IN_DISPLAY;

                if(clientLoja.perusingAround()){
                    clientLoja.iWantThis(idCliente, clientGeral);
                    this.estadoCliente=SCliente.BUYING_SOME_GOODS;
                }

                clientLoja.exitShop(idCliente, clientGeral);
                this.estadoCliente=SCliente.CARRYING_OUT_DAILY_CHORES;
            }
            else{
                clientLoja.tryAgainLater(idCliente, clientGeral);
                this.estadoCliente=SCliente.CARRYING_OUT_DAILY_CHORES;
            }

        }while(!clientGeral.endOpCliente());

    }

    /**
    *  devolve o numero de produtos que o cliente comprou
    * 
    * @return numero de produtos que o cliente comprou
    * 
    */
    public int getNumCompras() {
        return numCompras;
    }

    /**
    *  devolve o numero de identificacao do cliente
    * 
    * @return numero de identificacao do cliente
    * 
    */
    public int getIdCliente() {
        return idCliente;
    }

    /**
    *  o cliente fica em espera um tempo aleatorio(operação interna).
    */
    public void livingNormalLife()
    {
      try
      { sleep ((long) (1 + 40 * Math.random ()));
      }
      catch (InterruptedException e) {}
    }
    
    @Override
    public String toString() {
        return "Cliente{" + ", numCompras=" + numCompras + ", idCliente=" + idCliente + '}';
    }
    
}
