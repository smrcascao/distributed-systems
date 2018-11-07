package OficinaSide;

import ServerInterface.GeralInterface;
import ServerInterface.OficinaInterface;
import States.SArtesao;
import States.SDona;
import VectorTime.VectorTime;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Oficina
 *
 * Repositorio oficina As entidades que interage com este repositorio de dados
 * sao: artesaos dona
 *
 * @author rofler
 *
 */
public class monOficina implements OficinaInterface {
    //variaveis
    //PM

    /**
     * quantidade de PM em stock
     *
     * @serial stockPM
     */
    private int stockPM;

    /**
     * quantidade total de PM encomendada
     *
     * @serial totalPM
     */
    private int totalPM;

    /**
     * num total de fornecimentos de PM
     *
     * @serial fornecimentosPM
     */
    private int fornecimentosPM;

    /**
     * PM para construir um produto
     *
     * @serial produtoPM
     */
    private int produtoPM;

    /**
     * limite minimo de PM
     *
     * @serial limiteMinPM
     */
    private int limiteMinPM;

    //Produto
    /**
     * Produtos em stock
     *
     * @serial stockProdutos
     */
    private int stockProdutos;

    /**
     * quantidade total de produtos produzidos
     *
     * @serial totalProdutos
     */
    private int totalProdutos;

    /**
     * Limite de Alarme da quantidade de produtos
     *
     * @serial alarmeProdutos
     */
    private int alarmeProdutos;

    /**
     * Limite maximo de produtos
     *
     * @serial limiteMaxProdutos
     */
    private int limiteMaxProdutos;

    /**
     * quantidade de produtos que a dona leva para a loja
     *
     * @serial produtosDona
     */
    private int produtosDona;

    /**
     * dona chamada (por falta de PM)
     *
     * @serial donaChamadaPM
     */
    private boolean donaChamadaPM;

    /**
     * dona chamada (por existirem demasiados produtos em stock)
     *
     * @serial donaChamadaProdutos
     */
    private boolean donaChamadaProdutos;

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
     * @param alarmeProd Limite de Alarme da quantidade de produtos
     * @param limiteProd Limite maximo de produtos
     * @param alarmePM limite minimo de PM
     * @param prodPM PM para construir um produto
     * @param prodDona quantidade de produtos que a dona leva para a loja
     * @param geralInter interface remota ao repositorio Geral
     *
     */
    public monOficina(int alarmeProd, int limiteProd, int alarmePM, int prodPM, int prodDona, GeralInterface geralInter) {
        this.alarmeProdutos = alarmeProd;
        this.donaChamadaPM = false;
        this.donaChamadaProdutos = false;
        this.fornecimentosPM = 0;
        this.limiteMaxProdutos = limiteProd;
        this.limiteMinPM = alarmePM;
        this.produtoPM = prodPM;
        this.stockPM = 0;
        this.stockProdutos = 0;
        this.totalPM = 0;
        this.totalProdutos = 0;
        this.produtosDona = prodDona;
        this.geralInter = geralInter;

        this.vecTime = new VectorTime(7, -1);
    }

    /* funcoes dos artesaos*/
    /**
     *
     * funcao usada pelo artesao para ver se ha PM para poder trabalhar
     *
     * @param id id do artesao
     * @param v relogio vectorial
     *
     * @return 0 se a quantidade de PM nao for suficiente para fazer um produto
     * 2 se for necessario chamar a dona para repor o stock de PM 3 se nao
     * houver espaco para mais produtos na oficina 1 para outros casos
     * @throws java.rmi.RemoteException excepcao para remote do rmi
     *
     */
    @Override
    public synchronized int checkForMaterials(int id, VectorTime v) throws RemoteException {
        vecTime.update(v);

        if (geralInter.endOpArtesao()) {
            return 4;
        } else if (stockProdutos == limiteMaxProdutos) {
            return 3;
        } else if (stockPM < produtoPM) {
            return 0;
        } else if (stockPM - produtoPM < limiteMinPM) {
            stockPM -= produtoPM;         //para o artesao reservar a PM que vai usar (se nao outro artesao pode pensar que a pode usar)
            return 2;
        } else {
            stockPM -= produtoPM;         //para o artesao reservar a PM que vai usar (se nao outro artesao pode pensar que a pode usar)
            return 1;
        }
    }

    /**
     *
     * funcao usada pelo artesao para recolher PM para produzir um produto
     *
     * @param id id do artesao
     * @throws java.rmi.RemoteException excepcao para remote do rmi
     *
     */
    @Override
    public synchronized void collectMaterials(int id) throws RemoteException {
        geralInter.setnPMNaOficina(this.stockPM);
        geralInter.pMGasta(produtoPM);

    }

    /**
     *
     * funcao usada pelo artesao para chamar a dona por falta de PM
     *
     * @param id id do artesao
     * @param v relogio vectorial
     * @return retorna a versao acualizada do relogio vectorial
     * @throws java.rmi.RemoteException excepcao para remote do rmi
     *
     */
    @Override
    public synchronized VectorTime primeMaterialsNeeded(int id, VectorTime v) throws RemoteException {
        vecTime.update(v);

        if (this.donaChamadaPM == false) {

            try {
                geralInter.setEstadoArtesao(id, SArtesao.CONTACTING_THE_ENTREPRENEUR, vecTime.getCopy());
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(monOficina.class.getName()).log(Level.SEVERE, null, ex);
            }

            this.donaChamadaPM = true;
            geralInter.setDonaChamadaPM(true);
            notifyAll();
        } else {
            try {
                this.wait();
            } catch (InterruptedException ex) {

            }
        }

        try {
            return vecTime.getCopy();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(monOficina.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    /**
     *
     * funcao usada pelo artesao para chamar a dona quando a quantidade de
     * produtos excede o nivel de alarme
     *
     * @param id id do artesao
     * @param v relogio vectorial
     * @return retorna a versao acualizada do relogio vectorial
     * @throws java.rmi.RemoteException excepcao para remote do rmi
     *
     */
    @Override
    public synchronized VectorTime batchReadyForTransfer(int id, VectorTime v) throws RemoteException {
        vecTime.update(v);

        if (this.donaChamadaProdutos == false) {
            try {
                geralInter.setEstadoArtesao(id, SArtesao.CONTACTING_THE_ENTREPRENEUR, vecTime.getCopy());
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(monOficina.class.getName()).log(Level.SEVERE, null, ex);
            }

            this.donaChamadaProdutos = true;
            geralInter.setDonaChamadaProdutos(true);
            notifyAll();
        } else {
            try {
                this.wait();
            } catch (InterruptedException ex) {

            }
        }

        try {
            return vecTime.getCopy();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(monOficina.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    /**
     *
     * funcao usada pelo artesao para voltar a trabalhar apos ter terminado um
     * produto ou contactado a dona
     *
     * @param id id do artesao
     * @throws java.rmi.RemoteException excepcao para remote do rmi
     *
     */
    @Override
    public synchronized void backToWork(int id) throws RemoteException {
        try {
            geralInter.setEstadoArtesao(id, SArtesao.FETCHING_PRIME_MATERIALS, vecTime.getCopy());
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(monOficina.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * funcao usada pelo artesao para colocar o produto que acabou de produzir
     * no armazem da oficina
     *
     * @param id id do artesao
     * @param v relogio vectorial
     *
     * @return 0 se o armazem estiver cheio 2 se a quantidade de produtos no
     * armazem da oficina exceder o nivel de alarme 1 para outros casos
     * @throws java.rmi.RemoteException excepcao para remote do rmi
     *
     */
    @Override
    public synchronized int goToStore(int id, VectorTime v) throws RemoteException {
        vecTime.update(v);

        try {
            geralInter.setEstadoArtesao(id, SArtesao.STORING_IT_FOR_TRANSFER, vecTime.getCopy());
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(monOficina.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.stockProdutos++;
        this.totalProdutos++;
        geralInter.artesaoFezProduto(id);
        geralInter.setnProdutosNaOficina(this.stockProdutos);
        if (stockProdutos >= limiteMaxProdutos) {
            return 0;
        } else if (stockProdutos >= alarmeProdutos) {
            return 2;
        } else {
            return 1;
        }
    }

    /**
     *
     * funcao usada pelo artesao para comecar a fazer um produto
     *
     * @param id id do artesao
     * @param v relogio vectorial
     * @return retorna a versao acualizada do relogio vectorial
     * @throws java.rmi.RemoteException excepcao para remote do rmi
     *
     */
    @Override
    public synchronized VectorTime prepareToProduce(int id, VectorTime v) throws RemoteException {
        vecTime.update(v);

        try {
            geralInter.setEstadoArtesao(id, SArtesao.PRODUCING_A_NEW_PIECE, vecTime.getCopy());
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(monOficina.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            return vecTime.getCopy();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(monOficina.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    /*funcoes da dona*/
    /**
     *
     * funcao usada pela dona para receber produtos do armazem da oficina
     *
     *
     * @param v relogio vectorial
     * @return retorna a versao acualizada do relogio vectorial
     * @throws java.rmi.RemoteException excepcao para remote do rmi
     */
    @Override
    public synchronized VectorTime goToWorkShop(VectorTime v) throws RemoteException {
        vecTime.update(v);

        if (!geralInter.endOpArtesao()) {
            this.stockProdutos -= this.produtosDona;
            geralInter.donaRecebeProdutos(this.produtosDona);
            geralInter.setnProdutosNaOficina(this.stockProdutos);

            try {
                geralInter.setEstadoDona(SDona.COLLECTING_A_BATCH_OF_PRODUCTS, vecTime.getCopy());
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(monOficina.class.getName()).log(Level.SEVERE, null, ex);
            }

            this.donaChamadaProdutos = false;
            geralInter.setDonaChamadaProdutos(false);
            //System.out.println("\nA dona recolheu produtos.\n Quantidade de produtos que a dona leva: "+this.produtosDona+"\n Stock restante: "+this.stockProdutos);
            notifyAll();
        } else {
            //System.out.println("\nA dona recolheu produtos.\n Quantidade de produtos que a dona leva: "+this.stockProdutos+"\n Stock restante: 0");
            geralInter.donaRecebeProdutos(this.stockProdutos);
            this.stockProdutos = 0;
            geralInter.setnProdutosNaOficina(this.stockProdutos);

            try {
                geralInter.setEstadoDona(SDona.COLLECTING_A_BATCH_OF_PRODUCTS, vecTime.getCopy());
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(monOficina.class.getName()).log(Level.SEVERE, null, ex);
            }

            this.donaChamadaProdutos = false;
            geralInter.setDonaChamadaProdutos(false);
            notifyAll();
        }

        try {
            return vecTime.getCopy();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(monOficina.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    /**
     *
     * funcao usada pela dona para repor PM na oficina
     *
     * @param pm quantidade de pm fornecida pela dona
     * @param v relogio vectorial
     * @return retorna a versao acualizada do relogio vectorial
     * @throws java.rmi.RemoteException excepcao para remote do rmi
     *
     */
    @Override
    public synchronized VectorTime replenishStock(int pm, VectorTime v) throws RemoteException {
        vecTime.update(v);

        this.fornecimentosPM++;
        geralInter.novoFornecimentoPM(pm);
        this.totalPM += pm;
        this.stockPM += pm;
        geralInter.setnPMNaOficina(this.stockPM);

        try {
            geralInter.setEstadoDona(SDona.DELIVERING_PRIME_MATERIALS, vecTime.getCopy());
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(monOficina.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.donaChamadaPM = false;
        geralInter.setDonaChamadaPM(false);

        //System.out.println("\nA dona repos o stock de PM. \n quantidade de PM reposta: "+pm+"\n Stock de PM na Oficina: "+this.stockPM);
        notifyAll();

        try {
            return vecTime.getCopy();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(monOficina.class.getName()).log(Level.SEVERE, null, ex);
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
        //GenericIO.writelnString("O Repositorio Geral vai encerrar");
    }

    /**
     *
     * consulta da flag de shut down.
     *
     * @return 0 se nao deve terminar. 1 caso contrario.
     */
    public int getFlag() {
        //GenericIO.writelnString(this.flag+"\n");
        return this.flag;
    }

}
