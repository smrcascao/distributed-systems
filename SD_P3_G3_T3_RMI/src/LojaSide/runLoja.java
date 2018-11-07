/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LojaSide;

import ServerInterface.GeralInterface;
import ServerInterface.LojaInterface;
import ServerInterface.Register;
import genclass.GenericIO;
import static java.lang.Thread.sleep;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 *  Este tipo de dados gera o Problema Obrigatorio 3 do lado do servidor (instanciação da Loja como
 *  um objecto remoto)(acesso ao Repositorio Geral como um objecto remoto).
 *  Solução do Problema Obrigatorio 3 que implementa o modelo cliente-servidor de tipo 2 (replicação do
 *  servidor).
 */
public class runLoja {

    public static void main(String[] args) {

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

        String nameEntryBase = "RegisterHandler";
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

        
        String nameEntryGeral = "GeralEst";
        GeralInterface geralInter = null;             // interface da barbearia (objecto remoto)

        try {
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
        monLoja loja = new monLoja(150, 0, geralInter);
        LojaInterface lojaInter = null;
        int listeningPort = 22322;

        try {
            lojaInter = (LojaInterface) UnicastRemoteObject.exportObject(loja, listeningPort);
        } catch (RemoteException e) {
            GenericIO.writelnString("Excepção na geração do stub para a loja: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        GenericIO.writelnString("O stub para a loja foi gerado!");

        /* seu registo no serviço de registo RMI */
        String nameEntry = "LojaEst";

        try {
            reg.bind(nameEntry, lojaInter);
        } catch (RemoteException e) {
            GenericIO.writelnString("Excepção no registo da Loja: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (AlreadyBoundException e) {
            GenericIO.writelnString("A Loja já está registada: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        GenericIO.writelnString("A Loja foi registada!\nPort: "+listeningPort+"\n");

        
        while (loja.getFlag() == 0) {
            try {
                sleep((long) (3000));
            } catch (InterruptedException e) {
            }
        }
        GenericIO.writelnString("A loja vai encerrar");
        
        try{
            reg.unbind(nameEntry);
        } catch (RemoteException ex) {
            GenericIO.writelnString("Excepção ao encerrar do Repositorio Geral: " + ex.getMessage());
        } catch (NotBoundException ex) {
            GenericIO.writelnString("Excepção ao encerrar do Repositorio Geral: " + ex.getMessage());
        }
        
        System.exit(0);
    }
}
