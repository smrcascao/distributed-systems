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
public interface intDonaLoja {
    
    public int addressACustomer(intDonaGeral dona);
    public void sayGoodbyeToCustomer(intDonaGeral dona, int idCliente);
    //public boolean serviceCustomer();
    public void prepareToWork(intDonaGeral dona);
    public int appraiseSit(intDonaGeral dona);
    public boolean customersInTheShop();
    public void closeTheDoor();
    public void prepareToLeave(intDonaGeral dona);
    public void goToWorkShop(intDonaGeral dona);
    public void returnToShop(intDonaGeral dona);
}
