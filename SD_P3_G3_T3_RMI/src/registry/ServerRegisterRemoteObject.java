package registry;

import ServerInterface.Register;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import genclass.GenericIO;
import static java.lang.Thread.sleep;
import java.rmi.NotBoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This data type instantiates and registers a remote object that enables the
 * registration of other remote objects located in the same or other processing
 * nodes in the local registry service. Communication is based in Java RMI.
 */
public class ServerRegisterRemoteObject {

    /**
     * Main task.
     *
     * @param args parametros de entrada.
     */
    public static void main(String[] args) {
        /* get location of the registry service */

        String rmiRegHostName;
        int rmiRegPortNumb;

        GenericIO.writeString("Nome do nó de processamento onde está localizado o serviço de registo? (Apenas o numero da maquina) ");
        rmiRegHostName = "l040101-ws" + GenericIO.readlnString() + ".ua.pt";
        GenericIO.writeString("Número do port de escuta do serviço de registo? ");
        rmiRegPortNumb = GenericIO.readlnInt();

        /* create and install the security manager */
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        GenericIO.writelnString("Security manager was installed!");

        /* instantiate a registration remote object and generate a stub for it */
        RegisterRemoteObject regEngine = new RegisterRemoteObject(rmiRegHostName, rmiRegPortNumb);
        Register regEngineStub = null;
        int listeningPort = 22320;                            /* it should be set accordingly in each case */

        try {
            regEngineStub = (Register) UnicastRemoteObject.exportObject(regEngine, listeningPort);
        } catch (RemoteException e) {
            GenericIO.writelnString("RegisterRemoteObject stub generation exception: " + e.getMessage());
            System.exit(1);
        }
        GenericIO.writelnString("Stub was generated!");

        /* register it with the local registry service */
        String nameEntry = "RegisterHandler";
        Registry registry = null;

        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException e) {
            GenericIO.writelnString("RMI registry creation exception: " + e.getMessage());
            System.exit(1);
        }
        GenericIO.writelnString("RMI registry was created!");

        try {
            registry.rebind(nameEntry, regEngineStub);
        } catch (RemoteException e) {
            GenericIO.writelnString("RegisterRemoteObject remote exception on registration: " + e.getMessage());
            System.exit(1);
        }
        GenericIO.writelnString("RegisterRemoteObject object was registered!\nPort: " + listeningPort + "\n");

        while (regEngine.getFlag() == 0) {
            try {
                sleep((long) (3000));
            } catch (InterruptedException e) {
            }
        }
        GenericIO.writelnString("O Remote Object vai encerrar");

        try {
            registry.unbind(nameEntry);
        } catch (RemoteException ex) {
            GenericIO.writelnString("Excepção ao encerrar do Repositorio Geral: " + ex.getMessage());
        } catch (NotBoundException ex) {
            GenericIO.writelnString("Excepção ao encerrar do Repositorio Geral: " + ex.getMessage());
        }
        
        System.exit(0);
    }
}
