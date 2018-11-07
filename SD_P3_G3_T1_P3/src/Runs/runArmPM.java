/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Runs;

import Monitores.monArmazemPM;
import Monitores.monLoja;
import Monitores.monOficina;
import ServerInterface.ArmazemPMInterface;
import ServerInterface.GeralInterface;
import ServerInterface.LojaInterface;
import ServerInterface.OficinaInterface;
import genclass.GenericIO;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author rofler
 */
public class runArmPM {
    public static void main(String[] args) {

        String rmiRegHostNameGeral, rmiRegHostName;
        int rmiRegPortNumbGeral, rmiRegPortNumb;

        GenericIO.writeString("Nome do nó de processamento onde está localizado o serviço de registo? ");
        rmiRegHostName = GenericIO.readlnString();
        GenericIO.writeString("Número do port de escuta do serviço de registo? ");
        rmiRegPortNumb = GenericIO.readlnInt();

        /* instanciação e instalação do gestor de segurança */
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        /* obtenção da localização do serviço de registo RMI */
        GenericIO.writeString("Nome do nó de processamento onde está localizado o serviço de registo do Repositorio Geral? (apenas numero da maquina)");
        rmiRegHostNameGeral = "ld040101-ws" + GenericIO.readlnString();
        GenericIO.writeString("Número do port de escuta do serviço de registo do rep Geral? ");
        rmiRegPortNumbGeral = GenericIO.readlnInt();

        String nameEntryGeral = "GeralEst";
        GeralInterface geralInter = null;             // interface da barbearia (objecto remoto)

        try {
            Registry registry = LocateRegistry.getRegistry(rmiRegHostNameGeral, rmiRegPortNumbGeral);
            geralInter = (GeralInterface) registry.lookup(nameEntryGeral);
        } catch (RemoteException e) {
            GenericIO.writelnString("Excepção na localização do Repositorio Geral: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            GenericIO.writelnString("O Repositorio Geral não está registado: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        /* instanciação do objecto remoto que representa o Repositorio Geral e geração de um stub para ele */
        monArmazemPM armPM = null;
        ArmazemPMInterface armPMInter = null;

        armPM = new monArmazemPM(300, 0, 0, geralInter);
        try {
            armPMInter = (ArmazemPMInterface) UnicastRemoteObject.exportObject(armPM, 22324);
        } catch (RemoteException e) {
            GenericIO.writelnString("Excepção na geração do stub para o Armazem PM: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        GenericIO.writelnString("O stub para o ArmazemPM foi gerado!");

        /* seu registo no serviço de registo RMI */
        String nameEntry = "ArmazemPMEst";
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
            registry.bind(nameEntry, armPMInter);
        } catch (RemoteException e) {
            GenericIO.writelnString("Excepção no registo dao Armazem PM: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (AlreadyBoundException e) {
            GenericIO.writelnString("O Armazem PM já está registado: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        GenericIO.writelnString("O Armazem PM foi registada!");
    }
}
