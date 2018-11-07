package ServerInterface;

import Message.Message;
import Message.MessageException;
import Monitores.monLoja;

/**
 *
 *   Este tipo de dados define o interface à Loja numa solução do Problema Obrigatorio 2 que implementa
 *   o modelo cliente-servidor de tipo 2 (replicação do servidor).
 * 
 * @author rofler
 */
public class LojaInterface {
    
    /**
     * Loja (Regiao partilhada)
     * 
     * @serial loja
     * 
     */
    private monLoja loja;
    
    /**
   *  integer usado para receber valores temporarios.
   *
   *    @serial temp
   */
    private int temp=0;
    
    /**
   *  boolean usado para receber valores temporarios.
   *
   *    @serial tempb
   */
    private boolean tempb=false;
    
    /**
     * construtor.
     * 
     * @param loj repositorio de dados Loja
     */
    public LojaInterface(monLoja loj)
    {
        this.loja=loj;
    }
    
    /**
   *  Processamento das mensagens através da execução da tarefa correspondente.
   *  Geração de uma mensagem de resposta.
   *
   *    @param inMessage mensagem com o pedido
   *
   *    @return mensagem de resposta
   *
   *    @throws MessageException se a mensagem com o pedido for considerada inválida
   */
    public Message processAndReply (Message inMessage) throws MessageException
   {
       Message outMessage = null;                           // mensagem de resposta

       /* validação da mensagem recebida */
       /*cliente*/
       switch (inMessage.getType()) {
           case Message.REQENTS:
               if ((inMessage.getId()< 0) || (inMessage.getId()>2)) {
                   throw new MessageException("Id do cliente inválido!", inMessage);
               }
               break;
           case Message.ISDOORO:
               if ((inMessage.getId()< 0) || (inMessage.getId()>2)) {
                   throw new MessageException("Id do cliente inválido!", inMessage);
               }
               break;
           case Message.GOSHOP:
               if ((inMessage.getId() < 0) || (inMessage.getId()>2)) {
                   throw new MessageException("Id do cliente inválido!", inMessage);
               }
               break;   
            case Message.GOTOSLP:
               if ((inMessage.getId() < 0) || (inMessage.getId()>2)) {
                   throw new MessageException("Id do cliente inválido!", inMessage);
               }
               break;
            case Message.PERUSE:
               if ((inMessage.getId() < 0) || (inMessage.getId()>2)) {
                   throw new MessageException("Id do cliente inválido!", inMessage);
               }
               break;
            case Message.IWANT:
               if ((inMessage.getId() < 0) || (inMessage.getId()>2)) {
                   throw new MessageException("Id do cliente inválido!", inMessage);
               }
               break;
            case Message.REQEXTS:
               if ((inMessage.getId() < 0) || (inMessage.getId()>2)) {
                   throw new MessageException("Id do cliente inválido!", inMessage);
               }
               break;
            case Message.TRYLAT:
               if ((inMessage.getId() < 0) || (inMessage.getId()>2)) {
                   throw new MessageException("Id do cliente inválido!", inMessage);
               }
               break;
           
            //DONA
            case Message.SHUT:
            case Message.PREPTW:
            case Message.APRSIT:
            case Message.ADDCUST:
            case Message.SAYGBCUST:
            case Message.PREPTL:
            case Message.CLOSSHDOOR:
            case Message.CUSTINSH:
            case Message.RETSHP:
               break;
            
                
           default:
               throw new MessageException("Tipo inválido!", inMessage);
       }
       
       /* processamento das mensagens na regiao partilhada */
       //CLIENTE
        switch (inMessage.getType()) {
            case Message.REQENTS:
               loja.enterShop(inMessage.getId());
                outMessage = new Message(Message.ACK);
               break;
            case Message.ISDOORO:
               if(loja.isDoorOpen())
                   outMessage = new Message(Message.SHOPOP);
               else
                   outMessage = new Message(Message.SHOPCL);
               break;
            case Message.GOSHOP:
               loja.goShopping(inMessage.getId());
                outMessage = new Message(Message.ACK);
               break;   
            case Message.PERUSE:
               if(loja.perusingAround())
                   outMessage = new Message(Message.WILLBUY);
               else
                   outMessage = new Message(Message.WONTBUY);
               break;
            case Message.IWANT:
               loja.iWantThis(inMessage.getId());
                outMessage = new Message(Message.ACK);                
               break;
            case Message.REQEXTS:
                loja.exitShop(inMessage.getId());
                outMessage = new Message(Message.ACK);
               break;
            case Message.TRYLAT:
               loja.tryAgainLater(inMessage.getId());
               outMessage = new Message(Message.ACK);
               break;
                
            //DONA
            case Message.SHUT:
                loja.shutdown();
                break;
            case Message.PREPTW:
               loja.prepareToWork();
               outMessage = new Message(Message.ACK);
               break;
            case Message.APRSIT:
               temp=loja.appraiseSit();
               outMessage = new Message(Message.appraiseReply, temp);
               break;
            case Message.ADDCUST:
               temp=loja.addressACustomer();
               outMessage = new Message(Message.ACK, temp);
               break;
            case Message.SAYGBCUST:
               loja.sayGoodbyeToCustomer(inMessage.getId());
               outMessage = new Message(Message.ACK);
               break;
            case Message.PREPTL:
               loja.prepareToLeave();
               outMessage = new Message(Message.ACK);
               break;
            case Message.CLOSSHDOOR:
               loja.closeTheDoor();
               outMessage = new Message(Message.ACK);
               break;
            case Message.CUSTINSH:
               tempb=loja.customersInTheShop();
               outMessage = new Message(Message.CUSTINSHR, tempb);
               break;
            case Message.RETSHP:
               loja.returnToShop();
               outMessage = new Message(Message.ACK);
               break;
        }
       
       return (outMessage);
       
   }
    
}
