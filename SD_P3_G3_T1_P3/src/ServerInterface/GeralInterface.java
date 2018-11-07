/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerInterface;

import States.SArtesao;
import States.SCliente;
import States.SDona;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author rofler
 */
public interface GeralInterface extends Remote
{
    
    public void setArmPM(boolean armPM) throws RemoteException;
    
    public boolean isArmPM() throws RemoteException;
    
    public void pMGasta(int quantPM) throws RemoteException;
    
    public void novoFornecimentoPM(int quantPM) throws RemoteException;
    
    public void setShopDoorStat(String shopDoorStat) throws RemoteException;
    
    public void setnProdutosNaLoja(int nProdutosNaLoja) throws RemoteException;
    
    public void setnProdutosNaOficina(int nProdutosNaOficina) throws RemoteException;
    
    public void setnPMNaOficina(int nPMNaOficina) throws RemoteException;
    
    public void compraCliente(int idCliente, int numProdutos) throws RemoteException;
    
    public void artesaoFezProduto(int idArtesao) throws RemoteException;
    
    public void setEstadoDona(SDona estado) throws RemoteException;
    
    public void setEstadoCliente(int id, SCliente estado) throws RemoteException;
    
    public void clienteEntra() throws RemoteException;
    
    public void clienteSai() throws RemoteException;
    
    public void setEstadoArtesao(int id, SArtesao estado) throws RemoteException;
    
    public int getProdutosComDona() throws RemoteException;
    
    public void donaRecebeProdutos(int p) throws RemoteException;
    
    public boolean endOpDona() throws RemoteException;
    
    public boolean endOpCliente() throws RemoteException;
    
    public boolean endOpArtesao() throws RemoteException;
    
    public boolean isDonaChamadaPM() throws RemoteException;
    
    public boolean isDonaChamadaProdutos() throws RemoteException;
    
    public void setDonaChamadaPM(boolean donaChamadaPM) throws RemoteException;
    
    public void setDonaChamadaProdutos(boolean donaChamadaProdutos) throws RemoteException;
    
    
    
}
