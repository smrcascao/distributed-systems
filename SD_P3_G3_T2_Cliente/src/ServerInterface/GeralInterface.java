/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerInterface;

import Message.Message;
import Message.MessageException;
import Monitores.monGeral;

/**
 *
 *   Este tipo de dados define o interface ao Repositorio Geral numa solução do Problema Obrigatorio 2 que implementa
 *   o modelo cliente-servidor de tipo 2 (replicação do servidor).
 * 
 * @author rofler
 */
public class GeralInterface {
    /**
     * Loja (Regiao partilhada)
     * 
     * @serial geral
     * 
     */
    private monGeral geral;
    
    
    /**
   *  integer usado para receber valores temporarios.
   *
   *    @serial temp
   */
    private int temp=0;
    
    
    /**
     * construtor.
     * 
     * @param ger repositorio de dados repositorio geral
     */
    public GeralInterface(monGeral ger)
    {
        this.geral=ger;
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
            /*artesao*/
            case Message.START:
                if ((inMessage.getId()< 0) || (inMessage.getId()>2)) {
                    throw new MessageException("Id do artesao inválido!", inMessage);
                }
                break;
            case Message.isDonaChamadaProdutos:
            case Message.isDonaChamadaPM:
                break;
            case Message.ISARMPM:
            case Message.ENDOPA:
                break;
            /*cliente*/
            case Message.STCLI:
                if ((inMessage.getId()< 0) || (inMessage.getId()>2)) {
                    throw new MessageException("Id do cliente inválido!", inMessage);
                }
                break;
            case Message.ENDOPCL:
                if ((inMessage.getId()< 0) || (inMessage.getId()>2)) {
                    throw new MessageException("Id do cliente inválido!", inMessage);
                }
                break;
            /*dona*/
            case Message.SHUT:
            case Message.STDON:
            case Message.ENDOPD:
            /*armazem PM*/
            case Message.SETARMPM:
                break;
            /*loja*/
            case Message.CLIIN:
            case Message.CLIOUT:
                break;
            case Message.CLIBUY:
                if ((inMessage.getId()< 0) || (inMessage.getId()>2)) {
                    throw new MessageException("Id do cliente inválido!", inMessage);
                }
                break;
            case Message.PRODINST:
                break;
            case Message.SETDOORSTAT:
                if ((!inMessage.getTempS().equals("open")) && (!inMessage.getTempS().equals("opdc")) && (!inMessage.getTempS().equals("clos"))) {
                    throw new MessageException("o estado da porta pedido nao e permitido", inMessage);
                }
                break;
            case Message.GETPRODD:
                break;
            /*Oficina*/
            case Message.SETPMW:
            case Message.PMGAST:
            case Message.SETDCPM:
            case Message.SETDCPR:
            case Message.SETPRW:
            case Message.DONRPR:
            case Message.NEWPMF:
                break;
            case Message.ARTFP:
                if ((inMessage.getId()< 0) || (inMessage.getId()>2)) {
                    throw new MessageException("Id do artesao inválido!", inMessage);
                }
                break;
                
           default:
               throw new MessageException("Tipo inválido!", inMessage);
       }
       
       /* processamento das mensagens na regiao partilhada */
        switch (inMessage.getType()) {
            /*artesao*/
            case Message.START:
                geral.setEstadoArtesao(inMessage.getId(), inMessage.getEstadoArtesao());
                outMessage = new Message(Message.ACK);
                break;
            case Message.isDonaChamadaProdutos:
                if(geral.isDonaChamadaProdutos())
                    outMessage = new Message(Message.donaChamadaProdutos);
                else
                    outMessage = new Message(Message.donaNaoChamadaProdutos);
                break;
            case Message.isDonaChamadaPM:
                if(geral.isDonaChamadaPM())
                    outMessage = new Message(Message.donaJaFoiAlertada);
                else
                    outMessage = new Message(Message.donaNaoFoiAlertada);
                break;
            case Message.ISARMPM:
                if(geral.isArmPM())
                    outMessage = new Message(Message.ISARMPMR, true);
                else
                    outMessage = new Message(Message.ISARMPMR, false);
                break;
            case Message.ENDOPA:
                if(geral.endOpArtesao())
                    outMessage = new Message(Message.END);
                else
                    outMessage = new Message(Message.CONT);
                break;
            /*cliente*/
            case Message.STCLI:
                geral.setEstadoCliente(inMessage.getId(), inMessage.getEstadoCliente());
                outMessage = new Message(Message.ACK);
                break;
            case Message.ENDOPCL:
                if(geral.endOpCliente())
                    outMessage = new Message(Message.END);
                else
                    outMessage = new Message(Message.CONT);
                break;
            /*dona*/
            case Message.SHUT:
                geral.shutdown();
                break;
            case Message.STDON:
                geral.setEstadoDona(inMessage.getEstadoDona());
                outMessage = new Message(Message.ACK);
                break;
            case Message.ENDOPD:
                if(geral.endOpDona())
                    outMessage = new Message(Message.END);
                else
                    outMessage = new Message(Message.CONT);
                break;
            /*armazem PM*/
            case Message.SETARMPM:
                geral.setArmPM(inMessage.isAnswer());
                outMessage = new Message(Message.ACK);
                break;
                
            /*Loja*/
            case Message.CLIIN:
                geral.clienteEntra();
                outMessage = new Message(Message.ACK);
                break;
            case Message.CLIOUT:
                geral.clienteSai();
                outMessage = new Message(Message.ACK);
                break;
            case Message.CLIBUY:
                geral.compraCliente(inMessage.getId(), inMessage.getNTry());
                outMessage = new Message(Message.ACK);
                break;
            case Message.PRODINST:
                geral.setnProdutosNaLoja(inMessage.getId());
                outMessage = new Message(Message.ACK);
                break;
            case Message.SETDOORSTAT:
                geral.setShopDoorStat(inMessage.getTempS());
                outMessage = new Message(Message.ACK);
                break;
            case Message.GETPRODD:
                temp = geral.getProdutosComDona();
                outMessage = new Message(Message.GETPRODDR, temp);
                break;
            
            /*Oficina*/
            case Message.SETPMW:
                geral.setnPMNaOficina(inMessage.getId());
                outMessage = new Message(Message.ACK);
                break;
            case Message.PMGAST:
                geral.pMGasta(inMessage.getId());
                outMessage = new Message(Message.ACK);
                break;
            case Message.SETDCPM:
                geral.setDonaChamadaPM(inMessage.isAnswer());
                outMessage = new Message(Message.ACK);
                break;
            case Message.SETDCPR:
                geral.setDonaChamadaProdutos(inMessage.isAnswer());
                outMessage = new Message(Message.ACK);
                break;
            case Message.SETPRW:
                geral.setnProdutosNaOficina(inMessage.getId());
                outMessage = new Message(Message.ACK);
                break;
            case Message.DONRPR:
                geral.donaRecebeProdutos(inMessage.getId());
                outMessage = new Message(Message.ACK);
                break;
            case Message.NEWPMF:
                geral.novoFornecimentoPM(inMessage.getId());
                outMessage = new Message(Message.ACK);
                break;
            case Message.ARTFP:
                geral.artesaoFezProduto(inMessage.getId());
                outMessage = new Message(Message.ACK);
                break;
                
        }
       
       return (outMessage);
       
   }
}
