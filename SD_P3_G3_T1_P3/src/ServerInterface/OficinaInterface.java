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
public interface OficinaInterface extends Remote{
    
    public  int checkForMaterials(int id) throws RemoteException;
    
    public  void collectMaterials(int id) throws RemoteException;
    
    public  void primeMaterialsNeeded(int id) throws RemoteException;
    
    public  void batchReadyForTransfer(int id) throws RemoteException;
    
    public  void backToWork(int id) throws RemoteException;
    
    public  int goToStore(int id) throws RemoteException;
    
    public  void prepareToProduce(int id) throws RemoteException;
    
    public  void goToWorkShop() throws RemoteException;
    
    public  void replenishStock(int pm) throws RemoteException;
    
    
    
    
    
}
