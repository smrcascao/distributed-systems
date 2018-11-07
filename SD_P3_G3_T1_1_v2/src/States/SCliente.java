/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package States;

/**
 *
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
