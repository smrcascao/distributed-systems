package sd_p3_g3_t1;

import Entidades.Artesao;
import genclass.GenericIO;

/**
 *Programa para a execuçao dos clientes (artesaos).
 * @author rofler
 */
public class RunCliente_Artesao {
    
    public static void main(String[] args) {
        int nArtesaos = 3;                                     // número máximo de artesaos
        Artesao [] artesaos = new Artesao[nArtesaos];            // array de threads artesao
        String serverHostNameGeral = "l040101-ws01.ua.pt", serverHostNameOficina = "l040101-ws03.ua.pt", serverHostNameLoja = "l040101-ws04.ua.pt", serverHostNameArmazemPM = "l040101-ws05.ua.pt";                        // nome do sistema computacional onde está o servidor
        int serverPortNumbGeral = 22320, serverPortNumbOficina = 22321;                                  // número do port de escuta do servidor

        /* Criação dos threads artesao*/
        for (int i = 0; i < nArtesaos; i++) {
            artesaos[i] = new Artesao(i, serverHostNameGeral, serverPortNumbGeral, serverHostNameOficina, serverPortNumbOficina);
        }
        
        /* Arranque da simulação */
        for (int i = 0; i < nArtesaos; i++) {
            artesaos[i].start();
        }
        System.out.println("artesaos iniciaram");

        for (int i = 0; i < nArtesaos; i++) {
            try {
                artesaos[i].join();
            } catch (InterruptedException e) {
            }
            GenericIO.writelnString("o artesao " + i + " terminou.");
        }
        GenericIO.writelnString();

    }
}
