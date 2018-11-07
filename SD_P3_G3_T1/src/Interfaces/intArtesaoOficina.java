/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

/**
 *
 * @author rofler
 */
public interface intArtesaoOficina {
    
    public int checkForMaterials(int idArtesao);
    public void collectMaterials(int idArtesao);
    public void primeMaterialsNeeded(int idArtesao, intArtesaoGeral artesao);
    public void backToWork(int idArtesao, intArtesaoGeral artesao);
    public int goToStore(int idArtesao, intArtesaoGeral artesao);
    //public boolean ShapingItUp();
    public void prepareToProduce(int idArtesao, intArtesaoGeral artesao);
    public void batchReadyForTransfer(int idArtesao, intArtesaoGeral artesao);
    
    
}
