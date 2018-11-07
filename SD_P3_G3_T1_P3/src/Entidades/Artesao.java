/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import Monitores.*;
import ServerInterface.GeralInterface;
import ServerInterface.OficinaInterface;
import States.SArtesao;
import genclass.GenericIO;
/**
 *
 * @author rofler
 */
public class Artesao extends Thread{
    
    /**
    *  id do artesao
    *
    *    @serialField idArtesao
    */
    private int idArtesao;
    
    /**
    *  estado actual do artesao
    *
    *    @serialField estadoArtesao
    */
    private SArtesao estadoArtesao;
       
    /**
    *  Repositorio Geral
    *
    *    @serialField artesaoGeral
    */
    private GeralInterface artesaoGeral;
    
    /**
    *  Oficina
    *
    *    @serialField artesaoOficina
    */
    private OficinaInterface artesaoOficina;
        
    /**
    *  numero de produtos feitos pelo artesao
    *
    *    @serialField produtosFeitos
    */
    private int produtosFeitos;
    
    
    /**
     *
     *  Construtor
     * 
     * @param idArtesao id do artesao
     * @param artesaoGeral repositorio geral
     * @param artesaoOficina oficina
     *
     */
    public Artesao(int idArtesao, GeralInterface artesaoGeral, OficinaInterface artesaoOficina) {
        
        this.idArtesao=idArtesao;
        this.artesaoGeral=artesaoGeral;
        this.artesaoOficina=artesaoOficina;
        this.estadoArtesao=SArtesao.FETCHING_PRIME_MATERIALS;
        
        this.produtosFeitos=0;
        
    }
    
    
    /**
    *  funcao que executa o ciclo de vida do artesao
    */
    public void run() {
        
        boolean canWork;
        int m=1, ligaDona;
        try {
            

        do{
           canWork= false;

           while(!canWork)
           {
               m= artesaoOficina.checkForMaterials(idArtesao);
               estadoArtesao=SArtesao.FETCHING_PRIME_MATERIALS;

               switch(m)
               {
               case(1):
                        canWork=true;
                        break;
               case(2):
                        if (artesaoGeral.isDonaChamadaPM()==false && artesaoGeral.isArmPM())
                        {
                            artesaoOficina.primeMaterialsNeeded(idArtesao);
                            estadoArtesao=SArtesao.CONTACTING_THE_ENTREPRENEUR;
                            artesaoOficina.backToWork(idArtesao);
                            estadoArtesao=SArtesao.FETCHING_PRIME_MATERIALS;
                        }
                        canWork=true;
                        break;
               case(3):
                        break;
               case(0):
                        if (artesaoGeral.isDonaChamadaPM()==false && artesaoGeral.isArmPM())
                        {
                            artesaoOficina.primeMaterialsNeeded(idArtesao);
                            estadoArtesao=SArtesao.CONTACTING_THE_ENTREPRENEUR;
                            artesaoOficina.backToWork(idArtesao);
                            estadoArtesao=SArtesao.FETCHING_PRIME_MATERIALS;
                        }
                        break;
                   
                case(4):
                        canWork=true;
                        break;


               }
           }    
            
           if(m!=4)
           {
                artesaoOficina.collectMaterials(idArtesao);
                artesaoOficina.prepareToProduce(idArtesao);
                estadoArtesao=SArtesao.PRODUCING_A_NEW_PIECE;

                shapingItUp();
                produtosFeitos++;

                ligaDona =artesaoOficina.goToStore(idArtesao);
                estadoArtesao=SArtesao.STORING_IT_FOR_TRANSFER;

                if((ligaDona==2 || ligaDona==0) && artesaoGeral.isDonaChamadaProdutos()==false)
                {
                    artesaoOficina.batchReadyForTransfer(idArtesao);
                    estadoArtesao=SArtesao.CONTACTING_THE_ENTREPRENEUR;
                }
                artesaoOficina.backToWork(idArtesao);
           }

        }while(!artesaoGeral.endOpArtesao());

        
        if(artesaoGeral.isDonaChamadaProdutos()==false && m!= 4)
        {
            artesaoOficina.batchReadyForTransfer(idArtesao);
            estadoArtesao=SArtesao.CONTACTING_THE_ENTREPRENEUR;
        }
        } catch (Exception e) {
                   GenericIO.writelnString("Excepção na invocação remota de método sobre a regiao partilhada pelo Artesao "
                    + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
    *  fabrica o produto (operação interna).
    */
    public void shapingItUp()
    {
     try
      { sleep ((long) (1 + 10 * Math.random ()));
      }
      catch (InterruptedException e) {}
    }

    /**
    *  devolve o numero de produtos que o artesao fez
    * 
    * @return numero de produtos que o artesao fez
    * 
    */
    public int getProdutosFeitos() {
        return produtosFeitos;
    }



    @Override
    public String toString() {
        return "Artesao{" + "id=" + idArtesao + ", produtosFeitos=" + produtosFeitos + '}';
    }


    
}
