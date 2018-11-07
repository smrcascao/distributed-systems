/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Monitores;

import ServerInterface.GeralInterface;
import ServerInterface.LojaInterface;
import States.SCliente;
import States.SDona;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 *
 * Estrutura de da Loja
 * 
 * 
 */
public class monLoja implements LojaInterface{
    
     //variaveis
    /**
    *  Numero de produtos em exposicao na loja
    *
    *    @serialField nProdutosLoja
    */
    private int nProdutosLoja;
    
    /**
    *  Numero total de produtos vendidos na loja
    *
    *    @serialField nTotalProdutosVendidos
    */
    private int nTotalProdutosVendidos;
    
    /**
    *  Numero total de produtos vendidos na loja do dia (por usar)
    *
    *    @serialField nTotalProdutosVendidosPorDia
    */
    private int nTotalProdutosVendidosPorDia;
    
    /**
    *  preço de cada produto
    *
    *    @serialField precoProduto
    */
    private int precoProduto; 
    
   /**
   *    Numero maximo de produtos na loja
   *
   *    @serialField nMaximoProdutosLoja
   */
    private int nMaximoProdutosLoja; 
    
   /**
   *  Estado da porta da loja (0- porta fechada ; 1- porta Aberta)
   *
   *    @serialField portaAberta
   *    
   */
    private boolean portaAberta; 
    
   /**
   *  Numero de clientes dentro da loja
   *
   *    @serialField nClientesLoja
   */
    private int nClientesLoja;
    
   /**
   *  fila de clientes a espera para comprar produtos
   *
   *    @serialField filaClientes
   */
    private Queue<Integer> filaClientes;
    
    /**
   *  interface com o rep geral
   *
   *    @serialField geralInter
   */
    private GeralInterface geralInter;
    
    
    /**
     *
     *  Construtor
     *
     * @param nMaximoProdutosLoja Limite maximo de produtos em exposicao na loja
     * @param precoProduto preco de cada produto
     *
     */
    public monLoja(int nMaximoProdutosLoja, int precoProduto, GeralInterface geralInter) {
        
        this.nClientesLoja=0;
        this.filaClientes= new LinkedList();
        
        if(nMaximoProdutosLoja>0)
            this.nMaximoProdutosLoja=nMaximoProdutosLoja;
        
        this.portaAberta=false;
        this.nProdutosLoja=0;
        
        if(precoProduto>0)
            this.precoProduto=precoProduto;
        
        this.nTotalProdutosVendidos=0;
        this.nTotalProdutosVendidosPorDia=0;
        
        this.geralInter=geralInter;
        
    }
    
    /*funcoes do cliente*/
    
    /**
     *
     *  establece a primenira interaccao do cliente com o loja
     *
     * @param idCliente id do cliente
     * @param cliente usado para alterar o estado do cliente no repositorio geral
     * 
     */
    public synchronized void goShopping(int idCliente) throws RemoteException{
        
        geralInter.setEstadoCliente(idCliente, SCliente.CHECKING_DOOR_OPEN);
    
    }
    
    
    /**
     *
     *  funcao usada pelo cliente para ver se a porta esta aberta
     *
     * @return   true, se a porta esta aberta
     *           false, em caso contrário 
     */
    public synchronized boolean isDoorOpen(){
        return this.portaAberta;
    }
    
    
    /**
     *
     *  Ciente entra na loja
     *
     * @param idCliente id do cliente
     * @param cliente usado para alterar o estado do cliente no repositorio geral
     * 
     */  
    public synchronized void enterShop(int idCliente) throws RemoteException{
        nClientesLoja++;
        geralInter.clienteEntra();
        geralInter.setEstadoCliente(idCliente, SCliente.APPRAISING_OFFER_IN_DISPLAY);
        
    }
    
    
    /**
     *
     *  O cliente observa os produtos na loja
     *
     * @return   true, se o cliente quer comprar um ou mais produtos
     *           false, em caso contrário (se nao existirem produtos na loja, o return e sempre false)
     */
    public synchronized boolean perusingAround() {
        if(this.nProdutosLoja==0)
            return false;
        Random espera = new Random();
        return espera.nextInt(100) >= 10;
    }
    
    
    /**
     *
     *  Ciente sai da loja
     *
     * @param idCliente id do cliente
     * @param cliente usado para alterar o estado do cliente no repositorio geral
     * 
     */  
    public synchronized void exitShop(int idCliente) throws RemoteException {
        this.nClientesLoja--;
        geralInter.clienteSai();
        geralInter.setEstadoCliente(idCliente, SCliente.CARRYING_OUT_DAILY_CHORES);
        if(nClientesLoja == 0)
            notifyAll();
    }
    
    
    /**
     *
     *  Ciente quer comprar produtos (o numero de produtos a comprar e calculado aleatoriamente) 
     *  e e inserido na fila de espera e fica a dormir ate ser chamado pela dona
     * 
     * @param idCliente id do cliente
     * @param cliente usado para alterar o estado do cliente no repositorio geral
     * 
     */  
    public synchronized void iWantThis(int idCliente) throws RemoteException {
        
        Random rand= new Random();
        int produtosAComprar= rand.nextInt(this.nProdutosLoja+1);
        this.nProdutosLoja-=produtosAComprar;
        
        geralInter.setnProdutosNaLoja(this.nProdutosLoja);
        
        
        
        geralInter.setEstadoCliente(idCliente, SCliente.BUYING_SOME_GOODS);
        filaClientes.add(idCliente);
        notifyAll();
        try {
            this.wait();
        } catch (InterruptedException ex) {
            
        }
        
        geralInter.compraCliente(idCliente, produtosAComprar);
    }
    
    
    /**
     *
     *  Ciente termina a interaccao com a loja sem nunca ter entrado 
     *
     * @param idCliente id do cliente
     * @param cliente usado para alterar o estado do cliente no repositorio geral
     * 
     */  
    public synchronized void tryAgainLater(int idCliente) throws RemoteException
    {
      geralInter.setEstadoCliente(idCliente, SCliente.CARRYING_OUT_DAILY_CHORES);
    }
    
    
    /*funcoes da dona*/
    
    /**
     *
     *  A dona atende um cliente (inicio da interaccao)
     *
     * @param dona usado para alterar o estado da dona no repositorio geral
     * 
     * @return id do cliente a servir  
     *  
     */
    public synchronized int addressACustomer() throws RemoteException
    {
        geralInter.setEstadoDona(SDona.ATTENDIND_A_CUSTOMER);
        return filaClientes.poll();
    }
    
    
    /**
     *
     *  A dona despede-se do cliente (fim da interaccao)
     *
     * @param idCliente id do cliente
     * @param dona usado para alterar o estado da dona no repositorio geral
     * 
     */
    public synchronized void sayGoodbyeToCustomer(int idCliente) throws RemoteException
    {
        geralInter.setEstadoDona(SDona.WAITING_FOR_NEXT_TASK);
        notifyAll();
    }
       
    
    /**
     *
     *  A dona despede-se do cliente (fim da interaccao)
     *
     * @param dona usado para alterar o estado da dona no repositorio geral
     * 
     */
    public synchronized void prepareToWork() throws RemoteException
    {
        this.portaAberta=true;
        geralInter.setShopDoorStat("open");
        geralInter.setEstadoDona(SDona.WAITING_FOR_NEXT_TASK);
        this.nProdutosLoja+=geralInter.getProdutosComDona();
        
        geralInter.setnProdutosNaLoja(this.nProdutosLoja);
        
        
        try {
            this.wait();
        } catch (InterruptedException ex) {
            
        }
    }
    
    /**
     *
     *  A dona atende um cliente (inicio da interaccao)
     *
     * @param dona usado para alterar o estado da dona no repositorio geral
     * 
     * @return 1 se o ciclo de vida da dona deve terminar;
     *         2 se a fila de clientes nao esta vazia; 
     *         3 se a dona tiver sido chamada para ir buscar produtos a ofcina;
     *         4 se a dona tiver sido chamada para levar PM a oficina; 
     *         0 para outros casos 
     *  
     */
    public synchronized int appraiseSit() throws RemoteException
    {
        if(!this.filaClientes.isEmpty())
            return 2;
        else if(geralInter.isDonaChamadaPM())
        {   
            return 4;
        }
        else if(geralInter.isDonaChamadaProdutos())
            return 3;
        else if(geralInter.endOpDona())
        {   
            return 1;
        }
        else
            return 0;
    }
    
    /**
     *
     *  funcao usada pela dona para ver se ha clientes na loja
     *
     * @return true se houverem clientes na loja
     *         false se nao houverem clientes na loja
     *  
     */
    public synchronized boolean customersInTheShop()
    {
        return(this.nClientesLoja!=0);
    }
    
    
    /**
     *
     *  funcao usada pela dona para fechar a porta da loja
     *
     * @param dona usado para alterar o repositorio geral
     * 
     */
    public synchronized void closeTheDoor() throws RemoteException
    {
        
        this.portaAberta=false;
        geralInter.setShopDoorStat("opdc");
        
    }
    
    
    /**
     *
     *  funcao usada pela dona para abandonar a Loja
     *
     * @param dona usado para alterar o repositorio geral
     * 
     */
    public synchronized void prepareToLeave() throws RemoteException
    {
        geralInter.setEstadoDona(SDona.CLOSING_THE_SHOP);
        geralInter.setShopDoorStat("clos");
    }
    
    
    /**
     *
     *  funcao usada pela dona para regressar a Loja
     *
     * @param dona usado para alterar o estado da dona no repositorio geral
     * 
     */   
    public synchronized void returnToShop() throws RemoteException
    {
        geralInter.setEstadoDona(SDona.OPENING_THE_SHOP);        
    }
    
}
