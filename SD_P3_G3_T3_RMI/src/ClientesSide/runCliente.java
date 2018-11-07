package ClientesSide;

import ServerInterface.GeralInterface;
import ServerInterface.LojaInterface;
import genclass.GenericIO;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *  Este tipo de dados gera o Problema Obrigatorio 3 do lado do cliente (Clientes) (acesso às Regioes Partilhadas como
 *  objectos remotos).
 *  Solução do Problema Obrigatorio 3 que implementa o modelo cliente-servidor de tipo 2 (replicação do
 *  servidor).
 */
public class runCliente {

    public static void main(String[] args) {

        int nCustomer = 3;                                  // Número de clientes que frequentam a Loja
        Cliente[] clientes = new Cliente[nCustomer];        // array de threads cliente

        String rmiRegHostName;                                // nome do sistema onde está localizado o serviço de registos RMI 
        int rmiRegPortNumb;                                   // port de escuta do serviço 


        GenericIO.writeString("Nome do nó de processamento onde está localizado o serviço de registo? (Apenas o numero da maquina) ");
        rmiRegHostName = "l040101-ws"+GenericIO.readlnString()+".ua.pt";
        GenericIO.writeString("Número do port de escuta do serviço de registo? ");
        rmiRegPortNumb = GenericIO.readlnInt();

        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException e) {
            GenericIO.writelnString("RMI registry creation exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        
        
        /* localização por nome do objecto remoto no serviço de registos RMI */
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

        String nameEntryLoja = "LojaEst";
        LojaInterface lojaInter = null;             // interface da barbearia (objecto remoto)

        try {
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

        /* criaçao da thread*/
        for (int i = 0; i < nCustomer; i++) {
            clientes[i] = new Cliente(i, geralInter, lojaInter);
        }

        /* arranque da simulação */
        for (int i = 0; i < nCustomer; i++) {
            clientes[i].start();
        }

        /* aguardar o fim da simulação */
        for (int i = 0; i < nCustomer; i++) {
            try {
                clientes[i].join();
            } catch (InterruptedException e) {
            }
            GenericIO.writelnString("o cliente " + i + " terminou.");
        }
        GenericIO.writelnString();

    }
}
