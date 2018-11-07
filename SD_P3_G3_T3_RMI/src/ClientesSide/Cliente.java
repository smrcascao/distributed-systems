package ClientesSide;

import ServerInterface.GeralInterface;
import ServerInterface.LojaInterface;
import States.SCliente;
import VectorTime.VectorTime;
import genclass.GenericIO;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Entidade activa Cliente. O Cliente apenas interage com a loja e o repositorio
 * geral. Compra produtos e morre depois do artesao e antes da dona
 *
 * @author rofler
 *
 */
public class Cliente extends Thread {

    /**
     * Identificação do cliente
     *
     * @serial customerId
     */
    private int idCliente;

    /**
     * estado actual do cliente
     *
     * @serial estadoCliente
     */
    private int estadoCliente;

    /**
     * interface ao Repositorio Geral
     *
     * @serial clientGeral
     */
    private GeralInterface clientGeral;

    /**
     * interface a Loja
     *
     * @serial clientLoja
     */
    private LojaInterface clientLoja;

    /**
     * Numero de compras efectuadas pelo cliente
     *
     * @serial cumCompras
     */
    private int numCompras;

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
     * @param idCliente id do cliente
     * @param clientGeral interface ao repositorio geral
     * @param clientLoja interface a Loja
     *
     */
    public Cliente(int idCliente, GeralInterface clientGeral, LojaInterface clientLoja) {

        this.numCompras = 0;
        this.idCliente = idCliente;
        this.clientGeral = clientGeral;
        this.clientLoja = clientLoja;
        this.estadoCliente = SCliente.CARRYING_OUT_DAILY_CHORES;
        this.vecTime = new VectorTime(7, idCliente + 1);
    }

    /**
     * funcao que executa o ciclo de vida do artesao
     */
    @Override
    public void run() {

        boolean portaAberta;
        try {

            do {
                livingNormalLife();
                this.estadoCliente = SCliente.CHECKING_DOOR_OPEN;
                vecTime.increments();
                vecTime.update(clientLoja.goShopping(idCliente, vecTime.getCopy()));
                portaAberta = clientLoja.isDoorOpen();

                if (portaAberta) {
                    this.estadoCliente = SCliente.APPRAISING_OFFER_IN_DISPLAY;
                    vecTime.increments();
                    vecTime.update(clientLoja.enterShop(idCliente, vecTime.getCopy()));

                    if (clientLoja.perusingAround()) {
                        this.estadoCliente = SCliente.BUYING_SOME_GOODS;
                        vecTime.increments();
                        vecTime.update(clientLoja.iWantThis(idCliente, vecTime.getCopy()));
                    }

                    this.estadoCliente = SCliente.CARRYING_OUT_DAILY_CHORES;
                    vecTime.increments();
                    vecTime.update(clientLoja.exitShop(idCliente, vecTime.getCopy()));
                } else {
                    this.estadoCliente = SCliente.CARRYING_OUT_DAILY_CHORES;
                    vecTime.increments();
                    vecTime.update(clientLoja.tryAgainLater(idCliente, vecTime.getCopy()));
                }

            } while (!clientGeral.endOpCliente());

        } catch (RemoteException e) {
            GenericIO.writelnString("Excepção na invocação remota de método sobre a regiao partilhada pelo cliente " + this.idCliente
                    + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * devolve o numero de produtos que o cliente comprou
     *
     * @return numero de produtos que o cliente comprou
     *
     */
    public int getNumCompras() {
        return numCompras;
    }

    /**
     * devolve o numero de identificacao do cliente
     *
     * @return numero de identificacao do cliente
     *
     */
    public int getIdCliente() {
        return idCliente;
    }

    /**
     * o cliente fica em espera um tempo aleatorio(operação interna).
     */
    public void livingNormalLife() {
        try {
            sleep((long) (1 + 40 * Math.random()));
        } catch (InterruptedException e) {
        }
    }

    @Override
    public String toString() {
        return "Cliente{" + ", numCompras=" + numCompras + ", idCliente=" + idCliente + '}';
    }

}
