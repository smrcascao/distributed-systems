/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ArmazemPMSide;

import ServerInterface.ArmazemPMInterface;
import ServerInterface.GeralInterface;
import States.SDona;
import VectorTime.VectorTime;
import genclass.GenericIO;
import java.rmi.RemoteException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Estrutura de dados Armazem de PM. Armazem ao qual a dona vai buscar PM para
 * levar para a Oficina
 *
 * Entidades Acitvas que interagem com a regiao partilhada: Dona
 *
 */
public class monArmazemPM implements ArmazemPMInterface {

    //variaveis
    /**
     * PM disponivel para venda
     *
     * @serial stockPM
     */
    private int stockPM;

    /**
     * preco da unidade de PM
     *
     * @serial precoPM
     */
    private int precoPM;

    /**
     * quantidade total de PM vendida
     *
     * @serial totalVendaPM
     */
    private int totalVendaPM;

    /**
     * Numero de vendas
     *
     * @serial vendasPM
     */
    private int vendasPM;

    //quantidade de PM por fornecimento
    /**
     * quantidade de PM por fornecimento
     *
     * @serial fornecimentoPM
     */
    private int fornecimentoPM;

    /**
     * PM para fazer u produto
     *
     * @serial prodPM
     */
    private int prodPM;

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
     * @param stockInicialPM PM disponivel para venda
     * @param preco preco da unidade de PM
     * @param PMPorFornecimento quantidade de PM por fornecimento
     * @param geralInter interface remota ao repositorio Geral
     * @param prodPM pm para produzir um produto
     *
     */
    public monArmazemPM(int stockInicialPM, int preco, int PMPorFornecimento, int prodPM, GeralInterface geralInter) {
        this.fornecimentoPM = 0;
        this.precoPM = preco;
        this.stockPM = stockInicialPM;
        this.totalVendaPM = 0;
        this.vendasPM = 0;
        this.geralInter = geralInter;
        this.prodPM = prodPM;
        this.vecTime = new VectorTime(7, -1);
    }

    /**
     *
     * A dona visita o armazem PM para recolher a materia prima que vai leva
     * para a Oficina.
     *
     * @param v relogio vectorial
     * @return quantidade de PM que a dona recolheu (gerada aleatoriamente).
     * @throws java.rmi.RemoteException excepcao para remote do rmi
     *
     */
    @Override
    public int visitSuppliers(VectorTime v) throws RemoteException {
        
        vecTime.update(v);
        
        Random randPercent = new Random();
        Random rand = new Random();
        this.fornecimentoPM = 0;
        
        if (this.stockPM > this.prodPM * 2) {
            int percent = randPercent.nextInt(101);
            
            if (percent < 80) {
                while (this.fornecimentoPM == 0) {
                    this.fornecimentoPM = rand.nextInt((int) ((int) (this.stockPM) * 0.3 + 1));
                }
                this.stockPM -= this.fornecimentoPM;
                
            } else {
                while (this.fornecimentoPM == 0) {
                    this.fornecimentoPM = rand.nextInt((this.stockPM + 1));
                }
                this.stockPM -= this.fornecimentoPM;
            }
        } else {
            this.fornecimentoPM = this.stockPM;
            this.stockPM = 0;
        }
        
        try {
            geralInter.setEstadoDona(SDona.BUYING_PRIME_MATERIALS, vecTime.getCopy());
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(monArmazemPM.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //System.out.println("A dona visitou o armazem. \n Quantidade de PM recolhida: " + this.fornecimentoPM + "\n Stock restante: " + this.stockPM + "\n");
        if (this.stockPM == 0) {
            System.out.println("armazem pm esta vazio.");
            geralInter.setArmPM(false);
        }
        
        return fornecimentoPM;
    }
    
    /**
     *
     * shutdown do server.
     *
     */
    @Override
    public void shutdown() {
        this.flag=1;
        //GenericIO.writelnString("O Repositorio Geral vai encerrar");
    }

    /**
     *
     * consulta da flag de shut down.
     *
     * @return 0 se nao deve terminar.
     *         1 caso contrario.
     */
    public int getFlag() {
      //  GenericIO.writelnString(this.flag+"\n");
        return flag;
    }
    
}
