package States;

/**
 * Estados Possiveis para a entidade Activa Cliente.
 * @author rofler
 */
public enum SCliente {
    /**
     * estado independente na simulação, o cliente deve ser posto a dormir durante um intervalo de tempo aleatório
     */
    CARRYING_OUT_DAILY_CHORES, 
    /**
     * estado de transição
     */
    CHECKING_DOOR_OPEN,
    /**
     * estado de transição BUYING_SOME_GOODS – estado de bloqueio o cliente é acordado pela dona da empresa quando completa a transacção, sayGoodByeToCustomer
     */
    APPRAISING_OFFER_IN_DISPLAY,
    /**
     * estado de bloqueio, o cliente é acordado pela dona da empresa quando completa a transacção, sayGoodByeToCustomer
    */
    BUYING_SOME_GOODS 
    
}
