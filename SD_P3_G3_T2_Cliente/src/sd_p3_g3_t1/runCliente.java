package sd_p3_g3_t1;

import Entidades.Artesao;
import Entidades.Cliente;
import Entidades.Dona;
import genclass.GenericIO;

/**
 *Programa de teste para a execuçao dos clientes (artesaos, clientes e dona).
 * @author rofler
 */
public class runCliente {
    
    public static void main(String[] args) {
        int nClientes = 3;                                   // número de clientes
        int nArtesaos = 3;                                     // número máximo de barbeiros
        Cliente [] clientes = new Cliente[nClientes];        // array de threads cliente
        Artesao [] artesaos = new Artesao[nArtesaos];            // array de threads artesao
        Dona dona;
        String serverHostNameGeral = "geral", serverHostNameOficina = "oficina", serverHostNameLoja = "loja", serverHostNameArmazemPM = "armazemPM";                        // nome do sistema computacional onde está o servidor
        int serverPortNumbGeral = 4000, serverPortNumbOficina = 4001, serverPortNumbLoja = 4002, serverPortNumbArmazemPM = 4003;                                  // número do port de escuta do servidor

        /* Criação dos threads artesao. cliente e dona */
        for (int i = 0; i < nArtesaos; i++) {
            artesaos[i] = new Artesao(i, serverHostNameGeral, serverPortNumbGeral, serverHostNameOficina, serverPortNumbOficina);
        }
        for (int i = 0; i < nClientes; i++) {
            clientes[i] = new Cliente(i, serverHostNameGeral, serverPortNumbGeral, serverHostNameLoja, serverPortNumbLoja);
        }
        dona = new Dona(serverHostNameGeral, serverPortNumbGeral, serverHostNameLoja, serverPortNumbLoja, serverHostNameOficina, serverPortNumbOficina, serverHostNameArmazemPM, serverPortNumbArmazemPM);
        
        /* Arranque da simulação */
        for (int i = 0; i < nArtesaos; i++) {
            artesaos[i].start();
        }
        System.out.println("artesaos iniciaram");
        for (int i = 0; i < nClientes; i++) {
            clientes[i].start();
        }
        System.out.println("clientes iniciaram");
        dona.start();
        System.out.println("dona iniciou");

        for (int i = 0; i < nArtesaos; i++) {
            try {
                artesaos[i].join();
            } catch (InterruptedException e) {
            }
            GenericIO.writelnString("o artesao " + i + " terminou.");
        }
        GenericIO.writelnString();
        
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
        
        
        
        try {
            dona.join();
        } catch (InterruptedException e) {
            GenericIO.writelnString("erro");
        }
        GenericIO.writelnString("a thread dona terminou.");
        GenericIO.writelnString();
        
        dona.shutdown();
    }
}
