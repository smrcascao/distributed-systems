
package Entidades;

import Interfaces.*;
import States.SDona;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author smrcascao
 */
public class Dona extends Thread{

    /**
    *  Repositorio Geral
    *
    *    @serialField dona
    */
    private intDonaGeral dona;
    
    /**
    *  Loja
    *
    *    @serialField donaLoja
    */
    private intDonaLoja donaLoja;
    
    /**
    *  Oficina
    *
    *    @serialField donaOficina
    */
    private intDonaOficina donaOficina;
    
    /**
    *  ArmazemPM
    *
    *    @serialField donaArmazemPM
    */
    private intDonaArmazemPM donaArmazemPM;
    
    
    //private boolean canGoOut;
    //private int sit, idCliente;

    public Dona(intDonaGeral dona, intDonaLoja donaLoja, intDonaOficina donaOficina, intDonaArmazemPM donaArmazemPM) {
        //this.dona.setEstadoDona(SDona.OPENING_THE_SHOP);
        //this.canGoOut = false;
        this.dona=dona;
        this.donaArmazemPM=donaArmazemPM;
        this.donaLoja=donaLoja;
        this.donaOficina=donaOficina;
    }

    @Override
    public void run() {
        
        boolean canGoOut=false;
        int sit=0, idCliente;
        
        try {
            do{
                donaLoja.prepareToWork(dona);
                
                while(!canGoOut)
                {
                    sit=donaLoja.appraiseSit(dona);
                    switch(sit)
                    {
                        case 2: //caso tenha de atender cliente
                                idCliente=donaLoja.addressACustomer(dona);
                                serviceCustomer();
                                donaLoja.sayGoodbyeToCustomer(dona, idCliente);
                                break;
                        case 3://caso tenha de ir buscar produtos a oficina
                        case 4://caso tenha de ir buscar PM ao armazem
                                donaLoja.closeTheDoor();
                                canGoOut=!donaLoja.customersInTheShop();
                                break;
                    }
                    
                    
                }
                donaLoja.prepareToLeave(dona);
                
                if(sit==3)
                    donaLoja.goToWorkShop(dona);
                else if(sit==4)
                {
                    donaArmazemPM.visitSuppliers(dona);
                    donaOficina.replenishStock(dona);
                }
                donaLoja.returnToShop(dona);
                
            }while(!dona.endOpDona());
            
        } catch (Exception ex) {
            Logger.getLogger(Dona.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
   /**
   *  atende o cliente (operação interna).
   */
   private void serviceCustomer()
   {
      try
      { sleep ((long) (1 + 100 * Math.random ()));
      }
      catch (InterruptedException e) {}
   }

}
