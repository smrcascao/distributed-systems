package ServerInterface;

import Message.Message;
import Message.MessageException;
import Monitores.monArmazemPM;

/**
 * Este tipo de dados define o interface ao Armazem PM numa solução do Problema Obrigatorio 2 que implementa o modelo cliente-servidor de tipo 2 (replicação do servidor).
 *
 * @author rofler
 */
public class ArmazemPMInterface {
    
    /**
     * armazem PM (Regiao partilhada)
     * 
     * @serial armazemPM
     * 
     */
    private monArmazemPM armazemPM;
    
    /**
   *  quantidade de PM por fornecimento
   *
   *    @serial fornecimentoPM
   */
    private int fornecimentoPM;
    
    
    /**
     * construtor.
     * 
     * @param aPM repositorio de dados armazem PM
     */
    public ArmazemPMInterface(monArmazemPM aPM)
    {
        this.armazemPM=aPM;
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
       if((inMessage.getType() != Message.DONAVPM) && (inMessage.getType() != Message.SHUT))
               throw new MessageException("Tipo inválido!", inMessage);
       
       
       /* processamento das mensagens na regiao partilhada */
        switch (inMessage.getType()) {
            case Message.DONAVPM:
               fornecimentoPM = armazemPM.visitSuppliers();
               outMessage= new Message(Message.DONVOPM, fornecimentoPM);
               break;
            case Message.SHUT:
                armazemPM.shutdown();
                break;
        }
       
       return (outMessage);
       
   }
    
}
