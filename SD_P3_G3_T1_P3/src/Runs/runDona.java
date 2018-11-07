/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Runs;

import Entidades.Dona;
import ServerInterface.ArmazemPMInterface;
import ServerInterface.GeralInterface;
import ServerInterface.LojaInterface;
import ServerInterface.OficinaInterface;
import genclass.GenericIO;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author rofler
 */
public class runDona {

    public static void main(String[] args) {
        Dona dona;

        String rmiRegHostNameGeral, rmiRegHostNameLoja, rmiRegHostNameOficina, rmiRegHostNameArmazemPM;                                // nome do sistema onde está localizado o serviço de registos RMI do rep geral
        int rmiRegPortNumbGeral, rmiRegPortNumbLoja, rmiRegPortNumbOficina, rmiRegPortNumbArmazemPM;                                   // port de escuta do serviço do rep geral

        /* obtenção da localização do serviço de registo RMI */
        GenericIO.writeString("Nome do nó de processamento onde está localizado o serviço de registo do Repositorio Geral? (apenas numero da maquina)");
        rmiRegHostNameGeral = "ld040101-ws" + GenericIO.readlnString();
        GenericIO.writeString("Número do port de escuta do serviço de registo do rep Geral? ");
        rmiRegPortNumbGeral = GenericIO.readlnInt();

        GenericIO.writeString("Nome do nó de processamento onde está localizado o serviço de registo da Loja? (apenas numero da maquina)");
        rmiRegHostNameLoja = "ld040101-ws" + GenericIO.readlnString();
        GenericIO.writeString("Número do port de escuta do serviço de registo da Loja? ");
        rmiRegPortNumbLoja = GenericIO.readlnInt();

        GenericIO.writeString("Nome do nó de processamento onde está localizado o serviço de registo da Oficina? (apenas numero da maquina)");
        rmiRegHostNameOficina = "ld040101-ws" + GenericIO.readlnString();
        GenericIO.writeString("Número do port de escuta do serviço de registo da Oficina? ");
        rmiRegPortNumbOficina = GenericIO.readlnInt();

        GenericIO.writeString("Nome do nó de processamento onde está localizado o serviço de registo do Armazem PM? (apenas numero da maquina)");
        rmiRegHostNameArmazemPM = "ld040101-ws" + GenericIO.readlnString();
        GenericIO.writeString("Número do port de escuta do serviço de registo do Armazem PM? ");
        rmiRegPortNumbArmazemPM = GenericIO.readlnInt();

        /* localização por nome do objecto remoto no serviço de registos RMI */
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

        String nameEntryLoja = "LojaEst";
        LojaInterface lojaInter = null;             // interface da barbearia (objecto remoto)

        try {
            Registry registry = LocateRegistry.getRegistry(rmiRegHostNameGeral, rmiRegPortNumbGeral);
            lojaInter = (LojaInterface) registry.lookup(nameEntryLoja);
        } catch (RemoteException e) {
            GenericIO.writelnString("Excepção na localização da Loja: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            GenericIO.writelnString("A loja não está registada: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        String nameEntryOficina = "OficinaEst";
        OficinaInterface oficinaInter = null;             // interface da barbearia (objecto remoto)

        try {
            Registry registry = LocateRegistry.getRegistry(rmiRegHostNameGeral, rmiRegPortNumbGeral);
            oficinaInter = (OficinaInterface) registry.lookup(nameEntryOficina);
        } catch (RemoteException e) {
            GenericIO.writelnString("Excepção na localização da Loja: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            GenericIO.writelnString("A loja não está registada: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        String nameEntryArmazemPM = "ArmazemPMEst";
        ArmazemPMInterface armPMInter = null;             // interface da barbearia (objecto remoto)

        try {
            Registry registry = LocateRegistry.getRegistry(rmiRegHostNameGeral, rmiRegPortNumbGeral);
            armPMInter = (ArmazemPMInterface) registry.lookup(nameEntryArmazemPM);
        } catch (RemoteException e) {
            GenericIO.writelnString("Excepção na localização do Armazem PM: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            GenericIO.writelnString("O Armazem PM não está registado: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        /* criaçao da thread*/
        dona = new Dona(geralInter, lojaInter, oficinaInter, armPMInter);

        /* arranque da simulação */
        dona.start();

        /* aguardar o fim da simulação */
        GenericIO.writelnString();
        try {
            dona.join();
        } catch (InterruptedException e) {
        }
        GenericIO.writelnString("A dona terminou.");

        GenericIO.writelnString();

    }

}
