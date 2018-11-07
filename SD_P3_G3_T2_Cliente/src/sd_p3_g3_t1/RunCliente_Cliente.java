package sd_p3_g3_t1;

import Entidades.Cliente;
import genclass.GenericIO;

/**
 *Programa para a execuçao dos clientes (clientes).
 * @author rofler
 */
public class RunCliente_Cliente {
    
    public static void main(String[] args) {
        int nClientes = 3;                                   // número de clientes
        Cliente [] clientes = new Cliente[nClientes];        // array de threads cliente
        String serverHostNameGeral = "l040101-ws01.ua.pt", serverHostNameOficina = "l040101-ws03.ua.pt", serverHostNameLoja = "l040101-ws04.ua.pt", serverHostNameArmazemPM = "l040101-ws05.ua.pt";                        // nome do sistema computacional onde está o servidor
        int serverPortNumbGeral = 22320, serverPortNumbLoja = 22323;                                  // número do port de escuta do servidor

        /* Criação dos threads cliente */
        for (int i = 0; i < nClientes; i++) {
            clientes[i] = new Cliente(i, serverHostNameGeral, serverPortNumbGeral, serverHostNameLoja, serverPortNumbLoja);
        }
        /* Arranque da simulação */
        for (int i = 0; i < nClientes; i++) {
            clientes[i].start();
        }
        System.out.println("clientes iniciaram");
        
        /* Aguardar o fim da simulação */
        GenericIO.writelnString();
        for (int i = 0; i < nClientes; i++) {
            try {
                clientes[i].join();
            } catch (InterruptedException e) {
            }
            GenericIO.writelnString("O cliente " + i + " terminou.");
        }
        GenericIO.writelnString();
        
    }
}
