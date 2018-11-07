/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Runs;

import Entidades.Artesao;
import ServerInterface.GeralInterface;
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
public class runArtesao {

    public static void main(String[] args) {

        int nArts = 3;                                  // Número de artesaos que frequentam a Oficina
        Artesao[] artesaos = new Artesao[nArts];        // array de threads artesaos

        String rmiRegHostNameGeral, rmiRegHostNameOficina;                               // nome do sistema onde está localizado o serviço de registos RMI do rep geral
        int rmiRegPortNumbGeral, rmiRegPortNumbOficina;                                  // port de escuta do serviço do rep geral

        /* obtenção da localização do serviço de registo RMI */
        GenericIO.writeString("Nome do nó de processamento onde está localizado o serviço de registo do Repositorio Geral? (apenas numero da maquina)");
        rmiRegHostNameGeral = "ld040101-ws" + GenericIO.readlnString();
        GenericIO.writeString("Número do port de escuta do serviço de registo do rep Geral? ");
        rmiRegPortNumbGeral = GenericIO.readlnInt();

        GenericIO.writeString("Nome do nó de processamento onde está localizado o serviço de registo da Oficina? (apenas numero da maquina)");
        rmiRegHostNameOficina = "ld040101-ws" + GenericIO.readlnString();
        GenericIO.writeString("Número do port de escuta do serviço de registo da Oficina? ");
        rmiRegPortNumbOficina = GenericIO.readlnInt();

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

        String nameEntryOficina = "OficinaEst";
        OficinaInterface OficinaInter = null;             // interface da barbearia (objecto remoto)

        try {
            Registry registry = LocateRegistry.getRegistry(rmiRegHostNameGeral, rmiRegPortNumbGeral);
            OficinaInter = (OficinaInterface) registry.lookup(nameEntryOficina);
        } catch (RemoteException e) {
            GenericIO.writelnString("Excepção na localização da Oficina: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            GenericIO.writelnString("A Oficina não está registada: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        /* criaçao da thread*/
        for (int i = 0; i < nArts; i++) {
            artesaos[i] = new Artesao(i, geralInter, OficinaInter);
        }

        /* arranque da simulação */
        for (int i = 0; i < nArts; i++) {
            artesaos[i].start();
        }

        /* aguardar o fim da simulação */
        for (int i = 0; i < nArts; i++) {
            try {
                artesaos[i].join();
            } catch (InterruptedException e) {
            }
            GenericIO.writelnString("o artesao " + i + " terminou.");
        }
        GenericIO.writelnString();

    }
}
