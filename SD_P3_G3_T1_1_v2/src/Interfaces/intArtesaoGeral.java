/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import States.SArtesao;

/**
 *
 * @author rofler
 */
public interface intArtesaoGeral {
    
    /*Altera o estado do artesao definido pelo 'id' introduzido para o estado 'state' introduzido*/
    public void setEstadoArtesao(int id, SArtesao state);
    public void setDonaChamadaPM(boolean donaChamadaPM);
    public void setDonaChamadaProdutos(boolean donaChamadaProdutos);
    public boolean enOpArtesao();
}
