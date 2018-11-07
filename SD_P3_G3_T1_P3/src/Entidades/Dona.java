package Entidades;

import ServerInterface.ArmazemPMInterface;
import ServerInterface.GeralInterface;
import ServerInterface.LojaInterface;
import ServerInterface.OficinaInterface;
import States.SDona;
import VectorTime.VectorTime;
import genclass.GenericIO;
import java.rmi.RemoteException;

/**
 *Entidade activa Dona.
 * A dona interage com todas as regioes partilhadas.
 * Atende clientes na loja, vai buscar produtos a oficina quando chamada pelos artesaos ou repoe a quantidade de pm na oficina.
 * é a ultima entidade a morrer.
 * @author rofler
 **/
public class Dona extends Thread {

    /**
     * estado actual da dona
     *
     * @serialField estadoDona
     */
    private SDona estadoDona;

    /**
     * interface de comunicaçao com o Repositorio Geral
     *
     * @serialField dona
     */
    private GeralInterface dona;

    /**
     * interface de comunicaçao com a Loja
     *
     * @serialField donaLoja
     */
    private LojaInterface donaLoja;

    /**
     * interface de comunicaçao com a Oficina
     *
     * @serialField donaOficina
     */
    private OficinaInterface donaOficina;

    /**
     * interface de comunicaçao com o ArmazemPM
     *
     * @serialField donaArmazemPM
     */
    private ArmazemPMInterface donaArmazemPM;
    
    /**
     * Relogio Vectorial
     *
     * @serialField vecTime
     */
    private VectorTime vecTime;

    /**
     *
     * Construtor.
     *
     * @param dona interface de comunicaçao com o repositorio geral
     * @param donaLoja interface de comunicaçao com a Loja
     * @param donaOficina interface de comunicaçao com a Oficina
     * @param donaArmazemPM interface de comunicaçao com o ArmazemPM
     *
     */
    public Dona(GeralInterface dona, LojaInterface donaLoja, OficinaInterface donaOficina, ArmazemPMInterface donaArmazemPM) {
        this.dona = dona;
        this.donaArmazemPM = donaArmazemPM;
        this.donaLoja = donaLoja;
        this.donaOficina = donaOficina;
        this.estadoDona = SDona.OPENING_THE_SHOP;
        this.vecTime = new VectorTime(7, 0);
    }

    /**
     * funcao que executa o ciclo de vida da dona.
     */
    @Override
    public void run() {

        boolean canGoOut;
        int sit = 0, idCliente, pm;
        try {
            
            do {
                donaLoja.prepareToWork();
                estadoDona = SDona.WAITING_FOR_NEXT_TASK;
                canGoOut = false;

                while (!canGoOut) {
                    sit = donaLoja.appraiseSit();
                    switch (sit) {
                        case 2: //caso tenha de atender cliente
                            idCliente = donaLoja.addressACustomer();
                            estadoDona = SDona.ATTENDIND_A_CUSTOMER;
                            serviceCustomer();
                            donaLoja.sayGoodbyeToCustomer(idCliente);
                            estadoDona = SDona.WAITING_FOR_NEXT_TASK;
                            break;
                        case 3://caso tenha de ir buscar produtos a oficina
                        case 4://caso tenha de ir buscar PM ao armazem
                            donaLoja.closeTheDoor();
                            canGoOut = !donaLoja.customersInTheShop();
                            break;
                        case 1:

                            canGoOut = true;
                            break;
                        case 0:
                            canGoOut = false;
                            break;
                    }

                }
                donaLoja.prepareToLeave();
                estadoDona = SDona.CLOSING_THE_SHOP;

                if (sit == 3) {
                    donaOficina.goToWorkShop();
                    estadoDona = SDona.COLLECTING_A_BATCH_OF_PRODUCTS;
                } else if (sit == 4) {
                    pm = donaArmazemPM.visitSuppliers();
                    estadoDona = SDona.BUYING_PRIME_MATERIALS;
                    donaOficina.replenishStock(pm);
                    estadoDona = SDona.DELIVERING_PRIME_MATERIALS;
                }
                donaLoja.returnToShop();
                estadoDona = SDona.OPENING_THE_SHOP;

            } while (!dona.endOpDona());
            
        } catch (RemoteException e) {
            GenericIO.writelnString("Excepção na invocação remota de método sobre a regiao partilhada pela dona "
                    + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

    }

    /**
     * atende o cliente (operação interna).
     */
    private void serviceCustomer() {
        try {
            sleep((long) (1 + 1 * Math.random()));
        } catch (InterruptedException e) {
        }
    }

}
