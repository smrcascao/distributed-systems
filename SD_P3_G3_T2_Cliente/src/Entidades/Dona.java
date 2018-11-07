package Entidades;

import Com.ClientCom;
import Message.Message;
import States.SDona;
import genclass.GenericIO;

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
     * @serial estadoDona
     */
    private SDona estadoDona;

    ;

    /**
     * Nome do sistema computacional onde está localizado o servidor do
     * REpositorio Geral
     *
     * @serial serverHostNameGeral
     */
    private String serverHostNameGeral = null;

    /**
     * Nome do sistema computacional onde está localizado o servidor da Oficina
     *
     * @serial serverHostNameOficina
     */
    private String serverHostNameOficina = null;

    /**
     * Nome do sistema computacional onde está localizado o servidor do armazem
     * de PM
     *
     * @serial serverHostNameArmazemPM
     */
    private String serverHostNameArmazemPM = null;

    /**
     * Nome do sistema computacional onde está localizado o servidor da Loja
     *
     * @serial serverHostNameLoja
     */
    private String serverHostNameLoja = null;

    /**
     * Número do porto de escuta do servidor (repositorio geral)
     *
     * @serial serverPortNumbGeral
     */
    private int serverPortNumbGeral;

    /**
     * Número do porto de escuta do servidor (Loja)
     *
     * @serial serverPortNumbLoja
     */
    private int serverPortNumbloja;

    /**
     * Número do porto de escuta do servidor (Oficina)
     *
     * @serial serverPortNumbOficina
     */
    private int serverPortNumbOficina;

    /**
     * Número do porto de escuta do servidor (armazem pm)
     *
     * @serial serverPortNumbArmPM
     */
    private int serverPortNumbArmPM;

    /**
     *
     * Construtor
     *
     * @param hostNameGeral nome do sistema computacional onde está localizado o servidor (regiao partilhada - repositorio geral)
     * @param hostNameLoja nome do sistema computacional onde está localizado o servidor (regiao partilhada - Loja)
     * @param portGeral número do porto de escuta do servidor do repositorio geral
     * @param portLoja número do porto de escuta do servidor da loja
     * @param hostNameOficina nome do sistema computacional onde está localizado o servidor (regiao partilhada -  Oficina)
     * @param hostNameArmazemPM nome do sistema computacional onde está localizado o servidor (regiao partilhada - armazem PM)
     * @param portOficina número do porto de escuta do servidor da  Oficina
     * @param portArmazemPM número do porto de escuta do servidor da armazem PM
     *
     */
    public Dona(String hostNameGeral, int portGeral, String hostNameLoja, int portLoja, String hostNameOficina, int portOficina, String hostNameArmazemPM, int portArmazemPM) {
        this.estadoDona=SDona.OPENING_THE_SHOP;
        this.serverHostNameArmazemPM=hostNameArmazemPM;
        this.serverHostNameGeral=hostNameGeral;
        this.serverHostNameLoja=hostNameLoja;
        this.serverHostNameOficina=hostNameOficina;
        this.serverPortNumbArmPM=portArmazemPM;
        this.serverPortNumbGeral=portGeral;
        this.serverPortNumbOficina=portOficina;
        this.serverPortNumbloja=portLoja;
        this.estadoDona = SDona.OPENING_THE_SHOP;
    }

    /**
     * funcao que executa o ciclo de vida da dona.
     */
    @Override
    public void run() {

        boolean canGoOut = false;
        int sit = 0, idCliente, pm;
        do {
            prepareToWork();
            canGoOut = false;

            while (!canGoOut) {
                sit = appraiseSit();
                switch (sit) {
                    case 2: //caso tenha de atender cliente
                        idCliente = addressACustomer();
                        serviceCustomer();
                        sayGoodbyeToCustomer(idCliente);
                        break;
                    case 3://caso tenha de ir buscar produtos a oficina
                    case 4://caso tenha de ir buscar PM ao armazem
                        closeTheDoor();
                        canGoOut = !customersInTheShop();
                        break;
                    case 1:
                        canGoOut = true;
                        break;
                    case 0:
                        canGoOut = false;
                        break;
                }

            }
            prepareToLeave();

            if (sit == 3) {
                goToWorkShop();
            } else if (sit == 4) {
                pm = visitSuppliers();
                replenishStock(pm);
            }
            returnToShop();

        } while (!endOpDona());
        
        //shutdown();
    }

    /**
     * 
     * atende o cliente (operação interna).
     */
    private void serviceCustomer() {
        try {
            sleep((long) (1 + 1 * Math.random()));
        } catch (InterruptedException e) {
        }
    }

    
    
    /*interaccao dona - Oficina*/
    /**
     *
     * A dona vai a oficina buscar produtos.
     * (comunica ao server a mensagem GTWKSHP)
     *
     */
    private void goToWorkShop() {
        estadoDona = SDona.COLLECTING_A_BATCH_OF_PRODUCTS;

        ClientCom con = new ClientCom(serverHostNameOficina, serverPortNumbOficina);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.GTWKSHP);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK) {
            GenericIO.writelnString("Thread " + getName() + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }
    
    /**
     *
     * A dona vai a oficina buscar levar PM.
     * (comunica ao server a mensagem REPSTCK)
     *
     * @param pm quantidade de pm que a dona traz para a oficina
     */
    private void replenishStock(int pm) {
        estadoDona = SDona.DELIVERING_PRIME_MATERIALS;

        ClientCom con = new ClientCom(serverHostNameOficina, serverPortNumbOficina);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.REPSTCK, pm);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK) {
            GenericIO.writelnString("Thread " + getName() + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }
    
    /*interaccao dona armazemPM*/
    /**
     *
     * A dona visita o armazem de PM. (comunica ao server a mensagem REQEXTS)
     *
     * @return quantidade de pm que a dona recolheu do armazem de PM
     */
    private int visitSuppliers() {
        this.estadoDona = SDona.BUYING_PRIME_MATERIALS;

        ClientCom con = new ClientCom(serverHostNameArmazemPM, serverPortNumbArmPM);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(Message.DONAVPM);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.DONVOPM) {
            GenericIO.writelnString("Thread " + getName() + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close();

        return inMessage.getId();

    }

    
    /*interaccao dona loja*/
    /**
     *
     * A dona abre a loja e prepara se para trabalhar . (comunica ao server a
     * mensagem PREPTW)
     *
     */
    private void prepareToWork() {
        this.estadoDona = SDona.WAITING_FOR_NEXT_TASK;

        ClientCom con = new ClientCom(serverHostNameLoja, serverPortNumbloja);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.PREPTW);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK) {
            GenericIO.writelnString("Thread " + getName() + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    /**
     *
     * A dona verifica se ha tarefas que necessita a sua accao. (comunica ao
     * server a mensagem APRSIT)
     *
     * @return 1 se o ciclo de vida da dona deve terminar;
     *         2 se a fila de clientes nao esta vazia; 
     *         3 se a dona tiver sido chamada para ir buscar produtos a ofcina;
     *         4 se a dona tiver sido chamada para levar PM a oficina; 
     *         0 para outros casos 
     * 
     */
    private int appraiseSit() {
        ClientCom con = new ClientCom(serverHostNameLoja, serverPortNumbloja);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(Message.APRSIT);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.appraiseReply) {
            GenericIO.writelnString("Thread " + getName() + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close();

        return inMessage.getId();

    }

    /**
     *
     * A dona atende um cliente. (comunica ao server a mensagem ADDCUST)
     *
     * @return id do cliente que a dona vai atender
     * 
     */
    private int addressACustomer() {
        estadoDona = SDona.ATTENDIND_A_CUSTOMER;
        
        ClientCom con = new ClientCom(serverHostNameLoja, serverPortNumbloja);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(Message.ADDCUST);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK) {
            GenericIO.writelnString("Thread " + getName() + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close();

        return inMessage.getId();

    }

    /**
     *
     * A dona despede se de um cliente.
     * (comunica ao server a mensagem SAYGBCUST)
     *
     * @param idCliente id do cliente que a dona acabou de atender
     * 
     */
    private void sayGoodbyeToCustomer(int idCliente) {
        estadoDona = SDona.WAITING_FOR_NEXT_TASK;

        ClientCom con = new ClientCom(serverHostNameLoja, serverPortNumbloja);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.SAYGBCUST, idCliente);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK) {
            GenericIO.writelnString("Thread " + getName() + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }
    
    
    /**
     *
     * A dona fecha a loja (para ir buscar pm/produtos ou porque terminou o trabalho). (comunica ao server a
     * mensagem PREPTL)
     *
     */
    private void prepareToLeave() {
        estadoDona = SDona.CLOSING_THE_SHOP;

        ClientCom con = new ClientCom(serverHostNameLoja, serverPortNumbloja);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.PREPTL);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK) {
            GenericIO.writelnString("Thread " + getName() + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }
    
    
    /**
     *
     * A dona fecha a porta da loja. (comunica ao server a
     * mensagem CLOSSHDOOR)
     *
     */
    private void closeTheDoor() {

        ClientCom con = new ClientCom(serverHostNameLoja, serverPortNumbloja);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.CLOSSHDOOR);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK) {
            GenericIO.writelnString("Thread " + getName() + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }
    
    
    /**
     *
     * A dona ve se ainda esta algm cliente na loja. (comunica ao server a mensagem CUSTINSH)
     *
     * @return true se ha clientes na loja
     *         false caso contrario
     */
    private boolean customersInTheShop() {
        
        ClientCom con = new ClientCom(serverHostNameLoja, serverPortNumbloja);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(Message.CUSTINSH);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.CUSTINSHR) {
            GenericIO.writelnString("Thread " + getName() + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close();

        return inMessage.isAnswer();

    }
    
    /**
     *
     * A dona volta a loja.
     * (comunica ao server a mensagem RETSHP)
     *
     */
    private void returnToShop() {
        estadoDona = SDona.OPENING_THE_SHOP;

        ClientCom con = new ClientCom(serverHostNameLoja, serverPortNumbloja);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(Message.RETSHP);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != Message.ACK) {
            GenericIO.writelnString("Thread " + getName() + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }
    
    
    /*interaccao dona geral*/
    /**
     *
     * Funcao usada para saber se a dona deve terminar o seu ciclo de vida. (comunica ao server a mensagem ENDOPD)
     *
     * @return true se a dona deve terminar o seu ciclo de vida
     *         false caso contrario
     */
    private boolean endOpDona() {
        ClientCom con = new ClientCom(serverHostNameGeral, serverPortNumbGeral);
        Message inMessage, outMessage;

        if (!con.open()) {
            return false;                                // nao foi possivel entrar em comunicacao
        }
        outMessage = new Message(Message.ENDOPD);        // pede a realização do serviço
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if ((inMessage.getType() != Message.END) && (inMessage.getType() != Message.CONT)) {
            GenericIO.writelnString("Thread " + getName() + ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close();

        if (inMessage.getType() == Message.END)
            return true;                                                // o cliente deve terminar
        else
            return false;                                          // o cliente nao deve morrer
    }
    
    /**
     *
     * Funcao para encerrar os Servidores. (comunica ao server a mensagem SHUT)
     *
     */
    public void shutdown() {
        //terminar armazem PM
        ClientCom con = new ClientCom(serverHostNameArmazemPM, serverPortNumbArmPM);
        Message inMessage, outMessage;

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }    
        
        outMessage = new Message(Message.SHUT);        // pede a realização do serviço
        con.writeObject(outMessage);
        con.close();
        
        //terminar Loja
        con = new ClientCom(serverHostNameLoja, serverPortNumbloja);
        

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }    
        
        outMessage = new Message(Message.SHUT);        // pede a realização do serviço
        con.writeObject(outMessage);
        con.close();
        
        //terminar Oficina
        con = new ClientCom(serverHostNameOficina, serverPortNumbOficina);
        

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }    
        
        outMessage = new Message(Message.SHUT);        // pede a realização do serviço
        con.writeObject(outMessage);
        con.close();
        
        //terminar repositorio Geral
        con = new ClientCom(serverHostNameGeral, serverPortNumbGeral);
        

        while (!con.open()) // aguarda ligação
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }    
        
        outMessage = new Message(Message.SHUT);        // pede a realização do serviço
        con.writeObject(outMessage);
        con.close();
        
        

    }
    
    /**
     *
     * Funcao para encerrar os Servidores (antes de começar, para evitar conflitos). (comunica ao server a mensagem SHUT)
     *
     */
    public void shutdownFirst() {
        //terminar armazem PM
        ClientCom con = new ClientCom(serverHostNameArmazemPM, serverPortNumbArmPM);
        Message inMessage, outMessage;
        
        
        try {
             con.open();
             outMessage = new Message(Message.SHUT);        // pede a realização do serviço
             con.writeObject(outMessage);
             con.close();
            } catch (Exception e) {
        }
        
        
        
        //terminar Loja
        con = new ClientCom(serverHostNameLoja, serverPortNumbloja);
        

        try {
             con.open();
             outMessage = new Message(Message.SHUT);        // pede a realização do serviço
             con.writeObject(outMessage);
             con.close();
            } catch (Exception e) {
        }
        
        
        //terminar Oficina
        con = new ClientCom(serverHostNameOficina, serverPortNumbOficina);
        

        try {
             con.open();
             outMessage = new Message(Message.SHUT);        // pede a realização do serviço
             con.writeObject(outMessage);
             con.close();
            } catch (Exception e) {
        }
        
        
        //terminar repositorio Geral
        con = new ClientCom(serverHostNameGeral, serverPortNumbGeral);
        

        try {
             con.open();
             outMessage = new Message(Message.SHUT);        // pede a realização do serviço
             con.writeObject(outMessage);
             con.close();
            } catch (Exception e) {
        }
        
        
        

    }
    
}
