/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author rofler
 */
public interface ArmazemPMInterface extends Remote 
{
    
    public int visitSuppliers() throws RemoteException;
    
    
}
