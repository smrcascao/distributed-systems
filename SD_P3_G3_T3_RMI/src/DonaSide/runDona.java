package DonaSide;

import ServerInterface.ArmazemPMInterface;
import ServerInterface.GeralInterface;
import ServerInterface.LojaInterface;
import ServerInterface.OficinaInterface;
import genclass.GenericIO;
import static java.lang.Thread.sleep;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Este tipo de dados gera o Problema Obrigatorio 3 do lado do cliente (Dona)
 * (acesso às Regioes Partilhadas como objectos remotos). Solução do Problema
 * Obrigatorio 3 que implementa o modelo cliente-servidor de tipo 2 (replicação
 * do servidor).
 */
public class runDona {

    public static void main(String[] args) {
        Dona dona;

        String rmiRegHostName;                                // nome do sistema onde está localizado o serviço de registos RMI 
        int rmiRegPortNumb;                                   // port de escuta do serviço 

        GenericIO.writeString("Nome do nó de processamento onde está localizado o serviço de registo? (Apenas o numero da maquina) ");
        rmiRegHostName = "l040101-ws" + GenericIO.readlnString() + ".ua.pt";
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

        String nameEntryOficina = "OficinaEst";
        OficinaInterface oficinaInter = null;             // interface da barbearia (objecto remoto)

        try {
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

        try {
            sleep((long) (3000));
        } catch (InterruptedException e) {
        }

        try {
            armPMInter.shutdown();
            lojaInter.shutdown();
            oficinaInter.shutdown();
            try {
                sleep((long) (3000));
            } catch (InterruptedException e) {
            }
            geralInter.shutdown();
        } catch (RemoteException ex) {
            Logger.getLogger(runDona.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
