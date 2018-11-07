/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import States.SCliente;

/**
 *
 * @author rofler
 */
public interface intClienteGeral {
      public void setEstadoCliente(int id, SCliente state);
         //public void livingNormalLife(int idCliente);
         //public boolean tryAgainLater();
         public boolean endOpCliente(int idCliente);

}
