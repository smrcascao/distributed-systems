package States;

import java.io.Serializable;

/**
 * Estados Possiveis para a entidade Activa Artesao.
 *
 * @author rofler
 */
public class SArtesao implements Serializable {

    /**
     * estado de transição com bloqueio eventual o artesão bloqueia se não tiver
     * matérias primas para produzir um novo produto e é acordado pela dona da
     * empresa quando fornece mais matérias primas, replenishStock
     *
     * @serial FETCHING_PRIME_MATERIALS
     */
    public static final int FETCHING_PRIME_MATERIALS = 0;
    /**
     * estado independente na simulação, o artesão deve ser posto a dormir
     * durante um intervalo de tempo aleatório
     *
     * @serial PRODUCING_A_NEW_PIECE
     */
    public static final int PRODUCING_A_NEW_PIECE = 1;
    /**
     * estado de transição
     *
     * @serial STORING_IT_FOR_TRANSFER
     */
    public static final int STORING_IT_FOR_TRANSFER = 2;
    /**
     * estado de transição
     *
     * @serial CONTACTING_THE_ENTREPRENEUR
     */
    public static final int CONTACTING_THE_ENTREPRENEUR = 3;

    /**
     * estado
   *  @serial state
     */
    private final int state;
    
    public SArtesao(){
        state = SArtesao.CONTACTING_THE_ENTREPRENEUR;
    }

}
