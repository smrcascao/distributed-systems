package ServerInterface;

import Message.Message;
import Message.MessageException;
import Monitores.monOficina;

/**
 *Este tipo de dados define o interface à Oficina numa solução do Problema Obrigatorio 2 que implementa o modelo cliente-servidor de tipo 2 (replicação do servidor).
 * @author rofler
 */
public class OficinaInterface {
    
    /**
     * Loja (Regiao partilhada)
     * 
     * @serial oficina
     * 
     */
    private monOficina oficina;
    
      /**
     * variavel usada para escolher mensagem de resposta.
     * 
     * @serial temp 
     * 
     */
    int temp;
    
    
        /**
     * construtor.
     * 
     * @param of repositorio de dados Oficina
     */
    public OficinaInterface(monOficina of)
    {
        this.oficina=of;
    }
 
        public Message processAndReply (Message inMessage) throws MessageException
   {
       Message outMessage = null;                           // mensagem de resposta

       /* validação da mensagem recebida */
       /*cliente*/
       switch (inMessage.getType()) {
           case Message.checkForMaterials:
               if ((inMessage.getId()< 0) || (inMessage.getId()>2)) {
                   throw new MessageException("Id do cliente inválido!", inMessage);
               }
               break;
           case Message.goToStore:
               if ((inMessage.getId()< 0) || (inMessage.getId()>2)) {
                   throw new MessageException("Id do cliente inválido!", inMessage);
               }
               break;
           case Message.batchReadyForTransfer:
               if ((inMessage.getId() < 0) || (inMessage.getId()>2)) {
                   throw new MessageException("Id do cliente inválido!", inMessage);
               }
               break;   
            case Message.primeMaterialsNeeded:
               if ((inMessage.getId() < 0) || (inMessage.getId()>2)) {
                   throw new MessageException("Id do cliente inválido!", inMessage);
               }
               break;
            case Message.backToWork:
               if ((inMessage.getId() < 0) || (inMessage.getId()>2)) {
                   throw new MessageException("Id do cliente inválido!", inMessage);
               }
               break;
            case Message.collectMaterials:
               if ((inMessage.getId() < 0) || (inMessage.getId()>2)) {
                   throw new MessageException("Id do cliente inválido!", inMessage);
               }
               break;
            case Message.prepareToProduce:
               if ((inMessage.getId() < 0) || (inMessage.getId()>2)) {
                   throw new MessageException("Id do cliente inválido!", inMessage);
               }
               break;
            /*Dona*/
            case Message.SHUT:
            case Message.GTWKSHP:
            case Message.REPSTCK:  
                break;
            default:
               throw new MessageException("Tipo inválido!", inMessage);
       }
       
        /* processamento das mensagens na regiao partilhada */
       //Artesao
        switch (inMessage.getType()) {
            case Message.checkForMaterials:
               
                int temp = oficina.checkForMaterials(inMessage.getId());

                switch (temp) {

                    case 1:
                        outMessage = new Message(Message.PMOK);
                        break;

                    case 3:
                        outMessage = new Message(Message.maxStockProdutos);
                        break;
                    case 4:
                        outMessage = new Message(Message.endOpArtesao);
                        break;

                    case 0:
                        outMessage = new Message(Message.PMInsuficiente);
                        break;
                    case 2:
                        outMessage = new Message(Message.PMInfAAlarme);
                        break;

                }
                
                break;
                 
                       
               
            case Message.goToStore:
                
                
                temp=oficina.goToStore(inMessage.getId());
                
                
                switch(temp){
                case 1: 
                        outMessage = new Message(Message.ACK);
                        break;
                case 2: 
                        outMessage = new Message(Message.alarmeProdutosOficina);
                        break;
                case 0: 
                        outMessage = new Message(Message.maxStockProdutos);
                        break;
                default:
                        outMessage = new Message(Message.CONT);
                        break;
                }
                
                break;

            case Message.batchReadyForTransfer:
               oficina.batchReadyForTransfer(inMessage.getId());
               outMessage = new Message(Message.ACK);
               break; 

            case Message.primeMaterialsNeeded:
                oficina.primeMaterialsNeeded(inMessage.getId());
                outMessage= new Message(Message.ACK);
                break;
            case Message.backToWork:
                oficina.backToWork(inMessage.getId());
                outMessage = new Message(Message.ACK);                
               break;

            case Message.collectMaterials:
                oficina.collectMaterials(inMessage.getId());
                outMessage = new Message(Message.ACK);
               break;
                
            case Message.prepareToProduce:
               oficina.prepareToProduce(inMessage.getId());
               outMessage = new Message(Message.ACK);
               break;
             
            /*Dona*/    
                
            case Message.SHUT:
                oficina.shutdown();
                break;
                
            case Message.GTWKSHP:
               oficina.goToWorkShop();
               outMessage = new Message(Message.ACK);
               break;
                
            case Message.REPSTCK:
               oficina.replenishStock(inMessage.getId());
               outMessage = new Message(Message.ACK);
               break;
            
        }
       
       return (outMessage);
       
   }
       
       
  
       
   
}
