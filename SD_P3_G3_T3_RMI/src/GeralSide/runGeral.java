/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GeralSide;

import ServerInterface.GeralInterface;
import ServerInterface.Register;
import genclass.GenericIO;
import static java.lang.Thread.sleep;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Este tipo de dados gera o Problema Obrigatorio 3 do lado do servidor
 * (instanciação do Repositorio Geral como um objecto remoto). Solução do
 * Problema Obrigatorio 3 que implementa o modelo cliente-servidor de tipo 2
 * (replicação do servidor).
 */
public class runGeral {

    public static void main(String[] args) { //args-> 0=hostName, 1=portNumb, 2=totalPM, 3=prodPM, 4=logFileName

        String rmiRegHostName;                      // nome do sistema onde está localizado o serviço de registos RMI
        int rmiRegPortNumb;                         // port de escuta do serviço

        GenericIO.writeString("Nome do nó de processamento onde está localizado o serviço de registo? (Apenas o numero da maquina) ");
        rmiRegHostName = "l040101-ws"+GenericIO.readlnString()+".ua.pt";
        GenericIO.writeString("Número do port de escuta do serviço de registo? ");
        rmiRegPortNumb = GenericIO.readlnInt();

        /* instanciação e instalação do gestor de segurança */
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        GenericIO.writelnString("Security manager was installed!");

        /* instanciação do objecto remoto que representa o Repositorio Geral e geração de um stub para ele */
        monGeral geral = new monGeral(3, 3, 300, 2, "log");
        GeralInterface geralInter = null;
        int listeningPort = 22321;

        try {
            geralInter = (GeralInterface) UnicastRemoteObject.exportObject(geral, listeningPort);
        } catch (RemoteException e) {
            GenericIO.writelnString("Excepção na geração do stub para o Rpeositorio Geral: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        GenericIO.writelnString("O stub para o repositorio geral foi gerado!");

        /* seu registo no serviço de registo RMI */
        String nameEntryBase = "RegisterHandler";
        String nameEntry = "GeralEst";
        Registry registry = null;
        Register reg = null;

        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException e) {
            GenericIO.writelnString("RMI registry creation exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        GenericIO.writelnString("RMI registry was created!");

        try {
            reg = (Register) registry.lookup(nameEntryBase);
        } catch (RemoteException e) {
            GenericIO.writelnString("RegisterRemoteObject lookup exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            GenericIO.writelnString("RegisterRemoteObject not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        try {
            reg.bind(nameEntry, geralInter);
        } catch (RemoteException e) {
            GenericIO.writelnString("Excepção no registo do Repositorio Geral: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (AlreadyBoundException e) {
            GenericIO.writelnString("O Repositorio Geral já está registado: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        GenericIO.writelnString("O Repositorio Geral foi registado!\nPort: "+listeningPort+"\n");

        
        while (geral.getFlag() == 0) {
            try {
                sleep((long) (3000));
            } catch (InterruptedException e) {
            }
        }
        GenericIO.writelnString("O Repositorio Geral vai encerrar");
        
        try{
            reg.unbind(nameEntry);
        } catch (RemoteException ex) {
            GenericIO.writelnString("Excepção ao encerrar do Repositorio Geral: " + ex.getMessage());
        } catch (NotBoundException ex) {
            GenericIO.writelnString("Excepção ao encerrar do Repositorio Geral: " + ex.getMessage());
        }
        
        try {
            reg.shutdown();
        } catch (RemoteException ex) {
            Logger.getLogger(runGeral.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.exit(0);
    }

}
