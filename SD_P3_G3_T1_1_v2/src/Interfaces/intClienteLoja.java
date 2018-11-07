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
public interface intClienteLoja {
    
    public void goShopping(int idCliente,  intClienteGeral cliente);
    public boolean isDoorOpen(int idCliente, intClienteGeral cliente);
    public boolean perusingAround();
    public void iWantThis(int idCliente, intClienteGeral cliente);
    public void exitShop(int idCliente, intClienteGeral cliente);
    public void enterShop(int idCliente, intClienteGeral cliente);
    
}
