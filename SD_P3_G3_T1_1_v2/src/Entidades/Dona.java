
package Entidades;

import Monitores.*;
import States.SDona;

/**
 *
 * @author smrcascao
 */
public class Dona extends Thread{

    /**
    *  estado actual da dona
    *
    *    @serialField estadoDona
    */
    private SDona estadoDona;
    
    /**
    *  Repositorio Geral
    *
    *    @serialField dona
    */
    private monGeral dona;
    
    /**
    *  Loja
    *
    *    @serialField donaLoja
    */
    private monLoja donaLoja;
    
    /**
    *  Oficina
    *
    *    @serialField donaOficina
    */
    private monOficina donaOficina;
    
    /**
    *  ArmazemPM
    *
    *    @serialField donaArmazemPM
    */
    private monArmazemPM donaArmazemPM;
    
    /**
     *
     *  Construtor
     *
     * @param dona repositorio geral
     * @param donaLoja Loja
     * @param donaOficina Oficina
     * @param donaArmazemPM ArmazemPM
     *
     */
    public Dona(monGeral dona, monLoja donaLoja, monOficina donaOficina, monArmazemPM donaArmazemPM) {
        //this.dona.setEstadoDona(SDona.OPENING_THE_SHOP);
        //this.canGoOut = false;
        this.dona=dona;
        this.donaArmazemPM=donaArmazemPM;
        this.donaLoja=donaLoja;
        this.donaOficina=donaOficina;
        this.estadoDona=SDona.OPENING_THE_SHOP;
    }

    /**
    *  funcao que executa o ciclo de vida da dona
    */
    @Override
    public void run() {
        
        boolean canGoOut=false;
        int sit=0, idCliente, pm;
              do{
                donaLoja.prepareToWork(dona);
                estadoDona=SDona.WAITING_FOR_NEXT_TASK;
                canGoOut=false;          
                
                while(!canGoOut)
                {
                    sit=donaLoja.appraiseSit(dona);
                    switch(sit)
                    {
                        case 2: //caso tenha de atender cliente
                                idCliente=donaLoja.addressACustomer(dona);
                                estadoDona=SDona.ATTENDIND_A_CUSTOMER;
                                serviceCustomer();
                                donaLoja.sayGoodbyeToCustomer(dona, idCliente);
                                estadoDona=SDona.WAITING_FOR_NEXT_TASK;
                                break;
                        case 3://caso tenha de ir buscar produtos a oficina
                        case 4://caso tenha de ir buscar PM ao armazem
                                donaLoja.closeTheDoor(dona);
                                canGoOut=!donaLoja.customersInTheShop();
                                break;
                        case 1:
                                
                                canGoOut=true;
                                break;
                        case 0:
                                canGoOut=false;
                                break;
                    }
                    
                    
                }
                donaLoja.prepareToLeave(dona);
                estadoDona=SDona.CLOSING_THE_SHOP;
                
                if(sit==3)
                {
                    donaOficina.goToWorkShop(dona);
                    estadoDona=SDona.COLLECTING_A_BATCH_OF_PRODUCTS;
                }
                else if(sit==4)
                {
                    pm = donaArmazemPM.visitSuppliers(dona);
                    estadoDona=SDona.BUYING_PRIME_MATERIALS;
                    donaOficina.replenishStock(dona, pm);
                    estadoDona=SDona.DELIVERING_PRIME_MATERIALS;
                }
                donaLoja.returnToShop(dona);
                estadoDona=SDona.OPENING_THE_SHOP;
                
            }while(!dona.endOpDona());
            
       
    }
    
    
   /**
   *  atende o cliente (operação interna).
   */
   private void serviceCustomer()
   {
      try
      { sleep ((long) (1 + 1 * Math.random ()));
      }
      catch (InterruptedException e) {}
   }

}
