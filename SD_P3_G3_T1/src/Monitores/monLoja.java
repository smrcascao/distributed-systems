/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Monitores;

import Interfaces.*;
import States.SCliente;
import States.SDona;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 *
 * @author rofler
 */
public class monLoja implements intClienteLoja, intDonaLoja{
    private int nProdutosLoja;
    private int nTotalProdutosVendidos;
    private int nTotalProdutosVendidosPorDia;
    private int precoProduto; /*preço de cada produto*/
    private int nMaximoProdutosLoja; /*Numero maximo de produtos na loja*/
    private boolean portaAberta; /*0- porta fechada
                                  1- porta Aberta*/
    private int nClientesLoja;/*Numero de clientes dentro da loja*/
    private Queue<Integer> filaClientes;
    
    
    public monLoja(int nMaximoProdutosLoja, int precoProduto) {
        
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
        
    }
    /*funcoes do cliente*/
    
    @Override
    public void goShopping(int idCliente,  intClienteGeral cliente){
        cliente.setEstadoCliente(idCliente, SCliente.CHECKING_DOOR_OPEN);
    }
    
    
    @Override
    public synchronized boolean isDoorOpen(int idCliente, intClienteGeral cliente){
        return this.portaAberta;
    }
    
    @Override
    public synchronized void enterShop(int idCliente, intClienteGeral cliente){
        nClientesLoja++;
        cliente.setEstadoCliente(idCliente, SCliente.APPRAISING_OFFER_IN_DISPLAY);
    }
    
    @Override
    public boolean perusingAround() {
        Random espera = new Random();
        return espera.nextInt(100) >= 50;
    }
    
    @Override
    public synchronized void exitShop(int idCliente, intClienteGeral cliente) {
        this.nClientesLoja--;
        cliente.setEstadoCliente(idCliente, SCliente.CARRYING_OUT_DAILY_CHORES);
        if(nClientesLoja == 0)
            notifyAll();
    }
    
    @Override
    public synchronized void iWantThis(int idCliente, intClienteGeral cliente) {
        cliente.setEstadoCliente(idCliente, SCliente.BUYING_SOME_GOODS);
        filaClientes.add(idCliente);
        
    }
    
    
    /*funcoes da dona*/
    
    @Override
    public synchronized int addressACustomer(intDonaGeral dona)
    {
        dona.setEstadoDona(SDona.ATTENDIND_A_CUSTOMER);
        return filaClientes.poll();
    }
    
    @Override
    public synchronized void sayGoodbyeToCustomer(intDonaGeral dona, int idCliente)
    {
        dona.setEstadoDona(SDona.WAITING_FOR_NEXT_TASK);
        notifyAll();
    }
    
    /*@Override
    public synchronized boolean serviceCustomer()
    {
        Random espera = new Random();
        return espera.nextInt(100) >= 50;   
    }*/
    
    @Override
    public synchronized void prepareToWork(intDonaGeral dona)
    {
        dona.setEstadoDona(SDona.WAITING_FOR_NEXT_TASK);
        this.nProdutosLoja+=dona.getProdutosComDona();
        
    }
    
    @Override
    public synchronized int appraiseSit(intDonaGeral dona)
    {
        if(!this.filaClientes.isEmpty())
            return 2;
        else if(dona.isDonaChamadaPM())
            return 4;
        else if(dona.isDonaChamadaProdutos())
            return 3;
        else
            return 1;
    }
    
    @Override
    public synchronized boolean customersInTheShop()
    {
        return(this.nClientesLoja!=0);
    }
    
    @Override
    public synchronized void closeTheDoor()
    {
        this.portaAberta=false;
    }
    
    
    @Override
    public synchronized void prepareToLeave(intDonaGeral dona)
    {
        dona.setEstadoDona(SDona.CLOSING_THE_SHOP);
    }
    
    @Override
    public synchronized void goToWorkShop(intDonaGeral dona)
    {
        dona.setEstadoDona(SDona.COLLECTING_A_BATCH_OF_PRODUCTS);
    }
    
    @Override
    public synchronized void returnToShop(intDonaGeral dona)
    {
        dona.setEstadoDona(SDona.OPENING_THE_SHOP);        
    }
    
}
