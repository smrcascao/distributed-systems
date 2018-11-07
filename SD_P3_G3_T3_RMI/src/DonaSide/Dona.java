package DonaSide;

import ServerInterface.ArmazemPMInterface;
import ServerInterface.GeralInterface;
import ServerInterface.LojaInterface;
import ServerInterface.OficinaInterface;
import States.SDona;
import VectorTime.VectorTime;
import genclass.GenericIO;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Entidade activa Dona. A dona interage com todas as regioes partilhadas.
 * Atende clientes na loja, vai buscar produtos a oficina quando chamada pelos
 * artesaos ou repoe a quantidade de pm na oficina. é a ultima entidade a
 * morrer.
 *
 * @author rofler
 *
 */
public class Dona extends Thread {

    /**
     * estado actual da dona
     *
     * @serial estadoDona
     */
    private int estadoDona;

    /**
     * interface de comunicaçao com o Repositorio Geral
     *
     * @serial dona
     */
    private GeralInterface dona;

    /**
     * interface de comunicaçao com a Loja
     *
     * @serial donaLoja
     */
    private LojaInterface donaLoja;

    /**
     * interface de comunicaçao com a Oficina
     *
     * @serial donaOficina
     */
    private OficinaInterface donaOficina;

    /**
     * interface de comunicaçao com o ArmazemPM
     *
     * @serial donaArmazemPM
     */
    private ArmazemPMInterface donaArmazemPM;

    /**
     * Relogio Vectorial
     *
     * @serial vecTime
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
                estadoDona = SDona.WAITING_FOR_NEXT_TASK;
                vecTime.increments();
                vecTime.update(donaLoja.prepareToWork(vecTime.getCopy()));
                

                canGoOut = false;
                while (!canGoOut) {
                    sit = donaLoja.appraiseSit();
                    switch (sit) {
                        case 2: //caso tenha de atender cliente

                            estadoDona = SDona.ATTENDIND_A_CUSTOMER;
                            vecTime.increments();
                            idCliente = donaLoja.addressACustomer(vecTime.getCopy());

                            serviceCustomer();

                            estadoDona = SDona.WAITING_FOR_NEXT_TASK;
                            vecTime.increments();
                            vecTime.update(donaLoja.sayGoodbyeToCustomer(idCliente, vecTime.getCopy()));

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

                estadoDona = SDona.CLOSING_THE_SHOP;
                vecTime.increments();
                vecTime.update(donaLoja.prepareToLeave(vecTime.getCopy()));

                if (sit == 3) {

                    estadoDona = SDona.COLLECTING_A_BATCH_OF_PRODUCTS;
                    vecTime.increments();
                    vecTime.update(donaOficina.goToWorkShop(vecTime.getCopy()));

                } else if (sit == 4) {

                    estadoDona = SDona.BUYING_PRIME_MATERIALS;
                    vecTime.increments();
                    pm = donaArmazemPM.visitSuppliers(vecTime.getCopy());

                    estadoDona = SDona.DELIVERING_PRIME_MATERIALS;
                    vecTime.increments();
                    vecTime.update(donaOficina.replenishStock(pm, vecTime.getCopy()));
                }
                
                estadoDona = SDona.OPENING_THE_SHOP;
                vecTime.increments();
                vecTime.update(donaLoja.returnToShop(vecTime.getCopy()));

            } while (!dona.endOpDona());

        } catch (RemoteException e) {
            GenericIO.writelnString("Excepção na invocação remota de método sobre a regiao partilhada pela dona "
                    + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Dona.class.getName()).log(Level.SEVERE, null, ex);
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
