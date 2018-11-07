/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import ServerInterface.GeralInterface;
import ServerInterface.LojaInterface;
import States.SCliente;
import genclass.GenericIO;
import java.rmi.RemoteException;

/**
 *
 * @author smrcascao
 */
public class Cliente extends Thread {

    /**
     * Identificação do cliente
     *
     * @serialField customerId
     */
    private int idCliente;

    /**
     * estado actual do cliente
     *
     * @serialField estadoCliente
     */
    private SCliente estadoCliente;

    /**
     * Repositorio Geral
     *
     * @serialField clientGeral
     */
    private GeralInterface clientGeral;

    /**
     * Loja
     *
     * @serialField clientLoja
     */
    private LojaInterface clientLoja;

    /**
     * Numero de compras efectuadas pelo cliente
     *
     * @serialField cumCompras
     */
    private int numCompras;

    /**
     *
     * Construtor
     *
     * @param idCliente id do cliente
     * @param clientGeral repositorio geral
     * @param clientLoja Loja
     *
     */
    public Cliente(int idCliente, GeralInterface clientGeral, LojaInterface clientLoja) {

        this.numCompras = 0;
        this.idCliente = idCliente;
        this.clientGeral = clientGeral;
        this.clientLoja = clientLoja;
        this.estadoCliente = SCliente.CARRYING_OUT_DAILY_CHORES;
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
                clientLoja.goShopping(idCliente);
                this.estadoCliente = SCliente.CHECKING_DOOR_OPEN;
                portaAberta = clientLoja.isDoorOpen();

                if (portaAberta) {
                    clientLoja.enterShop(idCliente);
                    this.estadoCliente = SCliente.APPRAISING_OFFER_IN_DISPLAY;

                    if (clientLoja.perusingAround()) {
                        clientLoja.iWantThis(idCliente);
                        this.estadoCliente = SCliente.BUYING_SOME_GOODS;
                    }

                    clientLoja.exitShop(idCliente);
                    this.estadoCliente = SCliente.CARRYING_OUT_DAILY_CHORES;
                } else {
                    clientLoja.tryAgainLater(idCliente);
                    this.estadoCliente = SCliente.CARRYING_OUT_DAILY_CHORES;
                }

            } while (!clientGeral.endOpCliente());

        } catch (RemoteException e) {
            GenericIO.writelnString("Excepção na invocação remota de método sobre a regiao partilhada pelo cliente "+ this.idCliente
                    + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
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
