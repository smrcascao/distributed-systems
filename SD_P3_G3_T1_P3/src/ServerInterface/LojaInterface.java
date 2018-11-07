/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerInterface;

import Monitores.monGeral;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author rofler
 */
public interface LojaInterface extends Remote{
    
    public void goShopping(int idCliente) throws RemoteException;
    
    public  boolean isDoorOpen() throws RemoteException;
    
    public  void enterShop(int idCliente) throws RemoteException;
    
    public  boolean perusingAround() throws RemoteException;
    
    public  void exitShop(int idCliente) throws RemoteException;
    
    public  void iWantThis(int idCliente) throws RemoteException;
    
    public  void tryAgainLater(int idCliente) throws RemoteException;
    
    public  int addressACustomer() throws RemoteException;
    
    public  void sayGoodbyeToCustomer(int idCliente) throws RemoteException;
    
    public  void prepareToWork() throws RemoteException;
    
    public  int appraiseSit() throws RemoteException;
    
    public  boolean customersInTheShop() throws RemoteException;
    
    public  void closeTheDoor() throws RemoteException;
    
    public  void prepareToLeave() throws RemoteException;
    
    public  void returnToShop() throws RemoteException;
    
    
}
