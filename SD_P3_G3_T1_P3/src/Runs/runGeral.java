/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Runs;

import Monitores.monGeral;
import ServerInterface.GeralInterface;
import genclass.GenericIO;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author rofler
 */
public class runGeral {

    public static void main(String[] args) { //args-> 0=hostName, 1=portNumb, 2=totalPM, 3=prodPM, 4=logFileName

        String rmiRegHostName;                      // nome do sistema onde está localizado o serviço de registos RMI
        int rmiRegPortNumb;                         // port de escuta do serviço

        GenericIO.writeString("Nome do nó de processamento onde está localizado o serviço de registo? ");
        rmiRegHostName = GenericIO.readlnString();
        GenericIO.writeString("Número do port de escuta do serviço de registo? ");
        rmiRegPortNumb = GenericIO.readlnInt();

        /* instanciação e instalação do gestor de segurança */
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        /* instanciação do objecto remoto que representa o Repositorio Geral e geração de um stub para ele */
        monGeral geral = null;
        GeralInterface geralInter = null;

        geral = new monGeral(3, 3, 300, 2, "log");
        try {
            geralInter = (GeralInterface) UnicastRemoteObject.exportObject(geral, 22321);
        }
        catch (RemoteException e) {
            GenericIO.writelnString("Excepção na geração do stub para o Rpeositorio Geral: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        GenericIO.writelnString("O stub para o repositorio geral foi gerado!");

        /* seu registo no serviço de registo RMI */
        String nameEntry = "GeralEst";
        Registry registry = null;

        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException e) {
            GenericIO.writelnString("Excepção na criação do registo RMI: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        GenericIO.writelnString("O registo RMI foi criado!");

        try {
            registry.bind(nameEntry, geralInter);
        } catch (RemoteException e) {
            GenericIO.writelnString("Excepção no registo do Repositorio Geral: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (AlreadyBoundException e) {
            GenericIO.writelnString("O Repositorio Geral já está registado: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        GenericIO.writelnString("O Repositorio Geral foi registado!");

    }

}
