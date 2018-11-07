/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import Interfaces.*;
/**
 *
 * @author rofler
 */
public class Artesao extends Thread{
    
    private int idArtesao;
   
    //private int m;
    //private SArtesao estados_art;
    
    /**
    *  Repositorio Geral
    *
    *    @serialField artesaoGeral
    */
    private intArtesaoGeral artesaoGeral;
    
    /**
    *  Oficina
    *
    *    @serialField artesaoOficina
    */
    private intArtesaoOficina artesaoOficina;
    
    //private int diasTrabalho;
    
    /**
    *  produtosFeitos
    *
    *    @serialField numero de produtos feitos pelo artesao
    */
    private int produtosFeitos;
    
    /**
    *  chamadasDona
    *
    *    @serialField numero de vezes que o artesao ja chamou a dona
    */
    private int chamadasDona;
    
    /**
    *  vezesParou
    *
    *    @serialField numero de vezes que o artesao ja parou de trabalhar por falta de PM
    */
    private int vezesParou;
    
    public Artesao(intArtesaoGeral artesaoGeral, intArtesaoOficina artesaoOficina) {
        
        this.artesaoGeral=artesaoGeral;
        this.artesaoOficina=artesaoOficina;
        
        this.chamadasDona=0;
        //this.diasTrabalho=0;
        this.produtosFeitos=0;
        this.vezesParou=0;
        //this.estados_art=estados_art.FETCHING_PRIME_MATERIALS;
        
    }
    
    public void run() {
        
        boolean canWork;
        int m, ligaDona;
        
        do{
           canWork= false;

           while(!canWork)
           {
               m= artesaoOficina.checkForMaterials(idArtesao);

               switch(m)
               {
               case(1):
                        canWork=true;
                        break;
               case(2):
                        artesaoOficina.primeMaterialsNeeded(idArtesao, artesaoGeral);
                        chamadasDona++;

               case(0):
                        vezesParou++;
                        break;


               }
           }    
            
           artesaoOficina.collectMaterials(idArtesao);
           artesaoOficina.prepareToProduce(idArtesao, artesaoGeral);
            
           shapingItUp();
           produtosFeitos++;
            
             ligaDona =artesaoOficina.goToStore(idArtesao, artesaoGeral);

             if(ligaDona==2)
                 artesaoOficina.batchReadyForTransfer(idArtesao, artesaoGeral);

             artesaoOficina.backToWork(idArtesao, artesaoGeral);


        }while(artesaoGeral.enOpArtesao());

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

    /*public SArtesao getEstados_art() {
        return estados_art;
    }

    public int getDiasTrabalho() {
        return diasTrabalho;
    }
    public void setEstados_art(SArtesao estados_art) {
        this.estados_art = estados_art;
    }

    public void setDiasTrabalho(int diasTrabalho) {
        this.diasTrabalho = diasTrabalho;
    }*/

    public int getProdutosFeitos() {
        return produtosFeitos;
    }

    public int getChamadasDona() {
        return chamadasDona;
    }

    public int getVezesParou() {
        return vezesParou;
    }

    

    public void setProdutosFeitos(int produtosFeitos) {
        this.produtosFeitos = produtosFeitos;
    }

    public void setChamadasDona(int chamadasDona) {
        this.chamadasDona = chamadasDona;
    }

    public void setVezesParou(int vezesParou) {
        this.vezesParou = vezesParou;
    }

    @Override
    public String toString() {
        return "Artesao{" + "id=" + idArtesao + ", produtosFeitos=" + produtosFeitos + ", chamadasDona=" + chamadasDona + ", vezesParou=" + vezesParou + '}';
    }


    
}
