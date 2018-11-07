package Message;

/**
 *
 *   Este tipo de dados define uma excepção que é lançada se a mensagem for inválida.
 * 
 *   @author rofler
 */

public class MessageException extends Exception
{
  /**
   *  Mensagem que originou a excepção
   *    @serial msg
   */

   private Message msg;

  /**
   *  Instanciação de uma mensagem.
   *
   *    @param errorMessage texto sinalizando a condição de erro
   *    @param msg mensagem que está na origem da excepção
   */

   public MessageException (String errorMessage, Message msg)
   {
     super (errorMessage);
     this.msg = msg;
   }

  /**
   *  Obtenção da mensagem que originou a excepção.
   *
   *    @return mensagem
   */

   public Message getMessageVal ()
   {
     return (msg);
   }
}