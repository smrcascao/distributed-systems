package States;

import java.io.Serializable;

/**
 * Estados Possiveis para a entidade Activa Dona.
 * @author rofler
 */
public class SDona implements Serializable{
    /**
     * estado inicial / final (de transição)
   *  @serial OPENING_THE_SHOP
    */        
    public static final int OPENING_THE_SHOP=0;       
    /** estado de transição com espera eventual
     * a dona da empresa bloqueia se não houver pedidos de serviço e é acordada
     * por um cliente que pretende comprar um ou mais ou mais produtos,
     * iWantThis, ou que abandona a loja, exitShop, ou por um artesão que requer
     * o envio de mais matérias primas, primeMaterialsNeeded, ou que pretende a
     * transferência de produtos acabados da oficina para a loja, batchReadyForTransfer
   *  @serial WAITING_FOR_NEXT_TASK
     */
    public static final int WAITING_FOR_NEXT_TASK=1;   
    /** estado independente
     * na simulação, a dona da empresa deve ser posta a dormir durante um
     * intervalo de tempo aleatório
   *  @serial ATTENDIND_A_CUSTOMER
    */
    public static final int ATTENDIND_A_CUSTOMER=2;    
    /**
     * estado de transição
   *  @serial CLOSING_THE_SHOP
     */
    public static final int CLOSING_THE_SHOP=3;               
    /**
     * estado de transição
   *  @serial BUYING_PRIME_MATERIALS
    */        
    public static final int BUYING_PRIME_MATERIALS=4;        
    /**
     * estado de transição
   *  @serial DELIVERING_PRIME_MATERIALS
    */
    public static final int DELIVERING_PRIME_MATERIALS=5;        
    /**
     * estado de transição
   *  @serial COLLECTING_A_BATCH_OF_PRODUCTS
    */
    public static final int COLLECTING_A_BATCH_OF_PRODUCTS =6;  
    
    /**
     * estado
   *  @serial state
     */
    private final int state;
    
    public SDona(){
        state = SDona.ATTENDIND_A_CUSTOMER;
    }
    
}
