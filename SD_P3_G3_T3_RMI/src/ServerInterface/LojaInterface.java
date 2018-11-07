package ServerInterface;

import VectorTime.VectorTime;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *   Este tipo de dados descreve o protocolo de interacção com a Loja, como um objecto remoto, que constitui o
 *   serviço prestado numa solução do Problema Obrigatorio 3 que implementa o modelo cliente-servidor de
 *   tipo 2 (replicação do servidor).
 *   Transposição da solução concorrente estática.
 */
public interface LojaInterface extends Remote{
    
    public VectorTime goShopping(int idCliente, VectorTime v) throws RemoteException;
    
    public  boolean isDoorOpen() throws RemoteException;
    
    public  VectorTime enterShop(int idCliente, VectorTime v) throws RemoteException;
    
    public  boolean perusingAround() throws RemoteException;
    
    public  VectorTime exitShop(int idCliente, VectorTime v) throws RemoteException;
    
    public  VectorTime iWantThis(int idCliente, VectorTime v) throws RemoteException;
    
    public  VectorTime tryAgainLater(int idCliente, VectorTime v) throws RemoteException;
    
    public  int addressACustomer(VectorTime v) throws RemoteException;
    
    public  VectorTime sayGoodbyeToCustomer(int idCliente, VectorTime v) throws RemoteException;
    
    public  VectorTime prepareToWork(VectorTime v) throws RemoteException;
    
    public  int appraiseSit() throws RemoteException;
    
    public  boolean customersInTheShop() throws RemoteException;
    
    public  void closeTheDoor() throws RemoteException;
    
    public  VectorTime prepareToLeave(VectorTime v) throws RemoteException;
    
    public  VectorTime returnToShop(VectorTime v) throws RemoteException;
    
    public void shutdown() throws RemoteException;
    
}
