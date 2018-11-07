package ArtesaosSide;

import ServerInterface.GeralInterface;
import ServerInterface.OficinaInterface;
import States.SArtesao;
import VectorTime.VectorTime;
import genclass.GenericIO;
import static java.lang.Thread.sleep;

/**
 * Entidade activa Artesao. O artesao apenas interage com a oficina e o
 * repositorio geral. Faz produtos e alerta a Dona para a falta de PM ou excesso
 * de produtos no armazem
 *
 * @author rofler
 *
 */
public class Artesao extends Thread {

    /**
     * id do artesao
     *
     * @serial idArtesao
     */
    private int idArtesao;

    /**
     * estado actual do artesao
     *
     * @serial estadoArtesao
     */
    private int estadoArtesao;

    /**
     * Interface Remota ao Repositorio Geral
     *
     * @serial artesaoGeral
     */
    private GeralInterface artesaoGeral;

    /**
     * Intterface Remota a Oficina
     *
     * @serial artesaoOficina
     */
    private OficinaInterface artesaoOficina;

    /**
     * numero de produtos feitos pelo artesao
     *
     * @serial produtosFeitos
     */
    private int produtosFeitos;

    /**
     * Relogio Vectorial
     *
     * @serial vecTime
     */
    private VectorTime vecTime;

    /**
     *
     * Construtor
     *
     * @param idArtesao id do artesao
     * @param artesaoGeral interface ao repositorio geral
     * @param artesaoOficina interface a oficina
     *
     */
    public Artesao(int idArtesao, GeralInterface artesaoGeral, OficinaInterface artesaoOficina) {

        this.idArtesao = idArtesao;
        this.artesaoGeral = artesaoGeral;
        this.artesaoOficina = artesaoOficina;
        this.estadoArtesao = SArtesao.FETCHING_PRIME_MATERIALS;

        this.produtosFeitos = 0;

        this.vecTime = new VectorTime(7, idArtesao + 4);
    }

    /**
     * funcao que executa o ciclo de vida do artesao
     */
    public void run() {

        boolean canWork;
        int m = 1, ligaDona;
        try {

            do {
                canWork = false;
                estadoArtesao = SArtesao.FETCHING_PRIME_MATERIALS;
                vecTime.increments();

                while (!canWork) {
                   // estadoArtesao = SArtesao.FETCHING_PRIME_MATERIALS;
                    // vecTime.increments();
                    m = artesaoOficina.checkForMaterials(idArtesao, vecTime.getCopy());

                    switch (m) {
                        case (1):
                            canWork = true;
                            break;
                        case (2):
                            if (artesaoGeral.isDonaChamadaPM() == false && artesaoGeral.isArmPM()) {

                                estadoArtesao = SArtesao.CONTACTING_THE_ENTREPRENEUR;
                                vecTime.increments();
                                vecTime.update(artesaoOficina.primeMaterialsNeeded(idArtesao, vecTime.getCopy()));

                                estadoArtesao = SArtesao.FETCHING_PRIME_MATERIALS;
                                vecTime.increments();
                                artesaoOficina.backToWork(idArtesao);
                            }
                            canWork = true;
                            break;
                        case (3):
                            break;
                        case (0):
                            if (artesaoGeral.isDonaChamadaPM() == false && artesaoGeral.isArmPM()) {

                                estadoArtesao = SArtesao.CONTACTING_THE_ENTREPRENEUR;
                                vecTime.increments();
                                vecTime.update(artesaoOficina.primeMaterialsNeeded(idArtesao, vecTime.getCopy()));

                                estadoArtesao = SArtesao.FETCHING_PRIME_MATERIALS;
                                vecTime.increments();
                                artesaoOficina.backToWork(idArtesao);
                            }
                            break;

                        case (4):
                            canWork = true;
                            break;

                    }
                }

                if (m != 4) {
                    artesaoOficina.collectMaterials(idArtesao);

                    estadoArtesao = SArtesao.PRODUCING_A_NEW_PIECE;
                    vecTime.increments();
                    vecTime.update(artesaoOficina.prepareToProduce(idArtesao, vecTime.getCopy()));

                    shapingItUp();
                    produtosFeitos++;

                    estadoArtesao = SArtesao.STORING_IT_FOR_TRANSFER;
                    vecTime.increments();
                    ligaDona = artesaoOficina.goToStore(idArtesao, vecTime.getCopy());

                    if ((ligaDona == 2 || ligaDona == 0) && artesaoGeral.isDonaChamadaProdutos() == false) {
                        estadoArtesao = SArtesao.CONTACTING_THE_ENTREPRENEUR;
                        vecTime.increments();
                        vecTime.update(artesaoOficina.batchReadyForTransfer(idArtesao, vecTime.getCopy()));
                    }
                    artesaoOficina.backToWork(idArtesao);
                }

            } while (!artesaoGeral.endOpArtesao());

            if (artesaoGeral.isDonaChamadaProdutos() == false && m != 4) {
                estadoArtesao = SArtesao.CONTACTING_THE_ENTREPRENEUR;
                vecTime.increments();
                vecTime.update(artesaoOficina.batchReadyForTransfer(idArtesao, vecTime.getCopy()));
            }
        } catch (Exception e) {
            GenericIO.writelnString("Excepção na invocação remota de método sobre a regiao partilhada pelo Artesao "
                    + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * fabrica o produto (operação interna).
     */
    public void shapingItUp() {
        try {
            sleep((long) (1 + 10 * Math.random()));
        } catch (InterruptedException e) {
        }
    }

    /**
     * devolve o numero de produtos que o artesao fez
     *
     * @return numero de produtos que o artesao fez
     *
     */
    public int getProdutosFeitos() {
        return produtosFeitos;
    }

    @Override
    public String toString() {
        return "Artesao{" + "id=" + idArtesao + ", produtosFeitos=" + produtosFeitos + '}';
    }

}
