package States;

/**
 * Estados Possiveis para a entidade Activa Dona.
 * @author rofler
 */
public enum SDona {
    /**
     * estado inicial / final (de transição)
    */        
    OPENING_THE_SHOP,       
    /** estado de transição com espera eventual
     * a dona da empresa bloqueia se não houver pedidos de serviço e é acordada
     * por um cliente que pretende comprar um ou mais ou mais produtos,
     * iWantThis, ou que abandona a loja, exitShop, ou por um artesão que requer
     * o envio de mais matérias primas, primeMaterialsNeeded, ou que pretende a
     * transferência de produtos acabados da oficina para a loja, batchReadyForTransfer
     */
    WAITING_FOR_NEXT_TASK,  
    /** estado independente
     * na simulação, a dona da empresa deve ser posta a dormir durante um
     * intervalo de tempo aleatório
    */
    ATTENDIND_A_CUSTOMER,   
    /**
     * estado de transição
     */
    CLOSING_THE_SHOP,               
    /**
     * estado de transição
    */        
    BUYING_PRIME_MATERIALS,         
    /**
     * estado de transição
    */
    DELIVERING_PRIME_MATERIALS,         
    /**
     * estado de transição
    */
    COLLECTING_A_BATCH_OF_PRODUCTS  
}
