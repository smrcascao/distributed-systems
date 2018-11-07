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
public enum SArtesao {
        /**
        * estado de transição com bloqueio eventual
        * o artesão bloqueia se não tiver matérias primas para produzir um novo
        * produto e é acordado pela dona da empresa quando fornece mais
        * matérias primas, replenishStock
        */
        FETCHING_PRIME_MATERIALS ,  
        /**
         * estado independente
         * na simulação, o artesão deve ser posto a dormir durante um intervalo
         * de tempo aleatório    
         */ 
        PRODUCING_A_NEW_PIECE,
        /**
         * estado de transição
         */
        STORING_IT_FOR_TRANSFER,    
        /**
         * estado de transição
         */
        CONTACTING_THE_ENTREPRENEUR 
}
