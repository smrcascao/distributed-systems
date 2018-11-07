/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LojaSide;

import ServerInterface.GeralInterface;
import ServerInterface.LojaInterface;
import States.SCliente;
import States.SDona;
import VectorTime.VectorTime;
import genclass.GenericIO;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Estrutura de da Loja
 *
 *
 */
public class monLoja implements LojaInterface {

    //variaveis
    /**
     * Numero de produtos em exposicao na loja
     *
     * @serial nProdutosLoja
     */
    private int nProdutosLoja;

    /**
     * Numero total de produtos vendidos na loja
     *
     * @serial nTotalProdutosVendidos
     */
    private int nTotalProdutosVendidos;

    /**
     * Numero total de produtos vendidos na loja do dia (por usar)
     *
     * @serial nTotalProdutosVendidosPorDia
     */
    private int nTotalProdutosVendidosPorDia;

    /**
     * preço de cada produto
     *
     * @serial precoProduto
     */
    private int precoProduto;

    /**
     * Numero maximo de produtos na loja
     *
     * @serial nMaximoProdutosLoja
     */
    private int nMaximoProdutosLoja;

    /**
     * Estado da porta da loja (0- porta fechada ; 1- porta Aberta)
     *
     * @serial portaAberta
     *
     */
    private boolean portaAberta;

    /**
     * Numero de clientes dentro da loja
     *
     * @serial nClientesLoja
     */
    private int nClientesLoja;

    /**
     * fila de clientes a espera para comprar produtos
     *
     * @serial filaClientes
     */
    private Queue<Integer> filaClientes;

    /**
     * interface com o rep geral
     *
     * @serial geralInter
     */
    private GeralInterface geralInter;

    /**
     * Relogio Vectorial
     *
     * @serial vecTime
     */
    private VectorTime vecTime;

    /**
     * flag para shutdown
     *
     * @serial flag
     */
    private int flag;

    /**
     *
     * Construtor
     *
     * @param nMaximoProdutosLoja Limite maximo de produtos em exposicao na loja
     * @param precoProduto preco de cada produto
     * @param geralInter interface de comunicaçao com o repositorio geral
     *
     */
    public monLoja(int nMaximoProdutosLoja, int precoProduto, GeralInterface geralInter) {

        this.nClientesLoja = 0;
        this.filaClientes = new LinkedList();

        if (nMaximoProdutosLoja > 0) {
            this.nMaximoProdutosLoja = nMaximoProdutosLoja;
        }

        this.portaAberta = false;
        this.nProdutosLoja = 0;

        if (precoProduto > 0) {
            this.precoProduto = precoProduto;
        }

        this.nTotalProdutosVendidos = 0;
        this.nTotalProdutosVendidosPorDia = 0;

        this.geralInter = geralInter;

        this.vecTime = new VectorTime(7, -1);
    }

    /*funcoes do cliente*/
    /**
     *
     * establece a primenira interaccao do cliente com o loja
     *
     * @param idCliente id do cliente
     * @param v relogio vectorial
     * @return retorna a versao acualizada do relogio vectorial
     * @throws java.rmi.RemoteException excepcao para remote do rmi
     *
     */
    @Override
    public synchronized VectorTime goShopping(int idCliente, VectorTime v) throws RemoteException {

        vecTime.update(v);

        try {
            geralInter.setEstadoCliente(idCliente, SCliente.CHECKING_DOOR_OPEN, vecTime.getCopy());
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(monLoja.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            return vecTime.getCopy();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(monLoja.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     *
     * funcao usada pelo cliente para ver se a porta esta aberta
     *
     * @return true, se a porta esta aberta false, em caso contrário
     */
    @Override
    public synchronized boolean isDoorOpen() {
        return this.portaAberta;
    }

    /**
     *
     * Ciente entra na loja
     *
     * @param idCliente id do cliente
     * @param v Relogio vectorial 
     * @return retorna a versao acualizada do relogio vectorial
     * @throws java.rmi.RemoteException excepcao para remote do rmi
     *
     */
    @Override
    public synchronized VectorTime enterShop(int idCliente, VectorTime v) throws RemoteException {

        vecTime.update(v);

        nClientesLoja++;
        geralInter.clienteEntra();
        try {
            geralInter.setEstadoCliente(idCliente, SCliente.APPRAISING_OFFER_IN_DISPLAY, vecTime.getCopy());
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(monLoja.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            return vecTime.getCopy();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(monLoja.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     *
     * O cliente observa os produtos na loja
     *
     * @return true, se o cliente quer comprar um ou mais produtos false, em
     * caso contrário (se nao existirem produtos na loja, o return e sempre
     * false)
     */
    @Override
    public synchronized boolean perusingAround() {
        if (this.nProdutosLoja == 0) {
            return false;
        }
        Random espera = new Random();
        return espera.nextInt(100) >= 10;
    }

    /**
     *
     * Ciente sai da loja
     *
     * @param idCliente id do cliente
     * @param v Relogio vectorial 
     * @return retorna a versao acualizada do relogio vectorial
     * @throws java.rmi.RemoteException excepcao para remote do rmi
     *
     */
    @Override
    public synchronized VectorTime exitShop(int idCliente, VectorTime v) throws RemoteException {

        vecTime.update(v);

        this.nClientesLoja--;
        geralInter.clienteSai();
        try {
            geralInter.setEstadoCliente(idCliente, SCliente.CARRYING_OUT_DAILY_CHORES, vecTime.getCopy());
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(monLoja.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (nClientesLoja == 0) {
            notifyAll();
        }

        try {
            return vecTime.getCopy();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(monLoja.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    /**
     *
     * Ciente quer comprar produtos (o numero de produtos a comprar e calculado
     * aleatoriamente) e e inserido na fila de espera e fica a dormir ate ser
     * chamado pela dona
     *
     * @param idCliente id do cliente
     * @param v relogio vectorial
     * @return retorna a versao acualizada do relogio vectorial
     * @throws java.rmi.RemoteException excepcao para remote do rmi
     *
     */
    @Override
    public synchronized VectorTime iWantThis(int idCliente, VectorTime v) throws RemoteException {

        vecTime.update(v);

        Random rand = new Random();
        int produtosAComprar = 0;
        if (this.nProdutosLoja != 0) {
            while (produtosAComprar == 0) {
                produtosAComprar = rand.nextInt(this.nProdutosLoja + 1);
            }
            this.nProdutosLoja -= produtosAComprar;

            geralInter.setnProdutosNaLoja(this.nProdutosLoja);

            try {
                geralInter.setEstadoCliente(idCliente, SCliente.BUYING_SOME_GOODS, vecTime.getCopy());
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(monLoja.class.getName()).log(Level.SEVERE, null, ex);
            }
            filaClientes.add(idCliente);

            notifyAll();
            try {
                this.wait();
            } catch (InterruptedException ex) {

            }

            //System.out.println("\nO cliente " + idCliente + " comprou " + produtosAComprar + " produtos.\n Produtos em exposiçao na Loja: " + this.nProdutosLoja);
            geralInter.compraCliente(idCliente, produtosAComprar);
        }

        try {
            return vecTime.getCopy();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(monLoja.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     *
     * Ciente termina a interaccao com a loja sem nunca ter entrado
     *
     * @param idCliente id do cliente
     * @param v relogio vectorial
     * @return retorna a versao acualizada do relogio vectorial
     * @throws java.rmi.RemoteException excepcao para remote do rmi
     *
     */
    @Override
    public synchronized VectorTime tryAgainLater(int idCliente, VectorTime v) throws RemoteException {
        
        vecTime.update(v);
        
        try {
            geralInter.setEstadoCliente(idCliente, SCliente.CARRYING_OUT_DAILY_CHORES, vecTime.getCopy());
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(monLoja.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            return vecTime.getCopy();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(monLoja.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        
    }

    /*funcoes da dona*/
    /**
     *
     * A dona atende um cliente (inicio da interaccao)
     *
     * @param v relogio vectorial
     * @return id do cliente a servir
     * @throws java.rmi.RemoteException excepcao para remote do rmi
     *
     */
    @Override
    public synchronized int addressACustomer(VectorTime v) throws RemoteException {
        vecTime.update(v);

        try {
            geralInter.setEstadoDona(SDona.ATTENDIND_A_CUSTOMER, vecTime.getCopy());
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(monLoja.class.getName()).log(Level.SEVERE, null, ex);
        }
        return filaClientes.poll();
    }

    /**
     *
     * A dona despede-se do cliente (fim da interaccao)
     *
     * @param idCliente id do cliente
     * @param v relogio vectorial
     * @return retorna a versao acualizada do relogio vectorial
     * @throws java.rmi.RemoteException excepcao para remote do rmi
     *
     */
    @Override
    public synchronized VectorTime sayGoodbyeToCustomer(int idCliente, VectorTime v) throws RemoteException {
        vecTime.update(v);

        try {
            geralInter.setEstadoDona(SDona.WAITING_FOR_NEXT_TASK, vecTime.getCopy());
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(monLoja.class.getName()).log(Level.SEVERE, null, ex);
        }
        notifyAll();

        try {
            return vecTime.getCopy();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(monLoja.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     *
     * A dona prepara se para trabalhar (se traz produtos da oficina, estes sao
     * adicionados ao stock da loja).
     *
     * @param v relogio vectorial
     * @return retorna a versao acualizada do relogio vectorial
     * @throws java.rmi.RemoteException excepcao para remote do rmi
     */
    @Override
    public synchronized VectorTime prepareToWork(VectorTime v) throws RemoteException {
        vecTime.update(v);
        this.portaAberta = true;
        geralInter.setShopDoorStat("open");

        try {
            geralInter.setEstadoDona(SDona.WAITING_FOR_NEXT_TASK, vecTime.getCopy());
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(monLoja.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.nProdutosLoja += geralInter.getProdutosComDona();

        int aux = geralInter.getProdutosComDona();
        if (aux != 0) {
            this.nProdutosLoja += aux;
            //System.out.println("\n A dona traz "+aux+" produtos para a loja.\n Quantidade de produtos em disposiçao: "+this.nProdutosLoja);

        }

        geralInter.setnProdutosNaLoja(this.nProdutosLoja);

        try {
            this.wait();
        } catch (InterruptedException ex) {

        }

        try {
            return vecTime.getCopy();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(monLoja.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    /**
     *
     * A dona verifica se ha alguma tarefa que requer a sua accao
     *
     *
     * @return 1 se o ciclo de vida da dona deve terminar; 2 se a fila de
     * clientes nao esta vazia; 3 se a dona tiver sido chamada para ir buscar
     * produtos a ofcina; 4 se a dona tiver sido chamada para levar PM a
     * oficina; 0 para outros casos
     * @throws java.rmi.RemoteException excepcao para remote do rmi
     *
     */
    @Override
    public synchronized int appraiseSit() throws RemoteException {
        if (!this.filaClientes.isEmpty()) {
            return 2;
        } else if (geralInter.isDonaChamadaPM()) {
            return 4;
        } else if (geralInter.isDonaChamadaProdutos()) {
            return 3;
        } else if (geralInter.endOpDona()) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     *
     * funcao usada pela dona para ver se ha clientes na loja
     *
     * @return true se houverem clientes na loja false se nao houverem clientes
     * na loja
     *
     */
    @Override
    public synchronized boolean customersInTheShop() {
        return (this.nClientesLoja != 0);
    }

    /**
     *
     * funcao usada pela dona para fechar a porta da loja
     *
     *
     * @throws java.rmi.RemoteException excepcao para remote do rmi
     */
    @Override
    public synchronized void closeTheDoor() throws RemoteException {

        this.portaAberta = false;
        geralInter.setShopDoorStat("opdc");

    }

    /**
     *
     * funcao usada pela dona para abandonar a Loja
     *
     *
     * @param v relogio vectorial
     * @return retorna a versao acualizada do relogio vectorial
     * @throws java.rmi.RemoteException excepcao para remote do rmi
     */
    @Override
    public synchronized VectorTime prepareToLeave(VectorTime v) throws RemoteException {
        vecTime.update(v);

        try {
            geralInter.setEstadoDona(SDona.CLOSING_THE_SHOP, vecTime.getCopy());
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(monLoja.class.getName()).log(Level.SEVERE, null, ex);
        }
        geralInter.setShopDoorStat("clos");

        try {
            return vecTime.getCopy();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(monLoja.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    /**
     *
     * funcao usada pela dona para regressar a Loja
     *
     *
     * @param v relogio vectorial
     * @return retorna a versao acualizada do relogio vectorial
     * @throws java.rmi.RemoteException excepcao para remote do rmi
     */
    @Override
    public synchronized VectorTime returnToShop(VectorTime v) throws RemoteException {
        vecTime.update(v);

        try {
            geralInter.setEstadoDona(SDona.OPENING_THE_SHOP, vecTime.getCopy());
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(monLoja.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            return vecTime.getCopy();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(monLoja.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     *
     * shutdown do server.
     *
     */
    @Override
    public void shutdown() {
        this.flag = 1;
        // GenericIO.writelnString("O Repositorio Geral vai encerrar");
    }

    /**
     *
     * consulta da flag de shut down.
     *
     * @return 0 se nao deve terminar. 1 caso contrario.
     */
    public int getFlag() {
        //GenericIO.writelnString(this.flag+"\n");
        return flag;
    }

}
