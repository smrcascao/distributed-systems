package GeralSide;

import ServerInterface.GeralInterface;
import States.*;
import VectorTime.VectorTime;
import genclass.GenericIO;
import genclass.TextFile;

/**
 * Regiao partilhada Repositorio Geral (Servidor). esta regiao contem os estados
 * de todas as entidades activas. É responsavel pela criaçao e preenchimento do
 * ficheiro de logging
 *
 * @author rofler
 */
public class monGeral implements GeralInterface {

    //variaveis
    /**
     * Estado presente dos artesaos
     *
     * @serial estadosArtesao
     */
    private int[] estadosArtesao;

    /**
     * produtos Produzidos pelos artesaos
     *
     * @serial produtosArtesao
     */
    private int[] produtosArtesao;

    /**
     * Estado presente dos Clientes
     *
     * @serial estadosCliente
     */
    private int[] estadosCliente;

    /**
     * Compras dos clientes
     *
     * @serial comprasCliente
     */
    private int[] comprasCliente;

    /**
     * Estado presente da Dona
     *
     * @serial estadosDona
     */
    private int estadosDona;

    /**
     * Produtos que a dona esta a transportar da oficina para a loja
     *
     * @serial produtosComDona
     */
    private int produtosComDona;

    /**
     * PM total no sistema.
     *
     * @serial totalPM
     */
    private int totalPM;

    /**
     * numero total de produtos na oficina e na loja.
     *
     * @serial totalProdutos
     */
    private int totalProdutos;

    /**
     * numero total de produtos produzidos na oficina.
     *
     * @serial totalProdutosOficina
     */
    private int totalProdutosOficina;

    /**
     * quantidade total de PM fornecida a oficina.
     *
     * @serial totalPMOficina
     */
    private int totalPMOficina;

    /**
     * dona chamada por falta de PM na oficina.
     *
     * @serial donaChamadaPM
     */
    private boolean donaChamadaPM;

    /**
     * dona chamada por excesso de produtos na oficina.
     *
     * @serial donaChamadaProdutos
     */
    private boolean donaChamadaProdutos;

    /**
     * Número de artesaos que frequentam a Oficina
     *
     * @serial nArtesaos
     */
    private int nCArtesaos = 0;

    /**
     * Número de clientes que frequentam a Loja
     *
     * @serial nClientes
     */
    private int nClientes = 0;

    /**
     * Número de clientes que frequentam a Loja no instante actual
     *
     * @serial nClientesNaLoja
     */
    private int nClientesNaLoja = 0;

    /**
     * Número de produtos na loja
     *
     * @serial nProdutosNaLoja
     */
    private int nProdutosNaLoja = 0;

    /**
     * Número de produtos na Oficina
     *
     * @serial nProdutosNaOficina
     */
    private int nProdutosNaOficina = 0;

    /**
     * quantidade de PM na oficina
     *
     * @serial nPMNaOficina
     */
    private int nPMNaOficina = 0;

    /**
     * fornecimentos de PM a oficina
     *
     * @serial nFornOficina
     */
    private int nFornOficina = 0;

    /**
     * Nome do ficheiro de logging
     *
     * @serial fileName
     */
    private String fileName = "log.txt";

    /**
     * Estado da porta da loja (open || opdc || clos)
     *
     * @serial shopDoorStat
     */
    private String shopDoorStat = "";

    /**
     *
     * Estado do armazem de PM (true se houver PM, false se estiver vazio)
     *
     * @serial armPM
     *
     */
    private boolean armPM;

    /**
     * PM para construir um produto
     *
     * @serial produtoPM
     */
    private int produtoPM;

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
     * @param numArtesaos Número de artesaos que frequentam a Oficina
     * @param numClientes Número de clientes que frequentam a Loja
     * @param totalPM total de PM no sistema no instante inicial
     * @param fileName ficheiro de logging
     * @param produtoPM pm para produzir um produto
     *
     */
    public monGeral(int numArtesaos, int numClientes, int totalPM, int produtoPM, String fileName) {
        if (numArtesaos > 0) {
            this.nCArtesaos = numArtesaos;
            this.estadosArtesao = new int[numArtesaos];
            this.produtosArtesao = new int[numArtesaos];
        }
        if (numClientes > 0) {
            this.nClientes = numClientes;
            this.estadosCliente = new int[numClientes];
            this.comprasCliente = new int[numClientes];
        }
        this.totalPM = totalPM;
        this.totalProdutos = 0;
        this.shopDoorStat = "clos";
        this.nClientesNaLoja = 0;
        this.nProdutosNaLoja = 0;
        this.nProdutosNaOficina = 0;
        this.nPMNaOficina = 0;
        this.nFornOficina = 0;
        this.totalPMOficina = 0;
        this.armPM = true;
        this.produtoPM = produtoPM;

        this.estadosDona = SDona.OPENING_THE_SHOP;

        for (int i = 0; i < this.nCArtesaos; i++) {
            this.estadosArtesao[i] = SArtesao.FETCHING_PRIME_MATERIALS;
            this.produtosArtesao[i] = 0;
        }
        for (int i = 0; i < this.nClientes; i++) {
            this.estadosCliente[i] = SCliente.CARRYING_OUT_DAILY_CHORES;
            this.comprasCliente[i] = 0;
        }

        if ((fileName != null) && !("".equals(fileName))) {
            this.fileName = fileName;
        }
        
        
        this.vecTime = new VectorTime(7, -1);
        
        this.flag=0;
        
        reportInitialStatus();

    }

    /**
     *
     * altera o estado do armazemPM no repositorio geral
     *
     * @param armPM novo estado do armazemPM
     *
     */
    @Override
    public void setArmPM(boolean armPM) {
        this.armPM = armPM;
    }

    /**
     *
     * devolve o estado do armazem PM
     *
     * @return true - tem PM false - esta vazio
     *
     */
    @Override
    public boolean isArmPM() {
        return armPM;
    }

    /**
     *
     * reduz o nivel total de pm no sistema
     *
     * @param quantPM quantidade de pm usada
     *
     */
    @Override
    public void pMGasta(int quantPM) {
        this.totalPM -= quantPM;

        //System.err.println(this.totalPM);       //debug

    }

    /**
     *
     * Incrementa o numero de fornecimentos de pm a oficina
     *
     * @param quantPM quantidade de PM fornecida
     */
    @Override
    public void novoFornecimentoPM(int quantPM) {
        this.nFornOficina++;
        this.totalPMOficina += quantPM;

    }

    /**
     *
     * Altera estado da porta da loja
     *
     * @param shopDoorStat novo estado da porta da loja
     */
    @Override
    public void setShopDoorStat(String shopDoorStat) {
        this.shopDoorStat = shopDoorStat;

    }

    /**
     *
     * Altera o numero de produtos na loja dentro do repositorio geral
     *
     * @param nProdutosNaLoja numero de produtos na loja
     */
    @Override
    public void setnProdutosNaLoja(int nProdutosNaLoja) {
        this.nProdutosNaLoja = nProdutosNaLoja;

    }

    /**
     *
     * Altera o numero de produtos na Oficina dentro do repositorio geral
     *
     * @param nProdutosNaOficina numero de produtos na Oficina
     */
    @Override
    public void setnProdutosNaOficina(int nProdutosNaOficina) {
        this.nProdutosNaOficina = nProdutosNaOficina;

    }

    /**
     *
     * Altera a quantidade de PM na Oficina dentro do repositorio geral
     *
     * @param nPMNaOficina quantidade de pm na oficina
     */
    @Override
    public void setnPMNaOficina(int nPMNaOficina) {
        this.nPMNaOficina = nPMNaOficina;

    }

    /**
     *
     * Acrescenta produtos a quantidade previamente na posse do cliente.
     *
     * @param idCliente id do cliente
     * @param numProdutos numero de produtos que o cliente comprou
     *
     */
    @Override
    public void compraCliente(int idCliente, int numProdutos) {
        this.comprasCliente[idCliente] += numProdutos;
        this.totalProdutos -= numProdutos;

        //System.out.println(totalProdutos);
    }

    /**
     *
     * Acrescenta um produto a quantidade de produtos feitos pelo artesao.
     *
     * @param idArtesao id do artesao que fez o produto
     */
    @Override
    public void artesaoFezProduto(int idArtesao) {
        this.produtosArtesao[idArtesao]++;
        this.totalProdutos++;
        this.totalProdutosOficina++;
    }

    /**
     *
     * Altera o estado da Dona.
     *
     * @param estado novo estado da dona
     * @param v Relogio vectorial 
     */
    @Override
    public void setEstadoDona(int estado, VectorTime v) {
        vecTime.update(v);
        this.estadosDona = estado;
        reportStatus();
    }

    /**
     *
     * Altera o estado do cliente.
     *
     * @param id id do cliente
     * @param estado novo estado do cliente
     * @param v relogio vectorial
     */
    @Override
    public void setEstadoCliente(int id, int estado, VectorTime v) {
        vecTime.update(v);
        this.estadosCliente[id] = estado;
        reportStatus();
    }

    /**
     *
     * Cliente entrou na loja.
     *
     */
    @Override
    public void clienteEntra() {
        this.nClientesNaLoja++;
    }

    /**
     *
     * Cliente saiu da loja.
     *
     */
    @Override
    public void clienteSai() {
        this.nClientesNaLoja--;
    }

    /**
     *
     * Altera o estado do artesao.
     *
     * @param id id do artesao
     * @param estado novo estado do artesao
     * @param v relogio vectorial
     */
    @Override
    public void setEstadoArtesao(int id, int estado, VectorTime v) {
        vecTime.update(v);
        this.estadosArtesao[id] = estado;
        reportStatus();
    }

    /**
     *
     * funcao usada pela dona para depositar na loja os produtos que traz da
     * oficina.
     *
     * @return numero de produtos depositados na loja
     *
     */
    @Override
    public int getProdutosComDona() {
        int aux = this.produtosComDona;
        this.produtosComDona = 0;
        return aux;
    }

    /**
     *
     * funcao usada pela receber produtos.
     *
     * @param p quantidade de produtos que a dona recebe
     */
    @Override
    public void donaRecebeProdutos(int p) {
        this.produtosComDona = p;
    }

    /**
     *
     * funcao usada para calcular se a thread dona deve terminar.
     *
     * @return true, se a thread deve acabar false, em caso contrário
     */
    @Override
    public boolean endOpDona() {
        return (this.totalPM < this.produtoPM && this.totalProdutos == 0);
    }

    /**
     *
     * funcao usada para calcular se a thread cliente deve terminar.
     *
     * @return true, se a thread deve acabar false, em caso contrário
     */
    @Override
    public synchronized boolean endOpCliente() {
        if (this.totalPM < this.produtoPM && this.totalProdutos == 0) {
            notifyAll();
            return true;
        }
        return false;

    }

    /**
     *
     * funcao usada para calcular se a thread artesao deve terminar.
     *
     * @return true, se a thread deve acabar false, em caso contrário
     */
    @Override
    public boolean endOpArtesao() {
        return (this.totalPM < this.produtoPM);
    }

    /**
     *
     * funcao usada pela dona para saber se foi chamada para fornecer mais PM a
     * oficina.
     *
     * @return true, se a dona foi chamada false, em caso contrário
     */
    @Override
    public boolean isDonaChamadaPM() {
        return donaChamadaPM;
    }

    /**
     *
     * funcao usada pela dona para saber se foi chamada para recolher produtos
     * da oficina.
     *
     * @return true, se a dona foi chamada false, em caso contrário
     */
    @Override
    public boolean isDonaChamadaProdutos() {
        return donaChamadaProdutos;
    }

    /**
     *
     * funcao usada pelos artesaos para chamar a dona quando ha falta de PM.
     *
     * @param donaChamadaPM variavel booleana (true - dona chamada; false -
     * cancelar chamamento da dona)
     *
     */
    @Override
    public void setDonaChamadaPM(boolean donaChamadaPM) {
        this.donaChamadaPM = donaChamadaPM;
    }

    /**
     *
     * funcao usada pelos artesaos para chamar a dona quando a quantidade de
     * produtos na oficina passa o nivel de alarme.
     *
     * @param donaChamadaProdutos variavel booleana (true - dona chamada; false
     * - cancelar chamamento da dona)
     *
     */
    @Override
    public void setDonaChamadaProdutos(boolean donaChamadaProdutos) {
        this.donaChamadaProdutos = donaChamadaProdutos;
    }

    /**
     * Escrever o estado inicial (operação interna).
     * <p>
     * A dona vai abrir a loja, os artesaos iniciam o trabalho e os clientes a
     * realizar as tarefas do dia a dia.
     */
    private void reportInitialStatus() {
        TextFile log = new TextFile();                      // instanciação de uma variável de tipo ficheiro de texto

        if (!log.openForWriting(".", fileName)) {
            GenericIO.writelnString("A operação de criação do ficheiro " + fileName + " falhou!");
            System.exit(1);
        }
        log.writelnString("                Aveiro Handicraft SARL - Description of the internal state\n\n"
                // + "\tENTREPRE\tCUST_0\t\tCRAFT_0\t\t\t\t\tSHOP\t\t\t\t\t\tWORKSHOP\n");
                + "\tENTREPRE\tCUST_0\t\tCUST_1\t\tCUST_2\t\tCRAFT_0\t\t\tCRAFT_1\t\t\tCRAFT_2\t\t\t\t\tSHOP\t\t\t\t\t\t\tWORKSHOP\t\t\t\t\t\tCLOCK");
        //log.writelnString ("\nNúmero de iterações = " + nIter + "\n");
        if (!log.close()) {
            GenericIO.writelnString("A operação de fecho do ficheiro " + fileName + " falhou!");
            System.exit(1);
        }
        reportStatus();
    }

    /**
     * Escrever o estado actual (operação interna).
     * <p>
     * Uma linha de texto com o estado de actividade da Dona, dos artesaos e dos
     * clientes é escrito no ficheiro.
     */
    private void reportStatus() {
        TextFile log = new TextFile();                      // instanciação de uma variável de tipo ficheiro de texto

        String lineStatus = "";                              // linha a imprimir

        if (!log.openForAppending(".", fileName)) {
            GenericIO.writelnString("A operação de criação do ficheiro " + fileName + " falhou!");
            System.exit(1);
        }

        switch (this.estadosDona) {
            case SDona.OPENING_THE_SHOP:
                lineStatus += "\tOPTS\t\t";
                break;
            case SDona.WAITING_FOR_NEXT_TASK:
                lineStatus += "\tWFNT\t\t";
                break;
            case SDona.ATTENDIND_A_CUSTOMER:
                lineStatus += "\tATAC\t\t";
                break;
            case SDona.CLOSING_THE_SHOP:
                lineStatus += "\tCLTS\t\t";
                break;
            case SDona.BUYING_PRIME_MATERIALS:
                lineStatus += "\tBUPM\t\t";
                break;
            case SDona.DELIVERING_PRIME_MATERIALS:
                lineStatus += "\tDEPM\t\t";
                break;
            case SDona.COLLECTING_A_BATCH_OF_PRODUCTS:
                lineStatus += "\tCABOP\t\t";
                break;

        }
        for (int i = 0; i < nClientes; i++) {
            switch (this.estadosCliente[i]) {
                case SCliente.CARRYING_OUT_DAILY_CHORES:
                    lineStatus += "CODC\t" + this.comprasCliente[i] + "\t";
                    break;
                case SCliente.CHECKING_DOOR_OPEN:
                    lineStatus += "CDOP\t" + this.comprasCliente[i] + "\t";
                    break;
                case SCliente.APPRAISING_OFFER_IN_DISPLAY:
                    lineStatus += "AOID\t" + this.comprasCliente[i] + "\t";
                    break;
                case SCliente.BUYING_SOME_GOODS:
                    lineStatus += "BUSG\t" + this.comprasCliente[i] + "\t";
                    break;
            }
        }

        for (int i = 0; i < this.nCArtesaos; i++) {
            switch (this.estadosArtesao[i]) {
                case SArtesao.FETCHING_PRIME_MATERIALS:
                    lineStatus += "FEPM\t" + this.produtosArtesao[i] + "\t\t";
                    break;
                case SArtesao.PRODUCING_A_NEW_PIECE:
                    lineStatus += "PANP\t" + this.produtosArtesao[i] + "\t\t";
                    break;
                case SArtesao.STORING_IT_FOR_TRANSFER:
                    lineStatus += "SIFT\t" + this.produtosArtesao[i] + "\t\t";
                    break;
                case SArtesao.CONTACTING_THE_ENTREPRENEUR:
                    lineStatus += "CTEP\t" + this.produtosArtesao[i] + "\t\t";
                    break;
            }
        }

        lineStatus += this.shopDoorStat + "\t";
        lineStatus += this.nClientesNaLoja + "\t";
        lineStatus += this.nProdutosNaLoja + "\t";
        lineStatus += this.donaChamadaProdutos + "\t";
        lineStatus += this.donaChamadaPM + "\t\t";

        lineStatus += this.nPMNaOficina + "\t";
        lineStatus += this.nProdutosNaOficina + "\t";
        lineStatus += this.nFornOficina + "\t";
        lineStatus += this.totalPMOficina + "\t";
        lineStatus += this.totalProdutosOficina + "\t\t";
        //lineStatus += this.totalPM + "\t\t";

        int[] vectorInt;
        vectorInt = this.vecTime.toIntArray();
        lineStatus += vectorInt[0] + "\t";
        lineStatus += vectorInt[1] + "\t";
        lineStatus += vectorInt[2] + "\t";
        lineStatus += vectorInt[3] + "\t";
        lineStatus += vectorInt[4] + "\t";
        lineStatus += vectorInt[5] + "\t";
        lineStatus += vectorInt[6] + "\t";

        log.writelnString(lineStatus);
        if (!log.close()) {
            GenericIO.writelnString("A operação de fecho do ficheiro " + fileName + " falhou!");
            System.exit(1);
        }
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
        //GenericIO.writelnString(this.flag+"\n");
        return flag;
    }

    
}
