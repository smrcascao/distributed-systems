package ServerInterface;

import VectorTime.VectorTime;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *   Este tipo de dados descreve o protocolo de interacção com o Armazem PM, como um objecto remoto, que constitui o
 *   serviço prestado numa solução do Problema Obrigatorio 3 que implementa o modelo cliente-servidor de
 *   tipo 2 (replicação do servidor).
 *   Transposição da solução concorrente estática.
 */
public interface ArmazemPMInterface extends Remote 
{
    
    public int visitSuppliers(VectorTime v) throws RemoteException;
    
    public void shutdown() throws RemoteException;
    
}
