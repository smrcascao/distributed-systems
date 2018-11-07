package ServerInterface;

import VectorTime.VectorTime;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *   Este tipo de dados descreve o protocolo de interacção com a Oficina, como um objecto remoto, que constitui o
 *   serviço prestado numa solução do Problema Obrigatorio 3 que implementa o modelo cliente-servidor de
 *   tipo 2 (replicação do servidor).
 *   Transposição da solução concorrente estática.
 */
public interface OficinaInterface extends Remote{
    
    public  int checkForMaterials(int id, VectorTime v) throws RemoteException;
    
    public  void collectMaterials(int id) throws RemoteException;
    
    public  VectorTime primeMaterialsNeeded(int id, VectorTime v) throws RemoteException;
    
    public  VectorTime batchReadyForTransfer(int id, VectorTime v) throws RemoteException;
    
    public  void backToWork(int id) throws RemoteException;
    
    public  int goToStore(int id, VectorTime v) throws RemoteException;
    
    public  VectorTime prepareToProduce(int id, VectorTime v) throws RemoteException;
    
    public  VectorTime goToWorkShop(VectorTime v) throws RemoteException;
    
    public  VectorTime replenishStock(int pm, VectorTime v) throws RemoteException;
    
    public void shutdown() throws RemoteException;
    
    
    
    
}
