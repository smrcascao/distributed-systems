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
        FETCHING_PRIME_MATERIALS ,  /*estado de transição com bloqueio eventual
                                        o artesão bloqueia se não tiver matérias primas para produzir um novo
                                        produto e é acordado pela dona da empresa quando fornece mais
                                        matérias primas, replenishStock*/
        PRODUCING_A_NEW_PIECE,      /*estado independente
                                        na simulação, o artesão deve ser posto a dormir durante um intervalo
                                        de tempo aleatório*/    
        STORING_IT_FOR_TRANSFER,    /*estado de transição*/
        CONTACTING_THE_ENTREPRENEUR /*estado de transição*/
}
