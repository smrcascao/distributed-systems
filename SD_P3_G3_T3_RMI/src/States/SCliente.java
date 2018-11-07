package States;

import java.io.Serializable;

/**
 * Estados Possiveis para a entidade Activa Cliente.
 * @author rofler
 */
public class SCliente implements Serializable{
    /**
     * estado independente na simulação, o cliente deve ser posto a dormir durante um intervalo de tempo aleatório
   *  @serial OPENING_THE_SHOP
     */
    public static final int CARRYING_OUT_DAILY_CHORES=0;  
    /**
     * estado de transição
   *  @serial OPENING_THE_SHOP
     */
    public static final int CHECKING_DOOR_OPEN=1; 
    /**
     * estado de transição BUYING_SOME_GOODS – estado de bloqueio o cliente é acordado pela dona da empresa quando completa a transacção, sayGoodByeToCustomer
   *  @serial OPENING_THE_SHOP
     */
    public static final int APPRAISING_OFFER_IN_DISPLAY=2; 
    /**
     * estado de bloqueio, o cliente é acordado pela dona da empresa quando completa a transacção, sayGoodByeToCustomer
   *  @serial OPENING_THE_SHOP
    */
    public static final int BUYING_SOME_GOODS=3; 
    /**
     * estado
   *  @serial state
     */
    private final int state;
    
    public SCliente(){
        state = SCliente.APPRAISING_OFFER_IN_DISPLAY;
    }
}
